/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreriaanace;

/**
 *
 * @author ma5ti
 */
public class Cliente extends Persona{
    private String ruc; 
    private String direccion;

    public Cliente() {
    }

    public Cliente(String ruc, String direccion) {
        this.ruc = ruc;
        this.direccion = direccion;
    }

    public Cliente(String nombre) {
        super(nombre);
    }

    public Cliente(String nombre, String telefono,String dni,String ruc, String direccion) {
        super(nombre, telefono, dni);
        this.ruc = ruc;
        this.direccion = direccion;
    }

    public String getRuc() {
        
        return ruc;
    }

    public void setRuc(String ruc) {
        if(ruc.isEmpty()){
            throw new IllegalArgumentException("El ruc no puede estar vacío");
        }
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if(direccion.isEmpty()){
            throw new IllegalArgumentException("La direccion no puede estar vacío");
        }
        this.direccion = direccion;
    }
    
    @Override
    public String toString() {
        return "Cliente{" + "ruc=" + ruc + ", direccion=" + direccion + '}';
    }
    
}
