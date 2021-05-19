package com.proyectofct.salinappservice.Tareas.TareasCliente;

import com.proyectofct.salinappservice.Modelos.ClienteDB;

public class TareaObtenerNuevoIDDireccionCliente implements java.util.concurrent.Callable<Integer> {
    private int nuevoIDDirecciónCliente = 0;

    @Override
    public Integer call() throws Exception {
        nuevoIDDirecciónCliente = ClienteDB.obtenerNuevoIDDireccionCliente();
        return nuevoIDDirecciónCliente;
    }
}
