package com.proyectofct.salinappservice.Clases.Productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.util.ArrayList;

import static com.proyectofct.salinappservice.Utilidades.ImagenesBlobBitmap.blob_to_bitmap;

public class ListaImagenesProductosAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<ProductosPublicados> listaProductosPublicados;

    public ListaImagenesProductosAdapter(Context c,ArrayList<ProductosPublicados> listaProductosPublicados){
        this.c = c;
        this.listaProductosPublicados = listaProductosPublicados;
    }

    @Override
    public int getCount() {
        return listaProductosPublicados.size();
    }

    @Override
    public Object getItem(int position) {
        return listaProductosPublicados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_recyclerview_producto_publicado, null);
        }

        return convertView;
    }

    public void onBindViewHolder(@NonNull ProductoPublicadoViewHolder holder, int position) {
        ProductosPublicados productosPublicadosActual = listaProductosPublicados.get(position);
        if(productosPublicadosActual.getP().getImagen() == null){
            holder.imgProductoPublicado.setImageResource(R.drawable.producto);
        } else{
            holder.imgProductoPublicado.setImageBitmap(blob_to_bitmap(productosPublicadosActual.getP().getImagen(), ConfiguracionesGeneralesDB.ANCHO_FOTO, ConfiguracionesGeneralesDB.ALTO_FOTO));

        }
    }
}
