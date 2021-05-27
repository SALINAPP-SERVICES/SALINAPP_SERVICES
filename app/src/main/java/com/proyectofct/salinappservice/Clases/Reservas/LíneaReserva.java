package com.proyectofct.salinappservice.Clases.Reservas;

import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;

import java.io.Serializable;

public class LíneaReserva implements Serializable {
    private int idLíneaReserva;
    private int idReserva;
    private ProductosPublicados productosPublicados;
    private int cantidad;

    public LíneaReserva(int idLíneaReserva, int idReserva, ProductosPublicados productosPublicados, int cantidad) {
        this.idLíneaReserva = idLíneaReserva;
        this.idReserva = idReserva;
        this.productosPublicados = productosPublicados;
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

    public ProductosPublicados getProductoPublicado() {
        return productosPublicados;
    }

    public void setProductoPublicado(ProductosPublicados productosPublicados) {
        this.productosPublicados = productosPublicados;
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
                ", idProductoEmpresa=" + productosPublicados +
                ", cantidad=" + cantidad +
                '}';
    }
}
