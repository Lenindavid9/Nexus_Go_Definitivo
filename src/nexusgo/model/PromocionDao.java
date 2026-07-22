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
public class PromocionDao {
    
    private final Conexion conexion = new Conexion();

    public boolean guardarPromocion(Promocion promo) {
        String sql = "INSERT INTO promociones (id_producto, porcentaje_descuento, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, promo.getIdProducto());
            ps.setDouble(2, promo.getPorcentajeDescuento());
            ps.setDate(3, new java.sql.Date(promo.getFechaInicio().getTime()));
            ps.setDate(4, new java.sql.Date(promo.getFechaFin().getTime()));
            ps.setString(5, promo.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar promoción: " + e.getMessage());
            return false;
        }
    }
    
}
