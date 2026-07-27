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
import java.sql.Timestamp;
import java.time.LocalTime;

/**
 *
 * @author HOME
 */
public class CitaDao {

    Conexion conexion = new Conexion();

    /**
     * Inserta una nueva cita en la base de datos.
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
     * Valida si la fecha y hora exacta ya están ocupadas para cualquier profesional.
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
     * Valida si existe una cita para un profesional específico en un horario determinado.
     */
    public boolean existeCitaEnHorario(int idProfesional, String fechaHora) {
        String sql = "SELECT COUNT(*) FROM citas WHERE id_profesional = ? AND fecha_hora_programada = ? AND estado != 'CANCELADA'";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProfesional);
            ps.setString(2, fechaHora);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar existencia de cita por profesional: " + e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene los rangos ocupados de un profesional en una fecha determinada.
     */
    public List<HorarioNegocio.RangoOcupado> obtenerRangosOcupados(int idProfesional, LocalDate fecha) {
        List<HorarioNegocio.RangoOcupado> rangos = new ArrayList<>();

        String sql = "SELECT c.fecha_hora_programada, s.duracion_minutos "
                   + "FROM citas c "
                   + "INNER JOIN servicios s ON c.id_servicio = s.id_servicio "
                   + "WHERE c.id_profesional = ? AND DATE(c.fecha_hora_programada) = ? AND c.estado != 'CANCELADA'";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idProfesional);
            ps.setDate(2, Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                while (rs.next()) {
                    String fechaHoraBD = rs.getString("fecha_hora_programada");
                    int duracion = rs.getInt("duracion_minutos");

                    LocalDateTime inicio = LocalDateTime.parse(fechaHoraBD, formatter);
                    LocalTime horaInicio = inicio.toLocalTime();
                    LocalTime horaFin = horaInicio.plusMinutes(duracion);

                    rangos.add(new HorarioNegocio.RangoOcupado(horaInicio, horaFin));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener rangos ocupados: " + e.getMessage());
        }
        return rangos;
    }

    /**
     * Obtiene los nombres de los servicios activos.
     */
    public List<String> obtenerListaServicios() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre_servicio FROM servicios WHERE activo = 1";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("nombre_servicio"));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo servicios: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene el ID del servicio según su nombre.
     */
    public int obtenerIdServicioPorNombre(String nombre) {
        String sql = "SELECT id_servicio FROM servicios WHERE nombre_servicio = ?";
        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_servicio");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo ID de servicio: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Obtiene la duración en minutos de un servicio según su nombre.
     */
    public int obtenerDuracionServicioPorNombre(String nombre) {
        String sql = "SELECT duracion_minutos FROM servicios WHERE nombre_servicio = ?";
        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("duracion_minutos");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo duración de servicio: " + e.getMessage());
        }
        return 30; // Valor por defecto
    }

    /**
     * Obtiene la lista de nombres de profesionales/peluqueros disponibles.
     */
    public List<String> obtenerListaProfesionales() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM usuarios WHERE rol IN ('PROFESIONAL', 'BARBERO', 'PELUQUERO')";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo lista de profesionales: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene el ID del profesional según su nombre.
     */
    public int obtenerIdProfesionalPorNombre(String nombre) {
        String sql = "SELECT id_usuario FROM usuarios WHERE nombre = ?";
        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo ID del profesional: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Obtiene las citas asignadas a un profesional en un rango de fechas especificado.
     */
    public List<Cita> obtenerCitasSemanaPorProfesional(int idProfesional, String fechaInicio, String fechaFin) {
        List<Cita> lista = new ArrayList<>();

        String sql = "SELECT c.id_cita, c.id_cliente, c.id_profesional, c.id_servicio, "
                   + "       c.fecha_hora_programada, c.estado, "
                   + "       CONCAT(u.nombre, ' ', u.apellido) AS cliente_nombre, "
                   + "       s.nombre_servicio "
                   + "FROM citas c "
                   + "INNER JOIN usuarios u ON c.id_cliente = u.id_usuario "
                   + "INNER JOIN servicios s ON c.id_servicio = s.id_servicio "
                   + "WHERE c.id_profesional = ? "
                   + "  AND c.fecha_hora_programada >= ? "
                   + "  AND c.fecha_hora_programada <= ? "
                   + "  AND c.estado != 'CANCELADA' "
                   + "ORDER BY c.fecha_hora_programada ASC";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProfesional);
            ps.setString(2, fechaInicio + " 00:00:00");
            ps.setString(3, fechaFin + " 23:59:59");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    cita.setIdCliente(rs.getInt("id_cliente"));
                    cita.setIdProfesional(rs.getInt("id_profesional"));
                    cita.setIdServicio(rs.getInt("id_servicio"));
                    cita.setFechaHoraProgramada(rs.getString("fecha_hora_programada"));
                    cita.setEstado(rs.getString("estado"));
                    cita.setNombreCliente(rs.getString("cliente_nombre"));
                    cita.setNombreServicio(rs.getString("nombre_servicio"));
                    lista.add(cita);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar citas de la semana: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza el estado de una cita por su ID.
     */
    public boolean actualizarEstadoCita(int idCita, String nuevoEstado) {
        String sql = "UPDATE citas SET estado = ? WHERE id_cita = ?";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de cita ID " + idCita + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el estado de una cita filtrando por profesional y coincidencia de horario.
     */
    public boolean actualizarEstadoCitaPorHorario(int idProfesional, String fechaHora, String nuevoEstado) {
        String sql = "UPDATE citas SET estado = ? WHERE id_profesional = ? AND fecha_hora_programada LIKE ?";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idProfesional);
            ps.setString(3, fechaHora + "%");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado por horario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cambia la fecha y hora programada de una cita existente.
     */
    public boolean reagendarCita(int idCita, String nuevaFechaHora) {
        String sql = "UPDATE citas SET fecha_hora_programada = ? WHERE id_cita = ?";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nuevaFechaHora);
            ps.setInt(2, idCita);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reagendar cita ID " + idCita + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene los detalles de una cita específica por profesional y hora aproximada.
     */
    public Cita obtenerCitaPorDetalles(int idProfesional, String fechaHora) {
        Cita cita = null;
        String sql = "SELECT id_cita, id_cliente, id_profesional, id_servicio, fecha_hora_programada, estado "
                   + "FROM citas WHERE id_profesional = ? AND fecha_hora_programada LIKE ? LIMIT 1";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idProfesional);
            ps.setString(2, fechaHora + "%");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    cita.setIdCliente(rs.getInt("id_cliente"));
                    cita.setIdProfesional(rs.getInt("id_profesional"));
                    cita.setIdServicio(rs.getInt("id_servicio"));
                    cita.setFechaHoraProgramada(rs.getString("fecha_hora_programada"));
                    cita.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cita por detalles: " + e.getMessage());
        }
        return cita;
    }

    /**
     * Obtiene el correo electrónico de un usuario por su ID.
     */
    public String obtenerCorreoPorUsuarioId(int idUsuario) {
        String correo = null;
        String sql = "SELECT correo FROM usuarios WHERE id_usuario = ?";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    correo = rs.getString("correo");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar correo de usuario: " + e.getMessage());
        }

        return correo;
    }

    /**
     * Método fallback/auxiliar para ID por defecto.
     */
    public int obtenerIdProfesionalPorDefecto() {
        return 5;
    }
}
