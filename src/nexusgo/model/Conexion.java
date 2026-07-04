/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class Conexion {

    Connection con;
    String url = "jdbc:mysql://localhost:3306/nexus_go_prueba?useSSL=false&serverTimezone=UTC";
    String user = "root";
    String pass = "";

    /**
     * Establece y retorna la conexión activa con la base de datos.
     */
    public Connection getConection() {
        try {
            // 1. Cargar el Driver de MySQL en memoria
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Intentar la conexión con el servidor local
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexion exitosa con nexusGodb");

        } catch (Exception e) {
            // Se ordenaron los parámetros correctamente para evitar errores en Swing
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar a la base de datos.\nDetalle: " + e.getMessage(),
                    "Error de Conexión - Base de Datos Apagada",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return con;
    }

}
