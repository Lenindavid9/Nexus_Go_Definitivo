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

    public java.util.List<Object[]> listarCitasPorCliente(int idCliente) {
        java.util.List<Object[]> listaCitas = new java.util.ArrayList<>();

        // Query que une la cita con el servicio/producto correspondiente
        String sql = "SELECT s.nombre, c.fecha_hora, s.precio "
                + "FROM citas c "
                + "INNER JOIN servicios s ON c.id_servicio = s.id_servicio "
                + "WHERE c.id_cliente = ? AND c.estado = 'Vigente' "
                + "ORDER BY c.fecha_hora ASC";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[]{
                        rs.getString("nombre"),
                        rs.getString("fecha_hora"),
                        rs.getDouble("precio")
                    };
                    listaCitas.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.listarCitasPorCliente: " + e.getMessage());
        }
        return listaCitas;
    }

    public boolean registrarCita(int idCliente, int idServicio, String fechaHora) {
        String sql = "INSERT INTO citas (id_cliente, id_servicio, fecha_hora, estado) VALUES (?, ?, ?, 'Vigente')";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setInt(2, idServicio);
            ps.setString(3, fechaHora);

            // executeUpdate devuelve el número de filas afectadas. Si es > 0, guardó correctamente.
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.registrarCita: " + e.getMessage());
            return false;
        }

    }
    
    public boolean actualizarContrasena(String correo, String nuevaContrasena) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";
        
        // Usamos Try-with-resources para asegurar el cierre automático de la conexión y el statement
        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nuevaContrasena);
            ps.setString(2, correo);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // Si modificó 1 o más filas, devuelve true
            
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDao.actualizarContrasena: " + e.getMessage());
            return false;
        }
    }
    
    
}
