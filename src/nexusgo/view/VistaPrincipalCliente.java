/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
    
    private JLabel fondo;
    private JPanel panelContenedorFlotante;
    private JPanel contenidoCentralDinamico; 
    private JPanel panelGridProductos;
    private JPanel panelGridPromociones;
    private JScrollPane scrollContenido;

    // Componentes interactivos accesibles para el controlador
    public JButton btnReservarCita;
    public JButton btnCerrarSesion;
    public JButton btnHistorial;
    public JLabel lblBienvenida;
    public JLabel lblPromociones;
    public JLabel lblProductosDisponibles;

    // Referencia pública a la barra lateral
    public VistaBarraLateral sidebar;

    /**
     * Construye la interfaz gráfica principal del cliente.
     * @param nombreUsuario Nombre del cliente logueado.
     * @param rolUsuario Rol asignado.
     */
    public VistaPrincipalCliente(String nombreUsuario, String rolUsuario) {
        super("Nexus GO - Cliente");

        // 1. Fondo principal de mármol
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setLayout(new GridBagLayout());
        this.setContentPane(fondo);

        // 2. Contenedor flotante centrado
        panelContenedorFlotante = new JPanel(new BorderLayout());
        panelContenedorFlotante.setPreferredSize(new Dimension(980, 680));
        panelContenedorFlotante.setOpaque(false);

        // 3. Barra Lateral de Navegación
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(80, 680));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(100, 10, 20, 10));

        // Desactivar accesos no autorizados para el cliente
        sidebar.bInventario.setVisible(false);
        sidebar.misCitas.setVisible(true);

        // 4. Panel Dinámico Central
        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setOpaque(false);
        contenidoCentralDinamico.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Encabezado y Saludo
        lblBienvenida = new JLabel("Hola, " + nombreUsuario + " | Bienvenido a Nexus GO", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(Color.BLACK);

        btnHistorial = new JButton("Historial");
        
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(255, 220, 90));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Botón central destacado para agendar citas
        btnReservarCita = new JButton("Reservar cita");
        btnReservarCita.setBackground(new Color(255, 220, 90));
        btnReservarCita.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReservarCita.setAlignmentX(CENTER_ALIGNMENT);
        btnReservarCita.setFocusPainted(false);
        btnReservarCita.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReservarCita.setMaximumSize(new Dimension(180, 35));

        // Etiquetas de secciones
        lblProductosDisponibles = new JLabel("PRODUCTOS DISPONIBLES", SwingConstants.CENTER);
        lblProductosDisponibles.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblProductosDisponibles.setAlignmentX(CENTER_ALIGNMENT);

        lblPromociones = new JLabel("PROMOCIONES ESPECIALES", SwingConstants.CENTER);
        lblPromociones.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPromociones.setForeground(new Color(210, 160, 20));
        lblPromociones.setAlignmentX(CENTER_ALIGNMENT);

        // Grids visuales de 3 columnas para tarjetas
        panelGridProductos = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGridProductos.setOpaque(false);

        panelGridPromociones = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGridPromociones.setOpaque(false);

        // Organizar vista inicial
        restaurarComponentesTienda();

        // ScrollPane transparente para navegar el catálogo
        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setOpaque(false);
        scrollContenido.getViewport().setOpaque(false);
        scrollContenido.setBorder(null);

        // Ensamblaje final de la ventana
        panelContenedorFlotante.add(sidebar, BorderLayout.WEST);
        panelContenedorFlotante.add(scrollContenido, BorderLayout.CENTER);

        this.add(panelContenedorFlotante);
        this.setSize(1040, 750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Reorganiza y reestablece los componentes base del catálogo central.
     */
    public void restaurarComponentesTienda() {
        contenidoCentralDinamico.removeAll();

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.add(lblBienvenida, BorderLayout.CENTER);
        panelHeader.add(btnCerrarSesion, BorderLayout.EAST);

        contenidoCentralDinamico.add(panelHeader);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(btnReservarCita);
        contenidoCentralDinamico.add(Box.createVerticalStrut(20));
        contenidoCentralDinamico.add(lblProductosDisponibles);
        contenidoCentralDinamico.add(Box.createVerticalStrut(10));
        contenidoCentralDinamico.add(panelGridProductos);
        contenidoCentralDinamico.add(Box.createVerticalStrut(25));
        contenidoCentralDinamico.add(lblPromociones);
        contenidoCentralDinamico.add(Box.createVerticalStrut(10));
        contenidoCentralDinamico.add(panelGridPromociones);

        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }

    /**
     * Limpia la cuadrícula de productos regulares.
     */
    public void limpiarGridProductos() {
        panelGridProductos.removeAll();
        panelGridProductos.revalidate();
        panelGridProductos.repaint();
    }

    /**
     * Limpia la cuadrícula de productos en promoción.
     */
    public void limpiarGridPromociones() {
        panelGridPromociones.removeAll();
        panelGridPromociones.revalidate();
        panelGridPromociones.repaint();
    }

    /**
     * Agrega una tarjeta individual al grid de productos regulares.
     */
    public void agregarTarjetaProducto(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = crearTarjetaVisual(String.valueOf(id), nombre, precio, rutaImagen, listener);
        panelGridProductos.add(tarjeta);
        panelGridProductos.revalidate();
        panelGridProductos.repaint();
    }

    /**
     * Agrega una tarjeta individual al grid de promociones.
     */
    public void agregarTarjetaPromocion(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = crearTarjetaVisual(String.valueOf(id), nombre, precio, rutaImagen, listener);
        panelGridPromociones.add(tarjeta);
        panelGridPromociones.revalidate();
        panelGridPromociones.repaint();
    }

    /**
     * Método fábrica interno para maquetar tarjetas uniformes.
     */
    private JPanel crearTarjetaVisual(String idStr, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new EmptyBorder(12, 12, 12, 12));
        tarjeta.setName(idStr);
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tarjeta.addMouseListener(listener);

        JLabel lblImg = new JLabel();
        lblImg.setAlignmentX(CENTER_ALIGNMENT);
        lblImg.setName(idStr);
        lblImg.addMouseListener(listener);

        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            rutaImagen = "src/nexusgo/img/default.jpg";
        }

        ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
        if (iconoOriginal.getImage() != null) {
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(170, 115, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(imgEscalada));
        }

        JLabel lblNombre = new JLabel(nombre.toUpperCase());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblPrecio);

        return tarjeta;
    }

    /**
     * Retorna el panel central dinámico para que los controladores puedan sustituir vistas.
     */
    public JPanel getContenidoCentralDinamico() {
        return this.contenidoCentralDinamico;
    }

}
