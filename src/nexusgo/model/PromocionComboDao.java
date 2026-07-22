/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class PromocionComboDao {

    private final Conexion conexion = new Conexion();

    public boolean registrarComboConDetalles(PromocionCombo combo) {
        String sqlCombo = """
                          INSERT INTO promocion_combo 
                          (nombre_combo, descripcion, fecha_inicio, fecha_fin, precio_combo, ruta_imagen, estado) 
                          VALUES (?, ?, ?, ?, ?, ?, 'ACTIVA')
                          """;

        String sqlDetalleProducto = """
                                   INSERT INTO detalle_promocion_producto 
                                   (id_promocion, id_producto) 
                                   VALUES (?, ?)
                                   """;

        String sqlDetalleServicio = """
                                   INSERT INTO detalle_promocion_servicio 
                                   (id_promocion, id_servicio) 
                                   VALUES (?, ?)
                                   """;

        Connection con = null;

        try {
            con = conexion.getConection();
            con.setAutoCommit(false); // Desactivar autocommit para iniciar transacción

            int idPromocionGenerado = -1;

            // 1. Insertar el encabezado de la promoción combo
            try (PreparedStatement psCombo = con.prepareStatement(sqlCombo, Statement.RETURN_GENERATED_KEYS)) {
                psCombo.setString(1, combo.getNombreCombo());
                psCombo.setString(2, combo.getDescripcion());
                psCombo.setDate(3, new java.sql.Date(combo.getFechaInicio().getTime()));
                psCombo.setDate(4, new java.sql.Date(combo.getFechaFin().getTime()));
                psCombo.setDouble(5, combo.getPrecioCombo());
                psCombo.setString(6, combo.getRutaImagen());

                int filasAfectadas = psCombo.executeUpdate();
                if (filasAfectadas == 0) {
                    con.rollback();
                    return false;
                }

                try (ResultSet rsKeys = psCombo.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        idPromocionGenerado = rsKeys.getInt(1);
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }

            // 2. Insertar los productos asociados al combo
            if (combo.getProductos() != null && !combo.getProductos().isEmpty()) {
                try (PreparedStatement psProd = con.prepareStatement(sqlDetalleProducto)) {
                    for (Producto p : combo.getProductos()) {
                        psProd.setInt(1, idPromocionGenerado);
                        psProd.setInt(2, p.getIdProducto());
                        psProd.addBatch();
                    }
                    psProd.executeBatch();
                }
            }

            // 3. Insertar los servicios asociados al combo
            if (combo.getServicios() != null && !combo.getServicios().isEmpty()) {
                try (PreparedStatement psServ = con.prepareStatement(sqlDetalleServicio)) {
                    for (Servicios s : combo.getServicios()) {
                        psServ.setInt(1, idPromocionGenerado);
                        psServ.setInt(2, s.getIdServicio());
                        psServ.addBatch();
                    }
                    psServ.executeBatch();
                }
            }

            // Confirmar todos los cambios en la base de datos
            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar la promoción combo: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("❌ Error al realizar el rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    /**
     * R - READ: Listar combos activos que estén vigentes a la fecha actual.
     *
     * @return Lista de objetos PromocionCombo.
     */
    public List<PromocionCombo> listarCombosActivos() {
        List<PromocionCombo> lista = new ArrayList<>();
        String sql = """
                     SELECT id_promocion, nombre_combo, descripcion, fecha_inicio, fecha_fin, precio_combo, ruta_imagen 
                     FROM promocion_combo 
                     WHERE UPPER(estado) = 'ACTIVA' 
                     AND (fecha_fin IS NULL OR fecha_fin >= CURDATE())
                     """;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PromocionCombo combo = new PromocionCombo();
                combo.setIdPromocion(rs.getInt("id_promocion"));
                combo.setNombreCombo(rs.getString("nombre_combo"));
                combo.setDescripcion(rs.getString("descripcion"));
                combo.setFechaInicio(rs.getDate("fecha_inicio"));
                combo.setFechaFin(rs.getDate("fecha_fin"));
                combo.setPrecioCombo(rs.getDouble("precio_combo"));
                combo.setRutaImagen(rs.getString("ruta_imagen"));

                lista.add(combo);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar promociones combo activas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * D - DELETE: Desactivar o eliminar una promoción combo por su ID.
     *
     * @param idPromocion Identificador del combo a desactivar.
     * @return int número de filas afectadas (1 si fue exitoso, 0 si no).
     */
    public int eliminarCombo(int idPromocion) {
        String sql = "DELETE FROM promocion_combo WHERE id_promocion = ?";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar la promoción combo: " + e.getMessage());
        }
        return 0;
    }

}
