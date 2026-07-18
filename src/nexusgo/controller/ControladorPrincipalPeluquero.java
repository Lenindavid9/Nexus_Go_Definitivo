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
import nexusgo.view.PanelInventarioProductosPeluquero;
import nexusgo.view.PanelModificarCita;
import nexusgo.view.VistaPrincipalPeluquero;
/**
 *
 * @author HOME
 */
public class ControladorPrincipalPeluquero implements ActionListener{
 private final VistaPrincipalPeluquero vista;

    public ControladorPrincipalPeluquero(VistaPrincipalPeluquero vista) {
        this.vista = vista;

        // Centralizamos los escuchadores asignando esta misma clase como Listener
        this.vista.btnInicio.addActionListener(this);
        this.vista.btnInventario.addActionListener(this);
        this.vista.btnCitas.addActionListener(this);
        this.vista.btnCerrarSesion.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object botonPresionado = e.getSource();

        // 1. BOTÓN INICIO (CASA)
        if (botonPresionado == vista.btnInicio) {
            // Restaura los componentes originales del panel central por defecto
            vista.restaurarComponentesPrincipales(); 
            
        // 2. BOTÓN INVENTARIO
        } else if (botonPresionado == vista.btnInventario) {
            try {
                PanelInventarioProductosPeluquero panelInventario = new PanelInventarioProductosPeluquero();
                new ControladorInventarioPeluquero(panelInventario, vista);
                
                cambiarPanelCentral(panelInventario);
            } catch (Exception ex) {
                System.out.println("Error al abrir inventario: " + ex.getMessage());
                JOptionPane.showMessageDialog(vista, "Error al abrir el inventario de productos: " + ex.getMessage(), 
                                              "Error de Navegación", JOptionPane.ERROR_MESSAGE);
            }

        // 3. BOTÓN CITAS
        } else if (botonPresionado == vista.btnCitas) {
            try {
                PanelModificarCita panelCitas = new PanelModificarCita();
                // Vinculamos su respectivo controlador de gestión de citas aquí
                new ControladorAgendaCitasPeluquero(panelCitas, vista);
                
                cambiarPanelCentral(panelCitas);
            } catch (Exception ex) {
                System.out.println("Error al abrir agenda de citas: " + ex.getMessage());
                JOptionPane.showMessageDialog(vista, "Error al abrir la gestión de citas: " + ex.getMessage(), 
                                              "Error de Navegación", JOptionPane.ERROR_MESSAGE);
            }

        // 4. BOTÓN CERRAR SESIÓN
        } else if (botonPresionado == vista.btnCerrarSesion) {
            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Desea cerrar la sesión actual de Peluquero?", 
                                                             "Confirmar Salida", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                vista.dispose(); // Destruye el frame de forma limpia liberando memoria
            }
        }
    }

    /*
     * Reemplaza e intercambia las subvistas dentro del panel central dinámico 
     * manteniendo intacto el menú de navegación lateral.
     */
    private void cambiarPanelCentral(JPanel nuevoPanel) {
        JPanel contenedorDinamico = vista.getContenidoCentralDinamico();
        
        // Limpiamos los componentes que se encuentren actualmente en el centro
        contenedorDinamico.removeAll();
        
        // Insertamos el nuevo panel en el área del centro
        contenedorDinamico.add(nuevoPanel, BorderLayout.CENTER);
        
        // Forzamos a Swing a recalcular la jerarquía visual de componentes y redibujamos
        contenedorDinamico.revalidate();
        contenedorDinamico.repaint();
    }
}