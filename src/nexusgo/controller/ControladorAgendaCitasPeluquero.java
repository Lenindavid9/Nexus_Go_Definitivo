/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import nexusgo.view.PanelModificarCita;
import nexusgo.view.VistaPrincipalPeluquero;

/**
 *
 * @author HOME
 */
public class ControladorAgendaCitasPeluquero implements ActionListener{
    private final PanelModificarCita panel;
    private final VistaPrincipalPeluquero vistaPrincipal;

    public ControladorAgendaCitasPeluquero(PanelModificarCita panel, VistaPrincipalPeluquero vistaPrincipal) {
        this.panel = panel;
        this.vistaPrincipal = vistaPrincipal;

        // Enlazamos los escuchadores utilizando los getters del panel
        this.panel.getBtnGuardar().addActionListener(this);
        this.panel.getBtnImagen().addActionListener(this);
        this.panel.getBtnCerrarSesion().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();

        // 1. ACCIÓN ACCIONAR BOTÓN GUARDAR
        if (origen == panel.getBtnGuardar()) {
            guardarModificaciones();

        // 2. ACCIÓN BOTÓN VOLVER AL INICIO
        } else if (origen == panel.getBtnImagen()) {
            // Aquí puedes implementar un JFileChooser en el futuro si deseas cargar archivos reales
            JOptionPane.showMessageDialog(panel, "Función para examinar archivos de imagen en desarrollo.", 
                                          "Examinar Imagen", JOptionPane.INFORMATION_MESSAGE);

        // 4. ACCIÓN BOTÓN CERRAR SESIÓN
        } else if (origen == panel.getBtnCerrarSesion()) {
            int confirmacion = JOptionPane.showConfirmDialog(vistaPrincipal, "¿Desea cerrar la sesión actual de Peluquero?", 
                                                             "Confirmar Salida", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                vistaPrincipal.dispose();
            }
        }
    }

    /**
     * Procesa y valida los campos del formulario antes de simular el guardado.
     */
    private void guardarModificaciones() {
        String servicioSeleccionado = (String) panel.getComboServicios().getSelectedItem();
        int filaSeleccionada = panel.getTablaHorarios().getSelectedRow();
        int columnaSeleccionada = panel.getTablaHorarios().getSelectedColumn();

        // Validar selección de servicio
        if (panel.getComboServicios().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(panel, "Por favor, seleccione un servicio válido de la lista.", 
                                          "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar precio vacío o con el placeholder por defecto
        

        // Validar selección de un bloque horario en la cuadrícula de la tabla
        if (filaSeleccionada == -1 || columnaSeleccionada <= 0) {
            JOptionPane.showMessageDialog(panel, "Por favor, seleccione una celda correspondiente al día y hora en la agenda.", 
                                          "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            
            String hora = panel.getTablaHorarios().getValueAt(filaSeleccionada, 0).toString();
            String dia = panel.getTablaHorarios().getColumnName(columnaSeleccionada);

            // Actualizamos visualmente la tabla de manera inmediata para reflejar los cambios
            panel.getTablaHorarios().setValueAt(servicioSeleccionado, filaSeleccionada, columnaSeleccionada);

            JOptionPane.showMessageDialog(panel, "Cita modificada exitosamente:\n"
                                          + "- Servicio: " + servicioSeleccionado + "\n"
                                        
                                          + "- Espacio: " + dia + " a las " + hora, 
                                          "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, "El precio ingresado no es válido. Use solo números.", 
                                          "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
