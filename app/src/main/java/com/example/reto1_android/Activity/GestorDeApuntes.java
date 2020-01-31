package com.example.reto1_android.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ApuntesBeans;
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.model.MateriasBeans;
import com.example.reto1_android.model.UserBean;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.MateriaAPIClient;
import com.example.reto1_android.retrofit.MateriaManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Esta clase gestiona los apuntes existentes.
 * @author Ricardo Peinado Lastra
 */
public class GestorDeApuntes extends AppCompatActivity {
    private ListView lista;
    private ApunteBean apuntes[] = new ApunteBean[0];
    private static final Logger LOGGER = Logger.getLogger(GestorDeApuntes.class.getName());
    private Button buscar;
    private Button btnRefrescar;
    private EditText textField;
    private Spinner spinnerOrdenar;
    private Spinner spinnerMateria;
    private boolean filtrado = false;
    private String cadena;
    private ApunteBean apuntesFiltradosPorNombre[];
    private ApunteBean apuntesFiltradosPorOrden[];
    private UserBean cliente;
    private String[] materias = new String[0];
    private MediaPlayer mp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_de_apuntes);

        Intent intent = getIntent();
        cliente = (UserBean) intent.getSerializableExtra("user");
        buscar = (Button) findViewById(R.id.liaa_btnBuscar);
        textField = (EditText) findViewById(R.id.liaa_textField);
        btnRefrescar = (Button) findViewById(R.id.liaa_btnRefrescar);
        mp= MediaPlayer.create(this,R.raw.pulsar_boton);
        btnRefrescar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                rellena();
            }
        });
        buscar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                filtrarPorNombre();
            }
        });
        spinnerOrdenar = (Spinner) findViewById(R.id.liaa_spinnerOrdenar);
        spinnerMateria = (Spinner) findViewById(R.id.liaa_spinnerMateria);
        String[] opciones = {getResources().getString(R.string.g1), getResources().getString(R.string.g2), getResources().getString(R.string.g3), getResources().getString(R.string.g4), getResources().getString(R.string.g5)};
        ArrayAdapter<String> adapterOrdenar = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerOrdenar.setAdapter(adapterOrdenar);
        spinnerOrdenar.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ordenar(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LOGGER.info("No toca ningun ningun filtro de ordenar.");
            }
        });
        spinnerMateria.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buscarMateria(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LOGGER.info("No toca ningun filtro de materia.");
            }
        });

        lista = (ListView) findViewById(R.id.liaa_lv1);
        rellena();


        LOGGER.info("Termina el init");


    }

    /**
     * Manda la acción de filtrar.
     * @param position
     */
    private void buscarMateria(int position) {
        filtrarPorNombre();
    }

    /**
     * Gestiona a que metodo llamar para ordenar.
     * @param position
     */
    private void ordenar(Integer position) {
        switch (position) {
            case 0:
                ordenarSinFiltro();
                break;
            case 1:
                ordenarABC();
                break;
            case 2:
                ordenarZYX();
                break;
            case 3:
                ordenarPreioAsc();
                break;
            case 4:
                ordenarPrecioDesc();
                break;
        }
    }

    /**
     * Ordena los apuntes por precio descendente.
     */
    private void ordenarPrecioDesc() {
        ApunteBean aux;
        if (filtrado)
            apuntesFiltradosPorOrden = apuntesFiltradosPorNombre;
        else
            apuntesFiltradosPorOrden = apuntes;

        for (int i = 0; i < apuntesFiltradosPorOrden.length; i++) {
            for (int j = i + 1; j < apuntesFiltradosPorOrden.length; j++) {
                if (apuntesFiltradosPorOrden[i].getPrecio() < apuntesFiltradosPorOrden[j].getPrecio()) {
                    aux = apuntesFiltradosPorOrden[i];
                    apuntesFiltradosPorOrden[i] = apuntesFiltradosPorOrden[j];
                    apuntesFiltradosPorOrden[j] = aux;
                }
            }
        }

        lista.setAdapter(new Adaptador(this, apuntesFiltradosPorOrden));
    }

    /**
     * Ordena los pauntes por precio ascendente.
     */
    private void ordenarPreioAsc() {
        ApunteBean aux;
        if (filtrado)
            apuntesFiltradosPorOrden = apuntesFiltradosPorNombre;
        else
            apuntesFiltradosPorOrden = apuntes;

        for (int i = 0; i < apuntesFiltradosPorOrden.length; i++) {
            for (int j = i + 1; j < apuntesFiltradosPorOrden.length; j++) {
                if (apuntesFiltradosPorOrden[i].getPrecio() > apuntesFiltradosPorOrden[j].getPrecio()) {
                    aux = apuntesFiltradosPorOrden[i];
                    apuntesFiltradosPorOrden[i] = apuntesFiltradosPorOrden[j];
                    apuntesFiltradosPorOrden[j] = aux;
                }
            }
        }
        for (ApunteBean a : apuntesFiltradosPorOrden) {
            LOGGER.severe(a.getPrecio() + "€");
        }
        lista.setAdapter(new Adaptador(this, apuntesFiltradosPorOrden));
    }

    /**
     * Ordena los apuntes en el orden contrario del abecedario.
     */
    private void ordenarZYX() {

        ApunteBean aux;
        if (filtrado)
            apuntesFiltradosPorOrden = apuntesFiltradosPorNombre;
        else
            apuntesFiltradosPorOrden = apuntes;

        for (int i = 0; i < apuntesFiltradosPorOrden.length; i++) {
            for (int j = i + 1; j < apuntesFiltradosPorOrden.length; j++) {
                if (apuntesFiltradosPorOrden[i].getTitulo().compareToIgnoreCase(apuntesFiltradosPorOrden[j].getTitulo()) < 0) {
                    aux = apuntesFiltradosPorOrden[i];
                    apuntesFiltradosPorOrden[i] = apuntesFiltradosPorOrden[j];
                    apuntesFiltradosPorOrden[j] = aux;
                }
            }
        }

        lista.setAdapter(new Adaptador(this, apuntesFiltradosPorOrden));
    }

    /**
     * Ordena los apuntes por el orden del abecedario.
     */
    private void ordenarABC() {
        ApunteBean aux;
        if (filtrado)
            apuntesFiltradosPorOrden = apuntesFiltradosPorNombre;
        else
            apuntesFiltradosPorOrden = apuntes;

        for (int i = 0; i < apuntesFiltradosPorOrden.length; i++) {
            for (int j = i + 1; j < apuntesFiltradosPorOrden.length; j++) {
                if (apuntesFiltradosPorOrden[i].getTitulo().compareToIgnoreCase(apuntesFiltradosPorOrden[j].getTitulo()) > 0) {
                    aux = apuntesFiltradosPorOrden[i];
                    apuntesFiltradosPorOrden[i] = apuntesFiltradosPorOrden[j];
                    apuntesFiltradosPorOrden[j] = aux;
                }
            }
        }

        lista.setAdapter(new Adaptador(this, apuntesFiltradosPorOrden));
    }

    /**
     * Gestiona que lista se tiene que cargar dependiendo si esta filtrado o no.
     */
    private void ordenarSinFiltro() {
        if (filtrado) {
            apuntesFiltradosPorOrden = apuntesFiltradosPorNombre;
            lista.setAdapter(new Adaptador(this, apuntesFiltradosPorNombre));
        } else {
            apuntesFiltradosPorOrden = apuntes;
            lista.setAdapter(new Adaptador(this, apuntes));
        }
    }

    /**
     * Filtra la lista de apuntes determinando cuales cumplen el filtrado.
     */
    private void filtrarPorNombre() {

        if (textField.getText().toString() == "" && spinnerMateria.getSelectedItemPosition() == 0) {
            LOGGER.info("camino 1");
            lista.setAdapter(new Adaptador(this, apuntes));
            filtrado = false;
        } else if (spinnerMateria.getSelectedItemPosition() == 0) {
            LOGGER.info("camino 2");
            filtrado = true;
            cadena = textField.getText().toString().toLowerCase();
            ArrayList<ApunteBean> apuntesNombre = new ArrayList<ApunteBean>();
            for (ApunteBean a : apuntes) {
                if (a.getTitulo().toLowerCase().contains(textField.getText().toString().toLowerCase())) {
                    apuntesNombre.add(a);
                }
            }
            apuntesFiltradosPorNombre = new ApunteBean[apuntesNombre.size()];
            apuntesFiltradosPorNombre = apuntesNombre.toArray(apuntesFiltradosPorNombre);
            lista.setAdapter(new Adaptador(this, apuntesFiltradosPorNombre));
        } else if (textField.getText().toString().trim().equals("")) {
            LOGGER.info("camino 3");
            filtrado = true;
            cadena = textField.getText().toString().toLowerCase();
            ArrayList<ApunteBean> apuntesNombre = new ArrayList<ApunteBean>();
            for (ApunteBean a : apuntes) {

                if (a.getMateria().getTitulo().trim().toLowerCase().equals(materias[spinnerMateria.getSelectedItemPosition()].trim().toLowerCase())) {
                    apuntesNombre.add(a);
                }
            }
            apuntesFiltradosPorNombre = new ApunteBean[apuntesNombre.size()];
            apuntesFiltradosPorNombre = apuntesNombre.toArray(apuntesFiltradosPorNombre);
            lista.setAdapter(new Adaptador(this, apuntesFiltradosPorNombre));

        } else {
            LOGGER.info("camino 4" + textField.getText().toString().trim());
            filtrado = true;
            cadena = textField.getText().toString().toLowerCase();
            ArrayList<ApunteBean> apuntesNombre = new ArrayList<ApunteBean>();
            for (ApunteBean a : apuntes) {
                LOGGER.severe(a.getMateria().getTitulo().trim().toLowerCase() + " == " + materias[spinnerMateria.getSelectedItemPosition()].trim().toLowerCase());

                if (a.getMateria().getTitulo().trim().toLowerCase().equals(materias[spinnerMateria.getSelectedItemPosition()].trim().toLowerCase()) && a.getTitulo().toLowerCase().contains(textField.getText().toString().trim().toLowerCase())) {
                    apuntesNombre.add(a);
                }
            }
            apuntesFiltradosPorNombre = new ApunteBean[apuntesNombre.size()];
            apuntesFiltradosPorNombre = apuntesNombre.toArray(apuntesFiltradosPorNombre);
            lista.setAdapter(new Adaptador(this, apuntesFiltradosPorNombre));
        }
        ordenar(spinnerOrdenar.getSelectedItemPosition());
    }

    /**
     * Rellena los apuntes como las materias en la lsita y en el spinner correspondientes.
     */
    private void rellena() {
        try {
            ApunteManager apunteManager = ApunteAPIClient.getClient();
            Call<ApuntesBeans> call = apunteManager.findAll();

            call.enqueue(new Callback<ApuntesBeans>() {
                @Override
                public void onResponse(Call<ApuntesBeans> call, Response<ApuntesBeans> response) {
                    if (response.isSuccessful()) {
                        insertarApuntes(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ApuntesBeans> call, Throwable t) {
                    LOGGER.severe("Failure " + t.getMessage() + " " + t.getCause());//gda2
                    informar(getResources().getString(R.string.gda2));
                }
            });


        } catch (Exception e) {
            LOGGER.severe("ERROR " + e.getMessage() + "   " + e.getCause());//gda1
            informar(getResources().getString(R.string.gda1));
        }
        try {

            MateriaManager materiaManager = MateriaAPIClient.getClient();
            Call<MateriasBeans> call = materiaManager.findAll();


            call.enqueue(new Callback<MateriasBeans>() {
                @Override
                public void onResponse(Call<MateriasBeans> call, Response<MateriasBeans> response) {
                    if (response.isSuccessful()) {

                        insertarMaterias(response.body());

                    }
                }

                @Override
                public void onFailure(Call<MateriasBeans> call, Throwable t) {
                    LOGGER.severe("Failure " + t.getMessage() + " " + t.getCause());//gda2
                    informar(getResources().getString(R.string.gda2));
                }
            });


            LOGGER.info("CUANTAS MATERIAS-->" + materias.length);

        } catch (Exception e) {
            LOGGER.severe("ERROR cargar las materias " + e.getMessage() + "   " + e.getCause());//gda1
            informar(getResources().getString(R.string.gda1));
        }
        LOGGER.info("Recuento de apuntes: " + apuntes.length + " recuento de materias:" + materias.length);

    }

    /**
     * Inserta los apuntes en la lista.
     *
     * @param apuntesBeans La lista de los apuntes.
     */
    public void insertarApuntes(ApuntesBeans apuntesBeans) {

        Set<ApunteBean> apu = apuntesBeans.getApuntes();
        apuntes = new ApunteBean[apu.size()];
        apu.toArray(apuntes);
        lista.setAdapter(new Adaptador(this, apuntes));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mp.start();
                Intent visorApunte = new Intent(view.getContext(), VisorApunteEditableAdmin.class);
                if (filtrado == false) {
                    if (apuntesFiltradosPorOrden[position] == null)
                        LOGGER.severe("Error esta vacio en la posición de orden");
                    visorApunte.putExtra("apunte", (Serializable) apuntes[position]);
                } else {
                    if (apuntesFiltradosPorOrden[position] == null)
                        LOGGER.severe("Error esta vacio en la posición de orden");
                    visorApunte.putExtra("apunte", (Serializable) apuntesFiltradosPorOrden[position]);
                }
                startActivityForResult(visorApunte, 1);
            }
        });
        filtrarPorNombre();

    }

    /**
     * Inserta las materias en el spinner.
     *
     * @param materiasBeans La lista de materias a insertar.
     */
    public void insertarMaterias(MateriasBeans materiasBeans) {
        Set<MateriaBean> lasMaterias = materiasBeans.getMaterias();
        materias = new String[lasMaterias.size() + 1];
        int indice = 1;
        materias[0] = getResources().getString(R.string.g1);//g1
        for (MateriaBean mat : lasMaterias) {
            materias[indice] = mat.getTitulo();
            indice++;
        }
        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materias);
        spinnerMateria.setAdapter(adapterMaterias);

    }

    private void rellenaLosApuntes(Response<ApuntesBeans> response) {
        try {
            LOGGER.info("Empieza a rellenar los apuntes.");
            Set<ApunteBean> apu = response.body().getApuntes();
            apuntes = new ApunteBean[apu.size()];
            int i = 0;
            for (ApunteBean a : apu) {
                LOGGER.info("vamos " + i + " apuntes");
                apuntes[i] = a;
                i++;
            }
        } catch (Exception e) {
            LOGGER.severe("ERROR en rellenaLosApuntes " + e.getMessage());//gda1
            informar(getResources().getString(R.string.gda1));
        }
    }

    /**
     * Recoge el resultado de alguna actividad que provenga de esta.
     *
     * @param requestCode El code de la petición.
     * @param resultCode  El codigo de resultado.
     * @param data        Datos que se puedan haber pasado.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //recoger nuevos datos ya que se ha hecho una modificacion.
                String elTitulo = data.getStringExtra("result");
                Toast.makeText(this,  elTitulo, Toast.LENGTH_LONG).show();//tx1
                rellena();
            }
        }
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
