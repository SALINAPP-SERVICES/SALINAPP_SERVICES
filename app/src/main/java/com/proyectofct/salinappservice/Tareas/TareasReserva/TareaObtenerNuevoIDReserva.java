package com.proyectofct.salinappservice.Tareas.TareasReserva;

import com.proyectofct.salinappservice.Modelos.ReservaDB;

public class TareaObtenerNuevoIDReserva implements java.util.concurrent.Callable<Integer> {
    private int nuevoIDReserva = 0;

    @Override
    public Integer call() throws Exception {
        nuevoIDReserva = ReservaDB.obtenerNuevoIDReserva();
        return nuevoIDReserva;
    }
}
