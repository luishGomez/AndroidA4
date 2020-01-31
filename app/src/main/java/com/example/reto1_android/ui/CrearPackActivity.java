package com.example.reto1_android.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.PackBean;
import com.example.reto1_android.retrofit.PackAPIClient;
import com.example.reto1_android.retrofit.PackManager;

import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.reto1_android.ui.Util.isNetworkAvailable;
import static com.example.reto1_android.ui.Util.showErrorInternetAlert;

public class CrearPackActivity extends AppCompatActivity {

    EditText etTitulo;
    EditText etDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_pack);

        etTitulo = findViewById(R.id.etTituloCrearPack);
        etDescripcion = findViewById(R.id.etDescripcionCrearPack);
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnCrearPack.
     * @param v Vista que lanza la acción.
     */
    public void btnCrearPack(View v){
        if(etTitulo.getText().length()>0 && etDescripcion.getText().length()>0){
            if(isNetworkAvailable(this)){
                PackBean pack = new PackBean();
                pack.setIdPack(0);
                pack.setTitulo(etTitulo.getText().toString().trim());
                pack.setDescripcion(etDescripcion.getText().toString().trim());
                //pack.setFechaModificacion(new Date().toString());

                PackManager manager = PackAPIClient.getClient();

                Call<Void> call = manager.create(pack);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            showSuccessAlert(getString(R.string.packCreate));
                        }else{
                            showErrorAlert(getString(R.string.error));
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        showErrorAlert(getString(R.string.error));
                    }
                });
            }else{
                showErrorInternetAlert(this);
            }
        }else{
            showErrorAlert(getString(R.string.rellenarCampos));
        }
    }

    /**
     * Alert que se muestra cuando ocurre un error.
     * @param message Mensaje del alert.
     */
    private void showErrorAlert(String message){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(message)
                .show();
    }

    /**
     * Alert que se muestra cuando algo haya ocurrido bien.
     * @param message Mensaje del alert.
     */
    private void showSuccessAlert(String message){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(message)
                .setConfirmText(getString(R.string.btnOk))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        onBackPressed();
                    }
                }).show();
    }
}
