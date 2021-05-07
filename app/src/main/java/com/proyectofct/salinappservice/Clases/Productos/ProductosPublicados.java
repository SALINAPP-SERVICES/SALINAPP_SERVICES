package com.proyectofct.salinappservice.Clases.Productos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.proyectofct.salinappservice.Clases.Empresa.Empresa;

import java.io.Serializable;
import java.util.Objects;

public class ProductosPublicados implements Serializable {
    private int idproductoempresa;
    private int cantidad;
    private double precioventa;
    private boolean habilitado;
    private boolean archivado;
    private Producto p;
    private Empresa e;

    public ProductosPublicados(int idproductoempresa, int cantidad, double precioventa, boolean habilitado, boolean archivado, Producto p, Empresa e) {
        this.idproductoempresa = idproductoempresa;
        this.cantidad = cantidad;
        this.precioventa = precioventa;
        this.habilitado = habilitado;
        this.archivado = archivado;
        this.p = p;
        this.e = e;
    }

    public int getIdproductoempresa() {
        return idproductoempresa;
    }

    public void setIdproductoempresa(int idproductoempresa) {
        this.idproductoempresa = idproductoempresa;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(double precioventa) {
        this.precioventa = precioventa;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public boolean isArchivado() {
        return archivado;
    }

    public void setArchivado(boolean archivado) {
        this.archivado = archivado;
    }

    public Producto getP() {
        return p;
    }

    public void setP(Producto p) {
        this.p = p;
    }

    public Empresa getE() {
        return e;
    }

    public void setE(Empresa e) {
        this.e = e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductosPublicados that = (ProductosPublicados) o;
        return idproductoempresa == that.idproductoempresa;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(idproductoempresa);
    }

    @Override
    public String toString() {
        return "ProductosPublicados{" +
                "idproductoempresa=" + idproductoempresa +
                ", cantidad=" + cantidad +
                ", precioventa=" + precioventa +
                ", habilitado=" + habilitado +
                ", archivado=" + archivado +
                ", p=" + p +
                ", e=" + e +
                '}';
    }
}
