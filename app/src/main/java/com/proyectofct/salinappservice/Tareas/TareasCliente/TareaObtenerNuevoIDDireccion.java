package com.proyectofct.salinappservice.Tareas.TareasCliente;

import com.proyectofct.salinappservice.Modelos.ClienteDB;

public class TareaObtenerNuevoIDDireccion implements java.util.concurrent.Callable<Integer> {
    private int nuevoIDDirección = 0;

    @Override
    public Integer call() throws Exception {
        nuevoIDDirección = ClienteDB.obtenerNuevoIDDireccion();
        return nuevoIDDirección;
    }
}
