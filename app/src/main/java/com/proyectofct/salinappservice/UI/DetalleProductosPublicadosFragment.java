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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.proyectofct.salinappservice.Clases.Productos.ProductoCarrito;
import com.proyectofct.salinappservice.Clases.Productos.ProductoPublicado;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
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

    public static final String EXTRA_OBJETO_RESERVA = "com.proyectofct.salinappservice.Reserva";

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

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

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        btAñadirAlCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = 0;
                String textoCantidad = String.valueOf(edtCantidad.getText());
                if(!textoCantidad.isEmpty()){
                    cantidad = Integer.parseInt(textoCantidad);
                    if (cantidad <= 0){
                        edtCantidad.setError("La cantidad mínima es 1");
                    }
                }else {
                    Log.i("", "Error");
                }

                ProductoCarrito productoCarrito = new ProductoCarrito(productoPublicado.getP().getCod_producto(), cantidad, productoPublicado.getP().getDescripción(), productoPublicado.getE().getCod_empresa(), "fotoURL", productoPublicado.getP().getMarca(), productoPublicado.getP().getModelo(), productoPublicado.getPrecioventa());

                DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
                Map<String, Object> mapaProductoCarrito = new HashMap<>();
                mapaProductoCarrito.put(productoCarrito.getCodProducto(), productoCarrito);

                documentReference.set(mapaProductoCarrito, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Producto añadido al carrito correctamente", LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(""," Error al añadir el producto al carrito");
                    }
                });

                /*
                //Creo la reserva
                Cliente cliente = new Cliente(1, "email", "pass", "datos");
                Direcciones direccion = new Direcciones(1, "direccion");
                DireccionesClientes direccionesClientes = new DireccionesClientes(1, direccion, cliente);

                int idReserva = ReservaController.obtenerNuevoIDReserva();
                ArrayList<LíneaReserva> líneasReserva = new ArrayList<LíneaReserva>();
                Date fechaActual = new Date();
                double precioTotal = productoPublicado.getCantidad() * productoPublicado.getPrecioventa();
                Reserva reserva = new Reserva(idReserva, líneasReserva, fechaActual, precioTotal, direccionesClientes);

                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_OBJETO_RESERVA, reserva);
                Navigation.findNavController(view).navigate(R.id.nav_fragment_carrito, bundle);


                //Creo la línea de reserva
                int idLíneaReserva = ReservaController.obtenerNuevoIDLíneaReserva();
                int cantidad = Integer.parseInt(String.valueOf(edtCantidad.getText()));
                if (cantidad <= 0){
                    edtCantidad.setError("La cantidad mínima es 1");
                }
                LíneaReserva líneaReserva = new LíneaReserva(idLíneaReserva, reserva, productoPublicado, cantidad);
                */
            }
        });

        return vista;
    }
}