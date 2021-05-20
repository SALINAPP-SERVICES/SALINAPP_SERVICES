package com.proyectofct.salinappservice.Clases.Reservas;

import com.proyectofct.salinappservice.Clases.Productos.ProductoPublicado;

import java.io.Serializable;

public class LíneaReserva implements Serializable {
    private int idLíneaReserva;
    private int idReserva;
    private ProductoPublicado productoPublicado;
    private int cantidad;

    public LíneaReserva(int idLíneaReserva, int idReserva, ProductoPublicado productoPublicado, int cantidad) {
        this.idLíneaReserva = idLíneaReserva;
        this.idReserva = idReserva;
        this.productoPublicado = productoPublicado;
        this.cantidad = cantidad;
    }

    public int getIdLíneaReserva() {
        return idLíneaReserva;
    }

    public void setIdLíneaReserva(int idLíneaReserva) {
        this.idLíneaReserva = idLíneaReserva;
    }

    public int getReserva() {
        return idReserva;
    }

    public void setReserva(int reserva) {
        this.idReserva = idReserva;
    }

    public ProductoPublicado getProductoPublicado() {
        return productoPublicado;
    }

    public void setProductoPublicado(ProductoPublicado productoPublicado) {
        this.productoPublicado = productoPublicado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "LíneaReserva{" +
                "idLíneaReserva=" + idLíneaReserva +
                ", idReserva=" + idReserva +
                ", idProductoEmpresa=" + productoPublicado +
                ", cantidad=" + cantidad +
                '}';
    }
}
