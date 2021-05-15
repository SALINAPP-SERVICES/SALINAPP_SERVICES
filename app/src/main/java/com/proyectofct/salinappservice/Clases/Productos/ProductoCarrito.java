package com.proyectofct.salinappservice.Clases.Productos;

import java.io.Serializable;

public class ProductoCarrito implements Serializable {
    private String codProducto;
    private int cantidad;
    private String descripción;
    private String codEmpresa;
    private String fotoURL;
    private String marca;
    private String modelo;
    private double precio;

    public ProductoCarrito(String codProducto, int cantidad, String descripción, String codEmpresa, String fotoURL, String marca, String modelo, double precio) {
        this.codProducto = codProducto;
        this.cantidad = cantidad;
        this.descripción = descripción;
        this.codEmpresa = codEmpresa;
        this.fotoURL = fotoURL;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
    }

    public ProductoCarrito() {

    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ProductoCarrito{" +
                "codProducto=" + codProducto +
                ", cantidad=" + cantidad +
                ", descripción='" + descripción + '\'' +
                ", codEmpresa='" + codEmpresa + '\'' +
                ", fotoURL='" + fotoURL + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precio='" + precio + '\'' +
                '}';
    }
}
