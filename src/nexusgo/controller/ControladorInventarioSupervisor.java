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

    // Instancia de los DAOs del proyecto (Cada uno para su respectiva tabla)
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
            // Inicialización de las vistas del módulo
            this.panelInventario = new VistaInventarioSupervisor();
            this.panelProgramarMantenimiento = new VistaProgramarMantenimiento();

            inicializarListeners();

            // Carga inicial de datos desde MySQL
            listarProductosEnTabla();
            listarHerramientasEnTabla();

            // Panel de bienvenida por defecto al cargar el sistema
            cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar el módulo de Supervisor: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarListeners() {
        try {
            // Escuchar clics en los botones de la barra lateral izquierda
            this.vistaPrincipal.sidebar.bCasa.addActionListener(this);
            this.vistaPrincipal.sidebar.bInventario.addActionListener(this);

            // Escuchar los botones de la vista sencilla de programación
            this.panelProgramarMantenimiento.btnGuardarMantenimiento.addActionListener(this);
            this.panelProgramarMantenimiento.btnVolver.addActionListener(this);

            // Clic en Productos -> Modo Solo Lectura para el Rol de Supervisor
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

            // Clic en Herramientas -> Dispara las opciones de gestión de mantenimiento
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

        if (seleccion == 1) { // Programar Agenda (Abre el formulario básico)
            panelProgramarMantenimiento.txtEquipo.setText(nombreHerramientaSeleccionada);
            cambiarPanelCentral(this.panelProgramarMantenimiento);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // --- EVENTOS DE NAVEGACIÓN ---
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

            // --- BOTÓN PRINCIPAL GUARDAR ---
            if (e.getSource() == panelProgramarMantenimiento.btnGuardarMantenimiento) {
                ejecutarGuardadoProgramacion();
            }

        } catch (Exception ex) {
            System.err.println("Error en enrutamiento de eventos: " + ex.getMessage());
        }
    }

    private void ejecutarGuardadoProgramacion() {
        try {
            // 1. Extraer los datos desde la interfaz de usuario sencilla
            Date fechaSeleccionada = (Date) panelProgramarMantenimiento.spinnerFechaHora.getValue(); 
            String tipoMantenimiento = panelProgramarMantenimiento.cbTipoMantenimiento.getSelectedItem().toString();
            String fallaProblema = panelProgramarMantenimiento.txtFallaProblema.getText().trim();
            String observaciones = panelProgramarMantenimiento.txtObservaciones.getText().trim();

            // Juntamos falla y observaciones en una sola cadena de texto para la BD
            String notasCompletas = "Falla: " + fallaProblema + " | Obs: " + observaciones;

            // 2. Validación obligatoria de campos vacíos
            if (tipoMantenimiento.equals("Seleccione su tipo de mantenimiento") || fallaProblema.isEmpty()) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor, seleccione un tipo de mantenimiento e ingrese la falla o problema.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Validación de Regra de Negocio para la Fecha (No hoy, no pasados)
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

            // 4. Instanciar la clase normal Mantenimiento con la sesión activa
            Mantenimiento nuevoMantenimiento = new Mantenimiento(
                    idHerramientaSeleccionada, 
                    tipoMantenimiento, 
                    fechaSeleccionada, 
                    notasCompletas, 
                    usuarioLogueado.getIdUsuario() // ID del supervisor que agendó en la BD
            );

            // 5. Enviar el objeto al nuevo MantenimientoDao independiente
            boolean guardadoExitoso = mantenimientoDao.registrarProgramacion(nuevoMantenimiento);

            if (guardadoExitoso) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "¡Mantenimiento programado con éxito en la Base de Datos!\n\n" +
                        "Herramienta: " + nombreHerramientaSeleccionada + "\n" +
                        "Tipo: " + tipoMantenimiento + "\n" +
                        "Fecha: " + sdf.format(fechaSeleccionada),
                        "NEXUS GO - Éxito", JOptionPane.INFORMATION_MESSAGE);

                // 6. Limpieza de interfaz y redirección al inventario
                limpiarCamposProgramacion();
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            } else {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "No se pudo guardar la programación. Revise la consola del servidor.",
                        "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelProgramarMantenimiento, 
                    "Error al procesar el guardado: " + ex.getMessage(), 
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
