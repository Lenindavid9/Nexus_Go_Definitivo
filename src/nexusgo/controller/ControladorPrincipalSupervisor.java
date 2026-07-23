/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.CajaDao;
import nexusgo.model.Herramientas;
import nexusgo.model.HerramientaDao;
import nexusgo.model.Mantenimiento;
import nexusgo.model.MantenimientoDao;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.Usuario;
import nexusgo.view.AperturaCierre;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaInventarioSupervisor;
import nexusgo.view.VistaPdV;
import nexusgo.view.VistaPrincipalSupervisor;
import nexusgo.view.VistaProgramarMantenimiento;

/**
 *
 * @author USUARIO
 */
public class ControladorPrincipalSupervisor implements ActionListener {

    private final VistaPrincipalSupervisor vistaPrincipal;
    private VistaInventarioSupervisor panelInventario;
    private VistaProgramarMantenimiento panelProgramarMantenimiento;
    private AperturaCierre panelAperturaCierre;

    // DAOs y Sesión
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private final MantenimientoDao mantenimientoDao = new MantenimientoDao();
    private final CajaDao cajaDao = new CajaDao();
    private final Usuario usuarioLogueado;

    // Estado interno del supervisor
    private int idCajaActual = 0; // 0 = sin caja abierta
    private int idHerramientaSeleccionada = -1;
    private String nombreHerramientaSeleccionada = "";

    public ControladorPrincipalSupervisor(VistaPrincipalSupervisor vistaPrincipal, Usuario usuarioLogueado) {
        this.vistaPrincipal = vistaPrincipal;
        this.usuarioLogueado = usuarioLogueado;

        try {
            // 1. Inicialización de las vistas secundarias del panel
            this.panelInventario = new VistaInventarioSupervisor();
            this.panelProgramarMantenimiento = new VistaProgramarMantenimiento();
            this.panelAperturaCierre = new AperturaCierre();

            // 2. Verificar estado de caja previo
            this.idCajaActual = cajaDao.obtenerCajaAbierta();

            if (this.idCajaActual > 0) {
                double montoApertura = cajaDao.obtenerMontoApertura(this.idCajaActual);
                panelAperturaCierre.getLbltxtMontoA().setText(String.format("$%,.2f", montoApertura));
            }

            // 3. Suscripción a eventos
            inicializarListeners();

            // 4. Carga inicial de datos
            listarProductosEnTabla();
            listarHerramientasEnTabla();

            // 5. Configuración de la interfaz principal
            this.vistaPrincipal.setTitle("Sistema NexusGO - Panel de Supervisión: " + usuarioLogueado.getNombre());
            mostrarInicio();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar el módulo de Supervisor: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarListeners() {
        try {
            // Navegación Sidebar y Barra Principal
            this.vistaPrincipal.sidebar.bCasa.addActionListener(this);
            this.vistaPrincipal.sidebar.bInventario.addActionListener(this);
            this.vistaPrincipal.sidebar.misCitas.addActionListener(this);
            this.vistaPrincipal.btnCerrarSesion.addActionListener(this);

            if (this.vistaPrincipal.btnCaja != null) {
                this.vistaPrincipal.btnCaja.addActionListener(this);
            }

            // Eventos del Panel Caja
            this.panelAperturaCierre.getBtnApertura().addActionListener(this);
            this.panelAperturaCierre.getBtnCalcular().addActionListener(this);

            // Eventos del Panel Mantenimiento
            this.panelProgramarMantenimiento.btnGuardarMantenimiento.addActionListener(this);
            this.panelProgramarMantenimiento.btnVolver.addActionListener(this);

            // Listeners de Tablas (Modo lectura y selección)
            this.panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaProductos.getSelectedRow();
                    if (fila >= 0) {
                        JOptionPane.showMessageDialog(panelInventario,
                                "Los productos están en modo de solo lectura para el perfil de Supervisor.",
                                "Modo Consulta", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

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
            System.err.println("Error al vincular eventos en el controlador del supervisor: " + npe.getMessage());
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
            JOptionPane.showMessageDialog(panelInventario,
                    "Abriendo registro inmediato de mantenimientos ejecutados para:\n" + nombreHerramientaSeleccionada,
                    "Módulo Herramientas", JOptionPane.INFORMATION_MESSAGE);
        } else if (seleccion == 1) {
            panelProgramarMantenimiento.txtEquipo.setText(nombreHerramientaSeleccionada);
            cambiarPanelCentral(this.panelProgramarMantenimiento);
        }
    }

    private void mostrarInicio() {
        PanelBienvenida bienvenida = new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol());
        cambiarPanelCentral(bienvenida);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // --- NAVEGACIÓN PRINCIPAL ---
            if (e.getSource() == vistaPrincipal.sidebar.bCasa) {
                mostrarInicio();
            }

            if (e.getSource() == vistaPrincipal.btnCerrarSesion) {
                ejecutarCerrarSesion();
            }

            // --- ABRIR PUNTO DE VENTA (PdV) ---
            if (e.getSource() == vistaPrincipal.sidebar.bInventario) {
                VistaPdV vistaPdV = new VistaPdV();
                JPanel panelPdV = vistaPdV.VistaNexus();

                new ControladorPdV(vistaPdV, panelPdV, idCajaActual);

                List<Producto> listaProductos = productoDao.listar();

                if (listaProductos != null && !listaProductos.isEmpty()) {
                    for (Producto p : listaProductos) {
                        String precioFormateado = String.format("$%.0f", p.getPrecioCompra());
                        String imagen = (p.getUrlImagen() != null && !p.getUrlImagen().isEmpty())
                                ? p.getUrlImagen()
                                : "tratamiento.png";

                        vistaPdV.agregarTarjeta(
                                p.getNombreProducto(),
                                precioFormateado,
                                p.getStockActual(),
                                imagen
                        );
                    }
                }

                cambiarPanelCentral(panelPdV);
            }

            // --- VISTA INVENTARIO / TABLAS ---
            if (e.getSource() == vistaPrincipal.sidebar.misCitas) {
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
                listarHerramientasEnTabla();
            }

            // --- MODULO DE CAJA ---
            if (e.getSource() == vistaPrincipal.btnCaja) {
                cambiarPanelCentral(this.panelAperturaCierre);
            }

            // --- EVENTOS CAJA: APERTURA ---
            if (e.getSource() == panelAperturaCierre.getBtnApertura()) {
                if (idCajaActual > 0) {
                    JOptionPane.showMessageDialog(vistaPrincipal,
                            "Ya existe una caja abierta. Debe realizar el cierre antes de registrar una nueva apertura.",
                            "Caja en Uso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String montoStr = panelAperturaCierre.getTxtMontoInicial().getText();
                if (!montoStr.trim().isEmpty()) {
                    double monto;
                    try {
                        monto = parsearMonto(montoStr);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(vistaPrincipal,
                                "El monto ingresado no es un número válido.",
                                "Formato Inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (monto <= 0 || monto > 99999999.99) {
                        JOptionPane.showMessageDialog(vistaPrincipal,
                                "El monto debe ser mayor a $0 y no superar $99.999.999,99.",
                                "Monto Fuera de Rango", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    idCajaActual = cajaDao.guardarApertura(monto, usuarioLogueado.getIdUsuario());

                    if (idCajaActual > 0) {
                        panelAperturaCierre.getLbltxtMontoA().setText(String.format("$%,.2f", monto));
                        JOptionPane.showMessageDialog(vistaPrincipal,
                                "Apertura de caja realizada con: $" + String.format("%,.2f", monto),
                                "Caja Registrada", JOptionPane.INFORMATION_MESSAGE);
                        
                        refrescarVistaDinamica();
                    } else {
                        JOptionPane.showMessageDialog(vistaPrincipal,
                                "No se pudo registrar la apertura de caja en la base de datos.",
                                "Error de BD", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            // --- EVENTOS CAJA: CIERRE ---
            if (e.getSource() == panelAperturaCierre.getBtnCalcular()) {
                String montoFisicoStr = panelAperturaCierre.getTxtMontoF().getText();
                
                if (!montoFisicoStr.trim().isEmpty() && idCajaActual > 0) {
                    try {
                        double montoFisico = parsearMonto(montoFisicoStr);
                        cajaDao.guardarCierre(idCajaActual, montoFisico, montoFisico);

                        JOptionPane.showMessageDialog(vistaPrincipal,
                                "Cierre procesado exitosamente.\nMonto en caja: $" + String.format("%,.2f", montoFisico),
                                "Cierre de Caja", JOptionPane.INFORMATION_MESSAGE);

                        idCajaActual = 0;
                        panelAperturaCierre.getLbltxtMontoA().setText("");
                        panelAperturaCierre.getTxtMontoF().setText("");
                        panelAperturaCierre.getTxtMontoInicial().setText("");

                        refrescarVistaDinamica();
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(vistaPrincipal,
                                "Ingrese un monto físico válido para realizar el cierre.",
                                "Monto Inválido", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (idCajaActual <= 0) {
                    JOptionPane.showMessageDialog(vistaPrincipal,
                            "No hay ninguna caja abierta actualmente para cerrar.",
                            "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }

            // --- EVENTOS PANEL PROGRAMACIÓN MANTENIMIENTO ---
            if (e.getSource() == panelProgramarMantenimiento.btnVolver) {
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            }

            if (e.getSource() == panelProgramarMantenimiento.btnGuardarMantenimiento) {
                ejecutarGuardadoProgramacion();
            }

        } catch (Exception ex) {
            System.err.println("Error general en eventos del Supervisor: " + ex.getMessage());
        }
    }

    private double parsearMonto(String texto) {
        String limpio = texto.replace("$", "").trim();
        limpio = limpio.replace(".", "");       // Quita separadores de miles
        limpio = limpio.replace(",", ".");      // Normaliza el separador decimal
        return Double.parseDouble(limpio);
    }

    private void ejecutarGuardadoProgramacion() {
        try {
            // 1. Obtención de fecha
            Date fechaCalendario = panelProgramarMantenimiento.fechaProgramacion.getDate();

            if (fechaCalendario == null) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor seleccione una fecha válida en el calendario.",
                        "Fecha Vacía", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Fusión de fecha y hora del Spinner
            Calendar calFechaElegida = Calendar.getInstance();
            calFechaElegida.setTime(fechaCalendario);

            if (panelProgramarMantenimiento.spinnerHora != null) {
                Date horaSpinner = (Date) panelProgramarMantenimiento.spinnerHora.getValue();
                Calendar calHora = Calendar.getInstance();
                calHora.setTime(horaSpinner);

                calFechaElegida.set(Calendar.HOUR_OF_DAY, calHora.get(Calendar.HOUR_OF_DAY));
                calFechaElegida.set(Calendar.MINUTE, calHora.get(Calendar.MINUTE));
                calFechaElegida.set(Calendar.SECOND, 0);
                calFechaElegida.set(Calendar.MILLISECOND, 0);
            }

            Date fechaFinalProgramada = calFechaElegida.getTime();

            // 3. Campos del formulario
            String tipoMantenimiento = panelProgramarMantenimiento.cbTipoMantenimiento.getSelectedItem().toString();
            String fallaProblema = panelProgramarMantenimiento.txtFallaProblema.getText().trim();
            String observaciones = (panelProgramarMantenimiento.txtObservaciones != null)
                    ? panelProgramarMantenimiento.txtObservaciones.getText().trim() : "";

            File imagenAdjunta = panelProgramarMantenimiento.getArchivoImagenSeleccionado();
            String nombreImagen = (imagenAdjunta != null) ? imagenAdjunta.getName() : "Sin imagen";

            String notasCompletas = "Falla: " + fallaProblema + " | Obs: " + observaciones + " | Img: " + nombreImagen;

            // 4. Validaciones obligatorias
            if (tipoMantenimiento.equals("Seleccione su tipo de mantenimiento") || fallaProblema.isEmpty()) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Debe seleccionar el tipo de mantenimiento y describir la falla/problema.",
                        "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 5. Regla de Negocio: Mínimo 48 horas de anticipación
            Calendar calLimiteMañana = Calendar.getInstance();
            calLimiteMañana.add(Calendar.DAY_OF_MONTH, 1);
            calLimiteMañana.set(Calendar.HOUR_OF_DAY, 23);
            calLimiteMañana.set(Calendar.MINUTE, 59);
            calLimiteMañana.set(Calendar.SECOND, 59);
            calLimiteMañana.set(Calendar.MILLISECOND, 999);

            if (calFechaElegida.before(calLimiteMañana)) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Excepción de Agenda:\n\n"
                        + "• No se permite agendar mantenimientos para hoy ni días pasados.\n"
                        + "• No se permite agendar mantenimientos para el día de mañana.\n\n"
                        + "Requiere un margen mínimo de 48 horas. Seleccione a partir de pasado mañana.",
                        "Fecha Restringida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 6. Construcción del modelo y guardado
            Mantenimiento nuevoMantenimiento = new Mantenimiento(
                    idHerramientaSeleccionada,
                    tipoMantenimiento,
                    fechaFinalProgramada,
                    notasCompletas,
                    usuarioLogueado.getIdUsuario()
            );

            boolean guardadoExitoso = mantenimientoDao.registrarProgramacion(nuevoMantenimiento);

            if (guardadoExitoso) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "¡Mantenimiento agendado exitosamente!\n\n"
                        + "Herramienta: " + nombreHerramientaSeleccionada + "\n"
                        + "Tipo: " + tipoMantenimiento + "\n"
                        + "Fecha Programada: " + sdf.format(fechaFinalProgramada) + "\n"
                        + "Adjunto: " + nombreImagen,
                        "NEXUS GO - Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarCamposProgramacion();
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            } else {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "No se pudo guardar la información en la base de datos.",
                        "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                    "Error inesperado al agendar: " + ex.getMessage(),
                    "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCamposProgramacion() {
        panelProgramarMantenimiento.cbTipoMantenimiento.setSelectedIndex(0);
        panelProgramarMantenimiento.txtFallaProblema.setText("");
        if (panelProgramarMantenimiento.txtObservaciones != null) {
            panelProgramarMantenimiento.txtObservaciones.setText("");
        }
        panelProgramarMantenimiento.lblNombreImagen.setText("Ninguna imagen seleccionada");

        Date hoy = new Date();
        panelProgramarMantenimiento.fechaProgramacion.setDate(hoy);
        panelProgramarMantenimiento.fechaProgramacion.setDate(hoy);
    }

    private void ejecutarCerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vistaPrincipal,
                "¿Desea cerrar la sesión actual del sistema?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            vistaPrincipal.dispose();
            
            // Reabre el login para nuevas autenticaciones
            VistaInicioSesion vistaLogin = new VistaInicioSesion();
            new ControladorInicioSesion(vistaLogin);
            vistaLogin.setVisible(true);
        }
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
            System.err.println("Error al cargar la lista de productos: " + e.getMessage());
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
                    modelo.addRow(new Object[]{h.getIdHerramienta(), h.getNombreHerramienta(), h.getEstadoActual(), "Activo"});
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la lista de herramientas: " + e.getMessage());
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {
        try {
            vistaPrincipal.getContenidoCentralDinamico().removeAll();
            vistaPrincipal.getContenidoCentralDinamico().add(panelNuevo, BorderLayout.CENTER);
            refrescarVistaDinamica();
        } catch (Exception e) {
            System.err.println("Error en la conmutación de paneles centrales: " + e.getMessage());
        }
    }

    private void refrescarVistaDinamica() {
        SwingUtilities.invokeLater(() -> {
            vistaPrincipal.getContenidoCentralDinamico().revalidate();
            vistaPrincipal.getContenidoCentralDinamico().repaint();
        });
    }
}
