/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//
package nexusgo.controller;

import java.awt.BorderLayout;
import nexusgo.model.Herramientas;
import nexusgo.model.HerramientaDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaAgregarProducto;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.view.VistaPrincipalOperario;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import nexusgo.model.Usuario;
import nexusgo.view.VistaAgregarHerramienta;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaRegistrarSalida;

/**
 *
 * @author USUARIO
 */
public class ControladorInventarioOperario implements ActionListener {

   private final VistaOperarioInventario panelInventario;
    private final JPanel contenedorCentral; // <--- Referencia segura, directa y definitiva
    private final Usuario usuarioLogueado;

    private VistaAgregarProducto panelFormulario;
    private VistaAgregarHerramienta panelFormularioHerramienta;
    private VistaRegistrarSalida panelSalidaInsumo;

    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private int idSeleccionado = -1;

    /**
     * CONSTRUCTOR DEFINITIVO: Ahora recibe obligatoriamente el contenedor central de la app
     * para realizar transiciones de pantalla seguras y limpias sin recurrir a getParent().
     */
    public ControladorInventarioOperario(VistaOperarioInventario panelInventario, Usuario usuarioLogueado, JPanel contenedorCentral) {
        this.panelInventario = panelInventario;
        this.usuarioLogueado = usuarioLogueado;
        this.contenedorCentral = contenedorCentral; // Guardamos la referencia segura del contenedor principal

        try {
            // Inicialización de sub-formularios
            this.panelFormulario = new VistaAgregarProducto();
            this.panelFormularioHerramienta = new VistaAgregarHerramienta();
            this.panelSalidaInsumo = new VistaRegistrarSalida();

            inicializarListeners();

            // Carga inicial de datos desde MySQL
            listarProductosEnTabla();
            listarHerramientasEnTabla();

            // Configura la visibilidad de botones según rol
            aplicarPermisosPorRol();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar los módulos de inventario: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarPermisosPorRol() {
        // Ambos roles tienen acceso total y visibilidad de los botones de creación
        panelInventario.btnAgregarProducto.setVisible(true);
        panelInventario.btnAgregarHerramienta.setVisible(true);
    }

    private void inicializarListeners() {
        try {
            // Quitamos la barra lateral de aquí (ya la maneja el ControladorPrincipal)
            this.panelInventario.btnAgregarProducto.addActionListener(this);
            this.panelInventario.btnAgregarHerramienta.addActionListener(this);
            this.panelInventario.cerrarSesion.addActionListener(this);

            // Clics en la tabla de productos / insumos
            this.panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaProductos.getSelectedRow();
                    if (fila >= 0) {
                        lanzarMenuDecision("Producto", fila);
                    }
                }
            });

            // Clics en la tabla de herramientas
            this.panelInventario.tablaHerramientas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaHerramientas.getSelectedRow();
                    if (fila >= 0) {
                        lanzarMenuDecision("Herramienta", fila);
                    }
                }
            });

            this.panelFormulario.btnVolver.addActionListener(this);
            this.panelFormulario.btnEditar.addActionListener(this);
            this.panelFormulario.btnImagen.addActionListener(this);

            this.panelFormularioHerramienta.btnVolver.addActionListener(this);
            this.panelFormularioHerramienta.btnEditar.addActionListener(this);
            this.panelFormularioHerramienta.btnImagen.addActionListener(this);

            this.panelSalidaInsumo.btnRegistrarSalida.addActionListener(this);
            this.panelSalidaInsumo.btnVolver.addActionListener(this);

        } catch (NullPointerException npe) {
            System.err.println("Error al enlazar los listeners del controlador: " + npe.getMessage());
        }
    }

    private void lanzarMenuDecision(String tipo, int fila) {
        String[] opciones = {"Registrar Salida", "Editar", "Eliminar"};
        int seleccion = JOptionPane.showOptionDialog(panelInventario,
                "¿Qué acción desea realizar con el registro seleccionado?",
                "NEXUS - Panel de Control",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) { // Registrar Salida
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);
                panelSalidaInsumo.txtCantidadSalida.setText("");
                cambiarPanelCentral(this.panelSalidaInsumo);
            } else {
                JOptionPane.showMessageDialog(panelInventario, "Las herramientas cambian por estado físico, no numérico.");
            }
        } else if (seleccion == 1) { // Editar
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);
                panelFormulario.txtNombre.setText(panelInventario.tablaProductos.getValueAt(fila, 1).toString());
                panelFormulario.txtCantidad.setText(panelInventario.tablaProductos.getValueAt(fila, 3).toString());
                panelFormulario.txtPrecio.setText(panelInventario.tablaProductos.getValueAt(fila, 2).toString());
                panelFormulario.txtDescripcion.setText("");
                panelFormulario.txtStockMinimo.setText("");
                panelFormulario.btnEditar.setText("Editar");
                cambiarPanelCentral(this.panelFormulario);
            } else {
                idSeleccionado = (int) panelInventario.tablaHerramientas.getValueAt(fila, 0);
                panelFormularioHerramienta.txtIdHerramienta.setText(String.valueOf(idSeleccionado));
                panelFormularioHerramienta.txtIdHerramienta.setEditable(false);
                panelFormularioHerramienta.txtNombre.setText(panelInventario.tablaHerramientas.getValueAt(fila, 1).toString());
                panelFormularioHerramienta.btnEditar.setText("Editar");
                cambiarPanelCentral(this.panelFormularioHerramienta);
            }
        } else if (seleccion == 2) { // Eliminar
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);
                eliminarProducto(idSeleccionado);
            } else {
                idSeleccionado = (int) panelInventario.tablaHerramientas.getValueAt(fila, 0);
                eliminarHerramienta(idSeleccionado);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == panelInventario.cerrarSesion) {
                ejecutarCerrarSesion();
            }

            if (e.getSource() == panelInventario.btnAgregarProducto) {
                limpiarCamposFormularioProducto();
                panelFormulario.btnEditar.setText("Guardar");
                cambiarPanelCentral(this.panelFormulario);
            }

            if (e.getSource() == panelFormulario.btnImagen) {
                buscarYCopiarImagen("producto");
            }

            if (e.getSource() == panelFormulario.btnEditar) {
                if (panelFormulario.btnEditar.getText().equals("Guardar")) {
                    registrarNuevoProducto();
                } else {
                    actualizarProducto();
                }
            }

            if (e.getSource() == panelFormulario.btnVolver) {
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
            }

            if (e.getSource() == panelInventario.btnAgregarHerramienta) {
                limpiarCamposFormularioHerramienta();
                panelFormularioHerramienta.btnEditar.setText("Guardar");
                cambiarPanelCentral(this.panelFormularioHerramienta);
            }

            if (e.getSource() == panelFormularioHerramienta.btnImagen) {
                buscarYCopiarImagen("herramienta");
            }

            if (e.getSource() == panelFormularioHerramienta.btnEditar) {
                if (panelFormularioHerramienta.btnEditar.getText().equals("Guardar")) {
                    registrarNuevaHerramienta();
                } else {
                    actualizarHerramienta();
                }
            }

            if (e.getSource() == panelFormularioHerramienta.btnVolver) {
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            }

            if (e.getSource() == panelSalidaInsumo.btnRegistrarSalida) {
                ejecutarRestaDeStock();
            }

            if (e.getSource() == panelSalidaInsumo.btnVolver) {
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
            }

        } catch (Exception ex) {
            System.err.println("Error de control en eventos: " + ex.getMessage());
        }
    }

    private void ejecutarRestaDeStock() {
        try {
            int cantidadARestar = Integer.parseInt(panelSalidaInsumo.txtCantidadSalida.getText().trim());
            if (cantidadARestar <= 0) {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "La cantidad debe ser mayor a cero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (productoDao.registrarSalidaStock(idSeleccionado, cantidadARestar)) {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "¡Transacción exitosa! El stock se actualizó.");
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
            } else {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "Error: Inventario insuficiente.", "Aviso", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(panelSalidaInsumo, "Ingrese un número entero válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevoProducto() {
        try {
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombreProducto(panelFormulario.txtNombre.getText().trim());
            nuevoProducto.setDescripcion(panelFormulario.txtDescripcion.getText().trim());
            nuevoProducto.setStockActual(Integer.parseInt(panelFormulario.txtCantidad.getText().trim()));
            nuevoProducto.setStockMinimo(panelFormulario.txtStockMinimo.getText().trim().isEmpty() ? 0 : Integer.parseInt(panelFormulario.txtStockMinimo.getText().trim()));

            String precioLimpio = panelFormulario.txtPrecio.getText().replace("$", "").replace(".", "").trim();
            nuevoProducto.setPrecioCompra(Double.parseDouble(precioLimpio));
            nuevoProducto.setUrlImagen(panelFormulario.lblNombreImagen.getText());

            if (productoDao.agregar(nuevoProducto) > 0) {
                JOptionPane.showMessageDialog(panelFormulario, "¡Insumo registrado con éxito!");
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelFormulario, "Campos inválidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        try {
            Producto p = new Producto();
            p.setIdProducto(idSeleccionado);
            p.setNombreProducto(panelFormulario.txtNombre.getText().trim());
            p.setDescripcion(panelFormulario.txtDescripcion.getText().trim());
            p.setStockActual(Integer.parseInt(panelFormulario.txtCantidad.getText().trim()));
            p.setStockMinimo(panelFormulario.txtStockMinimo.getText().trim().isEmpty() ? 0 : Integer.parseInt(panelFormulario.txtStockMinimo.getText().trim()));

            String precioLimpio = panelFormulario.txtPrecio.getText().replace("$", "").replace(".", "").trim();
            p.setPrecioCompra(Double.parseDouble(precioLimpio));
            p.setUrlImagen(panelFormulario.lblNombreImagen.getText());

            if (productoDao.editar(p) > 0) {
                JOptionPane.showMessageDialog(panelFormulario, "¡Insumo modificado correctamente!");
                cambiarPanelCentral(this.panelInventario);
                listarProductosEnTabla();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelFormulario, "Error al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevaHerramienta() {
        try {
            Herramientas nuevaHerramienta = new Herramientas();
            nuevaHerramienta.setIdHerramienta(Integer.parseInt(panelFormularioHerramienta.txtIdHerramienta.getText().trim()));
            nuevaHerramienta.setNombreHerramienta(panelFormularioHerramienta.txtNombre.getText().trim());
            nuevaHerramienta.setEstadoActual("Excelente");

            if (herramientaDao.agregar(nuevaHerramienta) > 0) {
                JOptionPane.showMessageDialog(panelFormularioHerramienta, "¡Herramienta registrada exitosamente!");
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelFormularioHerramienta, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarHerramienta() {
        try {
            Herramientas h = new Herramientas();
            h.setIdHerramienta(idSeleccionado);
            h.setNombreHerramienta(panelFormularioHerramienta.txtNombre.getText().trim());
            h.setEstadoActual("Excelente");

            if (herramientaDao.editar(h) > 0) {
                JOptionPane.showMessageDialog(panelFormularioHerramienta, "¡Herramienta modificada correctamente!");
                cambiarPanelCentral(this.panelInventario);
                listarHerramientasEnTabla();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelFormularioHerramienta, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto(int id) {
        int confirmar = JOptionPane.showConfirmDialog(panelInventario, "¿Eliminar permanentemente este insumo?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmar == JOptionPane.YES_OPTION) {
            if (productoDao.eliminar(id) > 0) {
                listarProductosEnTabla();
            }
        }
    }

    private void eliminarHerramienta(int id) {
        int confirmar = JOptionPane.showConfirmDialog(panelInventario, "¿Eliminar permanentemente esta herramienta?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmar == JOptionPane.YES_OPTION) {
            if (herramientaDao.eliminar(id) > 0) {
                listarHerramientasEnTabla();
            }
        }
    }

    private void buscarYCopiarImagen(String tipoModulo) {
        JFileChooser selector = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png");
        selector.setFileFilter(filtro);

        JPanel panelPadre = tipoModulo.equals("producto") ? panelFormulario : panelFormularioHerramienta;
        int resultado = selector.showOpenDialog(panelPadre);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                File archivoSeleccionado = selector.getSelectedFile();
                String nombreOriginal = archivoSeleccionado.getName();
                String prefijo = tipoModulo.equals("producto") ? "prod_" : "herr_";
                String nombreLimpio = System.currentTimeMillis() + "_" + prefijo + nombreOriginal.replaceAll("\\s+", "_");

                Path destino = Paths.get("src/nexusgo/img/" + nombreLimpio);
                Files.createDirectories(destino.getParent());
                Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                if (tipoModulo.equals("producto")) {
                    panelFormulario.lblNombreImagen.setText(nombreLimpio);
                } else {
                    panelFormularioHerramienta.lblNombreImagen.setText(nombreLimpio);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelPadre, "Error de transferencia de archivo: " + ex.getMessage(), "Error de Imagen", JOptionPane.ERROR_MESSAGE);
            }
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

    private void limpiarCamposFormularioProducto() {
        panelFormulario.txtNombre.setText("");
        panelFormulario.txtDescripcion.setText("");
        panelFormulario.txtCantidad.setText("");
        panelFormulario.txtPrecio.setText("");
        panelFormulario.txtStockMinimo.setText("");
        panelFormulario.lblNombreImagen.setText("ningún archivo seleccionado");
    }

    private void limpiarCamposFormularioHerramienta() {
        panelFormularioHerramienta.txtIdHerramienta.setText("");
        panelFormularioHerramienta.txtIdHerramienta.setEditable(true);
        panelFormularioHerramienta.txtNombre.setText("");
        panelFormularioHerramienta.lblNombreImagen.setText("ningún archivo seleccionado");
    }

    private void ejecutarCerrarSesion() {
        int confirmar = JOptionPane.showConfirmDialog(null, "¿Desea cerrar sesión en NEXUS?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            // Guardamos la ventana en un Object genérico (sin imports)
            Object ventana = panelInventario.getTopLevelAncestor();
            if (ventana != null) {
                try {
                    // Buscamos y ejecutamos el método 'dispose' en tiempo de ejecución
                    ventana.getClass().getMethod("dispose").invoke(ventana);
                } catch (Exception ex) {
                    System.err.println("No se pudo cerrar la ventana: " + ex.getMessage());
                }
            }

            VistaInicioSesion loginVista = new VistaInicioSesion();
            new ControladorInicioSesion(loginVista);
            loginVista.setLocationRelativeTo(null);
            loginVista.setVisible(true);
        }
    }

    /**
     * MÉTODO DE NAVEGACIÓN CORREGIDO:
     * Utiliza la referencia directa y confiable 'contenedorCentral' recibida del menu principal.
     */
    private void cambiarPanelCentral(JPanel panelNuevo) {
        try {
            if (contenedorCentral != null) {
                contenedorCentral.removeAll();
                contenedorCentral.setLayout(new java.awt.BorderLayout());
                contenedorCentral.add(panelNuevo, BorderLayout.CENTER);
                contenedorCentral.revalidate();
                contenedorCentral.repaint();
            } else {
                System.err.println("Error: No se puede cambiar de sub-vista debido a que 'contenedorCentral' es nulo.");
            }
        } catch (Exception e) {
            System.err.println("Error en enrutador de vistas: " + e.getMessage());
        }
    }
}
