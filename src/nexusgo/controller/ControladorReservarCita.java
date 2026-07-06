/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaReservarCitas;

/**
 *
 * @author HOME
 */
public class ControladorReservarCita implements ActionListener{
    
    private VistaReservarCitas vistaReserva;
    private VistaPrincipalCliente vistaPrincipal;
    private UsuarioDao usuarioDao;
    private int idClienteLogueado;

   
    public ControladorReservarCita (VistaReservarCitas vistaReserva, VistaPrincipalCliente vistaPrincipal, int idClienteLogueado) {
        this.vistaReserva = vistaReserva;
        this.vistaPrincipal = vistaPrincipal;
        this.idClienteLogueado = idClienteLogueado;
        this.usuarioDao = new UsuarioDao();

        // Escuchamos las acciones de los botones en la vista
        this.vistaReserva.btnAgendar.addActionListener(this);
        this.vistaReserva.btnVolver.addActionListener(this);
        
        // Llenamos el selector de servicios al iniciar
        cargarServicios();
    }

    /**
     * Llena el JComboBox con los servicios disponibles
     */
    private void cargarServicios() {
        vistaReserva.comboServicios.removeAllItems();
        vistaReserva.comboServicios.addItem("--- Seleccione un servicio ---");
        vistaReserva.comboServicios.addItem("1. Shampoo de Cebolla Anyeluz");
        vistaReserva.comboServicios.addItem("2. Corte de Cabello Premium");
        vistaReserva.comboServicios.addItem("3. Manicure + Pedicure");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // --- EVENTO: CONFIRMAR RESERVA ---
        if (e.getSource() == vistaReserva.btnAgendar) {
            int seleccionIdx = vistaReserva.comboServicios.getSelectedIndex();
            String fechaHora = vistaReserva.txtFechaHora.getText().trim();

            // Validaciones básicas de campos vacíos
            if (seleccionIdx == 0) {
                JOptionPane.showMessageDialog(vistaReserva, "Por favor, seleccione un servicio válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaHora.isEmpty()) {
                JOptionPane.showMessageDialog(vistaReserva, "Por favor, ingrese la fecha y hora para la cita.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Usamos el índice seleccionado como el ID del servicio (1, 2, 3...)
            int idServicio = seleccionIdx; 

            // Registramos en la base de datos usando el UsuarioDAO
            boolean guardadoExitoso = usuarioDao.registrarCita(idClienteLogueado, idServicio, fechaHora);

            if (guardadoExitoso) {
                JOptionPane.showMessageDialog(vistaReserva, "¡Cita reservada con éxito!");
                regresarAlMenu();
            } else {
                JOptionPane.showMessageDialog(vistaReserva, "No se pudo registrar la cita. Verifique la conexión.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- EVENTO: BOTÓN VOLVER ---
        if (e.getSource() == vistaReserva.btnVolver) {
            regresarAlMenu();
        }
    }

    /**
     * Limpia la pantalla central para regresar al catálogo principal
     */
    private void regresarAlMenu() {
        JPanel contenedorCentral = vistaPrincipal.getContenidoCentralDinamico();
        contenedorCentral.removeAll();
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }
}
