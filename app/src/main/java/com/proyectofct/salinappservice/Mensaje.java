package com.proyectofct.salinappservice;

public class Mensaje {

    //ATRIBUTOS
    private String mensaje;
    private String nombre;
    private String fotoPerfil;
    private String tipoMensaje;
    private String imagenUrl;

    //CONSTRUCTORES
    public Mensaje() {

    }

    public Mensaje(String mensaje, String nombre, String fotoPerfil, String tipoMensaje) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.tipoMensaje = tipoMensaje;
    }

    public Mensaje(String mensaje, String imagenUrl, String nombre, String fotoPerfil, String tipoMensaje) {
        this.mensaje = mensaje;
        this.imagenUrl = imagenUrl;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.tipoMensaje = tipoMensaje;
    }

    //GETTERS & SETTERS
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
