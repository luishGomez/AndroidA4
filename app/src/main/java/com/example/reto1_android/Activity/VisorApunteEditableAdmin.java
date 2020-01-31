package com.example.reto1_android.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.model.MateriasBeans;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.MateriaAPIClient;
import com.example.reto1_android.retrofit.MateriaManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Muestra un apunte y todas las opciones que puede hacer un administrador.
 * @author Ricardo Peinado Lastra
 */
public class VisorApunteEditableAdmin extends AppCompatActivity {

    private static final Logger LOGGER = Logger.getLogger(VisorApunteEditableAdmin.class.getName());

    private EditText editTextTitulo;
    private EditText editTextDesc;
    private Spinner materiaSpinner;
    private Button btnActualizar;
    private Button btnBorrar;
    private Button btnVerficiar;
    private TextView textVerificacion;
    private ApunteBean apunte;
    private String[] materias=new String[0];//Para que no salte un nullpointerException
    private Calendar calendar = Calendar.getInstance();
    private final int year = calendar.get(Calendar.YEAR);
    private final int month = calendar.get(Calendar.MONTH);
    private final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog.OnDateSetListener setListener;
    private MateriasBeans listaDeMaterias;
    private MediaPlayer mp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_apunte_editable_admin);

        editTextDesc = (EditText) findViewById(R.id.vaea_desc);
        editTextDesc.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editTextTitulo = (EditText) findViewById(R.id.vaea_titulo);
        materiaSpinner = (Spinner) findViewById(R.id.vaea_spinner);
        btnActualizar = (Button) findViewById(R.id.vaea_actualizar);
        btnVerficiar = (Button) findViewById(R.id.vaea_btnValidacion);
        textVerificacion = (TextView) findViewById(R.id.vaea_textValidacion);
        mp= MediaPlayer.create(this,R.raw.pulsar_boton);
        btnBorrar = (Button) findViewById(R.id.vaea_borrar);

        Intent intent = getIntent();
        apunte = (ApunteBean) intent.getSerializableExtra("apunte");
        editTextDesc.setText(apunte.getDescripcion());
        editTextTitulo.setText(apunte.getTitulo());
        LOGGER.info("LA FECHA " + apunte.getFechaValidacion());
        if (apunte.getFechaValidacion() != null)
            textVerificacion.setText(apunte.getFechaValidacion());


        rellenarMaterias();

        btnActualizar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Boolean estaBien = true;
                if (editTextDesc.getText().toString().trim().length() < 3 || editTextDesc.getText().toString().trim().length() > 250) {
                    estaBien = false;
                    editTextDesc.setTextColor(Color.RED);
                    Toast.makeText(VisorApunteEditableAdmin.this, getResources().getString(R.string.vaea1), Toast.LENGTH_LONG).show();//vaea1
                } else
                    editTextDesc.setTextColor(Color.BLACK);
                if (editTextTitulo.getText().toString().trim().length() < 3 || editTextTitulo.getText().toString().trim().length() > 250) {
                    estaBien = false;
                    editTextTitulo.setTextColor(Color.RED);
                    Toast.makeText(VisorApunteEditableAdmin.this, getResources().getString(R.string.vaea2), Toast.LENGTH_LONG).show();//vaea2
                } else
                    editTextTitulo.setTextColor(Color.BLACK);
                if (estaBien) {
                    editTextDesc.setTextColor(Color.BLACK);
                    editTextTitulo.setTextColor(Color.BLACK);
                    actualizarElApunte();
                    Intent resultItem = new Intent();
                    resultItem.putExtra("result", getResources().getString(R.string.tx1)+": " + editTextTitulo.getText().toString());
                    setResult(RESULT_OK, resultItem);
                    finish();
                }
            }
        });
        btnVerficiar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        VisorApunteEditableAdmin.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        SimpleDateFormat formatoParaDate = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        try {
                            textVerificacion.setText(formatter.format(formatoParaDate.parse(date)));
                        } catch (Exception e) {

                        }
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String data = dayOfMonth + "/" + month + "/" + year;
                textVerificacion.setText(data);
            }
        };
        btnBorrar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                intentarBorrarElApunte();
            }
        });

    }

    /**
     * Prepara y compreba que se pueda borrar el apunte..
     */
    private void intentarBorrarElApunte() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.vaea3))//vaea3
                .setPositiveButton(getResources().getString(R.string.vaea4), new DialogInterface.OnClickListener() {//vaea4
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
                                        LOGGER.severe("Error al coger cuantas compras");//gda1
                                        informar(getResources().getString(R.string.gda1));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    LOGGER.severe("FALLO al coger cuantas compras" + t.getMessage());//gda2
                                    informar(getResources().getString(R.string.gda2));
                                }
                            });
                        } catch (Exception e) {
                            LOGGER.severe("Error al coger cuentas compras");
                            informar(getResources().getString(R.string.gda1));
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.vaea5), new DialogInterface.OnClickListener() {//vaea5
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog alertaParaBorrar = builder.create();
        alertaParaBorrar.show();

    }

    /**
     * Borra el apunte.
     * @param body El contenido del resultado de si a sido comprado o no el apunte.
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
                            LOGGER.severe("Error al borrar el apunte");//gda1
                            informar(getResources().getString(R.string.gda1));
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        LOGGER.severe("FALLO AL INTENTAR BORRAR EL APUNTE");//gda2
                        informar(getResources().getString(R.string.gda2));
                    }
                });
            } catch (Exception e) {
                LOGGER.severe("Error al borrar un apunte: "+e.getMessage());//gda1
                informar(getResources().getString(R.string.gda1));
            }

        } else {
            Toast.makeText(this,getResources().getString(R.string.vaea6), Toast.LENGTH_LONG).show();//vaea6
        }
    }

    /**
     * Cierra el activity
     */
    private void cerrar() {
        Intent resultItem = new Intent();
        resultItem.putExtra("result", getResources().getString(R.string.vaea7)+": " + editTextTitulo.getText().toString());//vaea7
        setResult(RESULT_OK, resultItem);
        finish();
    }

    /**
     * Actualiza el apunte
     */
    private void actualizarElApunte() {


        try {
            ApunteBean elApunteModificado = apunte;
            apunte.setTitulo(editTextTitulo.getText().toString().trim());
            apunte.setDescripcion(editTextDesc.getText().toString().trim());
            apunte.setMateria(buscaLaMateria(materiaSpinner.getSelectedItemPosition()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            apunte.setFechaValidacion(textVerificacion.getText().toString());
            ApunteManager apunteManager = ApunteAPIClient.getClient();
            Call<ResponseBody> call = apunteManager.edit(elApunteModificado);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        LOGGER.info("Se a realizado la actualización correctamente");
                    } else {
                        LOGGER.severe("Error al actualizar el apunte: "+response.code());//gda1
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    LOGGER.severe("Fallo al actualizar el apunte: "+t.getMessage());//gda2
                    informar(getResources().getString(R.string.gda2));
                }
            });
        } catch (Exception e) {
            LOGGER.severe("Error al modificar el apunte " + e.getMessage());//gda1
            informar(getResources().getString(R.string.gda1));
        }
    }

    /**
     * Busca la materia del spinner.
     * @param selectedItem La materia selecciona desde el spinner.
     * @return Devuelve la materia seleccionada.
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
     * Rellena las materias.
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
                        LOGGER.severe("Error al rellenar materias: "+response.code());//gda1
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<MateriasBeans> call, Throwable t) {
                    LOGGER.severe("Fallo " + t.getMessage() + " " + t.getCause());//gda2
                    informar(getResources().getString(R.string.gda2));
                }
            });


        } catch (Exception e) {
            LOGGER.severe("ERROR cargar materias " + e.getMessage() + "   " + e.getCause());//gda1
            informar(getResources().getString(R.string.gda1));
        }
    }

    /**
     * Inserta las materias en sus respectivos lugares.
     * @param body
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
