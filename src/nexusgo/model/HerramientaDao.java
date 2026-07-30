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

    /**
     * Inserta una nueva herramienta en MySQL.
     */
    public int agregar(Herramientas her) {
        String sql = "INSERT INTO herramientas (id_herramienta, nombre_herramienta, estado_actual) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                System.err.println("❌ No se pudo establecer conexión con la base de datos.");
                return 0;
            }

            ps = con.prepareStatement(sql);
            ps.setInt(1, her.getIdHerramienta());
            ps.setString(2, her.getNombreHerramienta());
            ps.setString(3, her.getEstadoActual());

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error SQL al intentar registrar la herramienta: " + e.getMessage());
            e.printStackTrace();
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
                e.printStackTrace();
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

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                return lista;
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Herramientas her = new Herramientas();
                her.setIdHerramienta(rs.getInt("id_herramienta"));
                her.setNombreHerramienta(rs.getString("nombre_herramienta"));
                her.setEstadoActual(rs.getString("estado_actual"));

                String disponibilidad = null;
                try {
                    disponibilidad = rs.getString("disponibilidad");
                } catch (SQLException ignorada) {
                    // La columna 'disponibilidad' aún no existe en la BD
                }
                her.setDisponibilidad(disponibilidad != null ? disponibilidad : "ACTIVA");
                lista.add(her);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar herramientas: " + e.getMessage());
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        return lista;
    }

    /**
     * D - DELETE: Eliminar herramienta por ID.
     */
    public int eliminar(int id) {
        String sql = "DELETE FROM herramientas WHERE id_herramienta = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                return 0;
            }

            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar herramienta en DAO: " + e.getMessage());
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

    /**
     * U - UPDATE: Editar los datos de una herramienta.
     */
    public int editar(Herramientas herramienta) {
        String sql = "UPDATE herramientas SET nombre_herramienta = ?, estado_actual = ? WHERE id_herramienta = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                return 0;
            }

            ps = con.prepareStatement(sql);
            ps.setString(1, herramienta.getNombreHerramienta());
            ps.setString(2, herramienta.getEstadoActual());
            ps.setInt(3, herramienta.getIdHerramienta());

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al editar herramienta en DAO: " + e.getMessage());
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el historial de mantenimientos cruzado con la tabla herramientas.
     */
    public List<Mantenimiento> listarMantenimientosRealizados() {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id_mantenimiento, h.id_herramienta, h.nombre_herramienta, m.fecha_mantenimiento "
                + "FROM mantenimiento_herramientas m "
                + "INNER JOIN herramientas h ON m.id_herramienta = h.id_herramienta "
                + "ORDER BY m.fecha_mantenimiento DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                return lista;
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Mantenimiento m = new Mantenimiento();
                m.setIdMantenimiento(rs.getInt("id_mantenimiento"));
                m.setIdHerramienta(rs.getInt("id_herramienta"));
                m.setNombreHerramienta(rs.getString("nombre_herramienta"));
                m.setMarca("Original");
                m.setFechaHora(rs.getString("fecha_mantenimiento"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar mantenimientos: " + e.getMessage());
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        return lista;
    }

    /**
     * Actualiza el estado actual (e.g. Óptimo, Reparación) de una herramienta.
     */
    public boolean actualizarEstado(int idHerramienta, String nuevoEstado) {
        String sql = "UPDATE herramientas SET estado_actual = ? WHERE id_herramienta = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                return false;
            }

            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idHerramienta);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar el estado de la herramienta: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cambia únicamente la disponibilidad (ACTIVA / OCUPADA) de una
     * herramienta.
     */
    public boolean actualizarDisponibilidad(int idHerramienta, String disponibilidad) {
        String sql = "UPDATE herramientas SET disponibilidad = ? WHERE id_herramienta = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                return false;
            }

            ps = con.prepareStatement(sql);
            ps.setString(1, disponibilidad);
            ps.setInt(2, idHerramienta);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar la disponibilidad de la herramienta: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Guarda el checklist de herramientas seleccionadas para una cita y las
     * marca como OCUPADA. Utiliza una transacción explícita (commit/rollback).
     */
    public boolean reservarHerramientasParaCita(int idCita, List<Integer> idsHerramientas) {
        if (idsHerramientas == null || idsHerramientas.isEmpty()) {
            return true;
        }

        String sqlInsert = "INSERT IGNORE INTO detalle_cita_herramientas (id_cita, id_herramienta) VALUES (?, ?)";
        String sqlEstado = "UPDATE herramientas SET disponibilidad = 'OCUPADA' WHERE id_herramienta = ?";

        Connection con = null;
        PreparedStatement psInsert = null;
        PreparedStatement psEstado = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                System.err.println("❌ No se pudo establecer conexión con la base de datos.");
                return false;
            }

            con.setAutoCommit(false);

            psInsert = con.prepareStatement(sqlInsert);
            psEstado = con.prepareStatement(sqlEstado);

            for (Integer idHerramienta : idsHerramientas) {
                psInsert.setInt(1, idCita);
                psInsert.setInt(2, idHerramienta);
                psInsert.addBatch();

                psEstado.setInt(1, idHerramienta);
                psEstado.addBatch();
            }

            psInsert.executeBatch();
            psEstado.executeBatch();

            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al reservar herramientas para la cita " + idCita + ": " + e.getMessage());
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                if (psEstado != null) {
                    psEstado.close();
                }
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
     * Libera (vuelve a ACTIVA) todas las herramientas asociadas a una cita.
     */
    public boolean liberarHerramientasDeCita(int idCita) {
        String sql = "UPDATE herramientas h "
                + "INNER JOIN detalle_cita_herramientas d ON h.id_herramienta = d.id_herramienta "
                + "SET h.disponibilidad = 'ACTIVA' WHERE d.id_cita = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = conexion.getConection();
            if (con == null) {
                return false;
            }

            ps = con.prepareStatement(sql);
            ps.setInt(1, idCita);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al liberar herramientas de la cita " + idCita + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
