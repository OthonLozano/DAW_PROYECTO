package Modelo.JavaBeans;

import java.io.Serializable;

public class ComentariosConRelaciones implements Serializable {
    private static final long serialVersionUID = 1L;

    private Comentarios comentario;
    private Usuarios usuario;

    // Constructores
    public ComentariosConRelaciones() {}

    public ComentariosConRelaciones(Comentarios comentario, Usuarios usuario) {
        this.comentario = comentario;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Comentarios getComentario() {
        return comentario;
    }

    public void setComentario(Comentarios comentario) {
        this.comentario = comentario;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    // Métodos de conveniencia
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

    public boolean esComentarioActivo() {
        return comentario != null && "Alta".equalsIgnoreCase(comentario.getEstatus());
    }

    @Override
    public String toString() {
        return "ComentarioConRelaciones{" +
                "comentario=" + (comentario != null ? comentario.getComentarioID() : "null") +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "null") +
                '}';
    }
}
