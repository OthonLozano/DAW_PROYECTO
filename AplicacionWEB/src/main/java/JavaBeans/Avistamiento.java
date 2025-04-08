package JavaBeans;

import java.io.Serializable;
import java.util.Date;

public class Avistamiento implements Serializable {
    private static final long serialVersionUID = 1L;

    private int AvistamientoID;
    private int R_Reporte;
    private int R_UsuarioReportante;
    private Date Fecha_Avistamiento;
    private String Ubicacion;
    private String Descripcion;
    private String Contacto;
    private Date Fecha_Registro;
    private int R_Imagen;

    public Avistamiento() {
    }

    public int getR_Imagen() {
        return R_Imagen;
    }

    public void setR_Imagen(int r_Imagen) {
        R_Imagen = r_Imagen;
    }

    public Date getFecha_Registro() {
        return Fecha_Registro;
    }

    public void setFecha_Registro(Date fecha_Registro) {
        Fecha_Registro = fecha_Registro;
    }

    public String getContacto() {
        return Contacto;
    }

    public void setContacto(String contacto) {
        Contacto = contacto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public Date getFecha_Avistamiento() {
        return Fecha_Avistamiento;
    }

    public void setFecha_Avistamiento(Date fecha_Avistamiento) {
        Fecha_Avistamiento = fecha_Avistamiento;
    }

    public int getR_UsuarioReportante() {
        return R_UsuarioReportante;
    }

    public void setR_UsuarioReportante(int r_UsuarioReportante) {
        R_UsuarioReportante = r_UsuarioReportante;
    }

    public int getR_Reporte() {
        return R_Reporte;
    }

    public void setR_Reporte(int r_Reporte) {
        R_Reporte = r_Reporte;
    }

    public int getAvistamientoID() {
        return AvistamientoID;
    }

    public void setAvistamientoID(int avistamientoID) {
        AvistamientoID = avistamientoID;
    }
}
