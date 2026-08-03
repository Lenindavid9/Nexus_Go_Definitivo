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
public class ServicioDao {

    private final Conexion conexion = new Conexion();

    public boolean registrarServicio(Servicios servicio) {
        String sql = "INSERT INTO servicios (nombre_servicio, descripcion, duracion_minutos, precio, activo, url_imagen) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, servicio.getNombreServicio());
            ps.setString(2, servicio.getDescripcion());
            ps.setInt(3, servicio.getDuracionMinutos());
            ps.setDouble(4, servicio.getPrecio());
            ps.setBoolean(5, servicio.isActivo());
            ps.setString(6, servicio.getUrlImagen());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar el servicio: " + e.getMessage());
            return false;
        }
    }

    public List<Servicios> listarServicios() {
        List<Servicios> lista = new ArrayList<>();
        String sql = "SELECT id_servicio, nombre_servicio, descripcion, duracion_minutos, precio, activo, url_imagen FROM servicios ORDER BY id_servicio DESC";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Servicios s = new Servicios();
                s.setIdServicio(rs.getInt("id_servicio"));
                s.setNombreServicio(rs.getString("nombre_servicio"));
                s.setDescripcion(rs.getString("descripcion"));
                s.setDuracionMinutos(rs.getInt("duracion_minutos"));
                s.setPrecio(rs.getDouble("precio"));
                s.setActivo(rs.getBoolean("activo"));
                s.setUrlImagen(rs.getString("url_imagen"));

                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los servicios: " + e.getMessage());
        }
        return lista;
    }

    public List<Servicios> listarServiciosActivos() {
        List<Servicios> lista = new ArrayList<>();
        String sql = "SELECT id_servicio, nombre_servicio, descripcion, duracion_minutos, precio, activo, url_imagen FROM servicios WHERE activo = 1 ORDER BY nombre_servicio ASC";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Servicios s = new Servicios();
                s.setIdServicio(rs.getInt("id_servicio"));
                s.setNombreServicio(rs.getString("nombre_servicio"));
                s.setDescripcion(rs.getString("descripcion"));
                s.setDuracionMinutos(rs.getInt("duracion_minutos"));
                s.setPrecio(rs.getDouble("precio"));
                s.setActivo(rs.getBoolean("activo"));
                s.setUrlImagen(rs.getString("url_imagen"));

                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar servicios activos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarServicio(Servicios servicio) {
        String sql = "UPDATE servicios SET nombre_servicio = ?, descripcion = ?, duracion_minutos = ?, precio = ?, activo = ?, url_imagen = ? WHERE id_servicio = ?";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, servicio.getNombreServicio());
            ps.setString(2, servicio.getDescripcion());
            ps.setInt(3, servicio.getDuracionMinutos());
            ps.setDouble(4, servicio.getPrecio());
            ps.setBoolean(5, servicio.isActivo());
            ps.setString(6, servicio.getUrlImagen());
            ps.setInt(7, servicio.getIdServicio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el servicio: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoServicio(int idServicio, boolean activo) {
        String sql = "UPDATE servicios SET activo = ? WHERE id_servicio = ?";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setInt(2, idServicio);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar el estado del servicio: " + e.getMessage());
            return false;
        }
    }

    public Servicios obtenerServicioPorId(int idServicio) {
        String sql = "SELECT id_servicio, nombre_servicio, descripcion, duracion_minutos, precio, activo, url_imagen FROM servicios WHERE id_servicio = ?";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idServicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Servicios s = new Servicios();
                    s.setIdServicio(rs.getInt("id_servicio"));
                    s.setNombreServicio(rs.getString("nombre_servicio"));
                    s.setDescripcion(rs.getString("descripcion"));
                    s.setDuracionMinutos(rs.getInt("duracion_minutos"));
                    s.setPrecio(rs.getDouble("precio"));
                    s.setActivo(rs.getBoolean("activo"));
                    s.setUrlImagen(rs.getString("url_imagen"));
                    return s;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener servicio por ID: " + e.getMessage());
        }
        return null;
    }

}
