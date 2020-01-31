package com.example.reto1_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ApuntesBeans;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.OfertaBean;
import com.example.reto1_android.model.OfertaBeans;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.GestorOfertaAPIClient;
import com.example.reto1_android.retrofit.GestorOfertaManager;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Biblioteca que contine todos los apuntes coprados del cliente
 */
public class BibliotecaActivity extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(BibliotecaActivity.class.getName());
    private ListView listViewApuntes;
    private EditText txtApunteBusqueda;
    private int anterior = -1;
    Set<ApunteBean> apuntes;
    ClienteBean cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);
        listViewApuntes = findViewById(R.id.listViewApuntes);
        txtApunteBusqueda = findViewById(R.id.txtApunteBusqueda);
        Intent intent = getIntent();
        cliente = (ClienteBean) intent.getSerializableExtra("cliente");
        rellenadoLista();
        listViewApuntes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BibliotecaActivity.this, BibliotecaApunteLikeDescarga.class);
                for(ApunteBean apunte: apuntes) {
                    if (toFrase(apunte).equals(parent.getItemAtPosition(position))) {
                        intent.putExtra("apunte", (Serializable) apunte);
                        intent.putExtra("cliente", (Serializable) cliente);
                        break;
                    }
                }
                startActivity(intent);
            }
        });
    }

    /**
     * Filtro de busqueda por medio de Titulo del mismo.
     * @param view
     */
    public void onClickBuscar(View view) {
        LOGGER.info(txtApunteBusqueda.getText().toString());
        int size=0;
        String array[]=null;
        ArrayAdapter<String> adapter;
        Set<String> frase = new HashSet<>();
        if(!txtApunteBusqueda.getText().toString().isEmpty()){
            for(ApunteBean apunte:apuntes){
                if(apunte.getTitulo().toUpperCase().contains(txtApunteBusqueda.getText().toString().toUpperCase().trim())){
                    size++;
                    frase.add(toFrase(apunte));
                    LOGGER.info(toFrase(apunte));
                }
            }
            array = new String[size];
            frase.toArray(array);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            listViewApuntes.setAdapter(adapter);
        }
        else{
            rellenadoLista();
        }
    }
    /**
     * Recoge los apuntes existentes.
     */
    private void rellenadoLista() {
        ApunteManager apunteManager = ApunteAPIClient.getClient();
        Call<ApuntesBeans> ofes = apunteManager.getApuntesByComprador(cliente.getId());
        ofes.enqueue(new Callback<ApuntesBeans>() {
            @Override
            public void onResponse(Call<ApuntesBeans> call2, Response<ApuntesBeans> response) {
                if (response.isSuccessful()) {
                    LOGGER.info("todo bn por ahora");
                    rellenadoListView(response);
                } else {
                    LOGGER.severe("No se ha podido rellenar los datos."+response.message());
                }
            }
            @Override
            public void onFailure(Call<ApuntesBeans> call2, Throwable t) {
                LOGGER.severe("vaya ha ocurrido algo:--->" + t.getMessage());
            }
        });
    }

    /**
     * Inserta los apuntes previamente recogidas.
     * @param response Apuntes recogidas.
     */
    private void rellenadoListView(Response<ApuntesBeans> response) {
        String array[]=null;
        ArrayAdapter<String> adapter;
        apuntes = response.body().getApuntes();
        Set<String> frase = new HashSet<>();
        if(apuntes != null) {
            array = new String[apuntes.size()];
            for (ApunteBean apunte : apuntes) {
                LOGGER.info("Frase:-->"+toFrase(apunte));
                frase.add(toFrase(apunte));
            }
            frase.toArray(array);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            listViewApuntes.setAdapter(adapter);
        }
        else
            listViewApuntes.setAdapter(null);

    }

    /**
     * Convierte en un String todos los elementos o los mas relevantes del apunte
     * @param apunte
     * @return
     */
    private String toFrase(ApunteBean apunte) {
        return "Titulo: "+apunte.getTitulo()+" "+"\n\n" +
                "Materia: "+apunte.getMateria()+"  Autor:"+apunte.getCreador()+" \n" +
                "Fecha validacion: "+apunte.getFechaValidacion();
    }


}
