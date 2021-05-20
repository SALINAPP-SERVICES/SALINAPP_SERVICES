package com.proyectofct.salinappservice.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.sql.Blob;
import java.util.ArrayList;

import static com.proyectofct.salinappservice.Clases.Productos.ProductoPublicadoViewHolder.EXTRA_OBJETO_GRUPO_PRODUCTO_PUBLICADO;
import static com.proyectofct.salinappservice.Utilidades.ImagenesBlobBitmap.blob_to_bitmap;

public class DetalleGrupoProductosPublicadosFragment extends Fragment {

    private ImageView imgDetalleProductoPublicado;
    private TextView txtCantidadDetalleProductoPublicado;
    private TextView txtPrecioDetalleProductoPublicado;
    private TextView txtMarcaDetalleProductoPublicado;
    private TextView txtModeloDetalleProductoPublicado;
    private Spinner spTallasColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_detalle_grupo_productos_publicados, container, false);
        //DETALLES PRODUCTO PUBLICADO
        imgDetalleProductoPublicado = (ImageView) vista.findViewById(R.id.imgDetalleGrupoProductoPublicado);

        txtCantidadDetalleProductoPublicado = (TextView) vista.findViewById(R.id.txtCantidadDetalleGrupoProductoPublicado);
        txtPrecioDetalleProductoPublicado = (TextView) vista.findViewById(R.id.txtPrecioDetalleGrupoProductoPublicado);
        txtMarcaDetalleProductoPublicado = (TextView) vista.findViewById(R.id.txtMarcaDetalleGrupoProductoPublicado);
        txtModeloDetalleProductoPublicado = (TextView) vista.findViewById(R.id.txtModeloDetalleGrupoProductoPublicado);
        spTallasColor = (Spinner) vista.findViewById(R.id.spTallasColor);

        ArrayList<ProductosPublicados> productosPublicado = (ArrayList<ProductosPublicados>) getArguments().getSerializable(EXTRA_OBJETO_GRUPO_PRODUCTO_PUBLICADO);
        ArrayAdapter<ProductosPublicados> adapter = new ArrayAdapter<>(getContext(),R.layout.spinner_tallas_color_item,R.id.txtSpinnerTallaColor,productosPublicado);
        adapter.setDropDownViewResource(R.layout.spinner_tallas_color_item);
        spTallasColor.setAdapter(adapter);



        if (productosPublicado != null){
            spTallasColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ProductosPublicados productoPublicado = (ProductosPublicados) spTallasColor.getSelectedItem();
                    Blob imagenProductoPublicado = productoPublicado.getP().getImagen();
                    if (imagenProductoPublicado != null) {
                        imgDetalleProductoPublicado.setImageBitmap(blob_to_bitmap(imagenProductoPublicado, ConfiguracionesGeneralesDB.ANCHO_FOTO, ConfiguracionesGeneralesDB.ALTO_FOTO));
                    } else {
                        imgDetalleProductoPublicado.setImageResource(R.drawable.producto);
                    }
                    txtCantidadDetalleProductoPublicado.setText(String.valueOf(productoPublicado.getCantidad()) + " unidades");
                    txtPrecioDetalleProductoPublicado.setText(String.valueOf(productoPublicado.getPrecioventa() + "€"));
                    txtMarcaDetalleProductoPublicado.setText(String.valueOf(productoPublicado.getP().getMarca()));
                    txtModeloDetalleProductoPublicado.setText(String.valueOf(productoPublicado.getP().getModelo()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }else {
            Log.i("ERROR", "El producto publicado no se ha recibido del fragment anterior correctamente");
        }

        return vista;
    }
}
