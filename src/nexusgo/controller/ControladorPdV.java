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
    private JPanel contenedorCentral;

    // Estado del Carrito y Vista
    private final List<DetalleCarrito> carrito = new ArrayList<>();
    private final List<VistaPdV.TarjetaProductoComponentes> componentesTarjetas = new ArrayList<>();
    private double totalVenta = 0.0;
    private int contadorProductos = 0;

    // Constructor Principal (recibe el contenedor explícito)
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

    // Constructor Sobrecargado (Compatibilidad cuando no se pasa el contenedor)
    public ControladorPdV(VistaPdV vista) {
        this(vista, null);
    }

    private void cargarProductos() {
        List<Producto> productos = productoDao.listar();

        if (productos != null && !productos.isEmpty()) {
            for (Producto p : productos) {
                String precioFormateado = String.format("$%.0f", p.getPrecioCompra());
                String imagen = (p.getUrlImagen() != null && !p.getUrlImagen().isEmpty()) 
                                ? p.getUrlImagen() 
                                : "tratamiento.png";

                // Crear la tarjeta en la vista y recibir sus componentes interactivos
                VistaPdV.TarjetaProductoComponentes componentes = vista.agregarTarjetaComponentes(
                        p.getNombreProducto(), 
                        precioFormateado, 
                        p.getStockActual(), 
                        imagen
                );

                componentesTarjetas.add(componentes);

                // Configurar el listener del botón Agregar (+) para acumular dinámicamente
                componentes.getBtnAgregar().addActionListener(e -> {
                    int cantidadIngresada = (int) componentes.getSpinner().getValue();
                    double precioUnitario = p.getPrecioCompra();

                    // Sumar o actualizar en la lista del carrito
                    agregarOActualizarItem(p, cantidadIngresada, precioUnitario);

                    // Actualizar contadores globales del punto de venta
                    totalVenta += (precioUnitario * cantidadIngresada);
                    contadorProductos += cantidadIngresada;

                    // Actualizar el estado del botón Facturar en la vista
                    vista.actualizarTextoFacturar(contadorProductos);
                });
            }
        }
    }

    /**
     * Acumula la cantidad seleccionada en el producto existente dentro del carrito,
     * o agrega una nueva línea si no había sido seleccionado antes.
     */
    private void agregarOActualizarItem(Producto p, int cantidad, double precioUnitario) {
        boolean productoExiste = false;

        for (DetalleCarrito item : carrito) {
            if (item.getIdProducto() == p.getIdProducto()) {
                // CORRECCIÓN AQUÍ: Usar el setter correspondiente
                item.setCantidad(item.getCantidad() + cantidad);
                productoExiste = true;
                break;
            }
        }

        if (!productoExiste) {
            carrito.add(new DetalleCarrito(p.getIdProducto(), p.getNombreProducto(), precioUnitario, cantidad));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getFacturarButton()) {
            
            if (carrito.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El carrito está vacío. Agrega productos presionando el botón (+).", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double subtotal = totalVenta;
            double descuento = 0.0;
            double total = subtotal - descuento;

            // Datos base para el registro de venta
            int idVenta = 1; 
            int idCliente = 1; 
            int idCaja = 1; 

            // Crear modelo de Factura
            Factura factura = new Factura(0, idVenta, idCliente, idCaja, subtotal, descuento, total, new Date());

            // Guardar factura en la base de datos a través del DAO
            boolean exito = facturaDao.guardarFactura(factura);

            if (exito) {
                JOptionPane.showMessageDialog(vista, "Factura N° " + factura.getIdFactura() + " guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Transición a la vista de Factura detallada
                VistaFactura vistaFactura = new VistaFactura(factura, carrito);
                cambiarPanel(vistaFactura);

                reiniciarCarrito();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al procesar la factura en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == vista.getReiniciarButton()) {
            reiniciarCarrito();
            JOptionPane.showMessageDialog(vista, "El carrito se ha reiniciado correctamente.", "Carrito Vaciado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void reiniciarCarrito() {
        carrito.clear();
        totalVenta = 0.0;
        contadorProductos = 0;
        vista.actualizarTextoFacturar(0);

        // Restablecer spinners de las tarjetas de productos
        for (VistaPdV.TarjetaProductoComponentes comp : componentesTarjetas) {
            comp.getBtnAgregar().setEnabled(true);
            comp.getSpinner().setValue(1);
        }
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        JPanel objetivo = contenedorCentral;
        if (objetivo == null && vista.getParent() instanceof JPanel) {
            objetivo = (JPanel) vista.getParent();
        }

        if (objetivo != null) {
            objetivo.removeAll();
            objetivo.setLayout(new BorderLayout());
            objetivo.add(nuevoPanel, BorderLayout.CENTER);
            objetivo.revalidate();
            objetivo.repaint();
        }
    }
  
}
