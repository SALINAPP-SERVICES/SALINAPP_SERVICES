package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Clases.Productos.ProductoPublicado;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TareaBuscarProductoPublicado implements Callable<ArrayList<ProductoPublicado>> {
    private String cod_producto = null;
    private ArrayList<ProductoPublicado> productosPublicadosEncontrados = null;

    public TareaBuscarProductoPublicado(String cod_producto) {
        this.cod_producto = cod_producto;
    }

    @Override
    public ArrayList<ProductoPublicado> call() throws Exception {
        //productosPublicadosEncontrados = ProductosPublicadosDB.buscarProductoPublicados(cod_producto);
        return productosPublicadosEncontrados;
    }
}
