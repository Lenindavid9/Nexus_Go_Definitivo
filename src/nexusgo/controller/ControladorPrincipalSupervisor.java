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

    // --- Instancia del panel AperturaCierre ---
    private AperturaCierre panelAperturaCierre;

    // Componentes de datos y sesión
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private final MantenimientoDao mantenimientoDao = new MantenimientoDao();
    private final CajaDao cajaDao = new CajaDao();
    private final Usuario usuarioLogueado;

    // ID de la caja actualmente abierta (0 = no hay caja abierta)
    private int idCajaActual = 0;
    private int idHerramientaSeleccionada = -1;
    private String nombreHerramientaSeleccionada = "";

    public ControladorPrincipalSupervisor(VistaPrincipalSupervisor vistaPrincipal, Usuario usuarioLogueado) {
        this.vistaPrincipal = vistaPrincipal;
        this.usuarioLogueado = usuarioLogueado;

        try {
            // Inicialización de las vistas del módulo
            this.panelInventario = new VistaInventarioSupervisor();
            this.panelProgramarMantenimiento = new VistaProgramarMantenimiento();

            // Inicializar Vista de Caja
            this.panelAperturaCierre = new AperturaCierre();

            // Verificar si ya existe una caja abierta (de una sesión anterior)
            this.idCajaActual = cajaDao.obtenerCajaAbierta();
            
            // Si la caja sigue abierta debe mostrar su monto de apertura
            if (this.idCajaActual > 0) {
                double montoApertura = cajaDao.obtenerMontoApertura(this.idCajaActual);
                panelAperturaCierre.getLbltxtMontoA().setText(String.format("$%,.2f", montoApertura));
            }

            inicializarListeners();

            // Carga de datos en las JTables
            listarProductosEnTabla();
            listarHerramientasEnTabla();

            // Título de la app según sesión
            vistaPrincipal.setTitle("Sistema NexusGO - Panel de Supervisión: " + usuarioLogueado.getNombre());

            // Panel de inicio por defecto
            mostrarInicio();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar el módulo de Supervisor: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarListeners() {
        try {
            // Escuchar la barra lateral y botones principales
            this.vistaPrincipal.sidebar.bCasa.addActionListener(this);
            this.vistaPrincipal.sidebar.bInventario.addActionListener(this);
            this.vistaPrincipal.sidebar.misCitas.addActionListener(this);
            this.vistaPrincipal.btnCerrarSesion.addActionListener(this);

            // Registrar el botón Caja
            if (this.vistaPrincipal.btnCaja != null) {
                this.vistaPrincipal.btnCaja.addActionListener(this);
            }

            // Escuchar botones del panel AperturaCierre (Caja)
            this.panelAperturaCierre.getBtnApertura().addActionListener(this);
            this.panelAperturaCierre.getBtnCalcular().addActionListener(this);

            // Escuchar botones de programación de mantenimiento
            this.panelProgramarMantenimiento.btnGuardarMantenimiento.addActionListener(this);
            this.panelProgramarMantenimiento.btnVolver.addActionListener(this);

            // Tablas (MouseListeners)
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
            // --- EVENTOS DE NAVEGACIÓN ---
            if (e.getSource() == vistaPrincipal.sidebar.bCasa) {
                mostrarInicio();
            }

            if (e.getSource() == vistaPrincipal.btnCerrarSesion) {
                ejecutarCerrarSesion();
            }

            // --- ABRIR LA VISTA DEL PUNTO DE VENTA (PdV) ---
            if (e.getSource() == vistaPrincipal.sidebar.bInventario) {
                VistaPdV vistaPdV = new VistaPdV();
                JPanel panelPdV = vistaPdV.VistaNexus();

                ControladorPdV controladorPdV = new ControladorPdV(vistaPdV, panelPdV, idCajaActual);

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

            if (e.getSource() == vistaPrincipal.sidebar.misCitas) {
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
                listarHerramientasEnTabla();
            }

            // --- ACCIÓN AL PRESIONAR BOTÓN CAJA ---
            if (e.getSource() == vistaPrincipal.btnCaja) {
                cambiarPanelCentral(this.panelAperturaCierre);
            }

            // --- LÓGICA DE APERTURA / CIERRE DE CAJA ---
            if (e.getSource() == panelAperturaCierre.getBtnApertura()) {
                String montoStr = panelAperturaCierre.getTxtMontoInicial().getText();
                if (!montoStr.trim().isEmpty()) {
                    double monto;
                    try {
                        monto = parsearMonto(montoStr);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(vistaPrincipal, "El monto ingresado no es un número válido.",
                                "Formato inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (monto <= 0 || monto > 99999999.99) {
                        JOptionPane.showMessageDialog(vistaPrincipal, "El monto debe ser mayor a 0 y no superar $99.999.999,99.",
                                "Monto fuera de rango", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    idCajaActual = cajaDao.guardarApertura(monto, usuarioLogueado.getIdUsuario());

                    if (idCajaActual > 0) {
                        panelAperturaCierre.getLbltxtMontoA().setText(String.format("$%,.2f", monto));
                        JOptionPane.showMessageDialog(vistaPrincipal, "Apertura de caja realizada con: $" + String.format("%,.2f", monto),
                                "Caja Registrada", JOptionPane.INFORMATION_MESSAGE);
                        vistaPrincipal.repaint();
                    } else {
                        JOptionPane.showMessageDialog(vistaPrincipal, "No se pudo registrar la apertura de caja en la base de datos.",
                                "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            if (e.getSource() == panelAperturaCierre.getBtnCalcular()) {
                String montoFisico = panelAperturaCierre.getTxtMontoF().getText().replace("$", "").trim();
                if (!montoFisico.isEmpty() && idCajaActual > 0) {
                    double monto = Double.parseDouble(montoFisico);
                    cajaDao.guardarCierre(idCajaActual, monto, monto);
                    JOptionPane.showMessageDialog(vistaPrincipal, "Cierre procesado con monto físico en caja: $" + montoFisico,
                            "Cierre de Caja", JOptionPane.INFORMATION_MESSAGE);
                    idCajaActual = 0;
                    panelAperturaCierre.getLbltxtMontoA().setText("");
                } else if (idCajaActual <= 0) {
                    JOptionPane.showMessageDialog(vistaPrincipal, "No hay ninguna caja abierta para cerrar.",
                            "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }

            // --- EVENTOS PANEL PROGRAMACIÓN ---
            if (e.getSource() == panelProgramarMantenimiento.btnVolver) {
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            }

            if (e.getSource() == panelProgramarMantenimiento.btnGuardarMantenimiento) {
                ejecutarGuardadoProgramacion();
            }

        } catch (Exception ex) {
            System.err.println("Error en eventos del Supervisor: " + ex.getMessage());
        }
    }

    private double parsearMonto(String texto) {
        String limpio = texto.replace("$", "").trim();
        limpio = limpio.replace(".", "");       // quita separadores de miles
        limpio = limpio.replace(",", ".");      // convierte la coma decimal en punto
        return Double.parseDouble(limpio);
    }

   private void ejecutarGuardadoProgramacion() {
        try {
            // 1. Obtener fecha directamente del selector gráfico
            Date fechaCalendario = panelProgramarMantenimiento.selectorFecha.getDate();

            if (fechaCalendario == null) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor seleccione una fecha válida en el calendario.",
                        "Fecha Vacía", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Fusionar fecha con la hora elegida en el Spinner de hora
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

            // 3. Obtener textos del formulario
            String tipoMantenimiento = panelProgramarMantenimiento.cbTipoMantenimiento.getSelectedItem().toString();
            String fallaProblema = panelProgramarMantenimiento.txtFallaProblema.getText().trim();
            String observaciones = (panelProgramarMantenimiento.txtObservaciones != null) 
                    ? panelProgramarMantenimiento.txtObservaciones.getText().trim() : "";

            File imagenAdjunta = panelProgramarMantenimiento.getArchivoImagenSeleccionado();
            String nombreImagen = (imagenAdjunta != null) ? imagenAdjunta.getName() : "Sin imagen";

            String notasCompletas = "Falla: " + fallaProblema + " | Obs: " + observaciones + " | Img: " + nombreImagen;

            // 4. Validar campos de texto obligatorios
            if (tipoMantenimiento.equals("Seleccione su tipo de mantenimiento") || fallaProblema.isEmpty()) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor, seleccione un tipo de mantenimiento e ingrese la falla o problema.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 5. REGLA DE NEGOCIO Y EXCEPCIONES DE FECHA (Mínimo 48 horas)
            Calendar calLimiteMañana = Calendar.getInstance();
            calLimiteMañana.add(Calendar.DAY_OF_MONTH, 1);
            calLimiteMañana.set(Calendar.HOUR_OF_DAY, 23);
            calLimiteMañana.set(Calendar.MINUTE, 59);
            calLimiteMañana.set(Calendar.SECOND, 59);
            calLimiteMañana.set(Calendar.MILLISECOND, 999);

            if (calFechaElegida.before(calLimiteMañana)) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Excepción de Agenda:\n\n" +
                        "• No se permite programar mantenimientos para fechas pasadas.\n" +
                        "• No se permite programar mantenimientos para hoy.\n" +
                        "• No se permite programar mantenimientos para mañana.\n\n" +
                        "La agenda requiere un margen mínimo de 48 horas. Seleccione a partir de pasado mañana.",
                        "Fecha No Permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 6. Instanciar objeto modelo e insertar en la Base de Datos
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
                        "¡Mantenimiento programado con éxito!\n\n" +
                        "Herramienta: " + nombreHerramientaSeleccionada + "\n" +
                        "Tipo: " + tipoMantenimiento + "\n" +
                        "Fecha Agendada: " + sdf.format(fechaFinalProgramada) + "\n" +
                        "Imagen Adjunta: " + nombreImagen,
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

    private void limpiarCamposProgramacion() {
        panelProgramarMantenimiento.cbTipoMantenimiento.setSelectedIndex(0);
        panelProgramarMantenimiento.txtFallaProblema.setText("");
        if (panelProgramarMantenimiento.txtObservaciones != null) {
            panelProgramarMantenimiento.txtObservaciones.setText("");
        }
        panelProgramarMantenimiento.lblNombreImagen.setText("Ninguna imagen seleccionada");
        if (panelProgramarMantenimiento.selectorFecha != null) {
            panelProgramarMantenimiento.selectorFecha.setDate(new Date());
        }
    }

    public void listarProductosEnTabla() {
        try {
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Tipo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            panelInventario.tablaProductos.setModel(modeloBlindado);
            List<Producto> lista = productoDao.listar();
            if (lista != null) {
                for (Producto p : lista) {
                    modeloBlindado.addRow(new Object[]{p.getIdProducto(), p.getNombreProducto(), p.getPrecioCompra(), p.getStockActual(), "Insumo Interno"});
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    public void listarHerramientasEnTabla() {
        try {
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Estado", "Tipo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            panelInventario.tablaHerramientas.setModel(modeloBlindado);
            List<Herramientas> lista = herramientaDao.listar();
            if (lista != null) {
                for (Herramientas h : lista) {
                    modeloBlindado.addRow(new Object[]{h.getIdHerramienta(), h.getNombreHerramienta(), h.getEstadoActual(), "Activo"});
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar herramientas: " + e.getMessage());
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {
        JPanel contenedor = vistaPrincipal.getContenidoCentralDinamico();

        if (contenedor != null) {
            contenedor.removeAll();
            contenedor.setLayout(new BorderLayout());
            contenedor.add(panelNuevo, BorderLayout.CENTER);
            contenedor.revalidate();
            contenedor.repaint();
        }
    }

    private void ejecutarCerrarSesion() {
        int confirmar = JOptionPane.showConfirmDialog(null, "¿Desea cerrar sesión en NEXUS GO?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            vistaPrincipal.dispose();

            VistaInicioSesion loginVista = new VistaInicioSesion();
            new ControladorInicioSesion(loginVista);
            loginVista.setLocationRelativeTo(null);
            loginVista.setVisible(true);
        }
    }
}
