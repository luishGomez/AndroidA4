package com.example.reto1_android.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.model.MateriasBeans;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.MateriaAPIClient;
import com.example.reto1_android.retrofit.MateriaManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Visualiza el editor de apunte para un cliente.
 * @author Ricardo Peinado Lastra
 */
public class VisorApunteEditable extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(VisorApunteEditable.class.getName());
    private int STORAGE_PERMISSION_CODE = 1;

    private EditText editTextTitulo;
    private EditText editTextDesc;
    private Spinner materiaSpinner;
    private EditText editTextPrecio;
    private Button btnActualizar;
    private Button btnBorrar;
    private Button btnDescargar;
    private ClienteBean cliente;
    private ApunteBean apunte;
    private String[] materias = new String[0];
    private MateriasBeans listaDeMaterias;
    private String fileName;
    private MediaPlayer mp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_apunte_editable);

        editTextDesc = (EditText) findViewById(R.id.vae_desc);
        editTextDesc.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editTextPrecio = (EditText) findViewById(R.id.vae_precio);
        editTextTitulo = (EditText) findViewById(R.id.vae_titulo);
        materiaSpinner = (Spinner) findViewById(R.id.vae_spinner);
        btnActualizar = (Button) findViewById(R.id.vae_actualizar);
        btnBorrar = (Button) findViewById(R.id.vae_borrar);
        btnDescargar = (Button) findViewById(R.id.vae_descargar);
        mp= MediaPlayer.create(this,R.raw.pulsar_boton);
        btnDescargar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                if (ContextCompat.checkSelfPermission(VisorApunteEditable.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(VisorApunteEditable.this, "You have already granted this permission!",
                    //        Toast.LENGTH_SHORT).show();
                    descargarApunte();
                } else {
                    requestStoragePermission();
                }
            }
        });

        Intent intent = getIntent();
        cliente = (ClienteBean) intent.getSerializableExtra("cliente");
        apunte = (ApunteBean) intent.getSerializableExtra("apunte");

        editTextDesc.setText(apunte.getDescripcion());
        editTextPrecio.setText(apunte.getPrecio() + "");
        editTextTitulo.setText(apunte.getTitulo());

        rellenarMaterias();
        int i = 0;
        for (String materia : materias) {
            if (apunte.getMateria().getTitulo().equals(materias[i])) {
                materiaSpinner.setSelection(i);
                break;
            }
            i++;
        }
        btnActualizar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Boolean estaBien = true;
                if (editTextDesc.getText().toString().trim().length() < 3 || editTextDesc.getText().toString().trim().length() > 250) {
                    estaBien = false;
                    editTextDesc.setTextColor(Color.RED);
                    Toast.makeText(VisorApunteEditable.this, getResources().getString(R.string.vaea1), Toast.LENGTH_LONG).show();
                } else
                    editTextDesc.setTextColor(Color.BLACK);
                if (editTextTitulo.getText().toString().trim().length() < 3 || editTextTitulo.getText().toString().trim().length() > 250) {
                    estaBien = false;
                    editTextTitulo.setTextColor(Color.RED);
                    Toast.makeText(VisorApunteEditable.this, getResources().getString(R.string.vaea2), Toast.LENGTH_LONG).show();
                } else
                    editTextTitulo.setTextColor(Color.BLACK);
                if (precioValido() && estaBien) {
                    editTextDesc.setTextColor(Color.BLACK);
                    editTextTitulo.setTextColor(Color.BLACK);
                    actualizarElApunte();
                    Intent resultItem = new Intent();
                    resultItem.putExtra("result", getResources().getString(R.string.ca2)+" " + apunte.getTitulo() + getResources().getString(R.string.vad1));//vad1
                    setResult(RESULT_OK, resultItem);
                    finish();
                }

            }
        });
        btnBorrar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                intentarBorrarElApunte();
            }
        });


    }

    /**
     * Hace el proceso de borrar un apunte.
     */
    private void intentarBorrarElApunte() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.vaea3))
                .setPositiveButton(getResources().getString(R.string.vaea4), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            ApunteManager apunteManager = ApunteAPIClient.getClientText();
                            Call<Integer> call = apunteManager.cuantasCompras(apunte.getIdApunte());
                            call.enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if (response.isSuccessful()) {
                                        borrarElApunte(response.body());
                                    } else {
                                        LOGGER.severe("Error al coger cuantas compras tiene el apunte");
                                        informar(getResources().getString(R.string.gda1));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    LOGGER.severe("FALLO coger cuantas compras" + t.getMessage());
                                    informar(getResources().getString(R.string.gda2));
                                }
                            });
                        } catch (Exception e) {
                            LOGGER.severe("Error al intentar coger cuantas compras tiene el apunte a borrar");
                            informar(getResources().getString(R.string.gda1));
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.vaea5), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog alertaParaBorrar = builder.create();
        alertaParaBorrar.show();

    }

    /**
     * Borra el apunte si no tiene ninguna compra.
     * @param body El numero de compras.
     */
    private void borrarElApunte(Integer body) {
        if (body == 0) {
            try {
                ApunteManager apunteManager = ApunteAPIClient.getClient();
                Call<Void> call = apunteManager.remove(apunte.getIdApunte());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            cerrar();
                        } else {
                            LOGGER.severe("Error al borrar el apunte");
                            informar(getResources().getString(R.string.gda1));
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        LOGGER.severe("FALLO al intentar borrar el apunte");
                        informar(getResources().getString(R.string.gda2));
                    }
                });
            } catch (Exception e) {
                LOGGER.severe("Error al intentar borrar el apunte");
                informar(getResources().getString(R.string.gda1));
            }

        } else {
            Toast.makeText(this, getResources().getString(R.string.vad3), Toast.LENGTH_LONG).show();//vad3
        }
    }

    /**
     * Cierra el activity
     */
    private void cerrar() {
        Intent resultItem = new Intent();
        resultItem.putExtra("result", getResources().getString(R.string.vad2)+": " + editTextTitulo.getText().toString());//vad2
        setResult(RESULT_OK, resultItem);
        finish();
    }
    /**
     * Convierte un texto en hexadeciaml en una lista de bytes.
     * @param s El texto en hexadecimal.
     * @return La coleccion en bytes.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Descarga el apunte.
     */
    private void descargarApunte() {
        ApunteManager apunteManager = ApunteAPIClient.getClientText();
        Call<String> call = apunteManager.getArchivoDelApunte(apunte.getIdApunte());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String pathDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "";
                    Date now = new Date();
                    pathDescargas = pathDescargas + "/" + apunte.getTitulo() + "D" + now.getDay() + "M" + now.getMonth() + "Y" + now.getYear() + "M" + now.getMinutes() + "S" + now.getSeconds() + ".pdf";
                    FileOutputStream fileOuputStream = null;
                    File file= new File(pathDescargas);
                    try {
                        fileOuputStream = new FileOutputStream(file.getPath());
                        //fileOuputStream.write(Base64.decode(apunte.getArchivo().toString(), Base64.DEFAULT));
                        fileOuputStream.write(hexStringToByteArray(response.body()));
                        sacarInformacion(getResources().getString(R.string.g9));
                    } catch (IOException e) {
                        LOGGER.severe("Error al descargar el apunte en un fichero: " + e.getMessage());
                        informar(getResources().getString(R.string.gda1));
                    } finally {
                        if (fileOuputStream != null) {
                            try {
                                fileOuputStream.close();
                            } catch (IOException e) {
                                LOGGER.severe("Error al intentar cerrar el stream para descargar el fichero: " + e.getMessage());
                                informar(getResources().getString(R.string.gda1));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * Saca por patana una información.
     * @param mensaje El mensaje a transmitir
     */
    private void sacarInformacion(String mensaje) {
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
    }

    /**
     * Valida si el precio es valido o no
     * @return True si es valido | False en los demas casos
     */
    private boolean precioValido() {
        boolean resultado = true;
        editTextPrecio.setTextColor(Color.BLACK);
        try {
            String elPrecio = editTextPrecio.getText().toString().trim();
            elPrecio = elPrecio.replace(",", ".");
            float precio = Float.parseFloat(elPrecio);
            if (elPrecio.indexOf(".") != -1) {
                if ((elPrecio.length() - elPrecio.indexOf(".")) > 3) {
                    editTextPrecio.setTextColor(Color.RED);
                    resultado = false;
                }
            }
            if (100000 - precio < 0) {
                editTextPrecio.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.ca4), Toast.LENGTH_LONG).show();
                resultado = false;
            } else if (precio - (0.3) < 0) {
                editTextPrecio.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.ca5), Toast.LENGTH_LONG).show();
                resultado = false;
            }

        } catch (Exception e) {
            resultado = false;
            editTextPrecio.setTextColor(Color.RED);
        }
        return resultado;
    }

    /**
     * Pide las materias al servido.
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
                        LOGGER.severe("Error al cargar los apuntes "+response.code());
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<MateriasBeans> call, Throwable t) {
                    LOGGER.severe("Fallo al cargar las materias " + t.getMessage() + " " + t.getCause());
                    informar(getResources().getString(R.string.gda2));
                }
            });


        } catch (Exception e) {
            LOGGER.severe("ERROR cargar materias " + e.getMessage() + "   " + e.getCause());
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
        materiaSpinner.setAdapter(adapterMaterias);
        int i = 0;
        for (String materia : materias) {
            if (apunte.getMateria().getTitulo().equals(materias[i])) {
                materiaSpinner.setSelection(i);
                break;
            }
            i++;
        }
    }

    /**
     * Actualiza el apunte.
     */
    private void actualizarElApunte() {
        try {
            ApunteBean elApunteModificado = apunte;
            apunte.setTitulo(editTextTitulo.getText().toString().trim());
            apunte.setDescripcion(editTextDesc.getText().toString().trim());
            apunte.setMateria(buscaLaMateria(materiaSpinner.getSelectedItemPosition()));
            apunte.setPrecio(Float.parseFloat(editTextPrecio.getText().toString().trim()));
            ApunteManager apunteManager = ApunteAPIClient.getClient();
            Call<ResponseBody> call = apunteManager.edit(elApunteModificado);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        LOGGER.info("Apunte actualizado");
                    } else {
                        LOGGER.severe("Error al actualizar el apunte "+response.code());
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    LOGGER.severe("Fallor al actualizar el apunte "+t.getMessage());
                    informar(getResources().getString(R.string.gda2));
                }
            });
        } catch (Exception e) {
            LOGGER.severe("Error al modificar el apunte " + e.getMessage());
            informar(getResources().getString(R.string.gda1));
        }
    }

    /**
     * Busca la materia correspondiente del spinner para darsela al apunte.
     * @param selectedItem La materia seleccionada.
     * @return La materia.
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
     * Pide al cliente el permiso de guardado de ficheros.
     */
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.vad5))//vad5
                    .setMessage(getResources().getString(R.string.vad6))//vad6
                    .setPositiveButton(getResources().getString(R.string.vaea4), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(VisorApunteEditable.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.vaea5), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    /**
     * Recoge el resultado de la entrega de permisos.
     * @param requestCode El codigo de la petición.
     * @param permissions Los permisos.
     * @param grantResults Los permisos concedidos.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getResources().getString(R.string.vad7), Toast.LENGTH_SHORT).show();//vad7
            } else {
                Toast.makeText(this, getResources().getString(R.string.vad8), Toast.LENGTH_SHORT).show();//vad8
            }
        }
    }
    /**
     * Informa al usuario de algun error o fallo.
     *
     * @param frase La frase que le muestra al usuario.
     */
    public void informar(String frase) {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialog.setMessage(frase);
        alertDialog.show();
    }
}
