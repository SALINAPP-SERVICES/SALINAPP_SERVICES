package com.proyectofct.salinappservice.Tareas.TareasCliente;

import com.proyectofct.salinappservice.Clases.Clientes.DireccionesClientes;
import com.proyectofct.salinappservice.Modelos.ClienteDB;

import java.util.concurrent.Callable;

public class TareaInsertarDireccionesClientes implements Callable<Boolean> {
    public DireccionesClientes direccionesClientes;

    public TareaInsertarDireccionesClientes(DireccionesClientes direccionesClientes) {
        this.direccionesClientes = direccionesClientes;
    }

    @Override
    public Boolean call() throws Exception {
        boolean insertadoOk = ClienteDB.insertarDireccionesClientes(direccionesClientes);
        return insertadoOk;
    }
}
