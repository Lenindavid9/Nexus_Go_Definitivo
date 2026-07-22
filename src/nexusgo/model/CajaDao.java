package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CajaDao {

    private final Conexion cn = new Conexion();

    /**
     * Abre una nueva caja para el operador indicado. Devuelve el id_caja
     * generado, o 0 si falla.
     */
    public int guardarApertura(double montoApertura, int idOperador) {
        String sql = "INSERT INTO cajas (id_operador, fecha_apertura, monto_apertura, estado_caja) "
                + "VALUES (?, NOW(), ?, 'ABIERTA')";
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idOperador);
            ps.setDouble(2, montoApertura);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Cierra la caja indicada.
     */
    public boolean guardarCierre(int idCaja, double montoCierreEfectivo, double montoCierreCalculado) {
        String sql = "UPDATE cajas SET fecha_cierre = NOW(), monto_cierre_efectivo = ?, "
                + "monto_cierre_calculado = ?, estado_caja = 'CERRADA' WHERE id_caja = ?";
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, montoCierreEfectivo);
            ps.setDouble(2, montoCierreCalculado);
            ps.setInt(3, idCaja);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Devuelve el id_caja de la caja actualmente ABIERTA (la más reciente), o 0
     * si no hay ninguna.
     */
    public int obtenerCajaAbierta() {
        String sql = "SELECT id_caja FROM cajas WHERE estado_caja = 'ABIERTA' ORDER BY id_caja DESC LIMIT 1";
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double obtenerMontoApertura(int idCaja) {
        String sql = "SELECT monto_apertura FROM cajas WHERE id_caja = ?";
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCaja);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
