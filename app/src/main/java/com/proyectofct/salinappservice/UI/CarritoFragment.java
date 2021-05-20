package com.proyectofct.salinappservice.UI;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;
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

public class CarritoFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private ProductoCarrito productoCarrito;

    private RecyclerView rvProductosCarrito;
    private ArrayList<ProductoCarrito> productosCarrito;
    private ListaProductosCarritoAdapter listaProductosCarritoAdapter;

    private Button btCrearReserva;

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

        //OBTENER PRODUCTOS DE CARRITO DE FIRESTORE
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                            //CREAR RESERVA
                            btCrearReserva = vista.findViewById(R.id.btCrearReserva);
                            btCrearReserva.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Creo la línea de reserva y la añado a un ArrayList
                                    int idLíneaReserva = ReservaController.obtenerNuevoIDLíneaReserva();
                                    int idReserva = ReservaController.obtenerNuevoIDReserva();
                                    ProductosPublicados productosPublicadosCarrito = new ProductosPublicados(productoCarrito.getCodProducto(), productoCarrito.getCantidad(), productoCarrito.getPrecio(), true, false, new Producto(String.valueOf(productoCarrito.getCodProducto()), "codQR", productoCarrito.getMarca(), productoCarrito.getModelo(), productoCarrito.getDescripción(), null /*productoCarrito.getFotoURL()*/), new Empresa(productoCarrito.getCodEmpresa(), "claveEmpresa", "datosEmpresa"));

                                    LíneaReserva líneaReserva = new LíneaReserva(idLíneaReserva, idReserva, productosPublicadosCarrito, productoCarrito.getCantidad());
                                    ArrayList<LíneaReserva> líneasReserva = new ArrayList<LíneaReserva>();
                                    líneasReserva.add(líneaReserva);

                                    //Creo la reserva
                                    Date fechaActual = new Date();
                                    double precioTotal = productoCarrito.getCantidad() * productoCarrito.getPrecio();
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
}