package com.example.reto1_android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ApuntesBeans;
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.MateriaAPIClient;
import com.example.reto1_android.retrofit.MateriaManager;
import java.util.Set;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.reto1_android.ui.Util.isNetworkAvailable;
import static com.example.reto1_android.ui.Util.showErrorInternetAlert;

/**
 *
 */
public class ModificarMateriaActivity extends AppCompatActivity {

    MateriaBean materia;
    EditText etTitulo;
    EditText etDescripcion;
    Boolean materiaVacia = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_materia);

        materia = (MateriaBean) getIntent().getSerializableExtra("materia");

        etTitulo = findViewById(R.id.etTituloMateriaModificarMateria);
        etDescripcion = findViewById(R.id.etDescripcionMateriaModificarMateria);

        etTitulo.setText(materia.getTitulo());
        etDescripcion.setText(materia.getDescripcion());
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnModificarMateria.
     * @param v Vista que lanza la acción.
     */
    public void btnModificarMateria(View v){
        if(isNetworkAvailable(this)) {
            //TODO comprobar que no es igual y los limites de longitud.
            materia.setTitulo(etTitulo.getText().toString().trim());
            materia.setDescripcion(etDescripcion.getText().toString().trim());

            MateriaManager manager = MateriaAPIClient.getClient();

            Call<Void> call = manager.edit(materia);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        showSuccessAlert(getString(R.string.materiaModif));
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
    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnEliminarMateria.
     * @param v Vista que lanza la acción.
     */
    public void btnEliminarMateria(View v){
        if(isNetworkAvailable(this)) {
            comprobar();
        }else{
            showErrorInternetAlert(this);
        }
    }

    /**
     * Comprueba que la materia que se quiere eliminar no tenga asociada ningún apunte.
     */
    private void comprobar(){
        materiaVacia = true;
        ApunteManager manager = ApunteAPIClient.getClient();

        Call<ApuntesBeans> call = manager.findAll();

        call.enqueue(new Callback<ApuntesBeans>() {
            @Override
            public void onResponse(Call<ApuntesBeans> call, Response<ApuntesBeans> response) {
                if (response.isSuccessful()) {
                    Set<ApunteBean> apuntes = response.body().getApuntes();
                    for(ApunteBean a : apuntes){
                        if(a.getMateria().getIdMateria().equals(materia.getIdMateria())){
                            materiaVacia = false;
                            break;
                        }
                    }
                    if(materiaVacia){
                        eliminarMateria();
                    }else{
                        showErrorAlert(getString(R.string.noDeleteMateria));
                    }
                }else{
                    showErrorAlert(getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<ApuntesBeans> call, Throwable t) {
                showErrorAlert(t.getMessage());
            }
        });
    }

    /**
     * Método que pide la confirmación de eliminar la materia y si es afirmativo, la elimina.
     */
    private void eliminarMateria(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.alertConfirmationDeleteMateria))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        MateriaManager manager = MateriaAPIClient.getClient();

                        Call<Void> call = manager.remove(materia.getIdMateria());

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    showSuccessAlert(getString(R.string.materiaElim));
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
