package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.CajaDao;
import nexusgo.model.DetalleCarrito;
import nexusgo.model.FacturaDao;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.PromocionCombo;
import nexusgo.model.PromocionComboDao;
import nexusgo.model.ServicioDao;
import nexusgo.model.Servicios;
import nexusgo.view.VistaMetododePago;
import nexusgo.view.VistaPdV;

public class ControladorPdV implements ActionListener {

    private static final int UMBRAL_DESCUENTO_UNIDADES = 10;
    private static final double PORCENTAJE_DESCUENTO = 0.10;

    private final VistaPdV vista;
    private final FacturaDao facturaDao;
    private final ProductoDao productoDao;
    private final CajaDao cajaDao = new CajaDao();
    private final ServicioDao servicioDao = new ServicioDao();
    private final PromocionComboDao comboDao = new PromocionComboDao();
    private JPanel contenedorCentral;

    private int idCajaActual = 0;

    private nexusgo.model.Usuario usuarioLogueado = null;

    public void setUsuarioLogueado(nexusgo.model.Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    private final List<DetalleCarrito> carrito = new ArrayList<>();
    private final List<VistaPdV.TarjetaProductoComponentes> componentesTarjetas = new ArrayList<>();
    private double totalVenta = 0.0;
    private int contadorProductos = 0;

    public ControladorPdV(VistaPdV vista, JPanel contenedorCentral, int idCajaActual) {
        this.vista = vista;
        this.contenedorCentral = contenedorCentral;
        this.facturaDao = new FacturaDao();
        this.productoDao = new ProductoDao();
        this.idCajaActual = idCajaActual;

        if (this.idCajaActual <= 0) {
            this.idCajaActual = cajaDao.obtenerCajaAbierta();
        }

        this.vista.getFacturarButton().addActionListener(this);
        this.vista.getReiniciarButton().addActionListener(this);

        cargarProductos();
        cargarServicios();
        cargarCombos();
    }

    public ControladorPdV(VistaPdV vista, JPanel contenedorCentral) {
        this(vista, contenedorCentral, 0);
    }

    public ControladorPdV(VistaPdV vista) {
        this(vista, null, 0);
    }

    private void cargarServicios() {
        List<Servicios> servicios = servicioDao.listarServiciosActivos();
        if (servicios != null) {
            for (Servicios s : servicios) {
                String precioFormateado = String.format("$%.0f", s.getPrecio());

                VistaPdV.TarjetaProductoComponentes componentes = vista.agregarTarjetaServicio(
                        s.getNombreServicio(), precioFormateado, null);
                componentesTarjetas.add(componentes);

                componentes.getBtnAgregar().addActionListener(e -> {
                    int cantidadIngresada = (int) componentes.getSpinner().getValue();
                    double precioUnitario = s.getPrecio();

                    if (agregarOActualizarItem(s.getIdServicio(), "SERVICIO", s.getNombreServicio(), cantidadIngresada, precioUnitario, -1)) {
                        totalVenta += (precioUnitario * cantidadIngresada);
                        contadorProductos += cantidadIngresada;
                        vista.actualizarTextoFacturar(contadorProductos);
                    }
                });
            }
        }
    }

    private void cargarCombos() {
        List<PromocionCombo> combos = comboDao.listarCombosActivos();
        if (combos != null) {
            for (PromocionCombo c : combos) {
                String precioFormateado = String.format("$%.0f", c.getPrecioCombo());

                VistaPdV.TarjetaProductoComponentes componentes = vista.agregarTarjetaCombo(
                        c.getNombreCombo(), precioFormateado, c.getRutaImagen());
                componentesTarjetas.add(componentes);

                componentes.getBtnAgregar().addActionListener(e -> {
                    int cantidadIngresada = (int) componentes.getSpinner().getValue();
                    double precioUnitario = c.getPrecioCombo();

                    if (agregarOActualizarItem(c.getIdPromocion(), "COMBO", c.getNombreCombo(), cantidadIngresada, precioUnitario, -1)) {
                        totalVenta += (precioUnitario * cantidadIngresada);
                        contadorProductos += cantidadIngresada;
                        vista.actualizarTextoFacturar(contadorProductos);
                    }
                });
            }
        }
    }

    private void cargarProductos() {
        List<Producto> productos = productoDao.listar();

        if (productos != null && !productos.isEmpty()) {
            for (Producto p : productos) {
                String precioFormateado = String.format("$%.0f", p.getPrecioCompra());
                String imagen = (p.getUrlImagen() != null && !p.getUrlImagen().isEmpty())
                        ? p.getUrlImagen()
                        : "tratamiento.png";

                VistaPdV.TarjetaProductoComponentes componentes = vista.agregarTarjetaComponentes(
                        p.getNombreProducto(),
                        precioFormateado,
                        p.getStockActual(),
                        imagen
                );

                componentesTarjetas.add(componentes);

                componentes.getBtnAgregar().addActionListener(e -> {
                    int cantidadIngresada = (int) componentes.getSpinner().getValue();
                    double precioUnitario = p.getPrecioCompra();

                    if (agregarOActualizarItem(p.getIdProducto(), "PRODUCTO", p.getNombreProducto(), cantidadIngresada, precioUnitario, p.getStockActual())) {
                        totalVenta += (precioUnitario * cantidadIngresada);
                        contadorProductos += cantidadIngresada;

                        vista.actualizarTextoFacturar(contadorProductos);
                    }
                });
            }
        }
    }

    private boolean agregarOActualizarItem(int id, String tipo, String nombre, int cantidad, double precioUnitario, int stockDisponible) {
        boolean itemExiste = false;
        int cantidadYaEnCarrito = 0;

        for (DetalleCarrito item : carrito) {
            if (item.getIdProducto() == id && tipo.equals(item.getTipo())) {
                cantidadYaEnCarrito = item.getCantidad();
                itemExiste = true;
                break;
            }
        }

        if (stockDisponible >= 0 && (cantidadYaEnCarrito + cantidad > stockDisponible)) {
            JOptionPane.showMessageDialog(vista,
                    "No hay suficiente stock de \"" + nombre + "\".\n"
                    + "Disponible: " + stockDisponible + " | Ya en el carrito: " + cantidadYaEnCarrito,
                    "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (itemExiste) {
            for (DetalleCarrito item : carrito) {
                if (item.getIdProducto() == id && tipo.equals(item.getTipo())) {
                    item.setCantidad(item.getCantidad() + cantidad);
                    break;
                }
            }
        } else {
            carrito.add(new DetalleCarrito(id, nombre, precioUnitario, cantidad, tipo));
        }
        return true;
    }

    private boolean aplicaDescuentoPorVolumen() {
        return contadorProductos >= UMBRAL_DESCUENTO_UNIDADES;
    }

    private double calcularTotalConDescuento() {
        if (aplicaDescuentoPorVolumen()) {
            return totalVenta - (totalVenta * PORCENTAJE_DESCUENTO);
        }
        return totalVenta;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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

            double totalFinal = calcularTotalConDescuento();

            if (aplicaDescuentoPorVolumen()) {
                JOptionPane.showMessageDialog(vista,
                        "¡Se aplicó un " + (int) (PORCENTAJE_DESCUENTO * 100) + "% de descuento por llevar "
                        + contadorProductos + " o más productos/servicios!",
                        "Descuento Aplicado", JOptionPane.INFORMATION_MESSAGE);
            }

            VistaMetododePago vistaPago = new VistaMetododePago();

            ControladorMetododePago controladorPago = new ControladorMetododePago(vistaPago, carrito, totalFinal, obtenerContenedorObjetivo(), idCajaActual);
            controladorPago.setOperarioLogueado(usuarioLogueado);

            cambiarPanel(vistaPago);
        } else if (e.getSource() == vista.getReiniciarButton()) {
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
