package com.proyectofct.salinappservice.Controladores;

import com.proyectofct.salinappservice.Tareas.TareasCliente.TareaObtenerNuevoIDCliente;
import com.proyectofct.salinappservice.Tareas.TareasCliente.TareaObtenerNuevoIDDireccion;
import com.proyectofct.salinappservice.Tareas.TareasCliente.TareaObtenerNuevoIDDireccionCliente;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ClienteController {
    public static int obtenerNuevoIDCliente() {
        int últimoID = 0;
        FutureTask tarea = new FutureTask (new TareaObtenerNuevoIDCliente());
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(tarea);
        try {
            últimoID = (int) tarea.get();
            es.shutdown();
            try {
                if (!es.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    es.shutdownNow();
                }
            } catch (InterruptedException e) {
                es.shutdownNow();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return últimoID;
    }

    public static int obtenerNuevoIDDirección() {
        int últimoID = 0;
        FutureTask tarea = new FutureTask (new TareaObtenerNuevoIDDireccion());
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(tarea);
        try {
            últimoID = (int) tarea.get();
            es.shutdown();
            try {
                if (!es.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    es.shutdownNow();
                }
            } catch (InterruptedException e) {
                es.shutdownNow();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return últimoID;
    }

    public static int obtenerNuevoIDDirecciónCliente() {
        int últimoID = 0;
        FutureTask tarea = new FutureTask (new TareaObtenerNuevoIDDireccionCliente());
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(tarea);
        try {
            últimoID = (int) tarea.get();
            es.shutdown();
            try {
                if (!es.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    es.shutdownNow();
                }
            } catch (InterruptedException e) {
                es.shutdownNow();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return últimoID;
    }
}
