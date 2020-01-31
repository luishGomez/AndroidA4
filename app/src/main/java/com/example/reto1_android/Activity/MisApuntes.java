package com.example.reto1_android.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
 * Visualiza todos los apuntes creador por el cliente concentado.
 * @author Ricardo Peinado Lastra
 */
public class MisApuntes extends AppCompatActivity {

    private ListView lista;
    private ApunteBean apuntes[] = new ApunteBean[0];
    private static final Logger LOGGER = Logger.getLogger(MisApuntes.class.getName());
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
        setContentView(R.layout.activity_mis_apuntes);

        Intent intent = getIntent();
        cliente = (ClienteBean) intent.getSerializableExtra("cliente");
        buscar = (Button) findViewById(R.id.ma_btnBuscar);
        textField = (EditText) findViewById(R.id.ma_textField);
        btnRefrescar = (Button) findViewById(R.id.ma_btnRefrescar);
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
        spinnerOrdenar = (Spinner) findViewById(R.id.ma_spinnerOrdenar);
        spinnerMateria = (Spinner) findViewById(R.id.ma_spinnerMateria);
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
                LOGGER.info("No a seleccionado ningun filtro.");
            }
        });
        spinnerMateria.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buscarMateria(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LOGGER.info("No a seleccionado ninguna materia.");
            }
        });

        lista = (ListView) findViewById(R.id.ma_lv1);
        rellena();

    }


    /**
     * Manda filtrar, por todos los filtros.
     *
     * @param position
     */
    private void buscarMateria(int position) {
        filtrarPorNombre();
    }

    /**
     * Gestiona que tipo de orden se tienen que ordenador los apuntes.
     *
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
     * Ordena por precio descendente los apuntes.
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
        lista.setAdapter(new Adaptador(this, apuntesFiltradosPorOrden));
    }

    /**
     * Ordena los apuntes por el orden contrario del abecedario.
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
     * Gestiona si tiene que filtrar los apuntes sin filtrado o con el filtrado.
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
     * Filtra por todos las restricciones seleccionadas.
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
     * Rellena las materias como los apuntes.
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
                    } else {
                        LOGGER.severe("Ocurrio un error al intentar pedir los apuntes");//gda1
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<ApuntesBeans> call, Throwable t) {
                    LOGGER.severe("Fallor al intentar pedir los apuntes " + t.getMessage() + " " + t.getCause()); //gda2
                    informar(getResources().getString(R.string.gda2));
                }
            });
        } catch (Exception e) {
            LOGGER.severe("ERROR al pedir los apuntes " + e.getMessage() + "   " + e.getCause());//gda1
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
                    } else {
                        LOGGER.severe("Error al intentar pedir las materias");//gda1
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<MateriasBeans> call, Throwable t) {
                    LOGGER.severe("Fallo al pedir las materias " + t.getMessage() + " " + t.getCause());//gda2
                    informar(getResources().getString(R.string.gda2));
                }
            });
            LOGGER.info("CUANTAS MATERIAS-->" + materias.length);
        } catch (Exception e) {
            LOGGER.severe("ERROR cargar materias " + e.getMessage() + "   " + e.getCause());//gda1
            informar(getResources().getString(R.string.gda1));
        }
    }

    /**
     * Inserta los apuntes en la lista que los visualiza.
     *
     * @param apuntesBeans La lista de los apuntes.
     */
    public void insertarApuntes(ApuntesBeans apuntesBeans) {

        Set<ApunteBean> apu = new HashSet<ApunteBean>();
        for (ApunteBean apunte : apuntesBeans.getApuntes()) {
            if (apunte.getCreador().getId().equals(cliente.getId())) {
                apu.add(apunte);
            }
        }
        LOGGER.severe("CUANTOS APUNTOS" + apu.size());
        apuntes = new ApunteBean[apu.size()];
        apu.toArray(apuntes);
        LOGGER.info("Hay A" + apuntes.length);
        lista.setAdapter(new Adaptador(this, apuntes));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mp.start();
                Intent visorApunte = new Intent(view.getContext(), VisorApunteEditable.class);
                visorApunte.putExtra("cliente", (Serializable) cliente);
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
     * Inserta las amterias en un spinner.
     *
     * @param materiasBeans La lista de las materias.
     */
    public void insertarMaterias(MateriasBeans materiasBeans) {
        Set<MateriaBean> lasMaterias = materiasBeans.getMaterias();
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
     * El resultado de una actividad creada desde esta activity.
     *
     * @param requestCode El codido de petición.
     * @param resultCode  El codigo de resultado.
     * @param data        Algun data que haya retornado.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //recoger nuevos datos ya que se ha hecho una modificacion.
                String elTitulo = data.getStringExtra("result");
                Toast.makeText(this, elTitulo, Toast.LENGTH_LONG).show();//ma2
                rellena();

            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                rellena();
                Toast.makeText(this, getResources().getString(R.string.ma1), Toast.LENGTH_LONG).show();//m1
            }
        }
    }
    //menu

    /**
     * Crea las opciones del menu, en este caso para crear el apunte
     *
     * @param menu El menu.
     * @return Retorna siempre un true.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mis_apuntes, menu);
        return true;
    }

    /**
     * Gestiona la selecciones del menu.
     *
     * @param item El menu seleccionado.
     * @return Retorna si se a seleccionado el item que se a pasado como parametro.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuMA1) {
            Intent visorApunte = new Intent(MisApuntes.this, CrearApunte.class);
            visorApunte.putExtra("cliente", (Serializable) cliente);
            startActivityForResult(visorApunte, 2);
        }
        return super.onOptionsItemSelected(item);
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
