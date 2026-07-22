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
import nexusgo.view.VistaAgregarProducto;
import nexusgo.view.VistaOperarioInventario;
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
    private final JPanel contenedorCentral;
    private final Usuario usuarioLogueado;

    // Paneles de formulario manejados centralmente
    private final VistaAgregarProducto panelFormulario;
    private final VistaAgregarHerramienta panelFormularioHerramienta;
    private final VistaRegistrarSalida panelSalidaInsumo;

    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private int idSeleccionado = -1;

    public ControladorInventarioOperario(VistaOperarioInventario panelInventario, Usuario usuarioLogueado, JPanel contenedorCentral) {
        this.panelInventario = panelInventario;
        this.usuarioLogueado = usuarioLogueado;
        this.contenedorCentral = contenedorCentral;

        // 1. Instanciación centralizada de sub-vistas
        this.panelFormulario = new VistaAgregarProducto();
        this.panelFormularioHerramienta = new VistaAgregarHerramienta();
        this.panelSalidaInsumo = new VistaRegistrarSalida();

        // 2. Vinculación de eventos y carga inicial
        inicializarListeners();
        listarProductosEnTabla();
        listarHerramientasEnTabla();
        aplicarPermisosPorRol();
    }

    private void aplicarPermisosPorRol() {
        if (panelInventario.btnAgregarProducto != null) panelInventario.btnAgregarProducto.setVisible(true);
        if (panelInventario.btnAgregarHerramienta != null) panelInventario.btnAgregarHerramienta.setVisible(true);
    }

    private void inicializarListeners() {
        // Listeners del panel principal de inventario
        if (panelInventario.btnAgregarProducto != null) panelInventario.btnAgregarProducto.addActionListener(this);
        if (panelInventario.btnAgregarHerramienta != null) panelInventario.btnAgregarHerramienta.addActionListener(this);
        if (panelInventario.cerrarSesion != null) panelInventario.cerrarSesion.addActionListener(this);

        // Listeners de tablas
        if (panelInventario.tablaProductos != null) {
            panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaProductos.getSelectedRow();
                    if (fila >= 0) lanzarMenuDecision("Producto", fila);
                }
            });
        }

        if (panelInventario.tablaHerramientas != null) {
            panelInventario.tablaHerramientas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaHerramientas.getSelectedRow();
                    if (fila >= 0) lanzarMenuDecision("Herramienta", fila);
                }
            });
        }

        // Listeners del formulario de productos
        if (panelFormulario.btnVolver != null) panelFormulario.btnVolver.addActionListener(this);
        if (panelFormulario.btnEditar != null) panelFormulario.btnEditar.addActionListener(this);
        if (panelFormulario.btnImagen != null) panelFormulario.btnImagen.addActionListener(this);

        // Listeners del formulario de herramientas
        if (panelFormularioHerramienta.btnVolver != null) panelFormularioHerramienta.btnVolver.addActionListener(this);
        if (panelFormularioHerramienta.btnEditar != null) panelFormularioHerramienta.btnEditar.addActionListener(this);
        if (panelFormularioHerramienta.btnImagen != null) panelFormularioHerramienta.btnImagen.addActionListener(this);

        // Listeners del formulario de salida
        if (panelSalidaInsumo.btnRegistrarSalida != null) panelSalidaInsumo.btnRegistrarSalida.addActionListener(this);
        if (panelSalidaInsumo.btnVolver != null) panelSalidaInsumo.btnVolver.addActionListener(this);
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
                JOptionPane.showMessageDialog(panelInventario, "Las herramientas cambian por estado físico, no numérico.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (seleccion == 1) { // Editar
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);
                Producto p = productoDao.buscarPorId(idSeleccionado);
                if (p != null) {
                    panelFormulario.txtNombre.setText(p.getNombreProducto());
                    panelFormulario.txtDescripcion.setText(p.getDescripcion() != null ? p.getDescripcion() : "");
                    panelFormulario.txtCantidad.setText(String.valueOf(p.getStockActual()));
                    panelFormulario.txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
                    panelFormulario.txtPrecio.setText(String.valueOf(p.getPrecioCompra()));
                    panelFormulario.txtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
                    panelFormulario.txtProveedor.setText(p.getProveedor() != null ? p.getProveedor() : "");
                    panelFormulario.lblNombreImagen.setText(p.getUrlImagen() != null ? p.getUrlImagen() : "sin_imagen.jpg");
                }
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
        Object src = e.getSource();

        if (src == panelInventario.cerrarSesion) {
            ejecutarCerrarSesion();
        } else if (src == panelInventario.btnAgregarProducto) {
            limpiarCamposFormularioProducto();
            panelFormulario.btnEditar.setText("Guardar");
            cambiarPanelCentral(this.panelFormulario);
        } else if (src == panelFormulario.btnImagen) {
            buscarYCopiarImagen("producto");
        } else if (src == panelFormulario.btnEditar) {
            if ("Guardar".equals(panelFormulario.btnEditar.getText())) {
                registrarNuevoProducto();
            } else {
                actualizarProducto();
            }
        } else if (src == panelFormulario.btnVolver) {
            cambiarPanelCentral(this.panelInventario);
            listarProductosEnTabla();
        } else if (src == panelInventario.btnAgregarHerramienta) {
            limpiarCamposFormularioHerramienta();
            panelFormularioHerramienta.btnEditar.setText("Guardar");
            cambiarPanelCentral(this.panelFormularioHerramienta);
        } else if (src == panelFormularioHerramienta.btnImagen) {
            buscarYCopiarImagen("herramienta");
        } else if (src == panelFormularioHerramienta.btnEditar) {
            if ("Guardar".equals(panelFormularioHerramienta.btnEditar.getText())) {
                registrarNuevaHerramienta();
            } else {
                actualizarHerramienta();
            }
        } else if (src == panelFormularioHerramienta.btnVolver) {
            cambiarPanelCentral(this.panelInventario);
            listarHerramientasEnTabla();
        } else if (src == panelSalidaInsumo.btnRegistrarSalida) {
            ejecutarRestaDeStock();
        } else if (src == panelSalidaInsumo.btnVolver) {
            cambiarPanelCentral(this.panelInventario);
            listarProductosEnTabla();
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {
        if (contenedorCentral != null) {
            contenedorCentral.removeAll();
            contenedorCentral.setLayout(new BorderLayout());
            contenedorCentral.add(panelNuevo, BorderLayout.CENTER);
            contenedorCentral.revalidate();
            contenedorCentral.repaint();
        } else {
            System.err.println("Error: 'contenedorCentral' es nulo en el controlador.");
        }
    }

    // Métodos auxiliares CRUD (sin cambios estructurales necesarios)
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
            double precioCompra = Double.parseDouble(precioLimpio);
            nuevoProducto.setPrecioCompra(precioCompra);

            String precioVentaLimpio = panelFormulario.txtPrecioVenta.getText().replace("$", "").replace(".", "").trim();
            double precioVenta = precioVentaLimpio.isEmpty() ? precioCompra : Double.parseDouble(precioVentaLimpio);
            nuevoProducto.setPrecioVenta(precioVenta);

            nuevoProducto.setProveedor(panelFormulario.txtProveedor.getText().trim());
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
            double precioCompra = Double.parseDouble(precioLimpio);
            p.setPrecioCompra(precioCompra);

            String precioVentaLimpio = panelFormulario.txtPrecioVenta.getText().replace("$", "").replace(".", "").trim();
            double precioVenta = precioVentaLimpio.isEmpty() ? precioCompra : Double.parseDouble(precioVentaLimpio);
            p.setPrecioVenta(precioVenta);

            p.setProveedor(panelFormulario.txtProveedor.getText().trim());
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
            nuevaHerramienta.setEstadoActual("EXCELENTE");

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
            h.setEstadoActual("EXCELENTE");

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

                // Crear carpeta externa si no existe para asegurar persistencia fuera del archivo JAR
                Path directorioDestino = Paths.get("img");
                if (!Files.exists(directorioDestino)) {
                    Files.createDirectories(directorioDestino);
                }

                Path destino = directorioDestino.resolve(nombreLimpio);
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
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio Compra", "Stock Actual", "Stock Mínimo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            panelInventario.tablaProductos.setModel(modeloBlindado);
            List<Producto> lista = productoDao.listar();
            if (lista != null) {
                for (Producto p : lista) {
                    modeloBlindado.addRow(new Object[]{p.getIdProducto(), p.getNombreProducto(), p.getPrecioCompra(), p.getStockActual(), p.getStockMinimo()});
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
                    modeloBlindado.addRow(new Object[]{h.getIdHerramienta(), h.getNombreHerramienta(), h.getEstadoActual(), "Herramienta"});
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
        panelFormulario.txtPrecioVenta.setText("");
        panelFormulario.txtProveedor.setText("");
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
            Object ventana = panelInventario.getTopLevelAncestor();
            if (ventana != null) {
                try {
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
}
