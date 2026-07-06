/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import nexusgo.model.Conexion;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaNuevaContrasena;

/**
 *
 * @author USUARIO
 */
public class ControladorNuevaContrasena {
 
    private VistaNuevaContrasena vista;
    private UsuarioDao usuarioDAO;
    private String correoUsuario;

    public ControladorNuevaContrasenarasena(VistaNuevaContrasena vista, String correo) {
        this.vista = vista;
        this.correoUsuario = correo;
        this.usuarioDAO = new UsuarioDao();

        this.vista.cambiar.addActionListener(this);
        
        this.vista.setSize(450, 350);
        this.vista.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.cambiar) {
            String pass = vista.tContrasena.getText().trim();
            String confirmarPass = vista.tConfirmar.getText().trim();

            if (pass.isEmpty() || confirmarPass.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos de contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!pass.equals(confirmarPass)) {
                JOptionPane.showMessageDialog(vista, "Las contraseñas ingresadas no coinciden. Inténtelo de nuevo.", "Error de Coincidencia", JOptionPane.ERROR_MESSAGE);
                vista.tContrasena.setText("");
                vista.tConfirmar.setText("");
                vista.tContrasena.requestFocus();
                return;
            }

            boolean exito = usuarioDAO.actualizarContrasena(correoUsuario, pass);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "Su contraseña ha sido reestablecida exitosamente en Nexus GO.", "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(vista, "Hubo un error al intentar actualizar la contraseña.", "Error de Sistema", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public boolean actualizarContrasena(String correo, String nuevaContrasena) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";
        
        try {
            con = Conexion.getConnection(); // Ajusta a tu método de conexión
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevaContrasena);
            ps.setString(2, correo);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
            }
        }
    }
}
