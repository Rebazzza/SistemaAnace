
package com.mycompany.ejercicio22;

import java.util.Scanner;

public class Ejercicio22 {

    public static void main(String[] args) {
        UsuarioController controladorUsuario = new UsuarioController();
        
        Usuario u1 = new Usuario(1,"Ana", "ana@gmail.com");
        Usuario u2 = new Usuario(2,"Juan", "juan@gmail.com");
        Usuario u3 = new Usuario(3,"Abel", "abel@gmail.com");
        Usuario u4 = new Usuario(4,"Karla", "karla@gmail.com");
        
        System.out.println("Agregando usuarios...");
        controladorUsuario.agregarUsuario(u1);
        controladorUsuario.agregarUsuario(u2);
        controladorUsuario.agregarUsuario(u3);
        controladorUsuario.agregarUsuario(u4);
        
        System.out.println("\n Listar usuarios");
        controladorUsuario.listarUsuarios();
        
        System.out.println("Actualizar email");
        Scanner linea = new Scanner(System.in);
        System.out.println("Insertar id:");
        int id = linea.nextInt();
        System.out.println("Insertar nuevo email:");
        String nuevoEmail = linea.next();
        controladorUsuario.actualizarEmail(id, nuevoEmail);
        System.out.println("\n Nueva lista");
        controladorUsuario.listarUsuarios();
        
        System.out.println("Eliminar usuario");
        System.out.println("Inserte id de usuario a eliminar: ");
        int el = linea.nextInt();
        controladorUsuario.eliminarUsuario(el);
        
        System.out.println("Nueva lista:");
        controladorUsuario.listarUsuarios();
        
        
        
    }
}
