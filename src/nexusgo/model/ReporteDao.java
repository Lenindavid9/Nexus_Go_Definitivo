package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteDao {
    
    //este es el metoodo que va obtener el reporte de un mes y año en especifico

    public ReporteFinanciero obtenerReporte(int mes, int anio) {
        //Creamos este objeto donde se van a guardar los resultados 
        ReporteFinanciero reporte = new ReporteFinanciero();
        Conexion conexion = new Conexion();

        try (Connection conn = conexion.getConection()) {

            // 1. Suma servicios/productos vendidos ( la primera consulta
            PreparedStatement psServicios = conn.prepareStatement(
                    "SELECT SUM(dfp.cantidad * dfp.precio_unitario_historico) AS suma_servicios "
                    + "FROM detalle_factura_productos dfp "
                    + "JOIN facturas f ON dfp.id_factura = f.id_factura "
                    + "WHERE MONTH(f.fecha_emision) = ? AND YEAR(f.fecha_emision) = ?"
            );

            psServicios.setInt(1, mes);
            psServicios.setInt(2, anio);
            ResultSet rsServicios = psServicios.executeQuery();
            if (rsServicios.next()) {
                reporte.setSumaServicios(rsServicios.getDouble("suma_servicios"));
            }

            // 2. Suma promociones/descuentos aplicados
            PreparedStatement psPromos = conn.prepareStatement(
                "SELECT SUM(f.descuento_aplicado) AS suma_promociones " +
                "FROM facturas f " +
                "WHERE MONTH(f.fecha_emision) = ? AND YEAR(f.fecha_emision) = ?"
            );
            psPromos.setInt(1, mes);
            psPromos.setInt(2, anio);
            ResultSet rsPromos = psPromos.executeQuery();
            if (rsPromos.next()) {
                reporte.setSumaPromociones(rsPromos.getDouble("suma_promociones"));
            }

            // 3. Total neto
            PreparedStatement psTotal = conn.prepareStatement(
                "SELECT SUM(f.total) AS total_neto " +
                "FROM facturas f " +
                "WHERE MONTH(f.fecha_emision) = ? AND YEAR(f.fecha_emision) = ?"
            );
            psTotal.setInt(1, mes);
            psTotal.setInt(2, anio);
            ResultSet rsTotal = psTotal.executeQuery();
            if (rsTotal.next()) {
                reporte.setTotalNeto(rsTotal.getDouble("total_neto"));
            }

            // 4. Servicio más vendido del mes
            PreparedStatement psTop = conn.prepareStatement(
                    "SELECT s.nombre_servicio, COUNT(*) AS veces "
                    + "FROM detalle_factura_productos dfp "
                    + "JOIN servicios s ON dfp.id_producto = s.id_servicio "
                    + "JOIN facturas f ON dfp.id_factura = f.id_factura "
                    + "WHERE MONTH(f.fecha_emision) = ? AND YEAR(f.fecha_emision) = ? "
                    + "GROUP BY s.nombre_servicio ORDER BY veces DESC LIMIT 1"
            );

            psTop.setInt(1, mes);
            psTop.setInt(2, anio);
            ResultSet rsTop = psTop.executeQuery();
            if (rsTop.next()) {
                reporte.setServicioMes(rsTop.getString("nombre_servicio"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reporte;
    }
}
