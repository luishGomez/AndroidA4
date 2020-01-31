package com.example.reto1_android.adapter;


import android.app.Activity;
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
import com.example.reto1_android.model.ApuntePackBean;

import java.util.List;

public class ApunteCompraPackAdapter extends RecyclerView.Adapter<ApunteCompraPackAdapter.ApuntePackHolder> {
    private List<ApuntePackBean> apuntes;
    private Context context;

    public ApunteCompraPackAdapter(Context context, List<ApuntePackBean> apuntes){
        this.context = context;
        this.apuntes = apuntes;
    }

    @NonNull
    @Override
    public ApuntePackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_apunte_pack, parent, false);
        return new ApunteCompraPackAdapter.ApuntePackHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApuntePackHolder holder, final int position) {
        if(apuntes != null){
            ApuntePackBean apunte = apuntes.get(position);

            holder.tvTitulo.setText(apunte.getTitulo());
        }
        else{
            Toast.makeText(context, "No hay apuntes", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return apuntes.size();
    }

    class ApuntePackHolder extends RecyclerView.ViewHolder{
        private final TextView tvTitulo;
        public ApuntePackHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloApunteAdapterPack);
        }
    }
}
