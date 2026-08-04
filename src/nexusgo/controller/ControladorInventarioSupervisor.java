/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.Herramientas;
import nexusgo.model.HerramientaDao;
import nexusgo.model.Mantenimiento;
import nexusgo.model.MantenimientoDao;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.Usuario;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaInventarioSupervisor;
import nexusgo.view.VistaPrincipalSupervisor;
import nexusgo.view.VistaProgramarMantenimiento;
import nexusgo.view.VistaRealizacionMantenimiento;

/**
 *
 * @author USUARIO
 */
public class ControladorInventarioSupervisor implements ActionListener {

    private final VistaPrincipalSupervisor vistaPrincipal;
    private VistaInventarioSupervisor panelInventario;
    private VistaProgramarMantenimiento panelProgramarMantenimiento;
    private VistaRealizacionMantenimiento panelRealizacionMantenimiento;

    // Instancias para el acceso a datos (DAOs)
    private final ProductoDao productoDao;
    private final HerramientaDao herramientaDao;
    private final MantenimientoDao mantenimientoDao;

    private final Usuario usuarioLogueado;

    private int idHerramientaSeleccionada = -1;
    private String nombreHerramientaSeleccionada = "";

    public ControladorInventarioSupervisor(VistaPrincipalSupervisor vistaPrincipal, Usuario usuarioLogueado) {
        this.vistaPrincipal = vistaPrincipal;
        this.usuarioLogueado = usuarioLogueado;

        // Inicialización de DAOs
        this.productoDao = new ProductoDao();
        this.herramientaDao = new HerramientaDao();
        this.mantenimientoDao = new MantenimientoDao();

        try {
            // Inicialización de vistas
            this.panelInventario = new VistaInventarioSupervisor();
            this.panelProgramarMantenimiento = new VistaProgramarMantenimiento();
            this.panelRealizacionMantenimiento = new VistaRealizacionMantenimiento();

            // Vincular listeners
            inicializarListeners();

            // Cargar datos iniciales
            listarProductosEnTabla();
            listarHerramientasEnTabla();

            // Vista por defecto
            cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar el módulo de Supervisor: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarListeners() {
        try {
            // Menú lateral (Sidebar)
            if (this.vistaPrincipal.sidebar != null) {
                this.vistaPrincipal.sidebar.bCasa.addActionListener(this);
                this.vistaPrincipal.sidebar.bInventario.addActionListener(this);
            }

            // Botones de programación
            if (this.panelProgramarMantenimiento != null) {
                this.panelProgramarMantenimiento.btnGuardarMantenimiento.addActionListener(this);
                this.panelProgramarMantenimiento.btnVolver.addActionListener(this);
            }

            // Botones de ejecución
            if (this.panelRealizacionMantenimiento != null) {
                this.panelRealizacionMantenimiento.btnGuardar.addActionListener(this);
                this.panelRealizacionMantenimiento.btnVolver.addActionListener(this);
            }

            // Evento: Selección de producto (Solo Lectura)
            this.panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int filaVisual = panelInventario.tablaProductos.getSelectedRow();
                    if (filaVisual >= 0) {
                        JOptionPane.showMessageDialog(panelInventario,
                                "Los productos están en modo de solo lectura para el rol Supervisor.",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            // Evento: Selección de herramienta para mantenimiento
            this.panelInventario.tablaHerramientas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int filaVisual = panelInventario.tablaHerramientas.getSelectedRow();
                    if (filaVisual >= 0) {
                        // Conversión de índice para soportar ordenamiento/filtrado en el JTable
                        int filaModelo = panelInventario.tablaHerramientas.convertRowIndexToModel(filaVisual);

                        idHerramientaSeleccionada = Integer.parseInt(
                                panelInventario.tablaHerramientas.getModel().getValueAt(filaModelo, 0).toString()
                        );
                        nombreHerramientaSeleccionada = panelInventario.tablaHerramientas.getModel().getValueAt(filaModelo, 1).toString();

                        lanzarMenuDecisionMantenimiento();
                    }
                }
            });

        } catch (NullPointerException npe) {
            System.err.println("Error al enlazar los listeners del Supervisor: " + npe.getMessage());
        }
    }

    private void lanzarMenuDecisionMantenimiento() {
        String[] opciones = {"Registrar Ejecutado", "Programar Agenda", "Cancelar"};

        int seleccion = JOptionPane.showOptionDialog(panelInventario,
                "¿Qué acción de mantenimiento desea gestionar para:\n" + nombreHerramientaSeleccionada + "?",
                "NEXUS GO - Gestión de Mantenimiento",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == 0) {
            panelRealizacionMantenimiento.cargarHerramientas(herramientaDao.listar());
            panelRealizacionMantenimiento.seleccionarHerramientaPorId(idHerramientaSeleccionada);
            cambiarPanelCentral(this.panelRealizacionMantenimiento);
        } else if (seleccion == 1) {
            limpiarCamposProgramacion();
            panelProgramarMantenimiento.txtEquipo.setText(nombreHerramientaSeleccionada);
            cambiarPanelCentral(this.panelProgramarMantenimiento);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // --- NAVEGACIÓN PRINCIPAL ---
            if (vistaPrincipal.sidebar != null && e.getSource() == vistaPrincipal.sidebar.bCasa) {
                cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
            }

            if (vistaPrincipal.sidebar != null && e.getSource() == vistaPrincipal.sidebar.bInventario) {
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
                listarHerramientasEnTabla();
            }

            if (e.getSource() == panelProgramarMantenimiento.btnVolver) {
                limpiarCamposProgramacion();
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            }

            if (e.getSource() == panelRealizacionMantenimiento.btnVolver) {
                panelRealizacionMantenimiento.limpiarFormulario();
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            }

            // --- ACCIONES DE GUARDADO ---
            if (e.getSource() == panelProgramarMantenimiento.btnGuardarMantenimiento) {
                ejecutarGuardadoProgramacion();
            }

            if (e.getSource() == panelRealizacionMantenimiento.btnGuardar) {
                ejecutarGuardadoEjecucion();
            }

        } catch (Exception ex) {
            System.err.println("Error en enrutamiento de eventos: " + ex.getMessage());
        }
    }

    private void ejecutarGuardadoProgramacion() {
        try {
            Date fechaCalendario = panelProgramarMantenimiento.fechaProgramacion.getDate();

            if (fechaCalendario == null) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor seleccione una fecha válida para el mantenimiento.",
                        "Fecha Vacía", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // --- VALIDACIÓN DE LA HORA DESDE EL COMBOBOX ---
            int indexHora = panelProgramarMantenimiento.comboHora.getSelectedIndex();
            if (indexHora <= 0) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor seleccione una hora de atención válida.",
                        "Hora No Seleccionada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Calendar calFechaElegida = Calendar.getInstance();
            calFechaElegida.setTime(fechaCalendario);

            // Ajustar la hora seleccionada (06:00 a. m. es el índice 1 -> 6 hrs, etc.)
            // índice 1 = 6:00 AM, índice 2 = 7:00 AM ... índice 16 = 21:00 (09:00 PM)
            int hora24 = indexHora + 5;
            calFechaElegida.set(Calendar.HOUR_OF_DAY, hora24);
            calFechaElegida.set(Calendar.MINUTE, 0);
            calFechaElegida.set(Calendar.SECOND, 0);
            calFechaElegida.set(Calendar.MILLISECOND, 0);

            Date fechaFinalProgramada = calFechaElegida.getTime();

            Object tipoSeleccionado = panelProgramarMantenimiento.cbTipoMantenimiento.getSelectedItem();
            String tipoMantenimiento = (tipoSeleccionado != null) ? tipoSeleccionado.toString() : "";
            String fallaProblema = panelProgramarMantenimiento.txtFallaProblema.getText().trim();
            String observaciones = (panelProgramarMantenimiento.txtObservaciones != null)
                    ? panelProgramarMantenimiento.txtObservaciones.getText().trim() : "";

            File imagenAdjunta = panelProgramarMantenimiento.getArchivoImagenSeleccionado();

            if (imagenAdjunta == null) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Debe adjuntar una foto del equipo para poder programar el mantenimiento.",
                        "Foto Obligatoria", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombreImagen = imagenAdjunta.getName();
            String notasCompletas = "Falla: " + fallaProblema + " | Obs: " + observaciones + " | Img: " + nombreImagen;

            if (tipoMantenimiento.isEmpty() || tipoMantenimiento.equals("Seleccione su tipo de mantenimiento") || fallaProblema.isEmpty()) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor, seleccione un tipo de mantenimiento e ingrese la falla o problema.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Margen mínimo de 48 horas (A partir de pasado mañana)
            Calendar calLimiteManana = Calendar.getInstance();
            calLimiteManana.add(Calendar.DAY_OF_MONTH, 1);
            calLimiteManana.set(Calendar.HOUR_OF_DAY, 23);
            calLimiteManana.set(Calendar.MINUTE, 59);
            calLimiteManana.set(Calendar.SECOND, 59);
            calLimiteManana.set(Calendar.MILLISECOND, 999);

            if (calFechaElegida.before(calLimiteManana)) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Excepción de Agenda:\n\n"
                        + "• No se permite programar mantenimientos para fechas pasadas.\n"
                        + "• No se permite programar mantenimientos para hoy.\n"
                        + "• No se permite programar mantenimientos para mañana.\n\n"
                        + "La agenda requiere un margen mínimo de 48 horas. Seleccione a partir de pasado mañana.",
                        "Fecha No Permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Mantenimiento nuevoMantenimiento = new Mantenimiento(
                    idHerramientaSeleccionada,
                    tipoMantenimiento,
                    fechaFinalProgramada,
                    notasCompletas,
                    usuarioLogueado.getIdUsuario()
            );
            nuevoMantenimiento.setFotoReporte(imagenAdjunta.getAbsolutePath());

            boolean guardadoExitoso = mantenimientoDao.registrarProgramacion(nuevoMantenimiento);

            if (guardadoExitoso) {
                herramientaDao.actualizarEstado(idHerramientaSeleccionada, "REQUIERE_MANTENIMIENTO");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "¡Mantenimiento programado con éxito!\n\n"
                        + "Herramienta: " + nombreHerramientaSeleccionada + "\n"
                        + "Tipo: " + tipoMantenimiento + "\n"
                        + "Fecha Agendada: " + sdf.format(fechaFinalProgramada) + "\n"
                        + "Imagen Adjunta: " + nombreImagen,
                        "NEXUS GO - Agenda Exitosa", JOptionPane.INFORMATION_MESSAGE);

                limpiarCamposProgramacion();
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            } else {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Ocurrió un problema al guardar en la base de datos.",
                        "Error de Almacenamiento", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                    "Error al procesar el guardado: " + ex.getMessage(),
                    "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarGuardadoEjecucion() {
        try {
            Herramientas herramientaSeleccionada = panelRealizacionMantenimiento.getHerramientaSeleccionada();
            if (herramientaSeleccionada == null) {
                JOptionPane.showMessageDialog(panelRealizacionMantenimiento,
                        "Seleccione la herramienta que fue reparada/atendida.",
                        "Herramienta Requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String descripcionTrabajo = panelRealizacionMantenimiento.txtDescripcionTrabajo.getText().trim();
            if (descripcionTrabajo.isEmpty()) {
                JOptionPane.showMessageDialog(panelRealizacionMantenimiento,
                        "Describa brevemente el trabajo realizado.",
                        "Campo Requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            File fotoDespues = panelRealizacionMantenimiento.getArchivoImagenDespues();

            if (fotoDespues == null || !fotoDespues.exists() || !fotoDespues.isFile() || fotoDespues.getAbsolutePath().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panelRealizacionMantenimiento,
                        "¡ATENCIÓN: LA FOTO DE DESPUÉS ES OBLIGATORIA!\n\n"
                        + "Para completar la ejecución del mantenimiento debe adjuntar la imagen\n"
                        + "que evidencia el estado final de la herramienta reparada.",
                        "Foto de Después Requerida", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (panelRealizacionMantenimiento.cbHorasInvertidas.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(panelRealizacionMantenimiento,
                        "Por favor seleccione las horas invertidas en el mantenimiento.",
                        "Horas Requeridas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double horasInvertidas;
            try {
                Object horasObj = panelRealizacionMantenimiento.cbHorasInvertidas.getSelectedItem();
                String horasTexto = (horasObj != null) ? horasObj.toString().trim() : "0";
                horasInvertidas = Double.parseDouble(horasTexto);
            } catch (NumberFormatException nfe) {
                horasInvertidas = 0.0;
            }

            String observaciones = panelRealizacionMantenimiento.txtObservaciones.getText().trim();

            boolean guardado = mantenimientoDao.registrarEjecucion(
                    herramientaSeleccionada.getIdHerramienta(),
                    descripcionTrabajo,
                    fotoDespues.getAbsolutePath(),
                    horasInvertidas,
                    observaciones
            );

            if (guardado) {
                herramientaDao.actualizarEstado(herramientaSeleccionada.getIdHerramienta(), "EXCELENTE");

                JOptionPane.showMessageDialog(panelRealizacionMantenimiento,
                        "¡Mantenimiento finalizado con éxito!\n\nHerramienta: " + herramientaSeleccionada.getNombreHerramienta()
                        + "\nLa herramienta vuelve a estar disponible.",
                        "NEXUS GO - Mantenimiento Completado", JOptionPane.INFORMATION_MESSAGE);

                panelRealizacionMantenimiento.limpiarFormulario();
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            } else {
                JOptionPane.showMessageDialog(panelRealizacionMantenimiento,
                        "No se encontró un mantenimiento programado y pendiente para esta herramienta, o falló el guardado.",
                        "Error de Almacenamiento", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelRealizacionMantenimiento,
                    "Error al registrar la ejecución del mantenimiento: " + ex.getMessage(),
                    "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCamposProgramacion() {
        if (panelProgramarMantenimiento.cbTipoMantenimiento.getItemCount() > 0) {
            panelProgramarMantenimiento.cbTipoMantenimiento.setSelectedIndex(0);
        }
        panelProgramarMantenimiento.txtFallaProblema.setText("");
        if (panelProgramarMantenimiento.txtObservaciones != null) {
            panelProgramarMantenimiento.txtObservaciones.setText("");
        }
        panelProgramarMantenimiento.lblNombreImagen.setText("Ninguna imagen seleccionada");

        // Reset explícito del archivo adjunto en la vista de programación
        if (panelProgramarMantenimiento.getArchivoImagenSeleccionado() != null) {
            panelProgramarMantenimiento.setArchivoImagenSeleccionado(null);
        }

        panelProgramarMantenimiento.fechaProgramacion.setDate(new Date());
    }

    public void listarProductosEnTabla() {
        try {
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Tipo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            panelInventario.tablaProductos.setModel(modelo);
            List<Producto> lista = productoDao.listar();
            if (lista != null) {
                for (Producto p : lista) {
                    modelo.addRow(new Object[]{p.getIdProducto(), p.getNombreProducto(), p.getPrecioCompra(), p.getStockActual(), "Insumo Interno"});
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    public void listarHerramientasEnTabla() {
        try {
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Estado", "Tipo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            panelInventario.tablaHerramientas.setModel(modelo);
            List<Herramientas> lista = herramientaDao.listar();
            if (lista != null) {
                for (Herramientas h : lista) {
                    String tipoDisponibilidad = "OCUPADA".equalsIgnoreCase(h.getDisponibilidad()) ? "Ocupado" : "Activo";
                    modelo.addRow(new Object[]{h.getIdHerramienta(), h.getNombreHerramienta(), h.getEstadoActual(), tipoDisponibilidad});
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar herramientas: " + e.getMessage());
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {
        try {
            vistaPrincipal.getContenidoCentralDinamico().removeAll();
            vistaPrincipal.getContenidoCentralDinamico().add(panelNuevo, BorderLayout.CENTER);
            vistaPrincipal.getContenidoCentralDinamico().revalidate();
            vistaPrincipal.getContenidoCentralDinamico().repaint();
        } catch (Exception e) {
            System.err.println("Error en el enrutador dinámico de vistas: " + e.getMessage());
        }
    }
}
