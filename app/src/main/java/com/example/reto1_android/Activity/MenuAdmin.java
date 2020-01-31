package com.example.reto1_android.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.reto1_android.R;
import com.example.reto1_android.model.UserBean;
import com.example.reto1_android.ui.GestorMateriaActivity;
import com.example.reto1_android.ui.GestorPackActivity;

import java.io.Serializable;
import java.util.logging.Logger;


/**
 * @author Ricardo Peinado Lastra
 */
public class MenuAdmin extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(MenuAdmin.class.getName());
    private UserBean user;
    private MediaPlayer mp;
    private Button btnApuntes;
    private Button btnPaquetes;
    private Button btnOfertas;
    private Button btnMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        Intent intent = getIntent();
        user = (UserBean) intent.getSerializableExtra("user");

        mp = MediaPlayer.create(this, R.raw.pulsar_boton);
        btnApuntes = (Button) findViewById(R.id.btnApuntes);
        btnMaterias = (Button) findViewById(R.id.btnMaterias);
        btnOfertas = (Button) findViewById(R.id.btnOfertas);
        btnPaquetes = (Button) findViewById(R.id.btnPaquetes);

        btnApuntes.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    iniciarGestorApuntes();
                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });
        btnMaterias.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    iniciarGestorMaterias();
                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });
        btnOfertas.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    iniciarGestorOfertas();
                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });
        btnPaquetes.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    iniciarGestorPacks();
                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });


    }

    private void iniciarGestorOfertas() {
        Intent intent = new Intent(this, GestorOferta.class);
        startActivity(intent);
    }

    private void iniciarGestorPacks() {
        Intent intent = new Intent(this, GestorPackActivity.class);
        startActivity(intent);
    }

    private void iniciarGestorMaterias() {
        Intent intent = new Intent(this, GestorMateriaActivity.class);
        startActivity(intent);
    }

    private void iniciarGestorApuntes() {
        Intent intent = new Intent(this, GestorDeApuntes.class);
        intent.putExtra("user", (Serializable) user);
        startActivity(intent);
    }

    public void informar(String frase) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(frase);
        alertDialog.show();
    }
}
