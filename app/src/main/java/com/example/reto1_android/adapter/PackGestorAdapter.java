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
import com.example.reto1_android.model.PackBean;
import com.example.reto1_android.ui.ModificarPackActivity;

import java.util.Date;
import java.util.List;

public class PackGestorAdapter extends RecyclerView.Adapter<PackGestorAdapter.MateriaHolder> {
    private List<PackBean> packs;
    private Context context;
    private Activity activity;

    public PackGestorAdapter(Context context, List<PackBean> packs, Activity activity){
        this.context = context;
        this.packs = packs;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MateriaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_pack, parent, false);
        return new PackGestorAdapter.MateriaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MateriaHolder holder, final int position) {
        if(packs != null){
            PackBean pack = packs.get(position);

            holder.tvTitulo.setText(pack.getTitulo());
            holder.tvDescripcion.setText(pack.getDescripcion());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ModificarPackActivity.class);
                    intent.putExtra("pack", pack);
                    Date d = new Date();
                    System.out.println(d);
                    System.out.println(pack.getFechaModificacion());
                    context.startActivity(intent);
                    activity.onBackPressed();
                }
            });
        }
        else{
            Toast.makeText(context, "No hay packBeans", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return packs.size();
    }

    public void setListaPacks(List<PackBean> listaPackBeans){
        this.packs = listaPackBeans;
        notifyDataSetChanged();
    }

    class MateriaHolder extends RecyclerView.ViewHolder{
        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        public MateriaHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloPackAdapter);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPackAdapter);
        }
    }
}
