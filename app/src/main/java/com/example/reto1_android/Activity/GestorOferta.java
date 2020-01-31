package com.example.reto1_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.reto1_android.R;
import com.example.reto1_android.model.OfertaBean;
import com.example.reto1_android.model.OfertaBeans;
import com.example.reto1_android.retrofit.GestorOfertaAPIClient;
import com.example.reto1_android.retrofit.GestorOfertaManager;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Gestor de ofertas
 */
public class GestorOferta extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(GestorOferta.class.getName());
    private ListView list;
    private EditText txtBusqueda;
    private int anterior = -1;
    Set<OfertaBean> ofertas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_oferta);
        list = findViewById(R.id.listViewApuntes);
        txtBusqueda = findViewById(R.id.txtApunteBusqueda);
        //Intent intent = getIntent();
        rellenadoLista();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GestorOferta.this, ModificarBorrarOfertaActivity.class);
                for(OfertaBean oferta: ofertas) {
                    LOGGER.info("titulo: "+oferta.getTitulo());
                    if (oferta.tofrase().equals(parent.getItemAtPosition(position))) {
                        intent.putExtra("oferta", (Serializable) oferta);
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
        int size=0;
        String cosa[]=null;
        ArrayAdapter<String> adapter;
        Set<String> frase = new HashSet<>();
        if(!txtBusqueda.getText().toString().isEmpty()){
            for(OfertaBean oferta:ofertas){
                if(oferta.getTitulo().contains(txtBusqueda.getText().toString().trim())){
                    size++;
                    frase.add(oferta.tofrase());
                }
            }
            cosa = new String[size];
            frase.toArray(cosa);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cosa);
            list.setAdapter(adapter);
        }
        else{
            rellenadoLista();
        }
    }
    /**
     * Actualiza y rellena previamente a los posibles cambios en la lista.
     * @param view
     */
    public void onClickActualizar(View view) {
        rellenadoLista();
    }

    /**
     * Recoge las ofertas existentes.
     */
    private void rellenadoLista() {
        GestorOfertaManager ofertaManager = GestorOfertaAPIClient.getClient();
        Call<OfertaBeans> ofes = ofertaManager.findAllOfertas();
        LOGGER.info(ofertaManager.findAllOfertas().getClass().toString());
        ofes.enqueue(new Callback<OfertaBeans>() {
            @Override
            public void onResponse(Call<OfertaBeans> call2, Response<OfertaBeans> response) {
                if (response.isSuccessful()) {
                    LOGGER.info("numero de filas child:"+list.getChildCount());
                    rellenadoListView(response);
                } else {
                    LOGGER.severe("FALLO TODO  MIRA!!!");
                }
            }
            @Override
            public void onFailure(Call<OfertaBeans> call2, Throwable t) {
                LOGGER.severe("vaya ha ocurrido algo:--->" + t.getMessage());
            }
        });
    }

    /**
     * Inserta las ofertas previamente recogidas.
     * @param response Ofertas recogidas.
     */
    private void rellenadoListView(Response<OfertaBeans> response) {
        String cosa[]=null;
        ArrayAdapter<String> adapter;
        ofertas = response.body().getOfertas();
        Set<String> frase = new HashSet<>();
        if(ofertas != null) {
            cosa = new String[ofertas.size()];
            for (OfertaBean oferta : ofertas) {
                frase.add(oferta.tofrase());
            }
            frase.toArray(cosa);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cosa);
            list.setAdapter(adapter);
        }
        else
            list.setAdapter(null);
    }


}
