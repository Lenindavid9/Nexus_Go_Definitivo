/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author USUARIO
 */
public class MantenimientoDao {

    Conexion conexion = new Conexion();

    public boolean registrarProgramacion(Mantenimiento mant) {
        String sql = """
                     INSERT INTO mantenimientos 
                     (id_herramienta, tipo_mantenimiento, fecha_programada, evidencia_notas, id_tecnico_responsable) 
                     VALUES (?, ?, ?, ?, ?)
                     """;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mant.getIdHerramienta());
            // .toUpperCase() para cumplir con el ENUM de MySQL
            ps.setString(2, mant.getTipoMantenimiento() != null ? mant.getTipoMantenimiento().toUpperCase() : "PREVENTIVO");

            // Si fechaHora o fechaProgramada es un String o Date, convertir adecuadamente
            if (mant.getFechaHora() != null) {
                ps.setString(3, mant.getFechaHora());
            } else {
                ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            }

            // Usamos observaciones que es el atributo definido en Mantenimiento.java
            ps.setString(4, mant.getObservaciones());
            ps.setInt(5, mant.getIdTecnicoResponsable());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar programación de mantenimiento: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarEjecucion(int idHerramienta, String descripcionTrabajo, String fotoDespuesPath, double horasInvertidas, String observaciones) {
        String sql = """
                     UPDATE mantenimientos 
                     SET descripcion_trabajo = ?, foto_despues = ?, horas_invertidas = ?, observaciones = ?, estado = 'REALIZADO' 
                     WHERE id_herramienta = ? AND estado = 'PENDIENTE'
                     """;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, descripcionTrabajo);
            ps.setString(2, fotoDespuesPath);
            ps.setDouble(3, horasInvertidas);
            ps.setString(4, observaciones);
            ps.setInt(5, idHerramienta);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar ejecución de mantenimiento: " + e.getMessage());
            return false;
        }
    }

}
