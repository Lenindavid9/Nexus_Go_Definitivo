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
import java.util.List;
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
    public JButton btnReservarCita,btnCerrarSesion,btnHistorial;
    public JLabel lblBienvenida,lblPromociones;
    
  
    
    // Referencia pública a la barra lateral
    public VistaBarraLateral sidebar;

    /*
     * En el constructor construyo la interfaz gráfica. Vinculo la barra lateral con sus
     * accesos directos (Inicio, Historial de Compras y Citas Agendadas), configuro la vista
     * central y la sección inferior de promociones.
     */
    public VistaPrincipalCliente(String nombreUsuario, String rolUsuario) {
        super("Nexus GO - Cliente");

        // Fondo de mármol
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setLayout(new GridBagLayout());
        this.setContentPane(fondo);

        // Panel contenedor transparente
        panelContenedorFlotante = new JPanel(new BorderLayout());
        panelContenedorFlotante.setPreferredSize(new Dimension(980, 680));
        panelContenedorFlotante.setOpaque(false);

        // Instancio la barra lateral
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(80, 680));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(100, 10, 20, 10));
        sidebar.add(btnHistorial);
        

        // Desactivo componentes administrativos que no aplica mostrar al cliente
        sidebar.bInventario.setVisible(false);

        // Habilito el tercer botón para la gestión de Citas (vigentes, pasadas y por haber)
        sidebar.misCitas.setVisible(true);

        // Panel dinámico central
        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setOpaque(false);
        contenidoCentralDinamico.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Saludo y botón cerrar sesión
        lblBienvenida = new JLabel("Hola, " + nombreUsuario + " Bienvenido a Nexus GO", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(Color.BLACK);
        
        btnHistorial = new JButton("historial");
       

        btnCerrarSesion = new JButton("cerrar sesion");
        btnCerrarSesion.setBackground(new Color(255, 220, 90));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Botón central destacado para reservar citas
        btnReservarCita = new JButton("Reservar cita");
        btnReservarCita.setBackground(new Color(255, 220, 90));
        btnReservarCita.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReservarCita.setAlignmentX(CENTER_ALIGNMENT);
        btnReservarCita.setFocusPainted(false);
        btnReservarCita.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReservarCita.setMaximumSize(new Dimension(180, 35));

        // Grids visuales de 3 columnas
        panelGridProductos = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGridProductos.setOpaque(false);

        lblPromociones = new JLabel("Promociones", SwingConstants.CENTER);
        lblPromociones.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPromociones.setForeground(new Color(210, 160, 20));
        lblPromociones.setAlignmentX(CENTER_ALIGNMENT);

        panelGridPromociones = new JPanel(new GridLayout(0, 3, 15, 15));
        panelGridPromociones.setOpaque(false);

        restaurarComponentesTienda();

        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setOpaque(false);
        scrollContenido.getViewport().setOpaque(false);
        scrollContenido.setBorder(null);

        // Ubico la barra lateral a la izquierda y el catálogo en el centro
        panelContenedorFlotante.add(sidebar, BorderLayout.WEST);
        panelContenedorFlotante.add(scrollContenido, BorderLayout.CENTER);

        this.add(panelContenedorFlotante);
        this.setSize(1040, 750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*
     * En este método reorganizo los elementos en la vista central.
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
        contenidoCentralDinamico.add(panelGridProductos);
        contenidoCentralDinamico.add(Box.createVerticalStrut(25));
        contenidoCentralDinamico.add(lblPromociones);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(panelGridPromociones);

        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }

    /*
     * Limpio el grid de productos principales.
     */
    public void limpiarGridProductos() {
        panelGridProductos.removeAll();
        panelGridProductos.revalidate();
        panelGridProductos.repaint();
    }

    /*
     * Limpio el grid de productos en promoción.
     */
    public void limpiarGridPromociones() {
        panelGridPromociones.removeAll();
        panelGridPromociones.revalidate();
        panelGridPromociones.repaint();
    }

    /*
     * Agrego una tarjeta a la cuadrícula general usando solo datos primitivos.
     */
    public void agregarTarjetaProducto(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = crearTarjetaVisual(String.valueOf(id), nombre, precio, rutaImagen, listener);
        panelGridProductos.add(tarjeta);
        panelGridProductos.revalidate();
        panelGridProductos.repaint();
    }

    /*
     * Agrego una tarjeta a la cuadrícula de promociones.
     */
    public void agregarTarjetaPromocion(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = crearTarjetaVisual(String.valueOf(id), nombre, precio, rutaImagen, listener);
        panelGridPromociones.add(tarjeta);
        panelGridPromociones.revalidate();
        panelGridPromociones.repaint();
    }

    /*
     * Construyo la maquetación visual de la tarjeta individual.
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

        if (rutaImagen == null || rutaImagen.isEmpty()) {
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

    public JPanel getContenidoCentralDinamico() {
        return this.contenidoCentralDinamico;
    }
    
    
    
    
   

}
