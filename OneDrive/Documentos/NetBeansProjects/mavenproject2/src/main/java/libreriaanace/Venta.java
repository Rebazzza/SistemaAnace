
package libreriaanace;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ma5ti
 */
public class Venta {
    private String codigoVenta;
    private List<ProductoVendido> productos = new ArrayList<>();
    private double total;
    private LocalDate fecha;
    private double descuento;
    public Venta() {
    }

    public Venta(String codigoVenta, double total, LocalDate fecha) {
        this.codigoVenta = codigoVenta;
        this.total = total;
        this.fecha = fecha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public List<ProductoVendido> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoVendido> productos) {
        this.productos = productos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if(total < 0){
            throw new IllegalArgumentException("El total tiene que ser mayor a 0");
        }
        this.total = total;
    }

    @Override
    public String toString() {
        return "Venta{" + "codigoVenta=" + codigoVenta + ", productos=" + productos + ", total=" + total + ", fecha=" + fecha + '}';
    }

   
    
    
    

    

    
    
    
}
