package com.example.reto1_android.Activity;

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

public class InsertarOfertaActivity extends AppCompatActivity {
    private Date fecha;
    private SimpleDateFormat formateador = new SimpleDateFormat("yyyy/MM/dd");
    private OfertaBean oferta = new OfertaBean();
    private Button btnInsertarOferta;
    private EditText txtTitulo;
    private EditText txtRebaja;
    private EditText dateInicio;
    private EditText dateFin;
    private static final Logger LOGGER = Logger.getLogger(GestorOferta.class.getName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_oferta);

        txtTitulo = findViewById(R.id.txtTituloAModificar);
        txtRebaja = findViewById(R.id.txtRebajaAModificar);
        dateInicio = findViewById(R.id.dateInicioAModificar);
        dateFin = findViewById(R.id.dateFinAModificar);
    }
    public void onClickInsertarOferta(View view) {
        dateInicio.setTextColor(Color.BLACK);
        dateFin.setTextColor(Color.BLACK);
        LOGGER.info("he clicado");
        if(txtRebaja.getText().toString().trim().isEmpty()||txtTitulo.getText().toString().trim().isEmpty()||dateInicio.getText().toString().trim().isEmpty()||dateFin.getText().toString().trim().isEmpty()){
            Toast.makeText(InsertarOfertaActivity.this,"Falta un campo por rellenar",Toast.LENGTH_SHORT).show();
        }
        else{
            if(dateInicio.getText().toString().trim().length()==10&&dateFin.getText().toString().trim().length()==10){
                dateInicio.setTextColor(Color.BLACK);
                dateFin.setTextColor(Color.BLACK);GestorOfertaManager ofertaManager = GestorOfertaAPIClient.getClient();
                oferta.setTitulo(txtTitulo.getText().toString().trim());
                oferta.setRebaja(Float.valueOf(txtRebaja.getText().toString().trim()));
                try {
                    fecha = formateador.parse(dateInicio.getText().toString().trim());
                } catch (ParseException e) {
                    Toast.makeText(InsertarOfertaActivity.this,"Fecha Erronea",Toast.LENGTH_SHORT).show();
                }
                oferta.setFechaInicio(fecha.toString());
                try {
                    fecha = formateador.parse(dateFin.getText().toString().trim());
                } catch (ParseException e) {
                    Toast.makeText(InsertarOfertaActivity.this,"Fecha Erronea",Toast.LENGTH_SHORT).show();
                }
                oferta.setFechaFin(fecha.toString());
                Call<Void> ofes = ofertaManager.createOferta(oferta);
                ofes.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call2, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InsertarOfertaActivity.this,"Oferta insertada acecuadamente",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(InsertarOfertaActivity.this,"Oferta no se pudo insertar",Toast.LENGTH_SHORT).show();
                            LOGGER.info(oferta.getFechaInicio().toString());
                            LOGGER.info(oferta.getFechaFin().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call2, Throwable t) {
                        LOGGER.severe("vaya ha ocurrido algo:--->" + t.getMessage());
                    }
                 });
            }
            else{
                if(dateInicio.getText().length()!=10)
                    dateInicio.setTextColor(Color.RED);
                if(dateFin.getText().length()!=10)
                    dateFin.setTextColor(Color.RED);
             }

        }
    }
    public void onClickVolver(View v) {
        this.finish();
    }
}
