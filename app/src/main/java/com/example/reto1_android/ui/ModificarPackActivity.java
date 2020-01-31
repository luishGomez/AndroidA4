package com.example.reto1_android.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.PackBean;
import com.example.reto1_android.retrofit.PackAPIClient;
import com.example.reto1_android.retrofit.PackManager;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.reto1_android.ui.Util.isNetworkAvailable;
import static com.example.reto1_android.ui.Util.showErrorInternetAlert;

public class ModificarPackActivity extends AppCompatActivity {

    PackBean pack;
    EditText etTitulo;
    EditText etDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_pack);

        pack = (PackBean) getIntent().getSerializableExtra("pack");

        etTitulo = findViewById(R.id.etTituloPackModificarPack);
        etDescripcion = findViewById(R.id.etDescripcionPackModificarPack);

        etTitulo.setText(pack.getTitulo());
        etDescripcion.setText(pack.getDescripcion());
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnModificarPack.
     * @param v Vista que lanza la acción.
     */
    public void btnModificarPack(View v){
        if(isNetworkAvailable(this)) {
            if(!(pack.getTitulo().equals(etTitulo.getText().toString().trim()) && pack.getDescripcion().equals(etDescripcion.getText().toString().trim()))){
                pack.setTitulo(etTitulo.getText().toString().trim());
                pack.setDescripcion(etDescripcion.getText().toString().trim());

                PackManager manager = PackAPIClient.getClient();

                Call<Void> call = manager.edit(pack);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            showSuccessAlert(getString(R.string.packModif));
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
                showSuccessAlert(getString(R.string.packModif));
            }
        }else{
            showErrorInternetAlert(this);
        }
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnEliminarPack.
     * @param v Vista que lanza la acción.
     */
    public void btnEliminarPack(View v){
        if(isNetworkAvailable(this)) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.alertConfirmationDeleteMateria))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            PackManager manager = PackAPIClient.getClient();

                            Call<Void> call = manager.remove(pack.getIdPack());

                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()){
                                        showSuccessAlert(getString(R.string.packElim));
                                    }else{
                                        showErrorAlert(getString(R.string.error));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    showErrorAlert(getString(R.string.error));
                                }
                            });
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelButton(getString(R.string.btnCancelar), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    }).show();
        }else{
            showErrorInternetAlert(this);
        }
    }

    /**
     * Alert que se muestra cuando ocurre un error.
     * @param message Mensaje del alert.
     */
    private void showErrorAlert(String message){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(message)
                .setCancelText(getString(R.string.btnOk))
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
