/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreriaanace;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ma5ti
 */
public class VentaController {
    
    
    Inventario i = new Inventario();
    private List<Venta> VentasTotales = new  ArrayList<>();
    Scanner linea = new Scanner(System.in);
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
