package com.proyectofct.salinappservice.Clases.Reservas;

import com.proyectofct.salinappservice.Clases.Clientes.DireccionesClientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Reserva implements Serializable {
    private int idReserva;
    private ArrayList<LíneaReserva> líneasReserva;
    private Date fechaReserva;
    private double total;
    private DireccionesClientes idDireccionCliente;

    public Reserva(int idReserva, ArrayList<LíneaReserva> líneasReserva, Date fechaReserva, double total) {
        this.idReserva = idReserva;
        this.líneasReserva = líneasReserva;
        this.fechaReserva = fechaReserva;
        this.total = total;
    }

    public Reserva(int idReserva, ArrayList<LíneaReserva> líneasReserva, Date fechaReserva, double total, DireccionesClientes idDireccionCliente) {
        this.idReserva = idReserva;
        this.líneasReserva = líneasReserva;
        this.fechaReserva = fechaReserva;
        this.total = total;
        this.idDireccionCliente = idDireccionCliente;
    }

    public Reserva() {
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public ArrayList<LíneaReserva> getLíneasReserva() {
        return líneasReserva;
    }

    public void setLíneasReserva(ArrayList<LíneaReserva> líneasReserva) {
        this.líneasReserva = líneasReserva;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public DireccionesClientes getIdDireccionCliente() {
        return idDireccionCliente;
    }

    public void setIdDireccionCliente(DireccionesClientes idDireccionCliente) {
        this.idDireccionCliente = idDireccionCliente;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", líneasReserva=" + líneasReserva +
                ", fechaReserva=" + fechaReserva +
                ", total=" + total +
                ", idDireccionCliente=" + idDireccionCliente +
                '}';
    }
}
