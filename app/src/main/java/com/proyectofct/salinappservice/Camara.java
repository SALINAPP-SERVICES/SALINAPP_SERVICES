package com.proyectofct.salinappservice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class Camara extends AppCompatActivity {
    public static String IMAGEN = "";
    ImageView ivFoto;
    String rutaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        ivFoto = findViewById(R.id.ivFoto);

        TomarFoto();

        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TomarImagen();
            }
        });
    }

    public void TomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if(intent.resolveActivity(getPackageManager())!= null) {
        File imagenArchivo = null;
        try{
            imagenArchivo = crearImagen();
        } catch(IOException ex){
            Log.e("ERROR", ex.toString());
        }

        if(imagenArchivo != null){
            Uri fotoUri = FileProvider.getUriForFile(this, "com.proyectofct.salinappservice", imagenArchivo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            startActivityForResult(intent, 1);
        }

        //}
    }

    public void TomarImagen() {
        final CharSequence[] opciones={"Elegir producto","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(Camara.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Elegir producto")){
                    Intent intent = new Intent(Camara.this, HomeActivity.class);
                    IMAGEN = rutaImagen;
                    startActivity(intent);
                }else{
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            ivFoto.setImageBitmap(imgBitmap);
        }
    }

    public File crearImagen() throws IOException {
        String nombreImagen = "IMG-";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);

        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }

    public File getDirectorio() throws IOException{
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return directorio;
    }
}