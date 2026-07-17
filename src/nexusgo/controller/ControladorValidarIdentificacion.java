/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jakarta.mail.PasswordAuthentication;
import java.util.Properties;
import javax.swing.JOptionPane;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaValidarCodigo;
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
            JOptionPane.showMessageDialog(vista, "Por favor, digite su número de identificación.", "Campo Requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            // se valida la existencia del usuario en la base de datos
            Usuario usuarioEncontrado = usuarioDao.buscarUsuarioPorIdentificacion(documento);

            if (usuarioEncontrado != null) {

                // Si el usuario si existe entoncees agrarra el correo desde la bases de datos
                String correoDestino = usuarioEncontrado.getCorreo();

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
                // se despachar el correo utilizando el servidor SMTP integrado con Jakarta 
                //prueva rapida
                System.out.println("Correo: " + correoDestino);
                System.out.println("Nombre: " + usuarioEncontrado.getNombre());
                System.out.println("Token: " + tokenGenerado);

                //comentario
                boolean envioExitoso = despacharEmail(correoDestino, usuarioEncontrado.getNombre(), tokenGenerado);
                if (envioExitoso) {
                    JOptionPane.showMessageDialog(vista,
                            "El código de verificación fue enviado con éxito al correo electrónico ya registrado.",
                            "Mensaje Enviado", JOptionPane.INFORMATION_MESSAGE);

                    // se libera la ventana de la cédula
                    vista.dispose();

                    // Enrutamos hacia la vista y controlador
                    VistaValidarCodigo vistaSiguiente = new VistaValidarCodigo();
                    ControladorValidarCodigo controlVeriCodigo = new ControladorValidarCodigo(vistaSiguiente, tokenGenerado, usuarioEncontrado);
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

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error del sistema: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            vista.confirmar.setEnabled(true);
            vista.confirmar.setText("Confirmar");
        }
    }

    /* Este método se encarga de enviar un correo electrónico al usuario 
    con el código de verificación que servirá para restablecer su contraseña.
    Parámetros:
      - emailDestinatario: correo de la persona que recibirá el mensaje.
      - nombreUsuario: nombre del usuario que aparecerá en el saludo.
      - codigoToken: código de verificación que el usuario deberá ingresar.

      Retorna:
      - true: si el correo fue enviado correctamente.
      - false: si ocurrió algún error durante el envío.
     */
    private boolean despacharEmail(String emailDestinatario, String nombreUsuario, String codigoToken) {
        /*
        Se crea un objeto Properties donde se almacenan todas las las configuraciones necesarias
        para conectarse con el servidor de correos de Gmail. */
        Properties propiedades = new Properties();

        // Indica que para enviar correos será necesario autenticarse con un correo y una contraseña
        propiedades.put("mail.smtp.auth", "true");

        // Activa la seguridad TLS.
        // TLS cifra la comunicación entre el programa y Gmail para que la información viaje protegida por Internet.
        propiedades.put("mail.smtp.starttls.enable", "true");

        // Dirección del servidor SMTP de Gmail.
        // SMTP es el protocolo encargado de enviar correos electrónicos.
        propiedades.put("mail.smtp.host", "smtp.gmail.com");

        // Puerto utilizado por Gmail para conexiones seguras mediante TLS
        propiedades.put("mail.smtp.port", "587");

        //️ Parámetros de autenticación del remitente (osea con el correo de donde se manda el codigo)
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";

        /*No es la contraseña normal del correo.
        Google genera esta clave especial para permitir que aplicaciones
        externas puedan enviar correos de forma segura.
         */
        final String miClaveDeCorreo = "rksu umvz hnom irzf"; // Esa clave permite que el programa se autentique y envíe correos.

        /* La sesión guarda toda la configuración anterior y además
        registra las credenciales que utilizará Gmail para verificar
        que el programa tiene permiso para enviar correos. */
        Session sesionMail = Session.getInstance(propiedades, new Authenticator() {

            /*Este método es llamado automáticamente cuando Gmail solicita
            las credenciales del remitente. */
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return /*Se entregan el correo y la contraseña de aplicación
                        para que Gmail pueda autenticar al programa.
                        el programa toma la clave que había guardado y se la entrega a Gmail para autenticarse. */ new PasswordAuthentication(miCorreoRemitente, miClaveDeCorreo);
            }
        });
        try {

            /*Se crea el mensaje de correo que será enviado.
            Al principio está vacío y posteriormente se le agregan todos sus datos. */
            Message mensaje = new MimeMessage(sesionMail);

            /* Se establece el correo desde el cual será enviado el mensaje. */
            mensaje.setFrom(new InternetAddress(miCorreoRemitente));

            /*Se indica quién recibirá el correo.
            InternetAddress.parse convierte el texto del correo en un formato que JavaMail puede interpretar correctamente.*/
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));

            /*
            Asunto que aparecerá en la parte superior del correo. */
            mensaje.setSubject("Nexus GO - Restablecer Contraseña");

            /* Aqui se Se construye el contenido del mensaje.
            Se utiliza el nombre del usuario para personalizar el saludo
            y se inserta el código de verificación que deberá ingresar al coso de validar.*/
            String mensajeTexto = "Hola, " + nombreUsuario + ".\n"
                    + "Se ha solicitado un código para cambiar tu contraseña en Nexus GO.\n"
                    + "Tu código de verificación obligatorio es:\n"
                    + "   👉 " + codigoToken + " 👈\n"
                    + "Usa este codigo en la ventana usa este cogido en la ventana te lo solicita para actualizar tu clave.";

            //Se asigna el texto anterior como contenido del correo.
            mensaje.setText(mensajeTexto);

            // Finalmente se envía el mensaje al servidor de Gmail.
            Transport.send(mensaje);

            //Como el proceso terminó, se devuelve true.
            return true;

        } catch (MessagingException e) {

            /*Si ocurre algún problema (falta de conexión, error de autenticación o el servidor no responde),
            se captura la excepción para evitar que el programa se cierre.*/
            System.out.println("Error de red SMTP: " + e.getMessage());
            JOptionPane.showMessageDialog(vista,
                    "No se pudo establecer la conexión de red con el servidor de correos.",
                    "Fallo de Comunicación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
