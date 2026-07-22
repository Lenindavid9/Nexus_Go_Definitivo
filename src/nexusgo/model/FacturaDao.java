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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class FacturaDao {
    
    private final Conexion conexion = new Conexion();

    /**
     * Guarda una nueva factura en la base de datos junto con el detalle de sus productos.
     * Utiliza una transacción explícita (commit/rollback) para garantizar la consistencia.
     */
    public boolean guardarFactura(Factura factura) {
        String sqlFactura = "INSERT INTO facturas (id_cita, id_cliente, id_caja, subtotal, descuento_aplicado, total) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_factura_productos (id_factura, id_producto, cantidad, precio_unitario_historico) VALUES (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement psFactura = null;
        PreparedStatement psDetalle = null;
        ResultSet rsKeys = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                System.err.println("❌ No se pudo establecer conexión con la base de datos.");
                return false;
            }

            int idCajaValido = factura.getIdCaja();
            if (idCajaValido <= 0) {
                idCajaValido = obtenerUltimaCajaActiva(con);
            }

            con.setAutoCommit(false);

            // 1. Insertar Cabecera de la Factura
            psFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);

            if (factura.getIdVenta() > 0) {
                psFactura.setInt(1, factura.getIdVenta());
            } else {
                psFactura.setNull(1, Types.INTEGER);
            }

            if (factura.getIdCliente() > 0) {
                psFactura.setInt(2, factura.getIdCliente());
            } else {
                psFactura.setNull(2, Types.INTEGER);
            }

            if (idCajaValido > 0) {
                psFactura.setInt(3, idCajaValido);
            } else {
                psFactura.setNull(3, Types.INTEGER);
            }

            psFactura.setDouble(4, factura.getSubtotal());
            psFactura.setDouble(5, factura.getDescuentoAplicado());
            psFactura.setDouble(6, factura.getTotal());

            int filasAfectadas = psFactura.executeUpdate();

            if (filasAfectadas == 0) {
                con.rollback();
                return false;
            }

            rsKeys = psFactura.getGeneratedKeys();
            int idFacturaGenerado = 0;
            if (rsKeys.next()) {
                idFacturaGenerado = rsKeys.getInt(1);
                factura.setIdFactura(idFacturaGenerado);
            } else {
                con.rollback();
                return false;
            }

            // 2. Insertar Detalle de Productos
            psDetalle = con.prepareStatement(sqlDetalle);
            List<DetalleCarrito> detalles = factura.getDetalles();

            if (detalles != null && !detalles.isEmpty()) {
                for (DetalleCarrito item : detalles) {
                    psDetalle.setInt(1, idFacturaGenerado);
                    psDetalle.setInt(2, item.getIdProducto());
                    psDetalle.setInt(3, item.getCantidad());
                    psDetalle.setDouble(4, item.getPrecioUnitario());
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }

            con.commit();
            return true;

        } catch (SQLException ex) {
            System.err.println("❌ Error SQL al guardar la factura en FacturaDao: " + ex.getMessage());
            ex.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psFactura != null) psFactura.close();
                if (psDetalle != null) psDetalle.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el listado de facturas asociadas a un cliente para la vista Historial de Pagos.
     */
    public List<Factura> obtenerFacturasPorCliente(int idCliente) {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT f.id_factura, f.id_cita, f.id_cliente, f.id_caja, " +
                    "f.fecha_emision, f.subtotal, f.descuento_aplicado, f.total, " +
                    "s.nombre_servicio " +
                    "FROM facturas f " +
                    "LEFT JOIN citas c ON f.id_cita = c.id_cita " +
                    "LEFT JOIN servicios s ON c.id_servicio = s.id_servicio " +
                    "WHERE f.id_cliente = ? " +
                    "ORDER BY f.fecha_emision DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = conexion.getConection();
            if (con == null) return lista;

            ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            rs = ps.executeQuery();

            while (rs.next()) {
                Factura f = new Factura();
                f.setIdFactura(rs.getInt("id_factura"));
                f.setIdVenta(rs.getInt("id_cita"));
                f.setIdCliente(rs.getInt("id_cliente"));
                f.setIdCaja(rs.getInt("id_caja"));
                
                // Mapear fecha SQL a java.util.Date
                f.setFechaEmision(rs.getTimestamp("fecha_emision"));
                
                f.setSubtotal(rs.getDouble("subtotal"));
                f.setDescuentoAplicado(rs.getDouble("descuento_aplicado"));
                f.setTotal(rs.getDouble("total"));

                // Cargar detalles de productos
                f.setDetalles(obtenerDetallesProductos(f.getIdFactura(), con));

                String nombreServicio = rs.getString("nombre_servicio");
                if (nombreServicio != null && !nombreServicio.isEmpty()) {
                    f.setNombreServicio(nombreServicio);
                }

                lista.add(f);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener facturas del cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    /**
     * Método auxiliar para consultar los productos de una factura.
     */
    private List<DetalleCarrito> obtenerDetallesProductos(int idFactura, Connection con) {
        List<DetalleCarrito> detalles = new ArrayList<>();
        String sql = "SELECT df.id_producto, p.nombre_producto, df.cantidad, df.precio_unitario_historico " +
                     "FROM detalle_factura_productos df " +
                     "INNER JOIN productos p ON df.id_producto = p.id_producto " +
                     "WHERE df.id_factura = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleCarrito item = new DetalleCarrito();
                    item.setIdProducto(rs.getInt("id_producto"));
                    item.setNombreProducto(rs.getString("nombre_producto"));
                    item.setCantidad(rs.getInt("cantidad"));
                    item.setPrecioUnitario(rs.getDouble("precio_unitario_historico"));
                    item.setSubtotal(item.getCantidad() * item.getPrecioUnitario());
                    detalles.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error al obtener detalles de la factura #" + idFactura + ": " + e.getMessage());
        }
        return detalles;
    }

    /**
     * Método auxiliar para obtener el ID de la última caja registrada.
     */
    private int obtenerUltimaCajaActiva(Connection con) {
        String sql = "SELECT id FROM caja ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            String sqlFallback = "SELECT id_caja FROM cajas ORDER BY id_caja DESC LIMIT 1";
            try (PreparedStatement psF = con.prepareStatement(sqlFallback);
                 ResultSet rsF = psF.executeQuery()) {
                if (rsF.next()) {
                    return rsF.getInt(1);
                }
            } catch (SQLException ex) {
                System.err.println("⚠️ No se pudo obtener el ID de caja automático: " + ex.getMessage());
            }
        }
        return 0;
    }
}
