package com.proyectofct.salinappservice;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderMensaje extends RecyclerView.ViewHolder {

    private TextView txtNombreMensaje;
    private TextView txtMensajeMensaje;
    private TextView txtHoraMensaje;
    private CircleImageView civFotoPerfilMensaje;
    private ImageView imagenMensaje;

    public HolderMensaje(@NonNull View itemView) {
        super(itemView);
        txtNombreMensaje = (TextView) itemView.findViewById(R.id.txtNombreMensaje);
        txtMensajeMensaje = (TextView) itemView.findViewById(R.id.txtMensajeMensaje);
        txtHoraMensaje = (TextView) itemView.findViewById(R.id.txtHoraMensaje);
        civFotoPerfilMensaje = (CircleImageView) itemView.findViewById(R.id.civFotoPerfilMensaje);
        imagenMensaje = itemView.findViewById(R.id.imagenMensaje);
    }

    public TextView getTxtNombreMensaje() {
        return txtNombreMensaje;
    }

    public void setTxtNombreMensaje(TextView txtNombreMensaje) {
        this.txtNombreMensaje = txtNombreMensaje;
    }

    public TextView getTxtMensajeMensaje() {
        return txtMensajeMensaje;
    }

    public void setTxtMensajeMensaje(TextView txtMensajeMensaje) {
        this.txtMensajeMensaje = txtMensajeMensaje;
    }

    public TextView getTxtHoraMensaje() {
        return txtHoraMensaje;
    }

    public void setTxtHoraMensaje(TextView txtHoraMensaje) {
        this.txtHoraMensaje = txtHoraMensaje;
    }

    public CircleImageView getCivFotoPerfilMensaje() {
        return civFotoPerfilMensaje;
    }

    public void setCivFotoPerfilMensaje(CircleImageView civFotoPerfilMensaje) {
        this.civFotoPerfilMensaje = civFotoPerfilMensaje;
    }

    public ImageView getImagenMensaje() {
        return imagenMensaje;
    }

    public void setImagenMensaje(ImageView imagenMensaje) {
        this.imagenMensaje = imagenMensaje;
    }
}
