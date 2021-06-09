package com.proyectofct.salinappservice.Clases.Clientes;

import java.io.Serializable;

public class Cliente implements Serializable {
    private int idCliente;
    private String email;
    private String contraseña;
    private String datos;

    public Cliente(int idCliente, String email, String contraseña, String datos) {
        this.idCliente = idCliente;
        this.email = email;
        this.contraseña = contraseña;
        this.datos = datos;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", email='" + email + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", datos='" + datos + '\'' +
                '}';
    }
}
