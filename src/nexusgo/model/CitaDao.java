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
 * @author HOME
 */
public class CitaDao{
   
    Conexion conexion = new Conexion();

    /**
     * Inserta la cita directamente en la BD nexus_go_prueba
     */
    public boolean agendarCita(Cita cita) {
        String sql = "INSERT INTO citas (id_cliente, id_profesional, id_servicio, fecha_hora_programada, estado) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cita.getIdCliente());
            ps.setInt(2, cita.getIdProfesional());
            ps.setInt(3, cita.getIdServicio());
            ps.setString(4, cita.getFechaHoraProgramada());
            ps.setString(5, cita.getEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al agendar cita: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida que no haya colisión de horarios
     */
    public boolean existeCitaEnHorario(String fechaHora) {
        String sql = "SELECT COUNT(*) FROM citas WHERE fecha_hora_programada = ? AND estado != 'CANCELADA'";

        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fechaHora);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar disponibilidad: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca la ID del servicio seleccionado
     */
    public int obtenerIdServicioPorNombre(String nombreServicio) {
        String sql = "SELECT id_servicio FROM servicios WHERE nombre_servicio = ?";
        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreServicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_servicio");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de servicio: " + e.getMessage());
        }
        return 1; // ID de respaldo por si no coincide exactamente el string
    }

    /**
     * Obtiene profesional disponible (Rol 5 = Peluquero/Profesional)
     */
    public int obtenerIdProfesionalPorDefecto() {
        String sql = "SELECT id_usuario FROM usuarios WHERE id_rol = 5 LIMIT 1";
        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar profesional: " + e.getMessage());
        }
        return 5; // Respaldo
    }
}
