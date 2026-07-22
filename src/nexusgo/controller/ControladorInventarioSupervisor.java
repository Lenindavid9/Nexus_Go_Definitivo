/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//
package nexusgo.controller;

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

/**
 *
 * @author USUARIO
 */
public class ControladorInventarioSupervisor implements ActionListener {
    
    private final VistaPrincipalSupervisor vistaPrincipal;
    private VistaInventarioSupervisor panelInventario;
    private VistaProgramarMantenimiento panelProgramarMantenimiento;

    // Instancias para el acceso a datos (DAOs)
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private final MantenimientoDao mantenimientoDao = new MantenimientoDao();
    
    private final Usuario usuarioLogueado;
    
    private int idHerramientaSeleccionada = -1;
    private String nombreHerramientaSeleccionada = "";

    public ControladorInventarioSupervisor(VistaPrincipalSupervisor vistaPrincipal, Usuario usuarioLogueado) {
        this.vistaPrincipal = vistaPrincipal;
        this.usuarioLogueado = usuarioLogueado;

        try {
            // Inicializar las vistas
            this.panelInventario = new VistaInventarioSupervisor();
            this.panelProgramarMantenimiento = new VistaProgramarMantenimiento();

            // Vincular listeners de la interfaz
            inicializarListeners();

            // Cargar tablas desde la base de datos
            listarProductosEnTabla();
            listarHerramientasEnTabla();

            // Vista por defecto al ingresar al módulo
            cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar el módulo de Supervisor: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarListeners() {
        try {
            // Escuchar clics del menú lateral
            this.vistaPrincipal.sidebar.bCasa.addActionListener(this);
            this.vistaPrincipal.sidebar.bInventario.addActionListener(this);

            // Escuchar botones del formulario de programación
            this.panelProgramarMantenimiento.btnGuardarMantenimiento.addActionListener(this);
            this.panelProgramarMantenimiento.btnVolver.addActionListener(this);

            // Evento: Selección de producto (Solo Lectura)
            this.panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaProductos.getSelectedRow();
                    if (fila >= 0) {
                        JOptionPane.showMessageDialog(panelInventario, 
                                "Los productos están en modo de solo lectura para el Supervisor.", 
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            // Evento: Selección de herramienta para mantenimiento
            this.panelInventario.tablaHerramientas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaHerramientas.getSelectedRow();
                    if (fila >= 0) {
                        idHerramientaSeleccionada = (int) panelInventario.tablaHerramientas.getValueAt(fila, 0);
                        nombreHerramientaSeleccionada = panelInventario.tablaHerramientas.getValueAt(fila, 1).toString();
                        
                        lanzarMenuDecisionMantenimiento();
                    }
                }
            });

        } catch (NullPointerException npe) {
            System.err.println("Error al enlazar los listeners del Supervisor: " + npe.getMessage());
        }
    }

    // Opciones flotantes al presionar un elemento en la tabla de herramientas
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

        if (seleccion == 1) { // Redirigir al formulario de "Programar Agenda"
            panelProgramarMantenimiento.txtEquipo.setText(nombreHerramientaSeleccionada);
            cambiarPanelCentral(this.panelProgramarMantenimiento);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // --- NAVEGACIÓN PRINCIPAL ---
            if (e.getSource() == vistaPrincipal.sidebar.bCasa) {
                cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
            }

            if (e.getSource() == vistaPrincipal.sidebar.bInventario) {
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
                listarHerramientasEnTabla();
            }

            if (e.getSource() == panelProgramarMantenimiento.btnVolver) {
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            }

            // --- ACCIÓN DE GUARDADO ---
            if (e.getSource() == panelProgramarMantenimiento.btnGuardarMantenimiento) {
                ejecutarGuardadoProgramacion();
            }

        } catch (Exception ex) {
            System.err.println("Error en enrutamiento de eventos: " + ex.getMessage());
        }
    }

    private void ejecutarGuardadoProgramacion() {
        try {
            // 1. Obtener la fecha seleccionada en el JDateChooser (Mantenimiento)
            Date fechaCalendario = panelProgramarMantenimiento.selectorFecha.getDate();

            if (fechaCalendario == null) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor seleccione una fecha válida para el mantenimiento.",
                        "Fecha Vacía", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // --- NUEVO: Capturar y Validar Fechas de Promoción ---
            Date fechaInicioPromo = panelProgramarMantenimiento.fechaInicioPromocion.getDate();
            Date fechaFinPromo = panelProgramarMantenimiento.fechaFinPromocion.getDate();

            if (fechaInicioPromo == null || fechaFinPromo == null) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor complete ambas fechas de vigencia para la promoción (Inicio y Fin).",
                        "Fechas de Promoción Incompletas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (fechaInicioPromo.after(fechaFinPromo)) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "La fecha de inicio de la promoción no puede ser posterior a la fecha de fin.",
                        "Rango de Fechas Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Format para presentar o guardar las fechas de promoción
            SimpleDateFormat sdfPromo = new SimpleDateFormat("yyyy-MM-dd");
            String strInicioPromo = sdfPromo.format(fechaInicioPromo);
            String strFinPromo = sdfPromo.format(fechaFinPromo);

            // 2. Extraer y fusionar la hora proveniente del Spinner
            Date horaSpinner = (Date) panelProgramarMantenimiento.spinnerHora.getValue();

            Calendar calFechaElegida = Calendar.getInstance();
            calFechaElegida.setTime(fechaCalendario);

            Calendar calHoraElegida = Calendar.getInstance();
            calHoraElegida.setTime(horaSpinner);

            calFechaElegida.set(Calendar.HOUR_OF_DAY, calHoraElegida.get(Calendar.HOUR_OF_DAY));
            calFechaElegida.set(Calendar.MINUTE, calHoraElegida.get(Calendar.MINUTE));
            calFechaElegida.set(Calendar.SECOND, 0);
            calFechaElegida.set(Calendar.MILLISECOND, 0);

            Date fechaFinalProgramada = calFechaElegida.getTime();

            // 3. Extraer los datos textuales de la vista
            String tipoMantenimiento = panelProgramarMantenimiento.cbTipoMantenimiento.getSelectedItem().toString();
            String fallaProblema = panelProgramarMantenimiento.txtFallaProblema.getText().trim();
            String observaciones = panelProgramarMantenimiento.txtObservaciones.getText().trim();

            File imagenAdjunta = panelProgramarMantenimiento.getArchivoImagenSeleccionado();
            String nombreImagen = (imagenAdjunta != null) ? imagenAdjunta.getName() : "Sin imagen";

            // Se agregan los rangos de fecha de promoción a las notas de auditoría/registro
            String notasCompletas = "Falla: " + fallaProblema + 
                                    " | Promo: " + strInicioPromo + " a " + strFinPromo + 
                                    " | Obs: " + observaciones + 
                                    " | Img: " + nombreImagen;

            // 4. Validación de campos de texto obligatorios
            if (tipoMantenimiento.equals("Seleccione su tipo de mantenimiento") || fallaProblema.isEmpty()) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor, seleccione un tipo de mantenimiento e ingrese la falla o problema.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // =========================================================================
            // 5. REGLA DE NEGOCIO: VALIDACIÓN DE FECHA DE MANTENIMIENTO
            // =========================================================================
            Calendar calLimiteManana = Calendar.getInstance();
            calLimiteManana.add(Calendar.DAY_OF_MONTH, 1);
            calLimiteManana.set(Calendar.HOUR_OF_DAY, 23);
            calLimiteManana.set(Calendar.MINUTE, 59);
            calLimiteManana.set(Calendar.SECOND, 59);
            calLimiteManana.set(Calendar.MILLISECOND, 999);

            if (calFechaElegida.before(calLimiteManana)) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Excepción de Agenda:\n\n" +
                        "• No se permite programar mantenimientos para fechas pasadas.\n" +
                        "• No se permite programar mantenimientos para hoy o mañana.\n\n" +
                        "La agenda requiere un margen mínimo de 48 horas. Seleccione a partir de pasado mañana.",
                        "Fecha No Permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 6. Instanciar el modelo
            Mantenimiento nuevoMantenimiento = new Mantenimiento(
                    idHerramientaSeleccionada, 
                    tipoMantenimiento, 
                    fechaFinalProgramada, 
                    notasCompletas, 
                    usuarioLogueado.getIdUsuario()
            );

            // 7. Insertar el registro en la base de datos
            boolean guardadoExitoso = mantenimientoDao.registrarProgramacion(nuevoMantenimiento);

            if (guardadoExitoso) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "¡Mantenimiento y Promoción registrados con éxito!\n\n" +
                        "Herramienta: " + nombreHerramientaSeleccionada + "\n" +
                        "Tipo Mantenimiento: " + tipoMantenimiento + "\n" +
                        "Fecha Mantenimiento: " + sdf.format(fechaFinalProgramada) + "\n" +
                        "Promoción Vigente: Del " + strInicioPromo + " al " + strFinPromo + "\n" +
                        "Imagen Adjunta: " + nombreImagen,
                        "NEXUS GO - Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);

                // 8. Limpiar controles y regresar
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

    private void limpiarCamposProgramacion() {
        panelProgramarMantenimiento.cbTipoMantenimiento.setSelectedIndex(0);
        panelProgramarMantenimiento.txtFallaProblema.setText("");
        panelProgramarMantenimiento.txtObservaciones.setText("");
        panelProgramarMantenimiento.lblNombreImagen.setText("Ninguna imagen seleccionada");
        
        // Reset de calendarios JCalendar
        Date hoy = new Date();
        panelProgramarMantenimiento.selectorFecha.setDate(hoy);
        panelProgramarMantenimiento.fechaInicioPromocion.setDate(hoy);
        panelProgramarMantenimiento.fechaFinPromocion.setDate(hoy);
    }

    public void listarProductosEnTabla() {
        try {
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Tipo"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
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
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            panelInventario.tablaHerramientas.setModel(modelo);
            List<Herramientas> lista = herramientaDao.listar();
            if (lista != null) {
                for (Herramientas h : lista) {
                    modelo.addRow(new Object[]{h.getIdHerramienta(), h.getNombreHerramienta(), h.getEstadoActual(), "Activo"});
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar herramientas: " + e.getMessage());
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {
        try {
            vistaPrincipal.getContenidoCentralDinamico().removeAll();
            vistaPrincipal.getContenidoCentralDinamico().add(panelNuevo, java.awt.BorderLayout.CENTER);
            vistaPrincipal.getContenidoCentralDinamico().revalidate();
            vistaPrincipal.getContenidoCentralDinamico().repaint();
        } catch (Exception e) {
            System.err.println("Error en el enrutador dinámico de vistas: " + e.getMessage());
        }
    }
    
}
