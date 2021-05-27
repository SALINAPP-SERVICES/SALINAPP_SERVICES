package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Modelos.ProductosPublicadosDB;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TareaBuscarProductoPublicado implements Callable<ArrayList<ProductosPublicados>> {
    private String marca = null;
    private int página= 0;
    private ArrayList<ProductosPublicados> productosPublicadosEncontrados;

    public TareaBuscarProductoPublicado(String marca, int página) {
        this.marca = marca;
        this.página = página;
        this.productosPublicadosEncontrados= new ArrayList<>();
    }

    @Override
    public ArrayList<ProductosPublicados> call() throws Exception {
        productosPublicadosEncontrados = ProductosPublicadosDB.buscarProductoPublicados(página, marca);
        return productosPublicadosEncontrados;
    }
}
