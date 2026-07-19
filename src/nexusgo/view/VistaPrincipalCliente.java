/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USUARIO
 */
public class VistaPrincipalCliente extends JFrame {

  private JLabel fondo;
    private JPanel panelContenedorFlotante;
    private JPanel contenidoCentralDinamico; 
    private JPanel panelGridProductos;
    private JScrollPane scrollContenido;
    private JLabel lblSeccion;
    
    public JButton historial;
    public JButton CitasVigentes;
    public VistaBarraLateral sidebar;
    public PanelBienvenida panelBienvenida;
    public JButton btnCerrarSesion;

    public VistaPrincipalCliente(String nombreUsuario, String rolUsuario) {
        super("Nexus GO - Panel de Cliente");

        // 1. Fondo de mármol con centrado automático de la interfaz
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setLayout(new GridBagLayout());
        this.setContentPane(fondo);

        // 2. Contenedor tarjeta flotante
        panelContenedorFlotante = new JPanel(new BorderLayout());
        panelContenedorFlotante.setPreferredSize(new Dimension(980, 680));
        panelContenedorFlotante.setOpaque(false);

        // 3. BARRA LATERAL
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(110, 680));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(120, 10, 20, 10));
        
        historial = new JButton("Historial");
        CitasVigentes = new JButton("Citas Vigentes");
        sidebar.add(historial);
        sidebar.add(CitasVigentes);

        // Ocultar botones administrativos
        sidebar.bInventario.setVisible(false);
        sidebar.misCitas.setVisible(false);

        // 4. PANEL CENTRAL DINÁMICO
        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setOpaque(false);
        contenidoCentralDinamico.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Inicializar componentes de la tienda base
        panelBienvenida = new PanelBienvenida(nombreUsuario, rolUsuario);
        panelBienvenida.setPreferredSize(new Dimension(650, 150));
        panelBienvenida.setMaximumSize(new Dimension(650, 150));
        panelBienvenida.setOpaque(false);
        
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(Color.decode("#EFB810"));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setAlignmentX(CENTER_ALIGNMENT);
        
        lblSeccion = new JLabel(" PRODUCTOS DISPONIBLES ");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeccion.setAlignmentX(CENTER_ALIGNMENT);

        panelGridProductos = new JPanel(new GridLayout(0, 3, 20, 20));
        panelGridProductos.setOpaque(false);

        // Construir la vista de inicio por defecto
        restaurarComponentesTienda();

        // ScrollPane automático para navegar la tienda
        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setOpaque(false);
        scrollContenido.getViewport().setOpaque(false);
        scrollContenido.setBorder(null);

        // 6. Ensamblaje final
        panelContenedorFlotante.add(sidebar, BorderLayout.WEST);
        panelContenedorFlotante.add(scrollContenido, BorderLayout.CENTER);

        this.add(panelContenedorFlotante);

        setSize(1040, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*
     * Restaura los elementos básicos del catálogo (Bienvenida, Botones principales, Separador y Grid)
     */
    public void restaurarComponentesTienda() {
        contenidoCentralDinamico.removeAll();
        contenidoCentralDinamico.add(panelBienvenida);
        contenidoCentralDinamico.add(Box.createVerticalStrut(10));
        contenidoCentralDinamico.add(btnCerrarSesion);
        contenidoCentralDinamico.add(Box.createVerticalStrut(20));
        contenidoCentralDinamico.add(lblSeccion);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(panelGridProductos);
        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }

    /*
     * Dibuja los productos mapeando dinámicamente sus identidades y registrando los eventos de escucha.
     */
    public void cargarProductosEnInterfaz(List<nexusgo.model.Producto> listaProductos, MouseListener receptorEventos) {
        panelGridProductos.removeAll();

        for (nexusgo.model.Producto prod : listaProductos) {
            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
            tarjeta.setBackground(Color.WHITE);
            tarjeta.setBorder(new EmptyBorder(15, 15, 15, 15));
            
            // !!! CORRECCIÓN CLAVE: Seteamos el ID del producto en el Name del panel y le añadimos el listener
            tarjeta.setName(String.valueOf(prod.getIdProducto()));
            tarjeta.addMouseListener(receptorEventos);

            // Imagen del producto
            JLabel lblImg = new JLabel();
            lblImg.setAlignmentX(CENTER_ALIGNMENT);

            String rutaImagen = prod.getUrlImagen();
            if (rutaImagen == null || rutaImagen.isEmpty()) {
                rutaImagen = "src/nexusgo/img/default.jpg";
            }

            ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
            if (iconoOriginal.getImage() != null) {
                ImageIcon iconoEscalado = new ImageIcon(iconoOriginal.getImage().getScaledInstance(180, 130, java.awt.Image.SCALE_SMOOTH));
                lblImg.setIcon(iconoEscalado);
            }

            // Nombre
            JLabel lblNombre = new JLabel(prod.getNombreProducto().toUpperCase());
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblNombre.setAlignmentX(CENTER_ALIGNMENT);

            // Precio
            JLabel lblPrecio = new JLabel("$ " + String.format("%,.0f", prod.getPrecioCompra()));
            lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblPrecio.setForeground(Color.GRAY);
            lblPrecio.setAlignmentX(CENTER_ALIGNMENT);

            // Stock
            String estado = "Disponible";
            Color colorEstado = new Color(34, 139, 34);

            if (prod.getStockActual() <= 0) {
                estado = "AGOTADO";
                colorEstado = Color.RED;
            } else if (prod.getStockActual() <= prod.getStockMinimo()) {
                estado = "¡Pocas unidades!";
                colorEstado = Color.ORANGE;
            }

            JLabel lblStock = new JLabel(estado + " (" + prod.getStockActual() + ")");
            lblStock.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblStock.setForeground(colorEstado);
            lblStock.setAlignmentX(CENTER_ALIGNMENT);

            // Construcción secuencial de la tarjeta
            tarjeta.add(lblImg);
            tarjeta.add(Box.createVerticalStrut(10));
            tarjeta.add(lblNombre);
            tarjeta.add(Box.createVerticalStrut(5));
            tarjeta.add(lblPrecio);
            tarjeta.add(Box.createVerticalStrut(5));
            tarjeta.add(lblStock);

            panelGridProductos.add(tarjeta);
        }

        panelGridProductos.revalidate();
        panelGridProductos.repaint();
    }

    public JPanel getContenidoCentralDinamico() {
        return this.contenidoCentralDinamico;
    }
}
