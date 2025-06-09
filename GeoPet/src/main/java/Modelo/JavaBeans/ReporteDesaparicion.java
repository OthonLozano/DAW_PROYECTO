package Modelo.JavaBeans;

import java.io.Serializable;
import java.util.Date;

public class ReporteDesaparicion implements Serializable {
    private static final long serialVersionUID = 1L;

    private int ReporteID;
    private int R_Mascota;
    private int R_Usuario;
    private Date FechaDesaparicion;
    private String UbicacionUltimaVez;
    private String DescripcionSituacion;
    private double Recompensa;
    private String EstadoReporte;
    private Date Fecha_Registro;
    private String Estatus;

    // Constructores
    public ReporteDesaparicion() {
    }

    public ReporteDesaparicion(int r_Mascota, int r_Usuario, Date fechaDesaparicion,
                               String ubicacionUltimaVez, String descripcionSituacion,
                               double recompensa, String estadoReporte, Date fecha_Registro, String estatus) {
        R_Mascota = r_Mascota;
        R_Usuario = r_Usuario;
        FechaDesaparicion = fechaDesaparicion;
        UbicacionUltimaVez = ubicacionUltimaVez;
        DescripcionSituacion = descripcionSituacion;
        Recompensa = recompensa;
        EstadoReporte = estadoReporte;
        Fecha_Registro = fecha_Registro;
        Estatus = estatus;
    }

    // Getters y Setters existentes
    public int getReporteID() {
        return ReporteID;
    }

    public void setReporteID(int reporteID) {
        ReporteID = reporteID;
    }

    public int getR_Mascota() {
        return R_Mascota;
    }

    public void setR_Mascota(int r_Mascota) {
        R_Mascota = r_Mascota;
    }

    public int getR_Usuario() {
        return R_Usuario;
    }

    public void setR_Usuario(int r_Usuario) {
        R_Usuario = r_Usuario;
    }

    public Date getFechaDesaparicion() {
        return FechaDesaparicion;
    }

    public void setFechaDesaparicion(Date fechaDesaparicion) {
        FechaDesaparicion = fechaDesaparicion;
    }

    public String getUbicacionUltimaVez() {
        return UbicacionUltimaVez;
    }

    public void setUbicacionUltimaVez(String ubicacionUltimaVez) {
        UbicacionUltimaVez = ubicacionUltimaVez;
    }

    // ⬅️ NUEVOS getters y setters
    public String getDescripcionSituacion() {
        return DescripcionSituacion;
    }

    public void setDescripcionSituacion(String descripcionSituacion) {
        DescripcionSituacion = descripcionSituacion;
    }

    public double getRecompensa() {
        return Recompensa;
    }

    public void setRecompensa(double recompensa) {
        Recompensa = recompensa;
    }

    public String getEstadoReporte() {
        return EstadoReporte;
    }

    public void setEstadoReporte(String estadoReporte) {
        EstadoReporte = estadoReporte;
    }

    public Date getFecha_Registro() {
        return Fecha_Registro;
    }

    public void setFecha_Registro(Date fecha_Registro) {
        Fecha_Registro = fecha_Registro;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setEstatus(String estatus) {
        Estatus = estatus;
    }

    @Override
    public String toString() {
        return "ReporteDesaparicion{" +
                "ReporteID=" + ReporteID +
                ", R_Mascota=" + R_Mascota +
                ", R_Usuario=" + R_Usuario +
                ", FechaDesaparicion=" + FechaDesaparicion +
                ", UbicacionUltimaVez='" + UbicacionUltimaVez + '\'' +
                ", DescripcionSituacion='" + DescripcionSituacion + '\'' +
                ", Recompensa=" + Recompensa +
                ", EstadoReporte='" + EstadoReporte + '\'' +
                ", Fecha_Registro=" + Fecha_Registro +
                ", Estatus='" + Estatus + '\'' +
                '}';
    }
}