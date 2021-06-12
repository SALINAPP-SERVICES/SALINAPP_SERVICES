package com.proyectofct.salinappservice.Tareas.TareasProductoPublicado;

import com.proyectofct.salinappservice.Clases.Productos.FotosProducto;
import com.proyectofct.salinappservice.Modelos.ProductosPublicadosDB;

import java.util.concurrent.Callable;

public class TareaActualizarFotosProductos implements Callable<Boolean> {
    private FotosProducto fp = null;
    private String cod_producto;
    public TareaActualizarFotosProductos(FotosProducto fp, String cod_producto) {
        this.fp = fp;
        this.cod_producto = cod_producto;
    }

    @Override
    public Boolean call() throws Exception {
        boolean actualizadoOK = ProductosPublicadosDB.actualizarFotoProducto(fp, cod_producto);
        return actualizadoOK;
    }
}
