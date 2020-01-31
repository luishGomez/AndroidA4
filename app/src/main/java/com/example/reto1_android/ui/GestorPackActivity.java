package com.example.reto1_android.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;

/**
 * Clase que maneja el activity GestorPack.
 */
public class GestorPackActivity extends AppCompatActivity {
    private MediaPlayer mp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestor_pack);

        mp= MediaPlayer.create(this,R.raw.pulsar_boton);

    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnCrearPack.
     * @param v Vista que lanza la acción.
     */
    public void btnCrearPack(View v){
        mp.start();
        Intent i = new Intent(this, CrearPackActivity.class);
        startActivity(i);
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnListadoPack.
     * @param v Vista que lanza la acción.
     */
    public void btnListadoPack(View v){
        mp.start();
        Intent i = new Intent(this, ListadoPacksActivity.class);
        startActivity(i);
    }
}
