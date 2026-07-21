/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 *
 * @author USUARIO
 */
public class FacturaDao {

    private final Conexion conexion = new Conexion();

    public boolean guardarFactura(Factura factura) {
        String sql = "INSERT INTO facturas (id_cita, id_cliente, id_caja, subtotal, descuento_aplicado, total, fecha_emision) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setNull(1, Types.INTEGER);
            ps.setInt(2, factura.getIdCliente());
            ps.setInt(3, factura.getIdCaja());
            ps.setDouble(4, factura.getSubtotal());
            ps.setDouble(5, factura.getDescuentoAplicado());
            ps.setDouble(6, factura.getTotal());
            ps.setTimestamp(7, new java.sql.Timestamp(factura.getFechaEmision().getTime()));

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    factura.setIdFactura(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
