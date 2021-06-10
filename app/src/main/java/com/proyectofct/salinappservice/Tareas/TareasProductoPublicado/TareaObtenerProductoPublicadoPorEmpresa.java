package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Modelos.ProductosPublicadosDB;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TareaObtenerProductoPublicadoPorEmpresa implements Callable<ArrayList<ProductosPublicados>> {
    private ArrayList<ProductosPublicados> productosPublicadosDevueltos = null;
    private String cod_empr;

    public TareaObtenerProductoPublicadoPorEmpresa(String cod_empr) {
        this.productosPublicadosDevueltos = new ArrayList<ProductosPublicados>();
        this.cod_empr = cod_empr;
    }

    @Override
    public ArrayList<ProductosPublicados> call() throws Exception {
        productosPublicadosDevueltos = ProductosPublicadosDB.obtenerProductosPublicadosPorEmpresa(cod_empr);
        return productosPublicadosDevueltos;
    }
}

