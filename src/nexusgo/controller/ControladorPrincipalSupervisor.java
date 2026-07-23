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
import nexusgo.view.VistaRealizacionMantenimiento;

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

    // Estado interno
    private int idCajaActual = 0;
    private int idHerramientaSeleccionada = -1;
    private String nombreHerramientaSeleccionada = "";

    public ControladorPrincipalSupervisor(VistaPrincipalSupervisor vistaPrincipal, Usuario usuarioLogueado) {
        this.vistaPrincipal = vistaPrincipal;
        this.usuarioLogueado = usuarioLogueado;

        try {
            this.panelInventario = new VistaInventarioSupervisor();
            this.panelProgramarMantenimiento = new VistaProgramarMantenimiento();
            this.panelAperturaCierre = new AperturaCierre();

            this.idCajaActual = cajaDao.obtenerCajaAbierta();

            if (this.idCajaActual > 0) {
                double montoApertura = cajaDao.obtenerMontoApertura(this.idCajaActual);
                panelAperturaCierre.getLbltxtMontoA().setText(String.format("$%,.2f", montoApertura));
            }

            inicializarListeners();
            listarProductosEnTabla();
            listarHerramientasEnTabla();

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
            this.vistaPrincipal.sidebar.bCasa.addActionListener(this);
            this.vistaPrincipal.sidebar.bInventario.addActionListener(this);
            this.vistaPrincipal.sidebar.misCitas.addActionListener(this);
            this.vistaPrincipal.btnCerrarSesion.addActionListener(this);

            if (this.vistaPrincipal.btnCaja != null) {
                this.vistaPrincipal.btnCaja.addActionListener(this);
            }

            this.panelAperturaCierre.getBtnApertura().addActionListener(this);
            this.panelAperturaCierre.getBtnCalcular().addActionListener(this);

            this.panelProgramarMantenimiento.btnGuardarMantenimiento.addActionListener(this);
            this.panelProgramarMantenimiento.btnVolver.addActionListener(this);

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
            System.err.println("Error al vincular eventos en el controlador: " + npe.getMessage());
        }
    }

    private void lanzarMenuDecisionMantenimiento() {
        String[] opciones = {"Confirmacion de Mantenimiento", "Programar de Mantenimiento", "Cancelar"};

        int seleccion = JOptionPane.showOptionDialog(panelInventario,
                "¿Qué acción desea ejecutar para la herramienta:\n" + nombreHerramientaSeleccionada + "?",
                "NEXUS GO - Gestión de Mantenimiento",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == 0) {
            // Opción 1: Realizar Mantenimiento (Ejecución inmediata)
            abrirVistaRealizacionMantenimiento();
        } else if (seleccion == 1) {
            // Opción 2: Registrar Mantenimiento (Programación a futuro)
            panelProgramarMantenimiento.txtEquipo.setText(nombreHerramientaSeleccionada);
            cambiarPanelCentral(this.panelProgramarMantenimiento);
        }
    }

    private void abrirVistaRealizacionMantenimiento() {
        VistaRealizacionMantenimiento panelRealizar = new VistaRealizacionMantenimiento();

        // Cargar lista actualizada de herramientas en el JComboBox
        List<Herramientas> listaHerramientas = herramientaDao.listar();
        panelRealizar.cargarHerramientas(listaHerramientas);

        // Preseleccionar la herramienta que se clickeó en la tabla
        if (idHerramientaSeleccionada > 0) {
            panelRealizar.seleccionarHerramientaPorId(idHerramientaSeleccionada);
        }

        panelRealizar.btnVolver.addActionListener(e -> {
            cambiarPanelCentral(this.panelInventario);
            listarHerramientasEnTabla();
        });

        panelRealizar.btnGuardar.addActionListener(e -> {
            Herramientas herramientaElegida = panelRealizar.getHerramientaSeleccionada();

            if (herramientaElegida == null) {
                JOptionPane.showMessageDialog(panelRealizar,
                        "Por favor seleccione una herramienta de la lista desplegable.",
                        "Selección Requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String descripcionTrabajo = panelRealizar.txtDescripcionTrabajo.getText().trim();
            String horas = panelRealizar.txtHorasInvertidas.getText().trim();
            String observaciones = panelRealizar.txtObservaciones.getText().trim();

            if (descripcionTrabajo.isEmpty() || horas.isEmpty()) {
                JOptionPane.showMessageDialog(panelRealizar,
                        "Por favor complete la descripción del trabajo realizado y las horas invertidas.",
                        "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            File imgAntes = panelRealizar.getArchivoImagenAntes();
            File imgDespues = panelRealizar.getArchivoImagenDespues();

            String nombreAntes = (imgAntes != null) ? imgAntes.getName() : "Sin foto";
            String nombreDespues = (imgDespues != null) ? imgDespues.getName() : "Sin foto";

            String detalleNotas = "Ejecutado: " + descripcionTrabajo + " | Horas: " + horas + "h | Obs: "
                    + observaciones + " | Antes: " + nombreAntes + " | Desp: " + nombreDespues;

            // 1. Guardar el registro de mantenimiento realizado
            Mantenimiento mEjecutado = new Mantenimiento(
                    herramientaElegida.getIdHerramienta(),
                    "Mantenimiento Realizado Inmediato",
                    new Date(),
                    detalleNotas,
                    usuarioLogueado.getIdUsuario()
            );

            boolean guardado = mantenimientoDao.registrarProgramacion(mEjecutado);

            if (guardado) {
                // 2. Cambiar el estado de la herramienta en la BD (ej. "Ocupado" o "En Mantenimiento")
                String nuevoEstado = "Ocupado"; 
                boolean estadoActualizado = herramientaDao.actualizarEstado(herramientaElegida.getIdHerramienta(), nuevoEstado);

                String msgEstado = estadoActualizado 
                        ? "\nEstado de la herramienta actualizado a: '" + nuevoEstado + "'." 
                        : "\n(No se pudo actualizar el estado de la herramienta).";

                JOptionPane.showMessageDialog(panelRealizar,
                        "¡Mantenimiento de '" + herramientaElegida.getNombreHerramienta() + "' registrado exitosamente!" + msgEstado,
                        "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);

                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            } else {
                JOptionPane.showMessageDialog(panelRealizar,
                        "Error al guardar el registro del mantenimiento en la base de datos.",
                        "Error de BD", JOptionPane.ERROR_MESSAGE);
            }
        });

        cambiarPanelCentral(panelRealizar);
    }

    private void mostrarInicio() {
        PanelBienvenida bienvenida = new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol());
        cambiarPanelCentral(bienvenida);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == vistaPrincipal.sidebar.bCasa) {
                mostrarInicio();
            }

            if (e.getSource() == vistaPrincipal.btnCerrarSesion) {
                ejecutarCerrarSesion();
            }

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

            if (e.getSource() == vistaPrincipal.sidebar.misCitas) {
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
                listarHerramientasEnTabla();
            }

            if (e.getSource() == vistaPrincipal.btnCaja) {
                cambiarPanelCentral(this.panelAperturaCierre);
            }

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
        limpio = limpio.replace(".", "");
        limpio = limpio.replace(",", ".");
        return Double.parseDouble(limpio);
    }

    private void ejecutarGuardadoProgramacion() {
        try {
            Date fechaCalendario = panelProgramarMantenimiento.fechaProgramacion.getDate();

            if (fechaCalendario == null) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Por favor seleccione una fecha válida en el calendario.",
                        "Fecha Vacía", JOptionPane.WARNING_MESSAGE);
                return;
            }

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

            String tipoMantenimiento = panelProgramarMantenimiento.cbTipoMantenimiento.getSelectedItem().toString();
            String fallaProblema = panelProgramarMantenimiento.txtFallaProblema.getText().trim();
            String observaciones = (panelProgramarMantenimiento.txtObservaciones != null)
                    ? panelProgramarMantenimiento.txtObservaciones.getText().trim() : "";

            File imagenAdjunta = panelProgramarMantenimiento.getArchivoImagenSeleccionado();
            String nombreImagen = (imagenAdjunta != null) ? imagenAdjunta.getName() : "Sin imagen";

            String notasCompletas = "Falla: " + fallaProblema + " | Obs: " + observaciones + " | Img: " + nombreImagen;

            if (tipoMantenimiento.equals("Seleccione su tipo de mantenimiento") || fallaProblema.isEmpty()) {
                JOptionPane.showMessageDialog(panelProgramarMantenimiento,
                        "Debe seleccionar el tipo de mantenimiento y describir la falla/problema.",
                        "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

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
