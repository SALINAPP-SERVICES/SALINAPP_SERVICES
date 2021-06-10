package com.proyectofct.salinappservice.Modelos;

import android.util.Log;

import com.proyectofct.salinappservice.Clases.Clientes.Cliente;
import com.proyectofct.salinappservice.Clases.Clientes.Direcciones;
import com.proyectofct.salinappservice.Clases.Clientes.DireccionesClientes;
import com.proyectofct.salinappservice.Clases.Empresa.Empresa;
import com.proyectofct.salinappservice.Clases.Productos.Producto;
import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
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

            //Inserto la reserva
            int idReserva = reserva.getIdReserva();
            String ordenSQL1 = "INSERT INTO reserva (idreserva, fechar, total, iddireccioncliente) VALUES (?, ?, ?, ?);";
            PreparedStatement sentenciaPreparada1 = conexión.prepareStatement(ordenSQL1);
            sentenciaPreparada1.setInt(1, idReserva);
            SimpleDateFormat formatoHoraFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fechaHora = reserva.getFechaReserva();
            String fechaHoraActual = formatoHoraFecha.format(fechaHora);
            Timestamp fechaHoraActualTimestamp = Timestamp.valueOf(fechaHoraActual);
            sentenciaPreparada1.setTimestamp(2, fechaHoraActualTimestamp);
            sentenciaPreparada1.setDouble(3, reserva.getTotal());
            sentenciaPreparada1.setInt(4, reserva.getIdDireccionCliente().getIdDireccionCliente());
            int filasAfectadas1 = sentenciaPreparada1.executeUpdate();

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
                sentenciaPreparada1.close();
                sentenciaPreparada2.close();
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

            if(filasAfectadas1 > 0 && filasAfectadas2 > 0 && filasAfectadas3 > 0) {
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
            Statement sentencia1 = conexión.createStatement();
            String ordenSQL1 = "SELECT * FROM reserva";
            ResultSet resultado1 = sentencia1.executeQuery(ordenSQL1);
            while(resultado1.next()) {
                //Obtengo el id de la reserva
                int idReserva = resultado1.getInt("idreserva");
                //Obtengo las líneas de reserva
                ArrayList<LíneaReserva> líneasReserva = new ArrayList<LíneaReserva>();

                Statement sentencia2 = conexión.createStatement();
                String ordenSQL2 = "SELECT * FROM lineasreserva";
                ResultSet resultado2 = sentencia2.executeQuery(ordenSQL2);
                while (resultado2.next()){
                    //Obtengo el id de línea de reserva
                    int idLíneaReserva = resultado2.getInt("idlineasreserva");
                    //Obtengo el producto de la línea de reserva
                    Statement sentencia3 = conexión.createStatement();
                    String ordenSQL3 = "SELECT pp.idproductoempresa, pp.cantidad, pp.precioventa, pp.habilitado, pp.archivado, pp.cod_producto, pp.cod_empresa, e.clave_empr, e.datos_empr, p.cod_QR, p.marca, p.modelo, p.descripcion FROM productospublicados pp INNER JOIN empresas e INNER JOIN productos p ON (pp.cod_producto = p.cod_producto AND pp.cod_empresa = e.cod_empr) WHERE pp.habilitado = 1 AND pp.archivado = 0;";
                    ResultSet resultado3 = sentencia3.executeQuery(ordenSQL3);
                    ProductosPublicados productosPublicados = null;
                    while (resultado3.next()){
                        int idProductoEmpresa = resultado3.getInt("idproductoempresa");
                        int cantidadEnStock = resultado3.getInt("cantidad");
                        double precioVenta = resultado3.getDouble("precioventa");
                        int habilitadoI = resultado3.getInt("habilitado");
                        int archivadoI = resultado3.getInt("archivado");
                        String cod_producto = resultado3.getString("cod_producto");
                        String cod_empr = resultado3.getString("cod_empresa");
                        String clave_empr = resultado3.getString("clave_empr");
                        String datos_empr = resultado3.getString("datos_empr");
                        String cod_QR = resultado3.getString("cod_QR");
                        String marca = resultado3.getString("marca");
                        String modelo = resultado3.getString("modelo");
                        String descripción = resultado3.getString("descripcion");

                        boolean habilitado = false;
                        boolean archivado = false;

                        if (habilitadoI == 1) {
                            habilitado = true;
                        }
                        if (archivadoI == 1) {
                            archivado = true;
                        }

                        productosPublicados = new ProductosPublicados(idProductoEmpresa, cantidadEnStock, precioVenta, habilitado, archivado, new Producto(cod_producto, cod_QR, marca, modelo, descripción, null /*No me interesa la imagen*/), new Empresa(cod_empr, clave_empr, datos_empr));
                    }
                    sentencia3.close();
                    resultado3.close();

                    //Obtengo la cantidad solicitada de línea de reserva
                    int cantidadSolicitada = resultado2.getInt("cantidad");
                    LíneaReserva líneaReserva = new LíneaReserva(idLíneaReserva, idReserva, productosPublicados, cantidadSolicitada);
                    líneasReserva.add(líneaReserva);
                }
                sentencia2.close();
                resultado2.close();

                //Obtengo la fecha de la reserva
                Timestamp fechaReservaTimestamp = resultado1.getTimestamp("fechar");
                Date fechaReserva = new Date(fechaReservaTimestamp.getTime());
                //Obtengo el total de la reserva
                double total = resultado1.getDouble("total");
                //Obtengo las direcciones de cliente
                Statement sentencia4 = conexión.createStatement();
                String ordenSQL4 = "SELECT dc.iddireccioncliente, d.iddireccion, d.direccion, c.idcliente, c.emailc, c.clavec, c.datosc FROM direccionesclientes dc INNER JOIN direcciones d INNER JOIN clientes c ON (dc.iddireccion = d.iddireccion AND dc.idcliente = c.idcliente)";
                ResultSet resultado4 = sentencia4.executeQuery(ordenSQL4);
                DireccionesClientes direccionesCliente = null;
                while (resultado4.next()){
                    //Obtengo las direcciones
                    int idDireccion = resultado4.getInt("iddireccion");
                    String dirección = resultado4.getString("direccion");
                    Direcciones direcciones = new Direcciones(idDireccion, dirección);

                    //Obtengo los clientes
                    int idCliente = resultado4.getInt("idcliente");
                    String email = resultado4.getString("emailc");
                    String contraseña = resultado4.getString("clavec");
                    String datos = resultado4.getString("datosc");
                    Cliente cliente = new Cliente(idCliente, email, contraseña, datos);

                    int idDireccionesCliente = resultado4.getInt("iddireccioncliente");
                    direccionesCliente = new DireccionesClientes(idDireccionesCliente, direcciones, cliente);
                }
                resultado4.close();
                sentencia4.close();

                int cancelado = resultado1.getInt("Cancelado");

                Reserva r = new Reserva(idReserva, líneasReserva, fechaReserva, total, direccionesCliente);
                r.setCancelado(cancelado);
                reservasDevueltas.add(r);
            }
            resultado1.close();
            sentencia1.close();

            conexión.close();

            return reservasDevueltas;
        } catch (SQLException e) {
            Log.i("SQL", "Error al mostrar las reservas de la base de datos");
            return null;
        }
    }

    public static boolean actualizarReservas(Reserva reserva){
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if(conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return false;
        }
        try {
            String ordenSQL = "UPDATE reserva SET Cancelado = 1 WHERE idreserva = " + reserva.getIdReserva();
            PreparedStatement sentenciaPreparada = conexión.prepareStatement(ordenSQL);
            int filasAfectadas1 = sentenciaPreparada.executeUpdate();

            conexión.close();

            if(filasAfectadas1 > 0) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            Log.i("SQL", "Error al actualizar en la base de datos");
            return false;
        }
    }
}