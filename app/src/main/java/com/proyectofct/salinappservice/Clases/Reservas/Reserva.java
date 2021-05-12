package com.proyectofct.salinappservice.Clases.Reservas;

import com.google.type.DateTime;

import java.time.LocalDateTime;

public class Reserva {
    private int idReserva;
    private LocalDateTime fechaReserva;
    private double total;
    private int idDireccionCliente;

    public Reserva(int idReserva, LocalDateTime fechaReserva, double total) {
        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
        this.total = total;
    }

    public Reserva(int idReserva, LocalDateTime fechaReserva, double total, int idDireccionCliente) {
        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
        this.total = total;
        this.idDireccionCliente = idDireccionCliente;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdDireccionCliente() {
        return idDireccionCliente;
    }

    public void setIdDireccionCliente(int idDireccionCliente) {
        this.idDireccionCliente = idDireccionCliente;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", fechaReserva=" + fechaReserva +
                ", total=" + total +
                /*", idDireccionCliente=" + idDireccionCliente +*/
                '}';
    }
}
