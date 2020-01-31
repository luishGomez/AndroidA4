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
import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.ui.ModificarMateriaActivity;

import java.util.List;

public class MateriaAdapter  extends RecyclerView.Adapter<MateriaAdapter.MateriaHolder> {
    private List<MateriaBean> materias;
    private Context context;
    private Activity activity;

    public MateriaAdapter(Context context, List<MateriaBean> materias, Activity activity){
        this.context = context;
        this.materias = materias;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MateriaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_materia, parent, false);
        return new MateriaAdapter.MateriaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MateriaHolder holder, final int position) {
        if(materias != null){
            MateriaBean materia = materias.get(position);

            holder.tvTitulo.setText(materia.getTitulo());
            holder.tvDescripcion.setText(materia.getDescripcion());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ModificarMateriaActivity.class);
                    intent.putExtra("materia", materia);
                    context.startActivity(intent);
                    activity.onBackPressed();
                }
            });
        }
        else{
            Toast.makeText(context, "No hay materias", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return materias.size();
    }

    public void setListaMaterias(List<MateriaBean> listaMateriaBeans){
        this.materias = listaMateriaBeans;
        notifyDataSetChanged();
    }

    class MateriaHolder extends RecyclerView.ViewHolder{
        private final TextView tvTitulo;
        private final TextView tvDescripcion;
        public MateriaHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloMateriaAdapter);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionMateriaAdapter);
        }
    }
}
