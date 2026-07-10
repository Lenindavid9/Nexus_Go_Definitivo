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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.Herramientas;
import nexusgo.model.HerramientaDao;
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

    // Componentes de datos y estado de sesión
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private final Usuario usuarioLogueado;
    private int idHerramientaSeleccionada = -1;
    private String nombreHerramientaSeleccionada = "";

    public ControladorInventarioSupervisor(VistaPrincipalSupervisor vistaPrincipal, Usuario usuarioLogueado) {
        this.vistaPrincipal = vistaPrincipal;
        this.usuarioLogueado = usuarioLogueado;

        try {
            // Inicialización de componentes gráficos (Ya no requerimos VistaOpcionesHerramientas)
            this.panelInventario = new VistaInventarioSupervisor();
            this.panelProgramarMantenimiento = new VistaProgramarMantenimiento();

            inicializarListeners();

            // Carga inicial de datos desde MySQL
            listarProductosEnTabla();
            listarHerramientasEnTabla();

            aplicarPermisosPorRol();

            // Panel de bienvenida inicial
            cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar el módulo de Supervisor: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarPermisosPorRol() {
        vistaPrincipal.setTitle("Sistema NexusGO - Panel de Supervisión: " + usuarioLogueado.getNombre());
    }

    private void inicializarListeners() {
        try {
            this.vistaPrincipal.sidebar.bCasa.addActionListener(this);
            this.vistaPrincipal.sidebar.bInventario.addActionListener(this);

            // Listeners del formulario de programación
            this.panelProgramarMantenimiento.btnGuardarMantenimiento.addActionListener(this);
            this.panelProgramarMantenimiento.btnVolver.addActionListener(this);

            // Clics en Tabla de Productos (Solo lectura)
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

            // Clics en Tabla de Herramientas -> Aquí disparamos el JOptionPane de decisión
            this.panelInventario.tablaHerramientas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaHerramientas.getSelectedRow();
                    if (fila >= 0) {
                        idHerramientaSeleccionada = (int) panelInventario.tablaHerramientas.getValueAt(fila, 0);
                        nombreHerramientaSeleccionada = panelInventario.tablaHerramientas.getValueAt(fila, 1).toString();
                        
                        // Llamamos al cuadro de diálogo directo
                        lanzarMenuDecisionMantenimiento();
                    }
                }
            });

        } catch (NullPointerException npe) {
            System.err.println("Error al enlazar los listeners del Supervisor: " + npe.getMessage());
        }
    }

    /**
     * Muestra las opciones de mantenimiento usando los botones integrados de un JOptionPane.
     */
    private void lanzarMenuDecisionMantenimiento() {
        // Personalizamos las opciones del cuadro de diálogo
        String[] opciones = {"Registrar Ejecutado", "Programar Agenda", "Cancelar"};
        
        int seleccion = JOptionPane.showOptionDialog(panelInventario,
                "¿Qué acción de mantenimiento desea gestionar para:\n" + nombreHerramientaSeleccionada + "?",
                "NEXUS GO - Gestión de Mantenimiento",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opciones, 
                opciones[0]);

        // Opción 0: Registrar Ejecutado
        if (seleccion == 0) { 
            JOptionPane.showMessageDialog(panelInventario, 
                    "Abriendo registro inmediato de mantenimientos ejecutados para:\n" + nombreHerramientaSeleccionada, 
                    "Módulo Herramientas", JOptionPane.INFORMATION_MESSAGE);
            // Aquí puedes integrar el panel final de registro si lo creas después
        } 
        // Opción 1: Programar Agenda (Abre el panel del JSpinner)
        else if (seleccion == 1) { 
            panelProgramarMantenimiento.txtEquipo.setText(nombreHerramientaSeleccionada);
            panelProgramarMantenimiento.txtEquipo.setEditable(false); 
            cambiarPanelCentral(this.panelProgramarMantenimiento);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == vistaPrincipal.sidebar.bCasa) {
                cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
            }

            if (e.getSource() == vistaPrincipal.sidebar.bInventario) {
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
                listarHerramientasEnTabla();
            }

            // Botón Volver del formulario -> Regresa directamente a las pestañas del inventario
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
        Date fechaSeleccionada = (Date) panelProgramarMantenimiento.spinnerFechaHora.getValue();

        Calendar calSeleccionada = Calendar.getInstance();
        calSeleccionada.setTime(fechaSeleccionada);

        Calendar calLimiteHoy = Calendar.getInstance();
        calLimiteHoy.set(Calendar.HOUR_OF_DAY, 23);
        calLimiteHoy.set(Calendar.MINUTE, 59);
        calLimiteHoy.set(Calendar.SECOND, 59);

        if (calSeleccionada.before(calLimiteHoy)) {
            JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                    "Excepción de Agenda: No se puede programar mantenimiento para hoy mismo ni para fechas pasadas.\nDebe agendarse con un mínimo de anticipación a partir de mañana.",
                    "Fecha No Permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                "Mantenimiento para la herramienta '" + nombreHerramientaSeleccionada + "' programado con éxito.\nFecha asignada: " + sdf.format(fechaSeleccionada),
                "Programación Guardada", JOptionPane.INFORMATION_MESSAGE);

        // Regresar al inventario
        cambiarPanelCentral(this.panelInventario);
        listarHerramientasEnTabla();
    }

    public void listarProductosEnTabla() {
        try {
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Tipo"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
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
                @Override public boolean isCellEditable(int row, int column) { return false; }
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
        try {
            vistaPrincipal.getContenidoCentralDinamico().removeAll();
            vistaPrincipal.getContenidoCentralDinamico().add(panelNuevo, java.awt.BorderLayout.CENTER);
            vistaPrincipal.getContenidoCentralDinamico().revalidate();
            vistaPrincipal.getContenidoCentralDinamico().repaint();
        } catch (Exception e) {
            System.err.println("Error en enrutador de vistas de Supervisor: " + e.getMessage());
        }
    }
   
    
    
}
