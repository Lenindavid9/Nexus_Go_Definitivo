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
 * @author HOME
 */
public class CitaDao{
   Conexion conexion = new Conexion();

    // LISTAR CITAS
    public List<Cita> listarCitas() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT id_cita, cliente, servicio, precio, dia_semana, hora_inicio FROM citas";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita cita = new Cita();
                cita.setIdCita(rs.getInt("id_cita"));
                cita.setCliente(rs.getString("cliente"));
                cita.setServicio(rs.getString("servicio"));
                cita.setPrecio(rs.getDouble("precio"));
                cita.setDiaSemana(rs.getString("dia_semana"));
                cita.setHoraInicio(rs.getString("hora_inicio"));

                lista.add(cita);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        }
        return lista;
    }

    // U - UPDATE: MODIFICAR CITA
    public boolean modificar(Cita cita) {
        String sql = """
                     UPDATE citas 
                     SET servicio = ?, precio = ?, dia_semana = ?, hora_inicio = ? 
                     WHERE id_cita = ?
                     """;

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cita.getServicio());
            ps.setDouble(2, cita.getPrecio());
            ps.setString(3, cita.getDiaSemana());
            ps.setString(4, cita.getHoraInicio());
            ps.setInt(5, cita.getIdCita());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar cita: " + e.getMessage());
            return false;
        }
    }
}
