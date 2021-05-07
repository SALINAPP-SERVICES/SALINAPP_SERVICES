package com.proyectofct.salinappservice.Tareas.TareasEmpresa;

import com.proyectofct.salinappservice.Clases.Empresa.Empresa;
import com.proyectofct.salinappservice.Modelos.EmpresaDB;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TareaObtenerEmpresas implements Callable<ArrayList<Empresa>> {
    private ArrayList<Empresa> empresasDevueltas = null;
    private int página;

    public TareaObtenerEmpresas(int página) {
        this.página = página;
    }

    @Override
    public ArrayList<Empresa> call() throws Exception {
        empresasDevueltas = EmpresaDB.obtenerEmpresa(página);
        return empresasDevueltas;
    }
}
