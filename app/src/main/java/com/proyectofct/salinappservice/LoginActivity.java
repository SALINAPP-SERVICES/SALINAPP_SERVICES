package com.proyectofct.salinappservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInOptions gso;

    private EditText edtLUsuario;
    private EditText edtLContraseña;

    private boolean nuevoUsuario;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLUsuario = (EditText) findViewById(R.id.edtLUsuario);
        edtLContraseña = (EditText) findViewById(R.id.edtLContraseña);

        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser usuario = firebaseAuth.getCurrentUser();
            if(usuario != null){
                if(!nuevoUsuario){
                    Toast.makeText(LoginActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }};

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        nuevoUsuario = false;
    }

    private void signIn() {
        googleSignOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void googleSignOut(){
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LoginActivity.this, "Sesión de Google cerrada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth.signInWithEmailAndPassword(account.getEmail(), "123456").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Log.i("Error", "Excepción " + task.getException().toString());
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e){
                                nuevoUsuario = true;
                                //Toast.makeText(LoginActivity.this, "Usuario no encontrado", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                //Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                Log.d("", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("", "signInWithCredential:success");
                    if(nuevoUsuario){
                        addToFirebase("Usuarios"); //Las empresas no se pueden registrar con su cuenta de Google :)
                    }
                    //FirebaseUser user = firebaseAuth.getCurrentUser();
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("", "signInWithCredential:failure", task.getException());
                    //updateUI(null);
                }
            }
        });
    }

    private void addToFirebase(String tipoUsuario) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("Nombre", firebaseAuth.getCurrentUser().getDisplayName());
        usuario.put("Email", firebaseAuth.getCurrentUser().getEmail());
        usuario.put("Teléfono", "");

        db.collection(tipoUsuario).document(firebaseAuth.getCurrentUser().getUid()).set(usuario).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("", "Nuevo usuario en firebase");
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("", "Error al añadir el usuario en firebase", e);
            }
        });
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
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    public void inciarSesión(View view) {
        validacionIncioSesión();
    }

    public void inciarSesiónGoogle(View view) {
        signIn();
    }

    public void inciarSesiónFacebook(View view) {
        Toast.makeText(this, "Opción no disponible por el momento", Toast.LENGTH_LONG).show();
    }

    public void recordarContraseña(View view) {
        Intent intent = new Intent(LoginActivity.this, RecuperarPasswordActivity.class);
        startActivity(intent);
    }

    private void validacionIncioSesión() {
        String usuario = String.valueOf(edtLUsuario.getText());
        String contraseña = String.valueOf(edtLContraseña.getText());

        if (usuario.isEmpty() && contraseña.isEmpty()){
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
        }else if (!usuario.contains("@")){
            edtLUsuario.setError("Formato de usuario incorrecto (debe incluir @)");
        }else if (contraseña.length() < 6){
            edtLContraseña.setError("La contraseña debe de tener 6 caracteres o más");
        }else{
            login(usuario, contraseña);
        }
    }

    private void login(String usuario, String contraseña) {
        firebaseAuth.signInWithEmailAndPassword(usuario, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.i("Error", "Excepción " + task.getException().toString());
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        Toast.makeText(LoginActivity.this, "Usuario no encontrado", Toast.LENGTH_LONG).show();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}