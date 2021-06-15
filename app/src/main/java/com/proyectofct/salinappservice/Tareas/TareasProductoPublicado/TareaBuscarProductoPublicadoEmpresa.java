package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Modelos.ProductosPublicadosDB;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TareaBuscarProductoPublicadoEmpresa implements Callable<ArrayList<ProductosPublicados>> {
    private String[] marca = null;
    private String codEmpresa;
    private ArrayList<ProductosPublicados> productosPublicadosEncontrados;

    public TareaBuscarProductoPublicadoEmpresa(String[] marca, String codEmpresa) {
        this.marca = marca;
        this.codEmpresa = codEmpresa;
        this.productosPublicadosEncontrados= new ArrayList<>();
    }

    @Override
    public ArrayList<ProductosPublicados> call() throws Exception {
        productosPublicadosEncontrados = ProductosPublicadosDB.buscarProductoPublicadosPorEmpresa(marca,codEmpresa);
        return productosPublicadosEncontrados;
    }
}
