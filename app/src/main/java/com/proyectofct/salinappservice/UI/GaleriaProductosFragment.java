package com.proyectofct.salinappservice.UI;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proyectofct.salinappservice.Clases.Productos.ListaProductosPublicadosAdapter;
import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Controladores.ProductoPublicadoController;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.util.ArrayList;

public class GaleriaProductosFragment extends Fragment {

    private RecyclerView rvProductosBuscados;
    private ListaProductosPublicadosAdapter listaProductosPublicadosAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_galeria_productos, container, false);
        String busqueda = (String) getArguments().getSerializable(EmpresasFragment.EXTRA_OBJETO_PRODUCTOS);
        String[] palabras = busqueda.split("\\s+");
        ArrayList<ProductosPublicados> productosbuscados= ProductoPublicadoController.buscarProductoPublicado(palabras);
        rvProductosBuscados = (RecyclerView) vista.findViewById(R.id.rvProductosBuscados);
        if (productosbuscados != null) {
            System.out.println("Obtiene productos");
            listaProductosPublicadosAdapter = new ListaProductosPublicadosAdapter(getActivity(),productosbuscados);
            rvProductosBuscados.setAdapter(listaProductosPublicadosAdapter);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                rvProductosBuscados.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                rvProductosBuscados.setLayoutManager(new GridLayoutManager(getActivity(), ConfiguracionesGeneralesDB.LANDSCAPE_NUM_COLUMNAS));
            }
        }
        return vista;
    }
}