package com.proyectofct.salinappservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    //ATRIBUTOS
    private List<MensajeRecibir> listaMensajes = new ArrayList<>();
    private Context c;

    //CONTRUCTOR
    public AdapterMensajes(Context c) {
        this.c = c;
    }

    //MÉTODOS

    public void insertarMensaje(MensajeRecibir mensaje){
        listaMensajes.add(mensaje);
        notifyItemInserted(listaMensajes.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        holder.getTxtNombreMensaje().setText(listaMensajes.get(position).getNombre());
        holder.getTxtMensajeMensaje().setText(listaMensajes.get(position).getMensaje());

        if(listaMensajes.get(position).getTipoMensaje().equals("2")){
            holder.getImagenMensaje().setVisibility(View.VISIBLE);
            holder.getTxtMensajeMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(listaMensajes.get(position).getImagenUrl()).into(holder.getImagenMensaje());
        }else if(listaMensajes.get(position).getTipoMensaje().equals("1")){
            holder.getImagenMensaje().setVisibility(View.GONE);
            holder.getTxtMensajeMensaje().setVisibility(View.VISIBLE);
        }

        if(listaMensajes.get(position).getFotoPerfil().isEmpty()){
            holder.getCivFotoPerfilMensaje().setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(c).load(listaMensajes.get(position).getFotoPerfil()).into(holder.getCivFotoPerfilMensaje());
        }

        Long codigoHora = listaMensajes.get(position).getHora();
        Date d = new Date(codigoHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a"); //La 'a' indica PM o AM
        holder.getTxtHoraMensaje().setText(sdf.format(d));
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }
}
