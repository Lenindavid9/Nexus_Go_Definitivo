package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPanel;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.Usuario;
import nexusgo.view.PanelBienvenida;
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
        
        // Obtenemos la referencia al panel central
        this.contenedorCentral = vistaMenu.getContenedorCentral();

        // Inicializamos la barra lateral
        this.vistaMenu.getsidebar().bCasa.addActionListener(this);       
        this.vistaMenu.getsidebar().bInventario.addActionListener(this); 
        this.vistaMenu.getsidebar().misCitas.addActionListener(this);    

        // Cargamos la pantalla de bienvenida por defecto
        cambiarPanel(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        contenedorCentral.removeAll();
        contenedorCentral.setLayout(new BorderLayout());
        contenedorCentral.add(nuevoPanel, BorderLayout.CENTER);
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == vistaMenu.getsidebar().bCasa) {
            cambiarPanel(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
        }
        else if (e.getSource() == vistaMenu.getsidebar().bInventario) {
            // 1. Instanciar la vista de Punto de Venta
            VistaPdV vistaPdV = new VistaPdV();
            JPanel panelPdV = vistaPdV.VistaNexus();

            // 2. Enlazar el controlador del PdV
            new ControladorPdV(vistaPdV);

            // 3. Consultar y cargar los productos desde la base de datos
            List<Producto> listaProductos = productoDao.listar();

            if (listaProductos != null && !listaProductos.isEmpty()) {
                for (Producto p : listaProductos) {
                    // Formatear precio
                    String precioFormateado = String.format("$%.0f", p.getPrecioCompra());
                    
                    // Validar la ruta o nombre de la imagen
                    String imagen = (p.getUrlImagen() != null && !p.getUrlImagen().isEmpty()) 
                                    ? p.getUrlImagen() 
                                    : "tratamiento.png";

                    // Agregar dinámicamente cada producto a la vista
                    vistaPdV.agregarTarjeta(
                        p.getNombreProducto(), 
                        precioFormateado, 
                        p.getStockActual(), 
                        imagen
                    );
                }
            }

            // 4. Cambiar el contenedor central por la vista cargada
            cambiarPanel(panelPdV);
        }
        else if (e.getSource() == vistaMenu.getsidebar().misCitas) {
            VistaOperarioInventario panelInventario = new VistaOperarioInventario();
            
            ControladorInventarioOperario controlador = new ControladorInventarioOperario(panelInventario, usuarioLogueado, contenedorCentral);
            
            cambiarPanel(panelInventario);
        }
    }
}
