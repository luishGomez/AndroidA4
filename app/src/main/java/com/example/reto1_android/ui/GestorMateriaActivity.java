package com.example.reto1_android.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;

/**
 * Clase que maneja el activity GestorMateria.
 */
public class GestorMateriaActivity  extends AppCompatActivity {
    private MediaPlayer mp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ){
            setContentView(R.layout.gestor_materia);
        }else{
            setContentView(R.layout.gestor_materia_landscape);
        }
        mp= MediaPlayer.create(this,R.raw.pulsar_boton);
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnCrearMateria.
     * @param v Vista que lanza la acción.
     */
    public void btnCrearMateria(View v){
        mp.start();
        Intent i = new Intent(this, CrearMateriaActivity.class);
        startActivity(i);
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnListadoMateria.
     * @param v Vista que lanza la acción.
     */
    public void btnListadoMateria(View v){
        mp.start();
        Intent i = new Intent(this, ListadoMateriasActivity.class);
        startActivity(i);
    }
}