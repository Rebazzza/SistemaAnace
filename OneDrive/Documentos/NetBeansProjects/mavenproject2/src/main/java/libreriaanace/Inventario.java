/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreriaanace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ma5ti
 */
public class Inventario {

    Cconexion objetoConexion = new Cconexion();
    Connection conexion = objetoConexion.establecerConexion();
    Scanner linea = new Scanner(System.in);
    private List<Producto> productos = new ArrayList<>();
    private List<Marca> marcas = new ArrayList<>();

    public List<Marca> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<Marca> marcas) {
        this.marcas = marcas;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Inventario() {
    }

    public void RegistrarProducto(String nombre, String codigo, String codigoMarca, String stockStr, String precioStr) {
        nombre = nombre.trim();
        codigo = codigo.trim();
        codigoMarca = codigoMarca.trim();
        stockStr = stockStr.trim();
        precioStr = precioStr.trim();
        if (existeMarca(codigo)) {

        } else {
            mostrarError("Marca no encontrada");
        }
        if (codigoMarca.isEmpty()) {
            mostrarError("El codigo de marca no puede estar vacío");
            return;
        }
        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacío");
            return;
        }
        if (codigo.isEmpty()) {
            mostrarError("El código no puede estar vacío");
            return;
        }
        if (existeProducto(codigo)) {
            mostrarError("El código ya está registrado");
            return;
        }

        // Validar stock
        int stock;
        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                mostrarError("El stock no puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarError("Stock inválido (debe ser un número entero)");
            return;
        }

        // Validar precio
        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                mostrarError("El precio no puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarError("Precio inválido (debe ser un número decimal)");
            return;
        }
        Producto pc = new Producto(codigo, nombre, stock, precio, codigoMarca);
        agregarProducto(pc);

    }

    public void agregarProducto(Producto p) {
        String sql = "INSERT INTO PRODUCTO (CODIGO, CODIGO_MARCA, NOMBRE, STOCK, PRECIO) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());       // CODIGO (clave primaria)
            ps.setString(2, p.getMarca());        // CODIGO_MARCA (clave foránea)
            ps.setString(3, p.getNombre());       // NOMBRE
            ps.setInt(4, p.getStock());           // STOCK
            ps.setDouble(5, p.getPrecio());       // PRECIO (tipo MONEY en SQL)

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto registrado con éxito.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar producto: " + e.getMessage());
        }
    }

    public void RegistrarMarca(String nombre, String codigo) {
        nombre = nombre.trim();
        codigo = codigo.trim();

        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacio");
            return;
        }
        if (codigo.isEmpty()) {
            mostrarError("El codigo no puede estar vacio");
            return;
        }
        if (existeMarca(codigo)) {
            mostrarError("Codigo ya existente");
            return;
        }
        Marca mr = new Marca(nombre, codigo);
        AgregarMarca(mr);

    }

    public void AgregarMarca(Marca m) {
        marcas.add(m);

        String sql = "INSERT INTO MARCA (NOMBRE, CODIGO) VALUES (?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(2, m.getNombre());
            ps.setString(1, m.getCodigo());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Marca registrado con éxito.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar marca: " + e.getMessage());
        }
    }

    public void agregarMarca(Marca m) {
        marcas.add(m);
    }

    public void mostrarProductos() {

        System.out.println("---------------ANACE---------------");
        if (productos.isEmpty()) {
            System.out.println("Inventario vacio");
        } else {
            System.out.println("Productos:");
            for (Producto p : productos) {
                System.out.println(p.toString());
            }
        }

    }

    public Producto buscarProductoPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo)) {
                System.out.println("Producto encontrado");
                System.out.println(p.toString());
                return p;
            }
        }
        return null;
    }

    public Producto buscarProductoPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equals(nombre)) {
                System.out.println("Producto encontrado");
                System.out.println(p.toString());
                return p;
            }
        }
        return null;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public boolean existeMarca(String codigo) {
        Cconexion objConexion = new Cconexion();
        Connection conn = objConexion.establecerConexion();
        boolean existe = false;

        String sql = "SELECT 1 FROM MARCA WHERE CODIGO = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            existe = rs.next();
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return existe;
    }

    public boolean existeProducto(String codigo) {
        Cconexion objConexion = new Cconexion();
        Connection conn = objConexion.establecerConexion();
        boolean existe = false;

        String sql = "SELECT 1 FROM PRODUCTO WHERE CODIGO = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            existe = rs.next();
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return existe;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Informacion: ", JOptionPane.INFORMATION_MESSAGE);
    }

    public DefaultTableModel obtenerProductosConMarca() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Marca");
        modelo.addColumn("Stock");
        modelo.addColumn("Precio");

        String sql = "SELECT P.CODIGO, P.NOMBRE, M.NOMBRE AS MARCA, P.STOCK, P.PRECIO "
                + "FROM PRODUCTO P INNER JOIN MARCA M ON P.CODIGO_MARCA = M.CODIGO";

        try (Connection conn = conexion; PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = {
                    rs.getString("CODIGO"),
                    rs.getString("NOMBRE"),
                    rs.getString("MARCA"),
                    rs.getInt("STOCK"),
                    rs.getDouble("PRECIO")
                };
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener productos: " + e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel buscarProductoPorNombreTabla(String nombre) {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("Código");
    modelo.addColumn("Nombre");
    modelo.addColumn("Código Marca");
    modelo.addColumn("Stock");
    modelo.addColumn("Precio");

    Cconexion objConexion = new Cconexion();
    Connection conn = objConexion.establecerConexion();

    String sql = "SELECT * FROM PRODUCTO WHERE NOMBRE = ?";

    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre.trim());  // Comparación exacta
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] fila = {
                rs.getString("CODIGO"),
                rs.getString("NOMBRE"),
                rs.getString("CODIGO_MARCA"),
                rs.getInt("STOCK"),
                rs.getDouble("PRECIO")
            };
            modelo.addRow(fila);
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar producto: " + e.getMessage());
    }

    return modelo;
}
public DefaultTableModel buscarProductoPorCodigoTabla(String nombre) {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("Código");
    modelo.addColumn("Nombre");
    modelo.addColumn("Código Marca");
    modelo.addColumn("Stock");
    modelo.addColumn("Precio");

    Cconexion objConexion = new Cconexion();
    Connection conn = objConexion.establecerConexion();

    String sql = "SELECT * FROM PRODUCTO WHERE CODIGO = ?";

    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre.trim());  // Comparación exacta
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Object[] fila = {
                rs.getString("CODIGO"),
                rs.getString("NOMBRE"),
                rs.getString("CODIGO_MARCA"),
                rs.getInt("STOCK"),
                rs.getDouble("PRECIO")
            };
            modelo.addRow(fila);
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar producto: " + e.getMessage());
    }

    return modelo;
}
public void eliminarProductoPorNombreExacto(String nombre) {
    Cconexion objConexion = new Cconexion();
    Connection conn = objConexion.establecerConexion();

    String sql = "DELETE FROM PRODUCTO WHERE CODIGO = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nombre);

        int filasAfectadas = ps.executeUpdate();

        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
    }
}
public DefaultTableModel MostrarMarcas(){
     DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Marca");

        Cconexion con = new Cconexion();
        Connection conexion = con.establecerConexion();

        try {
            String consulta = "SELECT * FROM MARCA";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("CODIGO");
                fila[1] = rs.getString("NOMBRE");
                modelo.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        }

        return modelo;
}
public DefaultTableModel MostrarProductos(){
     DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Marca");

        Cconexion con = new Cconexion();
        Connection conexion = con.establecerConexion();

        try {
            String consulta = "SELECT * FROM PRODUCTO";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("CODIGO");
                fila[1] = rs.getString("NOMBRE");
                fila[2] = rs.getString("STOCK");
                fila[3] = rs.getString("PRECIO");
                modelo.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        }

        return modelo;
}public double agregarProductoPorCodigo(String codigo, JTable tabla, int cantidad, double totalActual) {
    Cconexion objConexion = new Cconexion();
    Connection conn = objConexion.establecerConexion();
    String sql = "SELECT * FROM PRODUCTO WHERE CODIGO = ?";

    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, codigo.trim());
        ResultSet rs = ps.executeQuery();

        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();  // modelo existente

        if (rs.next()) {
            String cod = rs.getString("CODIGO");
            String nombre = rs.getString("NOMBRE");
            double precio = rs.getDouble("PRECIO");

            double subtotal = cantidad * precio;
            totalActual += subtotal;

            Object[] fila = {cod, cantidad, nombre, precio, subtotal};
            modelo.addRow(fila);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un producto con ese código.");
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al buscar producto: " + e.getMessage());
    }

    return totalActual;
}
}
