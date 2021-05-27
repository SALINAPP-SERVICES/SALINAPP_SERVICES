package com.proyectofct.salinappservice.Tareas.TareasReserva;

import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Modelos.ReservaDB;

import java.util.concurrent.Callable;

public class TareaInsertarReserva implements Callable<Boolean> {
    private Reserva reserva;

    public TareaInsertarReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    @Override
    public Boolean call() throws Exception {
        boolean insertadoOk = ReservaDB.insertarReserva(reserva);
        return insertadoOk;
    }
}