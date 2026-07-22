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

        if (botonPresionado == vista.btnInicio) {
            // A. Limpiamos cualquier subvista previa (Inventario, Citas, etc.)
            JPanel contenedorDinamico = vista.getContenidoCentralDinamico();
            contenedorDinamico.removeAll();

            // B. Llamamos al método de la vista que vuelve a ensamblar la pantalla principal
            vista.restaurarComponentesPrincipales();

            // C. Revalidamos y redibujamos el contenedor para que Swing renderice la vista inicial
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
                PanelModificarCita panelCitas = new PanelModificarCita();
                new ControladorAgendaCitasPeluquero(panelCitas, vista);
                cambiarPanelCentral(panelCitas);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al abrir gestión de citas: " + ex.getMessage(),
                        "Error de Navegación", JOptionPane.ERROR_MESSAGE);
            }
        } // 4. BOTÓN CERRAR SESIÓN / REGISTRAR SALIDA
        else if (botonPresionado == vista.btnCerrarSesion) {
            ejecutarCerrarSesion();
        }
    }

    private void ejecutarCerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Desea cerrar la sesión y registrar su salida?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            vista.dispose(); // Cierra la ventana actual

            // Abre la pantalla de Login para un nuevo inicio de sesión
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

        // Refresco indispensable en Swing para evitar pantallas vacías
        contenedorDinamico.revalidate();
        contenedorDinamico.repaint();
    }
    
    
}
