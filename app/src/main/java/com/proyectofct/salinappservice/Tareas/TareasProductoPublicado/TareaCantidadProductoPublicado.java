package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Modelos.ProductosPublicadosDB;

public class TareaCantidadProductoPublicado implements java.util.concurrent.Callable<Integer> {
    private int cantidadProductosPublicados = 0;

    @Override
    public Integer call() throws Exception {
        cantidadProductosPublicados = ProductosPublicadosDB.obtenerCantidadProductosPublicados();
        return cantidadProductosPublicados;
    }
}
