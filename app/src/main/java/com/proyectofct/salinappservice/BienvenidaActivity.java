package com.proyectofct.salinappservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BienvenidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
    }

    public void entrarALaApp(View view) {
        Intent intent = new Intent(BienvenidaActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void inciarSesión(View view) {
        Intent intent = new Intent(BienvenidaActivity.this, EmpresaLoginActivity.class);
        startActivity(intent);
    }
}