
package nexusgo.model;

import nexusgo.model.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CajaDao {
    Conexion cn = new Conexion();
    Connection con;

    public boolean guardarApertura(double monto) {
        con = cn.getConection();
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO caja (monto_apertura, estado) VALUES (?, 'APERTURA EXITOSA')");
            ps.setDouble(1, monto);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean guardarCierre(double montoFinal) {
        con = cn.getConection();
        try {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE caja SET monto_final=?, estado='CIERRE DE CAJA EXITOSO' WHERE id=(SELECT MAX(id) FROM caja)");
            ps.setDouble(1, montoFinal);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double obtenerMontoApertura() {
        con = cn.getConection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT monto_apertura FROM caja ORDER BY id DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}