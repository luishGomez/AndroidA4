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
import com.example.reto1_android.adapter.MateriaAdapter;
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.model.MateriasBeans;
import com.example.reto1_android.retrofit.MateriaAPIClient;
import com.example.reto1_android.retrofit.MateriaManager;
import java.util.ArrayList;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.reto1_android.ui.Util.isNetworkAvailable;
import static com.example.reto1_android.ui.Util.showErrorInternetAlert;


/**
 * Clase que maneja el activity ListadoMaterias.
 */
public class ListadoMateriasActivity extends AppCompatActivity {

    private List<MateriaBean> materia = new ArrayList<MateriaBean>();
    private List<MateriaBean> materiasFiltradas;
    private RecyclerView recycler;
    private MateriaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestor_materia_listado);

        recycler = findViewById(R.id.rvMaterias);
        getMaterias();
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
                if(s.isEmpty()){
                    adapter.setListaMaterias(materia);
                }else{
                    arrayFilter(s);
                }
                return true;
            }
        });
        return true;
    }

    /**
     * Método que carga todas las materias.
     */
    private void getMaterias(){
        if(isNetworkAvailable(this)){
            MateriaManager manager = MateriaAPIClient.getClient();

            Call<MateriasBeans> call = manager.findAll();

            call.enqueue(new Callback<MateriasBeans>() {
                @Override
                public void onResponse(Call<MateriasBeans> call, Response<MateriasBeans> response) {
                    if(response.isSuccessful()){
                        materia.addAll(response.body().getMaterias());
                        cargarRecycler();
                    }else{
                        showErrorAlert(getString(R.string.error));
                    }
                }

                @Override
                public void onFailure(Call<MateriasBeans> call, Throwable t) {
                    showErrorAlert(getString(R.string.error));
                }
            });
        }else{
            showErrorInternetAlert(this);
        }
    }

    /**
     * Método que carga todas las materias en el adaptador del recyclerView.
     */
    private void cargarRecycler(){
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MateriaAdapter(this, materia, this);
        recycler.setFocusable(false);
        recycler.setAdapter(adapter);
    }

    /**
     * Método que filtra las materias según el parámetro.
     * @param filtrado Cadena de caracteres para filtrar.
     */
    private void arrayFilter(String filtrado){
        materiasFiltradas = new ArrayList<>(0);
        materiasFiltradas.clear();
        if(filtrado.length() == 0){
            materiasFiltradas = materia;
        }else{
            for(MateriaBean m : materia){
                if(m.getTitulo().toLowerCase() .contains(filtrado.toLowerCase())){
                    materiasFiltradas.add(m);
                }
            }
        }
        adapter.setListaMaterias(materiasFiltradas);
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
}
