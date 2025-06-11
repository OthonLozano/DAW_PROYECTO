package Modelo.JavaBeans;

import java.io.Serializable;

public class AvistamientoConRelaciones implements Serializable {
    private static final long serialVersionUID = 1L;

    private Avistamiento avistamiento;
    private Usuarios usuario;
    private ImagenMascota imagen; // Imagen del avistamiento (puede ser null)

    // Constructores
    public AvistamientoConRelaciones() {}

    public AvistamientoConRelaciones(Avistamiento avistamiento, Usuarios usuario, ImagenMascota imagen) {
        this.avistamiento = avistamiento;
        this.usuario = usuario;
        this.imagen = imagen;
    }

    // Getters y Setters
    public Avistamiento getAvistamiento() {
        return avistamiento;
    }

    public void setAvistamiento(Avistamiento avistamiento) {
        this.avistamiento = avistamiento;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public ImagenMascota getImagen() {
        return imagen;
    }

    public void setImagen(ImagenMascota imagen) {
        this.imagen = imagen;
    }

    // Métodos de conveniencia
    public boolean tieneImagen() {
        return imagen != null && imagen.getURL_Imagen() != null && !imagen.getURL_Imagen().trim().isEmpty();
    }

    public String getUrlImagen() {
        return tieneImagen() ? imagen.getURL_Imagen() : null;
    }

    public String getNombreCompleto() {
        if (usuario != null) {
            StringBuilder nombre = new StringBuilder();
            if (usuario.getNombre() != null) nombre.append(usuario.getNombre());
            if (usuario.getApellidoPat() != null) nombre.append(" ").append(usuario.getApellidoPat());
            if (usuario.getApellidoMat() != null) nombre.append(" ").append(usuario.getApellidoMat());
            return nombre.toString().trim();
        }
        return "Usuario desconocido";
    }

    public boolean esAvistamientoActivo() {
        return avistamiento != null && "Alta".equalsIgnoreCase(avistamiento.getEstatus());
    }

    @Override
    public String toString() {
        return "AvistamientoConRelaciones{" +
                "avistamiento=" + (avistamiento != null ? avistamiento.getAvistamientoID() : "null") +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "null") +
                ", tieneImagen=" + tieneImagen() +
                '}';
    }
}
