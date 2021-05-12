package com.proyectofct.salinappservice.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.proyectofct.salinappservice.Clases.Productos.ProductoPublicado;
import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.sql.Blob;
import java.time.LocalDateTime;

import static com.proyectofct.salinappservice.Clases.Productos.ProductoPublicadoViewHolder.EXTRA_OBJETO_PRODUCTO_PUBLICADO;
import static com.proyectofct.salinappservice.Utilidades.ImagenesBlobBitmap.blob_to_bitmap;

public class DetalleProductosPublicadosFragment extends Fragment {
    private Button btAtras;
    private Button btAñadirAlCarrito;

    private ImageView imgDetalleProductoPublicado;
    private TextView txtCantidadDetalleProductoPublicado;
    private TextView txtPrecioDetalleProductoPublicado;
    private TextView txtMarcaDetalleProductoPublicado;
    private TextView txtModeloDetalleProductoPublicado;

    private EditText edtCantidad;

    public static final String EXTRA_OBJETO_LÍNEA_RESERVA = "com.proyectofct.salinappservice.LíneaReserva";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_detalle_productos_publicados, container, false);

        /*
        //BOTÓN IR ATRÁS
        btAtras = (Button) vista.findViewById(R.id.btn_Atras);

        btAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(fragment_detalle_producto_publicado.this);
                navController.popBackStack();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        */

        //DETALLES PRODUCTO PUBLICADO
        imgDetalleProductoPublicado = (ImageView) vista.findViewById(R.id.imgDetalleProductoPublicado);
        txtCantidadDetalleProductoPublicado = (TextView) vista.findViewById(R.id.txtCantidadDetalleProductoPublicado);
        txtPrecioDetalleProductoPublicado = (TextView) vista.findViewById(R.id.txtPrecioDetalleProductoPublicado);
        txtMarcaDetalleProductoPublicado = (TextView) vista.findViewById(R.id.txtMarcaDetalleProductoPublicado);
        txtModeloDetalleProductoPublicado = (TextView) vista.findViewById(R.id.txtModeloDetalleProductoPublicado);

        ProductoPublicado productoPublicado = (ProductoPublicado) getArguments().getSerializable(EXTRA_OBJETO_PRODUCTO_PUBLICADO);
        if (productoPublicado != null){
            Blob imagenProductoPublicado = productoPublicado.getP().getImagen();
            if (imagenProductoPublicado != null){
                imgDetalleProductoPublicado.setImageBitmap(blob_to_bitmap(imagenProductoPublicado, ConfiguracionesGeneralesDB.ANCHO_FOTO, ConfiguracionesGeneralesDB.ALTO_FOTO));
            }else {
                imgDetalleProductoPublicado.setImageResource(R.drawable.producto);
            }
            txtCantidadDetalleProductoPublicado.setText(productoPublicado.getCantidad() + " unidades");
            txtPrecioDetalleProductoPublicado.setText(productoPublicado.getPrecioventa() + "€");
            txtMarcaDetalleProductoPublicado.setText(productoPublicado.getP().getMarca());
            txtModeloDetalleProductoPublicado.setText(productoPublicado.getP().getModelo());
        }else {
            Log.i("ERROR", "El producto publicado no se ha recibido del fragment anterior correctamente");
        }

        //AÑADIR AL CARRITO
        btAñadirAlCarrito = (Button) vista.findViewById(R.id.btAñadirAlCarrito);
        edtCantidad = (EditText) vista.findViewById(R.id.edtCantidad);

        btAñadirAlCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creo la reserva
                LocalDateTime fechaActual = LocalDateTime.now();
                double precioTotal = productoPublicado.getCantidad() * productoPublicado.getPrecioventa();
                Reserva reserva = new Reserva(1, fechaActual, precioTotal); //¿La idReserva como la vamos a actualizar? Cada vez que creemos una nueva reserva

                int cantidad = Integer.parseInt(String.valueOf(edtCantidad.getText()));
                LíneaReserva líneaReserva = new LíneaReserva(1, reserva, productoPublicado, cantidad); //¿La idLíneaReserva como la vamos a actualizar? Cada vez que creemos una nueva líneaReserva

                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_OBJETO_LÍNEA_RESERVA, líneaReserva);
                Navigation.findNavController(view).navigate(R.id.nav_fragment_carrito, bundle);
            }
        });

        return vista;
    }
}