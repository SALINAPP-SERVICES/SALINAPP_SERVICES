package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Clases.Productos.ProductoPublicado;

import java.util.ArrayList;
import java.util.concurrent.Callable;

//REPASAR CLASE
public class TareaObtenerProductoPublicado implements Callable<ArrayList<ProductoPublicado>> {
    private ArrayList<ProductoPublicado> productosPublicadosDevueltos = null;
    private int página;

    public  TareaObtenerProductoPublicado(int página) {
        this.página = página;
    }

    @Override
    public ArrayList<ProductoPublicado> call() throws Exception {
        //productosPublicadosDevueltos = ProductosPublicadosDB.obtenerProductosPublicados(página);
        return productosPublicadosDevueltos;
    }
}
