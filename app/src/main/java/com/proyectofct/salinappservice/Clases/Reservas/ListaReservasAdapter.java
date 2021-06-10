package com.proyectofct.salinappservice.Clases.Reservas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyectofct.salinappservice.Controladores.ReservaController;
import com.proyectofct.salinappservice.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListaReservasAdapter extends RecyclerView.Adapter<ReservaViewHolder> {
    private Context c;
    private ArrayList<Reserva> listaReservas;
    private LayoutInflater mInflater;

    public ListaReservasAdapter(Context c, ArrayList<Reserva> listaReservas) {
        this.c = c;
        this.listaReservas = listaReservas;
        mInflater = LayoutInflater.from(c);
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public ArrayList<Reserva> getListaReservas() {
        return listaReservas;
    }

    public void setListaReservas(ArrayList<Reserva> listaReservas) {
        this.listaReservas = listaReservas;
    }

    @NonNull
    @NotNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista = mInflater.inflate(R.layout.item_recyclerview_reserva, parent, false);
        return new ReservaViewHolder(vista, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReservaViewHolder holder, int position) {
        Reserva reservaActual = listaReservas.get(position);
        holder.txtIdReserva.setText("ID de la reserva: " + reservaActual.getIdReserva());
        holder.txtFechaReserva.setText("Fecha de la reserva: " + reservaActual.getFechaReserva());
        holder.txtTotalReserva.setText("Precio total de la reserva: " + reservaActual.getTotal() + "€");
        holder.txtDatosClienteReserva.setText("Datos del cliente: " + reservaActual.getIdDireccionCliente().getCliente().getDatos());
        holder.txtDireccionClienteReserva.setText("Dirección del cliente: " + reservaActual.getIdDireccionCliente().getDireccion().getDireccion());

        int cancelado = reservaActual.getCancelado();

        if (cancelado == 1){
            holder.btCancelarReserva.setText("Reserva cancelada");
            holder.btCancelarReserva.setEnabled(false);
        }

        holder.btCancelarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertaBorrar = new AlertDialog.Builder(getC());
                alertaBorrar.setTitle("¿Desea cancelar la reserva?");
                alertaBorrar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean insertadoOk = ReservaController.actualizarReserva(reservaActual);
                        if (insertadoOk){
                            holder.btCancelarReserva.setText("Reserva cancelada");
                            holder.btCancelarReserva.setEnabled(false);
                            Toast.makeText(getC(), "Reserva cancelada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertaBorrar.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertaBorrar.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listaReservas != null){
            return listaReservas.size();
        }else {
            return 0;
        }
    }
}
