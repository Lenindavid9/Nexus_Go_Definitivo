/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.Usuario;
import nexusgo.view.PanelInventarioProductosPeluquero;
import nexusgo.view.PanelModificarCita;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaPrincipalPeluquero;

/**
 *
 * @author HOME
 */
public class ControladorPrincipalPeluquero implements ActionListener {

    private final VistaPrincipalPeluquero vista;
    private final Usuario usuarioLogueado;

    public ControladorPrincipalPeluquero(VistaPrincipalPeluquero vista, Usuario usuarioLogueado) {
        this.vista = vista;
        this.usuarioLogueado = usuarioLogueado;

        // Registrar Listeners
        if (this.vista.btnInicio != null) {
            this.vista.btnInicio.addActionListener(this);
        }
        if (this.vista.btnInventario != null) {
            this.vista.btnInventario.addActionListener(this);
        }
        if (this.vista.btnCitas != null) {
            this.vista.btnCitas.addActionListener(this);
        }
        if (this.vista.btnCerrarSesion != null) {
            this.vista.btnCerrarSesion.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object botonPresionado = e.getSource();

        // 1. BOTÓN INICIO
        if (botonPresionado == vista.btnInicio) {
            JPanel contenedorDinamico = vista.getContenidoCentralDinamico();
            contenedorDinamico.removeAll();

            vista.restaurarComponentesPrincipales();

            contenedorDinamico.revalidate();
            contenedorDinamico.repaint();
        } // 2. BOTÓN INVENTARIO
        else if (botonPresionado == vista.btnInventario) {
            try {
                PanelInventarioProductosPeluquero panelInventario = new PanelInventarioProductosPeluquero();
                new ControladorInventarioPeluquero(panelInventario, vista);
                cambiarPanelCentral(panelInventario);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al abrir el inventario: " + ex.getMessage(),
                        "Error de Navegación", JOptionPane.ERROR_MESSAGE);
            }
        } // 3. BOTÓN CITAS
        else if (botonPresionado == vista.btnCitas) {
            try {
                // Instanciación limpia del panel con las correcciones en LGoodDatePicker
                PanelModificarCita panelCitas = new PanelModificarCita();

                // Enlace con el controlador de citas pasando la referencia de la vista principal
                new ControladorModificarCitas(panelCitas, usuarioLogueado.getIdUsuario(), this);

                cambiarPanelCentral(panelCitas);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al abrir gestión de citas: " + ex.getMessage(),
                        "Error de Navegación", JOptionPane.ERROR_MESSAGE);
            }
        } // 4. BOTÓN CERRAR SESIÓN
        else if (botonPresionado == vista.btnCerrarSesion) {
            ejecutarCerrarSesion();
        }
    }

    public void ejecutarCerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Desea cerrar la sesión y registrar su salida?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            vista.dispose(); // Cierra la ventana principal

            // Retorno al Login de la aplicación
            VistaInicioSesion loginVista = new VistaInicioSesion();
            new ControladorInicioSesion(loginVista);
            loginVista.setLocationRelativeTo(null);
            loginVista.setVisible(true);
        }
    }

    private void cambiarPanelCentral(JPanel nuevoPanel) {
        JPanel contenedorDinamico = vista.getContenidoCentralDinamico();
        contenedorDinamico.removeAll();
        contenedorDinamico.setLayout(new BorderLayout());
        contenedorDinamico.add(nuevoPanel, BorderLayout.CENTER);

        contenedorDinamico.revalidate();
        contenedorDinamico.repaint();
    }
}
