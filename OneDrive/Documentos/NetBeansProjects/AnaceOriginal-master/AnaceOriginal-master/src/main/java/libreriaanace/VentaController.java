/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreriaanace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ma5ti
 */
public class VentaController {
    
    
    Inventario i = new Inventario();
    private List<Venta> VentasTotales = new  ArrayList<>();
    Scanner linea = new Scanner(System.in);
    public DefaultTableModel obtenerVentasPorEmpleado(int codigoEmpleado) {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("Código Venta");
    modelo.addColumn("Fecha");
    modelo.addColumn("Total");
    modelo.addColumn("Descuento");

    Cconexion objConexion = new Cconexion();
    Connection conn = objConexion.establecerConexion();

    String sql = "SELECT V.CODIGO, V.FECHA, V.TOTAL, V.DESCUENTO " +
                 "FROM VENTA V WHERE V.CODIGO_EMPLEADO = ?";

    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, codigoEmpleado);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] fila = new Object[]{
                rs.getString("CODIGO"),
                rs.getDate("FECHA"),
                rs.getDouble("TOTAL"),
                rs.getDouble("DESCUENTO")
            };
            modelo.addRow(fila);
        }

        rs.close();
        ps.close();
        conn.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener ventas: " + e.getMessage());
    }

    return modelo;
}

public boolean registrarVenta(String codigoVenta, int codigoEmpleado, Date fecha, double descuento,
                              JTable tablaProductos, JLabel lblTotal) {

    Connection conn = null;
    PreparedStatement psVenta = null;
    PreparedStatement psDetalle = null;
    PreparedStatement psActualizarStock = null;

    try {
        Cconexion conexion = new Cconexion();
        conn = conexion.establecerConexion();
        conn.setAutoCommit(false); // Transacción manual

        // Insertar en VENTA
        String sqlVenta = "INSERT INTO VENTA (CODIGO, CODIGO_EMPLEADO, TOTAL, FECHA, DESCUENTO) VALUES (?, ?, ?, ?, ?)";
        psVenta = conn.prepareStatement(sqlVenta);

        double totalVenta = Double.parseDouble(lblTotal.getText().replace("S/", "").trim());

        psVenta.setString(1, codigoVenta);
        psVenta.setInt(2, codigoEmpleado);
        psVenta.setDouble(3, totalVenta);
        psVenta.setDate(4, new java.sql.Date(fecha.getTime()));
        psVenta.setDouble(5, descuento);
        psVenta.executeUpdate();

        
        String sqlDetalle = "INSERT INTO DETALLEVENTAS (CODIGO, CODIGO_VENTA, CODIGO_PRODUCTO, CANTIDAD, PRECIO_UNITARIO) VALUES (?, ?, ?, ?, ?)";
        psDetalle = conn.prepareStatement(sqlDetalle);

        // Actualizar STOCK
        String sqlActualizarStock = "UPDATE PRODUCTO SET STOCK = STOCK - ? WHERE CODIGO = ?";
        psActualizarStock = conn.prepareStatement(sqlActualizarStock);

        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String codProducto = modelo.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(modelo.getValueAt(i, 1).toString());
            double precioUnitario = Double.parseDouble(modelo.getValueAt(i, 3).toString());
            String codDetalle = codigoVenta + "-D" + (i + 1);

            // Insertar detalle
            psDetalle.setString(1, codDetalle);
            psDetalle.setString(2, codigoVenta);
            psDetalle.setString(3, codProducto);
            psDetalle.setInt(4, cantidad);
            psDetalle.setDouble(5, precioUnitario);
            psDetalle.addBatch();

            // Actualizar stock
            psActualizarStock.setInt(1, cantidad);
            psActualizarStock.setString(2, codProducto);
            psActualizarStock.addBatch();
        }

        psDetalle.executeBatch();
        psActualizarStock.executeBatch();

        conn.commit();
        JOptionPane.showMessageDialog(null, "Venta registrada correctamente.");
        return true;

    } catch (Exception e) {
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Error al registrar venta: " + e.getMessage());
        return false;

    } finally {
        try {
            if (psVenta != null) psVenta.close();
            if (psDetalle != null) psDetalle.close();
            if (psActualizarStock != null) psActualizarStock.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    public DefaultTableModel obtenerDetallesPorVenta(String codVenta) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio Unitario");
        modelo.addColumn("Precio Total");

        String sql = "SELECT D.CODIGO_PRODUCTO, D.CANTIDAD, D.PRECIO_UNITARIO, " +
                     "(D.CANTIDAD * D.PRECIO_UNITARIO) AS PRECIO_TOTAL " +
                     "FROM DETALLEVENTAS D WHERE D.CODIGO_VENTA = ?";

        try (Connection conn = new Cconexion().establecerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codVenta);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("CODIGO_PRODUCTO"),
                    rs.getInt("CANTIDAD"),
                    rs.getDouble("PRECIO_UNITARIO"),
                    rs.getDouble("PRECIO_TOTAL")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener detalles: " + e.getMessage());
        }

        return modelo;
    }
    public void generarVenta(){
        do{
            Venta v = new Venta();
            ArrayList<ProductoVendido> productos = new ArrayList<>();
            System.out.println("----------GENERAR-VENTA----------");
            System.out.println("[1]Agregar producto");  
            System.out.println("[2]Establecer fecha");  
            System.out.println("[3]Generar Total");
            System.out.println("[4]Establecer descuento");
            System.out.println("[5]Generar Venta");
            
            int op = linea.nextInt();
            linea.nextLine();
            switch(op){
                case 1:
                    
                    listarProducto(productos);
                case 2:
                    LocalDate fecha = null;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate hoy = LocalDate.now();
                    LocalDate minimo = hoy.minusDays(3);
                    while (true) {
                        
                        
                        System.out.println("Ingresar fecha de venta: (Formato: dd/MM/yyyy)");
                        
                        String fecha1 = linea.nextLine();
                        
                        try {
                            if (fecha1.trim().isEmpty()) {
                                throw new IllegalArgumentException("La fecha no puede ser vacio");
                            }
                            fecha = LocalDate.parse(fecha1, formatter); 
                            if(fecha.isAfter(hoy)){
                                throw new IllegalArgumentException("Fecha no puede ser futura a la actual");
                            }
                            if(fecha.isAfter(minimo)){
                                throw new IllegalArgumentException("Fecha no puede ser 3 dias antes que fecha actual");
                            }
                            break;
                        } catch (IllegalArgumentException  | DateTimeParseException e ) {
                            System.out.println("Error: Formato de fecha invalida");
                        }
                        
                    }
                    
                case 4:
                    while (true) {
                        System.out.println("Ingresar codigo de venta: ");
                        String codigo = linea.nextLine();
                        try {
                            if (codigo.trim().isEmpty()) {
                                throw new IllegalArgumentException("codigo no puede ser vacio");
                            }
                            
                            v.setCodigoVenta(codigo);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    
                    v.setProductos(productos);
                    
            }
        }while(true);
    }
    public void listarProducto(ArrayList<ProductoVendido> productos ){
        Venta v = new Venta();
        Producto pm=null;
        ProductoVendido pv = new ProductoVendido();
        Producto productoVendido = new Producto();
        int cant;
       
        
        while (true) {
             
            System.out.println("Ingresar nombre de producto: ");
            String nombre = linea.nextLine().trim();
            for (Producto p : i.getProductos() ) {
                    if(p.getNombre().equals(nombre)){
                        pm = p;
                    }
                    else{
                        pm  =null;
                    }
                }
            try {
                if (pm == null) {
                    throw new IllegalArgumentException("Nombre no existe");
                }
                if(nombre.trim().isEmpty()){
                    throw new IllegalArgumentException("Nombre no puede ser vacio");
                }
                if(i.buscarProductoPorNombre(nombre)!=null){
                    productoVendido = i.buscarProductoPorNombre(nombre);
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        while (true) {
            System.out.println("Ingrese cantidad vendida del producto: ");
            cant = linea.nextInt();
            try {
                
                if(cant <= 0){
                    throw new IllegalArgumentException("Cantidad no puede ser 0 / menor que 0");
                }
                
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        pv.setProducto(productoVendido);
        pv.setCantidad(cant);
        productos.add(pv);
    }
    
}
