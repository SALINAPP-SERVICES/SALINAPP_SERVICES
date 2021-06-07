package com.proyectofct.salinappservice.Clases.Reservas;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyectofct.salinappservice.R;

import org.jetbrains.annotations.NotNull;

public class ReservaViewHolder extends RecyclerView.ViewHolder {

    public ListaReservasAdapter listaReservasAdapter;
    public TextView txtIdReserva = null;
    public TextView txtFechaReserva = null;
    public TextView txtTotalReserva = null;
    public TextView txtDireccionClienteReserva = null;
    public TextView txtDatosClienteReserva = null;

    public ReservaViewHolder(@NonNull @NotNull View itemView, ListaReservasAdapter listaReservasAdapter) {
        super(itemView);
        txtIdReserva = itemView.findViewById(R.id.txtIdReserva);
        txtFechaReserva = itemView.findViewById(R.id.txtFechaReserva);
        txtTotalReserva = itemView.findViewById(R.id.txtTotalReserva);
        txtDireccionClienteReserva = itemView.findViewById(R.id.txtDireccionClienteReserva);
        txtDatosClienteReserva = itemView.findViewById(R.id.txtDatosClienteReserva);
        this.listaReservasAdapter = listaReservasAdapter;
    }
}
