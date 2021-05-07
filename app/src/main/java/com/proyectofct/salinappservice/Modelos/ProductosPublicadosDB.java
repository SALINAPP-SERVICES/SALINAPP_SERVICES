package com.proyectofct.salinappservice.Modelos;

import android.util.Log;

import com.proyectofct.salinappservice.Clases.Empresa.Empresa;
import com.proyectofct.salinappservice.Clases.Productos.Moda;
import com.proyectofct.salinappservice.Clases.Productos.ProductosPublicados;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.BaseDB;
import com.proyectofct.salinappservice.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductosPublicadosDB {
    //OBTENER TODOS LOS PRODUCTOS PUBLICADOS DE MOMENTO NO SE ESTÁ USANDO
    /*
    public static ArrayList<ProductosPublicados> obtenerProductosPublicados(int página) {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return null;
        }
        ArrayList<ProductosPublicados> productosPublicadosDevueltos = new ArrayList<ProductosPublicados>();
        try {
            Empresa empresa = new Empresa("", "", "");
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT * FROM empresas";
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            int numFilas = 0;
            while (resultado.next()) {
                numFilas++;
                String cod_empr = resultado.getString("cod_empr");
                String clave_empr = resultado.getString("clave_empr");
                String datos_empr = resultado.getString("datos_empr");
                empresa = new Empresa(cod_empr, clave_empr, datos_empr);
            }
            resultado.close();
            sentencia.close();
            if (numFilas == 0) {
                return null;
            }

            Statement sentencia2 = conexión.createStatement();
            int desplazamiento = página * ConfiguracionesGeneralesDB.ELEMENTOS_POR_PAGINA;
            String ordenSQL2 = "SELECT p.cod_producto, p.cod_QR, p.marca, p.modelo, p.descripcion, p.idfoto, m.talla, m.color, m.material, m.sexo, m.categoria_moda, pp.idproductoempresa, pp.cantidad, pp.precioventa, pp.habilitado, pp.archivado FROM productos p INNER JOIN moda m INNER JOIN productospublicados pp ON (m.cod_producto = p.cod_producto AND p.cod_producto = pp.cod_producto) WHERE pp.habilitado = 1 AND pp.archivado = 0 LIMIT " + desplazamiento + ", " + ConfiguracionesGeneralesDB.ELEMENTOS_POR_PAGINA;
            ResultSet resultado2 = sentencia2.executeQuery(ordenSQL2);
            while (resultado2.next()) {
                String cod_producto = resultado2.getString("cod_producto");
                Log.i("sql", "prueba cod");
                String cod_QR = resultado2.getString("cod_QR");
                String marca = resultado2.getString("marca");
                String modelo = resultado2.getString("modelo");
                String descripción = resultado2.getString("descripcion");
                int id_foto = resultado2.getInt("idfoto");
                String talla = resultado2.getString("talla");
                String color = resultado2.getString("color");
                String material = resultado2.getString("material");
                String sexo = resultado2.getString("sexo");
                String categoria_moda = resultado2.getString("categoria_moda");
                Moda moda = new Moda(cod_producto, cod_QR, marca, modelo, descripción, id_foto, talla, color, material, sexo, categoria_moda);

                int idproductoempresa = resultado2.getInt("idproductoempresa");
                int cantidad = resultado2.getInt("cantidad");
                double precioventa = resultado2.getDouble("precioventa");
                int habilitadoI = resultado2.getInt("habilitado");
                int archivadoI = resultado2.getInt("archivado");
                boolean habilitado;
                if (habilitadoI == 1) {
                    habilitado = true;
                } else {
                    habilitado = false;
                }
                boolean archivado;
                if (archivadoI == 1) {
                    archivado = true;
                } else {
                    archivado = false;
                }

                ProductosPublicados productoPublicado = new ProductosPublicados(idproductoempresa, cantidad, precioventa, habilitado, archivado, moda, empresa);
                productosPublicadosDevueltos.add(productoPublicado);
            }
            resultado2.close();
            sentencia2.close();

            conexión.close();

            return productosPublicadosDevueltos;
        } catch (SQLException e) {
            Log.i("SQL", "Error al mostrar los productos publicados de la base de datos");
            return null;
        }
    }
    */

    public static ArrayList<ProductosPublicados> obtenerProductosPublicadosPorEmpresa(int página, String cod_empresa) {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return null;
        }
        ArrayList<ProductosPublicados> productosPublicadosDevueltos = new ArrayList<ProductosPublicados>();
        try {
            Empresa empresa = new Empresa("", "", "");
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT * FROM empresas";
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            int numFilas = 0;
            while (resultado.next()) {
                numFilas++;
                String cod_empr = resultado.getString("cod_empr");
                String clave_empr = resultado.getString("clave_empr");
                String datos_empr = resultado.getString("datos_empr");
                empresa = new Empresa(cod_empr, clave_empr, datos_empr);
            }
            resultado.close();
            sentencia.close();
            if (numFilas == 0) {
                return null;
            }

            Statement sentencia2 = conexión.createStatement();
            int desplazamiento = página * ConfiguracionesGeneralesDB.ELEMENTOS_POR_PAGINA;
            String ordenSQL2 = "SELECT p.cod_producto, p.cod_QR, p.marca, p.modelo, p.descripcion, p.idfoto, m.talla, m.color, m.material, m.sexo, m.categoria_moda, pp.idproductoempresa, pp.cantidad, pp.precioventa, pp.habilitado, pp.archivado FROM productos p INNER JOIN moda m INNER JOIN productospublicados pp INNER JOIN empresas e ON (m.cod_producto = p.cod_producto AND p.cod_producto = pp.cod_producto AND pp.cod_empresa = e.cod_empr) WHERE pp.habilitado = 1 AND pp.archivado = 0 AND e.cod_empr = ? LIMIT " + desplazamiento + ", " + ConfiguracionesGeneralesDB.ELEMENTOS_POR_PAGINA;
            PreparedStatement sentenciaPreparada = conexión.prepareStatement(ordenSQL2);
            sentenciaPreparada.setString(1, cod_empresa);
            ResultSet resultado2 = sentenciaPreparada.executeQuery();
            while (resultado2.next()) {
                Moda moda = null;

                String cod_producto = resultado2.getString("cod_producto");
                Log.i("sql", "prueba cod");
                String cod_QR = resultado2.getString("cod_QR");
                String marca = resultado2.getString("marca");
                String modelo = resultado2.getString("modelo");
                String descripción = resultado2.getString("descripcion");
                int id_foto = resultado2.getInt("idfoto");
                String talla = resultado2.getString("talla");
                String color = resultado2.getString("color");
                String material = resultado2.getString("material");
                String sexo = resultado2.getString("sexo");
                String categoria_moda = resultado2.getString("categoria_moda");

                if (id_foto == 1){
                    moda = new Moda(cod_producto, cod_QR, marca, modelo, descripción, null, talla, color, material, sexo, categoria_moda);
                }else {
                    Blob imagen = null;

                    String ordenSQL3 = "SELECT foto FROM fotos_productos WHERE idfotos = ?";
                    PreparedStatement sentenciaPreparada2 = conexión.prepareStatement(ordenSQL3);
                    sentenciaPreparada2.setInt(1, id_foto);
                    ResultSet resultado3 = sentenciaPreparada2.executeQuery();
                    while (resultado3.next()) {
                        imagen = resultado3.getBlob("foto");
                    }
                    moda = new Moda(cod_producto, cod_QR, marca, modelo, descripción, imagen, talla, color, material, sexo, categoria_moda);

                    sentenciaPreparada2.close();
                    resultado3.close();
                }

                int idproductoempresa = resultado2.getInt("idproductoempresa");
                int cantidad = resultado2.getInt("cantidad");
                double precioventa = resultado2.getDouble("precioventa");
                int habilitadoI = resultado2.getInt("habilitado");
                int archivadoI = resultado2.getInt("archivado");
                boolean habilitado;
                if (habilitadoI == 1) {
                    habilitado = true;
                } else {
                    habilitado = false;
                }
                boolean archivado;
                if (archivadoI == 1) {
                    archivado = true;
                } else {
                    archivado = false;
                }

                ProductosPublicados productoPublicado = new ProductosPublicados(idproductoempresa, cantidad, precioventa, habilitado, archivado, moda, empresa);
                productosPublicadosDevueltos.add(productoPublicado);
            }
            resultado2.close();
            sentenciaPreparada.close();
            sentencia2.close();

            conexión.close();

            return productosPublicadosDevueltos;
        } catch (SQLException e) {
            Log.i("SQL", "Error al mostrar los productos publicados de la base de datos");
            return null;
        }
    }

    //REPASAR MÉTODO
    /*
    public static ArrayList<ProductosPublicados> buscarProductoPublicados(String nombreP) {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return null;
        }
        ArrayList<ProductosPublicados> productosPublicadosEncontrados = new ArrayList<ProductosPublicados>();
        try {
            ResultSet resultado = BaseDB.buscarFilas(conexión, "productospublicados", "idproductoempresa", nombreP);
            if (resultado == null) {
                return null;
            }
            while (resultado.next()) {
                int idproductoempresa = resultado.getInt("idproductoempresa");
                int cantidad = resultado.getInt("cantidad");
                double precioventa = resultado.getDouble("precioventa");
                boolean habilitado = resultado.getBoolean("habilitado");
                boolean archivado = resultado.getBoolean("archivado");
                String cod_producto = resultado.getString("cod_producto");
                String cod_empresa = resultado.getString("cod_empresa");
                ProductosPublicados p = new ProductosPublicados(idproductoempresa, cantidad, precioventa, habilitado, archivado, cod_producto, cod_empresa);
                productosPublicadosEncontrados.add(p);
            }
            resultado.close();

            conexión.close();

            return productosPublicadosEncontrados;
        } catch (SQLException e) {
            Log.i("SQL", "Error al buscar los productos publicados en la base de datos");
            return null;
        }
    }
    */

    public static int obtenerCantidadProductosPublicados() {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return 0;
        }
        int cantidadProductosPublicados = 0;
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT count(*) as cantidad FROM productospublicados";
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while (resultado.next()) {
                cantidadProductosPublicados = resultado.getInt("cantidad");
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            return cantidadProductosPublicados;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver el número de productos publicados de la base de datos");
            return 0;
        }
    }
}
