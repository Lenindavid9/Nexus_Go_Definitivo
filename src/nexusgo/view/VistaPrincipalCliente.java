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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
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

    // Grids independientes para cada catálogo
    private JPanel panelGridProductos;
    private JPanel panelGridPromociones;
    private JPanel panelGridCombos;      // SECCIÓN DEDICADA PARA COMBOS
    private JPanel panelGridServicios;
    private JScrollPane scrollContenido;

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

        this.fondoMarmol = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
        this.fondoMarmol.setLayout(new BorderLayout());
        this.setContentPane(fondoMarmol);

        JPanel contenedorEstructural = new JPanel(new BorderLayout(15, 0));
        contenedorEstructural.setPreferredSize(new Dimension(980, 650));
        contenedorEstructural.setOpaque(false);

        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(80, 650));
        sidebar.setBackground(Color.WHITE);
        if (sidebar.bInventario != null) {
            sidebar.bInventario.setVisible(false);
        }
        if (sidebar.misCitas != null) {
            sidebar.misCitas.setVisible(true);
        }

        panelFlotanteBlanco = new JPanel(new BorderLayout());
        panelFlotanteBlanco.setOpaque(false);
        panelFlotanteBlanco.setBorder(new EmptyBorder(15, 20, 15, 20));

        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setBackground(Color.WHITE);
        contenidoCentralDinamico.setOpaque(false);

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

        restaurarComponentesTienda();

        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setBackground(Color.WHITE);
        scrollContenido.getViewport().setBackground(Color.WHITE);
        scrollContenido.setBorder(null);

        panelFlotanteBlanco.add(scrollContenido, BorderLayout.CENTER);

        contenedorEstructural.add(sidebar, BorderLayout.WEST);
        contenedorEstructural.add(panelFlotanteBlanco, BorderLayout.CENTER);

        this.add(contenedorEstructural);
        this.setSize(1040, 720);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void restaurarComponentesTienda() {
        contenidoCentralDinamico.removeAll();

        // Inicialización de Grids
        panelGridProductos = crearGridPanel();
        panelGridPromociones = crearGridPanel();
        panelGridCombos = crearGridPanel();      // Grid Combos
        panelGridServicios = crearGridPanel();

        // Header Superior
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelHeader.add(lblBienvenida, BorderLayout.CENTER);

        JPanel panelBotonDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBotonDerecha.setOpaque(false);
        panelBotonDerecha.add(btnCerrarSesion);
        panelHeader.add(panelBotonDerecha, BorderLayout.EAST);

        // Banners / Encabezados de Sección
        JPanel panelEtiquetaPromo = crearBannerEtiqueta("Promociones");
        JPanel panelEtiquetaCombos = crearBannerEtiqueta("Kits y Combos");
        JPanel panelEtiquetaServicios = crearBannerEtiqueta("Servicios");

        // ENSAMBLADO VERTICAL ORDENADO
        contenidoCentralDinamico.add(panelHeader);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));

        contenidoCentralDinamico.add(panelGridProductos);
        contenidoCentralDinamico.add(Box.createVerticalStrut(20));

        contenidoCentralDinamico.add(btnReservarCita);
        contenidoCentralDinamico.add(Box.createVerticalStrut(20));

        // 1. Promociones
        contenidoCentralDinamico.add(panelEtiquetaPromo);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(panelGridPromociones);
        contenidoCentralDinamico.add(Box.createVerticalStrut(25));

        // 2. Combos
        contenidoCentralDinamico.add(panelEtiquetaCombos);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(panelGridCombos);
        contenidoCentralDinamico.add(Box.createVerticalStrut(25));

        // 3. Servicios
        contenidoCentralDinamico.add(panelEtiquetaServicios);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(panelGridServicios);

        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }

    private JPanel crearGridPanel() {
        JPanel grid = new JPanel(new GridLayout(0, 3, 15, 15));
        grid.setBackground(Color.WHITE);
        grid.setOpaque(false);
        return grid;
    }

    private JPanel crearBannerEtiqueta(String titulo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        JLabel label = new JLabel(titulo, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(255, 220, 90));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setPreferredSize(new Dimension(150, 26));
        panel.add(label);
        return panel;
    }

    // Métodos para agregar tarjetas por tipo explícito
    public void agregarTarjetaProducto(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        panelGridProductos.add(crearTarjetaVisual("PROD_" + id, nombre, precio, rutaImagen, listener));
    }

    public void agregarTarjetaPromocion(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        panelGridPromociones.add(crearTarjetaVisual("PROMO_" + id, nombre, precio, rutaImagen, listener));
    }

    public void agregarTarjetaCombo(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        panelGridCombos.add(crearTarjetaVisual("COMBO_" + id, nombre, precio, rutaImagen, listener));
    }

    public void agregarTarjetaServicio(int id, String nombre, double precio, String rutaImagen, MouseListener listener) {
        panelGridServicios.add(crearTarjetaVisual("SERV_" + id, nombre, precio, rutaImagen, listener));
    }

    private JPanel crearTarjetaVisual(String idStr, String nombre, double precio, String rutaImagen, MouseListener listener) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(248, 248, 248));
        tarjeta.setBorder(new EmptyBorder(10, 10, 10, 10));
        tarjeta.setName(idStr);
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (listener != null) {
            tarjeta.addMouseListener(listener);
        }

        JLabel lblImg = new JLabel();
        lblImg.setAlignmentX(CENTER_ALIGNMENT);
        lblImg.setName(idStr);
        if (listener != null) {
            lblImg.addMouseListener(listener);
        }

        String rutaLimpia = (rutaImagen != null) ? rutaImagen.trim() : "";

        /* Nos quedamos solo con el nombre del archivo (sin importar qué carpeta
        traiga por delante en la BD, como "img/productos/x.jpg" o "src/img/x.jpg").*/
        String nombreArchivo = rutaLimpia.isEmpty() ? "default.jpg" : new File(rutaLimpia).getName();

        ImageIcon iconoOriginal = null;

        /* Las imágenes subidas desde el panel de inventario quedan guardadas
        en disco, en la carpeta "img/" junto al sistema (no dentro del
        proyecto), así que se buscan primero ahí como archivo real.*/
        File archivoEnDisco = new File("img", nombreArchivo);

        // Se verifica si el archivo de imagen realmente existe en la ubicación indicada dentro del disco.
        if (archivoEnDisco.exists()) {

            /* Si el archivo fue encontrado, se crea un objeto ImageIcon
            utilizando la ruta del archivo. Este objeto permitirá
            mostrar la imagen*/
            iconoOriginal = new ImageIcon(archivoEnDisco.getPath());
        }

        /* Si no está en disco, probamos como recurso interno del proyecto
        (por si la imagen viene empaquetada dentro de src/nexusgo/img).
        
        Se verifica si la imagen aún no ha sido cargada correctamente.

        La condición será verdadera en dos casos:

        Priero Si iconoOriginal es null, significa que todavía no existe ninguna imagen cargada.
        
        segundo Si el ancho de la imagen es menor o igual a cero, significa
        que la imagen no pudo cargarse correctamente.*/
        if (iconoOriginal == null || iconoOriginal.getIconWidth() <= 0) {
            
             /* Se intenta como "localizar" la imagen dentro de los recursos
             internos del proyecto utilizando su nombre de archivo.*/
            URL imgURL = getClass().getResource("/nexusgo/img/" + nombreArchivo);
            
            // Se verifica que la imagen haya sido encontrada.
            if (imgURL != null) {
                
                // Si la imagen existe dentro del proyecto, se crea un nuevo objeto
                iconoOriginal = new ImageIcon(imgURL);
            }
        }

        // Si nada de lo anterior funcionó, usamos la imagen por defecto.
        if (iconoOriginal == null || iconoOriginal.getIconWidth() <= 0) {
            
            //Se usa la imagen predeterminada.
            URL defaultURL = getClass().getResource("/nexusgo/img/default.jpg");
            
            /*se utiliza el operador ternario para comprobar si la imagen predeterminada fue encontrada.
            (RECUERDEN QUE!! (? :), que es una forma abreviada de escribir un if...else.)
            Si defaultURL contiene una ruta válida, se crea un nuevo
            objeto ImageIcon con esa imagen.
            
            Si no se encuentra el archivo, la variable quedará con
            el valor null.*/
            iconoOriginal = (defaultURL != null) ? new ImageIcon(defaultURL) : null;
        }

        if (iconoOriginal != null && iconoOriginal.getImage() != null && iconoOriginal.getIconWidth() > 0) {
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(140, 100, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblImg.setText("[Sin Foto]");
            lblImg.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblImg.setForeground(Color.GRAY);
        }

        JLabel lblNombre = new JLabel(nombre, SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        lblNombre.setName(idStr);
        if (listener != null) {
            lblNombre.addMouseListener(listener);
        }

        JLabel lblPrecio = new JLabel(String.format("$%.2f", precio), SwingConstants.CENTER);
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrecio.setForeground(new Color(40, 140, 40));
        lblPrecio.setAlignmentX(CENTER_ALIGNMENT);
        lblPrecio.setName(idStr);
        if (listener != null) {
            lblPrecio.addMouseListener(listener);
        }

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
