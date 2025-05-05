/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreriaanace;

/**
 *
 * @author ma5ti
 */
public class Producto {
    
    private String codigo;
    private String nombre;
    private int stock;
    private double precio;
    private String marca;

    public Producto() {
    }

    public Producto(String codigo, String nombre, int stock, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
    }

    public Producto(String codigo, String nombre, int stock, double precio, String marca) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
        this.marca = marca;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        
        if( codigo.trim().isEmpty()){
            throw new IllegalArgumentException("El codigo no puede estar vacío");
        }
        this.codigo =codigo;
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if( nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if(stock <= 0){
            throw new IllegalArgumentException("El Stock tiene que ser mayor a 0");
        }
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if(precio < 0){
            throw new IllegalArgumentException("El precio tiene que ser mayor a 0");
        }
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        
        this.marca = marca;
    }
    public void obtenerMarca(String codigo){
        
    }
    @Override
    public String toString() {
        return "Producto{" + "codigo=" + codigo + ", nombre=" + nombre + ", stock=" + stock + ", precio=" + precio + ", marca=" + marca + '}';
    }
    

}
