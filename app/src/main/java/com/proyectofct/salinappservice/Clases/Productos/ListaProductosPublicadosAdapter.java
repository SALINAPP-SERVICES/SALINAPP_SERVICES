package com.proyectofct.salinappservice.Clases.Productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.proyectofct.salinappservice.BienvenidaActivity;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.util.ArrayList;

import static com.proyectofct.salinappservice.Clases.Productos.ProductoPublicadoViewHolder.EXTRA_OBJETO_GRUPO_PRODUCTO_PUBLICADO;
import static com.proyectofct.salinappservice.Utilidades.ImagenesBlobBitmap.blob_to_bitmap;


public class ListaProductosPublicadosAdapter extends RecyclerView.Adapter<ProductoPublicadoViewHolder> {
    private Context c;
    private ArrayList<ProductosPublicados> listaProductosPublicados;
    private LayoutInflater mInflater;
    private int página;
    private FirebaseAuth firebaseAuth;

    public ListaProductosPublicadosAdapter(Context c, ArrayList<ProductosPublicados> listaProductosPublicados) {
        this.c = c;
        this.listaProductosPublicados = listaProductosPublicados;
        mInflater = LayoutInflater.from(c);
        this.página = 0;
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public ArrayList<ProductosPublicados> getListaProductosPublicados() {
        return listaProductosPublicados;
    }

    public void setListaProductosPublicados(ArrayList<ProductosPublicados> listaProductosPublicados) {
        this.listaProductosPublicados = listaProductosPublicados;
    }

    @NonNull
    @Override
    public ProductoPublicadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView;
        firebaseAuth = FirebaseAuth.getInstance();

        String cod_empresa="";
        if(listaProductosPublicados.size()>0){
            cod_empresa = listaProductosPublicados.get(0).getE().getCod_empresa();
        }

        if(!firebaseAuth.getCurrentUser().getEmail().equals(cod_empresa)){
            mItemView = mInflater.inflate(R.layout.item_recyclerview_producto_publicado, parent, false);
        } else{
            mItemView = mInflater.inflate(R.layout.item_recyclerview_imagenes_producto, parent, false);
        }

        return new ProductoPublicadoViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoPublicadoViewHolder holder, int position) {
        ProductosPublicados productosPublicadosActual = listaProductosPublicados.get(position);
        if(!firebaseAuth.getCurrentUser().getEmail().equals(listaProductosPublicados.get(position).getE().getCod_empresa())) {
            holder.txtMarcaProductoPublicado.setText(String.valueOf("Marca : " + productosPublicadosActual.getP().getMarca()));
            holder.txtPrecioProductoPublicado.setText(String.valueOf("Precio : " + productosPublicadosActual.getPrecioventa() + " €"));
            holder.txtStockProductoPublicado.setText(String.valueOf("Cantidad : " + productosPublicadosActual.getCantidad() + " unidades"));
            holder.txtDescripciónProductoPublicado.setText(String.valueOf("Descripción : " + productosPublicadosActual.getP().getDescripción()));
        }
        holder.txtModeloProductoPublicado.setText(String.valueOf("Modelo : " + productosPublicadosActual.getP().getModelo()));

        if(productosPublicadosActual.getP().getImagen() == null){
            holder.imgProductoPublicado.setImageResource(R.drawable.producto);
        } else{
            holder.imgProductoPublicado.setImageBitmap(blob_to_bitmap(productosPublicadosActual.getP().getImagen(), ConfiguracionesGeneralesDB.ANCHO_FOTO, ConfiguracionesGeneralesDB.ALTO_FOTO));

        }
    }

    public ProductosPublicados getProductoPublicado(int position){
        ProductosPublicados pp = listaProductosPublicados.get(position);
        return pp;
    }

    @Override
    public int getItemCount() {
        if (listaProductosPublicados != null) {
            return listaProductosPublicados.size();
        } else {
            return 0;
        }
    }
}