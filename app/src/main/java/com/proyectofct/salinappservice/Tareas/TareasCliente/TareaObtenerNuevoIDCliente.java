package com.proyectofct.salinappservice.Tareas.TareasCliente;

import com.proyectofct.salinappservice.Modelos.ClienteDB;

public class TareaObtenerNuevoIDCliente implements java.util.concurrent.Callable<Integer> {
    private int nuevoIDCliente = 0;

    @Override
    public Integer call() throws Exception {
        nuevoIDCliente = ClienteDB.obtenerNuevoIdCliente();
        return nuevoIDCliente;
    }
}
