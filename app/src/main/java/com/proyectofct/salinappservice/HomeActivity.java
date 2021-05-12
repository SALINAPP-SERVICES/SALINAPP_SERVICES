package com.proyectofct.salinappservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db;

    private NavigationView navigationView;

    private MenuItem logoutMenu, loginMenu, volverInicio, nav_empresas, nav_galeria_productos, nav_carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_fragment_empresas, R.id.nav_fragment_productos_publicados, R.id.nav_fragment_detalle_productos_publicados, R.id.nav_fragment_carrito , R.id.nav_fragment_editar_info_empresas).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        db = FirebaseFirestore.getInstance();

        logoutMenu = (MenuItem) navigationView.getMenu().findItem(R.id.logoutMenu);
        loginMenu = (MenuItem) navigationView.getMenu().findItem(R.id.loginMenu);
        volverInicio = (MenuItem) navigationView.getMenu().findItem(R.id.volverInicio);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser usuario = firebaseAuth.getCurrentUser();
            if(usuario != null){
                View headerView = navigationView.getHeaderView(0);

                TextView txtUsuarioMenu = (TextView) headerView.findViewById(R.id.txtUsuarioBanner);
                TextView txtCorreoMenu = (TextView) headerView.findViewById(R.id.txtCorreoBanner);

                txtUsuarioMenu.setText(usuario.getDisplayName());
                txtCorreoMenu.setText(usuario.getEmail());

                logoutMenu.setVisible(true);
                loginMenu.setVisible(false);

                DocumentReference docRef = db.collection("Usuarios").document(usuario.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) { //El documento corresponde a un usuario
                                navigationView.getMenu().setGroupVisible(R.id.menuUsuarios, true);
                                navigationView.getMenu().setGroupVisible(R.id.menuEmpresas, false);
                                Log.d("", "DocumentSnapshot data: " + document.getData());
                            } else { //El documento corresponde a una empresa
                                navigationView.getMenu().setGroupVisible(R.id.menuUsuarios, false);
                                navigationView.getMenu().setGroupVisible(R.id.menuEmpresas, true);
                                Log.d("", "No such document");
                            }
                        } else {
                            Log.d("", "get failed with ", task.getException());
                        }
                    }
                });
            }else {
                View headerView = navigationView.getHeaderView(0);

                TextView txtUsuarioMenu = (TextView) headerView.findViewById(R.id.txtUsuarioBanner);
                TextView txtCorreoMenu = (TextView) headerView.findViewById(R.id.txtCorreoBanner);

                txtUsuarioMenu.setText("INVITADO");
                txtCorreoMenu.setText("");

                logoutMenu.setVisible(false);
                loginMenu.setVisible(true);

                navigationView.getMenu().setGroupVisible(R.id.menuUsuarios, false); //Como es lógico, los invitados no pueden ver el menú de usuarios
                navigationView.getMenu().setGroupVisible(R.id.menuEmpresas, false); //Como es lógico, los invitados no pueden ver el menú de empresas
            }
        }};

        logoutMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                firebaseAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, BienvenidaActivity.class);
                startActivity(intent);
                return false;
            }
        });

        loginMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });

        volverInicio.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(HomeActivity.this, BienvenidaActivity.class);
                startActivity(intent);
                return false;
            }
        });

        nav_empresas = (MenuItem) navigationView.getMenu().findItem(R.id.nav_empresas);
        nav_galeria_productos = (MenuItem) navigationView.getMenu().findItem(R.id.nav_galeria_productos);
        nav_carrito = (MenuItem) navigationView.getMenu().findItem(R.id.nav_carrito);

        nav_empresas.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Navigation.findNavController(menuItem.getActionView()).navigate(R.id.nav_fragment_empresas);
                return false;
            }
        });

        nav_galeria_productos.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Navigation.findNavController(menuItem.getActionView()).navigate(R.id.nav_fragment_galeria_productos);
                return false;
            }
        });

        nav_carrito.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Navigation.findNavController(menuItem.getActionView()).navigate(R.id.nav_fragment_carrito);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
}