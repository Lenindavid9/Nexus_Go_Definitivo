/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
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

    // Constructor del controlador
    public ControladorReservarCita(VistaReservarCitas panelReserva, VistaPrincipalCliente vistaPrincipal, int idUsuarioLogueado) {
        this.panelReserva = panelReserva;
        this.vistaPrincipal = vistaPrincipal;
        this.idUsuarioLogueado = idUsuarioLogueado;
        this.citaDao = new CitaDao();

        inicializarEventos();
        cargarServicios();
        cargarFechasOcupadas();
    }

    private void inicializarEventos() {
        if (this.panelReserva != null && this.panelReserva.btnAgendar != null) {
            this.panelReserva.btnAgendar.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelReserva.btnAgendar) {
            procesarReserva();
        }
    }

    private void procesarReserva() {
        try {
            // 1. Validar Servicio
            if (panelReserva.comboServicios.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(panelReserva,
                        "Por favor, seleccione un tipo de servicio.",
                        "Campo Requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Validar Fecha (LGoodDatePicker)
            LocalDate fechaSeleccionada = panelReserva.datePickerFecha.getDate();
            if (fechaSeleccionada == null) {
                JOptionPane.showMessageDialog(panelReserva,
                        "Por favor, seleccione una fecha válida.",
                        "Fecha Requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Validar que sea a partir de mañana
            LocalDate manana = LocalDate.now().plusDays(1);
            if (fechaSeleccionada.isBefore(manana)) {
                JOptionPane.showMessageDialog(panelReserva,
                        "No se pueden agendar citas para hoy ni fechas pasadas.\nPor favor, seleccione una fecha a partir de mañana.",
                        "Fecha No Válida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4. Validar Fecha y Hora completa
            String fechaHora = panelReserva.getFechaHoraFormateada(); // Asegúrate de tener este método en tu vista
            if (fechaHora == null || fechaHora.trim().isEmpty()) {
                JOptionPane.showMessageDialog(panelReserva,
                        "Por favor, asegúrese de seleccionar tanto la fecha como la hora.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 5. ESCUDO DE SEGURIDAD: Verificar si está ocupado
            if (citaDao.existeCitaEnHorario(fechaHora)) {
                JOptionPane.showMessageDialog(panelReserva,
                        "El horario seleccionado (" + fechaHora + ") ya se encuentra ocupado.\nPor favor, elige otra hora u otro día.",
                        "Horario No Disponible", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 6. Preparar datos para la BD
            String servicioNombre = (String) panelReserva.comboServicios.getSelectedItem();
            int idServicio = citaDao.obtenerIdServicioPorNombre(servicioNombre);
            if (idServicio == -1) idServicio = 1; 

            int idProfesional = citaDao.obtenerIdProfesionalPorDefecto();

            // 7. Instanciar Modelo
            Cita nuevaCita = new Cita(this.idUsuarioLogueado, idProfesional, idServicio, fechaHora);

            // 8. Guardar y Notificar
            if (citaDao.agendarCita(nuevaCita)) {
                String correoCliente = citaDao.obtenerCorreoPorUsuarioId(this.idUsuarioLogueado);

                if (correoCliente != null && !correoCliente.trim().isEmpty()) {
                    // Hilo secundario para que la UI no se trabe mientras envía el correo
                    new Thread(() -> {
                        enviarCorreoConfirmacion(correoCliente, servicioNombre, fechaHora);
                    }).start();
                }

                JOptionPane.showMessageDialog(panelReserva,
                        "¡Cita agendada con éxito!\n\nServicio: " + servicioNombre + "\nFecha y Hora: " + fechaHora + "\n\nSe ha enviado un correo de confirmación.",
                        "Reserva Exitosa", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(panelReserva,
                        "No se pudo guardar la cita. Verifica tu conexión a la base de datos.",
                        "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelReserva,
                    "Ocurrió un error inesperado al procesar la reserva:\n" + ex.getMessage(),
                    "Error del Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean enviarCorreoConfirmacion(String destinatario, String servicio, String fechaHora) {
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";
        final String miClaveDeCorreo = "rksu umvz hnom irzf"; 

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");

        Session sesionMail = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(miCorreoRemitente, miClaveDeCorreo);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesionMail);
            mensaje.setFrom(new InternetAddress(miCorreoRemitente, "NexusGO Reservas 🚀"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("✨ ¡Tu reserva en NexusGO ha sido confirmada! ✨");

            String cuerpoTexto = "¡Hola! 👋\n\n"
                    + "🎉 ¡Buenas noticias! Tu cita ha sido agendada con éxito en NexusGO.\n\n"
                    + "📍 === DETALLES DE TU RESERVA ===\n"
                    + "🛠️ Servicio: " + servicio + "\n"
                    + "📅 Fecha y Hora: " + fechaHora + "\n\n"
                    + "💡 Recuerda llegar con unos minutos de anticipación.\n\n"
                    + "✨ ¡Muchas gracias por confiar en nosotros! Nos alegra mucho atenderte.\n\n"
                    + "Atentamente,\nEl equipo de NexusGO 🚀";

            mensaje.setText(cuerpoTexto);
            Transport.send(mensaje);
            return true;

        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            return false;
        }
    }

    private void limpiarFormulario() {
        try {
            panelReserva.comboServicios.setSelectedIndex(0);
            if (panelReserva.txtObservaciones != null) panelReserva.txtObservaciones.setText("");
            
            panelReserva.datePickerFecha.setDate(LocalDate.now().plusDays(1));
            panelReserva.timePickerHora.clear();
            
            cargarFechasOcupadas(); 
        } catch (Exception e) {
            System.err.println("Error al limpiar el formulario: " + e.getMessage());
        }
    }

    private void cargarServicios() {
        try {
            panelReserva.comboServicios.removeAllItems();
            panelReserva.comboServicios.addItem("-- Seleccione un servicio --");

            List<String> servicios = citaDao.obtenerListaServicios();
            if (servicios != null) {
                for (String servicio : servicios) {
                    panelReserva.comboServicios.addItem(servicio);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar los servicios: " + e.getMessage());
        }
    }
    
    private void cargarFechasOcupadas() {
        if (panelReserva != null) {
            try {
                List<LocalDate> fechas = citaDao.obtenerFechasOcupadas();
                // Asegúrate de que este método exista en tu VistaReservarCitas
                panelReserva.marcarDiasOcupados(fechas);
            } catch (Exception e) {
                System.err.println("Error al cargar fechas ocupadas: " + e.getMessage());
            }
        }
    }
    
}
