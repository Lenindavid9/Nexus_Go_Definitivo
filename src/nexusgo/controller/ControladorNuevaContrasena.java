/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaNuevaContrasena;

/**
 *
 * @author USUARIO
 */
public class ControladorNuevaContrasena implements ActionListener {

    private VistaNuevaContrasena vista;
    private UsuarioDao usuarioDAO;
    private String correoUsuario;

    public ControladorNuevaContrasena(VistaNuevaContrasena vista, String correo) {
        this.vista = vista;
        this.correoUsuario = correo;
        this.usuarioDAO = new UsuarioDao();

        /*Se registra este controlador como escuchador del botón "Cambiar".
        con esto cuando el usuario haga clic sobre el botón,
        se ejecuta automáticamente el método actionPerformed().*/
        this.vista.cambiar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /*Se verifica que el evento haya sido generado
        específicamente por el botón "Cambiar".*/
        if (e.getSource() == vista.cambiar) {

            //Se obtiene la contraseña del el usuario en el primer campo de texto.
            String pass = vista.tContrasena.getText();

            //Se obtiene la confirmacion de la contraseña ingresada
            String confirmarPass = vista.tConfirmar.getText();

            /*Se verifica que ambos campos hayan sido diligenciados.
            Si alguno está vacío, el sistema no podra continuar*/
            if (pass.isEmpty() || confirmarPass.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos de contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Se comparan ambas contraseñas para verificar que sean iguales.
            if (!pass.equals(confirmarPass)) {
                JOptionPane.showMessageDialog(vista, "Las contraseñas ingresadas no coinciden. Inténtelo de nuevo.", "Error de Coincidencia", JOptionPane.ERROR_MESSAGE);
                
                // Se limpia el primer campo de contraseña para que el usuario vuelva a escribirla.
                vista.tContrasena.setText("");

                // También se limpia el campo de confirmación.
                vista.tConfirmar.setText("");

                /* Se coloca nuevamente el cursor sobre el primer campo
                para facilitar la guia de donde el usuario escriba la contraseña
                sin necesidad de hacer clic con el mouse.*/
                vista.tContrasena.requestFocus();
                return;
            }

            /*Si ambas contraseñas coinciden y las validaciones fueron
            aceptadas  correctamente, se llama al método encargado
            de actualizar la contraseña en la base de datos.*/

            boolean exito = usuarioDAO.actualizarContrasena(correoUsuario, pass);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "Su contraseña ha sido reestablecida exitosamente en Nexus GO.", "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
                //se cierra la ventana
                vista.dispose();

                // se llama a la vista
                VistaInicioSesion login = new VistaInicioSesion();

                //tambien se imvoca su controlador correspondiente
                ControladorInicioSesion inicioSesion = new ControladorInicioSesion(login);

                //Se establece el tamaño inicial que tendrá la ventana.
                login.setSize(450, 450);

                // La ventana se abre por completo en toda la pantalla
                login.setExtendedState(login.MAXIMIZED_BOTH);

                //muestra la ventana
                login.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(vista, "Hubo un error al intentar actualizar la contraseña.", "Error de Sistema", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
