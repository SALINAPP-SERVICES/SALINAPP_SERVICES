package com.proyectofct.salinappservice;

public class MensajeRecibir extends Mensaje {

    //ATRIBUTOS
    private Long hora;

    //CONSTRUCTOR
    public MensajeRecibir() {

    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String imagenUrl, String nombre, String fotoPerfil, String tipoMensaje, Long hora) {
        super(mensaje, imagenUrl, nombre, fotoPerfil, tipoMensaje);
        this.hora = hora;
    }

    //GETTERS & SETTERS
    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
