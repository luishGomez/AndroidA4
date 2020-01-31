package com.example.reto1_android.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;

import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.round;

/**
 * Visualiza la la compra de un apunte.
 * @author Ricardo Peinado Lastra
 */
public class VisorApunte extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(VisorApunteEditableAdmin.class.getName());
    private TextView titulo;
    private TextView desc;
    private TextView precio;
    private TextView materia;
    private TextView tuSaldo;
    private TextView tuNuevoSaldo;
    private Button btncomprar;
    private ClienteBean cliente;
    private ApunteBean apunte;
    private float nuevoSaldo;
    private MediaPlayer mp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_apunte);

        titulo = (TextView)findViewById(R.id.va_titulo);
        desc = (TextView)findViewById(R.id.va_desc);
        precio = (TextView)findViewById(R.id.va_precio);
        materia = (TextView)findViewById(R.id.va_materia);
        btncomprar = (Button)findViewById(R.id.va_btnComprar);
        tuSaldo = (TextView)findViewById(R.id.va_tuSaldo);
        tuNuevoSaldo = (TextView)findViewById(R.id.va_nuevoSaldo);
        mp= MediaPlayer.create(this,R.raw.pulsar_boton);

        Intent intent = getIntent();
        cliente= (ClienteBean) intent.getSerializableExtra("cliente");
        apunte=(ApunteBean)intent.getSerializableExtra("apunte");
        titulo.setText(apunte.getTitulo());
        desc.setText(apunte.getDescripcion());
        precio.setText(apunte.getPrecio()+"€");
        tuSaldo.setText(cliente.getSaldo()+"€");
        nuevoSaldo=round((cliente.getSaldo()-apunte.getPrecio())*100);
        nuevoSaldo=nuevoSaldo/100;
        tuNuevoSaldo.setText(nuevoSaldo+"€");
        materia.setText(apunte.getMateria().getTitulo());
        btncomprar.setEnabled(nuevoSaldo>=0);

        btncomprar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                preguntarSiQuiereComprarElapunte();
            }
        });
    }

    /**
     * Pregunta si quiere comprar el apunte.
     */
    private void preguntarSiQuiereComprarElapunte() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.va1)+" " + apunte.getTitulo() + "?")//va1
                    .setPositiveButton(getResources().getString(R.string.vaea4), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            empiezaCompra();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.vaea5), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            AlertDialog alertaParaBorrar = builder.create();
            alertaParaBorrar.show();
        }catch(Exception e ){
            LOGGER.severe("Errror al intentar comprar: "+e.getMessage());
            informar(getResources().getString(R.string.gda1));

        }
    }

    /**
     * Empieza el proceso de la compra.
     */
    private void empiezaCompra() {

        try {

            ClienteManager clienteManager = ClienteAPIClient.getClient();
            Call<Void> call = clienteManager.comprarApunte(cliente, apunte.getIdApunte());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        cliente.setSaldo(nuevoSaldo);
                        actualizaElCliente(cliente);
                    } else {
                        LOGGER.severe("Error al comprar el apunte"+response.code());
                        informar(getResources().getString(R.string.gda1));
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    LOGGER.severe("FALLO al comprar el apunte" + t.getMessage());
                    informar(getResources().getString(R.string.gda2));
                }
            });
        } catch (Exception e) {
            LOGGER.severe("ERROR al comprar el apunte: "+e.getMessage());
            informar(getResources().getString(R.string.gda1));
        }



    }

    /**
     * Actualiza el cliente.
     * @param cliente Los datos del cliente.
     */
    private void actualizaElCliente(ClienteBean cliente) {
        try {
            ClienteManager clienteManager = ClienteAPIClient.getClient();
            Call<Void> call = clienteManager.edit(cliente);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        LOGGER.info("Cliente actualizado.");
                        cerrar();
                    }else{
                        LOGGER.severe("Fallo al actualizar la aplicación.");
                        informar(getResources().getString(R.string.gda1));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    LOGGER.severe("Error al actualizar el cliente.");
                    informar(getResources().getString(R.string.gda2));
                }
            });
        }catch(Exception e){
            LOGGER.severe("Error al refrescar el cliente: "+e.getMessage());
            informar(getResources().getString(R.string.gda1));
        }
    }

    /**
     * Cierra el activity.
     */
    private void cerrar() {
        Intent resultItem = new Intent();
        resultItem.putExtra("result", getResources().getString(R.string.va2)+": " + apunte.getTitulo());//va2
        setResult(RESULT_OK, resultItem);
        finish();
    }
    /**
     * Informa al usuario de algun error o fallo.
     *
     * @param frase La frase que le muestra al usuario.
     */
    public void informar(String frase) {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialog.setMessage(frase);
        alertDialog.show();
    }
}
