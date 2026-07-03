/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author USUARIO
 */
public class UsuarioDao {

    Conexion conexion = new Conexion();

    public Usuario autenticarUsuario(String identificacion, String contrasena) {
        // Usa 'numero_identificacion' y conecta con la tabla roles
        String sql = """
                     SELECT u.nombre, r.nombre_rol AS rol 
                     FROM usuarios u 
                     INNER JOIN roles r ON u.id_rol = r.id_rol 
                     WHERE u.numero_identificacion = ? AND u.contrasena = ?
                     """;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Pasamos los parámetros limpios que vienen de la interfaz visual
            ps.setString(1, identificacion);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario user = new Usuario();

                    // Seteamos la identificación que usó para entrar
                    user.setIdentificacion(identificacion);

                    // Extraemos los datos reales devueltos por la base de datos
                    user.setNombre(rs.getString("nombre"));

                    // Capturamos el alias "rol" generado por el INNER JOIN (Ej: 'Operario', 'Supervisor')
                    user.setRol(rs.getString("rol"));

                    return user; // Retorna el usuario completamente armado y autenticado
                }
            }
        } catch (SQLException e) {
            System.err.println("Error crítico en la autenticación (UsuarioDao): " + e.getMessage());
        }

        return null; // Retorna null si no hubo coincidencia de credenciales o hubo un error
    }

    public int registrar(Usuario usuario) {
        // 1. Ajustamos la consulta para usar id_rol en lugar de rol
        String sql = "INSERT INTO usuarios (nombre, apellido, numero_identificacion, correo, contrasena, id_rol) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // 2. Mapeamos los datos exactamente como están en tu script SQL
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getIdentificacion());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getContrasena());

            // 3. OBLIGATORIO: Asignamos el ID numérico del rol Cliente (ej: 1)
            // Nota: Cambia este 1 por el id_rol real de 'Cliente' en tu tabla roles
            ps.setInt(6, 1);

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error crítico en UsuarioDao.registrar: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }

    }
    public Usuario buscarUsuarioPorIdentificacion(String identificacion) {
    String sql = "SELECT * FROM usuarios WHERE identificacion = ?";
    Usuario usuario = null;

    // Usamos Try-with-resources para cerrar conexiones automáticamente sin fugas de memoria
    try (Connection con = conexion.getConection(); // Reemplaza por tu método exacto de conexión
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, identificacion);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdentificacion(rs.getString("identificacion"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setRol(rs.getString("rol"));
                // Si tienes columna correo en tu BD, la mapeas aquí:
                // usuario.setCorreo(rs.getString("correo"));
            }
        }
    } catch (SQLException e) {
        System.out.println("Error en DAO al buscar identificación: " + e.getMessage());
    }
    return usuario;
}

}
