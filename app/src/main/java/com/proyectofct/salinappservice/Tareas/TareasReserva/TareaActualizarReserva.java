package com.proyectofct.salinappservice.Tareas.TareasReserva;

import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Modelos.ReservaDB;

import java.util.concurrent.Callable;

public class TareaActualizarReserva implements Callable<Boolean> {
    public Reserva reserva;

    public TareaActualizarReserva(Reserva reservaActual) {
        this.reserva = reservaActual;
    }

    @Override
    public Boolean call() throws Exception {
        boolean insertadoOk = ReservaDB.actualizarReservas(reserva);
        return insertadoOk;
    }
}
