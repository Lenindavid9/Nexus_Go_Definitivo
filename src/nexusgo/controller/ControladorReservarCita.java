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
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import nexusgo.model.Cita;
import nexusgo.model.CitaDao;
import nexusgo.model.HorarioNegocio;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
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
    private boolean cargandoServicios = false;

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

        if (this.panelReserva != null && this.panelReserva.datePickerFecha != null) {
            this.panelReserva.datePickerFecha.addDateChangeListener(evento -> {
                if (!cargandoServicios) {
                    this.panelReserva.limpiarSeleccionHora();
                    actualizarSlotsDisponibles();
                }
            });
        }

        if (this.panelReserva != null && this.panelReserva.comboServicios != null) {
            this.panelReserva.comboServicios.addItemListener(evento -> {
                if (evento.getStateChange() == ItemEvent.SELECTED && !cargandoServicios) {
                    this.panelReserva.limpiarSeleccionHora();
                    actualizarSlotsDisponibles();
                }
            });
        }
    }

    private void actualizarSlotsDisponibles() {
        if (panelReserva == null || panelReserva.comboServicios == null || panelReserva.datePickerFecha == null) {
            return;
        }

        if (panelReserva.comboServicios.getSelectedIndex() <= 0) {
            panelReserva.mostrarSlots(null, "Seleccione un servicio para ver las horas disponibles.");
            return;
        }

        LocalDate fecha = panelReserva.datePickerFecha.getDate();
        if (fecha == null) {
            panelReserva.mostrarSlots(null, "Seleccione una fecha para ver las horas disponibles.");
            return;
        }

        if (fecha.isBefore(LocalDate.now())) {
            panelReserva.mostrarSlots(null, "No se pueden consultar fechas pasadas.");
            return;
        }

        String servicioNombre = (String) panelReserva.comboServicios.getSelectedItem();

        // SwingWorker para ejecutar la consulta SQL y cálculo de slots en segundo plano
        SwingWorker<List<HorarioNegocio.SlotDisponibilidad>, Void> worker = new SwingWorker<>() {
            private String mensajeUi = "";

            @Override
            protected List<HorarioNegocio.SlotDisponibilidad> doInBackground() throws Exception {
                int duracionServicio = citaDao.obtenerDuracionServicioPorNombre(servicioNombre);
                // Validación de seguridad para evitar divisiones o bucles infinitos
                if (duracionServicio <= 0) {
                    duracionServicio = 30;
                }

                int idProfesional = citaDao.obtenerIdProfesionalPorDefecto();
                List<HorarioNegocio.RangoOcupado> rangosOcupados = citaDao.obtenerRangosOcupados(idProfesional, fecha);
                List<HorarioNegocio.SlotDisponibilidad> slots = HorarioNegocio.generarSlotsDelDia(fecha, duracionServicio, rangosOcupados);

                // Deshabilitar slots de horas pasadas si es la fecha actual
                if (fecha.equals(LocalDate.now())) {
                    LocalTime horaActual = LocalTime.now();
                    for (HorarioNegocio.SlotDisponibilidad slot : slots) {
                        if (slot.hora != null && slot.hora.isBefore(horaActual)) {
                            slot.disponible = false;
                        }
                    }
                }

                mensajeUi = HorarioNegocio.esFinDeSemanaOFestivo(fecha)
                        ? "Horario de fin de semana: 6:00 a. m. a 11:00 p. m."
                        : "Horario entre semana: 6:00 a. m. a 9:00 p. m.";

                boolean hayDisponibles = slots.stream().anyMatch(s -> s.disponible);
                if (!hayDisponibles) {
                    mensajeUi += " — No quedan horas disponibles este día, por favor elige otra fecha.";
                }

                return slots;
            }

            @Override
            protected void done() {
                try {
                    List<HorarioNegocio.SlotDisponibilidad> slots = get();
                    panelReserva.mostrarSlots(slots, mensajeUi);
                } catch (Exception e) {
                    System.err.println("Error al calcular las horas disponibles: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panelReserva.btnAgendar) {
            procesarReserva();
        }
    }

    private void procesarReserva() {
        try {
            if (panelReserva.comboServicios.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(panelReserva, "Por favor, seleccione un tipo de servicio.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate fechaSeleccionada = panelReserva.datePickerFecha.getDate();
            if (fechaSeleccionada == null) {
                JOptionPane.showMessageDialog(panelReserva, "Por favor, seleccione una fecha válida.", "Fecha Requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (fechaSeleccionada.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(panelReserva, "No se pueden agendar citas en fechas pasadas.", "Fecha No Válida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (panelReserva.getHoraSeleccionada() == null) {
                JOptionPane.showMessageDialog(panelReserva, "Por favor, seleccione una hora disponible (en verde) en la grilla de horarios.", "Hora Requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String fechaHora = panelReserva.getFechaHoraFormateada();
            if (fechaHora == null || fechaHora.trim().isEmpty()) {
                JOptionPane.showMessageDialog(panelReserva, "Por favor, asegúrese de seleccionar tanto la fecha como la hora.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String servicioNombre = (String) panelReserva.comboServicios.getSelectedItem();

            // SwingWorker para validar y registrar la cita sin congelar la interfaz
            SwingWorker<Boolean, Void> workerReserva = new SwingWorker<>() {
                private boolean choqueHorario = false;

                @Override
                protected Boolean doInBackground() throws Exception {
                    if (citaDao.existeCitaEnHorario(fechaHora)) {
                        choqueHorario = true;
                        return false;
                    }

                    int idServicio = citaDao.obtenerIdServicioPorNombre(servicioNombre);
                    if (idServicio == -1) {
                        idServicio = 1;
                    }

                    int idProfesional = citaDao.obtenerIdProfesionalPorDefecto();
                    Cita nuevaCita = new Cita(idUsuarioLogueado, idProfesional, idServicio, fechaHora);

                    return citaDao.agendarCita(nuevaCita);
                }

                @Override
                protected void done() {
                    try {
                        boolean exito = get();
                        if (choqueHorario) {
                            JOptionPane.showMessageDialog(panelReserva,
                                    "El horario seleccionado (" + fechaHora + ") acaba de ser ocupado.\nPor favor, elige otra hora u otro día.",
                                    "Horario No Disponible", JOptionPane.ERROR_MESSAGE);

                            panelReserva.limpiarSeleccionHora();
                            actualizarSlotsDisponibles();
                            return;
                        }

                        if (exito) {
                            // Enviar correo en un hilo independiente consultando los datos mediante UsuarioDao
                            new Thread(() -> {
                                System.out.println("🔍 [Depuración] ID Usuario Logueado recibido: " + idUsuarioLogueado);

                                UsuarioDao usuarioDao = new UsuarioDao();
                                Usuario usuarioActual = usuarioDao.obtenerPorId(idUsuarioLogueado);
                                String correoCliente = (usuarioActual != null) ? usuarioActual.getCorreo() : null;

                                System.out.println("🔍 [Depuración] Correo recuperado de la BD: " + correoCliente);

                                if (correoCliente != null && !correoCliente.trim().isEmpty()) {
                                    enviarCorreoConfirmacion(correoCliente, servicioNombre, fechaHora);
                                } else {
                                    System.err.println("⚠️ [Advertencia] No se encontró correo electrónico para el ID de usuario: " + idUsuarioLogueado);
                                }
                            }).start();

                            JOptionPane.showMessageDialog(panelReserva,
                                    "¡Cita agendada con éxito!\n\nServicio: " + servicioNombre + "\nFecha y Hora: " + fechaHora + "\n\nSe ha enviado un correo de confirmación.",
                                    "Reserva Exitosa", JOptionPane.INFORMATION_MESSAGE);

                            limpiarFormulario();
                        } else {
                            JOptionPane.showMessageDialog(panelReserva, "No se pudo guardar la cita. Verifica tu conexión a la base de datos.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panelReserva, "Ocurrió un error inesperado al procesar la reserva:\n" + ex.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            };

            workerReserva.execute();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelReserva, "Ocurrió un error inesperado al validar los campos:\n" + ex.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean enviarCorreoConfirmacion(String destinatario, String servicio, String fechaHora) {
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";
        // REMPLAZAR POR TU CONTRASEÑA DE APLICACIÓN DE GOOGLE (SIN ESPACIOS)
        final String miClaveDeCorreo = "rksuumvzhnomirzf";

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.starttls.required", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session sesionMail = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(miCorreoRemitente, miClaveDeCorreo);
            }
        });

        // Habilita los logs de SMTP en la consola para diagnóstico
        sesionMail.setDebug(true);

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
            System.out.println("✅ Correo enviado exitosamente a: " + destinatario);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error crítico al enviar el correo a " + destinatario + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void limpiarFormulario() {
        try {
            cargandoServicios = true;
            panelReserva.comboServicios.setSelectedIndex(0);
            if (panelReserva.txtObservaciones != null) {
                panelReserva.txtObservaciones.setText("");
            }
            panelReserva.datePickerFecha.setDate(LocalDate.now());
            panelReserva.limpiarSeleccionHora();
        } catch (Exception e) {
            System.err.println("Error al limpiar el formulario: " + e.getMessage());
        } finally {
            cargandoServicios = false;
            actualizarSlotsDisponibles();
        }
    }

    private void cargarServicios() {
        cargandoServicios = true;
        SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                return citaDao.obtenerListaServicios();
            }

            @Override
            protected void done() {
                try {
                    panelReserva.comboServicios.removeAllItems();
                    panelReserva.comboServicios.addItem("-- Seleccione un servicio --");

                    List<String> servicios = get();
                    if (servicios != null) {
                        for (String servicio : servicios) {
                            panelReserva.comboServicios.addItem(servicio);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar los servicios: " + e.getMessage());
                } finally {
                    cargandoServicios = false;
                    actualizarSlotsDisponibles();
                }
            }
        };
        worker.execute();
    }

}
