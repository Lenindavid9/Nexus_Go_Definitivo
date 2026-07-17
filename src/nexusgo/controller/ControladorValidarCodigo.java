/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import nexusgo.model.Usuario;
import nexusgo.view.VistaNuevaContrasena;
import nexusgo.view.VistaValidarCodigo;

/**
 *
 * @author USUARIO
 */
public class ControladorValidarCodigo implements ActionListener {

    private final VistaValidarCodigo vista;
    private final String tokenCorrecto;
    private final Usuario usuario;

    public ControladorValidarCodigo(VistaValidarCodigo vista, String tokenCorrecto, Usuario usuario) {
        this.vista = vista;
        this.tokenCorrecto = tokenCorrecto;
        this.vista.enviar.addActionListener(this);
        this.usuario = usuario;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.enviar) {
            // Se llama al método encargado de validar el código.
            procesarVerificacion();
        }
    }

    //Aquí se verifica que el campo no esté vacío y se compara el código ingresado
    //con el código enviado por correo.
    private void procesarVerificacion() {

        //Obtiene el texto escrito por el usuario.
        String codigoIngresado = vista.tVCodigo.getText();

        // Antes de realizar cualquier comparación, primero se verifica
        // que el usuario realmente haya escrito un código.
        if (codigoIngresado.isEmpty()) {

            /* Si el campo lo deja vacío, le mostrara una advertencia
            indicando que debe ingresar el código*/
            JOptionPane.showMessageDialog(vista,
                    "Por favor, ingrese el código de verificación.",
                    "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        /* Esto evita que el usuario haga varios clics seguidos mientra
            se realiza la validación del código.*/
        vista.enviar.setEnabled(false);

        /* Se compara el código escrito por el usuario con el código
        original que fue enviado al correo. */
        if (codigoIngresado.equals(tokenCorrecto)) {

            /*  Si ambos códigos son exactamente iguales,
            significa que la verificación fue exitosa*/
            JOptionPane.showMessageDialog(vista,
                    "¡Código Válido!", "Verificación Correcta",
                    JOptionPane.INFORMATION_MESSAGE);

            /* Ya despues que este validado el código,
            ya se cierra automáticamente ese modulo.*/
            vista.dispose();

            // se llama la vista
            VistaNuevaContrasena vistaContrasena = new VistaNuevaContrasena();

            // tambien se imvoca su controlador correspondiente
            ControladorNuevaContrasena controlNuevaContra = new ControladorNuevaContrasena(vistaContrasena, usuario.getCorreo());

            //Se establece el tamaño inicial que tendrá la ventana.
            vistaContrasena.setSize(450, 450);

            // La ventana se abre por completo en toda la pantalla
            vistaContrasena.setExtendedState(vistaContrasena.MAXIMIZED_BOTH);

            //muestra la ventana
            vistaContrasena.setVisible(true);

        } else {
            // Código incorrecto
            JOptionPane.showMessageDialog(vista,
                    "El código de verificación ingresado es incorrecto. Inténtelo de nuevo.",
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);

            /* Se limpia el campo de texto para que el usuario
            pueda escribir nuevamente el código.*/
            vista.tVCodigo.setText("");

            /* Se vuelve a habilitar el botón "Enviar"
            para permitir un nuevo intento de validación. */
            vista.enviar.setEnabled(true);
        }
    }
}
