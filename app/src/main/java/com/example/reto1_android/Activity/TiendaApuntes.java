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
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.model.MateriasBeans;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;
import com.example.reto1_android.retrofit.MateriaAPIClient;
import com.example.reto1_android.retrofit.MateriaManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Visualiza los apuntes que un cliente se puede comprar.
 * @author Ricardo Peinado Lastra
 */
public class TiendaApuntes extends AppCompatActivity {

    private ListView lista;
    private ApunteBean apuntes[] = new ApunteBean[0];
    private static final Logger LOGGER = Logger.getLogger(TiendaApuntes.class.getName());
    private Button buscar;
    private Button btnRefrescar;
    private EditText textField;
    private Spinner spinnerOrdenar;
    private Spinner spinnerMateria;
    private boolean filtrado = false;
    private boolean ordenado;
    private String cadena;
    private ApunteBean apuntesFiltradosPorNombre[];
    private ApunteBean apuntesFiltradosPorOrden[];
    private ClienteBean cliente;
    private String[] materias = new String[0];
    private MediaPlayer mp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda_apuntes);

        Intent intent = getIntent();
        cliente = (ClienteBean) intent.getSerializableExtra("cliente");
        buscar = (Button) findViewById(R.id.ta_btnBuscar);
        textField = (EditText) findViewById(R.id.ta_textField);
        btnRefrescar = (Button) findViewById(R.id.ta_btnRefrescar);
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
        spinnerOrdenar = (Spinner) findViewById(R.id.ta_spinnerOrdenar);
        spinnerMateria = (Spinner) findViewById(R.id.ta_spinnerMateria);
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
                LOGGER.info("No selecciona ningun orden");
            }
        });
        spinnerMateria.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buscarMateria(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LOGGER.info("No selecciona ninguna materia");
            }
        });

        lista = (ListView) findViewById(R.id.ta_listaView);
        rellena();

    }

    /**
     * Manda a filtrar los apuntes.
     * @param position
     */
    private void buscarMateria(int position) {
        filtrarPorNombre();
    }

    /**
     * Gestiona que tipo de orden se a va ordenar los apuntes.
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
     * Ordena los apuntes por precio ascendente.
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
     * Ordena los apuntes por el orden inverso del abecedario.
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
     * Gestiona si se va a mostrar los apuntes filtrados o los sin filtrar.
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
     * Filtra todos los aputnes por todas las resitricciones.
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
            LOGGER.info("camino 4" + textField.getText().toString().trim() + " asd");
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
     * Pide todos los apuntes y amterias al servido.
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
                    }else{
                        LOGGER.severe("Error al pedir los apuntes");
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<ApuntesBeans> call, Throwable t) {
                    LOGGER.severe("Fallo al pedir los apuntes " + t.getMessage() + " " + t.getCause());
                    informar(getResources().getString(R.string.gda2));
                }
            });
        } catch (Exception e) {
            LOGGER.severe("ERROR al pedir los apuntes " + e.getMessage() + "   " + e.getCause());
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
                    }else{
                        LOGGER.severe("Error al pedir las amterias"+response.code());
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<MateriasBeans> call, Throwable t) {
                    LOGGER.severe("Fallo al pedir las materias " + t.getMessage() + " " + t.getCause());
                    informar(getResources().getString(R.string.gda2));
                }
            });
            LOGGER.info("CUANTAS MATERIAS-->" + materias.length);
        } catch (Exception e) {
            LOGGER.severe("ERROR pedir materias " + e.getMessage() + "   " + e.getCause());
            informar(getResources().getString(R.string.gda1));
        }


    }

    /**
     * Inserta los apuntes en la lista.
     * @param apuntesBeans
     */
    public void insertarApuntes(ApuntesBeans apuntesBeans) {
        LOGGER.info("Cargar todos los apuntes comprados por el cliente");
        try {
            ApunteManager apunteManager = ApunteAPIClient.getClient();
            Call<ApuntesBeans> call = apunteManager.getApuntesByComprador(cliente.getId());
            call.enqueue(new Callback<ApuntesBeans>() {
                @Override
                public void onResponse(Call<ApuntesBeans> call, Response<ApuntesBeans> response) {
                    if (response.isSuccessful()) {
                        filtrarApuntesParaLaTienda(apuntesBeans, response.body());
                    }else{
                        LOGGER.severe("Error al cargar los apuntes comprados por el ciente"+response.code());
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<ApuntesBeans> call, Throwable t) {
                    LOGGER.severe("FALLO al intentar coger los apuntes comprados. " + t.getMessage());
                    informar(getResources().getString(R.string.gda2));
                }
            });
        } catch (Exception e) {
            LOGGER.severe("Error al coger "+e.getMessage());
            informar(getResources().getString(R.string.gda1));
        }

    }

    /**
     * Filtra los apuntes para que sean los que pueda comprar y no los que haya creado o comprado con anterioridad.
     * @param apuntesBeansTodos Todos los apuntes.
     * @param bodyComprados Los apuntes comnprados los el cliente.
     */
    private void filtrarApuntesParaLaTienda(ApuntesBeans apuntesBeansTodos, ApuntesBeans bodyComprados) {
        Set<ApunteBean> todos = apuntesBeansTodos.getApuntes();
        Set<ApunteBean> comprados = bodyComprados.getApuntes();
        Set<ApunteBean> apuntesNoCreadosPorMi = new HashSet<ApunteBean>();
        Set<ApunteBean> apuntesDeTienda = new HashSet<ApunteBean>();
        //Conseguir los apuntes no creador pos mi
        for (ApunteBean apunte : todos) {
            if (!apunte.getCreador().getId().equals(cliente.getId())) {
                apuntesNoCreadosPorMi.add(apunte);
            }
        }
        for (ApunteBean apunte : apuntesNoCreadosPorMi) {
            boolean comprado = false;
            for (ApunteBean apunteComprado : comprados) {
                if (apunte.getIdApunte().equals(apunteComprado.getIdApunte())) {
                    comprado = true;
                    break;
                }
            }
            if (!comprado)
                apuntesDeTienda.add(apunte);
        }
        //E insertamos todos los apuntes
        apuntes = new ApunteBean[apuntesDeTienda.size()];
        apuntesDeTienda.toArray(apuntes);
        LOGGER.info("Hay A" + apuntes.length);
        lista.setAdapter(new Adaptador(this, apuntes));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mp.start();
                Intent visorApunte = new Intent(view.getContext(), VisorApunte.class);
                visorApunte.putExtra("cliente", (Serializable) cliente);
                if (filtrado == false) {

                    visorApunte.putExtra("apunte", (Serializable) apuntes[position]);
                } else {

                    visorApunte.putExtra("apunte", (Serializable) apuntesFiltradosPorOrden[position]);

                }
                startActivityForResult(visorApunte, 1);
            }
        });
        filtrarPorNombre();

    }

    /**
     * Inserta las materias en un spinner.
     * @param materiasBeans La lista de las materias.
     */
    public void insertarMaterias(MateriasBeans materiasBeans) {
        Set<MateriaBean> lasMaterias = materiasBeans.getMaterias();
        LOGGER.info("CUANTOS materias" + lasMaterias.size());
        materias = new String[lasMaterias.size() + 1];
        int indice = 1;
        materias[0] = getResources().getString(R.string.g1);
        for (MateriaBean mat : lasMaterias) {
            materias[indice] = mat.getTitulo();
            indice++;
        }
        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materias);
        spinnerMateria.setAdapter(adapterMaterias);

    }

    /**
     * Recoge el resultado de la activity que se habra desde esta.
     * @param requestCode El codigo de petición
     * @param resultCode El codigo de resultado.
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //recoger nuevos datos ya que se ha hecho una modificacion.
                String mensaje = data.getStringExtra("result");
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                rellena();
                actualizaCliente();
            }
        }
    }

    /**
     * Actualiza el cliente.
     */
    private void actualizaCliente() {
        try {
            ClienteManager clienteManager = ClienteAPIClient.getClient();
            Call<ClienteBean> call = clienteManager.find(cliente.getId());
            call.enqueue(new Callback<ClienteBean>() {
                @Override
                public void onResponse(Call<ClienteBean> call, Response<ClienteBean> response) {
                    if (response.isSuccessful()) {
                        cliente = response.body();
                        LOGGER.info("Cliente actualizado.");
                    }else{
                        LOGGER.severe("Error al recargar el cliente"+response.code());
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<ClienteBean> call, Throwable t) {
                    LOGGER.severe("Fallo al recargar el cliente");
                    informar(getResources().getString(R.string.gda2));
                }
            });
        } catch (Exception e) {
            LOGGER.severe("Erro al actualizar el cliente" + e.getMessage());
            informar(getResources().getString(R.string.gda1));
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
