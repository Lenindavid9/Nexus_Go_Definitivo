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

        // Al declararse dentro del try, los recursos se cierran solos automáticamente
        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mant.getIdHerramienta());
            // .toUpperCase() asegura cumplir con el ENUM ('PREVENTIVO' o 'CORRECTIVO') de la BD
            ps.setString(2, mant.getTipoMantenimiento().toUpperCase()); 
            // Conversión de java.util.Date a java.sql.Date
            ps.setDate(3, new java.sql.Date(mant.getFechaProgramada().getTime()));
            ps.setString(4, mant.getEvidenciaNotas());
            ps.setInt(5, mant.getIdTecnicoResponsable());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se insertó el registro sin problemas

        } catch (SQLException e) {
            System.out.println("Error al registrar programación de mantenimiento: " + e.getMessage());
            return false;
        }
    }
    
    
    
}
