package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.Usuario;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.view.VistaPdV;
import nexusgo.view.VistaPrincipalOperario;

public class ControladorPrincipalOperario implements ActionListener {
    
  private final VistaPrincipalOperario vistaMenu;
    private final JPanel contenedorCentral;
    private final Usuario usuarioLogueado;

    // Instancia del DAO para obtener los productos de la BD
    private final ProductoDao productoDao = new ProductoDao();

    public ControladorPrincipalOperario(VistaPrincipalOperario vistaMenu, Usuario usuarioLogueado) {
        this.vistaMenu = vistaMenu;
        this.usuarioLogueado = usuarioLogueado;

        // Referencia al contenedor principal de vistas
        this.contenedorCentral = vistaMenu.getContenedorCentral();

        // Registro de eventos para la barra lateral y acciones globales
        this.vistaMenu.getsidebar().bCasa.addActionListener(this);
        this.vistaMenu.getsidebar().bInventario.addActionListener(this);
        this.vistaMenu.getsidebar().misCitas.addActionListener(this);
        this.vistaMenu.getBtnCerrarSesion().addActionListener(this);

        // Cargar vista de bienvenida inicial
        cambiarPanel(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // Navegación: Inicio / Dashboard
        if (src == vistaMenu.getsidebar().bCasa) {
            cambiarPanel(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
        } 
        // Navegación: Punto de Venta (PdV)
        else if (src == vistaMenu.getsidebar().bInventario) {
            cargarYMostrarPdV();
        } 
        // Navegación: Gestión de Inventario / Herramientas
        else if (src == vistaMenu.getsidebar().misCitas) {
            VistaOperarioInventario panelInventario = new VistaOperarioInventario();
            new ControladorInventarioOperario(panelInventario, usuarioLogueado, contenedorCentral);
            cambiarPanel(panelInventario);
        } 
        // Acción: Cerrar Sesión
        else if (src == vistaMenu.getBtnCerrarSesion()) {
            ejecutarCerrarSesion();
        }
    }

    private void cargarYMostrarPdV() {
        // 1. Instanciar vista y vincular controlador del PdV
        VistaPdV vistaPdV = new VistaPdV();
        JPanel panelPdV = vistaPdV.VistaNexus();
        new ControladorPdV(vistaPdV);

        // 2. Consultar y renderizar catálogo de productos
        List<Producto> listaProductos = productoDao.listar();

        if (listaProductos != null && !listaProductos.isEmpty()) {
            for (Producto p : listaProductos) {
                // Formato con precio de venta público
                String precioFormateado = String.format("$%.0f", p.getPrecioVenta());

                // Imagen predeterminada si el registro es nulo o vacío
                String imagen = (p.getUrlImagen() != null && !p.getUrlImagen().trim().isEmpty())
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

        // 3. Renderizar en el panel central
        cambiarPanel(panelPdV);
    }

    private void ejecutarCerrarSesion() {
        int confirmar = JOptionPane.showConfirmDialog(
            vistaMenu,
            "¿Desea cerrar sesión en NEXUS GO?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            vistaMenu.dispose();

            VistaInicioSesion loginVista = new VistaInicioSesion();
            new ControladorInicioSesion(loginVista);
            loginVista.setLocationRelativeTo(null);
            loginVista.setVisible(true);
        }
    }
    
}
