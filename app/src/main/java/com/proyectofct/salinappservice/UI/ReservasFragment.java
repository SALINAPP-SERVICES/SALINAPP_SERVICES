package com.proyectofct.salinappservice.UI;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proyectofct.salinappservice.Clases.Reservas.ListaReservasAdapter;
import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Controladores.ReservaController;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;
import com.proyectofct.salinappservice.R;

import java.util.ArrayList;

public class ReservasFragment extends Fragment {
    private RecyclerView rvReservas;
    private ArrayList<Reserva> reservas;
    private ListaReservasAdapter listaReservasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_reservas, container, false);

        //PREPARO EL RECYCLER VIEW
        rvReservas = (RecyclerView) vista.findViewById(R.id.rvReservas);
        reservas = ReservaController.obtenerReservas();
        if (reservas != null){
            listaReservasAdapter = new ListaReservasAdapter(getActivity(), reservas);
            rvReservas.setAdapter(listaReservasAdapter);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                rvReservas.setLayoutManager(new LinearLayoutManager(getActivity()));
            }else {
                rvReservas.setLayoutManager(new GridLayoutManager(getActivity(), ConfiguracionesGeneralesDB.LANDSCAPE_NUM_COLUMNAS));
            }
        }else {
            Log.i("", "Las reservas están vacías");
        }

        return vista;
    }
}