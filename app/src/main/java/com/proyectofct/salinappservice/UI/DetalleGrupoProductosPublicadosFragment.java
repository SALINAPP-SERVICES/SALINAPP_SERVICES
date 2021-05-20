package com.proyectofct.salinappservice.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.proyectofct.salinappservice.Clases.Productos.ProductoPublicadoViewHolder.EXTRA_OBJETO_GRUPO_PRODUCTO_PUBLICADO;
import static com.proyectofct.salinappservice.Utilidades.ImagenesBlobBitmap.blob_to_bitmap;

public class DetalleGrupoProductosPublicadosFragment extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    private ImageView imgDetalleProductoPublicado;
    private TextView txtCantidadDetalleProductoPublicado;
    private TextView txtPrecioDetalleProductoPublicado;
    private TextView txtMarcaDetalleProductoPublicado;
    private TextView txtModeloDetalleProductoPublicado;
    private Spinner spTallasColor;

    private Button btAñadirAlCarrito;

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

                    //AÑADIR AL CARRITO
                    db = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();

                    btAñadirAlCarrito = (Button) vista.findViewById(R.id.btAñadirAlCarrito);

                    btAñadirAlCarrito.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProductoCarrito productoCarrito = new ProductoCarrito(productoPublicado.getIdProductoEmpresa(), 1, productoPublicado.getP().getDescripción(), productoPublicado.getE().getCod_empresa(), "fotoURL", productoPublicado.getP().getMarca(), productoPublicado.getP().getModelo(), productoPublicado.getPrecioventa());
                            DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                    if(task.isComplete()){
                                        task.getResult();
                                        DocumentSnapshot data = task.getResult();
                                        ProductoCarrito miProductoCarrito = data.get(String.valueOf(productoPublicado.getIdProductoEmpresa()), ProductoCarrito.class);
                                        if(miProductoCarrito != null){
                                            int nuevaCantidad = miProductoCarrito.getCantidad() + 1;
                                            miProductoCarrito.setCantidad(nuevaCantidad);
                                            ActualizarProductoCarrito(miProductoCarrito, documentReference);
                                        }else{
                                            ActualizarProductoCarrito(productoCarrito, documentReference);
                                        }
                                    }
                                }
                            });
                        }
                    });
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

    public void ActualizarProductoCarrito(ProductoCarrito productoCarrito, DocumentReference documentReference){
        Map<String, Object> mapaProductoCarrito = new HashMap<>();
        mapaProductoCarrito.put(String.valueOf(productoCarrito.getCodProducto()), productoCarrito);

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
