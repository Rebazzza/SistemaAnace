/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicio22;

/**
 *
 * @author ma5ti
 */
public class Usuario {
      public int id;
    public String nombre;
    public String email;
    // CONSTRUCTORES
    public Usuario(){}
    public Usuario (int x, String y, String z){
        this.id = x;
        this.nombre = y;
        this.email = z;        
    }

    //GET Y SET
    public void setId(int x){
        this.id = x;
    }
    public int getId(){
        return this.id;
    }
    public void setNombre(String x){
        this.nombre = x;        
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setEmail(String correo){
        this.email = correo;
    }
    public String getEmail(){
        return this.email;
    }
    @Override
    public String toString(){
        return "Id: " + this.getId() + " Nombre: "+ this.getNombre() + 
        " Email: " + this.getEmail();        
    }
}
