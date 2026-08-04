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
    private boolean cargandoDatos = false;

    public ControladorReservarCita(VistaReservarCitas panelReserva, VistaPrincipalCliente vistaPrincipal, int idUsuarioLogueado) {
        this.panelReserva = panelReserva;
        this.vistaPrincipal = vistaPrincipal;
        this.idUsuarioLogueado = idUsuarioLogueado;
        this.citaDao = new CitaDao();

        inicializarEventos();
        cargarServiciosYProfesionales();
    }

    private void inicializarEventos() {
        if (this.panelReserva != null && this.panelReserva.btnAgendar != null) {
            this.panelReserva.btnAgendar.addActionListener(this);
        }

        if (this.panelReserva != null && this.panelReserva.datePickerFecha != null) {
            this.panelReserva.datePickerFecha.addDateChangeListener(evento -> {
                if (!cargandoDatos) {
                    this.panelReserva.limpiarSeleccionHora();
                    actualizarSlotsDisponibles();
                }
            });
        }

        if (this.panelReserva != null && this.panelReserva.comboServicios != null) {
            this.panelReserva.comboServicios.addItemListener(evento -> {
                if (evento.getStateChange() == ItemEvent.SELECTED && !cargandoDatos) {
                    this.panelReserva.limpiarSeleccionHora();
                    actualizarSlotsDisponibles();
                }
            });
        }

        // Evento para reaccionar al cambio de profesional en el ComboBox
        if (this.panelReserva != null && this.panelReserva.comboProfesionales != null) {
            this.panelReserva.comboProfesionales.addItemListener(evento -> {
                if (evento.getStateChange() == ItemEvent.SELECTED && !cargandoDatos) {
                    this.panelReserva.limpiarSeleccionHora();
                    actualizarSlotsDisponibles();
                }
            });
        }
    }

    private void actualizarSlotsDisponibles() {
        if (panelReserva == null || panelReserva.comboServicios == null || panelReserva.comboProfesionales == null || panelReserva.datePickerFecha == null) {
            return;
        }

        if (panelReserva.comboServicios.getSelectedIndex() <= 0) {
            panelReserva.mostrarSlots(null, "Seleccione un servicio para ver las horas disponibles.");
            return;
        }

        if (panelReserva.comboProfesionales.getSelectedIndex() <= 0) {
            panelReserva.mostrarSlots(null, "Seleccione un profesional para ver las horas disponibles.");
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
        String profesionalNombre = (String) panelReserva.comboProfesionales.getSelectedItem();

        // SwingWorker para ejecutar la consulta SQL y cálculo de slots en segundo plano
        SwingWorker<List<HorarioNegocio.SlotDisponibilidad>, Void> worker = new SwingWorker<>() {
            private String mensajeUi = "";

            @Override
            protected List<HorarioNegocio.SlotDisponibilidad> doInBackground() throws Exception {
                int duracionServicio = citaDao.obtenerDuracionServicioPorNombre(servicioNombre);
                if (duracionServicio <= 0) {
                    duracionServicio = 30;
                }

                // ID obtenido dinámicamente según el profesional seleccionado
                int idProfesional = citaDao.obtenerIdProfesionalPorNombre(profesionalNombre);

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
                    mensajeUi += " — No quedan horas disponibles con este profesional. Por favor elige otro o cambia la fecha.";
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

            if (panelReserva.comboProfesionales.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(panelReserva, "Por favor, seleccione un profesional.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(panelReserva, "Por favor, seleccione una hora disponible en la grilla.", "Hora Requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String fechaHora = panelReserva.getFechaHoraFormateada();
            if (fechaHora == null || fechaHora.trim().isEmpty()) {
                JOptionPane.showMessageDialog(panelReserva, "Por favor, asegúrese de seleccionar tanto la fecha como la hora.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String servicioNombre = (String) panelReserva.comboServicios.getSelectedItem();
            String profesionalNombre = (String) panelReserva.comboProfesionales.getSelectedItem();

            // Captura de las observaciones ingresadas por el cliente
            String observaciones = (panelReserva.txtObservaciones != null) ? panelReserva.txtObservaciones.getText().trim() : "";

            // SwingWorker para validar y registrar la cita sin congelar la interfaz
            SwingWorker<Boolean, Void> workerReserva = new SwingWorker<>() {
                private boolean choqueHorario = false;

                @Override
                protected Boolean doInBackground() throws Exception {
                    int idProfesional = citaDao.obtenerIdProfesionalPorNombre(profesionalNombre);

                    if (idProfesional == -1) {
                        return false;
                    }

                    if (citaDao.existeCitaEnHorario(idProfesional, fechaHora)) {
                        choqueHorario = true;
                        return false;
                    }

                    int idServicio = citaDao.obtenerIdServicioPorNombre(servicioNombre);
                    if (idServicio == -1) {
                        idServicio = 1;
                    }

                    Cita nuevaCita = new Cita(idUsuarioLogueado, idProfesional, idServicio, fechaHora);
                    return citaDao.agendarCita(nuevaCita);
                }

                @Override
                protected void done() {
                    try {
                        boolean exito = get();
                        if (choqueHorario) {
                            JOptionPane.showMessageDialog(panelReserva,
                                    "El horario seleccionado (" + fechaHora + ") ya fue ocupado para " + profesionalNombre + ".\nPor favor, elige otra hora u otro profesional.",
                                    "Horario No Disponible", JOptionPane.ERROR_MESSAGE);

                            panelReserva.limpiarSeleccionHora();
                            actualizarSlotsDisponibles();
                            return;
                        }

                        if (exito) {
                            // Hilo independiente para envío de correos (Cliente + Profesional)
                            new Thread(() -> {
                                UsuarioDao usuarioDao = new UsuarioDao();

                                // 1. Obtener datos del Cliente
                                Usuario clienteActual = usuarioDao.obtenerPorId(idUsuarioLogueado);
                                String correoCliente = (clienteActual != null) ? clienteActual.getCorreo() : null;
                                String nombreCliente = (clienteActual != null) ? clienteActual.getNombre() : "Cliente";

                                // Enviar correo al cliente
                                if (correoCliente != null && !correoCliente.trim().isEmpty()) {
                                    enviarCorreoConfirmacion(correoCliente, servicioNombre, profesionalNombre, fechaHora, observaciones);
                                } else {
                                    System.err.println("⚠️ [Advertencia] No se encontró correo para el cliente ID: " + idUsuarioLogueado);
                                }

                                // 2. Obtener datos del Profesional
                                int idProfesional = citaDao.obtenerIdProfesionalPorNombre(profesionalNombre);
                                Usuario profesionalActual = usuarioDao.obtenerPorId(idProfesional);
                                String correoProfesional = (profesionalActual != null) ? profesionalActual.getCorreo() : null;

                                // Enviar correo al profesional
                                if (correoProfesional != null && !correoProfesional.trim().isEmpty()) {
                                    enviarCorreoProfesional(correoProfesional, nombreCliente, servicioNombre, fechaHora, observaciones);
                                } else {
                                    System.err.println("⚠️ [Advertencia] No se encontró correo para el profesional: " + profesionalNombre + " (ID: " + idProfesional + ")");
                                }

                            }).start();

                            JOptionPane.showMessageDialog(panelReserva,
                                    "¡Cita agendada con éxito!\n\nServicio: " + servicioNombre + "\nProfesional: " + profesionalNombre + "\nFecha y Hora: " + fechaHora + "\n\nSe han enviado las notificaciones por correo electrónico.",
                                    "Reserva Exitosa", JOptionPane.INFORMATION_MESSAGE);

                            limpiarFormulario();
                        } else {
                            JOptionPane.showMessageDialog(panelReserva, "No se pudo guardar la cita. Verifica que el profesional existe y tu conexión a la base de datos.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
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

    // Correo enviado al CLIENTE
    private boolean enviarCorreoConfirmacion(String destinatario, String servicio, String profesional, String fechaHora, String observaciones) {
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";
        final String miClaveDeCorreo = "rksuumvzhnomirzf";

        Properties propiedades = crearPropiedadesSmtp();

        Session sesionMail = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(miCorreoRemitente, miClaveDeCorreo);
            }
        });

        sesionMail.setDebug(false);

        try {
            Message mensaje = new MimeMessage(sesionMail);
            mensaje.setFrom(new InternetAddress(miCorreoRemitente, "NexusGO Reservas 🚀"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("✨ ¡Tu reserva en NexusGO ha sido confirmada! ✨");

            String textoNotas = observaciones.isEmpty() ? "Sin observaciones específicas." : observaciones;

            String cuerpoTexto = "¡Hola! 👋\n\n"
                    + "🎉 ¡Buenas noticias! Tu cita ha sido agendada con éxito en NexusGO.\n\n"
                    + "📍 === DETALLES DE TU RESERVA ===\n"
                    + "🛠️ Servicio: " + servicio + "\n"
                    + "💈 Profesional: " + profesional + "\n"
                    + "📅 Fecha y Hora: " + fechaHora + "\n"
                    + "📝 Tus Observaciones: " + textoNotas + "\n\n"
                    + "💡 Recuerda llegar con unos minutos de anticipación.\n\n"
                    + "✨ ¡Muchas gracias por confiar en nosotros! Nos alegra mucho atenderte.\n\n"
                    + "Atentamente,\nEl equipo de NexusGO 🚀";

            mensaje.setText(cuerpoTexto);
            Transport.send(mensaje);
            System.out.println("✅ Correo de confirmación enviado al cliente: " + destinatario);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error crítico al enviar el correo al cliente " + destinatario + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Correo enviado al PROFESIONAL / PELUQUERO
    private boolean enviarCorreoProfesional(String destinatario, String clienteNombre, String servicio, String fechaHora, String observaciones) {
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";
        final String miClaveDeCorreo = "rksuumvzhnomirzf";

        Properties propiedades = crearPropiedadesSmtp();

        Session sesionMail = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(miCorreoRemitente, miClaveDeCorreo);
            }
        });

        sesionMail.setDebug(false);

        try {
            Message mensaje = new MimeMessage(sesionMail);
            mensaje.setFrom(new InternetAddress(miCorreoRemitente, "NexusGO Notificaciones 💈"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("🗓️ ¡Nueva cita asignada en NexusGO!");

            String textoNotas = observaciones.isEmpty() ? "El cliente no dejó notas adicionales." : observaciones;

            String cuerpoTexto = "¡Hola! 💈\n\n"
                    + "Se ha agendado una nueva cita asignada a tu agenda en NexusGO:\n\n"
                    + "📍 === DETALLES DEL CLIENTE Y RESERVA ===\n"
                    + "👤 Cliente: " + clienteNombre + "\n"
                    + "🛠️ Servicio: " + servicio + "\n"
                    + "📅 Fecha y Hora: " + fechaHora + "\n"
                    + "📝 Indicaciones / Recomendaciones del cliente: " + textoNotas + "\n\n"
                    + "Por favor asegúrate de estar disponible para atender al cliente a la hora estipulada.\n\n"
                    + "Atentamente,\nSistema NexusGO 🚀";

            mensaje.setText(cuerpoTexto);
            Transport.send(mensaje);
            System.out.println("✅ Correo de notificación enviado al profesional: " + destinatario);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error crítico al enviar correo al profesional " + destinatario + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Configuración compartida de parámetros SMTP
    private Properties crearPropiedadesSmtp() {
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.starttls.required", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return propiedades;
    }

    private void limpiarFormulario() {
        try {
            cargandoDatos = true;
            panelReserva.comboServicios.setSelectedIndex(0);
            panelReserva.comboProfesionales.setSelectedIndex(0);
            if (panelReserva.txtObservaciones != null) {
                panelReserva.txtObservaciones.setText("");
            }
            panelReserva.datePickerFecha.setDate(LocalDate.now());
            panelReserva.limpiarSeleccionHora();
        } catch (Exception e) {
            System.err.println("Error al limpiar el formulario: " + e.getMessage());
        } finally {
            cargandoDatos = false;
            actualizarSlotsDisponibles();
        }
    }

    private void cargarServiciosYProfesionales() {
        cargandoDatos = true;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private List<String> servicios;
            private List<String> profesionales;

            @Override
            protected Void doInBackground() throws Exception {
                servicios = citaDao.obtenerListaServicios();
                profesionales = citaDao.obtenerListaProfesionales();
                return null;
            }

            @Override
            protected void done() {
                try {
                    // Carga del ComboBox de Servicios
                    panelReserva.comboServicios.removeAllItems();
                    panelReserva.comboServicios.addItem("-- Seleccione un servicio --");
                    if (servicios != null) {
                        for (String servicio : servicios) {
                            panelReserva.comboServicios.addItem(servicio);
                        }
                    }

                    // Carga del ComboBox de Profesionales
                    panelReserva.comboProfesionales.removeAllItems();
                    panelReserva.comboProfesionales.addItem("-- Seleccione un profesional --");
                    if (profesionales != null) {
                        for (String profesional : profesionales) {
                            panelReserva.comboProfesionales.addItem(profesional);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar los desplegables: " + e.getMessage());
                } finally {
                    cargandoDatos = false;
                    actualizarSlotsDisponibles();
                }
            }
        };
        worker.execute();
    }
}
