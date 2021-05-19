package com.proyectofct.salinappservice.Modelos;

import android.util.Log;

import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.BaseDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteDB {
    public static int obtenerNuevoIDDireccionCliente() {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return 0;
        }
        int últimoIDDireccionCliente = 0;
        int nuevoIDDireccionCliente = 0;
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT MAX(iddireccioncliente) AS iddireccioncliente FROM direccionesclientes"; //Obtengo el ID de direccionesclientes más alto
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while (resultado.next()) {
                últimoIDDireccionCliente = resultado.getInt("iddireccioncliente");
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            nuevoIDDireccionCliente = últimoIDDireccionCliente + 1; //Sumo 1 al ID de direccionesclientes más alto para obtener un ID que nunca se repita
            return nuevoIDDireccionCliente;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver el nuevo ID de direccionesclientes");
            return 0;
        }
    }

    public static int obtenerNuevoIDDireccion() {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return 0;
        }
        int últimoIDDireccion = 0;
        int nuevoIDDireccion = 0;
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT MAX(iddireccion) AS iddireccion FROM direcciones"; //Obtengo el ID de direcciones más alto
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while (resultado.next()) {
                últimoIDDireccion = resultado.getInt("iddireccion");
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            nuevoIDDireccion = últimoIDDireccion + 1; //Sumo 1 al ID de direcciones más alto para obtener un ID que nunca se repita
            return nuevoIDDireccion;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver el nuevo ID de direcciones");
            return 0;
        }
    }

    public static int obtenerNuevoIdCliente() {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return 0;
        }
        int últimoIDCliente = 0;
        int nuevoIDCliente = 0;
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT MAX(idcliente) AS idcliente FROM clientes"; //Obtengo el ID de clientes más alto
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while (resultado.next()) {
                últimoIDCliente = resultado.getInt("idcliente");
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            nuevoIDCliente = últimoIDCliente + 1; //Sumo 1 al ID de clientes más alto para obtener un ID que nunca se repita
            return nuevoIDCliente;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver el nuevo ID de clientes");
            return 0;
        }
    }
}
