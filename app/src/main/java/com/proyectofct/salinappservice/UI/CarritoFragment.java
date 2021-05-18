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

                for(Map.Entry<String, Object> objeto : misDatos.entrySet()){
                    //System.out.println(objeto.getValue());
                    Object objetoProductoFirestore = objeto.getValue(); //AQUÍ SALTA ERROR POR CASTING
                    //LA GENTE LO HACE ASÍ O ACCEDIENDO SOLO A LOS VALORES, DA IGUAL LA FORMA
                    ProductoCarrito productoFirestore = (ProductoCarrito) objetoProductoFirestore;

                    ArrayList<ProductoCarrito> productosCarrito = new ArrayList<ProductoCarrito>();
                    productosCarrito.add(productoFirestore);

                    //Cada iteración de este for recorre cada uno de los productos del carrito de la empresa
                    for(int i = 0; productosCarrito.size() > i; i++){

                        //Creamos un objeto ObjectMapper, necesita en gradle: implementation 'com.fasterxml.jackson.core:jackson-databind:2.11.1'
                        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

                        //Sacamos el objeto ProductoCarrito (el objeto que se añade cuando pulsamos añadir al carro)
                        ProductoCarrito productoCarrito = mapper.convertValue(productosCarrito.get(i), ProductoCarrito.class);

                        ProductoPublicado productoPublicadoCarrito = new ProductoPublicado(Integer.parseInt(productoCarrito.getCodProducto()), productoCarrito.getCantidad(), productoCarrito.getPrecio(), true, false, new Producto(productoCarrito.getCodProducto(), "codQR", productoCarrito.getMarca(), productoCarrito.getModelo(), productoCarrito.getDescripción(), null /*productoCarrito.getFotoURL()*/), new Empresa(productoCarrito.getCodEmpresa(), "claveEmpresa", "datosEmpresa"));

                        int idReserva = ReservaController.obtenerNuevoIDReserva();

                        //Creo la línea de reserva
                        int idLíneaReserva = ReservaController.obtenerNuevoIDLíneaReserva();
                        LíneaReserva líneaReserva = new LíneaReserva(idLíneaReserva, idReserva, productoPublicadoCarrito, productoCarrito.getCantidad());

                        ArrayList<LíneaReserva> líneasReserva = new ArrayList<LíneaReserva>();
                        líneasReserva.add(líneaReserva);

                        //Creo la reserva
                        Cliente cliente = new Cliente(1, "email", "pass", "datos");
                        Direcciones direccion = new Direcciones(1, "direccion");
                        DireccionesClientes direccionesClientes = new DireccionesClientes(1, direccion, cliente);

                        Date fechaActual = new Date();
                        double precioTotal = productoCarrito.getCantidad() * productoCarrito.getPrecio();

                        Reserva reserva = new Reserva(idReserva, líneasReserva, fechaActual, precioTotal, direccionesClientes);

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