/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JOptionPane;
import nexusgo.model.Cita;
import nexusgo.model.CitaDao;
import nexusgo.view.VistaReservarCitas;
import nexusgo.view.VistaPrincipalCliente; 
/**
 *
 * @author HOME
 */
public class ControladorReservarCita implements ActionListener {

    private final VistaReservarCitas panelReserva;
    private final VistaPrincipalCliente vistaPrincipal;
    private final int idUsuarioLogueado;
    private final CitaDao citaDao;

    public ControladorReservarCita(VistaReservarCitas panelReserva, VistaPrincipalCliente vistaPrincipal, int idUsuarioLogueado) {
        this.panelReserva = panelReserva;
        this.vistaPrincipal = vistaPrincipal;
        this.idUsuarioLogueado = idUsuarioLogueado;
        this.citaDao = new CitaDao();

        inicializarEventos();
        cargarServicios();
    }

    private void inicializarEventos() {
        this.panelReserva.btnAgendar.addActionListener(this);
        this.panelReserva.btnVolver.addActionListener(this);
    }

    private void cargarServicios() {
        panelReserva.comboServicios.removeAllItems();
        panelReserva.comboServicios.addItem("-- Seleccione un servicio --");
        panelReserva.comboServicios.addItem("Consulta General");
        panelReserva.comboServicios.addItem("Asesoría Especializada");
        panelReserva.comboServicios.addItem("Soporte Técnico");
        panelReserva.comboServicios.addItem("Mantenimiento Preventivo");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelReserva.btnAgendar) {
            procesarReserva();
        } else if (e.getSource() == panelReserva.btnVolver) {
            System.out.println("Regresando al menú principal...");
            // Aquí puedes llamar un método de vistaPrincipal para cambiar de vista si deseas
        }
    }

    private void procesarReserva() {
        // 1. Validar que se seleccione un servicio
        if (panelReserva.comboServicios.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(panelReserva, 
                    "Por favor, seleccione un tipo de servicio.", 
                    "Campo Requerido", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validar Fecha y Hora
        String fechaHora = panelReserva.getFechaHoraFormateada();
        if (fechaHora.isEmpty()) {
            JOptionPane.showMessageDialog(panelReserva, 
                    "Por favor, seleccione una fecha y hora válidas.", 
                    "Fecha Requerida", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Validar disponibilidad de horario en la BD
        if (citaDao.existeCitaEnHorario(fechaHora)) {
            JOptionPane.showMessageDialog(panelReserva, 
                    "El horario seleccionado (" + fechaHora + ") ya se encuentra ocupado.\n" +
                    "Por favor, elige otra fecha u hora.", 
                    "Horario No Disponible", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Mapear datos necesarios
        String servicioNombre = (String) panelReserva.comboServicios.getSelectedItem();
        int idServicio = citaDao.obtenerIdServicioPorNombre(servicioNombre);
        int idProfesional = citaDao.obtenerIdProfesionalPorDefecto(); 

        // 5. Instanciar Cita utilizando la ID del usuario en sesión
        Cita nuevaCita = new Cita(this.idUsuarioLogueado, idProfesional, idServicio, fechaHora);

        // 6. Guardar cita en la BD
        if (citaDao.agendarCita(nuevaCita)) {
            JOptionPane.showMessageDialog(panelReserva, 
                    "¡Cita agendada con éxito en NexusGO!\n\n" +
                    "Servicio: " + servicioNombre + "\n" +
                    "Fecha y Hora: " + fechaHora, 
                    "Reserva Exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(panelReserva, 
                    "Ocurrió un error al intentar registrar la cita en la base de datos.", 
                    "Error de Registro", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        panelReserva.comboServicios.setSelectedIndex(0);
        panelReserva.txtObservaciones.setText("");
        panelReserva.dateChooserFecha.setDate(new Date());
    }
}
