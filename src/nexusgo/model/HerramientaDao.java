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
public class HerramientaDao {

    private final Conexion conexion = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    /**
     * Inserta una nueva herramienta en MySQL.
     */
    public int agregar(Herramientas her) {
        String sql = "INSERT INTO herramientas (id_herramienta, nombre_herramienta, estado_actual) VALUES (?, ?, ?)";

        try {
            con = conexion.getConection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, her.getIdHerramienta());
            ps.setString(2, her.getNombreHerramienta());
            ps.setString(3, her.getEstadoActual());

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error SQL al intentar registrar la herramienta: " + e.getMessage());
            return 0;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexiones: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene todos los registros para pintarlos en el JTable del panel de
     * inventario.
     */
    public List<Herramientas> listar() {
        List<Herramientas> lista = new ArrayList<>();
        String sql = "SELECT * FROM herramientas";

        try {
            con = conexion.getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Herramientas her = new Herramientas();
                her.setIdHerramienta(rs.getInt("id_herramienta"));
                her.setNombreHerramienta(rs.getString("nombre_herramienta"));
                her.setEstadoActual(rs.getString("estado_actual"));
                lista.add(her);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar herramientas: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar flujos: " + e.getMessage());
            }
        }
        return lista;
    }

    // D - DELETE: ELIMINAR HERRAMIENTA
    public int eliminar(int id) {
        String sql = "DELETE FROM herramientas WHERE id_herramienta = ?";

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar herramienta en DAO: " + e.getMessage());
        }
        return 0;
    }

    public int editar(Herramientas herramienta) {
        String sql = """
                     UPDATE herramientas 
                     SET nombre_herramienta = ?, estado_actual = ? 
                     WHERE id_herramienta = ?
                     """;

        try (Connection con = conexion.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, herramienta.getNombreHerramienta());
            ps.setString(2, herramienta.getEstadoActual());
            ps.setInt(3, herramienta.getIdHerramienta());

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al editar herramienta en DAO: " + e.getMessage());
        }
        return 0;
    }

}
