package com.proyectofct.salinappservice.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.proyectofct.salinappservice.BienvenidaActivity;
import com.proyectofct.salinappservice.Clases.Productos.FotosProducto;
import com.proyectofct.salinappservice.Clases.Productos.ProductoCarrito;
import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Controladores.FotosProductosController;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.io.File;
import java.io.IOException;
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

    private static final int IMAGEN = 1;
    private static final int FOTO = 2;
    Bitmap imagenNueva;

    private String path;
    File carpeta;
    Bitmap bitmap;


    private Button btAñadirAlCarrito;
    private Button btnGuardar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista;
        if(BienvenidaActivity.EMPRESA == true){
            vista = inflater.inflate(R.layout.fragment_detalle_grupo_imagenes_productos_publicados, container, false);
        } else{
            vista = inflater.inflate(R.layout.fragment_detalle_grupo_productos_publicados, container, false);
        }

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

                    if(BienvenidaActivity.EMPRESA == true) {
                        imgDetalleProductoPublicado.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
                                final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
                                alertOpciones.setTitle("Seleccione una opción");
                                alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (opciones[i].equals("Tomar Foto")) {
                                            File directorio = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
                                            boolean isCreada = directorio.exists();

                                            if (isCreada == false) {
                                                isCreada = directorio.mkdirs();
                                            }

                                            if (isCreada == true) {

                                                Long tiempo = System.currentTimeMillis() / 1000;
                                                String nombre = tiempo.toString() + ".jpg";

                                                path = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + nombre;

                                                carpeta = new File(path);
                                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(carpeta));

                                                startActivityForResult(intent, FOTO);
                                            }

                                        } else {
                                            if (opciones[i].equals("Cargar Imagen")) {
                                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                intent.setType("image/");
                                                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                                startActivityForResult(intent.createChooser(intent, "Seleccione una imagen"), IMAGEN);
                                            } else {
                                                dialogInterface.dismiss();
                                            }
                                        }
                                    }
                                });
                                alertOpciones.show();
                            }
                        });

                        btnGuardar = (Button) vista.findViewById(R.id.btnGuardar_fdgipp);
                        FotosProducto fp = new FotosProducto();

                        btnGuardar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String cod_producto = productoPublicado.getP().getCod_producto();
                                fp.setFotos(imagenNueva);
                                FotosProductosController.actualizarFoto(fp, cod_producto);
                                Toast.makeText(getActivity(), "Imagen guardada correctamente", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        //AÑADIR AL CARRITO
                        db = FirebaseFirestore.getInstance();
                        firebaseAuth = FirebaseAuth.getInstance();

                        btAñadirAlCarrito = (Button) vista.findViewById(R.id.btAñadirAlCarrito);

                        btAñadirAlCarrito.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ProductoCarrito productoCarrito = new ProductoCarrito(productoPublicado.getIdProductoEmpresa(), 1, productoPublicado.getP().getDescripción(), productoPublicado.getE().getCod_empresa(), String.valueOf(productoPublicado.getId_foto()), productoPublicado.getP().getMarca(), productoPublicado.getP().getModelo(), productoPublicado.getPrecioventa());
                                DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if (task.isComplete()) {
                                            task.getResult();
                                            DocumentSnapshot data = task.getResult();
                                            ProductoCarrito miProductoCarrito = data.get(String.valueOf(productoPublicado.getIdProductoEmpresa()), ProductoCarrito.class);
                                            if (miProductoCarrito != null) {
                                                int nuevaCantidad = miProductoCarrito.getCantidad() + 1;
                                                miProductoCarrito.setCantidad(nuevaCantidad);
                                                ActualizarProductoCarrito(miProductoCarrito, documentReference);
                                            } else {
                                                ActualizarProductoCarrito(productoCarrito, documentReference);
                                            }
                                            Navigation.findNavController(vista).navigate(R.id.nav_fragment_carrito);
                                        }
                                    }
                                });
                            }
                        });
                    }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGEN && resultCode == Activity.RESULT_OK) {
            Uri u = data.getData();
            imgDetalleProductoPublicado.setImageURI(u);
            try {
                imagenNueva = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), u);
            } catch (IOException e) {
                Log.i("", " Error al cambiar la imagen");
            }
        } else if (requestCode == FOTO && resultCode == Activity.RESULT_OK) {
            MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("", " Error al cambiar la imagen");
                }
            });
            bitmap = BitmapFactory.decodeFile(path);
            imgDetalleProductoPublicado.setImageBitmap(bitmap);
            imagenNueva = bitmap;

        }

    }
}
