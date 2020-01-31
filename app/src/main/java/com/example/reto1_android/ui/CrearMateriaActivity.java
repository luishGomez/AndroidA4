package com.example.reto1_android.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.retrofit.MateriaAPIClient;
import com.example.reto1_android.retrofit.MateriaManager;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.reto1_android.ui.Util.isNetworkAvailable;
import static com.example.reto1_android.ui.Util.showErrorInternetAlert;

/**
 * Clase que maneja el activity GestorMateria.
 */
public class CrearMateriaActivity  extends AppCompatActivity {

    EditText etTitulo;
    EditText etDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_materia);

        etTitulo = findViewById(R.id.etTituloMateriaCrearMateria);
        etDescripcion = findViewById(R.id.etDescripcionMateriaCrearMateria);
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnCrearMateria.
     * @param v Vista que lanza la acción.
     */
    public void btnCrearMateria(View v){
        if(etTitulo.getText().length()>0 && etDescripcion.getText().length()>0){
            if(isNetworkAvailable(this)){
                MateriaBean materiaBean = new MateriaBean(0, etTitulo.getText().toString(), etDescripcion.getText().toString());

                MateriaManager manager = MateriaAPIClient.getClient();

                Call<Void> call = manager.create(materiaBean);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            showSuccessAlert(getString(R.string.materiaCreate));
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
