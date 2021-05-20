package com.proyectofct.salinappservice.Clases.Clientes;

import java.io.Serializable;

public class Direcciones implements Serializable {
    private int idDireccion;
    private String direccion;

    public Direcciones(int idDireccion, String direccion) {
        this.idDireccion = idDireccion;
        this.direccion = direccion;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
