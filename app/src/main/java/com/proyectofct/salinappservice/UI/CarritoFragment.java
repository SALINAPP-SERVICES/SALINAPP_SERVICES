package com.proyectofct.salinappservice.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.proyectofct.salinappservice.Clases.Productos.ProductoCarrito;
import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.Controladores.ReservaController;
import com.proyectofct.salinappservice.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CarritoFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_carrito, container, false);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //Obtenemos la ruta de referencia del documento
        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());

        //Sacamos los datos que contienen la ruta de referencia
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                //Obtenemos el resultado de la consulta
                DocumentSnapshot data = task.getResult();

                //Convertimos los datos de la consulta a diccionario (map)
                Map<String, Object> misDatos = data.getData();

                //Cada iteración de este for es una empresa del carrito
                for(Map.Entry<String, Object> objeto : misDatos.entrySet()){

                    //Este array contiene todos los productos del carrito de la empresa
                    ArrayList<ProductoCarrito> productosCarrito = (ArrayList<ProductoCarrito>) objeto.getValue();

                    //Cada iteración de este for recorre cada uno de los productos del carrito de la empresa
                    for(int i = 0; productosCarrito.size()>i; i++){

                        //Creamos un objeto ObjectMapper, necesita en gradle: implementation 'com.fasterxml.jackson.core:jackson-databind:2.11.1'
                        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

                        //Sacamos el objeto ProductoCarrito (el objeto que se añade cuando pulsamos añadir al carro)
                        ProductoCarrito productoCarrito = mapper.convertValue(productosCarrito.get(i), ProductoCarrito.class);

                        Cliente cliente = new Cliente(1, "email", "pass", "datos");
                        Direcciones direccion = new Direcciones(1, "direccion");
                        DireccionesClientes direccionesClientes = new DireccionesClientes(1, direccion, cliente);

                        int idReserva = ReservaController.obtenerNuevoIDReserva();
                        ArrayList<LíneaReserva> líneasReserva = new ArrayList<LíneaReserva>();
                        Date fechaActual = new Date();
                        double precioTotal = productoCarrito.getCantidad() * productoCarrito.getPrecio();

                        int idLíneaReserva = ReservaController.obtenerNuevoIDLíneaReserva();
                        //LíneaReserva líneaReserva = new LíneaReserva(idLíneaReserva, null, productoCarrito, productoCarrito.getCantidad());
                        //¿EL TERCER PARÁMETRO DEBERÍA DE SER DE TIPO DE PRODUCTO PUBLICADO O PRODUCTO CARRITO?
                        //Si es de tipo producto publicado, habría que cambiar la consulta de inserción de la base de datos
                        //¿Como creo una línea de reserva si necesito una reserva y para crear una reserva necesito un arraylist con las lineas de reserva?
                        //líneasReserva.add(líneaReserva);

                        //Reserva reserva = new Reserva(idReserva, líneasReserva, fechaActual, precioTotal, direccionesClientes);

                        //ReservaController.insertarReserva(reserva);
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