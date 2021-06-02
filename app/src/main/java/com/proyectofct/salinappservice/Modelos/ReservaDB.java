package com.proyectofct.salinappservice.Modelos;

import android.util.Log;

import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.Clases.Reservas.Reserva;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.BaseDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReservaDB {
    public static boolean insertarReserva(Reserva reserva){
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if(conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return false;
        }
        try {
            conexión.setAutoCommit(false);

            //Inserto los clientes, las direcciones y las direcciones de cliente
            /*
            * POR HACER
            */

            //Inserto la reserva
            int idReserva = reserva.getIdReserva();

            String ordenSQL1 = "INSERT INTO reserva (idreserva, fechar, total, iddireccioncliente) VALUES (?, ?, ?, ?);";
            PreparedStatement sentenciaPreparada = conexión.prepareStatement(ordenSQL1);
            sentenciaPreparada.setInt(1, idReserva);
            SimpleDateFormat formatoHoraFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fechaHora = reserva.getFechaReserva();
            String fechaHoraActual = formatoHoraFecha.format(fechaHora);
            Timestamp fechaHoraActualTimestamp = Timestamp.valueOf(fechaHoraActual);
            sentenciaPreparada.setTimestamp(2, fechaHoraActualTimestamp);
            sentenciaPreparada.setDouble(3, reserva.getTotal());
            sentenciaPreparada.setInt(4, reserva.getIdDireccionCliente().getIdDireccionCliente());
            int filasAfectadas = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();

            int filasAfectadas2 = 0;
            int filasAfectadas3 = 0;
            ArrayList<LíneaReserva> líneaReservas = reserva.getLíneasReserva();
            for (LíneaReserva líneaReserva: líneaReservas) {
                //Inserto las líneas de reserva
                int idProductoEmpresa = líneaReserva.getProductoPublicado().getIdProductoEmpresa();
                int cantidadSolicitada = líneaReserva.getCantidad();

                String ordenSQL2 = "INSERT INTO lineasreserva (idlineasreserva, idreserva, idproductoempresa, cantidad) VALUES (?, ?, ?, ?);";
                PreparedStatement sentenciaPreparada2 = conexión.prepareStatement(ordenSQL2);
                sentenciaPreparada2.setInt(1, líneaReserva.getIdLíneaReserva());
                sentenciaPreparada2.setInt(2, idReserva);
                sentenciaPreparada2.setInt(3, idProductoEmpresa);
                sentenciaPreparada2.setInt(4, cantidadSolicitada);
                filasAfectadas2 = sentenciaPreparada2.executeUpdate();
                sentenciaPreparada2.close();

                //Obtengo el stock del producto de la DB
                String ordenSQL3 = "SELECT cantidad FROM productospublicados WHERE idproductoempresa = ?";
                PreparedStatement sentenciaPreparada3 = conexión.prepareStatement(ordenSQL3);
                sentenciaPreparada3.setInt(1, líneaReserva.getProductoPublicado().getIdProductoEmpresa());
                ResultSet resultado = sentenciaPreparada3.executeQuery();
                int cantidadAlmacenadaEnDB = 0;
                while (resultado.next()){
                    cantidadAlmacenadaEnDB = resultado.getInt("cantidad");
                }
                resultado.close();
                sentenciaPreparada3.close();

                if (cantidadAlmacenadaEnDB == 0){
                    conexión.rollback();
                    Log.i("SQL", "Reserva cancelada. El stock de este producto es 0");
                    return false;
                }else if (cantidadAlmacenadaEnDB < 0){ //En el remoto caso de que el producto tenga un stock negativo en la base de datos
                    conexión.rollback();
                    Log.i("SQL", "Reserva cancelada. El stock de este producto es negativo");
                    return false;
                } else if (cantidadAlmacenadaEnDB < cantidadSolicitada){
                    conexión.rollback();
                    Log.i("SQL", "Reserva cancelada. La cantidad solicitada es mayor de la disponible. Stock actual en la DB: " + cantidadAlmacenadaEnDB);
                    return false;
                }else {
                    //Resto la cantidad solicitada del producto a la cantidad disponible
                    cantidadAlmacenadaEnDB = cantidadAlmacenadaEnDB - cantidadSolicitada;

                    String ordenSQL4 = "UPDATE productospublicados SET cantidad = ? WHERE idproductoempresa = ?";
                    PreparedStatement sentenciaPreparada4 = conexión.prepareStatement(ordenSQL4);
                    sentenciaPreparada4.setInt(1, cantidadAlmacenadaEnDB);
                    sentenciaPreparada4.setInt(2, idProductoEmpresa);
                    filasAfectadas3 = sentenciaPreparada4.executeUpdate();
                    sentenciaPreparada4.close();
                }
            }

            conexión.commit();

            if(filasAfectadas > 0 && filasAfectadas2 > 0 && filasAfectadas3 > 0) {
                conexión.close();
                return true;
            }else {
                conexión.rollback();
                conexión.close();
                return false;
            }
        }catch (SQLException e1){
            try {
                conexión.rollback();
                conexión.close();
            }catch (SQLException e2){
                return false;
            }
            Log.i("SQL", "Error al insertar la venta");
            return false;
        }
    }

    public static int obtenerNuevoIDReserva() {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return 0;
        }
        int últimoIDReserva = 0;
        int nuevoIDReserva = 0;
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT MAX(idreserva) AS idreserva FROM reserva"; //Obtengo el ID de reserva más alto
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while (resultado.next()) {
                últimoIDReserva = resultado.getInt("idreserva");
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            nuevoIDReserva = últimoIDReserva + 1; //Sumo 1 al ID de reserva más alto para obtener un ID que nunca se repita
            return nuevoIDReserva;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver el nuevo ID de reserva");
            return 0;
        }
    }

    public static int obtenerNuevoIDLíneaReserva() {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return 0;
        }
        int últimoIDReserva = 0;
        int nuevoIDReserva = 0;
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT MAX(idlineasreserva) AS idlineasreserva FROM lineasreserva"; //Obtengo el ID de línea de reserva más alto
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while (resultado.next()) {
                últimoIDReserva = resultado.getInt("idlineasreserva");
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            nuevoIDReserva = últimoIDReserva + 1; //Sumo 1 al ID de línea de reserva máss alto para obtener un ID que nunca se repita
            return nuevoIDReserva;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver el nuevo ID de reserva");
            return 0;
        }
    }

    public static ArrayList<Reserva> obtenerReservas(){
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if(conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return null;
        }
        ArrayList<Reserva> reservasDevueltas = new ArrayList<Reserva>();
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT * FROM reserva";
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while(resultado.next()) {
                /*PENDIENTE*/
                /*POR*/
                /*HACER*/
                Reserva r = new Reserva();
                reservasDevueltas.add(r);
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            return reservasDevueltas;
        } catch (SQLException e) {
            Log.i("SQL", "Error al mostrar las reservas de la base de datos");
            return null;
        }
    }
}