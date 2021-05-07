package com.proyectofct.salinappservice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView civFotoPerfil;
    private TextView txtNombre;
    private RecyclerView rvMensajes;
    private EditText edtMensaje;

    private AdapterMensajes adapterMensajes;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private static final int ENVIAR_IMAGEN = 1;
    private static final int FOTO_PERFIL = 2;
    private String fotoPerfilCadena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        civFotoPerfil = (CircleImageView) findViewById(R.id.civFotoPerfil);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        edtMensaje = (EditText) findViewById(R.id.edtMensaje);
        fotoPerfilCadena = "";

        adapterMensajes = new AdapterMensajes(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(linearLayoutManager);
        rvMensajes.setAdapter(adapterMensajes);

        adapterMensajes.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                irAlÚltimoMensaje();
            }
        });

        civFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), FOTO_PERFIL);
            }
        });

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Chat"); //Nombre de la sala de chat
        storage = FirebaseStorage.getInstance();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MensajeRecibir m = snapshot.getValue(MensajeRecibir.class);
                adapterMensajes.insertarMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void irAlÚltimoMensaje() {
        rvMensajes.scrollToPosition(adapterMensajes.getItemCount()-1);
    }

    public void enviarMensaje(View view) {
        String mensaje = String.valueOf(edtMensaje.getText());
        String nombre = String.valueOf(txtNombre.getText());
        MensajeEnviar m = new MensajeEnviar(mensaje, nombre, fotoPerfilCadena, "1", ServerValue.TIMESTAMP); //1, porque es un mensaje y no una foto
        databaseReference.push().setValue(m);
        edtMensaje.setText("");
    }

    public void enviarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), ENVIAR_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ENVIAR_IMAGEN && resultCode == RESULT_OK) {
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat"); //Nombre de la carpeta donde se guaradaran las imágenes
            final StorageReference imagenReferencia = storageReference.child(u.getLastPathSegment());
            imagenReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> resultado = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        resultado.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //String u = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                String imagenUrl = uri.toString();
                                String nombre = String.valueOf(txtNombre.getText());
                                MensajeEnviar m = new MensajeEnviar("Dani te ha enviado una imagen", imagenUrl, nombre, fotoPerfilCadena, "2", ServerValue.TIMESTAMP); //2, porque es una foto
                                databaseReference.push().setValue(m);
                            }
                        });
                    }
                }
            });
        }else if(requestCode == FOTO_PERFIL && resultCode == RESULT_OK){
        Uri u = data.getData();
        storageReference = storage.getReference("fotos_perfil"); //Nombre de la carpeta donde se guaradaran las fotos de perfil
        final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
        fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> resultado= taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        resultado.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //String u = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                String imagenUrl = uri.toString();
                                String nombre = String.valueOf(txtNombre.getText());
                                fotoPerfilCadena = imagenUrl;
                                MensajeEnviar m = new MensajeEnviar("Dani ha actualizado su foto de perfil", imagenUrl, nombre, fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                                databaseReference.push().setValue(m);
                                Glide.with(ChatActivity.this).load(imagenUrl).into(civFotoPerfil);
                            }
                        });
                    }
                }
            }});
        }
    }
}