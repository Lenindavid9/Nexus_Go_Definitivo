package nexusgo.controller;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.DetalleCarrito;
import nexusgo.model.Factura;
import nexusgo.model.FacturaDao;
import nexusgo.model.Producto;
import nexusgo.view.VistaFactura;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaPdV;

public class ControladorPdV implements ActionListener {
    
    private final VistaPdV vista;
    private final FacturaDao facturaDao;
    private final ProductoDao productoDao;
    private final JPanel contenedorCentral;

    // TODO EL MODELO VIVE AQUÍ
    private final List<DetalleCarrito> carrito = new ArrayList<>();
    private final List<VistaPdV.TarjetaProductoComponentes> componentesTarjetas = new ArrayList<>();
    private double totalVenta = 0.0;
    private int contadorProductos = 0;

    public ControladorPdV(VistaPdV vista, JPanel contenedorCentral) {
        this.vista = vista;
        this.contenedorCentral = contenedorCentral;
        this.facturaDao = new FacturaDao();
        this.productoDao = new ProductoDao();

        // Enlazar eventos de los botones principales
        this.vista.getFacturarButton().addActionListener(this);
        this.vista.getReiniciarButton().addActionListener(this);

        // Cargar los productos dinámicamente
        cargarProductos();
    }

    private void cargarProductos() {
        List<Producto> productos = productoDao.listar();

        if (productos != null && !productos.isEmpty()) {
            for (Producto p : productos) {
                String precioFormateado = String.format("$%.0f", p.getPrecioCompra());
                String imagen = (p.getUrlImagen() != null && !p.getUrlImagen().isEmpty()) 
                                ? p.getUrlImagen() 
                                : "tratamiento.png";

                // Crear la tarjeta en la vista y recibir sus componentes interativos
                VistaPdV.TarjetaProductoComponentes componentes = vista.agregarTarjetaComponentes(
                        p.getNombreProducto(), 
                        precioFormateado, 
                        p.getStockActual(), 
                        imagen
                );

                componentesTarjetas.add(componentes);

                // Configurar el listener del botón Agregar directamente desde el controlador
                componentes.getBtnAgregar().addActionListener(e -> {
                    int cantidad = (int) componentes.getSpinner().getValue();
                    double precioUnitario = p.getPrecioCompra();

                    // Registrar en la lista carrito
                    carrito.add(new DetalleCarrito(p.getIdProducto(), p.getNombreProducto(), precioUnitario, cantidad));

                    // Actualizar contadores del controlador
                    totalVenta += (precioUnitario * cantidad);
                    contadorProductos += cantidad;

                    // Actualizar estado visual
                    vista.actualizarTextoFacturar(contadorProductos);
                    componentes.getBtnAgregar().setEnabled(false);
                });
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getFacturarButton()) {
            
            if (carrito.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El carrito está vacío. Agrega productos primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double subtotal = totalVenta;
            double descuento = 0.0;
            double total = subtotal - descuento;

            // Datos base del sistema
            int idVenta = 1; 
            int idCliente = 1; 
            int idCaja = 1; 

            // Crear modelo de Factura con todos los datos procesados
            Factura factura = new Factura(0, idVenta, idCliente, idCaja, subtotal, descuento, total, new Date());

            // Guardar factura en BD vía DAO
            boolean exito = facturaDao.guardarFactura(factura);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "Factura N° " + factura.getIdFactura() + " guardada exitosamente.");
                
                // Redirigir a la vista de Factura pasando la factura y la lista con el detalle del carrito
                VistaFactura vistaFactura = new VistaFactura(factura, carrito);
                cambiarPanel(vistaFactura);

                reiniciarCarrito();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al procesar la factura.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == vista.getReiniciarButton()) {
            reiniciarCarrito();
            JOptionPane.showMessageDialog(vista, "El carrito se ha reiniciado correctamente.");
        }
    }

    private void reiniciarCarrito() {
        carrito.clear();
        totalVenta = 0.0;
        contadorProductos = 0;
        vista.actualizarTextoFacturar(0);

        // Volver a habilitar todos los botones de las tarjetas
        for (VistaPdV.TarjetaProductoComponentes comp : componentesTarjetas) {
            comp.getBtnAgregar().setEnabled(true);
            comp.getSpinner().setValue(1);
        }
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        if (contenedorCentral != null) {
            contenedorCentral.removeAll();
            contenedorCentral.setLayout(new BorderLayout());
            contenedorCentral.add(nuevoPanel, BorderLayout.CENTER);
            contenedorCentral.revalidate();
            contenedorCentral.repaint();
        }
    }

  
}
