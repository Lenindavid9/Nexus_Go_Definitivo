/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author INGRID
 */
public class PanelAdmi extends JFrame { 

    // --- Componentes Visuales Principales ---
    private JPanel principal;
    private JButton btnCerrar;
    private JLabel TituloPrincipal, imagen, lblEstado, texto1, texto2, imgJornada, imgVentas, tituloJ, estadoJ, detalleJ, tituloVentas, actividadV, detalleVentas;
    private JPanel pnlTarjeta, pnlEstado, Jornada, textoJornada, Ventas, textoVentas;

    // --- Componentes de Navegación y Barra Lateral ---
    private VistaBarraLateral menuLateral;
    public JButton btnReporte;

    // Botones explícitos para la Sidebar
    public JButton bServicios;
    public JButton bPromociones;

    // --- Constantes de Estilo ---
    private final Color COLOR_DORADO = new Color(223, 205, 141);

    /**
     * Constructor principal. Configura la ventana, la barra lateral con los nuevos
     * botones de Servicios y Promociones, y ensambla el panel central.
     */
    public PanelAdmi() {
        super("Panel de Administración - N E X U S GO");
        
        // 1. Configuración de la imagen de fondo en el ContentPane
        JLabel fondoConImagen = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
        fondoConImagen.setLayout(new BorderLayout());
        this.setContentPane(fondoConImagen);

        // 2. Inicialización de la Barra Lateral
        menuLateral = new VistaBarraLateral();
        menuLateral.setBackground(Color.WHITE);
        menuLateral.setPreferredSize(new Dimension(250, 0));
        menuLateral.setBorder(BorderFactory.createEmptyBorder(30, 15, 10, 15));
        menuLateral.bInventario.setVisible(false); // Ventas / Inventario
        menuLateral.misCitas.setVisible(false);

        // Asignación/Creación de los botones de Servicios y Promociones en la sidebar
        bServicios = new JButton(new ImageIcon("src/nexusgo/img/aggServicios.jpg"));
        bServicios.setBorderPainted(false);      // quita el borde
        bServicios.setContentAreaFilled(false);  // quita el fondo gris
        bServicios.setFocusPainted(false);       // quita el resaltado al hacer clic
        bServicios.setOpaque(false);             // asegura transparencia
        bServicios.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bServicios.setToolTipText("Agregar Servicios");

        bPromociones = new JButton(new ImageIcon("src/nexusgo/img/aggProm.jpg"));
        bPromociones.setFont(new Font("SansSerif", Font.BOLD, 14));
        bPromociones.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregamos los botones al contenedor de la barra lateral si no estaban añadidos
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(bServicios);
        menuLateral.add(Box.createVerticalStrut(10));
        menuLateral.add(bPromociones);

        // 3. Inicialización del Panel Central de Trabajo
        principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(Color.WHITE);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Encabezado principal
        TituloPrincipal = new JLabel("Hola, Administrador de Peluqueria Bienvenid@ a N E X U S GO");
        TituloPrincipal.setForeground(COLOR_DORADO);
        TituloPrincipal.setFont(new Font("SansSerif", Font.BOLD, 30));
        TituloPrincipal.setAlignmentX(CENTER_ALIGNMENT);

        // Tarjeta central de acceso rápido a Reportes
        pnlTarjeta = new JPanel();
        pnlTarjeta.setLayout(new BoxLayout(pnlTarjeta, BoxLayout.Y_AXIS));
        pnlTarjeta.setBackground(Color.WHITE);
        pnlTarjeta.setPreferredSize(new Dimension(400, 400));

        imagen = new JLabel(new ImageIcon("accesorapido.png"));
        imagen.setAlignmentX(Component.CENTER_ALIGNMENT);

        texto1 = new JLabel("Gestión de Reportes Financieros");
        texto1.setFont(new Font("SansSerif", Font.PLAIN, 13));
        texto1.setAlignmentX(Component.CENTER_ALIGNMENT);

        texto2 = new JLabel("Reportes");
        texto2.setFont(new Font("SansSerif", Font.BOLD, 15));
        texto2.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlTarjeta.add(imagen);
        pnlTarjeta.add(Box.createVerticalStrut(10));
        pnlTarjeta.add(texto1);
        pnlTarjeta.add(texto2);

        // Sección de Estado del Sistema
        lblEstado = new JLabel("Estado del Sistema");
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblEstado.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlEstado = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pnlEstado.setBackground(Color.WHITE);

        // Subpanel: Jornada
        Jornada = new JPanel(new BorderLayout());
        Jornada.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        Jornada.setPreferredSize(new Dimension(450, 100));
        Jornada.setBackground(Color.WHITE);

        imgJornada = new JLabel(new ImageIcon("jornada.png"));
        imgJornada.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        Jornada.add(imgJornada, BorderLayout.WEST);

        textoJornada = new JPanel();
        textoJornada.setLayout(new BoxLayout(textoJornada, BoxLayout.Y_AXIS));
        textoJornada.setBackground(Color.WHITE);
        textoJornada.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 10));

        tituloJ = new JLabel("Jornada");
        tituloJ.setFont(new Font("SansSerif", Font.BOLD, 16));

        estadoJ = new JLabel("Último estado");
        estadoJ.setFont(new Font("SansSerif", Font.PLAIN, 12));

        detalleJ = new JLabel("Sin cambios recientes. Lista para apertura");
        detalleJ.setFont(new Font("SansSerif", Font.PLAIN, 12));

        textoJornada.add(tituloJ);
        textoJornada.add(estadoJ);
        textoJornada.add(detalleJ);

        Jornada.add(textoJornada, BorderLayout.CENTER);

        // Subpanel: Ventas
        Ventas = new JPanel(new BorderLayout());
        Ventas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        Ventas.setPreferredSize(new Dimension(450, 100));
        Ventas.setBackground(Color.WHITE);

        imgVentas = new JLabel(new ImageIcon("ventas.png"));
        imgVentas.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        Ventas.add(imgVentas, BorderLayout.WEST);

        textoVentas = new JPanel();
        textoVentas.setLayout(new BoxLayout(textoVentas, BoxLayout.Y_AXIS));
        textoVentas.setBackground(Color.WHITE);
        textoVentas.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        tituloVentas = new JLabel("Ventas (Punto de Venta)");
        tituloVentas.setFont(new Font("SansSerif", Font.BOLD, 16));

        actividadV = new JLabel("Actividad");
        actividadV.setFont(new Font("SansSerif", Font.PLAIN, 12));

        detalleVentas = new JLabel("Ventas Desactivadas");
        detalleVentas.setFont(new Font("SansSerif", Font.PLAIN, 12));

        textoVentas.add(tituloVentas);
        textoVentas.add(actividadV);
        textoVentas.add(detalleVentas);

        Ventas.add(textoVentas, BorderLayout.CENTER);

        pnlEstado.add(Jornada);
        pnlEstado.add(Ventas);

        // Ensamble de la vista central
        principal.add(TituloPrincipal);
        principal.add(Box.createVerticalStrut(40));
        principal.add(pnlTarjeta);
        principal.add(Box.createVerticalStrut(10));
        principal.add(lblEstado);
        principal.add(Box.createVerticalStrut(20));
        principal.add(pnlEstado);

        // Ensamble final en la ventana
        this.getContentPane().add(menuLateral, BorderLayout.WEST);
        this.getContentPane().add(principal, BorderLayout.CENTER);

        // Ajustes del Frame
        this.setSize(1250, 780);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // --- Getters de control ---
    public VistaBarraLateral getMenuLateral() {
        return menuLateral;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }
    
    public JPanel getPnlTarjeta() {
        return pnlTarjeta;
    }

    public JPanel getPanelTarjeta() {
        return pnlTarjeta;
    }
    
    public JButton getBtnReporte() {
        return btnReporte;
    }

    public JPanel getContenidoCentral() {
        return principal;
    }
}
