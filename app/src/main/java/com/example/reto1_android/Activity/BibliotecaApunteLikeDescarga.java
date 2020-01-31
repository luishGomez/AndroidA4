package com.example.reto1_android.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.reto1_android.R;
import com.example.reto1_android.exceptions.BusinessLogicException;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.ClienteBeans;
import com.example.reto1_android.model.ClientesBeans;
import com.example.reto1_android.model.OfertaBeans;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BibliotecaApunteLikeDescarga extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(BibliotecaApunteLikeDescarga.class.getName());
    private int STORAGE_PERMISSION_CODE = 1;
    Boolean votado = false;
    private TextView lblApunte;
    private TextView txtBibliotecaDescripcion;
    private Button btnLike;
    private Button btnDisike;
    private ClienteBean user;
    private ApunteBean apunte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca_like_descarga);

        lblApunte = findViewById(R.id.lblApunte);
        txtBibliotecaDescripcion = findViewById(R.id.txtBibliotecaDescripcion);
        btnLike = findViewById(R.id.btnLike);
        btnDisike = findViewById(R.id.btnDislike);

        Intent intent = getIntent();
        apunte = (ApunteBean) intent.getSerializableExtra("apunte");
        user = (ClienteBean) intent.getSerializableExtra("cliente");
        txtBibliotecaDescripcion.setText(apunte.getDescripcion());
        lblApunte.setText(apunte.getTitulo());

        ClienteManager clienteManager = ClienteAPIClient.getClient();
        Call<ClienteBeans> clientes=clienteManager.getVotantesId(apunte.getIdApunte());
        clientes.enqueue(new Callback<ClienteBeans>() {
            @Override
            public void onResponse(Call<ClienteBeans> call2, Response<ClienteBeans> response) {
                if (response.isSuccessful()) {
                    if(response!=null){
                        Set<ClienteBean> votantes = response.body().getClientes();
                        if(votantes!=null){
                            for(ClienteBean cliente:votantes){
                                if(cliente.getId().equals(user.getId())){
                                    votado = true;
                                    btnLike.setEnabled(false);
                                    btnDisike.setEnabled(false);
                                }
                            }
                        }
                    }
                } else {
                    LOGGER.severe("No se ha podido comprobar votacion.");
                }
            }
            @Override
            public void onFailure(Call<ClienteBeans> call2, Throwable t) {
                LOGGER.severe("vaya ha ocurrido algo:--->" + t.getMessage());
            }
        });

    }
    /**
     * Descarga el apunte de la BBDD.
     */
    private void descargarApunte() {
        ApunteManager apunteManager = ApunteAPIClient.getClientText();
        Call<String> call = apunteManager.getArchivoDelApunte(apunte.getIdApunte());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String pathDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "";
                    Date now = new Date();
                    pathDescargas = pathDescargas + "/" + apunte.getTitulo() + "D" + now.getDay() + "M" + now.getMonth() + "Y" + now.getYear() + "M" + now.getMinutes() + "S" + now.getSeconds() + ".pdf";
                    FileOutputStream fileOuputStream = null;
                    File file= new File(pathDescargas);
                    try {
                        fileOuputStream = new FileOutputStream(file.getPath());
                        fileOuputStream.write(hexStringToByteArray(response.body()));
                       // sacarInformacion(getResources().getString(R.string.g9));
                    } catch (IOException e) {
                        LOGGER.severe("Error al descargar el apunte en un fichero: " + e.getMessage());
                       // informar(getResources().getString(R.string.gda1));
                    } finally {
                        if (fileOuputStream != null) {
                            try {
                                fileOuputStream.close();
                            } catch (IOException e) {
                                LOGGER.severe("Error al intentar cerrar el stream para descargar el fichero: " + e.getMessage());
                               // informar(getResources().getString(R.string.gda1));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    /**
     * Convierte un texto en hexadeciaml en una lista de bytes.
     * @param s El texto en hexadecimal.
     * @return La coleccion en bytes.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    /**
     * Saca por patana una información.
     * @param mensaje El mensaje a transmitir
     */
    private void sacarInformacion(String mensaje) {
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
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

    /**
     * Cliente vota (Like) al apunte q ya haya comprada y este sin votar.
     * @param view
     */
    public void onClickLike(View view){
        ApunteManager apunteManager = ApunteAPIClient.getClient();
        Call<Void> votante = apunteManager.votacion(user.getId(),1,apunte);
        votante.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BibliotecaApunteLikeDescarga.this,"Se ha votado correctamente.",Toast.LENGTH_SHORT).show();
                    btnLike.setEnabled(false);
                    btnDisike.setEnabled(false);
                }else
                    Toast.makeText(BibliotecaApunteLikeDescarga.this,"No se ha podido votar correctamente.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LOGGER.severe("vaya ha ocurrido algo:--->"+t.getMessage());
            }
        });
    }
    /**
     * Cliente vota (Dislike) al apunte q ya haya comprada y este sin votar.
     * @param view
     */
    public void onClickDislike(View view){
        ApunteManager apunteManager = ApunteAPIClient.getClient();
        Call<Void> votante = apunteManager.votacion(user.getId(),-1,apunte);
        votante.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BibliotecaApunteLikeDescarga.this,"Se ha votado correctamente.",Toast.LENGTH_SHORT).show();
                    btnLike.setEnabled(false);
                    btnDisike.setEnabled(false);
                }else
                    Toast.makeText(BibliotecaApunteLikeDescarga.this,"No se ha podido votar correctamente.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LOGGER.severe("vaya ha ocurrido algo:--->"+t.getMessage());
            }
        });
    }
    /**
     * Verifica que el ciente ya haya dado permisos de escritura en el dispositivo a la aplicacioon,
     * caso contrario se le pedira dichos permisos (requestStoragePermission())
     * @param view
     */
    public void onClickDescargaBiblioteca (View view){
        if (ContextCompat.checkSelfPermission(BibliotecaApunteLikeDescarga.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(BibliotecaApunteLikeDescarga.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
            descargarApunte();
        } else {
            requestStoragePermission();
        }
    }
    /**
     * Permiso necesario para descargar apunte.
     */
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permiso necesario")
                    .setMessage("Este permiso es necesario para descargar el apunte.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BibliotecaApunteLikeDescarga.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    /**
     * Regresa al intent anterior.
     * @param view
     */
    public void onClickVolverBiblioteca(View view){this.finish();}
}
