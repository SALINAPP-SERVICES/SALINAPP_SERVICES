package com.proyectofct.salinappservice.Clases.Productos;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Objects;

public class FotosProducto implements Serializable {
    private int idfotos;
    private Bitmap fotos;

    public FotosProducto(int idfotos, Bitmap fotos) {
        this.idfotos = idfotos;
        this.fotos = fotos;
    }

    public FotosProducto() {

    }

    public int getIdfotos() {
        return idfotos;
    }

    public void setIdfotos(int idfotos) {
        this.idfotos = idfotos;
    }

    public Bitmap getFotos() {
        return fotos;
    }

    public void setFotos(Bitmap fotos) {
        this.fotos = fotos;
    }

    @Override
    public String toString() {
        return "FotosProducto{" +
                "idfotos=" + idfotos +
                ", fotos=" + fotos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FotosProducto that = (FotosProducto) o;
        return idfotos == that.idfotos &&
                fotos.equals(that.fotos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idfotos, fotos);
    }
}
