package com.example.reto1_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.reto1_android.R;

import clases.User;

/**
 * Principal Activity.
 * @author Luis
 */
public class PrincipalActivity extends AppCompatActivity {
    TextView tvBienvenida;
    TextView tvBienvenida2;

    /**
     * Al crear la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        tvBienvenida = findViewById(R.id.tvBienvenida);
        tvBienvenida2 = findViewById(R.id.tvBienvenida2);

        Intent intent = getIntent();
        User usuario = (User) intent.getSerializableExtra("user");
        tvBienvenida.setText(usuario.getFullname());
        tvBienvenida2.setText(usuario.getEmail());
    }
}
