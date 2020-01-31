package com.example.reto1_android.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ApuntePackBean;
import com.example.reto1_android.model.ApuntesBeans;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.OfertaBean;
import com.example.reto1_android.model.PackBean;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;
import com.example.reto1_android.retrofit.PackAPIClient;
import com.example.reto1_android.retrofit.PackManager;
import com.example.reto1_android.ui.CompraPackActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackTiendaAdapter extends RecyclerView.Adapter<PackTiendaAdapter.PackTiendaHolder> {
    private List<PackBean> packs;
    private Context context;
    private ClienteBean cliente;
    private float precio = 0;

    public PackTiendaAdapter(Context context, List<PackBean> packs, ClienteBean cliente){
        this.context = context;
        this.packs = packs;
        this.cliente = cliente;
    }

    @NonNull
    @Override
    public PackTiendaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_pack_tienda, parent, false);
        return new PackTiendaAdapter.PackTiendaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PackTiendaHolder holder, final int position) {
        if(packs != null){
            PackBean pack = packs.get(position);

            holder.tvTitulo.setText(pack.getTitulo());
            holder.tvDescripcion.setText(pack.getDescripcion());
            PackManager manager = PackAPIClient.getClient();

            Call<OfertaBean> call = manager.getOferta(pack.getIdPack());

            call.enqueue(new Callback<OfertaBean>() {
                @Override
                public void onResponse(Call<OfertaBean> call, Response<OfertaBean> response) {
                    if(response.isSuccessful()){
                        if(response.body() != null){
                            precio = pack.getPrecio() * (1 - (response.body().getRebaja() / 100));
                            holder.tvPrecio.setText(precio +"€");
                        }else{
                            precio = pack.getPrecio();
                            holder.tvPrecio.setText(precio + "€");
                        }
                    }else{
                        showErrorAlert(context.getString(R.string.error));
                    }
                }

                @Override
                public void onFailure(Call<OfertaBean> call, Throwable t) {
                    showErrorAlert(context.getString(R.string.error));
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Set<ApunteBean> apuntesCompEnPack = new HashSet<ApunteBean>();

                    ApunteManager manager = ApunteAPIClient.getClient();

                    Call<ApuntesBeans> call = manager.getApuntesByComprador(cliente.getId());

                    call.enqueue(new Callback<ApuntesBeans>() {
                        @Override
                        public void onResponse(Call<ApuntesBeans> call, Response<ApuntesBeans> response) {
                            if(response.isSuccessful()){
                                Set<ApunteBean> apuntes = response.body().getApuntes();
                                if(apuntes != null){
                                    for(ApunteBean a : apuntes){
                                        for(ApuntePackBean ap : pack.getApuntes()){
                                            if(a.getIdApunte() == ap.getIdApunte()){
                                                apuntesCompEnPack.add(a);
                                            }
                                        }
                                    }
                                    if(apuntesCompEnPack.size() != 0 ){
                                        for(ApunteBean a : apuntesCompEnPack){
                                            if(pack.getApuntes().contains(a)){
                                                pack.getApuntes().remove(a);
                                            }
                                        }
                                    }
                                }
                                if(pack.getApuntes().size() == 0){
                                    showErrorAlert(context.getString(R.string.tienesTodos));
                                }else{
                                    Intent i = new Intent(context, CompraPackActivity.class);
                                    i.putExtra("pack", pack);
                                    i.putExtra("cliente", cliente);
                                    context.startActivity(i);
                                }
                            }else{
                                showErrorAlert(context.getString(R.string.error));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApuntesBeans> call, Throwable t) {
                            showErrorAlert(context.getString(R.string.error));
                        }
                    });
                }
            });
        }
        else{
            Toast.makeText(context, "No hay packs", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return packs.size();
    }

    public void setListaPacks(List<PackBean> listaPacks){
        this.packs = listaPacks;
        notifyDataSetChanged();
    }

    private void showErrorAlert(String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(message)
                .show();
    }

    class PackTiendaHolder extends RecyclerView.ViewHolder{
        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        private final TextView tvPrecio;
        public PackTiendaHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloPackTiendaAdapter);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPackTiendaAdapter);
            tvPrecio = itemView.findViewById(R.id.tvPrecioPackTiendaAdapter);
        }
    }
}
