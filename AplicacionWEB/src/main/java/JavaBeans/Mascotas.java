package JavaBeans;
import java.io.Serializable;
import java.util.Date;

public class Mascotas implements Serializable {
    private static final long serialVersionUID = 1L;

    private int MascotaID;
    private int R_Usuario;
    private String Nombre;
    private int R_Especie;
    private int Edad;
    private String Sexo;
    private String Color;
    private String CaracteristicasDistintivas;
    private boolean Microchip;
    private int Numero_Microchip;
    private String Estado;
    private Date Fecha_Registro;

    public Mascotas() {
    }

    public int getMascotaID() {
        return MascotaID;
    }

    public void setMascotaID(int mascotaID) {
        MascotaID = mascotaID;
    }

    public int getR_Usuario() {
        return R_Usuario;
    }

    public void setR_Usuario(int r_Usuario) {
        R_Usuario = r_Usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getR_Especie() {
        return R_Especie;
    }

    public void setR_Especie(int r_Especie) {
        R_Especie = r_Especie;
    }

    public int getEdad() {
        return Edad;
    }

    public void setEdad(int edad) {
        Edad = edad;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getCaracteristicasDistintivas() {
        return CaracteristicasDistintivas;
    }

    public void setCaracteristicasDistintivas(String caracteristicasDistintivas) {
        CaracteristicasDistintivas = caracteristicasDistintivas;
    }

    public boolean isMicrochip() {
        return Microchip;
    }

    public void setMicrochip(boolean microchip) {
        Microchip = microchip;
    }

    public int getNumero_Microchip() {
        return Numero_Microchip;
    }

    public void setNumero_Microchip(int numero_Microchip) {
        Numero_Microchip = numero_Microchip;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public Date getFecha_Registro() {
        return Fecha_Registro;
    }

    public void setFecha_Registro(Date fecha_Registro) {
        Fecha_Registro = fecha_Registro;
    }
}
