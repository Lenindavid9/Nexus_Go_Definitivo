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

    // Componentes estructurales de la ventana
    private JLabel fondo;
    private JPanel panelContenedorFlotante;
    private JPanel contenidoCentralDinamico; // Panel central equivalente al 'contenido' del Operario
    private JPanel panelGridProductos;
    private JScrollPane scrollContenido;
    public JButton historial;
    public JButton CitasVigentes;

    // Componentes modulares tuyos inyectados directamente
    public VistaBarraLateral sidebar;
    public PanelBienvenida panelBienvenida;

    // Botón superior de cierre rápido
    public JButton btnCerrarSesion;

    public VistaPrincipalCliente(String nombreUsuario, String rolUsuario) {
        super("Nexus GO - Panel de Cliente");

        // 1. Fondo de mármol con centrado automático de la interfaz
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setLayout(new GridBagLayout());
        this.setContentPane(fondo);

        // 2. Contenedor "tarjeta" que flotará en medio del mármol
        panelContenedorFlotante = new JPanel(new BorderLayout());
        panelContenedorFlotante.setPreferredSize(new Dimension(980, 680));
        panelContenedorFlotante.setOpaque(false);

        // 3. BARRA LATERAL 
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(110, 680));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(120, 10, 20, 10));
        sidebar.add(historial = new JButton("Historial"));
        sidebar.add(CitasVigentes = new JButton("Citas Vigentes"));

        // Capamos los botones administrativos para el rol Cliente
        sidebar.bInventario.setVisible(false);
        sidebar.misCitas.setVisible(false);

        // 4. PANEL CENTRAL DINÁMICO (Misma lógica que usas en el operario)
        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setOpaque(false);
        contenidoCentralDinamico.setBorder(new EmptyBorder(20, 25, 20, 25));

        // INYECTAMOS PANEL BIENVENIDA DIRECTAMENTE
        panelBienvenida = new PanelBienvenida(nombreUsuario, rolUsuario);
        panelBienvenida.setPreferredSize(new Dimension(650, 150));
        panelBienvenida.setMaximumSize(new Dimension(650, 150));
        panelBienvenida.setOpaque(false); // Para que combine con el fondo de mármol
        contenidoCentralDinamico.add(panelBienvenida);
        contenidoCentralDinamico.add(Box.createVerticalStrut(10));

        // Botón cerrar sesión 
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(Color.decode("#EFB810"));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setAlignmentX(CENTER_ALIGNMENT);
        contenidoCentralDinamico.add(btnCerrarSesion);
        contenidoCentralDinamico.add(Box.createVerticalStrut(20));

        // Separador del catálogo
        JLabel lblSeccion = new JLabel(" PRODUCTOS DISPONIBLES ");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeccion.setAlignmentX(CENTER_ALIGNMENT);
        contenidoCentralDinamico.add(lblSeccion);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));

        // 5. Cuadrícula de productos (3 columnas)
        panelGridProductos = new JPanel(new GridLayout(0, 3, 20, 20));
        panelGridProductos.setOpaque(false);
        contenidoCentralDinamico.add(panelGridProductos);

        // ScrollPane automático para navegar los productos
        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setOpaque(false);
        scrollContenido.getViewport().setOpaque(false);
        scrollContenido.setBorder(null);

        // 6. Ensamblaje final en el cuerpo flotante
        panelContenedorFlotante.add(sidebar, BorderLayout.WEST);
        panelContenedorFlotante.add(scrollContenido, BorderLayout.CENTER);

        this.add(panelContenedorFlotante);

        // Ajustes del marco
        setSize(1040, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Dibuja los productos de la base de datos de manera limpia en la
     * cuadrícula
     */
    public void cargarProductosEnInterfaz(List<nexusgo.model.Producto> listaProductos) {
        panelGridProductos.removeAll();

        for (nexusgo.model.Producto prod : listaProductos) {
            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
            tarjeta.setBackground(Color.WHITE);
            tarjeta.setBorder(new EmptyBorder(15, 15, 15, 15));

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

    // Getter para que los controladores puedan acceder al panel del centro
    public JPanel getContenidoCentralDinamico() {
        return this.contenidoCentralDinamico;
    }

}
