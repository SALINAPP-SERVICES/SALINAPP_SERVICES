package com.proyectofct.salinappservice;

import java.util.Map;

public class MensajeEnviar extends Mensaje {

    //ATRIBUTOS
    private Map hora;

    //CONSTRUCTORES
    public MensajeEnviar() {

    }

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String imagenUrl, String nombre, String fotoPerfil, String tipoMensaje, Map hora) {
        super(mensaje, imagenUrl, nombre, fotoPerfil, tipoMensaje);
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String tipoMensaje, Map hora) {
        super(mensaje, nombre, fotoPerfil, tipoMensaje);
        this.hora = hora;
    }

    //GETTER & SETTER
    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
