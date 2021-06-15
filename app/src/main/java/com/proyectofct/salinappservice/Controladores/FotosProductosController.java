package com.proyectofct.salinappservice.Controladores;

import com.proyectofct.salinappservice.Clases.Productos.FotosProducto;
import com.proyectofct.salinappservice.Tareas.TareasProductoPublicado.TareaActualizarFotosProductos;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FotosProductosController {
    public static boolean actualizarFoto(FotosProducto fp, String cod_producto) {
        FutureTask t = new FutureTask(new TareaActualizarFotosProductos(fp, cod_producto));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(t);
        boolean actualizadoOK = false;
        try {
            actualizadoOK = (boolean) t.get();
            es.shutdown();
            try {
                if (!es.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    es.shutdownNow();
                }
            } catch (InterruptedException e) {
                es.shutdownNow();
            }
        } catch (
                ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return actualizadoOK;
        }
    }
}