package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Modelos.ProductoPublicadoDB;

public class TareaCantidadProductoPublicado implements java.util.concurrent.Callable<Integer> {
    private int cantidadProductosPublicados = 0;

    @Override
    public Integer call() throws Exception {
        cantidadProductosPublicados = ProductoPublicadoDB.obtenerCantidadProductosPublicados();
        return cantidadProductosPublicados;
    }
}
