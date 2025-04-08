package JavaBeans;
import java.io.Serializable;
import java.util.Date;

public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;

    private int UsuarioID;
    private String Nombre;
    private String ApellidoPat;
    private String ApellidoMat;
    private String Email;
    private String Contrasenia;
    private String Telefono;
    private String Direccion;
    private String Ciudad;
    private Date Fecha_Registro;
    private Date Ultimo_Accesso;

    public Usuarios() {
    }

    public int getUsuarioID() {
        return UsuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        UsuarioID = usuarioID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidoPat() {
        return ApellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        ApellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return ApellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        ApellidoMat = apellidoMat;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContrasenia() {
        return Contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        Contrasenia = contrasenia;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public Date getFecha_Registro() {
        return Fecha_Registro;
    }

    public void setFecha_Registro(Date fecha_Registro) {
        Fecha_Registro = fecha_Registro;
    }

    public Date getUltimo_Accesso() {
        return Ultimo_Accesso;
    }

    public void setUltimo_Accesso(Date ultimo_Accesso) {
        Ultimo_Accesso = ultimo_Accesso;
    }
}
