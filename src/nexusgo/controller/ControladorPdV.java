package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.DetalleCarrito;
import nexusgo.model.FacturaDao;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaMetododePago;
import nexusgo.view.VistaPdV;

public class ControladorPdV implements ActionListener {

    private final VistaPdV vista;
    private final FacturaDao facturaDao;
    private final ProductoDao productoDao;
    private final nexusgo.model.CajaDao cajaDao = new nexusgo.model.CajaDao();
    private JPanel contenedorCentral;

    // ID de la caja abierta con la que se está operando (0 = ninguna)
    private int idCajaActual = 0;

    // Estado del Carrito y Vista
    private final List<DetalleCarrito> carrito = new ArrayList<>();
    private final List<VistaPdV.TarjetaProductoComponentes> componentesTarjetas = new ArrayList<>();
    private double totalVenta = 0.0;
    private int contadorProductos = 0;

    // Constructor Principal (recibe el contenedor y el id de la caja abierta)
    public ControladorPdV(VistaPdV vista, JPanel contenedorCentral, int idCajaActual) {
        this.vista = vista;
        this.contenedorCentral = contenedorCentral;
        this.facturaDao = new FacturaDao();
        this.productoDao = new ProductoDao();
        this.idCajaActual = idCajaActual;

        // Respaldo: si no nos pasaron una caja abierta explícita, buscamos la más reciente
        if (this.idCajaActual <= 0) {
            this.idCajaActual = cajaDao.obtenerCajaAbierta();
        }

        // Enlazar eventos de los botones principales
        this.vista.getFacturarButton().addActionListener(this);
        this.vista.getReiniciarButton().addActionListener(this);

        // Cargar los productos dinámicamente desde el DAO
        cargarProductos();
    }

    // Constructor Sobrecargado (recibe el contenedor, sin id de caja explícito)
    public ControladorPdV(VistaPdV vista, JPanel contenedorCentral) {
        this(vista, contenedorCentral, 0);
    }

    // Constructor Sobrecargado (Compatibilidad cuando no se pasa el contenedor)
    public ControladorPdV(VistaPdV vista) {
        this(vista, null, 0);
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
     * Acumula la cantidad seleccionada en el producto existente dentro del
     * carrito, o agrega una nueva línea si no había sido seleccionado antes.
     */
    private void agregarOActualizarItem(Producto p, int cantidad, double precioUnitario) {
        boolean productoExiste = false;

        for (DetalleCarrito item : carrito) {
            if (item.getIdProducto() == p.getIdProducto()) {
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
        // Evento Botón Facturar -> Transición hacia Método de Pago
        if (e.getSource() == vista.getFacturarButton()) {

            if (carrito.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El carrito está vacío. Agrega productos presionando el botón (+).",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (idCajaActual <= 0) {
                JOptionPane.showMessageDialog(vista, "No hay ninguna caja abierta. Debe abrir caja antes de facturar.",
                        "Caja no disponible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 1. Instanciar la vista del Método de Pago
            VistaMetododePago vistaPago = new VistaMetododePago();

            // 2. Instanciar su controlador pasándole la vista, los datos de la venta y el contenedor
            ControladorMetododePago controladorPago = new ControladorMetododePago(vistaPago, carrito, totalVenta, obtenerContenedorObjetivo());

            // 3. Redireccionar a la pantalla de selección de Método de Pago
            cambiarPanel(vistaPago);
        } // Evento Botón Reiniciar / Limpiar Carrito
        else if (e.getSource() == vista.getReiniciarButton()) {
            reiniciarCarrito();
            JOptionPane.showMessageDialog(
                    vista,
                    "El carrito se ha reiniciado correctamente.",
                    "Carrito Vaciado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void reiniciarCarrito() {
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

    private JPanel obtenerContenedorObjetivo() {
        if (contenedorCentral != null) {
            return contenedorCentral;
        }
        if (vista.getParent() instanceof JPanel) {
            return (JPanel) vista.getParent();
        }
        return null;
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        JPanel objetivo = obtenerContenedorObjetivo();

        if (objetivo != null) {
            objetivo.removeAll();
            objetivo.setLayout(new BorderLayout());
            objetivo.add(nuevoPanel, BorderLayout.CENTER);
            objetivo.revalidate();
            objetivo.repaint();
        }
    }
}
