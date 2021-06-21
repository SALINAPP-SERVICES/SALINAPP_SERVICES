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
import com.proyectofct.salinappservice.Clases.Empresa.Empresa;
import com.proyectofct.salinappservice.Clases.Empresa.InfoEmpresa;
import com.proyectofct.salinappservice.Controladores.EmpresaController;

import java.util.HashMap;
import java.util.Map;

public class EmpresaRegistroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText edtEmail;
    private EditText edtNombre;
    private EditText edtCIF;
    private EditText edtPass;
    private EditText edtConfirmPass;
    private EditText edtDireccion;
    private EditText edtSector;
    private InfoEmpresa infoEmpresa = new InfoEmpresa();

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_registro);

        edtNombre = (EditText) findViewById(R.id.edtRENombre);
        edtCIF = (EditText) findViewById(R.id.edtRECIF);
        edtEmail = (EditText) findViewById(R.id.edtREEmail);
        edtPass = (EditText) findViewById(R.id.edtREPass);
        edtConfirmPass = (EditText) findViewById(R.id.edtREConfirmPass);
        edtDireccion = (EditText) findViewById(R.id.edtREDireccion);
        edtSector = (EditText) findViewById(R.id.edtRESector);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null){

                }
            }
        };

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
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void registrarEmpresa(View view) {
        String nombre = String.valueOf(edtNombre.getText());
        String direccion = String.valueOf(edtDireccion.getText());
        String CIF = String.valueOf(edtCIF.getText());
        String email = String.valueOf(edtEmail.getText());
        String pass = String.valueOf(edtPass.getText());
        String confirmPass = String.valueOf(edtConfirmPass.getText());
        String sector = String.valueOf(edtSector.getText());

        if (CIF.isEmpty() || nombre.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || sector.isEmpty() || direccion.isEmpty()){
            Toast.makeText(this,"Complete todos los campos",Toast.LENGTH_SHORT).show();
        }else if (!email.contains("@")){
            edtEmail.setError("Introduce un email válido");
        }else if (pass.length() < 6){
            edtPass.setError("La contraseña debe tener 6 o más caracteres");
        }else if (!confirmPass.equalsIgnoreCase(pass)){
            edtPass.setError("Las contraseñas no coinciden");
        }else{
            firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Log.i("RegistroEmpresa","Excepción: " + task.getException().toString());
                        try {
                            throw task.getException();
                        }catch (FirebaseAuthInvalidUserException e){
                            crearUsuarioFirebase(email,pass);
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            Toast.makeText(EmpresaRegistroActivity.this, "Empresa ya registrada", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void crearUsuarioFirebase(String email, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(EmpresaRegistroActivity.this, "Error al registrar la empresa", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EmpresaRegistroActivity.this, "Empresa registrada correctamente", Toast.LENGTH_SHORT).show();
                    addToFirestore();
                    Empresa empresa = new Empresa(email, "claveEmpresa", "datos");
                    boolean insertadoOK = EmpresaController.insertarEmpresa(empresa);
                }
            }
        });
    }

    private void addToFirestore() {
        String nombre = String.valueOf(edtNombre.getText());
        String direccion = String.valueOf(edtDireccion.getText());
        String CIF = String.valueOf(edtCIF.getText());
        String email = String.valueOf(edtEmail.getText());
        String pass = String.valueOf(edtPass.getText());
        String confirmPass = String.valueOf(edtConfirmPass.getText());
        String sector = String.valueOf(edtSector.getText());

        Map<String, Object> empresa = new HashMap<>();
        empresa.put("Nombre", nombre);
        empresa.put("Dirección",direccion);
        empresa.put("CIF", CIF);
        empresa.put("Email", email);
        empresa.put("Sector", sector);

        infoEmpresa.setSector(sector);
        infoEmpresa.setNombre(nombre);
        infoEmpresa.setDireccion(direccion);
        infoEmpresa.setNombre(nombre);

        db.collection("Empresas").document(firebaseAuth.getCurrentUser().getUid()).set(empresa).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                createDocument();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Firestore", "Error al añadir el usuario en firestore", e);
            }
        });
    }
    private void createDocument(){
        Map<String, Object> data = new HashMap<>();
        db.collection("businessdata")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("Firestore","Usuario nuevo en firestore");
                firebaseAuth.signOut();
                Intent intent = new Intent(EmpresaRegistroActivity.this,EmpresaLoginActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        DocumentReference documentReference = db.collection("businessdata").document(firebaseAuth.getCurrentUser().getEmail());
        documentReference.collection("editinfoempresa")
                .document("infoEmpresa").set(infoEmpresa).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}