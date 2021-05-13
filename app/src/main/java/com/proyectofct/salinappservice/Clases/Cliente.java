package com.proyectofct.salinappservice.Clases;

import java.io.Serializable;

public class Cliente implements Serializable {
    private int idCliente;
    private String email;
    private String pass;
    private String datos;

    public Cliente(int idCliente, String email, String pass, String datos) {
        this.idCliente = idCliente;
        this.email = email;
        this.pass = pass;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }
}
