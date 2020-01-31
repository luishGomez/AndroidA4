package com.example.reto1_android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteAndroid;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.model.MateriasBeans;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.MateriaAPIClient;
import com.example.reto1_android.retrofit.MateriaManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Permite al cliente crear un apunte.
 * @author Ricardo Peinado Lastra
 */
public class CrearApunte extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(CrearApunte.class.getName());

    private EditText titulo;
    private EditText desc;
    private EditText precio;
    private Button crear;
    private Button leerFichero;
    private Spinner spinnerMaterias;
    private TextView textPath;
    private String[] materias = new String[0];
    private boolean ficheroObtenido = false;
    private final int REQUEST_CODE = 43;
    private static final int READ_REQUEST_CODE = 42;
    private MateriasBeans listaDeMaterias;
    private ApunteAndroid nuevoApunte=new ApunteAndroid();
    private boolean hayFichero=false;
    private ClienteBean cliente;
    private String cadena64;
    private MediaPlayer mp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_apunte);

        Intent intent = getIntent();
        cliente = (ClienteBean) intent.getSerializableExtra("cliente");
        titulo = (EditText) findViewById(R.id.ca_titulo);
        desc = (EditText) findViewById(R.id.ca_desc);
        desc.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        precio = (EditText) findViewById(R.id.ca_precio);
        crear = (Button) findViewById(R.id.ca_crear);
        leerFichero = (Button) findViewById(R.id.ca_subirFichero);
        spinnerMaterias = (Spinner) findViewById(R.id.ca_spinner);
        textPath = (TextView) findViewById(R.id.ca_path);
        mp= MediaPlayer.create(this,R.raw.pulsar_boton);

        rellenarMaterias();
        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materias);
        spinnerMaterias.setAdapter(adapterMaterias);

        crear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Boolean estaBien = true;
                if (desc.getText().toString().trim().length() < 3 || desc.getText().toString().trim().length() > 250) {
                    estaBien = false;
                    desc.setTextColor(Color.RED);
                    Toast.makeText(CrearApunte.this, getResources().getString(R.string.vaea1), Toast.LENGTH_LONG).show();
                } else
                    desc.setTextColor(Color.BLACK);
                if (titulo.getText().toString().trim().length() < 3 || titulo.getText().toString().trim().length() > 250) {
                    estaBien = false;
                    titulo.setTextColor(Color.RED);
                    Toast.makeText(CrearApunte.this, getResources().getString(R.string.vaea2), Toast.LENGTH_LONG).show();
                } else
                    titulo.setTextColor(Color.BLACK);
                if(!hayFichero){
                    textPath.setText(getResources().getString(R.string.ca1));//ca1
                    textPath.setTextColor(Color.RED);
                }else{
                    textPath.setTextColor(Color.BLACK);
                }
                if (precioValido() && estaBien) {
                    desc.setTextColor(Color.BLACK);
                    titulo.setTextColor(Color.BLACK);
                    crearElApunte();

                }
            }
        });
        leerFichero.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

    }

    /**
     * Empieza el proceso de creación de un apunte.
     */
    private void crearElApunte() {
        nuevoApunte.setPrecio(Float.parseFloat(precio.getText().toString()));
        nuevoApunte.setMateria(buscaLaMateria(spinnerMaterias.getSelectedItemPosition()));
        nuevoApunte.setDescripcion(desc.getText().toString());
        nuevoApunte.setTitulo(titulo.getText().toString());
        nuevoApunte.setCreador(cliente);
        nuevoApunte.setIdApunte(0);
        try {
            ApunteManager apunteManager = ApunteAPIClient.getClient();
            Call<Void> call = apunteManager.createApunteAndroid(nuevoApunte);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Intent resultItem = new Intent();
                        resultItem.putExtra("result", getResources().getString(R.string.ca2)+" "+nuevoApunte.getTitulo()+" "+getResources().getString(R.string.ca3));//ca2 //ca3
                        setResult(RESULT_OK, resultItem);
                        finish();
                    }else {
                        LOGGER.severe("ERROR  al crear el apunte."+response.message());
                        informar(getResources().getString(R.string.gda1));
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    LOGGER.severe("FALLO al crear un apunte "+t.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            });
        }catch(Exception e){
            LOGGER.severe("Error al crear el apunte: "+e.getMessage());
            informar(getResources().getString(R.string.gda1));
        }

    }

    /**
     * Busca la materia seleccionada para darsela al apunte como objeto.
     * @param selectedItem La materia seleccionada.
     * @return La mataria como objeto.
     */
    private MateriaBean buscaLaMateria(int selectedItem) {
        MateriaBean laMateria = null;
        for (MateriaBean materia : listaDeMaterias.getMaterias()) {
            if (materia.getTitulo().equals(materias[selectedItem])) {
                laMateria = materia;
                break;
            }
        }
        return laMateria;
    }

    /**
     * Valida si el precio es valido o no.
     * @return True si es un precio | False en los demas casos
     */
    private boolean precioValido() {
        boolean resultado = true;
        precio.setTextColor(Color.BLACK);
        try {
            String elPrecio = precio.getText().toString().trim();
            elPrecio = elPrecio.replace(",", ".");
            float precioFinal = Float.parseFloat(elPrecio);
            if (elPrecio.indexOf(".") != -1) {
                if ((elPrecio.length() - elPrecio.indexOf(".")) > 3) {
                    precio.setTextColor(Color.RED);
                    resultado = false;
                }
            }
            if (100000 - precioFinal < 0) {
                precio.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.ca4), Toast.LENGTH_LONG).show();//ca4
                resultado = false;
            } else if (precioFinal - (0.3) < 0) {
                precio.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.ca5), Toast.LENGTH_LONG).show();//ca5
                resultado = false;
            }

        } catch (Exception e) {
            resultado = false;
            precio.setTextColor(Color.RED);
        }
        return resultado;
    }

    /**
     * Pide las materias.
     */
    private void rellenarMaterias() {
        try {

            MateriaManager materiaManager = MateriaAPIClient.getClient();
            Call<MateriasBeans> call = materiaManager.findAll();

            call.enqueue(new Callback<MateriasBeans>() {
                @Override
                public void onResponse(Call<MateriasBeans> call, Response<MateriasBeans> response) {
                    if (response.isSuccessful()) {
                        insertarMaterias(response.body());
                    }else{
                        LOGGER.severe("Error al pedir las materias.");
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<MateriasBeans> call, Throwable t) {
                    LOGGER.severe("Fallo al pedior las materias" + t.getMessage() + " " + t.getCause());
                    informar(getResources().getString(R.string.gda2));
                }
            });


        } catch (Exception e) {
            LOGGER.severe("Error al pedir las materias " + e.getMessage() + "   " + e.getCause());
            informar(getResources().getString(R.string.gda1));
        }
    }

    /**
     * Inserta las materias en el spinner de materias.
     * @param body La lista de materias.
     */
    private void insertarMaterias(MateriasBeans body) {
        listaDeMaterias = body;
        materias = new String[body.getMaterias().size()];
        int indice = 0;
        for (MateriaBean materia : body.getMaterias()) {
            materias[indice] = materia.getTitulo();
            indice++;

        }
        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materias);
        spinnerMaterias.setAdapter(adapterMaterias);

    }

    /**
     * El resultado de seleccionar algun archivo.
     * @param requestCode El codigo de petición.
     * @param resultCode El codigo de resultado.
     * @param data El contenido que devuelva.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                textPath.setText(uri.toString());
                ficheroObtenido = true;
                InputStream inputStream=null;
                try{


                    inputStream=getContentResolver().openInputStream(uri);
                    byte[] bFile = new byte[inputStream.available()];
                    inputStream.read(bFile);
                    nuevoApunte.setArchivo(Base64.encodeToString(bFile,Base64.DEFAULT));
                    hayFichero=true;
                    textPath.setTextColor(Color.BLACK);
                }catch(Exception e){
                    LOGGER.severe("Error al leer los bytes "+e.getMessage());
                    informar(getResources().getString(R.string.gda1));

                } finally {

                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            LOGGER.severe("Error al cerrar el stream");
                            informar(getResources().getString(R.string.gda1));
                        }
                    }
                }

            }else {
                hayFichero = false;
                textPath.setText("");
            }
        }
    }

    /**
     * Valida si el precio es valido o no.
     * @param precio El precio.
     * @return True si es valido | False en los demas casos.
     */
    private boolean precioValido(String precio) {
        Boolean result = true;
        try {
            float numero = Float.parseFloat(precio);
            int dondePunto = precio.indexOf('.');
            if (dondePunto > 0 && precio.length() > dondePunto + 3)
                result = false;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
    /**
     * Convierte una lista de bytes a Hexadecimal.
     * @param resumen La colección de bytes.
     * @return Los bytes en hexadecimal.
     */
    static String hexadecimal(byte[] resumen) {
        String HEX = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1)
                HEX += "0";
            HEX += h;
        }
        return HEX.toUpperCase();
    }
    /**
     * Informa al usuario de algun error o fallo.
     *
     * @param frase La frase que le muestra al usuario.
     */
    public void informar(String frase) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(frase);
        alertDialog.show();
    }


}
