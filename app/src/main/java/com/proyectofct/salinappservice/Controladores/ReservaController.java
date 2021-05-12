package com.proyectofct.salinappservice.Controladores;

import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.Tareas.TareasReserva.TareaInsertarReserva;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ReservaController {
    public static boolean insertarReserva(LíneaReserva líneaReserva) {
        FutureTask tarea = new FutureTask(new TareaInsertarReserva(líneaReserva));
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
}
