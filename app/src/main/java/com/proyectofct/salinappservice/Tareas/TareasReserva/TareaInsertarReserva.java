package com.proyectofct.salinappservice.Tareas.TareasReserva;

import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.Modelos.ReservaDB;

import java.util.concurrent.Callable;

public class TareaInsertarReserva implements Callable<Boolean> {
    private LíneaReserva líneaReserva;

    public TareaInsertarReserva(LíneaReserva líneaReserva) {
        this.líneaReserva = líneaReserva;
    }

    @Override
    public Boolean call() throws Exception {
        boolean insertadoOk = ReservaDB.insertarReserva(líneaReserva);
        return insertadoOk;
    }
}
