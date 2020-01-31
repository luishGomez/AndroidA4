package com.example.reto1_android.ui;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto1_android.R;
import com.example.reto1_android.adapter.ApunteCompraPackAdapter;
import com.example.reto1_android.model.ApuntePackBean;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.OfertaBean;
import com.example.reto1_android.model.PackBean;
import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;
import com.example.reto1_android.retrofit.PackAPIClient;
import com.example.reto1_android.retrofit.PackManager;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompraPackActivity  extends AppCompatActivity {

    private PackBean pack;
    private ClienteBean cliente;
    private float precio;
    private TextView tvTitulo;
    private TextView tvPrecio;
    private RecyclerView recycler;
    private ApunteCompraPackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_pack);

        pack = (PackBean) getIntent().getSerializableExtra("pack");
        cliente = (ClienteBean) getIntent().getSerializableExtra("cliente");

        tvTitulo = findViewById(R.id.tvTituloComprarPack);
        recycler = findViewById(R.id.rvApunteDePack);
        tvPrecio = findViewById(R.id.tvPrecioCompraPack);

        tvTitulo.setText(pack.getTitulo() + "\n" + pack.getDescripcion());
        cargarRecycler();
        PackManager manager = PackAPIClient.getClient();

        Call<OfertaBean> call = manager.getOferta(pack.getIdPack());

        call.enqueue(new Callback<OfertaBean>() {
            @Override
            public void onResponse(Call<OfertaBean> call, Response<OfertaBean> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        precio = pack.getPrecio() * (1 - (response.body().getRebaja() / 100));
                        tvPrecio.setText(precio +"€");
                    }else{
                        precio = pack.getPrecio();
                        tvPrecio.setText(precio + "€");
                    }
                }else{
                    showErrorAlert(getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<OfertaBean> call, Throwable t) {
                showErrorAlert(getString(R.string.error));
            }
        });

    }

    /**
     * Método que se ejecuta cuando se hace click en el botón btnComprarPack.
     * @param v Vista que lanza la acción.
     */
    public void btnComprarPack(View v){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.seguroComprarPack))
                .setContentText(getString(R.string.unaVezComp))
                .setCancelText(getString(R.string.btnCancelar))
                .setConfirmText(getString(R.string.btnSi))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        ClienteManager manager = ClienteAPIClient.getClient();

                        Call<Void> call = manager.comprarApunte(cliente, pack.getIdPack());

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    cliente.setSaldo(precio);

                                    Call<Void> call2 = manager.edit(cliente);

                                    call2.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful()){
                                                sDialog
                                                        .setTitleText(getString(R.string.packComprado))
                                                        .setConfirmText(getString(R.string.btnOk))
                                                        .setConfirmClickListener(null)
                                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
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
                                    showErrorAlert(getString(R.string.error));
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                showErrorAlert(getString(R.string.error));
                            }
                        });
                        sDialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Método que carga todos los packs en el adaptador del recyclerView.
     */
    private void cargarRecycler(){
        recycler.setLayoutManager(new LinearLayoutManager(this));
        List<ApuntePackBean> apuntes = new ArrayList<ApuntePackBean>();
        for(ApuntePackBean a : pack.getApuntes()){
            apuntes.add(a);
        }
        adapter = new ApunteCompraPackAdapter(this, apuntes);
        recycler.setFocusable(false);
        recycler.setAdapter(adapter);
    }

    /**
     * Alert que se muestra cuando ocurre un error.
     *
     * @param message Mensaje del alert.
     */
    private void showErrorAlert(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(message)
                .show();
    }
}
