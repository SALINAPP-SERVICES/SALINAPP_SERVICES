package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Modelos.ProductosPublicadosDB;

public class TareaCantidadProductoPublicado implements java.util.concurrent.Callable<Integer> {
    private int cantidadProductosPublicados = 0;
    private String codEmpresa = "";

    public TareaCantidadProductoPublicado(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    @Override
    public Integer call() throws Exception {
        cantidadProductosPublicados = ProductosPublicadosDB.obtenerCantidadProductosPublicadosPorEmpresa(codEmpresa);
        return cantidadProductosPublicados;
    }
}
