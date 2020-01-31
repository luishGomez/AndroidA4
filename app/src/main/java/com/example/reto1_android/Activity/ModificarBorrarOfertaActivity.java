package com.example.reto1_android.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.OfertaBean;
import com.example.reto1_android.retrofit.GestorOfertaAPIClient;
import com.example.reto1_android.retrofit.GestorOfertaManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Modificar(actualizar) o borrar Oferta de la BBDD
 */
public class ModificarBorrarOfertaActivity extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(GestorOferta.class.getName());
    private OfertaBean oferta;
    private EditText txtTitulo;
    private EditText txtRebaja;
    private EditText dateInicio;
    private EditText dateFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_borrar_oferta);
        Intent intent = getIntent();
        oferta = (OfertaBean) intent.getSerializableExtra("oferta");
        txtTitulo = findViewById(R.id.txtTituloAModificar);
        txtRebaja = findViewById(R.id.txtRebajaAModificar);
        dateInicio = findViewById(R.id.dateInicioAModificar);
        dateFin = findViewById(R.id.dateFinAModificar);
        txtTitulo.setHint(oferta.getTitulo());
        txtRebaja.setHint(String.valueOf(oferta.getRebaja()));
        dateInicio.setHint(oferta.getFechaInicio().toString().substring(0,10));
        dateFin.setHint(oferta.getFechaFin().toString().substring(0,10));
    }

    /**
     * Borra oferta previamente seleccionada.
     * @param view
     */
    public void onClickborrarOferta(View view) {
            GestorOfertaManager ofertaManager = GestorOfertaAPIClient.getClient();
                Call<Void> ofes = ofertaManager.deleteOferta(oferta.getIdOferta());
                ofes.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call2, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ModificarBorrarOfertaActivity.this,"Oferta Borrada acecuadamente",Toast.LENGTH_SHORT).show();
                            ModificarBorrarOfertaActivity.this.finish();

                        } else {
                            Toast.makeText(ModificarBorrarOfertaActivity.this,"Oferta no se pudo Borrar la oferta de id:"+oferta.getIdOferta(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call2, Throwable t) {
                        LOGGER.severe("vaya ha ocurrido algo:--->" + t.getMessage());
                    }
                });
    }

    /**
     * Vuelve al intent anterior(GestorOferta).
     * @param v
     */
    public void onClickVolver(View v) {
        this.finish();
    }
}
