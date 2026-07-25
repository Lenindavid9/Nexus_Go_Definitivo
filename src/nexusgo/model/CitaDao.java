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
import java.sql.Date;
import java.util.List;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author HOME
 */
public class CitaDao {

    Conexion conexion = new Conexion();


    /**
     * Inserta la cita directamente en la BD
     */
    public boolean agendarCita(Cita cita) {
        String sql = "INSERT INTO citas (id_cliente, id_profesional, id_servicio, fecha_hora_programada, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

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
     * Valida si la fecha y hora exacta ya están ocupadas
     */
    public boolean existeCitaEnHorario(String fechaHora) {
        String sql = "SELECT COUNT(*) FROM citas WHERE fecha_hora_programada = ? AND estado != 'CANCELADA'";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

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
     * Obtiene las fechas (LocalDate) que ya tienen citas agendadas 
     * para pintarlas de rosa en LGoodDatePicker.
     */
    public List<LocalDate> obtenerFechasOcupadas() {
        List<LocalDate> fechas = new ArrayList<>();
        String sql = "SELECT DISTINCT fecha_hora_programada FROM citas WHERE estado != 'CANCELADA'";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String strFechaHora = rs.getString("fecha_hora_programada");
                if (strFechaHora != null && !strFechaHora.isEmpty()) {
                    // Si el String viene completo con hora, tomamos solo la fecha
                    LocalDate fecha = LocalDateTime.parse(strFechaHora, formatter).toLocalDate();
                    if (!fechas.contains(fecha)) {
                        fechas.add(fecha);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener fechas ocupadas: " + e.getMessage());
        }
        return fechas;
    }

    public int obtenerIdServicioPorNombre(String nombreServicio) {
        String sql = "SELECT id_servicio FROM servicios WHERE nombre_servicio = ?";
        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreServicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_servicio");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de servicio: " + e.getMessage());
        }
        return 1;
    }

    public int obtenerIdProfesionalPorDefecto() {
        String sql = "SELECT id_usuario FROM usuarios WHERE id_rol = 5 LIMIT 1";
        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar profesional: " + e.getMessage());
        }
        return 5;
    }

    public List<String> obtenerListaServicios() {
        List<String> listaServicios = new ArrayList<>();
        String sql = "SELECT nombre_servicio FROM servicios";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listaServicios.add(rs.getString("nombre_servicio"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener lista de servicios: " + e.getMessage());
        }

        return listaServicios;
    }

    public String obtenerCorreoPorUsuarioId(int idUsuario) {
        String correo = null;
        String sql = "SELECT correo FROM usuarios WHERE id_usuario = ?";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    correo = rs.getString("correo");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el correo del usuario: " + e.getMessage());
        }

        return correo;
    }
    
    
}
