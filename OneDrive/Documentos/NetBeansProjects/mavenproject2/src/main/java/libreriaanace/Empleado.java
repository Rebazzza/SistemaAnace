
package libreriaanace;

/**
 *
 * @author ma5ti
 */
public class Empleado extends Persona{
    private String contraseña;
    private String rol;
    private String correo;

    public Empleado() {
    }

    public Empleado(String nombre, String telefono, String dni,String contraseña, String rol, String correo) {
        super(nombre,telefono,dni);
        this.contraseña = contraseña;
        this.rol = rol;
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        if(contraseña.isEmpty()){
            throw new IllegalArgumentException("La contraseña no puede estar vacia");
        }
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        if(rol.isEmpty()){
            throw new IllegalArgumentException("El rol no puede estar vacio");
        }
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if(correo.isEmpty()){
            throw new IllegalArgumentException("El correo no puede estar vacio");
        }
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Empleado{"+ super.toString() + "contrase\u00f1a=" + contraseña + ", rol=" + rol + ", correo=" + correo + '}';
    }
    
}
