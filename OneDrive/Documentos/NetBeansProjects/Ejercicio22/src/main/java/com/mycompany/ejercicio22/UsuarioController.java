/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicio22;
import java.util.ArrayList;
public class UsuarioController {
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    public void agregarUsuario (Usuario u){
        usuarios.add(u);
    }
    public void listarUsuarios (){
        for (Usuario u : usuarios) {
            System.out.println(u.toString());
        }
    } 
    public void actualizarEmail(int id, String nuevoEmail){
        for (Usuario u : usuarios) {
            if(id == u.getId() ){
                u.setEmail(nuevoEmail);
                return;
            } 
        }
    }
    public void eliminarUsuario(int id){
        for (Usuario u: usuarios) {
            if(id == u.getId()){
                usuarios.remove(u);
                return;
            }
        }
    }
}
