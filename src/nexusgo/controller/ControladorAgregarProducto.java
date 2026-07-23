/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaAgregarProducto;

/**
 *
 * @author USUARIO
 */
public class ControladorAgregarProducto implements ActionListener {

    private final VistaAgregarProducto panelFormulario;
    private final JPanel contenedorCentral;
    private final JPanel panelInventarioPadre;
    private final ProductoDao productoDao;

    private int idSeleccionado = -1;
    private Runnable alVolverCallback; // Permite refrescar la tabla del panel principal al regresar

    // Constructor para REGISTRAR un producto nuevo
    public ControladorAgregarProducto(VistaAgregarProducto panelFormulario, JPanel contenedorCentral, JPanel panelInventarioPadre, Runnable alVolverCallback) {
        this.panelFormulario = panelFormulario;
        this.contenedorCentral = contenedorCentral;
        this.panelInventarioPadre = panelInventarioPadre;
        this.alVolverCallback = alVolverCallback;
        this.productoDao = new ProductoDao();

        limpiarCamposFormularioProducto();
        this.panelFormulario.btnEditar.setText("Guardar");

        inicializarListeners();
    }

    // Constructor sobrecargado para EDITAR un producto existente
    public ControladorAgregarProducto(VistaAgregarProducto panelFormulario, JPanel contenedorCentral, JPanel panelInventarioPadre, int idProducto, Runnable alVolverCallback) {
        this.panelFormulario = panelFormulario;
        this.contenedorCentral = contenedorCentral;
        this.panelInventarioPadre = panelInventarioPadre;
        this.idSeleccionado = idProducto;
        this.alVolverCallback = alVolverCallback;
        this.productoDao = new ProductoDao();

        cargarDatosProducto(idProducto);
        this.panelFormulario.btnEditar.setText("Editar");

        inicializarListeners();
    }

    private void inicializarListeners() {
        /* Como panelFormulario se reutiliza cada vez que se abre esta pantalla,
        primero quitamos cualquier listener que haya quedado pegado de una
        apertura anterior,(PORQUE YA ME PASO) para que solo quede activo el de esta instancia actual.*/
        if (panelFormulario.btnVolver != null) {
            for (ActionListener al : panelFormulario.btnVolver.getActionListeners()) {
                panelFormulario.btnVolver.removeActionListener(al);
            }
            panelFormulario.btnVolver.addActionListener(this);
        }
        if (panelFormulario.btnEditar != null) {
            for (ActionListener al : panelFormulario.btnEditar.getActionListeners()) {
                panelFormulario.btnEditar.removeActionListener(al);
            }
            panelFormulario.btnEditar.addActionListener(this);
        }
        if (panelFormulario.btnImagen != null) {
            for (ActionListener al : panelFormulario.btnImagen.getActionListeners()) {
                panelFormulario.btnImagen.removeActionListener(al);
            }
            panelFormulario.btnImagen.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == panelFormulario.btnImagen) {
            buscarYCopiarImagen();
        } else if (src == panelFormulario.btnEditar) {
            if ("Guardar".equals(panelFormulario.btnEditar.getText())) {
                registrarNuevoProducto();
            } else {
                actualizarProducto();
            }
        } else if (src == panelFormulario.btnVolver) {
            volverAlPanelPrincipal();
        }
    }

    private void cargarDatosProducto(int id) {
        Producto p = productoDao.buscarPorId(id);
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
    }

    private void registrarNuevoProducto() {
        try {
            Producto nuevoProducto = extraermeObjetoProductoFormulario();

            if (productoDao.agregar(nuevoProducto) > 0) {
                JOptionPane.showMessageDialog(panelFormulario, "¡Insumo registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                volverAlPanelPrincipal();
            } else {
                JOptionPane.showMessageDialog(panelFormulario, "No se pudo registrar el producto en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panelFormulario, "Por favor revisa los números/precios ingresados.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelFormulario, "Campos inválidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        try {
            Producto p = extraermeObjetoProductoFormulario();
            p.setIdProducto(idSeleccionado);

            if (productoDao.editar(p) > 0) {
                JOptionPane.showMessageDialog(panelFormulario, "¡Insumo modificado correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                volverAlPanelPrincipal();
            } else {
                JOptionPane.showMessageDialog(panelFormulario, "No se pudo actualizar el producto en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panelFormulario, "Por favor revisa los números/precios ingresados.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelFormulario, "Error al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para evitar duplicidad de lectura de formulario
    private Producto extraermeObjetoProductoFormulario() {
        Producto p = new Producto();
        p.setNombreProducto(panelFormulario.txtNombre.getText().trim());
        p.setDescripcion(panelFormulario.txtDescripcion.getText().trim());
        p.setStockActual(Integer.parseInt(panelFormulario.txtCantidad.getText().trim()));
        
        String stockMinTexto = panelFormulario.txtStockMinimo.getText().trim();
        p.setStockMinimo(stockMinTexto.isEmpty() ? 0 : Integer.parseInt(stockMinTexto));

        // Limpieza de moneda (soporta formato $1.000,00 o $1000.00)
        String precioLimpio = panelFormulario.txtPrecio.getText().replaceAll("[^0-9,.]", "").replace(",", ".").trim();
        double precioCompra = Double.parseDouble(precioLimpio);
        p.setPrecioCompra(precioCompra);

        String precioVentaLimpio = panelFormulario.txtPrecioVenta.getText().replaceAll("[^0-9,.]", "").replace(",", ".").trim();
        double precioVenta = precioVentaLimpio.isEmpty() ? precioCompra : Double.parseDouble(precioVentaLimpio);
        p.setPrecioVenta(precioVenta);

        p.setProveedor(panelFormulario.txtProveedor.getText().trim());
        p.setUrlImagen(panelFormulario.lblNombreImagen.getText());

        return p;
    }

    private void buscarYCopiarImagen() {
        JFileChooser selector = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png");
        selector.setFileFilter(filtro);

        int resultado = selector.showOpenDialog(panelFormulario);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                File archivoSeleccionado = selector.getSelectedFile();
                String nombreOriginal = archivoSeleccionado.getName();
                String nombreLimpio = System.currentTimeMillis() + "_prod_" + nombreOriginal.replaceAll("\\s+", "_");

                Path directorioDestino = Paths.get("img");
                if (!Files.exists(directorioDestino)) {
                    Files.createDirectories(directorioDestino);
                }

                Path destino = directorioDestino.resolve(nombreLimpio);
                Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                panelFormulario.lblNombreImagen.setText(nombreLimpio);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelFormulario, "Error de transferencia de archivo: " + ex.getMessage(), "Error de Imagen", JOptionPane.ERROR_MESSAGE);
            }
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

    private void volverAlPanelPrincipal() {
        if (contenedorCentral != null) {
            contenedorCentral.removeAll();
            contenedorCentral.setLayout(new BorderLayout());
            contenedorCentral.add(panelInventarioPadre, BorderLayout.CENTER);
            contenedorCentral.revalidate();
            contenedorCentral.repaint();

            if (alVolverCallback != null) {
                alVolverCallback.run(); // Ejecuta el refresco de tabla en el controlador principal
            }
        }
    }
}
