package com.proyectofct.salinappservice.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.proyectofct.salinappservice.Clases.Productos.ProductoPublicado;
import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Controladores.ReservaController;
import com.proyectofct.salinappservice.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CarritoFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_carrito, container, false);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documento = task.getResult();
                Map<String, Object> misDatos = documento.getData();

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
                    ArrayList<ProductoCarrito> productosCarrito = new ArrayList<ProductoCarrito>();
                    productosCarrito.add(productoFirestore);

                    //Recorro el ArrayList de ProductoCarrito
                    for(int i = 0; productosCarrito.size() > i; i++){
                        //Creo un objeto ObjectMapper, necesita la implementación en gradle: implementation 'com.fasterxml.jackson.core:jackson-databind:2.11.1'
                        ObjectMapper mapper = new ObjectMapper();
                        ProductoCarrito productoCarrito = mapper.convertValue(productosCarrito.get(i), ProductoCarrito.class);

                        Log.i("PRUEBA", productoCarrito.toString());

                        //Creo la línea de reserva
                        int idLíneaReserva = ReservaController.obtenerNuevoIDLíneaReserva();
                        int idReserva = ReservaController.obtenerNuevoIDReserva();
                        ProductoPublicado productoPublicadoCarrito = new ProductoPublicado(productoCarrito.getCodProducto(), productoCarrito.getCantidad(), productoCarrito.getPrecio(), true, false, new Producto(String.valueOf(productoCarrito.getCodProducto()), "codQR", productoCarrito.getMarca(), productoCarrito.getModelo(), productoCarrito.getDescripción(), null /*productoCarrito.getFotoURL()*/), new Empresa(productoCarrito.getCodEmpresa(), "claveEmpresa", "datosEmpresa"));

                        LíneaReserva líneaReserva = new LíneaReserva(idLíneaReserva, idReserva, productoPublicadoCarrito, productoCarrito.getCantidad());

                        //Creo la reserva, el ArrayList de LíneaReserva y añado el objeto creado
                        ArrayList<LíneaReserva> líneasReserva = new ArrayList<LíneaReserva>();
                        líneasReserva.add(líneaReserva);
                        Date fechaActual = new Date();
                        double precioTotal = productoCarrito.getCantidad() * productoCarrito.getPrecio();
                        Cliente cliente = new Cliente(1, "email", "pass", "datos");
                        Direcciones direccion = new Direcciones(1, "direccion");
                        DireccionesClientes direccionesClientes = new DireccionesClientes(1, direccion, cliente);

                        Reserva reserva = new Reserva(idReserva, líneasReserva, fechaActual, precioTotal, direccionesClientes);

                        //Inserto la reserva
                        boolean insertadoOk = ReservaController.insertarReserva(reserva);
                        if (insertadoOk){
                            Toast.makeText(getActivity(), "Reserva creada correctamente", Toast.LENGTH_LONG).show();
                        }else {
                            Log.i("SQL", "Error al insertar la reserva en la base de datos");
                        }
                    }
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