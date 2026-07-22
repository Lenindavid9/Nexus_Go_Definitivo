/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    // Constructor con los 3 parámetros para integrarse con VistaPrincipalCliente
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
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    private void procesarReserva() {
        // 1. Validar Selección de Servicio
        if (panelReserva.comboServicios.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(panelReserva,
                    "Por favor, seleccione un tipo de servicio.",
                    "Campo Requerido",JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validar Fecha y Hora
        String fechaHora = panelReserva.getFechaHoraFormateada();
        if (fechaHora.isEmpty()) {
            JOptionPane.showMessageDialog(panelReserva,
                    "Por favor, seleccione una fecha y hora válidas.",
                    "Fecha Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 2.1 Validar que la fecha sea un día POSTERIOR a hoy (no se permite hoy ni fechas pasadas)
        Date fechaSeleccionada = panelReserva.dateChooserFecha.getDate();

        Calendar calSeleccionada = Calendar.getInstance();
        calSeleccionada.setTime(fechaSeleccionada);
        calSeleccionada.set(Calendar.HOUR_OF_DAY, 0);
        calSeleccionada.set(Calendar.MINUTE, 0);
        calSeleccionada.set(Calendar.SECOND, 0);
        calSeleccionada.set(Calendar.MILLISECOND, 0);

        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        if (!calSeleccionada.after(calHoy)) {
            JOptionPane.showMessageDialog(panelReserva,
                    "No se pueden agendar citas para el día de hoy ni para fechas pasadas.\n"
                    + "Por favor, seleccione un día posterior a hoy.",
                    "Fecha No Válida",JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Verificar disponibilidad en la base de datos
        if (citaDao.existeCitaEnHorario(fechaHora)) {
            JOptionPane.showMessageDialog(panelReserva,
                    "El horario seleccionado (" + fechaHora + ") ya se encuentra ocupado.\n"
                    + "Por favor, elige otra hora.",
                    "Horario No Disponible",JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Mapear datos necesarios
        String servicioNombre = (String) panelReserva.comboServicios.getSelectedItem();
        int idServicio = citaDao.obtenerIdServicioPorNombre(servicioNombre);
        if (idServicio == -1) {
            idServicio = 1; // Respaldo
        }

        int idProfesional = citaDao.obtenerIdProfesionalPorDefecto();

        // 5. Instanciar objeto Cita asociando el 'idUsuarioLogueado' directo
        Cita nuevaCita = new Cita(this.idUsuarioLogueado, idProfesional, idServicio, fechaHora);

        // 6. Guardar en la base de datos
        if (citaDao.agendarCita(nuevaCita)) {
            JOptionPane.showMessageDialog(panelReserva,
                    "¡Cita agendada con éxito!\n\n"
                    + "Servicio: " + servicioNombre + "\n"
                    + "Fecha y Hora: " + fechaHora,
                    "Reserva Exitosa",JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(panelReserva,
                    "Ocurrió un error al guardar la cita en la base de datos.",
                    "Error de Registro",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        panelReserva.comboServicios.setSelectedIndex(0);
        panelReserva.txtObservaciones.setText("");
        panelReserva.dateChooserFecha.setDate(new Date());
    }

    private void cargarServicios() {
        panelReserva.comboServicios.removeAllItems();
        panelReserva.comboServicios.addItem("-- Seleccione un servicio --");

        // Consulta la BD a través del DAO
        List<String> servicios = citaDao.obtenerListaServicios();

        // Llena el ComboBox con los datos reales
        for (String servicio : servicios) {
            panelReserva.comboServicios.addItem(servicio);
        }
    }
}
