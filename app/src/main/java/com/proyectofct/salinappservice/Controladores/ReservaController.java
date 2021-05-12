package com.proyectofct.salinappservice.Controladores;

import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Tareas.TareasReserva.TareaInsertarReserva;
import com.proyectofct.salinappservice.Tareas.TareasReserva.TareaObtenerNuevoIDLíneaReserva;
import com.proyectofct.salinappservice.Tareas.TareasReserva.TareaObtenerNuevoIDReserva;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ReservaController {
    public static boolean insertarReserva(Reserva reserva) {
        FutureTask tarea = new FutureTask(new TareaInsertarReserva(reserva));
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(tarea);
        boolean insertadoOk = false;
        try {
            insertadoOk = (boolean) tarea.get();
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
        finally {
            return insertadoOk;
        }
    }

    public static int obtenerNuevoIDReserva() {
        int últimoID = 0;
        FutureTask tarea = new FutureTask (new TareaObtenerNuevoIDReserva());
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

    public static int obtenerNuevoIDLíneaReserva() {
        int últimoID = 0;
        FutureTask tarea = new FutureTask (new TareaObtenerNuevoIDLíneaReserva());
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