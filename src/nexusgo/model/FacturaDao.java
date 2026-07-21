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
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class FacturaDao {
    
    private final Conexion conexion = new Conexion();
    
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

            // Validar o resolver el id_caja si no se asignó en el controlador
            int idCajaValido = factura.getIdCaja();
            if (idCajaValido <= 0) {
                idCajaValido = obtenerUltimaCajaActiva(con);
            }

            // Iniciar transacción explícita
            con.setAutoCommit(false);

            // 1. Insertar Cabecera de la Factura
            psFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);

            // id_cita (Si idVenta > 0 se asigna, de lo contrario NULL)
            if (factura.getIdVenta() > 0) {
                psFactura.setInt(1, factura.getIdVenta());
            } else {
                psFactura.setNull(1, Types.INTEGER);
            }

            // id_cliente (Si idCliente > 0 se asigna, de lo contrario NULL para Cliente General)
            if (factura.getIdCliente() > 0) {
                psFactura.setInt(2, factura.getIdCliente());
            } else {
                psFactura.setNull(2, Types.INTEGER);
            }

            // id_caja (Si tenemos caja activa asignamos su ID, de lo contrario enviamos NULL)
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

            // Obtener el ID autogenerado de la factura (id_factura)
            rsKeys = psFactura.getGeneratedKeys();
            int idFacturaGenerado = 0;
            if (rsKeys.next()) {
                idFacturaGenerado = rsKeys.getInt(1);
                factura.setIdFactura(idFacturaGenerado); // Actualiza la instancia
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
                    psDetalle.addBatch(); // Procesamiento por lotes para alto rendimiento
                }
                psDetalle.executeBatch();
            }

            // Confirmar transacción en MySQL
            con.commit();
            return true;

        } catch (SQLException ex) {
            System.err.println("❌ Error SQL al guardar la factura en FacturaDao: " + ex.getMessage());
            ex.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // Deshacer cambios si algo falla
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        } finally {
            // Cierre seguro de recursos
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
     * Método auxiliar para obtener el ID de la última caja registrada.
     * Busca primero en la tabla 'caja' y de respaldo en 'cajas'.
     */
    private int obtenerUltimaCajaActiva(Connection con) {
        // Consultar la última caja aperturada en la tabla caja
        String sql = "SELECT id FROM caja ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Si la tabla se llama 'cajas' (en plural), intentamos el fallback
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
        return 0; // Retorna 0 para enviar NULL a la BD
    }
}
