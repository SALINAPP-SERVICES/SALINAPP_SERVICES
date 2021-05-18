package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import android.widget.ArrayAdapter;

import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Modelos.ProductosPublicadosDB;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TareaObtenerVariantesProductoPublicado implements Callable<ArrayList<ProductosPublicados>> {
    private ArrayList<ProductosPublicados> grupoProductosPublicados = null;
    private String cod_producto;

    public TareaObtenerVariantesProductoPublicado(String cod_producto) {
        this.cod_producto = cod_producto;
        this.grupoProductosPublicados = new ArrayList<ProductosPublicados>();
    }

    @Override
    public ArrayList<ProductosPublicados> call() throws Exception {
        grupoProductosPublicados = ProductosPublicadosDB.obtenerVariantesProductoPublicado(cod_producto);
        return grupoProductosPublicados;
    }
}
