package com.proyectofct.salinappservice.Tareas.TareasEmpresa;

import com.proyectofct.salinappservice.Clases.Empresa.Empresa;
import com.proyectofct.salinappservice.Modelos.EmpresaDB;

import java.util.concurrent.Callable;

public class TareaInsertarEmpresas implements Callable<Boolean> {
    Empresa empresa;
    public TareaInsertarEmpresas(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public Boolean call() throws Exception {
        boolean insertadoOK = EmpresaDB.insertarEmpresa(empresa);
        return insertadoOK;
    }
}
