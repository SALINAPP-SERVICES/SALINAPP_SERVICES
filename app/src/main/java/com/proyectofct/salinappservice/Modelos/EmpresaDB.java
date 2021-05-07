package com.proyectofct.salinappservice.Modelos;

import android.util.Log;

import com.proyectofct.salinappservice.Clases.Empresa.Empresa;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.BaseDB;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EmpresaDB {
    public static ArrayList<Empresa> obtenerEmpresa(int página){
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if(conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return null;
        }
        ArrayList<Empresa> empresasDevueltas = new ArrayList<Empresa>();
        try {
            Statement sentencia = conexión.createStatement();
            int desplazamiento = página * ConfiguracionesGeneralesDB.ELEMENTOS_POR_PAGINA;
            String ordenSQL = "SELECT * FROM empresas LIMIT " + desplazamiento + ", " + ConfiguracionesGeneralesDB.ELEMENTOS_POR_PAGINA;
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while(resultado.next()) {
                String cod_empr = resultado.getString("cod_empr");
                String clave_empr = resultado.getString("clave_empr");
                String datos_empr = resultado.getString("datos_empr");
                Empresa e = new Empresa(cod_empr, clave_empr, datos_empr);
                empresasDevueltas.add(e);
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            return empresasDevueltas;
        } catch (SQLException e) {
            Log.i("SQL", "Error al mostrar las empresas de la base de datos");
            return null;
        }
    }

    public static int obtenerCantidadEmpresas() {
        Log.i("sql" , "llega aqui");
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return 0;
        }
        int cantidadEmpresas = 0;
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT count(*) as cantidad FROM empresas";
            ResultSet resultado  = sentencia.executeQuery(ordenSQL);
            while (resultado.next()) {
                cantidadEmpresas = resultado.getInt("cantidad");
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            return cantidadEmpresas;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver el número de empresas de la base de datos");
            return 0;
        }
    }
}
