/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import com.mysql.cj.Session;
import com.mysql.cj.protocol.Message;
import com.sun.jdi.connect.Transport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import javax.swing.JOptionPane;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaValidarIdentificacion;


/**
 *
 * @author USUARIO
 */

public class ControladorValidarIdentificacion implements ActionListener {

    private final VistaValidarIdentificacion vista;
    private final UsuarioDao usuarioDao;

    public ControladorValidarIdentificacion(VistaValidarIdentificacion vista) {
        this.vista = vista;
        this.usuarioDao = new UsuarioDao();
        // con esta se hace la escucha del botón de tu vista
        this.vista.confirmar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.confirmar) {
            ejecutarFlujoValidacion();
        }
    }

    private void ejecutarFlujoValidacion() {
        String documento = vista.tIdentificacion.getText().trim();

        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, digite su número de identificación.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
                try{
            // se valida la existencia del usuario en la base de datos
            Usuario usuarioEcontrado = usuarioDao.buscarUsuarioPorIdentificacion(documento);

        if (usuarioEcontrado != null) {

            // Si el usuario si existe entoncees agrarra el correo desde la bases de datos
            String correoDestino = usuarioEcontrado.getCorreo();

            // Genera el código
            /* recuerden que el Math.random() genera un número aleatorio, luego se multiplica por 900.000
            para ajustar el limite al  que va a llegar luego se convierte en un número entero con (int), se le suma
            100.000 para que siempre tenga los 6 dígitos y finalmente el String.valueOf() lo convierte
            en un String y lo guarda en la variable tokenGenerado */
            String tokenGenerado = String.valueOf((int) (Math.random() * 900000) + 100000);

            // Deshabilitamos temporalmente el botón para evitar dobles clics
            vista.confirmar.setEnabled(false);
            vista.confirmar.setText("Enviando...");

            /* Esta parte llama al método despacharEmail para enviar un correo al usuario con su nombre
            y un token, y guarda en la variable envioExitoso un valor booleano
            que indica si el envío fue exitoso (true) o falló (false). */
            // se despachar el correo utilizando el servidor SMTP integrado con Jakarta Mail
            // (boolean envioExitoso = despacharEmail(correoDestino, usuarioEcontrado.getNombre(), tokenGenerado);)

            if (envioExitoso) {
                JOptionPane.showMessageDialog(vista,
                        "El código de verificación fue enviado con éxito al correo electrónico ya registrado.",
                        "Mensaje Enviado", JOptionPane.INFORMATION_MESSAGE);

                // se libera la ventana de la cédula
                vista.dispose();

                // Enrutamos hacia la vista y controlador
                VistaValidarCodigo vistaSiguiente = new VistaValidarCodigo();
                /*ControladorVerificarCodigo = */ new ControladorVerificarCodigo(vistaSiguiente, tokenGenerado, usuarioEcontrado);
                vistaSiguiente.setLocationRelativeTo(null);
                vistaSiguiente.setVisible(true);

            } else {
                // Si el envío falla por red o algo asi entonces restauramos el botón de la vista original
                vista.confirmar.setEnabled(true);
                vista.confirmar.setText("Confirmar");
            }

        } else {
            JOptionPane.showMessageDialog(vista, "Número de identificación no registrado o incorrecto. Por favor, verifíquelo e intente de nuevo.",
            "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }

    }catch(Exception ex){
            JOptionPane.showMessageDialog(vista, "Error del sistema: " + ex.getMessage(), "Error General", JOptionPane.ERROR_MESSAGE);
            vista.confirmar.setEnabled(true);
            vista.confirmar.setText("Confirmar");
        }
    }
    }