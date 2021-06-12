package com.proyectofct.salinappservice.UI;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyectofct.salinappservice.BienvenidaActivity;
import com.proyectofct.salinappservice.Clases.Empresa.InfoEmpresa;
import com.proyectofct.salinappservice.Clases.Empresa.ListaEmpresasAdapter;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;
import com.proyectofct.salinappservice.Utilidades.RecyclerItemClickListener;

import java.util.ArrayList;

import static com.proyectofct.salinappservice.Clases.Empresa.EmpresaViewHolder.EXTRA_OBJETO_EMPRESA;

public class EmpresasFragment extends Fragment {
    private RecyclerView rvEmpresas;
    private ListaEmpresasAdapter listaEmpresasAdapter;
    private ArrayList<InfoEmpresa> infoEmpresas;
    private InfoEmpresa miEmpresa;
    private int pagina_actual;
    private int total_registros;
    private int total_paginas;
    private int num_columnas_landscape;

    private FirebaseFirestore db;
    private InfoEmpresa infoEmpresa;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_empresas, container, false);

        //COGER ELEMENTOS DE LA BASE DE DATOS DE FIREBASE
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        infoEmpresas = new ArrayList<InfoEmpresa>();
        rvEmpresas = (RecyclerView) vista.findViewById(R.id.rv_empresas);
        listaEmpresasAdapter = new ListaEmpresasAdapter(getActivity(), infoEmpresas);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvEmpresas.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            rvEmpresas.setLayoutManager(new GridLayoutManager(getActivity(), ConfiguracionesGeneralesDB.LANDSCAPE_NUM_COLUMNAS));
        }
        rvEmpresas.setAdapter(listaEmpresasAdapter);

        db.collection("businessdata").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.i("illo", "Coge los datos de businessdata");
                if (task.isSuccessful()) {
                    Log.i("illo", "Tiene exito coger datos");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference documentReference = db.collection("businessdata").document(document.getId()).collection("editinfoempresa").document("infoEmpresa");
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.i("illo", "Tiene exito coger el documento infoEmpresa");
                                    DocumentSnapshot info = task.getResult();
                                    if (info.exists()) {
                                        infoEmpresa = info.toObject(InfoEmpresa.class);
                                        if (BienvenidaActivity.EMPRESA == true){
                                            if(firebaseAuth.getCurrentUser().getEmail().equals(infoEmpresa.getCod_empresa())){
                                               listaEmpresasAdapter.addEmpresa(infoEmpresa);
                                                Log.i("illo", "Añade mi empresa al ArrayList");
                                            }
                                        } else{
                                            listaEmpresasAdapter.addEmpresa(infoEmpresa);
                                            Log.i("illo", "Añade empresas al ArrayList");
                                        }
                                        //mAdapter.addEmpresa(infoEmpresa);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        rvEmpresas.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rvEmpresas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                infoEmpresa = listaEmpresasAdapter.getEmpresa(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_OBJETO_EMPRESA, infoEmpresa);
                Navigation.findNavController(view).navigate(R.id.nav_fragment_productos_publicados, bundle);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        /*
        //PAGINACIÓN
        rvEmpresas.addOnScrollListener(new PaginationListener((LinearLayoutManager) rvEmpresas.getLayoutManager()) {
        private int empresas_leidas = 0;
        @Override
        protected void loadMoreItems() {
            int total_registros_leidos = rvEmpresas.getLayoutManager().getItemCount();
            if (total_registros_leidos < total_registros) {
                ArrayList<InfoEmpresa> nuevasEmpresas = EmpresaController.obtenerEmpresas(pagina_actual);
                empresas_leidas = nuevasEmpresas.size();
                pagina_actual++;

                Boolean resultado = rvEmpresas.post(new Runnable()
                {
                    @Override
                    public void run() {
                        ListaEmpresasAdapter mAdapter1 = (ListaEmpresasAdapter) rvEmpresas.getAdapter();
                        ArrayList<InfoEmpresa> empresas_rv = mAdapter1.getListaEmpresas();
                        empresas_rv.addAll(nuevasEmpresas);
                        rvEmpresas.getAdapter().notifyDataSetChanged();
                    }});
                Log.i("sql", "siguiente pagina -> " + String.valueOf(pagina_actual));
                Log.i("sql", "total registros -> " + String.valueOf(total_registros));
                Log.i("sql", "total registros leidos -> " + String.valueOf(total_registros_leidos));
                Log.i("sql", "empresas leidas -> " + String.valueOf(this.empresas_leidas));

            }
            else{
                empresas_leidas = 0;
            }
        }
        @Override
        public boolean isLastPage() {
            if(pagina_actual > total_paginas -1 ) {
                return true;
            }
            else{
                return false;
            }
        }});
        */
        return vista;
    }


    private void mostrarToast (String texto){
        Toast.makeText(getActivity(), texto, Toast.LENGTH_SHORT).show();
    }

}