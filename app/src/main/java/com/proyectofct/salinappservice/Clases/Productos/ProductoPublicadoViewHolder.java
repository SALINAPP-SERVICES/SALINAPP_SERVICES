package com.proyectofct.salinappservice.Clases.Productos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.proyectofct.salinappservice.BienvenidaActivity;
import com.proyectofct.salinappservice.Camara;
import com.proyectofct.salinappservice.Controladores.FotosProductosController;
import com.proyectofct.salinappservice.Controladores.ProductoPublicadoController;
import com.proyectofct.salinappservice.HomeActivity;
import com.proyectofct.salinappservice.LoginActivity;
import com.proyectofct.salinappservice.R;
import com.proyectofct.salinappservice.UI.ProductosPublicadosFragment;

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
            if(!Camara.IMAGEN.equals("")){
                Bitmap imgBitmap = BitmapFactory.decodeFile(Camara.IMAGEN);
                FotosProducto fp = new FotosProducto();

                String cod_producto = productosPublicados.getP().getCod_producto();
                fp.setFotos(imgBitmap);

                final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(v.getContext());
                alertOpciones.setMessage("¿Desea cambiar este producto?");
                alertOpciones.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FotosProductosController.actualizarFoto(fp, cod_producto);
                                Camara.IMAGEN = "";
                                Toast.makeText(v.getContext(), "Imagen guardada correctamente", Toast.LENGTH_LONG).show();
                            }
                        });
                alertOpciones.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertOpciones.show();

            }else {
                if (grupoProductos != null && grupoProductos.size() == 0) {
                    //PAGINA NORMAL SIN SPINNER
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_OBJETO_PRODUCTO_PUBLICADO, productoPublicado);
                    Navigation.findNavController(v).navigate(R.id.nav_fragment_detalle_productos_publicados, bundle);
                } else if (grupoProductos != null) {
                    //PÁGINA NORMAL CON SPINNER
                    grupoProductos.add(productoPublicado);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_OBJETO_GRUPO_PRODUCTO_PUBLICADO, grupoProductos);
                    Navigation.findNavController(v).navigate(R.id.nav_fragment_detalle_grupo_productos_publicados, bundle);
                }
            }
        }
        else if (productosPublicados.getP() instanceof Coches){

        }
    }
}
