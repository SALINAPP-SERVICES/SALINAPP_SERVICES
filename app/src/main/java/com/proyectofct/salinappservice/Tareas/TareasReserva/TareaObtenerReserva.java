package com.proyectofct.salinappservice.Tareas.TareasReserva;

import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Modelos.ReservaDB;

import java.util.ArrayList;

public class TareaObtenerReserva implements java.util.concurrent.Callable<ArrayList<Reserva>> {
    private ArrayList<Reserva> reservasDevueltas = null;

    @Override
    public ArrayList<Reserva> call() throws Exception {
        reservasDevueltas = ReservaDB.obtenerReservas();
        return reservasDevueltas;
    }
}
