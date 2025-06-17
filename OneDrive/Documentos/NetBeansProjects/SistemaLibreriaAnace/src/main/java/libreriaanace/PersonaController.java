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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ma5ti
 */
public class PersonaController {

    Cconexion objetoConexion = new Cconexion();
    Connection conexion = objetoConexion.establecerConexion();
    Scanner linea = new Scanner(System.in);
    private List<Cliente> Clientes = new ArrayList<>();
    private List<Empleado> Empleados = new ArrayList<>();

    public List<Cliente> getClientes() {
        return Clientes;
    }

    public DefaultTableModel obtenerClientes() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Teléfono");
        modelo.addColumn("DNI");
        modelo.addColumn("RUC");
        modelo.addColumn("Dirección");

        Cconexion con = new Cconexion();
        Connection conexion = con.establecerConexion();

        try {
            String consulta = "SELECT * FROM CLIENTE";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("NOMBRE");
                fila[1] = rs.getString("TELEFONO");
                fila[2] = rs.getString("DNI");
                fila[3] = rs.getString("RUC");
                fila[4] = rs.getString("DIRECCION");
                modelo.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        }

        return modelo;
    }

    public DefaultTableModel obtenerEmpleados() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Teléfono");
        modelo.addColumn("DNI");
        modelo.addColumn("Rol");
        modelo.addColumn("Correo");

        Cconexion con = new Cconexion();
        Connection conexion = con.establecerConexion();

        try {
            String consulta = "SELECT * FROM EMPLEADO";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("NOMBRE");
                fila[1] = rs.getString("TELEFONO");
                fila[2] = rs.getString("DNI");
                fila[3] = rs.getString("ROL");
                fila[4] = rs.getString("CORREO");
                modelo.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        }

        return modelo;
    }

    public void setClientes(List<Cliente> Clientes) {
        this.Clientes = Clientes;
    }

    public List<Empleado> getEmpleados() {
        return Empleados;
    }

    public void setEmpleados(List<Empleado> Empleados) {
        this.Empleados = Empleados;
    }

    public void AgregarCliente(Cliente cl) {
        Clientes.add(cl);

        String sql = "INSERT INTO CLIENTE (NOMBRE, TELEFONO, DNI, RUC, DIRECCION) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getTelefono());
            ps.setString(3, cl.getDni());
            ps.setString(4, cl.getRuc());
            ps.setString(5, cl.getDireccion());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente registrado con éxito.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar cliente: " + e.getMessage());
        }
    }

    public void AgregarEmpleado(Empleado e) {
        Empleados.add(e);
        Cconexion objetoconexion = new Cconexion();
        Connection conn = objetoconexion.establecerConexion();

        if (conn == null) {
            JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión con la base de datos.");
            return;
        }

        String sql = "INSERT INTO EMPLEADO   (NOMBRE, TELEFONO, DNI, CONTRASEÑA, ROL, CORREO) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, e.getNombre());
            pstmt.setString(2, e.getTelefono());
            pstmt.setString(3, e.getDni());
            pstmt.setString(4, e.getContraseña());
            pstmt.setString(5, e.getRol());
            pstmt.setString(6, e.getCorreo());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empleado agregado correctamente a la base de datos.");

            pstmt.close();
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al insertar empleado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void RegistrarCliente(String nombre, String telefono, String dni, String ruc, String direccion) {
        // Eliminar espacios al inicio y final
        nombre = nombre.trim();
        telefono = telefono.trim();
        dni = dni.trim();
        ruc = ruc.trim();
        direccion = direccion.trim();

        // Validar nombre
        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacío");
            return;
        }
        if (existeClientePorNombre(nombre)) {
            mostrarError("Nombre ya existente");
            return;
        }

        // Validar DNI
        if (existeClientePorDNI(dni)) {
            mostrarError("DNI ya existente");
            return;
        }

        // Validar dirección
        // Validar teléfono
        if (telefono.isEmpty()) {
            mostrarError("El teléfono no puede estar vacío");
            return;
        }

        // Validar RUC: debe tener 0 (vacío) o 11 dígitos exactamente
        if (!ruc.isEmpty() && ruc.length() != 11) {
            mostrarError("El RUC debe tener exactamente 11 dígitos o estar vacío");
            return;
        }

        // Crear y registrar cliente
        Cliente cl = new Cliente(nombre, telefono, dni, ruc, direccion);
        AgregarCliente(cl);
    }

    public void RegistrarEmpleado(String nombre, String telefono, String dni, String contraseña, String rol, String correo) {
        if (existeEmpleadoPorNombre(nombre)) {
            mostrarError("Nombre ya existente");
            return;
        } else if (nombre.trim().isEmpty()) {
            mostrarError("Nombre no puede estar vacio");
            return;
        }

        if (existeEmpleadoPorDNI(dni)) {
            mostrarError("DNI ya existente");
            return;
        }

        if (telefono.trim().isEmpty()) {
            mostrarError("Telefono no puede estar vacio");
            return;
        }
        if (contraseña.trim().isEmpty()) {
            mostrarError("La contraseña no puede ser vacía");
            return;
        }
        if (rol.trim().isEmpty()) {
            mostrarError("El rol no puede ser vacío");
            return;
        } else if (!(rol.equals("Vendedor") || rol.equals("Administrador"))) {
            mostrarError("El rol solo puede ser 'Vendedor' o 'Administrador'");
            return;
        }
        if (correo.trim().isEmpty()) {
            mostrarError("El correo no puede ser vacío");
            return;
        }

        Empleado e = new Empleado(nombre, telefono, dni, contraseña, rol, correo);
        AgregarEmpleado(e);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Informacion: ", JOptionPane.INFORMATION_MESSAGE);
    }

    public Cliente BuscarClientePorNombre(String nombre) {
        for (Cliente cl : Clientes) {
            if (cl.getNombre().equals(nombre)) {
                System.out.println("Cliente encontrado");
                System.out.println(cl.toString());
                return cl;
            }
        }
        return null;
    }

    public Cliente BuscarClientePorDNI(String dni) {
        for (Cliente cl : Clientes) {
            if (cl.getDni().equals(dni)) {
                System.out.println("Cliente encontrado");
                System.out.println(cl.toString());
                return cl;
            }
        }
        return null;
    }

    public Empleado BuscarEmpleadoPorNombre(String nombre) {
        for (Empleado e : Empleados) {
            if (e.getNombre().equals(nombre)) {
                System.out.println("Cliente encontrado");
                System.out.println(e.toString());
                return e;
            }
        }
        return null;
    }

    public Empleado BuscarEmpleadoPorDNI(String dni) {
        for (Empleado e : Empleados) {
            if (e.getDni().equals(dni)) {
                System.out.println("Cliente encontrado");
                System.out.println(e.toString());
                return e;
            }
        }
        return null;
    }

    public boolean existeEmpleadoPorNombre(String nombre) {
        Cconexion objConexion = new Cconexion();
        Connection conn = objConexion.establecerConexion();
        boolean existe = false;

        String sql = "SELECT 1 FROM EMPLEADO WHERE NOMBRE = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
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

    public boolean existeEmpleadoPorDNI(String dni) {
        Cconexion objConexion = new Cconexion();
        Connection conn = objConexion.establecerConexion();
        boolean existe = false;

        String sql = "SELECT 1 FROM EMPLEADO WHERE DNI = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dni);
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

    public boolean existeClientePorNombre(String nombre) {
        Cconexion objConexion = new Cconexion();
        Connection conn = objConexion.establecerConexion();
        boolean existe = false;

        String sql = "SELECT 1 FROM CLIENTE WHERE NOMBRE = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
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

    public boolean existeClientePorDNI(String dni) {
        Cconexion objConexion = new Cconexion();
        Connection conn = objConexion.establecerConexion();
        boolean existe = false;

        String sql = "SELECT 1 FROM CLIENTE WHERE DNI = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dni);
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

}
