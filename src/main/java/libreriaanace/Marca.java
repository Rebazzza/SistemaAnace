/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreriaanace;

import java.util.ArrayList;

/**
 *
 * @author ma5ti
 */
public class Marca {
    private String codigo;
    private String nombre;
    ArrayList<Producto> productos = new ArrayList<>();
    public Marca() {
    }
    public Marca(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if(codigo.isEmpty()){
            throw new IllegalArgumentException("El codigo no puede estar vacío");
        }
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre.isEmpty()){
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
    
    public void agregarProducto(Producto p){
        productos.add(p);
    }

    @Override
    public String toString() {
        return "Marca{" + "codigo=" + codigo + ", nombre=" + nombre + ", productos=" + productos + '}';
    }

    
    
}
