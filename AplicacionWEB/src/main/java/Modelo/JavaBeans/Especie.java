package Modelo.JavaBeans;
import java.io.Serializable;

public class Especie implements Serializable {
    private static final long serialVersionUID = 1L;

    private int EspecieID;
    private String Nombre;
    private String Descripcion;
    private int R_Raza;

    public Especie() {
    }

    public int getEspecieID() {
        return EspecieID;
    }

    public void setEspecieID(int especieID) {
        EspecieID = especieID;
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

    public int getR_Raza() {
        return R_Raza;
    }

    public void setR_Raza(int r_Raza) {
        R_Raza = r_Raza;
    }
}
