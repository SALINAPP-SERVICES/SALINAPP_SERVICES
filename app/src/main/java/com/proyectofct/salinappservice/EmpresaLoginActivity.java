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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmpresaLoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText edtEmail;
    private EditText edtPass;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_login);

        edtEmail = (EditText) findViewById(R.id.edtLECorreo);
        edtPass = (EditText) findViewById(R.id.edtLEPass);

        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null){
                    Toast.makeText(EmpresaLoginActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EmpresaLoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void loginEmpresa(View view) { validacionInicioSesion(); }

    private void validacionInicioSesion() {
        String usuario = String.valueOf(edtEmail.getText());
        String pass = String.valueOf(edtPass.getText());

        if (usuario.isEmpty() && pass.isEmpty()){
            edtEmail.setError("Introduce un email");
            edtPass.setError("Introduce la contraseña");
        }else if (!usuario.contains("@")){
            edtEmail.setError("Introduce un email correcto");
        }else if (pass.length() < 6){
            edtPass.setError("La contraseña debe tener 6 caracteres o mas");
        }else {
            login(usuario,pass);
        }
    }

    private void login(String usuario, String pass) {
        firebaseAuth.signInWithEmailAndPassword(usuario,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Log.i("Login","Excepción: " + task.getException().toString());
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        edtEmail.setError("Usuario no encontrado");
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        edtPass.setError("Contraseña incorrecta");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void signupEmpresa(View view) {
        Intent intent = new Intent(EmpresaLoginActivity.this,EmpresaRegistroActivity.class);
        startActivity(intent);
    }

    public void recordarPassEmpresa(View view) {
        Intent intent = new Intent(EmpresaLoginActivity.this,RecuperarPasswordActivity.class);
        startActivity(intent);
    }
}