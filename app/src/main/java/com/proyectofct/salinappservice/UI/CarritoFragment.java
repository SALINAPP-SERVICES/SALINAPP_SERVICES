package com.proyectofct.salinappservice.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.R;

import static com.proyectofct.salinappservice.UI.DetalleProductosPublicadosFragment.EXTRA_OBJETO_LÍNEA_RESERVA;

public class CarritoFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_carrito, container, false);

        LíneaReserva líneaReserva = (LíneaReserva) getArguments().getSerializable(EXTRA_OBJETO_LÍNEA_RESERVA);
        if (líneaReserva != null){

        }else {
            Log.i("ERROR", "La línea de reserva no se ha recibido del fragment anterior correctamente");
        }

        return vista;
    }
}