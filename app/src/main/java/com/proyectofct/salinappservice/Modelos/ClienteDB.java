package com.proyectofct.salinappservice.Modelos;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.proyectofct.salinappservice.Clases.Clientes.Cliente;
import com.proyectofct.salinappservice.Clases.Clientes.Direcciones;
import com.proyectofct.salinappservice.Clases.Clientes.DireccionesClientes;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.BaseDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteDB {
    public static FirebaseAuth firebaseAuth;

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

    public static DireccionesClientes obtenerDireccionesClientes() {
        firebaseAuth = FirebaseAuth.getInstance();

        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return null;
        }
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT dc.iddireccioncliente, d.iddireccion, d.direccion, c.idcliente, c.emailc, c.clavec, c.datosc FROM direccionesclientes dc INNER JOIN direcciones d INNER JOIN clientes c ON (dc.iddireccion = d.iddireccion AND dc.idcliente = c.idcliente) WHERE c.emailc LIKE '" + firebaseAuth.getCurrentUser().getEmail() + "';";
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            DireccionesClientes direccionesCliente = null;
            while (resultado.next()) {
                //Obtengo las direcciones
                int idDireccion = resultado.getInt("iddireccion");
                String dirección = resultado.getString("direccion");
                Direcciones direcciones = new Direcciones(idDireccion, dirección);

                //Obtengo los clientes
                int idCliente = resultado.getInt("idcliente");
                String email = resultado.getString("emailc");
                String contraseña = resultado.getString("clavec");
                String datos = resultado.getString("datosc");
                Cliente cliente = new Cliente(idCliente, email, contraseña, datos);

                //Obtengo el id de direcciones de clientes
                int idDireccionesCliente = resultado.getInt("iddireccioncliente");
                direccionesCliente = new DireccionesClientes(idDireccionesCliente, direcciones, cliente);
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            return direccionesCliente;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver las direcciones de clientes de la base de datos");
            return null;
        }
    }

    public static boolean insertarDireccionesClientes(DireccionesClientes direccionesClientes) {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if(conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return false;
        }
        try {
            int idCliente = direccionesClientes.getCliente().getIdCliente();
            int idDirección = direccionesClientes.getDireccion().getIdDireccion();
            int idDireccionesCliente = direccionesClientes.getIdDireccionCliente();

            //Inserto el cliente
            String ordenSQL1 = "INSERT INTO clientes (idcliente, emailc, clavec, datosc) VALUES (?, ?, ?, ?);";
            PreparedStatement sentenciaPreparada1 = conexión.prepareStatement(ordenSQL1);
            sentenciaPreparada1.setInt(1, idCliente);
            sentenciaPreparada1.setString(2, direccionesClientes.getCliente().getEmail());
            sentenciaPreparada1.setString(3, direccionesClientes.getCliente().getContraseña());
            sentenciaPreparada1.setString(4, direccionesClientes.getCliente().getDatos());
            int filasAfectadas1 = sentenciaPreparada1.executeUpdate();
            sentenciaPreparada1.close();

            //Inserto las direcciones
            String ordenSQL2 = "INSERT INTO direcciones (iddireccion, direccion) VALUES (?, ?);";
            PreparedStatement sentenciaPreparada2 = conexión.prepareStatement(ordenSQL2);
            sentenciaPreparada2.setInt(1, idDirección);
            sentenciaPreparada2.setString(2, direccionesClientes.getDireccion().getDireccion());
            int filasAfectadas2 = sentenciaPreparada2.executeUpdate();
            sentenciaPreparada2.close();

            //Inserto las direcciones de cliente
            String ordenSQL3 = "INSERT INTO direccionesclientes (iddireccioncliente, iddireccion, idcliente) VALUES (?, ?, ?);";
            PreparedStatement sentenciaPreparada3 = conexión.prepareStatement(ordenSQL3);
            sentenciaPreparada3.setInt(1, idDireccionesCliente);
            sentenciaPreparada3.setInt(2, idDirección);
            sentenciaPreparada3.setInt(3, idCliente);
            int filasAfectadas3 = sentenciaPreparada3.executeUpdate();
            sentenciaPreparada3.close();

            if (filasAfectadas1 > 0 && filasAfectadas2 > 0 && filasAfectadas3 > 0){
                conexión.close();
                return true;
            }else {
                conexión.close();
                return false;
            }
        }catch (SQLException e){
            try {
                conexión.close();
            } catch (SQLException e1) {
                return false;
            }
            Log.i("SQL", "Error al insertar las direcciones de los clientes");
            return false;
        }
    }
}
