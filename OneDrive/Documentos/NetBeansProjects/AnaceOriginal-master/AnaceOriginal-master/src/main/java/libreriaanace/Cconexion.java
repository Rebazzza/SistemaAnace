package libreriaanace;


import java.sql.Connection;

import javax.swing.JOptionPane;
import java.sql.DriverManager;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ma5ti
 */
public class Cconexion {
    Connection conectar = null;
    String usuario= "ma";
    String contraseña = "123";
    String bd = "LibreriaAnace";
    String ip = "localhost";
    String puerto= "1433";
    String cadena = "jdbc:sqlserver://"+ip+":"+puerto+"/"+bd;
    public Connection establecerConexion(){
        try {
            String cadena = "jdbc:sqlserver://localhost:" + puerto + ";"
                       + "databaseName=" + bd + ";"
                       + "encrypt=true;"
                       + "trustServerCertificate=true;";
            conectar = DriverManager.getConnection(cadena, usuario,contraseña);
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos, error: "+e.toString());
        }
        return conectar;
    }
       
}
