package com.proyectofct.salinappservice.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.proyectofct.salinappservice.Clases.Productos.ProductoCarrito;
import com.proyectofct.salinappservice.Clases.Productos.ProductoPublicado;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

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

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_detalle_productos_publicados, container, false);

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
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        btAñadirAlCarrito = (Button) vista.findViewById(R.id.btAñadirAlCarrito);

        btAñadirAlCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductoCarrito productoCarrito = new ProductoCarrito(productoPublicado.getP().getCod_producto(), 1, productoPublicado.getP().getDescripción(), productoPublicado.getE().getCod_empresa(), "fotoURL", productoPublicado.getP().getMarca(), productoPublicado.getP().getModelo(), productoPublicado.getPrecioventa());

                DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isComplete()){
                            task.getResult();
                            DocumentSnapshot data = task.getResult();
                            ProductoCarrito miProductoCarrito = data.get(productoPublicado.getP().getCod_producto(), ProductoCarrito.class);
                            if(miProductoCarrito != null){
                                int cantidad = miProductoCarrito.getCantidad() + 1;
                                miProductoCarrito.setCantidad(cantidad);
                                ActualizarProductoCarrito(miProductoCarrito, documentReference);
                            }else{
                                ActualizarProductoCarrito(productoCarrito, documentReference);
                            }
                        }
                    }
                });
            }
        });

        return vista;
    }

    public void ActualizarProductoCarrito(ProductoCarrito productoCarrito, DocumentReference documentReference){
        Map<String, Object> mapaProductoCarrito = new HashMap<>();
        mapaProductoCarrito.put(productoCarrito.getCodProducto(), productoCarrito);

        documentReference.set(mapaProductoCarrito, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Producto añadido al carrito correctamente", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(""," Error al añadir producto al carrito");
            }
        });
    }
}