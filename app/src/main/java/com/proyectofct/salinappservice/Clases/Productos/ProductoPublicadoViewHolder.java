package com.proyectofct.salinappservice.Clases.Productos;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.proyectofct.salinappservice.Controladores.ProductoPublicadoController;
import com.proyectofct.salinappservice.R;

import java.util.ArrayList;

public class ProductoPublicadoViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public static final String EXTRA_OBJETO_PRODUCTO_PUBLICADO = "com.proyectofct.salinappservice.ProductoPublicado";
    public static final String EXTRA_OBJETO_GRUPO_PRODUCTO_PUBLICADO = "com.proyectofct.salinappservice.ProductoPublicado";
    //public static final String EXTRA_IMAGEN_PRODUCTO_PUBLICADO = "com.proyectofct.salinappservice.ProductoPublicadoViewHolder.imagen_productoPublicado";

    public ListaProductosPublicadosAdapter listaProductosPublicadosAdapter;
    public ImageView imgProductoPublicado = null;
    public TextView txtMarcaProductoPublicado = null;
    public TextView txtModeloProductoPublicado = null;
    public TextView txtPrecioProductoPublicado = null;
    public TextView txtStockProductoPublicado = null;
    public TextView txtDescripciónProductoPublicado = null;

    public ProductoPublicadoViewHolder(@NonNull View itemView, ListaProductosPublicadosAdapter listaProductosPublicadosAdapter) {
        super(itemView);
        imgProductoPublicado = (ImageView)  itemView.findViewById(R.id.imgProductoPublicado);
        txtMarcaProductoPublicado = (TextView) itemView.findViewById(R.id.txtMarcaProductoPublicado);
        txtModeloProductoPublicado = (TextView) itemView.findViewById(R.id.txtModeloProductoPublicado);
        txtPrecioProductoPublicado = (TextView) itemView.findViewById(R.id.txtPrecioProductoPublicado);
        txtStockProductoPublicado = (TextView) itemView.findViewById(R.id.txtStockProductoPublicado);
        txtDescripciónProductoPublicado = (TextView) itemView.findViewById(R.id.txtDescripcionProductoPublicado);
        this.listaProductosPublicadosAdapter = listaProductosPublicadosAdapter;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int posición = getLayoutPosition();
        ProductosPublicados productosPublicados = this.listaProductosPublicadosAdapter.getListaProductosPublicados().get(posición);
        listaProductosPublicadosAdapter.notifyDataSetChanged();
        if (productosPublicados.getP() instanceof Moda){
            ProductosPublicados productoPublicado = new ProductosPublicados(productosPublicados.getIdProductoEmpresa(), productosPublicados.getCantidad(), productosPublicados.getPrecioventa(), productosPublicados.isHabilitado(), productosPublicados.isArchivado(), productosPublicados.getP(), productosPublicados.getE());
            ArrayList<ProductosPublicados> grupoProductos = ProductoPublicadoController.obtenerVariantesProductoPublicado(productoPublicado.getP().getCod_producto());
            if (grupoProductos != null && grupoProductos.size() == 0){
                //PAGINA NORMAL
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_OBJETO_PRODUCTO_PUBLICADO, productoPublicado);
                Navigation.findNavController(v).navigate(R.id.nav_fragment_detalle_productos_publicados, bundle);
            }else if (grupoProductos != null){
                //ADAPTAR CODIGO
                grupoProductos.add(productoPublicado);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_OBJETO_GRUPO_PRODUCTO_PUBLICADO, grupoProductos);
                Navigation.findNavController(v).navigate(R.id.nav_fragment_detalle_grupo_productos_publicados, bundle);
            }
        }
        else if (productosPublicados.getP() instanceof Coches){

        }

        /*ProductosPublicados productoPublicado = new ProductosPublicados(productosPublicados.getIdProductoEmpresa(), productosPublicados.getCantidad(), productosPublicados.getPrecioventa(), productosPublicados.isHabilitado(), productosPublicados.isArchivado(), productosPublicados.getP(), productosPublicados.getE());
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_OBJETO_PRODUCTO_PUBLICADO, productoPublicado);
        Navigation.findNavController(v).navigate(R.id.nav_fragment_detalle_productos_publicados, bundle);*/
    }
}
