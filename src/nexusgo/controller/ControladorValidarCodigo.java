/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import nexusgo.model.Usuario;
import nexusgo.view.VistaValidarCodigo;

/**
 *
 * @author USUARIO
 */
public class ControladorValidarCodigo {
    private final VistaValidarCodigo vista;
    private final String tokenCorrecto;
    private final Usuario usuarioActual;

    public ControladorValidarCodigo(VistaValidarCodigo vista, String tokenCorrecto, Usuario usuarioActual) {
        this.vista = vista;
        this.tokenCorrecto = tokenCorrecto;
        this.usuarioActual = usuarioActual;

        // IMPORTANTE: Hacemos que el botón "enviar" de tu vista sea escuchado.
        // Como en tu vista declaraste el botón "enviar" como privado, cámbialo a 'public' en tu VistaValidarCodigo.
        this.vista.enviar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.enviar) {
            procesarVerificacion();
        }
    }

    private void procesarVerificacion() {
        String codigoIngresado = vista.tVCodigo.getText().trim();

        // 1. Validar campo vacío
        if (codigoIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Por favor, ingrese el código de verificación.", 
                "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Deshabilitar botón temporalmente por seguridad
        vista.enviar.setEnabled(false);

        // 2. Comparar el código ingresado con el token enviado al correo
        if (codigoIngresado.equals(tokenCorrecto)) {
            JOptionPane.showMessageDialog(vista, 
                "¡Código verificado con éxito! Bienvenido " + usuarioActual.getNombre(), 
                "Verificación Correcta", JOptionPane.INFORMATION_MESSAGE);

            // Cerrar la ventana actual del código
            vista.dispose();

            // 3. SIGUIENTE PASO: Aquí enrutas a la vista de cambiar contraseña
            /* 
            VistaCambiarContrasena vistaSiguiente = new VistaCambiarContrasena();
            new ControladorCambiarContrasena(vistaSiguiente, usuarioActual);
            vistaSiguiente.setLocationRelativeTo(null);
            vistaSiguiente.setVisible(true);
            */
            
        } else {
            // Código incorrecto
            JOptionPane.showMessageDialog(vista, 
                "El código de verificación ingresado es incorrecto. Inténtelo de nuevo.", 
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            
            // Limpiar el campo y restablecer el botón para un nuevo intento
            vista.tVCodigo.setText("");
            vista.tVCodigo.requestFocus();
            vista.enviar.setEnabled(true);
        }
    }
    
}