package com.proyectofct.salinappservice.Clases.Reservas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.proyectofct.salinappservice.Clases.Productos.ProductoCarrito;
import com.proyectofct.salinappservice.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaProductosCarritoAdapter extends RecyclerView.Adapter<ProductoCarritoViewHolder> {
    private Context c;
    private ArrayList<ProductoCarrito> listaProductosCarrito;
    private LayoutInflater inflater;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private int cantidadActual;

    public ListaProductosCarritoAdapter(Context c, ArrayList<ProductoCarrito> listaProductosCarrito) {
        this.c = c;
        this.listaProductosCarrito = listaProductosCarrito;
        inflater = LayoutInflater.from(c);
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public ArrayList<ProductoCarrito> getListaProductosCarrito() {
        return listaProductosCarrito;
    }

    public void setListaProductosCarrito(ArrayList<ProductoCarrito> listaProductosCarrito) {
        this.listaProductosCarrito = listaProductosCarrito;
    }

    @NonNull
    @Override
    public ProductoCarritoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View item = inflater.inflate(R.layout.item_recyclerview_producto_carrito, parent, false);

        return new ProductoCarritoViewHolder(item, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductoCarritoViewHolder holder, int position) {
        ProductoCarrito productoCarritoActual = listaProductosCarrito.get(position);
        if (productoCarritoActual.getFotoURL() == "fotoURL"){
            holder.imgProductoCarrito.setImageResource(R.drawable.producto);
        }else {
            Glide.with(c).load(listaProductosCarrito.get(position).getFotoURL()).into(holder.imgProductoCarrito);
        }
        holder.txtDescripciónProductoCarrito.setText("Descripción: " + String.valueOf(productoCarritoActual.getDescripción()));
        holder.txtMarcaProductoCarrito.setText("Marca: " + String.valueOf(productoCarritoActual.getMarca()));
        holder.txtCantidadProductoCarrito.setText("Cantidad: " + String.valueOf(productoCarritoActual.getCantidad()) + " unidades");
        holder.txtPrecioProductoCarrito.setText("Precio: " + String.valueOf(productoCarritoActual.getPrecio()) + "€");

        //AUMENTAR O DISMINUIR CANTIDAD DEL PRODUCTO
        holder.btAumentarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productoCarritoActual.setCantidad(productoCarritoActual.getCantidad() + 1);
                aumentarCantidadProductoFirestore(productoCarritoActual);
                if (productoCarritoActual.getCantidad() > 0){
                    holder.txtCantidadProductoCarrito.setText("Cantidad: " + String.valueOf(productoCarritoActual.getCantidad()) + " unidades");
                }else {
                    Log.i("", "La cantidad de Firestore obtenida ha sido 0");
                }
            }
        });

        holder.btDisminuirCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productoCarritoActual.getCantidad() <= 1){
                    productoCarritoActual.setCantidad(productoCarritoActual.getCantidad() - 1);
                    borrarProductoFirestore(productoCarritoActual);
                    listaProductosCarrito.remove(holder.getAdapterPosition());
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listaProductosCarrito.size());
                    holder.itemView.setVisibility(View.GONE);
                }else if(productoCarritoActual.getCantidad() > 1){
                    productoCarritoActual.setCantidad(productoCarritoActual.getCantidad() - 1);
                    disminuirCantidadProductoFirestore(productoCarritoActual);
                    if (productoCarritoActual.getCantidad() > 0){
                        holder.txtCantidadProductoCarrito.setText("Cantidad: " + String.valueOf(productoCarritoActual.getCantidad()) + " unidades");
                    }else {
                        Log.i("", "La cantidad de Firestore obtenida ha sido 0");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listaProductosCarrito != null){
            return listaProductosCarrito.size();
        }else {
            return 0;
        }
    }

    //MÉTODO PARA CARGAR LOS PRODUCTOS DE CARRITO AL RECYCLER VIEW
    public void cargarRecyclerView(ProductoCarrito productoCarrito) {
        listaProductosCarrito.add(productoCarrito);
        notifyItemInserted(listaProductosCarrito.size());
    }

    //MÉTODO PARA BORRAR EL PRODUCTO DE FIRESTORE
    public void borrarProductoFirestore(ProductoCarrito productoCarrito) {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        Map<String, Object> productosCarrito = new HashMap<>();
        productosCarrito.put(String.valueOf(productoCarrito.getCodProducto()), FieldValue.delete());

        documentReference.update(productosCarrito).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                Toast.makeText(getC(), "Producto eliminado del carrito correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        documentReference.update(productosCarrito).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                Log.i("", "Error al borrar el producto del carrito");
            }
        });
    }

    //MÉTODO PARA AUMENTAR LA CANTIDAD DEL PRODUCTO DE FIRESTORE Y DEVOLVER LA CANTIDAD ACTUAL
    public void aumentarCantidadProductoFirestore(ProductoCarrito productoCarrito){
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @com.google.firebase.database.annotations.NotNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    task.getResult();
                    DocumentSnapshot data = task.getResult();
                    ProductoCarrito miProductoCarrito = data.get(String.valueOf(productoCarrito.getCodProducto()), ProductoCarrito.class);
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

    //MÉTODO PARA DISMINUIR LA CANTIDAD DEL PRODUCTO DE FIRESTORE Y DEVOLVER LA CANTIDAD ACTUAL
    public void disminuirCantidadProductoFirestore(ProductoCarrito productoCarrito){
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @com.google.firebase.database.annotations.NotNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    task.getResult();
                    DocumentSnapshot data = task.getResult();
                    ProductoCarrito miProductoCarrito = data.get(String.valueOf(productoCarrito.getCodProducto()), ProductoCarrito.class);
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

    //MÉTODO PARA ACTUALIZAR LA CANTIDAD DEL PRODUCTO DE FIRESTORE
    public void actualizarProductoFirestore(ProductoCarrito productoCarrito, DocumentReference documentReference){
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        Map<String, Object> mapaProductoCarrito = new HashMap<>();
        mapaProductoCarrito.put(String.valueOf(productoCarrito.getCodProducto()), productoCarrito);

        documentReference.set(mapaProductoCarrito, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getC(), "Producto actualizado correctamente en Firestore", Toast.LENGTH_LONG).show();
            }
        });
    }
}
