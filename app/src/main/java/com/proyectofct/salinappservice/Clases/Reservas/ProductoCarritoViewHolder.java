package com.proyectofct.salinappservice.Clases.Reservas;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyectofct.salinappservice.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductoCarritoViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView imgProductoCarrito;
    public TextView txtDescripciónProductoCarrito;
    public TextView txtMarcaProductoCarrito;
    public TextView txtCantidadProductoCarrito;
    public TextView txtPrecioProductoCarrito;

    final ListaProductosCarritoAdapter listaProductosCarritoAdapter;

    public Button btAumentarCantidad;
    public Button btDisminuirCantidad;

    public ProductoCarritoViewHolder(@NonNull View itemView, ListaProductosCarritoAdapter listaProductosCarritoAdapter) {
        super(itemView);
        imgProductoCarrito = (CircleImageView) itemView.findViewById(R.id.imgProductoCarrito);
        txtDescripciónProductoCarrito = (TextView) itemView.findViewById(R.id.txtDescripcionProductoCarrito);
        txtMarcaProductoCarrito = (TextView) itemView.findViewById(R.id.txtMarcaProductoCarrito);
        txtCantidadProductoCarrito = (TextView) itemView.findViewById(R.id.txtCantidadProductoCarrito);
        txtPrecioProductoCarrito = (TextView) itemView.findViewById(R.id.txtPrecioProductoCarrito);
        this.listaProductosCarritoAdapter = listaProductosCarritoAdapter;

        btAumentarCantidad = (Button) itemView.findViewById(R.id.btAumentarCantidad);
        btDisminuirCantidad = (Button) itemView.findViewById(R.id.btDisminuirCantidad);
    }
}
