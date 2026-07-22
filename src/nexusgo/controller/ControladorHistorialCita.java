/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaHistorialCita;
import nexusgo.view.VistaPrincipalCliente;

/**
 *
 * @author HOME
 */
public class ControladorHistorialCita implements ActionListener {

    private final VistaHistorialCita vistaHistorial;
    private final VistaPrincipalCliente vistaPrincipal;
    private final int idClienteLogueado;
    private final UsuarioDao usuarioDao;

    public ControladorHistorialCita(VistaHistorialCita vistaHistorial, VistaPrincipalCliente vistaPrincipal, int idClienteLogueado) {
        this.vistaHistorial = vistaHistorial;
        this.vistaPrincipal = vistaPrincipal;
        this.idClienteLogueado = idClienteLogueado;
        this.usuarioDao = new UsuarioDao();

        this.vistaHistorial.btnVolver.addActionListener(this);
        cargarHistorial();
    }

    public void cargarHistorial() {
        vistaHistorial.limpiarTabla();

        try {
            List<Object[]> registrosCitas = usuarioDao.listarCitasPorCliente(this.idClienteLogueado);

            if (registrosCitas == null || registrosCitas.isEmpty()) {
                vistaHistorial.agregarFila(new Object[]{"No registra citas vigentes", "-", "-"});
            } else {
                for (Object[] fila : registrosCitas) {
                    vistaHistorial.agregarFila(fila);
                }
            }

            SwingUtilities.invokeLater(() -> {
                vistaHistorial.tablaCitas.revalidate();
                vistaHistorial.tablaCitas.repaint();
            });

        } catch (Exception ex) {
            System.err.println("Error en ControladorHistorialCita: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                vistaHistorial, 
                "Error al cargar las citas: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaHistorial.btnVolver) {
            JPanel contenedorCentral = vistaPrincipal.getContenidoCentralDinamico();
            contenedorCentral.removeAll();
            contenedorCentral.revalidate();
            contenedorCentral.repaint();
        }
    }
    
}
