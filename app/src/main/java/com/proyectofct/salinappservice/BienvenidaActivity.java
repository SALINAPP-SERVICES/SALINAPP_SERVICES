package com.proyectofct.salinappservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BienvenidaActivity extends AppCompatActivity {

    public static boolean EMPRESA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
    }

    public void loginUsuarios(View view) {
        Intent intent = new Intent(BienvenidaActivity.this, LoginActivity.class);
        EMPRESA = false;
        startActivity(intent);
    }

    public void loginEmpresas(View view) {
        Intent intent = new Intent(BienvenidaActivity.this, EmpresaLoginActivity.class);
        EMPRESA = true;
        startActivity(intent);
    }
}