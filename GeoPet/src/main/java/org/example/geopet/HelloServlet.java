package org.example.geopet;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import Connection.Conexion;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>Prueba de Conexión a la Base de Datos</h1>");

        // Crear una instancia de la conexión
        Conexion conexionDB = new Conexion();
        Connection conn = conexionDB.getConnection();

        // Comprobar si la conexión es exitosa
        if (conn != null) {
            response.getWriter().println("<p>Conexión establecida correctamente.</p>");
            //conexionDB.closeConnection(conn);
        } else {
            response.getWriter().println("<p>Error al conectar a la base de datos.</p>");
        }
    }
}
