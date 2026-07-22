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

    // Componentes interactivos accesibles desde el controlador
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

        // 1. Fondo principal
        this.fondoMarmol = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
        this.fondoMarmol.setLayout(new BorderLayout());
        this.setContentPane(fondoMarmol);

        // 2. Contenedor ESTRUCTURAL
        JPanel contenedorEstructural = new JPanel(new BorderLayout(15, 0));
        contenedorEstructural.setPreferredSize(new Dimension(980, 650));
        contenedorEstructural.setOpaque(false);

        // 3. Barra Lateral (Sidebar)
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(80, 650));
        sidebar.setBackground(Color.WHITE);
        if (sidebar.bInventario != null) sidebar.bInventario.setVisible(false);
        if (sidebar.misCitas != null) sidebar.misCitas.setVisible(true);

        // 4. Tarjeta Blanca Central Flotante
        panelFlotanteBlanco = new JPanel(new BorderLayout());
        panelFlotanteBlanco.setOpaque(false);
        panelFlotanteBlanco.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 5. Panel Interno Dinámico
        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setBackground(Color.WHITE);
        contenidoCentralDinamico.setOpaque(false);

        // --- Componentes ---
        lblBienvenida = new JLabel("Hola, " + nombreUsuario + " | Bienvenido a Nexus GO", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblBienvenida.setForeground(Color.BLACK);

        btnCerrarSesion = new JButton("cerrar sesión");
        btnCerrarSesion.setBackground(new Color(255, 220, 90));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setPreferredSize(new Dimension(110, 28));

        btnReservarCita = new JButton("Reservar cita");
        btnReservarCita.setBackground(new Color(255, 220, 90));
        btnReservarCita.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReservarCita.setAlignmentX(CENTER_ALIGNMENT);
        btnReservarCita.setFocusPainted(false);
        btnReservarCita.setBorderPainted(false);
        btnReservarCita.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReservarCita.setMaximumSize(new Dimension(150, 32));

        btnHistorial = new JButton("Historial");

        // Cargar estructura inicial
        restaurarComponentesTienda();

        // ScrollPane
        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setBackground(Color.WHITE);
        scrollContenido.getViewport().setBackground(Color.WHITE);
        scrollContenido.setBorder(null);

        panelFlotanteBlanco.add(scrollContenido, BorderLayout.CENTER);

        // Ensamblaje
        contenedorEstructural.add(sidebar, BorderLayout.WEST);
        contenedorEstructural.add(panelFlotanteBlanco, BorderLayout.CENTER);

        this.add(contenedorEstructural);
        this.setSize(1040, 720);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Reconstruye y limpia el panel central para volver al catálogo.
     */
    public void restaurarComponentesTienda() {
        contenidoCentralDinamico.removeAll();

        panelGridProductos = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGridProductos.setBackground(Color.WHITE);
        panelGridProductos.setOpaque(false);

        panelGridPromociones = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGridPromociones.setBackground(Color.WHITE);
        panelGridPromociones.setOpaque(false);

        // Header Superior
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelHeader.add(lblBienvenida, BorderLayout.CENTER);

        JPanel panelBotonDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBotonDerecha.setOpaque(false);
        panelBotonDerecha.add(btnCerrarSesion);
        panelHeader.add(panelBotonDerecha, BorderLayout.EAST);

        // Banner de Promociones
        JPanel panelEtiquetaPromo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEtiquetaPromo.setOpaque(false);
        
        JLabel lblPromociones = new JLabel("Promociones", SwingConstants.CENTER);
        lblPromociones.setOpaque(true);
        lblPromociones.setBackground(new Color(255, 220, 90));
        lblPromociones.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPromociones.setPreferredSize(new Dimension(140, 26));
        panelEtiquetaPromo.add(lblPromociones);

        // Ensamblado vertical
        contenidoCentralDinamico.add(panelHeader);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(panelGridProductos);
        contenidoCentralDinamico.add(Box.createVerticalStrut(25));
        contenidoCentralDinamico.add(btnReservarCita);
        contenidoCentralDinamico.add(Box.createVerticalStrut(25));
        contenidoCentralDinamico.add(panelEtiquetaPromo);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(panelGridPromociones);

        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }

    public void limpiarGridProductos() {
        if (panelGridProductos != null) {
            panelGridProductos.removeAll();
            panelGridProductos.revalidate();
            panelGridProductos.repaint();
        }
    }

    public void limpiarGridPromociones() {
        if (panelGridPromociones != null) {
            panelGridPromociones.removeAll();
            panelGridPromociones.revalidate();
            panelGridPromociones.repaint();
        }
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
     * Maqueta la tarjeta visual e individual para cada producto u oferta.
     */
    private JPanel crearTarjetaVisual(String idStr, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(248, 248, 248));
        tarjeta.setBorder(new EmptyBorder(10, 10, 10, 10));
        tarjeta.setName(idStr);
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (listener != null) tarjeta.addMouseListener(listener);

        JLabel lblImg = new JLabel();
        lblImg.setAlignmentX(CENTER_ALIGNMENT);
        lblImg.setName(idStr);
        if (listener != null) lblImg.addMouseListener(listener);

        // --- 1. Formateo y Normalización de Ruta ---
        String rutaLimpia = (rutaImagen != null) ? rutaImagen.trim() : "";
        
        if (rutaLimpia.isEmpty()) {
            rutaLimpia = "/nexusgo/img/default.jpg";
        } else {
            if (rutaLimpia.startsWith("src/")) {
                rutaLimpia = rutaLimpia.substring(4);
            }
            if (rutaLimpia.startsWith("nexusgo/")) {
                rutaLimpia = "/" + rutaLimpia;
            } else if (!rutaLimpia.startsWith("/nexusgo/")) {
                if (rutaLimpia.startsWith("/")) {
                    rutaLimpia = "/nexusgo" + rutaLimpia;
                } else {
                    rutaLimpia = "/nexusgo/" + rutaLimpia;
                }
            }
        }

        // --- 2. Intento de Carga por Classpath y File Fallback ---
        ImageIcon iconoOriginal = null;
        try {
            java.net.URL imgURL = getClass().getResource(rutaLimpia);
            if (imgURL != null) {
                iconoOriginal = new ImageIcon(imgURL);
            } else {
                iconoOriginal = new ImageIcon("src" + rutaLimpia);
            }
        } catch (Exception ex) {
            System.err.println("❌ No se pudo cargar la imagen desde: " + rutaLimpia);
        }

        // --- 3. Renderizado en el JLabel ---
        if (iconoOriginal != null && iconoOriginal.getImage() != null && iconoOriginal.getIconWidth() > 0) {
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(140, 100, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblImg.setText("[Sin Foto]");
            lblImg.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblImg.setForeground(Color.GRAY);
        }

        // --- 4. Etiquetas de Texto (Nombre y Precio) ---
        JLabel lblNombre = new JLabel(nombre, SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        lblNombre.setName(idStr);
        if (listener != null) lblNombre.addMouseListener(listener);

        JLabel lblPrecio = new JLabel(String.format("$%.2f", precio), SwingConstants.CENTER);
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrecio.setForeground(new Color(40, 140, 40));
        lblPrecio.setAlignmentX(CENTER_ALIGNMENT);
        lblPrecio.setName(idStr);
        if (listener != null) lblPrecio.addMouseListener(listener);

        // --- Ensamblado de la tarjeta ---
        tarjeta.add(lblImg);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblPrecio);

        return tarjeta;
    }

    public JPanel getContenidoCentralDinamico() {
        return contenidoCentralDinamico;
    }
}
