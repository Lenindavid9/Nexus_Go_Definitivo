/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author USUARIO
 */
public class PromocionDao {
    
    private final Conexion conexion = new Conexion();

    public boolean guardarPromocion(Promocion promo) {
        String sql = "INSERT INTO promociones (id_producto, id_servicio, porcentaje_descuento, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Asignar ID Producto (o NULL)
            if (promo.getIdProducto() != null && promo.getIdProducto() > 0) {
                ps.setInt(1, promo.getIdProducto());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            // Asignar ID Servicio (o NULL)
            if (promo.getIdServicio() != null && promo.getIdServicio() > 0) {
                ps.setInt(2, promo.getIdServicio());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setDouble(3, promo.getPorcentajeDescuento());
            ps.setDate(4, new java.sql.Date(promo.getFechaInicio().getTime()));
            ps.setDate(5, new java.sql.Date(promo.getFechaFin().getTime()));
            ps.setString(6, promo.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar promoción: " + e.getMessage());
            return false;
        }
    }
}
