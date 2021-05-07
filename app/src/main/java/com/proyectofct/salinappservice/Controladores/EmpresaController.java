package com.proyectofct.salinappservice.Controladores;

import com.proyectofct.salinappservice.Clases.Empresa.Empresa;
import com.proyectofct.salinappservice.Tareas.TareasEmpresa.TareaCantidadEmpresas;
import com.proyectofct.salinappservice.Tareas.TareasEmpresa.TareaObtenerEmpresas;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class EmpresaController {
    public static ArrayList<Empresa> obtenerEmpresas(int página) {
        ArrayList<Empresa> empresasDevueltas = null;
        FutureTask tarea = new FutureTask(new TareaObtenerEmpresas(página));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(tarea);
        try {
            empresasDevueltas = (ArrayList<Empresa>) tarea.get();
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
        return empresasDevueltas;
    }

    public static int obtenerCantidadEmpresas() {
        int cantidadEmpresas = 0;
        FutureTask tarea = new FutureTask (new TareaCantidadEmpresas());
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(tarea);
        try {
            cantidadEmpresas = (int) tarea.get();
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
        return cantidadEmpresas;
    }
}
