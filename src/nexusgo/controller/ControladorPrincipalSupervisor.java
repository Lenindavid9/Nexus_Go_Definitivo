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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.Herramientas;
import nexusgo.model.HerramientaDao;
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
    private final Usuario usuarioLogueado;

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

    /**
     * Muestra la pantalla inicial (Panel de Bienvenida) garantizando 
     * un repintado correcto dentro del contenedor dinámico.
     */
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
                // 1. Instanciar la vista de Punto de Venta
                VistaPdV vistaPdV = new VistaPdV();
                JPanel panelPdV = vistaPdV.VistaNexus();

                // 2. Enlazar su controlador de eventos (para pagos, facturar y reiniciar)
                ControladorPdV controladorPdV = new ControladorPdV(vistaPdV);

                // 3. Obtener los productos reales registrados en la BD
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

                // 4. Cambiar el contenedor central por la vista del PdV
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
                String montoStr = panelAperturaCierre.getTxtMontoInicial().getText().replace("$", "").trim();
                if (!montoStr.isEmpty()) {
                    panelAperturaCierre.getLbltxtMontoA().setText("$" + montoStr);
                    JOptionPane.showMessageDialog(vistaPrincipal, "Apertura de caja realizada con: $" + montoStr, "Caja Registrada", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            if (e.getSource() == panelAperturaCierre.getBtnCalcular()) {
                String montoFisico = panelAperturaCierre.getTxtMontoF().getText().replace("$", "").trim();
                JOptionPane.showMessageDialog(vistaPrincipal, "Cierre procesado con monto físico en caja: $" + montoFisico, "Cierre de Caja", JOptionPane.INFORMATION_MESSAGE);
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

    private void ejecutarGuardadoProgramacion() {
        try {
            Date fechaSeleccionada = (Date) panelProgramarMantenimiento.spinnerFechaHora.getValue();
            String tipoMantenimiento = panelProgramarMantenimiento.cbTipoMantenimiento.getSelectedItem().toString();
            String fallaProblema = panelProgramarMantenimiento.txtFallaProblema.getText().trim();

            if (tipoMantenimiento.equals("Seleccione su tipo de mantenimiento") || fallaProblema.isEmpty()) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor, seleccione un tipo de mantenimiento e ingrese la falla o problema.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Calendar calSeleccionada = Calendar.getInstance();
            calSeleccionada.setTime(fechaSeleccionada);

            Calendar calLimiteHoy = Calendar.getInstance();
            calLimiteHoy.set(Calendar.HOUR_OF_DAY, 23);
            calLimiteHoy.set(Calendar.MINUTE, 59);
            calLimiteHoy.set(Calendar.SECOND, 59);

            if (calSeleccionada.before(calLimiteHoy)) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Excepción de Agenda: No se puede programar mantenimiento para hoy mismo ni para fechas pasadas.\nDebe agendarse a partir de mañana.",
                        "Fecha No Permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                    "¡Mantenimiento Programado!\n\n"
                    + "Herramienta: " + nombreHerramientaSeleccionada + "\n"
                    + "Tipo: " + tipoMantenimiento + "\n"
                    + "Fecha: " + sdf.format(fechaSeleccionada),
                    "NEXUS GO", JOptionPane.INFORMATION_MESSAGE);

            limpiarCamposProgramacion();
            cambiarPanelCentral(this.panelInventario);
            listarHerramientasEnTabla();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                    "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCamposProgramacion() {
        panelProgramarMantenimiento.cbTipoMantenimiento.setSelectedIndex(0);
        panelProgramarMantenimiento.txtFallaProblema.setText("");
        panelProgramarMantenimiento.txtObservaciones.setText("");
        panelProgramarMantenimiento.lblNombreImagen.setText("tratamiento.png");
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
