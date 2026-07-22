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
import java.util.Calendar;
import java.util.Date;
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

    public ControladorReservarCita(VistaReservarCitas panelReserva, VistaPrincipalCliente vistaPrincipal, int idUsuarioLogueado) {
        this.panelReserva = panelReserva;
        this.vistaPrincipal = vistaPrincipal;
        this.idUsuarioLogueado = idUsuarioLogueado;
        this.citaDao = new CitaDao();

        inicializarEventos();
        cargarServicios();
    }

    private void inicializarEventos() {
        if (this.panelReserva != null && this.panelReserva.btnAgendar != null) {
            this.panelReserva.btnAgendar.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelReserva.btnAgendar) {
            System.out.println("-> Clic detectado en ControladorReservarCita!"); 
            procesarReserva();
        }
    }

    private void procesarReserva() {
        // 1. Validar Selección de Servicio
        if (panelReserva.comboServicios.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(panelReserva,
                    "Por favor, seleccione un tipo de servicio.",
                    "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validar Selección del DateChooser
        Date fechaSeleccionada = panelReserva.dateChooserFecha.getDate();
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(panelReserva,
                    "Por favor, seleccione una fecha válida.",
                    "Fecha Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Validar Cadena de Fecha y Hora Formateada
        String fechaHora = panelReserva.getFechaHoraFormateada();
        if (fechaHora == null || fechaHora.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panelReserva,
                    "Por favor, seleccione una fecha y hora válidas.",
                    "Fecha Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 4. Validar Fecha: BLOQUEAR HOY Y FECHAS PASADAS (Solo permite desde mañana)
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

        // compareTo <= 0 bloquea exactamente la fecha de HOY y días anteriores
        if (calSeleccionada.compareTo(calHoy) <= 0) {
            JOptionPane.showMessageDialog(panelReserva,
                    "No se pueden agendar citas para el día de hoy ni para fechas pasadas.\n"
                    + "Por favor, seleccione una fecha a partir de mañana.",
                    "Fecha No Válida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 5. Verificar disponibilidad en la base de datos
        if (citaDao.existeCitaEnHorario(fechaHora)) {
            JOptionPane.showMessageDialog(panelReserva,
                    "El horario seleccionado (" + fechaHora + ") ya se encuentra ocupado.\n"
                    + "Por favor, elige otra hora.",
                    "Horario No Disponible", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 6. Mapear datos necesarios
        String servicioNombre = (String) panelReserva.comboServicios.getSelectedItem();
        int idServicio = citaDao.obtenerIdServicioPorNombre(servicioNombre);
        if (idServicio == -1) {
            idServicio = 1; // Respaldo por defecto
        }

        int idProfesional = citaDao.obtenerIdProfesionalPorDefecto();

        // 7. Instanciar objeto Cita
        Cita nuevaCita = new Cita(this.idUsuarioLogueado, idProfesional, idServicio, fechaHora);

        // 8. Guardar en la base de datos
        if (citaDao.agendarCita(nuevaCita)) {

            // Buscar correo del usuario para notificación
            String correoCliente = citaDao.obtenerCorreoPorUsuarioId(this.idUsuarioLogueado);

            if (correoCliente != null && !correoCliente.trim().isEmpty()) {
                // Se envía en un hilo separado para no congelar la UI de Swing
                new Thread(() -> {
                    enviarCorreoConfirmacion(correoCliente, servicioNombre, fechaHora);
                }).start();
            }

            JOptionPane.showMessageDialog(panelReserva,
                    "¡Cita agendada con éxito!\n\n"
                    + "Servicio: " + servicioNombre + "\n"
                    + "Fecha y Hora: " + fechaHora + "\n\n"
                    + "Se ha enviado un correo de confirmación.",
                    "Reserva Exitosa", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(panelReserva,
                    "Ocurrió un error al guardar la cita en la base de datos.",
                    "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método interno para enviar el correo de confirmación de cita por SMTP
     */
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
            mensaje.setSubject("✨ ¡Tu reserva en NexusGO ha sido confirmada!✨");

            // Mensaje estilizado con emojis y agradecimiento
            String cuerpoTexto = "¡Hola! 👋\n\n"
                    + "🎉 ¡Buenas noticias! Tu cita ha sido agendada con éxito en NexusGO.\n\n"
                    + "📍 === DETALLES DE TU RESERVA ===\n"
                    + "🛠️ Servicio: " + servicio + "\n"
                    + "📅 Fecha y Hora: " + fechaHora + "\n\n"
                    + "💡 Recuerda llegar con unos minutos de anticipación.\n\n"
                    + "✨ ¡Muchas gracias por confiar en nosotros! Nos alegra mucho atenderte.\n\n"
                    + "Atentamente,\n"
                    + "El equipo de NexusGO 🚀";

            mensaje.setText(cuerpoTexto);

            Transport.send(mensaje);
            return true;

        } catch (Exception e) {
            System.err.println("Error al enviar el correo de confirmación de cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void limpiarFormulario() {
        panelReserva.comboServicios.setSelectedIndex(0);
        panelReserva.txtObservaciones.setText("");
        
        // Dejar el DateChooser apuntando a mañana tras limpiar
        Calendar manana = Calendar.getInstance();
        manana.add(Calendar.DAY_OF_MONTH, 1);
        panelReserva.dateChooserFecha.setDate(manana.getTime());
    }

    private void cargarServicios() {
        panelReserva.comboServicios.removeAllItems();
        panelReserva.comboServicios.addItem("-- Seleccione un servicio --");

        List<String> servicios = citaDao.obtenerListaServicios();

        if (servicios != null) {
            for (String servicio : servicios) {
                panelReserva.comboServicios.addItem(servicio);
            }
        }
    }
    
}
