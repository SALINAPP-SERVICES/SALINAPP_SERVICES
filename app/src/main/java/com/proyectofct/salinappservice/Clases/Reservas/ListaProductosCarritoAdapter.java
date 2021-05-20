package com.proyectofct.salinappservice.Clases.Reservas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.proyectofct.salinappservice.Clases.Productos.ProductoCarrito;
import com.proyectofct.salinappservice.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListaProductosCarritoAdapter extends RecyclerView.Adapter<ProductoCarritoViewHolder> {
    private Context c;
    private ArrayList<ProductoCarrito> listaProductosCarrito;
    private LayoutInflater inflater;

    public ListaProductosCarritoAdapter(Context c, ArrayList<ProductoCarrito> listaProductosCarrito) {
        this.c = c;
        this.listaProductosCarrito = listaProductosCarrito;
        inflater = LayoutInflater.from(c);
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public ArrayList<ProductoCarrito> getListaProductosCarrito() {
        return listaProductosCarrito;
    }

    public void setListaProductosCarrito(ArrayList<ProductoCarrito> listaProductosCarrito) {
        this.listaProductosCarrito = listaProductosCarrito;
    }

    @NonNull
    @Override
    public ProductoCarritoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View item = inflater.inflate(R.layout.item_recyclerview_producto_carrito, parent, false);
        return new ProductoCarritoViewHolder(item, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductoCarritoViewHolder holder, int position) {
        ProductoCarrito productoCarritoActual = listaProductosCarrito.get(position);
        if (productoCarritoActual.getFotoURL() == "fotoURL"){
            holder.imgProductoCarrito.setImageResource(R.drawable.producto);
        }else {
            Glide.with(c).load(listaProductosCarrito.get(position).getFotoURL()).into(holder.imgProductoCarrito);
        }
        holder.txtDescripciónProductoCarrito.setText("Descripción: " + String.valueOf(productoCarritoActual.getDescripción()));
        holder.txtMarcaProductoCarrito.setText("Marca: " + String.valueOf(productoCarritoActual.getMarca()));
        holder.txtCantidadProductoCarrito.setText("Cantidad: " + String.valueOf(productoCarritoActual.getCantidad()) + " unidades");
        holder.txtPrecioProductoCarrito.setText("Precio :" + String.valueOf(productoCarritoActual.getPrecio()) + "€");

    }

    @Override
    public int getItemCount() {
        if (listaProductosCarrito != null){
            return listaProductosCarrito.size();
        }else {
            return 0;
        }
    }

    //MÉTODO PARA CARGAR LOS PRODUCTOS DE CARRITO AL RECYCLER VIEW
    public void cargarRecyclerView(ProductoCarrito productoCarrito) {
        listaProductosCarrito.add(productoCarrito);
        notifyItemInserted(listaProductosCarrito.size());
    }
}
