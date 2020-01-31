package com.example.reto1_android.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto1_android.R;
import com.example.reto1_android.adapter.PackTiendaAdapter;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.PackBean;
import com.example.reto1_android.model.PacksBeans;
import com.example.reto1_android.retrofit.PackAPIClient;
import com.example.reto1_android.retrofit.PackManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.reto1_android.ui.Util.isNetworkAvailable;
import static com.example.reto1_android.ui.Util.showErrorInternetAlert;

public class ListadoTiendaPackActivity extends AppCompatActivity {

    private List<PackBean> pack;
    private List<PackBean> packsFiltrados;
    private RecyclerView recycler;
    private PackTiendaAdapter adapter;
    private ClienteBean cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_pack_tienda);

        cliente = (ClienteBean) getIntent().getSerializableExtra("cliente");

        recycler = findViewById(R.id.rvTiendaPack);
        getPacks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    adapter.setListaPacks(pack);
                } else {
                    arrayFilter(s);
                }
                return true;
            }
        });
        return true;
    }

    /**
     * Método que carga todos los packs.
     */
    private void getPacks() {
        if (isNetworkAvailable(this)) {
            PackManager manager = PackAPIClient.getClient();

            Call<PacksBeans> call = manager.findAll();

            call.enqueue(new Callback<PacksBeans>() {
                @Override
                public void onResponse(Call<PacksBeans> call, Response<PacksBeans> response) {
                    if (response.isSuccessful()) {
                        pack = response.body().getPacks();
                        cargarRecycler();
                    } else {
                        showErrorAlert(getString(R.string.error));
                    }
                }

                @Override
                public void onFailure(Call<PacksBeans> call, Throwable t) {
                    showErrorAlert(getString(R.string.error));
                }
            });
        } else {
            showErrorInternetAlert(this);
        }
    }

    /**
     * Método que carga todos los packs en el adaptador del recyclerView.
     */
    private void cargarRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PackTiendaAdapter(this, pack, cliente);
        recycler.setFocusable(false);
        recycler.setAdapter(adapter);
    }

    /**
     * Método que filtra los packs según el parámetro.
     *
     * @param filtrado Cadena de caracteres para filtrar.
     */
    private void arrayFilter(String filtrado) {
        packsFiltrados = new ArrayList<>(0);
        packsFiltrados.clear();
        if (filtrado.length() == 0) {
            packsFiltrados = pack;
        } else {
            for (PackBean p : pack) {
                if (p.getTitulo().toLowerCase().contains(filtrado.toLowerCase())) {
                    packsFiltrados.add(p);
                }
            }
        }
        adapter.setListaPacks(packsFiltrados);
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
