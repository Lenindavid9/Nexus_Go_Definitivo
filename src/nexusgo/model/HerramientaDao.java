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
            cerrarRecursos();
        }
    }

    /**
     * Obtiene todos los registros para pintarlos en el JTable del panel de inventario.
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
            cerrarRecursos();
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

    // U - UPDATE: EDITAR HERRAMIENTA
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

    /**
     * Obtiene el historial de mantenimientos cruzado con la tabla herramientas.
     */
    public List<Mantenimiento> listarMantenimientosRealizados() {
        List<Mantenimiento> lista = new ArrayList<>();
        
        // Corregido: Usamos 'nombre_herramienta' que es la columna real de tu tabla herramientas
        String sql = """
                     SELECT m.id_mantenimiento, h.id_herramienta, h.nombre_herramienta, m.fecha_mantenimiento 
                     FROM mantenimiento_herramientas m 
                     INNER JOIN herramientas h ON m.id_herramienta = h.id_herramienta 
                     ORDER BY m.fecha_mantenimiento DESC
                     """;

        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mantenimiento m = new Mantenimiento();
                m.setIdMantenimiento(rs.getInt("id_mantenimiento"));
                m.setIdHerramienta(rs.getInt("id_herramienta"));
                m.setNombreHerramienta(rs.getString("nombre_herramienta"));
                m.setMarca("Original"); // Como tu tabla 'herramientas' no tiene columna 'marca', asignamos un valor por defecto legible
                m.setFechaHora(rs.getString("fecha_mantenimiento"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mantenimientos: " + e.getMessage());
        }
        return lista;
    }

    private void cerrarRecursos() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar flujos: " + e.getMessage());
        }
    }

}
