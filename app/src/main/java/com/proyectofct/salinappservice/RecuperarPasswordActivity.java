package com.proyectofct.salinappservice;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarPasswordActivity extends AppCompatActivity {

    private EditText edtRPUsuario;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        edtRPUsuario = (EditText) findViewById(R.id.edtRPUsuario);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void recordarContraseña(View view) {
        String email = String.valueOf(edtRPUsuario.getText());

        if(!email.isEmpty()){
            if(email.contains("@")){
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RecuperarPasswordActivity.this, "Revisa tu email", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RecuperarPasswordActivity.this, "Error al enviar el email de recuperación de contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(this, "Formato de email incorrecto (Debe de haber una @)", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Completa el campo", Toast.LENGTH_SHORT).show();
        }
    }
}