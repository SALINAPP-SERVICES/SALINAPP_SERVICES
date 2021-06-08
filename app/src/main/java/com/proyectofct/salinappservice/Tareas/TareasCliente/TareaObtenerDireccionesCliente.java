package com.proyectofct.salinappservice.Tareas.TareasCliente;

import com.proyectofct.salinappservice.Clases.Clientes.DireccionesClientes;
import com.proyectofct.salinappservice.Modelos.ClienteDB;

public class TareaObtenerDireccionesCliente implements java.util.concurrent.Callable<DireccionesClientes> {
    private DireccionesClientes direccionesClientes = null;

    @Override
    public DireccionesClientes call() throws Exception {
        direccionesClientes = ClienteDB.obtenerDireccionesClientes();
        return direccionesClientes;
    }
}
