/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class UsuarioDao {

    private final Conexion conexion = new Conexion();

    // AUTENTICAR USUARIO (LOGIN)
    public Usuario autenticarUsuario(int identificacion, String contrasena) {
        String sql = """
                     SELECT u.id_usuario, u.nombre, r.nombre_rol AS rol 
                     FROM usuarios u 
                     INNER JOIN roles r ON u.id_rol = r.id_rol 
                     WHERE u.numero_identificacion = ? AND u.contrasena = ?
                     """;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, identificacion);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario user = new Usuario();
                    user.setIdUsuario(rs.getInt("id_usuario"));
                    user.setIdentificacion(identificacion);
                    user.setNombre(rs.getString("nombre"));
                    user.setRol(rs.getString("rol"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error crítico en la autenticación (UsuarioDao): " + e.getMessage());
        }
        return null;
    }

    // REGISTRAR NUEVO USUARIO
    public int registrar(Usuario usuario) {
        String sql = """
                     INSERT INTO usuarios 
                     (nombre, apellido, tipo_documento, numero_identificacion, correo, contrasena, id_rol) 
                     VALUES (?, ?, ?, ?, ?, ?, ?)
                     """;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getTipoDocumento());
            ps.setInt(4, usuario.getIdentificacion());
            ps.setString(5, usuario.getCorreo());
            ps.setString(6, usuario.getContrasena());
            ps.setInt(7, 1); // Asigna por defecto el id_rol de 'Cliente'

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error crítico en UsuarioDao.registrar: " + e.getMessage());
            return 0;
        }
    }

    // BUSCAR USUARIO POR NUMERO DE IDENTIFICACION
    public Usuario buscarUsuarioPorIdentificacion(String identificacion) {
        String sql = """
                     SELECT u.*, r.nombre_rol AS rol 
                     FROM usuarios u 
                     INNER JOIN roles r ON u.id_rol = r.id_rol 
                     WHERE u.numero_identificacion = ?
                     """;

        Usuario usuario = null;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, identificacion.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setIdentificacion(rs.getInt("numero_identificacion"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setCorreo(rs.getString("correo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDao al buscar por identificación: " + e.getMessage());
        }
        return usuario;
    }

    
    // LISTAR USUARIOS COMPLETO PARA JTABLE
    public List<Usuario> listarUsuarios() {
    List<Usuario> lista = new ArrayList<>();
    String sql = "SELECT u.numero_identificacion, u.nombre, u.apellido, "
               + "COALESCE(r.nombre_rol, 'Sin Rol') AS rol, u.correo "
               + "FROM usuarios u "
               + "LEFT JOIN roles r ON u.id_rol = r.id_rol";

    try (Connection con = new Conexion().getConection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Usuario u = new Usuario();
            // ¡OJO! Usa los nombres de columna EXACTOS de tu BD:
            u.setIdentificacion(rs.getInt("numero_identificacion"));
            u.setNombre(rs.getString("nombre"));
            u.setApellido(rs.getString("apellido"));
            u.setRol(rs.getString("rol"));
            u.setCorreo(rs.getString("correo"));

            lista.add(u);
        }
    } catch (SQLException e) {
        System.err.println("❌ Error SQL: " + e.getMessage());
    }
    return lista;
}

    // ACTUALIZAR ROL DEL USUARIO (Subconsulta para obtener id_rol desde el nombre_rol)
    public boolean actualizarRol(String numeroIdentificacion, String nuevoRol) {
    String sql = "UPDATE usuarios "
               + "SET id_rol = (SELECT id_rol FROM roles WHERE nombre_rol = ?) "
               + "WHERE numero_identificacion = ?";

    try (Connection con = new Conexion().getConection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nuevoRol);
        ps.setString(2, numeroIdentificacion);

        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0;

    } catch (SQLException e) {
        System.err.println("❌ Error al actualizar rol: " + e.getMessage());
        return false;
    }
}

    // REGISTRAR CITA NUEVA
    public boolean registrarCita(int idCliente, int idServicio, String fechahora) {
        String sql = "INSERT INTO citas (id_cliente, id_servicio, fecha_hora_programada, estado) VALUES (?, ?, ?, 'Vigente')";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setInt(2, idServicio);
            ps.setString(3, fechahora);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDao.registrarCita: " + e.getMessage());
            return false;
        }
    }

    // ACTUALIZAR CONTRASEÑA POR CORREO
    public boolean actualizarContrasena(String correo, String nuevaContrasena) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaContrasena);
            ps.setString(2, correo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDao.actualizarContrasena: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listarPromociones() {
        List<Producto> lista = new ArrayList<>();
        String sql = """
                 SELECT p.* 
                 FROM productos p
                 INNER JOIN promociones pr ON p.id_producto = pr.id_producto
                 WHERE pr.estado = 'ACTIVA'
                 """;
        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Mapear campos de Producto...
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar promociones desde ProductoDao: " + e.getMessage());
        }
        return lista;
    }

    public Usuario obtenerPorId(int idUsuario) {
        String sql = """
                     SELECT u.*, r.nombre_rol AS rol 
                     FROM usuarios u 
                     INNER JOIN roles r ON u.id_rol = r.id_rol 
                     WHERE u.id_usuario = ?
                     """;

        Usuario usuario = null;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setIdentificacion(rs.getInt("numero_identificacion"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setTipoDocumento(rs.getString("tipo_documento"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setRol(rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDao.obtenerPorId: " + e.getMessage());
        }
        return usuario;
    }

    public List<Object[]> listarCitasPorCliente(int idCliente) {
        List<Object[]> lista = new ArrayList<>();

        // Ajusta los nombres de tablas/columnas según tu DB de Nexus GO
        String sql = "SELECT s.nombre_servicio, c.fecha_hora_programada, s.precio "
                + "FROM citas c "
                + "JOIN servicios s ON c.id_servicio = s.id_servicio "
                + "WHERE c.id_cliente = ?";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[]{
                        rs.getString("nombre_servicio"),
                        rs.getString("fecha_hora_programada"),
                        "$ " + String.format("%.2f", rs.getDouble("precio"))
                    };
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar citas: " + e.getMessage());
        }
        return lista;
    }
}
