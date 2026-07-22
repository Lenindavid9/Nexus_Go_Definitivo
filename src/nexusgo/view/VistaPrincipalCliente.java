/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USUARIO
 */
public class VistaPrincipalCliente extends JFrame {
    
   private JLabel fondoMarmol;
    private JPanel panelFlotanteBlanco;
    private JPanel contenidoCentralDinamico;
    private JPanel panelGridProductos;
    private JPanel panelGridPromociones;
    private JScrollPane scrollContenido;

    // Componentes interactivos para el controlador
    public JButton btnReservarCita;
    public JButton btnCerrarSesion;
    public JButton btnHistorial;
    public JLabel lblBienvenida;
    public VistaBarraLateral sidebar;

    public VistaPrincipalCliente() {
        this("Cliente", "Cliente");
    }

    public VistaPrincipalCliente(String nombreUsuario, String rolUsuario) {
        super("Nexus GO - Cliente");

        // 1. Fondo de Mármol
        this.fondoMarmol = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondoMarmol.setLayout(new GridBagLayout()); // Centra el contenedor interno
        this.fondoMarmol = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
        this.fondoMarmol.setLayout(new BorderLayout()); // Centra el contenedor interno
        this.setContentPane(fondoMarmol);

        // 2. Contenedor Principal (Marco transparente que une Sidebar + Tarjeta Blanca)
        JPanel contenedorEstructural = new JPanel(new BorderLayout(15, 0));
        contenedorEstructural.setPreferredSize(new Dimension(980, 650));
        contenedorEstructural.setOpaque(false);

        // 3. Barra Lateral (Sidebar)
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(70, 650));
        sidebar.setBackground(Color.WHITE);
        if (sidebar.bInventario != null) sidebar.bInventario.setVisible(true);
        if (sidebar.misCitas != null) sidebar.misCitas.setVisible(true);

        // 4. Tarjeta Blanca Central Flotante (Como en Figma)
        panelFlotanteBlanco = new JPanel(new BorderLayout());
        panelFlotanteBlanco.setBackground(Color.red);
        
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(80,0));
        if (sidebar.bInventario != null) sidebar.bInventario.setVisible(false);
        if (sidebar.misCitas != null) sidebar.misCitas.setVisible(true);
        


        // 4. Tarjeta Blanca Central Flotante (Como en Figma)
        panelFlotanteBlanco = new JPanel(new BorderLayout());
        panelFlotanteBlanco.setOpaque(false);
        panelFlotanteBlanco.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 5. Panel Interno Dinámico (Donde va el catálogo y otras vistas)
        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setBackground(Color.WHITE);
        contenidoCentralDinamico.setOpaque(false);

        // --- Botones y Encabezados ---
        lblBienvenida = new JLabel("Hola, " + nombreUsuario + " | Bienvenido a Nexus GO", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblBienvenida.setForeground(Color.BLACK);
        lblBienvenida.setForeground(Color.WHITE);

        btnCerrarSesion = new JButton("cerrar sesión");
        btnCerrarSesion.setBackground(new Color(255, 220, 90));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setPreferredSize(new Dimension(110, 28)); // Tamaño controlado para evitar que se estire

        btnReservarCita = new JButton("Reservar cita");
        btnReservarCita.setBackground(new Color(255, 220, 90));
        btnReservarCita.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReservarCita.setAlignmentX(CENTER_ALIGNMENT);
        btnReservarCita.setFocusPainted(false);
        btnReservarCita.setBorderPainted(false);
        btnReservarCita.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReservarCita.setMaximumSize(new Dimension(150, 32));

        btnHistorial = new JButton("Historial"); // Necesario para evitar NullPointerException en el controlador

        // --- Grids de Tarjetas ---
        panelGridProductos = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGridProductos.setBackground(Color.WHITE);

        panelGridPromociones = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGridPromociones.setBackground(Color.WHITE);

        // Cargar vista inicial de la tienda
        restaurarComponentesTienda();

        // ScrollPane transparente asignado a la tarjeta blanca
        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setBackground(Color.WHITE);
        scrollContenido.getViewport().setBackground(Color.WHITE);
        scrollContenido.setBorder(null);

        panelFlotanteBlanco.add(scrollContenido, BorderLayout.CENTER);

        // Ensamblar
        contenedorEstructural.add(sidebar, BorderLayout.WEST);
        contenedorEstructural.add(panelFlotanteBlanco, BorderLayout.CENTER);

        this.add(contenedorEstructural);
        this.setSize(1040, 720);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Reconstruye y limpia el panel central para volver al catálogo de productos.
     */
    public void restaurarComponentesTienda() {
        contenidoCentralDinamico.removeAll();

        // Header Superior: Saludo centrado y Botón Cerrar Sesión fijo a la derecha
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Color.WHITE);
        panelHeader.add(lblBienvenida, BorderLayout.CENTER);

        // Subpanel para que el botón "cerrar sesión" no se extienda a lo alto
        JPanel panelBotonDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBotonDerecha.setBackground(Color.WHITE);
        panelBotonDerecha.setOpaque(false);
        panelBotonDerecha.add(btnCerrarSesion);
        panelHeader.add(panelBotonDerecha, BorderLayout.EAST);

        // Banner/Pestaña amarilla de Promociones (Figma)
        JPanel panelEtiquetaPromo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEtiquetaPromo.setBackground(Color.WHITE);
        panelEtiquetaPromo.setOpaque(false);
        
        JLabel lblPromociones = new JLabel("Promociones", SwingConstants.CENTER);
        lblPromociones.setOpaque(true);
        lblPromociones.setBackground(new Color(255, 220, 90));
        lblPromociones.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPromociones.setPreferredSize(new Dimension(140, 26));
        panelEtiquetaPromo.add(lblPromociones);

        // Ensamblaje vertical
        contenidoCentralDinamico.add(panelHeader);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(btnReservarCita);
        contenidoCentralDinamico.add(Box.createVerticalStrut(20));
        contenidoCentralDinamico.add(panelGridProductos);
        contenidoCentralDinamico.add(Box.createVerticalStrut(20));
        contenidoCentralDinamico.add(panelEtiquetaPromo);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(panelGridPromociones);

        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }

    public void limpiarGridProductos() {
        panelGridProductos.removeAll();
        panelGridProductos.revalidate();
        panelGridProductos.repaint();
    }

    public void limpiarGridPromociones() {
        panelGridPromociones.removeAll();
        panelGridPromociones.revalidate();
        panelGridPromociones.repaint();
    }

    public void agregarTarjetaProducto(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = crearTarjetaVisual(String.valueOf(id), nombre, precio, rutaImagen, listener);
        panelGridProductos.add(tarjeta);
        panelGridProductos.revalidate();
        panelGridProductos.repaint();
    }

    public void agregarTarjetaPromocion(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = crearTarjetaVisual(String.valueOf(id), nombre, precio, rutaImagen, listener);
        panelGridPromociones.add(tarjeta);
        panelGridPromociones.revalidate();
        panelGridPromociones.repaint();
    }

    /**
     * Maqueta tarjetas individuales blancas con sombra/borde suave.
     */
    private JPanel crearTarjetaVisual(String idStr, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(248, 248, 248));
        tarjeta.setBorder(new EmptyBorder(10, 10, 10, 10));
        tarjeta.setName(idStr);
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tarjeta.addMouseListener(listener);

        JLabel lblImg = new JLabel();
        lblImg.setAlignmentX(CENTER_ALIGNMENT);
        lblImg.setName(idStr);
        lblImg.addMouseListener(listener);

        // 1. Normalización de la ruta proveniente de la Base de Datos
        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            rutaImagen = "/nexusgo/img/default.jpg";
        } else {
            rutaImagen = rutaImagen.trim();
            
            // Si la BD solo trae el nombre del archivo (ej. "champu.jpg")
            if (!rutaImagen.contains("/")) {
                rutaImagen = "/nexusgo/img/" + rutaImagen;
            } else {
                // Si la BD trae "src/nexusgo/img/champu.jpg", le remueve "src"
                if (rutaImagen.startsWith("src/")) {
                    rutaImagen = rutaImagen.substring(3);
                }
                // Asegurar que comience con '/' para el Classpath
                if (!rutaImagen.startsWith("/")) {
                    rutaImagen = "/" + rutaImagen;
                }
            }
        }

        // 2. Intentar cargar desde el Classpath
        ImageIcon iconoOriginal = null;
        try {
            java.net.URL imgURL = getClass().getResource(rutaImagen);
            if (imgURL != null) {
                iconoOriginal = new ImageIcon(imgURL);
            } else {
                // Intento secundario si la ruta es absoluta fuera del proyecto
                iconoOriginal = new ImageIcon(rutaImagen);
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar la imagen: " + rutaImagen);
        }

        // 3. Renderizar imagen si existe y no está vacía
        if (iconoOriginal != null && iconoOriginal.getImage() != null && iconoOriginal.getIconWidth() > 0) {
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(150, 105, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblImg.setText("Sin Imagen");
            lblImg.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        }

        JLabel lblNombre = new JLabel("<html><center>" + nombre.toUpperCase() + "</center></html>");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        lblNombre.setName(idStr);
        lblNombre.addMouseListener(listener);

        JLabel lblPrecio = new JLabel(String.format("$ %,.0f", precio));
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPrecio.setForeground(Color.GRAY);
        lblPrecio.setAlignmentX(CENTER_ALIGNMENT);
        lblPrecio.setName(idStr);
        lblPrecio.addMouseListener(listener);

        tarjeta.add(lblImg);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(3));
        tarjeta.add(lblPrecio);

        return tarjeta;
    }
    
    public JPanel getContenidoCentralDinamico() {
        return this.contenidoCentralDinamico;
    }
}
