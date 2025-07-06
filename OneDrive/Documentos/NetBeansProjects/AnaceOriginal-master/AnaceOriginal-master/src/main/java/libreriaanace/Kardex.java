/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreriaanace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ma5ti
 */
public class Kardex {

    Cconexion objConexion = new Cconexion();
    Connection conn = objConexion.establecerConexion();

    public boolean registrarEntradaKardex(String codigoProducto, int cantidad, double precioUnitario, Date fecha, String descripcion,String codigoRegistro) {
        Cconexion conexion = new Cconexion();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexion.establecerConexion();

            // Obtener stock anterior
            int stockAnterior = 0;
            String sqlStock = "SELECT STOCK FROM PRODUCTO WHERE CODIGO = ?";
            ps = conn.prepareStatement(sqlStock);
            ps.setString(1, codigoProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                stockAnterior = rs.getInt("STOCK");
            }
            rs.close();
            ps.close();

            int nuevoStock = stockAnterior + cantidad;
            String sqlUpdateStock = "UPDATE PRODUCTO SET STOCK = ? WHERE CODIGO = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock);
            psUpdate.setInt(1, nuevoStock); // El nuevo stock después de la salida
            psUpdate.setString(2, codigoProducto);
            psUpdate.executeUpdate();
            psUpdate.close();

            // Insertar en KARDEX
            String sqlKardex = "INSERT INTO KARDEX (CODIGO_PRODUCTO, FECHA, TIPO_MOVIMIENTO, CANTIDAD, PRECIO_UNITARIO, STOCK_ANTERIOR, STOCK_ACTUAL,DESCRIPCION,CODIGO_REGISTRO) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlKardex);
            ps.setString(1, codigoProducto);
            ps.setDate(2, new java.sql.Date(fecha.getTime()));
            ps.setString(3, "Entrada");
            ps.setInt(4, cantidad);
            ps.setDouble(5, precioUnitario);
            ps.setInt(6, stockAnterior);
            ps.setInt(7, nuevoStock);
            ps.setString(8, descripcion);
            ps.setString(9, codigoRegistro);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean registrarSalidaKardex(String codigoProducto, int cantidad, double precioUnitario, Date fecha, String descripcion,String codigoRegistro) {
        Cconexion conexion = new Cconexion();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = conexion.establecerConexion();

            // Obtener stock anterior
            int stockAnterior = 0;
            String sqlStock = "SELECT STOCK FROM PRODUCTO WHERE CODIGO = ?";
            ps = conn.prepareStatement(sqlStock);
            ps.setString(1, codigoProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                stockAnterior = rs.getInt("STOCK");
            }
            rs.close();
            ps.close();

            int nuevoStock = stockAnterior - cantidad;
            String sqlUpdateStock = "UPDATE PRODUCTO SET STOCK = ? WHERE CODIGO = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock);
            psUpdate.setInt(1, nuevoStock); // El nuevo stock después de la salida
            psUpdate.setString(2, codigoProducto);
            psUpdate.executeUpdate();
            psUpdate.close();
            // Insertar en KARDEX
            String sqlKardex = "INSERT INTO KARDEX (CODIGO_PRODUCTO, FECHA, TIPO_MOVIMIENTO, CANTIDAD, PRECIO_UNITARIO, STOCK_ANTERIOR, STOCK_ACTUAL, DESCRIPCION,CODIGO_REGISTRO) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlKardex);
            ps.setString(1, codigoProducto);
            ps.setDate(2, new java.sql.Date(fecha.getTime()));
            ps.setString(3, "Salida");
            ps.setInt(4, cantidad);
            ps.setDouble(5, precioUnitario);
            ps.setInt(6, stockAnterior);
            ps.setInt(7, nuevoStock);
            ps.setString(8, descripcion);
            ps.setString(9, codigoRegistro);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void mostrarKardexGeneral(JTable tabla) {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.setColumnIdentifiers(new Object[]{"Fecha", "Producto", "N°Registro/N°Venta" , "Detalle", "Entrada", "Salida", "Existencia", "Precio Unitario"});

    Cconexion con = new Cconexion();
    Connection conn = con.establecerConexion();

    String sql = "SELECT K.FECHA, P.NOMBRE AS PRODUCTO,K.CODIGO_REGISTRO, K.DESCRIPCION, " +
                 "CASE WHEN K.TIPO_MOVIMIENTO = 'Entrada' THEN K.CANTIDAD ELSE 0 END AS ENTRADA, " +
                 "CASE WHEN K.TIPO_MOVIMIENTO = 'Salida' THEN K.CANTIDAD ELSE 0 END AS SALIDA, " +
                 "K.STOCK_ACTUAL AS EXISTENCIA, K.PRECIO_UNITARIO " +
                 "FROM KARDEX K " +
                 "INNER JOIN PRODUCTO P ON K.CODIGO_PRODUCTO = P.CODIGO " +
                 "ORDER BY K.FECHA, P.NOMBRE";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] fila = {
                rs.getDate("FECHA"),
                rs.getString("PRODUCTO"),
                rs.getString("CODIGO_REGISTRO"),
                rs.getString("DESCRIPCION"),
                rs.getInt("ENTRADA"),
                rs.getInt("SALIDA"),
                rs.getInt("EXISTENCIA"),
                rs.getDouble("PRECIO_UNITARIO")
            };
            modelo.addRow(fila);
        }

        tabla.setModel(modelo);

        rs.close();
        ps.close();
        conn.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al mostrar Kardex General: " + e.getMessage());
    }
}

}
