package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TareaBuscarProductoPublicado implements Callable<ArrayList<ProductosPublicados>> {
    private String cod_producto = null;
    private ArrayList<ProductosPublicados> productosPublicadosEncontrados = null;

    public TareaBuscarProductoPublicado(String cod_producto) {
        this.cod_producto = cod_producto;
    }

    @Override
    public ArrayList<ProductosPublicados> call() throws Exception {
        //productosPublicadosEncontrados = ProductosPublicadosDB.buscarProductoPublicados(cod_producto);
        return productosPublicadosEncontrados;
    }
}
