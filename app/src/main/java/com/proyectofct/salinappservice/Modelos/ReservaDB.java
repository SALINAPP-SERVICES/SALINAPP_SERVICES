package com.proyectofct.salinappservice.Modelos;

import android.util.Log;
import android.widget.Toast;

import com.google.type.DateTime;
import com.proyectofct.salinappservice.Clases.Reservas.LíneaReserva;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.BaseDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class ReservaDB {
    public static boolean insertarReserva(LíneaReserva líneaReserva){
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if(conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return false;
        }
        try {
            conexión.setAutoCommit(false);

            //Inserto la reserva
            String ordenSQL = "INSERT INTO reserva (idreserva, fechar, total, iddireccioncliente) VALUES (?, ?, ?, ?);";
            PreparedStatement sentenciaPreparada = conexión.prepareStatement(ordenSQL);
            sentenciaPreparada.setInt(1, líneaReserva.getReserva().getIdReserva());
            LocalDateTime fecha = líneaReserva.getReserva().getFechaReserva();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String fechaActual = formato.format(fecha);
            sentenciaPreparada.setString(2, fechaActual);
            sentenciaPreparada.setDouble(3, líneaReserva.getReserva().getTotal());
            sentenciaPreparada.setInt(4, 1); /*líneaReserva.getR().getIdDireccionCliente() DE MOMENTO PONGO 1 PORQUE LOS MÉTODOS DE LOS CLIENTES NO ESTAN IMPLEMENTADOS*/
            int filasAfectadas = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();

            //Inserto las líneas de reserva
            String ordenSQL2 = "INSERT INTO lineasreserva (idlineasreserva, idreserva, idproductoempresa, cantidad) VALUES (?, ?, ?, ?);";
            PreparedStatement sentenciaPreparada2 = conexión.prepareStatement(ordenSQL2);
            sentenciaPreparada.setInt(1, líneaReserva.getIdLíneaReserva());
            sentenciaPreparada.setInt(2, líneaReserva.getReserva().getIdReserva());
            sentenciaPreparada.setInt(3, líneaReserva.getProductoPublicado().getIdProductoEmpresa());
            sentenciaPreparada.setInt(4, líneaReserva.getCantidad());
            int filasAfectadas2 = sentenciaPreparada.executeUpdate();
            sentenciaPreparada2.close();

            //Obtengo el stock del producto de la DB
            int cantidadDB = 0;

            String ordenSQL3 = "SELECT cantidad FROM productospublicados WHERE idproductoempresa = ?";
            PreparedStatement sentenciaPreparada3 = conexión.prepareStatement(ordenSQL3);
            sentenciaPreparada3.setInt(1, líneaReserva.getProductoPublicado().getIdProductoEmpresa());
            ResultSet resultado = sentenciaPreparada3.executeQuery();
            while (resultado.next()){
                cantidadDB = resultado.getInt("cantidad");
            }
            resultado.close();
            sentenciaPreparada3.close();

            int filasAfectadas3 = 0;
            int cantidadProducto = líneaReserva.getCantidad();
            if (cantidadDB == 0){
                conexión.rollback();
                Log.i("SQL", "Reserva cancelada. El stock de este producto es 0");
                return false;
            }else if (cantidadDB < 0){ //En el remoto caso de que el producto tenga un stock negativo en la base de datos
                conexión.rollback();
                Log.i("SQL", "Reserva cancelada. El stock de este producto es negativo");
                return false;
            } else if (cantidadDB < cantidadProducto){
                conexión.rollback();
                Log.i("SQL", "Reserva cancelada. La cantidad solicitada es mayor de la disponible. Stock actual en la DB: " + cantidadDB);
                return false;
            }else {
                //Resto la cantidad solicitada del producto a la cantidad disponible
                cantidadDB = cantidadDB - cantidadProducto;

                String ordenSQL4 = "UPDATE productospublicados SET cantidad = ? WHERE idproductoempresa = ?";
                PreparedStatement sentenciaPreparada4 = conexión.prepareStatement(ordenSQL4);
                sentenciaPreparada4.setInt(1, cantidadDB);
                sentenciaPreparada4.setInt(2, líneaReserva.getProductoPublicado().getIdProductoEmpresa());
                filasAfectadas3 = sentenciaPreparada4.executeUpdate();
                sentenciaPreparada4.close();
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
}
