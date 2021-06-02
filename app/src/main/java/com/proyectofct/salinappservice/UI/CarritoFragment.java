package com.proyectofct.salinappservice.UI;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.proyectofct.salinappservice.Clases.Clientes.Cliente;
import com.proyectofct.salinappservice.Clases.Clientes.Direcciones;
import com.proyectofct.salinappservice.Clases.Clientes.DireccionesClientes;
import com.proyectofct.salinappservice.Clases.Empresa.Empresa;
import com.proyectofct.salinappservice.Clases.Productos.Producto;
import com.proyectofct.salinappservice.Clases.Productos.ProductoCarrito;
import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Clases.Reservas.ListaProductosCarritoAdapter;
import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Controladores.ClienteController;
import com.proyectofct.salinappservice.Controladores.ReservaController;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.proyectofct.salinappservice.Clases.Productos.ProductoPublicadoViewHolder.EXTRA_OBJETO_PRODUCTO_PUBLICADO;

public class CarritoFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private ProductoCarrito productoCarrito;

    private RecyclerView rvProductosCarrito;
    private ArrayList<ProductoCarrito> productosCarrito;
    private ListaProductosCarritoAdapter listaProductosCarritoAdapter;

    private Button btCrearReserva;

    private Button btAumentarCantidad;
    private Button btDisminuirCantidad;

    private int cantidadActual;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_carrito, container, false);

        //PREPARO EL RECYCLER VIEW
        rvProductosCarrito = (RecyclerView) vista.findViewById(R.id.rvProductosCarrito);
        productosCarrito = new ArrayList<ProductoCarrito>();
        listaProductosCarritoAdapter = new ListaProductosCarritoAdapter(getActivity(), productosCarrito);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvProductosCarrito.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            rvProductosCarrito.setLayoutManager(new GridLayoutManager(getActivity(), ConfiguracionesGeneralesDB.LANDSCAPE_NUM_COLUMNAS));
        }
        rvProductosCarrito.setAdapter(listaProductosCarritoAdapter);

        btAumentarCantidad = vista.findViewById(R.id.btAumentarCantidad);
        btDisminuirCantidad = vista.findViewById(R.id.btDisminuirCantidad);

        //OBTENER PRODUCTOS DE CARRITO DE FIRESTORE
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documento = task.getResult();
                Map<String, Object> misDatos = documento.getData();
                if (misDatos != null){
                    //Recorro el HashMap que devuelve Firebase
                    for(Map.Entry<String, Object> objeto : misDatos.entrySet()){
                        Object objetoProductoFirestore = objeto.getValue();

                        //Creo un objeto ProductoCarrito con los atributos que recibo del HashMap
                        long codProductoLong = (long) ((HashMap<Long, Object>) objetoProductoFirestore).get("codProducto"); //El tipo Integer en Firestore, no se usa en su defecto se usa el tipo Long
                        int codProducto = Math.toIntExact(codProductoLong);
                        long cantidadLong = (long) ((HashMap<Long, Object>) objetoProductoFirestore).get("cantidad"); //El tipo Integer en Firestore, no se usa en su defecto se usa el tipo Long
                        int cantidad = Math.toIntExact(cantidadLong);
                        String descripción = (String)((HashMap<String, Object>) objetoProductoFirestore).get("descripción");
                        String codEmpresa = (String)((HashMap<String, Object>) objetoProductoFirestore).get("codEmpresa");
                        String fotoURL = (String)((HashMap<String, Object>) objetoProductoFirestore).get("fotoURL");
                        String marca = (String)((HashMap<String, Object>) objetoProductoFirestore).get("marca");
                        String modelo = (String)((HashMap<String, Object>) objetoProductoFirestore).get("modelo");
                        double precio = (double) ((HashMap<Double, Object>) objetoProductoFirestore).get("precio"); //El tipo Double si es admitido en Firestore
                        ProductoCarrito productoFirestore = new ProductoCarrito(codProducto, cantidad, descripción, codEmpresa, fotoURL, marca, modelo, precio);

                        //Creo un ArrayList de ProductoCarrito y añado el objeto creado
                        ArrayList<ProductoCarrito> productosCarritoFirestore = new ArrayList<ProductoCarrito>();
                        productosCarritoFirestore.add(productoFirestore);

                        //Recorro el ArrayList de ProductoCarrito
                        for(int i = 0; productosCarritoFirestore.size() > i; i++){
                            //Creo un objeto ObjectMapper, necesita la implementación en gradle: implementation 'com.fasterxml.jackson.core:jackson-databind:2.11.1'
                            ObjectMapper mapper = new ObjectMapper();
                            productoCarrito = mapper.convertValue(productosCarritoFirestore.get(i), ProductoCarrito.class);

                            //CARGO EL RECYCLER VIEW CON EL ARRAY LIST DE LOS PRODUCTOS QUE OBTENGO DE FIRESTORE
                            listaProductosCarritoAdapter.cargarRecyclerView(productoCarrito);

                            //AUMENTAR O DISMINUIR CANTIDAD DEL PRODUCTO
                            cantidadActual = productoCarrito.getCantidad();

                            btAumentarCantidad.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cantidadActual = cantidadActual + 1;
                                    productoCarrito.setCantidad(cantidadActual);
                                    aumentarCantidadProductoFirestore(productoCarrito);
                                    listaProductosCarritoAdapter.cargarRecyclerView(productoCarrito);
                                }
                            });

                            btDisminuirCantidad.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (cantidadActual <= 1){
                                        cantidadActual = cantidadActual - 1;
                                        productoCarrito.setCantidad(cantidadActual);
                                        borrarProductoFirestore(productoCarrito);
                                        listaProductosCarritoAdapter.cargarRecyclerView(productoCarrito);
                                    }else if(cantidadActual > 1){
                                        cantidadActual = cantidadActual - 1;
                                        productoCarrito.setCantidad(cantidadActual);
                                        disminuirCantidadProductoFirestore(productoCarrito);
                                        listaProductosCarritoAdapter.cargarRecyclerView(productoCarrito);
                                    }
                                }
                            });

                            //CREAR RESERVA
                            btCrearReserva = vista.findViewById(R.id.btCrearReserva);
                            btCrearReserva.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Creo la línea de reserva y la añado a un ArrayList
                                    int idLíneaReserva = ReservaController.obtenerNuevoIDLíneaReserva();
                                    int idReserva = ReservaController.obtenerNuevoIDReserva();
                                    ProductosPublicados productosPublicadosCarrito = new ProductosPublicados(productoCarrito.getCodProducto(), cantidadActual, productoCarrito.getPrecio(), true, false, new Producto(String.valueOf(productoCarrito.getCodProducto()), "codQR", productoCarrito.getMarca(), productoCarrito.getModelo(), productoCarrito.getDescripción(), null /*productoCarrito.getFotoURL()*/), new Empresa(productoCarrito.getCodEmpresa(), "claveEmpresa", "datosEmpresa"));

                                    LíneaReserva líneaReserva = new LíneaReserva(idLíneaReserva, idReserva, productosPublicadosCarrito, cantidadActual);
                                    ArrayList<LíneaReserva> líneasReserva = new ArrayList<LíneaReserva>();
                                    líneasReserva.add(líneaReserva);

                                    //Creo la reserva
                                    Date fechaActual = new Date();
                                    double precioTotal = cantidadActual * productoCarrito.getPrecio();
                                    int idCliente = ClienteController.obtenerNuevoIDCliente();
                                    int idDirección = ClienteController.obtenerNuevoIDDirección();
                                    int idDirecciónCliente = ClienteController.obtenerNuevoIDDirecciónCliente();
                                    Cliente cliente = new Cliente(idCliente, "email", "pass", "datos");
                                    Direcciones direccion = new Direcciones(idDirección, "direccion");
                                    DireccionesClientes direccionesClientes = new DireccionesClientes(idDirecciónCliente, direccion, cliente);

                                    Reserva reserva = new Reserva(idReserva, líneasReserva, fechaActual, precioTotal, direccionesClientes);

                                    //Inserto la reserva
                                    boolean insertadoOk = ReservaController.insertarReserva(reserva);
                                    if (insertadoOk){
                                        Toast.makeText(getActivity(), "Reserva creada correctamente", Toast.LENGTH_LONG).show();
                                    }else {
                                        Log.i("SQL", "Error al insertar la reserva en la base de datos");
                                    }
                                }
                            });
                        }
                    }
                }else {
                    Log.i("ERROR", "Error al obtener los datos de Firestore");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("ERROR", "Error al obtener los productos del carrito de Firestore");
            }
        });


        return vista;
    }

    public void borrarProductoFirestore(ProductoCarrito productoCarrito) {
        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        Map<String, Object> productosCarrito = new HashMap<>();
        productosCarrito.put(String.valueOf(productoCarrito.getCodProducto()), FieldValue.delete());

        documentReference.update(productosCarrito).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                Toast.makeText(getActivity(), "Producto eliminado del carrito correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        documentReference.update(productosCarrito).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                Log.i("", "Error al borrar el producto del carrito");
            }
        });
    }

    public void aumentarCantidadProductoFirestore(ProductoCarrito productoCarrito){
        ProductosPublicados productosPublicados = (ProductosPublicados) getArguments().getSerializable(EXTRA_OBJETO_PRODUCTO_PUBLICADO);

        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    task.getResult();
                    DocumentSnapshot data = task.getResult();
                    ProductoCarrito miProductoCarrito = data.get(String.valueOf(productosPublicados.getIdProductoEmpresa()), ProductoCarrito.class);
                    if(miProductoCarrito != null){
                        int nuevaCantidad = miProductoCarrito.getCantidad() + 1;
                        miProductoCarrito.setCantidad(nuevaCantidad);
                        actualizarProductoFirestore(miProductoCarrito, documentReference);
                    }else{
                        actualizarProductoFirestore(productoCarrito, documentReference);
                    }
                }
            }
        });
    }

    public void disminuirCantidadProductoFirestore(ProductoCarrito productoCarrito){
        ProductosPublicados productosPublicados = (ProductosPublicados) getArguments().getSerializable(EXTRA_OBJETO_PRODUCTO_PUBLICADO);

        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    task.getResult();
                    DocumentSnapshot data = task.getResult();
                    ProductoCarrito miProductoCarrito = data.get(String.valueOf(productosPublicados.getIdProductoEmpresa()), ProductoCarrito.class);
                    if(miProductoCarrito != null){
                        int nuevaCantidad = miProductoCarrito.getCantidad() - 1;
                        miProductoCarrito.setCantidad(nuevaCantidad);
                        actualizarProductoFirestore(miProductoCarrito, documentReference);
                    }else{
                        actualizarProductoFirestore(productoCarrito, documentReference);
                    }
                }
            }
        });
    }

    public void actualizarProductoFirestore(ProductoCarrito productoCarrito, DocumentReference documentReference){
        Map<String, Object> mapaProductoCarrito = new HashMap<>();
        mapaProductoCarrito.put(String.valueOf(productoCarrito.getCodProducto()), productoCarrito);

        documentReference.set(mapaProductoCarrito, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Producto actualizado correctamente en Firestore", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("","Error al actualizar el producto en Firestore");
            }
        });
    }
}