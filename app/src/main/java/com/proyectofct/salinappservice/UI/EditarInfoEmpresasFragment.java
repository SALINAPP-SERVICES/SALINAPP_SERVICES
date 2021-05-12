package com.proyectofct.salinappservice.UI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.proyectofct.salinappservice.Clases.Empresa.InfoEmpresa;
import com.proyectofct.salinappservice.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarInfoEmpresasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int LOGO_SEND = 1;

    private String mParam1;
    private String mParam2;

    private StorageReference storageReference;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    private CircleImageView logoEmpresa;
    private EditText nombreEmpresa;
    private EditText localizacionEmpresa, sector;
    private EditText cifEmpresa;
    private Button btnEditar;

    private InfoEmpresa infoEmpresa = new InfoEmpresa();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_editar_info_empresas, container, false);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        logoEmpresa = vista.findViewById(R.id.img_logo_editInfoE);
        nombreEmpresa = vista.findViewById(R.id.edt_nombre_editInfoE);
        localizacionEmpresa = vista.findViewById(R.id.edt_localizacion_editInfoE);
        cifEmpresa = vista.findViewById(R.id.edt_CIF_editInfoE);
        btnEditar = vista.findViewById(R.id.btn_editar_editInfoE);
        sector = vista.findViewById(R.id.edt_sector);

        ObtenerDatos();
        logoEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Seleccione una foto"), LOGO_SEND);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoEmpresa.setNombre(nombreEmpresa.getText().toString());
                infoEmpresa.setDireccion(localizacionEmpresa.getText().toString());
                infoEmpresa.setResumen(cifEmpresa.getText().toString());
                infoEmpresa.setCod_empresa(firebaseAuth.getCurrentUser().getEmail());
                infoEmpresa.setSector(sector.getText().toString());
                DocumentReference documentReference = db.collection("businessdata").document(firebaseAuth.getCurrentUser().getEmail());
                documentReference.collection("editinfoempresa")
                        .document("infoEmpresa").set(infoEmpresa).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Navigation.findNavController(vista).navigate(R.id.nav_editar_info_empresa);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        return vista;
    }

    private void ObtenerDatos(){
        DocumentReference documentReference = db.collection("businessdata").document(firebaseAuth.getCurrentUser().getEmail());
        documentReference.collection("editinfoempresa")
                .document("infoEmpresa").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){

                        infoEmpresa = document.toObject((InfoEmpresa.class));
                        Glide.with(EditarInfoEmpresasFragment.this).load(infoEmpresa.getLogoURL()).into(logoEmpresa);
                        nombreEmpresa.setText(infoEmpresa.getNombre());
                        localizacionEmpresa.setText(infoEmpresa.getDireccion());
                        cifEmpresa.setText(infoEmpresa.getCif());
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error al leer datos", "", e);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGO_SEND && resultCode == Activity.RESULT_OK) {
            Uri u = data.getData();
            storageReference = storage.getReference("logo_empresa");
            final StorageReference logoReferencia = storageReference.child(u.getLastPathSegment());
            logoReferencia.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    infoEmpresa.setLogoURL(imageUrl);
                                    Glide.with(EditarInfoEmpresasFragment.this).load(imageUrl).into(logoEmpresa);
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}