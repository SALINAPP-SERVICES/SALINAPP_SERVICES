package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Clases.Productos.ProductoPublicado;
import com.proyectofct.salinappservice.Modelos.ProductoPublicadoDB;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TareaObtenerProductoPublicadoPorEmpresa implements Callable<ArrayList<ProductoPublicado>> {
    private ArrayList<ProductoPublicado> productosPublicadosDevueltos = null;
    private int página;
    private String cod_empr;

    public TareaObtenerProductoPublicadoPorEmpresa(int página, String cod_empr) {
        this.productosPublicadosDevueltos = new ArrayList<ProductosPublicados>();
        this.página = página;
        this.cod_empr = cod_empr;
    }

    @Override
    public ArrayList<ProductoPublicado> call() throws Exception {
        productosPublicadosDevueltos = ProductoPublicadoDB.obtenerProductosPublicadosPorEmpresa(página, cod_empr);
        return productosPublicadosDevueltos;
    }
}

