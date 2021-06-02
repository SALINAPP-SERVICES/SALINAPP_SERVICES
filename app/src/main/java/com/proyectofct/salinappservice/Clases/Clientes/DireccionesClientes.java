package com.proyectofct.salinappservice.Clases.Clientes;

import java.io.Serializable;

public class DireccionesClientes implements Serializable {
    private int idDireccionCliente;
    private Direcciones direccion;
    private Cliente cliente;

    public DireccionesClientes(int idDireccionCliente, Direcciones direccion, Cliente cliente) {
        this.idDireccionCliente = idDireccionCliente;
        this.direccion = direccion;
        this.cliente = cliente;
    }

    public DireccionesClientes() {
    }

    public int getIdDireccionCliente() {
        return idDireccionCliente;
    }

    public void setIdDireccionCliente(int idDireccionCliente) {
        this.idDireccionCliente = idDireccionCliente;
    }

    public Direcciones getDireccion() {
        return direccion;
    }

    public void setDireccion(Direcciones direccion) {
        this.direccion = direccion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
