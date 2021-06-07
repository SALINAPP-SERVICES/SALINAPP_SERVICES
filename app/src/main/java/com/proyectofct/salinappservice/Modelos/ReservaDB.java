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

            int idCliente = reserva.getIdDireccionCliente().getCliente().getIdCliente();
            int idDirección = reserva.getIdDireccionCliente().getDireccion().getIdDireccion();
            int idDireccionesCliente = reserva.getIdDireccionCliente().getIdDireccionCliente();

            //Inserto el cliente
            String ordenSQL1 = "INSERT INTO clientes (idcliente, emailc, clavec, datosc) VALUES (?, ?, ?, ?);";
            PreparedStatement sentenciaPreparada1 = conexión.prepareStatement(ordenSQL1);
            sentenciaPreparada1.setInt(1, idCliente);
            sentenciaPreparada1.setString(2, reserva.getIdDireccionCliente().getCliente().getEmail());
            sentenciaPreparada1.setString(3, reserva.getIdDireccionCliente().getCliente().getContraseña());
            sentenciaPreparada1.setString(4, reserva.getIdDireccionCliente().getCliente().getDatos());
            int filasAfectadas1 = sentenciaPreparada1.executeUpdate();

            //Inserto las direcciones
            String ordenSQL2 = "INSERT INTO direcciones (iddireccion, direccion) VALUES (?, ?);";
            PreparedStatement sentenciaPreparada2 = conexión.prepareStatement(ordenSQL2);
            sentenciaPreparada2.setInt(1, idDirección);
            sentenciaPreparada2.setString(2, reserva.getIdDireccionCliente().getDireccion().getDireccion());
            int filasAfectadas2 = sentenciaPreparada2.executeUpdate();

            //Inserto las direcciones de cliente
            String ordenSQL3 = "INSERT INTO direccionesclientes (iddireccioncliente, iddireccion, idcliente) VALUES (?, ?, ?);";
            PreparedStatement sentenciaPreparada3 = conexión.prepareStatement(ordenSQL3);
            sentenciaPreparada3.setInt(1, idDireccionesCliente);
            sentenciaPreparada3.setInt(2, idDirección);
            sentenciaPreparada3.setInt(3, idCliente);
            int filasAfectadas3 = sentenciaPreparada3.executeUpdate();

            //Inserto la reserva
            int idReserva = reserva.getIdReserva();
            String ordenSQL4 = "INSERT INTO reserva (idreserva, fechar, total, iddireccioncliente) VALUES (?, ?, ?, ?);";
            PreparedStatement sentenciaPreparada4 = conexión.prepareStatement(ordenSQL4);
            sentenciaPreparada4.setInt(1, idReserva);
            SimpleDateFormat formatoHoraFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fechaHora = reserva.getFechaReserva();
            String fechaHoraActual = formatoHoraFecha.format(fechaHora);
            Timestamp fechaHoraActualTimestamp = Timestamp.valueOf(fechaHoraActual);
            sentenciaPreparada4.setTimestamp(2, fechaHoraActualTimestamp);
            sentenciaPreparada4.setDouble(3, reserva.getTotal());
            sentenciaPreparada4.setInt(4, idDireccionesCliente);
            int filasAfectadas4 = sentenciaPreparada4.executeUpdate();

            int filasAfectadas5 = 0;
            int filasAfectadas6 = 0;
            ArrayList<LíneaReserva> líneaReservas = reserva.getLíneasReserva();
            for (LíneaReserva líneaReserva: líneaReservas) {
                //Inserto las líneas de reserva
                int idProductoEmpresa = líneaReserva.getProductoPublicado().getIdProductoEmpresa();
                int cantidadSolicitada = líneaReserva.getCantidad();

                String ordenSQL5 = "INSERT INTO lineasreserva (idlineasreserva, idreserva, idproductoempresa, cantidad) VALUES (?, ?, ?, ?);";
                PreparedStatement sentenciaPreparada5 = conexión.prepareStatement(ordenSQL5);
                sentenciaPreparada5.setInt(1, líneaReserva.getIdLíneaReserva());
                sentenciaPreparada5.setInt(2, idReserva);
                sentenciaPreparada5.setInt(3, idProductoEmpresa);
                sentenciaPreparada5.setInt(4, cantidadSolicitada);
                filasAfectadas5 = sentenciaPreparada5.executeUpdate();

                //Obtengo el stock del producto de la DB
                String ordenSQL6 = "SELECT cantidad FROM productospublicados WHERE idproductoempresa = ?";
                PreparedStatement sentenciaPreparada6 = conexión.prepareStatement(ordenSQL6);
                sentenciaPreparada6.setInt(1, líneaReserva.getProductoPublicado().getIdProductoEmpresa());
                ResultSet resultado = sentenciaPreparada6.executeQuery();
                int cantidadAlmacenadaEnDB = 0;
                while (resultado.next()){
                    cantidadAlmacenadaEnDB = resultado.getInt("cantidad");
                }
                resultado.close();
                sentenciaPreparada1.close();
                sentenciaPreparada2.close();
                sentenciaPreparada3.close();
                sentenciaPreparada4.close();
                sentenciaPreparada5.close();
                sentenciaPreparada6.close();

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

                    String ordenSQL7 = "UPDATE productospublicados SET cantidad = ? WHERE idproductoempresa = ?";
                    PreparedStatement sentenciaPreparada7 = conexión.prepareStatement(ordenSQL7);
                    sentenciaPreparada7.setInt(1, cantidadAlmacenadaEnDB);
                    sentenciaPreparada7.setInt(2, idProductoEmpresa);
                    filasAfectadas6 = sentenciaPreparada7.executeUpdate();
                    sentenciaPreparada4.close();
                }
            }

            conexión.commit();

            if(filasAfectadas1 > 0 && filasAfectadas2 > 0 && filasAfectadas3 > 0 && filasAfectadas4 > 0 && filasAfectadas5 > 0 && filasAfectadas6 > 0) {
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
                    //Obtengo el id de producto empresa
                    int idProductoEmpresa = resultado2.getInt("idproductoempresa");
                    //Obtengo el producto de la línea de reserva
                    String ordenSQL3 = "SELECT pp.cantidad, pp.precioventa, pp.habilitado, pp.archivado, pp.cod_producto, pp.cod_empresa, e.clave_empr, e.datos_empr, p.cod_QR, p.marca, p.modelo, p.descripcion FROM productospublicados pp INNER JOIN empresas e INNER JOIN productos p ON (pp.cod_producto = p.cod_producto AND pp.cod_empresa = e.cod_empr) WHERE pp.habilitado = 1 AND pp.archivado = 0;";
                    int cantidadEnStock = resultado2.getInt("cantidad");
                    double precioVenta = resultado2.getDouble("precioventa");
                    int habilitadoI = resultado2.getInt("habilitado");
                    int archivadoI = resultado2.getInt("archivado");
                    String cod_producto = resultado2.getString("cod_producto");
                    String cod_QR = resultado2.getString("cod_QR");
                    String marca = resultado2.getString("marca");
                    String modelo = resultado2.getString("modelo");
                    String descripción = resultado2.getString("descripcion");
                    String cod_empr = resultado2.getString("cod_empr");
                    String clave_empr = resultado2.getString("clave_empr");
                    String datos_empr = resultado2.getString("datos_empr");

                    boolean habilitado = false;
                    boolean archivado = false;

                    if (habilitadoI == 1) {
                        habilitado = true;
                    }
                    if (archivadoI == 1) {
                        archivado = true;
                    }

                    ProductosPublicados productosPublicados = new ProductosPublicados(idProductoEmpresa, cantidadEnStock, precioVenta, habilitado, archivado, new Producto(cod_producto, cod_QR, marca, modelo, descripción, null /*No me interesa la imagen*/), new Empresa(cod_empr, clave_empr, datos_empr));
                    //Obtengo la cantidad solicitada de línea de reserva
                    int cantidadSolicitada = resultado2.getInt("cantidad");
                    LíneaReserva líneaReserva = new LíneaReserva(idLíneaReserva, idReserva, productosPublicados, cantidadSolicitada);
                    líneasReserva.add(líneaReserva);
                }
                //Obtengo la fecha de la reserva
                Timestamp fechaReservaTimestamp = resultado1.getTimestamp("fechar");
                Date fechaReserva = new Date(fechaReservaTimestamp.getTime());
                //Obtengo el total de la reserva
                double total = resultado1.getDouble("total");
                //Obtengo las direcciones de cliente
                Statement sentencia3 = conexión.createStatement();
                String ordenSQL3 = "SELECT dc.iddireccioncliente, d.iddireccion, d.direccion, c.idcliente, c.emailc, c.clavec, c.datosc FROM direccionesclientes dc INNER JOIN direcciones d INNER JOIN clientes c ON (dc.iddireccion = d.iddireccion AND dc.idcliente = c.idcliente)";
                ResultSet resultado3 = sentencia3.executeQuery(ordenSQL3);
                DireccionesClientes direccionesCliente = null;
                while (resultado3.next()){
                    //Obtengo las direcciones
                    int idDireccion = resultado3.getInt("iddireccion");
                    String dirección = resultado3.getString("direccion");
                    Direcciones direcciones = new Direcciones(idDireccion, dirección);

                    //Obtengo los clientes
                    int idCliente = resultado3.getInt("idcliente");
                    String email = resultado3.getString("emailc");
                    String contraseña = resultado3.getString("clavec");
                    String datos = resultado3.getString("datosc");
                    Cliente cliente = new Cliente(idCliente, email, contraseña, datos);

                    int idDireccionesCliente = resultado3.getInt("iddireccioncliente");
                    direccionesCliente = new DireccionesClientes(idDireccionesCliente, direcciones, cliente);
                }
                resultado3.close();
                sentencia3.close();

                Reserva r = new Reserva(idReserva, líneasReserva, fechaReserva, total, direccionesCliente);
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
}