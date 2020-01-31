package com.example.reto1_android.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;

/**
 * El adaptador de los apuntes para la tienda y gestion de apuntes.
 * @author Ricardo Peinado Lastra
 */
public class Adaptador extends BaseAdapter {
    private static LayoutInflater inflater = null;
    //El contorno para poder generar
    Context contexto;
    //es una matriz
    ApunteBean apuntes[];

    public Adaptador(Context contexto, ApunteBean[] apuntes) {
        this.contexto = contexto;
        this.apuntes = apuntes;
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Cuanta la cantidad de apuntes.
     * @return
     */
    @Override
    public int getCount() {
        if (apuntes != null)
            return apuntes.length;
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Los componentes de la vista.
     * @param i El indice.
     * @param convertView la vista.
     * @param parent El padre de la vista.
     * @return
     */
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.list_item_apunte_tienda, null);

        TextView titulo = (TextView) vista.findViewById(R.id.li_titulo);
        TextView desc = (TextView) vista.findViewById(R.id.li_desc);
        TextView materia = (TextView) vista.findViewById(R.id.li_materia);
        TextView precio = (TextView) vista.findViewById(R.id.li_precio);
        TextView creador = (TextView) vista.findViewById(R.id.li_creador);

        titulo.setText(apuntes[i].getTitulo());
        desc.setText(apuntes[i].getDescripcion());
        materia.setText(apuntes[i].getMateria().getTitulo());
        precio.setText(apuntes[i].getPrecio() + "€");
        creador.setText(apuntes[i].getCreador().getNombreCompleto());


        return vista;
    }
}
