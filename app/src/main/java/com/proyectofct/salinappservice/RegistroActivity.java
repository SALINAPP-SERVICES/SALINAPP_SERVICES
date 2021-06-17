package com.proyectofct.salinappservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText edtREmail;
    private EditText edtRContraseña;
    private EditText edtRConfirmarContraseña;
    private EditText edtRNombre;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtREmail = (EditText) findViewById(R.id.edtRE);
        edtRContraseña = (EditText) findViewById(R.id.edtRContraseña);
        edtRConfirmarContraseña = (EditText) findViewById(R.id.edtRConfirmarContraseña);
        edtRNombre = (EditText) findViewById(R.id.edtRNombre);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser usuario = firebaseAuth.getCurrentUser();
            if(usuario != null){
                //firebaseAuth.signOut();
                //Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                //startActivity(intent);
            }
        }};

        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void registrarUsuario(View view) {
        validaciónRegistroUsuario("Usuarios");
    }

    private void validaciónRegistroUsuario(String tipoUsuario) {
        String usuario = String.valueOf(edtREmail.getText());
        String contraseña = String.valueOf(edtRContraseña.getText());
        String confirmarContraseña = String.valueOf(edtRConfirmarContraseña.getText());

        if (usuario.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
        } else if (!usuario.contains("@")) {
            edtREmail.setError("Formato de usuario incorrecto (debe incluir @)");
        } else if (contraseña.length() < 6) {
            edtRContraseña.setError("La contraseña debe de tener 6 caracteres o más");
        } else if (!contraseña.equalsIgnoreCase(confirmarContraseña)) {
            edtRConfirmarContraseña.setError("Las contraseñas no coinciden");
        } else {
            firebaseAuth.signInWithEmailAndPassword(usuario, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Log.i("Error", "Excepción " + task.getException().toString());
                        try{
                            throw task.getException();
                        }catch (FirebaseAuthInvalidUserException e){
                            crearUsuarioFirebase(usuario, contraseña, tipoUsuario);
                        } catch (FirebaseAuthInvalidCredentialsException e){
                            Toast.makeText(RegistroActivity.this, "Usuario ya registrado", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void crearUsuarioFirebase(String usuario, String contraseña, String tipoUsuario) {
        firebaseAuth.createUserWithEmailAndPassword(usuario, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(RegistroActivity.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                }else {
                    addToFirestore(tipoUsuario);
                    Toast.makeText(RegistroActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addToFirestore(String tipoUsuario) {
        String nombre = String.valueOf(edtRNombre.getText());
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("Nombre", nombre);
        usuario.put("Email", firebaseAuth.getCurrentUser().getEmail());
        usuario.put("Teléfono", "");

        db.collection(tipoUsuario).document(firebaseAuth.getCurrentUser().getUid()).set(usuario).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map<String, Object> data = new HashMap<>();
                db.collection("businessdata")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("", "Nuevo usuario en firestore");
                        firebaseAuth.signOut();
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("", "Error al añadir el usuario en firestore", e);
            }
        });

        DocumentReference documentReference = db.collection("shoppingcars").document(firebaseAuth.getCurrentUser().getUid());
        Map<String, Object> carrito = new HashMap<>();

        documentReference.set(carrito).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Log.i("", "Carrito inicial creado correctamente");
                firebaseAuth.signOut();
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("ERROR", "Error al crear el carrito inicial");
            }
        });
    }
}