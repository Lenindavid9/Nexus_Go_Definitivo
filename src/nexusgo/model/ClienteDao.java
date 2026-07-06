package nexusgo.model;

import nexusgo.model.Cliente;
import nexusgo.model.Conexion;
import nexusgo.model.Crud;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteDao implements Crud<Cliente> {

    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try {
            con = conectar.getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id_cliente"));
                c.setIdUsuario(rs.getInt("id_usuario"));
                c.setNombre(rs.getString("nombre_cliente"));
                c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                c.setCorreo(rs.getString("correo"));
                c.setNumeroCompras(rs.getInt("numero_compras"));
                c.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                clientes.add(c);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(),
                "Error de consulta: " + e.getMessage(),
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clientes;
    }

    @Override
    public int setAgregar(Cliente c) {
        String sql = "INSERT INTO clientes (id_usuario, nombre_cliente, numero_identificacion, correo, numero_compras) VALUES (?,?,?,?,?)";
        try {
            con = conectar.getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, c.getIdUsuario());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getNumeroIdentificacion());
            ps.setString(4, c.getCorreo());
            ps.setInt(5, c.getNumeroCompras());
            return ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(),
                "Error en la inserción: " + e.getMessage(),
                JOptionPane.ERROR_MESSAGE);
            return 0;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int setActualizar(Cliente c) {
        String sql = "UPDATE clientes SET id_usuario=?, nombre_cliente=?, numero_identificacion=?, correo=?, numero_compras=? WHERE id_cliente=?";
        try {
            con = conectar.getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, c.getIdUsuario());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getNumeroIdentificacion());
            ps.setString(4, c.getCorreo());
            ps.setInt(5, c.getNumeroCompras());
            ps.setInt(6, c.getId());
            return ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(),
                "Error en la actualización: " + e.getMessage(),
                JOptionPane.ERROR_MESSAGE);
            return 0;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int setEliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente=?";
        try {
            con = conectar.getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(),
                "Error en la eliminación: " + e.getMessage(),
                JOptionPane.ERROR_MESSAGE);
            return 0;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 🔹 Método extra: buscar cliente por número de identificación
    public Cliente buscarPorIdentificacion(String numeroIdentificacion) {
        String sql = "SELECT * FROM clientes WHERE numero_identificacion = ?";
        try {
            con = conectar.getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, numeroIdentificacion);
            rs = ps.executeQuery();

            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id_cliente"));
                c.setIdUsuario(rs.getInt("id_usuario"));
                c.setNombre(rs.getString("nombre_cliente"));
                c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                c.setCorreo(rs.getString("correo"));
                c.setNumeroCompras(rs.getInt("numero_compras"));
                c.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                return c;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(),
                "Error en la búsqueda: " + e.getMessage(),
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
