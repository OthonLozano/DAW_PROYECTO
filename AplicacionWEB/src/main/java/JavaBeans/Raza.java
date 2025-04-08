package JavaBeans;
import java.io.Serializable;

public class Raza implements Serializable{
    private static final long serialVersionUID = 1L;

    private int RazaID;
    private String Nombre;
    private String Descripcion;

    public Raza() {
    }

    public int getRazaID() {
        return RazaID;
    }

    public void setRazaID(int razaID) {
        RazaID = razaID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
