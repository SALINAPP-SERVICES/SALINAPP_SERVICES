package com.proyectofct.salinappservice.Tareas.TareasReserva;

import com.proyectofct.salinappservice.Modelos.ReservaDB;

public class TareaObtenerNuevoIDLíneaReserva implements java.util.concurrent.Callable<Integer> {
    private int nuevoIDLíneaReserva = 0;

    @Override
    public Integer call() throws Exception {
        nuevoIDLíneaReserva = ReservaDB.obtenerNuevoIDLíneaReserva();
        return nuevoIDLíneaReserva;
    }
}
