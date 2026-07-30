/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import nexusgo.model.Cita;
import nexusgo.model.CitaDao;
import nexusgo.model.HerramientaDao;
import nexusgo.model.UsuarioDao;
import nexusgo.view.DialogDetalleModificarCita;
import nexusgo.view.PanelModificarCita;

/**
 *
 * @author HOME
 */
public class ControladorModificarCitas {

    private final PanelModificarCita vista;
    private final CitaDao citaDao;
    private final UsuarioDao usuarioDao;
    private final int idPeluqueroLogueado;
    private final ControladorPrincipalPeluquero controladorPrincipal;
    private final DateTimeFormatter formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter formatterHeader = DateTimeFormatter.ofPattern("dd/MM");

    public ControladorModificarCitas(PanelModificarCita vista, int idPeluquero, ControladorPrincipalPeluquero controladorPrincipal) {
        this.vista = vista;
        this.citaDao = new CitaDao();
        this.usuarioDao = new UsuarioDao();
        this.idPeluqueroLogueado = idPeluquero;
        this.controladorPrincipal = controladorPrincipal;

        inicializarListeners();
        cargarCitasSemana();
    }

    private void inicializarListeners() {
        vista.getTablaHorarios().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int fila = vista.getTablaHorarios().getSelectedRow();
                    int columna = vista.getTablaHorarios().getSelectedColumn();

                    if (columna > 0 && fila >= 0) {
                        LocalDate fechaBase = vista.getPickerFechaSemana().getDate() != null ? vista.getPickerFechaSemana().getDate() : LocalDate.now();
                        LocalDate domingo = fechaBase.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                        LocalDate fechaCelda = domingo.plusDays(columna - 1);

                        if (fechaCelda.isBefore(LocalDate.now())) {
                            JOptionPane.showMessageDialog(vista, "No se pueden gestionar ni modificar citas de fechas pasadas.", "Acción No Permitida", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        Object contenidoCelda = vista.getTablaHorarios().getValueAt(fila, columna);
                        if (contenidoCelda != null && !contenidoCelda.toString().trim().isEmpty()) {
                            gestionarSeleccionCita(contenidoCelda.toString(), fila, columna, fechaCelda);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "Error al seleccionar la celda: " + ex.getMessage(), "Error de Interfaz", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vista.getBtnActualizarSemana().addActionListener(e -> cargarCitasSemana());

        vista.getBtnCerrarSesion().addActionListener(e -> {
            if (controladorPrincipal != null) {
                controladorPrincipal.ejecutarCerrarSesion();
            }
        });
    }

    private void gestionarSeleccionCita(String infoCita, int fila, int columna, LocalDate fechaCelda) {
        try {
            if (infoCita.contains("[PENDIENTE]")) {
                procesarCitaPendiente(infoCita, fila, fechaCelda);
            } else {
                abrirVentanaModificacion(infoCita, fila, columna, fechaCelda);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al procesar la cita seleccionada: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarCitaPendiente(String infoCita, int fila, LocalDate fechaCelda) {
        final String prefijoFechaHora = fechaCelda.toString() + " " + String.format("%02d", fila + 6);
        String[] opciones = {"Aceptar Cita", "Rechazar Cita", "Cancelar"};

        int seleccion = JOptionPane.showOptionDialog(
                vista,
                "La siguiente cita está PENDIENTE de aprobación:\n\n" + infoCita + "\nFecha: " + fechaCelda + "\n\n¿Qué acción deseas realizar?",
                "Aprobación de Cita",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        // Aceptar Cita
        if (seleccion == 0) {
            SwingWorker<Boolean, Void> workerAceptar = new SwingWorker<>() {
                private String correoClienteTarget = null;
                private String servicioNombre = infoCita.replace("[PENDIENTE]", "").trim();
                private int idCitaAceptada = -1;

                @Override
                protected Boolean doInBackground() throws Exception {
                    // Reconstruir la fecha-hora exacta de la celda clickeada para localizar la cita activa
                    String strFechaHora = prefijoFechaHora;

                    // Obtener objeto cita o ID correspondiente
                    Cita citaActual = citaDao.obtenerCitaPorDetalles(idPeluqueroLogueado, strFechaHora);

                    boolean actualizado = false;
                    if (citaActual != null) {
                        actualizado = citaDao.actualizarEstadoCita(citaActual.getIdCita(), "CONFIRMADA");
                        if (actualizado) {
                            idCitaAceptada = citaActual.getIdCita();
                            if (citaActual.getIdCliente() > 0) {
                                correoClienteTarget = usuarioDao.obtenerCorreoPorUsuarioId(citaActual.getIdCliente());
                            }
                        }
                    } else {
                        // Respaldo de actualización general por coincidencia de horario
                        actualizado = citaDao.actualizarEstadoCitaPorHorario(idPeluqueroLogueado, strFechaHora, "CONFIRMADA");
                    }
                    return actualizado;
                }

                @Override
                protected void done() {
                    try {
                        boolean exito = get();
                        if (exito) {
                            if (correoClienteTarget != null && !correoClienteTarget.isEmpty()) {
                                final String destinatario = correoClienteTarget;
                                new Thread(() -> enviarCorreoConfirmacion(destinatario, servicioNombre, fechaCelda.toString())).start();
                            }
                            JOptionPane.showMessageDialog(vista, "Cita ACEPTADA exitosamente. Ahora la cita figura como CONFIRMADA.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            cargarCitasSemana();

                            // Requerimiento #6: al aceptar la cita se abre el checklist de
                            // herramientas para que el peluquero marque las que va a usar.
                            if (idCitaAceptada > 0) {
                                abrirChecklistHerramientas(idCitaAceptada, servicioNombre, fechaCelda.toString());
                            }
                        } else {
                            JOptionPane.showMessageDialog(vista, "No se pudo actualizar el estado de la cita en la base de datos.", "Error de Actualización", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vista, "Error al procesar la aceptación: " + ex.getMessage(), "Error de Proceso", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            workerAceptar.execute();

            // Rechazar Cita
        } else if (seleccion == 1) {
            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Deseas rechazar esta cita definitivamente?", "Confirmar Rechazo", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                SwingWorker<Boolean, Void> workerRechazar = new SwingWorker<>() {
                    private String correoClienteTarget = null;
                    private String servicioNombreRechazo = infoCita.replace("[PENDIENTE]", "").trim();
                    private int idCitaRechazada = -1;

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        String strFechaHora = prefijoFechaHora;
                        Cita citaActual = citaDao.obtenerCitaPorDetalles(idPeluqueroLogueado, strFechaHora);

                        boolean actualizado;
                        if (citaActual != null) {
                            actualizado = citaDao.actualizarEstadoCita(citaActual.getIdCita(), "CANCELADA");
                            if (actualizado) {
                                idCitaRechazada = citaActual.getIdCita();
                                if (citaActual.getIdCliente() > 0) {
                                    correoClienteTarget = usuarioDao.obtenerCorreoPorUsuarioId(citaActual.getIdCliente());
                                }
                            }
                        } else {
                            // Respaldo: si no se localizó la cita por ID exacto, se actualiza por horario.
                            actualizado = citaDao.actualizarEstadoCitaPorHorario(idPeluqueroLogueado, strFechaHora, "CANCELADA");
                        }
                        return actualizado;
                    }

                    @Override
                    protected void done() {
                        try {
                            if (get()) {
                                if (idCitaRechazada > 0) {
                                    // Si ya se habían reservado herramientas para esta cita, se liberan.
                                    new HerramientaDao().liberarHerramientasDeCita(idCitaRechazada);
                                }
                                if (correoClienteTarget != null && !correoClienteTarget.isEmpty()) {
                                    final String destinatario = correoClienteTarget;
                                    new Thread(() -> enviarCorreoRechazo(destinatario, servicioNombreRechazo, fechaCelda.toString())).start();
                                }
                                JOptionPane.showMessageDialog(vista, "La cita ha sido rechazada y marcada como CANCELADA.", "Cita Cancelada", JOptionPane.INFORMATION_MESSAGE);
                                cargarCitasSemana();
                            } else {
                                JOptionPane.showMessageDialog(vista, "No se pudo cancelar la cita en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(vista, "Error al rechazar la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                workerRechazar.execute();
            }
        }
    }

    /*Abre el diálogo de checklist de herramientas (requerimiento #6) justo
    después de que el peluquero acepta la cita.*/
    private void abrirChecklistHerramientas(int idCita, String servicio, String fecha) {
        java.awt.Window ventana = javax.swing.SwingUtilities.getWindowAncestor(vista);
        java.awt.Frame ventanaPadre = (ventana instanceof java.awt.Frame) ? (java.awt.Frame) ventana : null;

        String infoResumen = "Servicio: " + servicio + " | Fecha: " + fecha;
        ControladorChecklistHerramientas controladorChecklist
                = new ControladorChecklistHerramientas(ventanaPadre, idCita, infoResumen);
        controladorChecklist.mostrar();
    }

    private void abrirVentanaModificacion(String infoCita, int fila, int columna, LocalDate fechaCelda) {
        try {
            DialogDetalleModificarCita dialog = new DialogDetalleModificarCita(null, true);
            dialog.cargarDatosCita("Cliente Registrado", infoCita, 25000.0, fechaCelda.toString(), "08:00");
            dialog.getPickerNuevaFecha().setDate(fechaCelda);

            dialog.getBtnGuardarCambios().addActionListener(evt -> {
                try {
                    LocalDate nuevaFecha = dialog.getPickerNuevaFecha().getDate();
                    LocalTime nuevaHora = dialog.getPickerNuevaHora().getTime();

                    if (nuevaFecha == null || nuevaHora == null) {
                        JOptionPane.showMessageDialog(dialog, "Debe seleccionar la nueva fecha y hora.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    LocalDateTime nuevaFechaHoraCombined = LocalDateTime.of(nuevaFecha, nuevaHora);

                    if (nuevaFechaHoraCombined.isBefore(LocalDateTime.now())) {
                        JOptionPane.showMessageDialog(dialog, "No se puede reagendar a una fecha u hora pasada.", "Fecha Inválida", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String strFechaHoraSQL = nuevaFechaHoraCombined.format(formatterDB);
                    boolean horaOcupada = citaDao.existeCitaEnHorario(idPeluqueroLogueado, strFechaHoraSQL);

                    if (horaOcupada) {
                        JOptionPane.showMessageDialog(dialog, "El profesional ya tiene una cita ocupada en esa fecha y hora.", "Cruce de Horarios", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int respuesta = JOptionPane.showConfirmDialog(dialog, "¿Desea confirmar el cambio para " + nuevaFecha + " " + nuevaHora + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {

                        SwingWorker<Boolean, Void> workerModificar = new SwingWorker<>() {
                            private String correoClienteTarget = null;

                            @Override
                            protected Boolean doInBackground() throws Exception {
                                /* Igual que con las citas pendientes: se reconstruye la hora exacta
                                de la celda clickeada (fila = hora - 6) para no confundir esta cita
                                con otra del mismo profesional el mismo día.*/
                                String strFechaHoraOrigen = fechaCelda.toString() + " " + String.format("%02d", fila + 6);
                                Cita citaActual = citaDao.obtenerCitaPorDetalles(idPeluqueroLogueado, strFechaHoraOrigen);

                                if (citaActual != null) {
                                    boolean reordenado = citaDao.reagendarCita(citaActual.getIdCita(), strFechaHoraSQL);
                                    if (reordenado && citaActual.getIdCliente() > 0) {
                                        correoClienteTarget = usuarioDao.obtenerCorreoPorUsuarioId(citaActual.getIdCliente());
                                    }
                                    return reordenado;
                                }
                                return false;
                            }

                            @Override
                            protected void done() {
                                try {
                                    if (get()) {
                                        if (correoClienteTarget != null && !correoClienteTarget.isEmpty()) {
                                            final String correoDestino = correoClienteTarget;
                                            final String nuevaFechaHoraStr = nuevaFecha + " a las " + nuevaHora;
                                            new Thread(() -> enviarCorreoModificacion(correoDestino, infoCita, nuevaFechaHoraStr)).start();
                                        }

                                        JOptionPane.showMessageDialog(dialog, "Cita reagendada con éxito en la base de datos. Notificación enviada al cliente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                        dialog.dispose();
                                        cargarCitasSemana();
                                    } else {
                                        JOptionPane.showMessageDialog(dialog, "No se pudo actualizar el registro de la cita en la base de datos.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(dialog, "Error al reagendar la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        };
                        workerModificar.execute();
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error al validar la modificación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al abrir la ventana de modificación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCitasSemana() {
        try {
            LocalDate fechaSeleccionada = vista.getPickerFechaSemana().getDate();
            if (fechaSeleccionada == null) {
                fechaSeleccionada = LocalDate.now();
            }

            LocalDate domingo = fechaSeleccionada.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            LocalDate sabado = domingo.plusDays(6);

            actualizarCabeceraTabla(domingo);
            limpiarTablaHorarios();

            SwingWorker<List<Cita>, Void> workerCargar = new SwingWorker<>() {
                @Override
                protected List<Cita> doInBackground() throws Exception {
                    return citaDao.obtenerCitasSemanaPorProfesional(idPeluqueroLogueado, domingo.toString(), sabado.toString());
                }

                @Override
                protected void done() {
                    try {
                        List<Cita> listaCitas = get();
                        if (listaCitas == null) {
                            return;
                        }

                        for (Cita cita : listaCitas) {
                            try {
                                if (cita.getFechaHoraProgramada() == null || cita.getFechaHoraProgramada().isEmpty()) {
                                    continue;
                                }

                                LocalDateTime fechaHora = LocalDateTime.parse(cita.getFechaHoraProgramada(), formatterDB);

                                int columnaDia = (fechaHora.getDayOfWeek() == DayOfWeek.SUNDAY) ? 1 : fechaHora.getDayOfWeek().getValue() + 1;
                                int filaHora = fechaHora.getHour() - 6;

                                if (columnaDia >= 1 && columnaDia <= 7 && filaHora >= 0 && filaHora < vista.getTablaHorarios().getRowCount()) {
                                    String prefijoEstado = "PENDIENTE".equalsIgnoreCase(cita.getEstado()) ? "[PENDIENTE] " : "";
                                    String contenido = prefijoEstado + cita.getNombreServicio() + " - " + cita.getNombreCliente();
                                    vista.getTablaHorarios().setValueAt(contenido, filaHora, columnaDia);
                                }
                            } catch (DateTimeParseException e) {
                                System.err.println("Error al parsear fecha de cita ID " + cita.getIdCita() + ": " + e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error procesando la lista de citas: " + e.getMessage());
                    }
                }
            };
            workerCargar.execute();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar las citas de la semana: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCabeceraTabla(LocalDate domingo) {
        try {
            String[] diasSemana = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
            TableColumnModel modelCol = vista.getTablaHorarios().getColumnModel();

            for (int i = 0; i < 7; i++) {
                LocalDate diaActual = domingo.plusDays(i);
                String tituloColumna = diasSemana[i] + " " + diaActual.format(formatterHeader);
                modelCol.getColumn(i + 1).setHeaderValue(tituloColumna);
            }

            JTableHeader header = vista.getTablaHorarios().getTableHeader();
            if (header != null) {
                header.repaint();
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar la cabecera de fechas: " + e.getMessage());
        }
    }

    private void limpiarTablaHorarios() {
        try {
            DefaultTableModel model = vista.getModeloTabla();
            if (model == null) {
                return;
            }
            for (int f = 0; f < model.getRowCount(); f++) {
                for (int c = 1; c < model.getColumnCount(); c++) {
                    model.setValueAt("", f, c);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al limpiar el modelo de la tabla: " + e.getMessage());
        }
    }

    private boolean enviarCorreoConfirmacion(String destinatario, String servicio, String fechaHora) {
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";
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

        try {
            Message mensaje = new MimeMessage(sesionMail);
            mensaje.setFrom(new InternetAddress(miCorreoRemitente, "NexusGO Agenda 🚀"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("✅ ¡Tu cita en NexusGO ha sido ACEPTADA!");

            String cuerpoTexto = "¡Hola! 👋✨\n\n"
                    + "🎉 ¡Excelentes noticias! Tu cita ha sido ACEPTADA y confirmada por tu especialista.\n\n"
                    + "📌 === DETALLES DE TU RESERVA ===\n"
                    + "✂️ Servicio: " + servicio + "\n"
                    + "🗓️ Fecha y hora confirmada: " + fechaHora + "\n\n"
                    + "📋 === RECOMENDACIONES PARA TU VISITA ===\n"
                    + "⏰ Te aconsejamos llegar entre 5 y 10 minutos antes para brindarte la mejor experiencia.\n"
                    + "📱 Si surge algún imprevisto, puedes gestionar o modificar tu cita comunicandote con nuestro personal.\n\n"
                    + "🔥 ¡Todo está listo para recibirte y ofrecerte un servicio de primera!\n\n"
                    + "¡Te esperamos! 💈✨\n\n"
                    + "Atentamente\nEl equipo de NexusGO 🚀";

            mensaje.setText(cuerpoTexto);
            Transport.send(mensaje);
            System.out.println("Correo de confirmación enviado exitosamente a: " + destinatario);
            return true;
        } catch (Exception e) {
            System.err.println("Error crítico al enviar el correo a " + destinatario + ": " + e.getMessage());
            return false;
        }
    }

    /*Correo enviado al cliente cuando el peluquero RECHAZA su cita pendiente
    (parte del "else" de notificación del requerimiento #5).*/
    private boolean enviarCorreoRechazo(String destinatario, String servicio, String fechaHora) {
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";
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

        try {
            Message mensaje = new MimeMessage(sesionMail);
            mensaje.setFrom(new InternetAddress(miCorreoRemitente, "NexusGO Agenda 🚀"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("❌ Tu cita en NexusGO ha sido rechazada");

            String cuerpoTexto = "¡Hola! 👋\n\n"
                    + "Lamentamos informarte que, debido a un inconveniente personal del estilista, no podremos atender tu cita programada.\n\n"
                    + "📍 === DETALLES DE LA SOLICITUD ===\n"
                    + "🛠️ Servicio: " + servicio + "\n"
                    + "📅 Fecha y Hora solicitada: " + fechaHora + "\n\n"
                    + "😔 Pedimos disculpas por los inconvenientes causados. Te invitamos a agendar nuevamente en otro horario disponible o con otro especialista.\n\n"
                    + "Atentamente,\nEl equipo de NexusGO 🚀";

            mensaje.setText(cuerpoTexto);
            Transport.send(mensaje);
            System.out.println("Correo de rechazo enviado exitosamente a: " + destinatario);
            return true;
        } catch (Exception e) {
            System.err.println("Error al enviar el correo de rechazo a " + destinatario + ": " + e.getMessage());
            return false;
        }
    }

    private boolean enviarCorreoModificacion(String destinatario, String servicio, String nuevaFechaHora) {
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";
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

        try {
            Message mensaje = new MimeMessage(sesionMail);
            mensaje.setFrom(new InternetAddress(miCorreoRemitente, "NexusGO Agenda 🚀"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("🗓️ ¡Tu cita en NexusGO ha sido modificada!");

            String cuerpoTexto = "¡Hola! 👋✨\n\n"
                    + "🎉 ¡Buenas noticias! Tu cita ha sido modificada/reagendada con éxito.\n\n"
                    + "📌 === NUEVOS DETALLES DE TU RESERVA ===\n"
                    + "✂️ Servicio: " + servicio + "\n"
                    + "🗓️ Nueva fecha y hora: " + nuevaFechaHora + "\n\n"
                    + "📋 === INFORMACIÓN IMPORTANTE ===\n"
                    + "⏰ Te recomendamos llegar 5 a 10 minutos antes de tu hora programada.\n"
                    + "🔔 Si necesitas realizar algún otro cambio, por favor avísanos con anticipación desde la app.\n\n"
                    + "🙌 Agradecemos mucho tu flexibilidad y comprensión. ¡Estamos listos para darte la mejor atención!\n\n"
                    + "¡Nos vemos pronto! 💈✨\n\n"
                    + "Atentamente\nEl equipo de NexusGO 🚀";

            mensaje.setText(cuerpoTexto);
            Transport.send(mensaje);
            System.out.println("Correo de modificación enviado exitosamente a: " + destinatario);
            return true;
        } catch (Exception e) {
            System.err.println("Error crítico al enviar el correo a " + destinatario + ": " + e.getMessage());
            return false;
        }
    }
}
