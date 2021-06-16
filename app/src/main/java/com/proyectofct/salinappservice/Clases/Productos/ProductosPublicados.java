package com.proyectofct.salinappservice.Clases.Productos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.proyectofct.salinappservice.Clases.Empresa.Empresa;

import java.io.Serializable;
import java.util.Objects;

public class ProductosPublicados implements Serializable {
    private int idProductoEmpresa;
    private int cantidad;
    private double precioventa;
    private boolean habilitado;
    private boolean archivado;
    private int id_foto;
    private Producto p;
    private Empresa e;

    public ProductosPublicados(int idproductoempresa, int cantidad, double precioventa, boolean habilitado, boolean archivado, Producto p, Empresa e) {
        this.idProductoEmpresa = idproductoempresa;
        this.cantidad = cantidad;
        this.precioventa = precioventa;
        this.habilitado = habilitado;
        this.archivado = archivado;
        this.p = p;
        this.e = e;
    }

    public int getIdProductoEmpresa() {
        return idProductoEmpresa;
    }

    public void setIdProductoEmpresa(int idProductoEmpresa) {
        this.idProductoEmpresa = idProductoEmpresa;
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
        return idProductoEmpresa == that.idProductoEmpresa;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(idProductoEmpresa);
    }

    @Override
    public String toString() {
        return ((Moda) p).getTalla() + " " + ((Moda) p).getColor();
    }
}
