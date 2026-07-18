/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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

    private JPanel titulo, principal;
    private JButton btnCerrar;
    private JLabel logoyNombre, TituloPrincipal, imagen,lblEstado, texto1, texto2, imgJornada, imgVentas,tituloJ, estadoJ, detalleJ, tituloVentas,actividadV, detalleVentas;
    private JPanel OpcTitulo, pnlTarjeta, pnlEstado, Jornada, textoJornada, Ventas, textoVentas;

    // Tu clase VistaBarraLateral integrada
    private VistaBarraLateral menuLateral;

    private final Color COLOR_CAFE_OSCURO = new Color(62, 58, 46);
    private final Color COLOR_DORADO = new Color(223, 205, 141);

    // CONSTRUCTOR
    public PanelAdmi() {
        // Configuración del JFrame principal
        super("Panel de Administración - N E X U S GO");
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.white);

        // --- BARRA SUPERIOR (TITULO) ---
        titulo = new JPanel(new BorderLayout());
        titulo.setBackground(COLOR_CAFE_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Icon iconLogo = new ImageIcon("logo.png");
        logoyNombre = new JLabel("Panel de Administracion  - N E X U S", iconLogo, SwingConstants.LEFT);
        logoyNombre.setForeground(Color.WHITE);
        logoyNombre.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.add(logoyNombre, BorderLayout.WEST);

        OpcTitulo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        OpcTitulo.setOpaque(false);

        btnCerrar = new JButton("Cerrar Sesion");
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);

        OpcTitulo.add(btnCerrar);
        titulo.add(OpcTitulo, BorderLayout.EAST);

        // --- TU COMPLEMENTO: VistaBarraLateral ---
        menuLateral = new VistaBarraLateral();
        menuLateral.setBackground(COLOR_DORADO);
        menuLateral.setPreferredSize(new Dimension(250, 0));
        menuLateral.setBorder(BorderFactory.createEmptyBorder(30, 15, 10, 15));


        // --- PANEL PRINCIPAL (CONTENIDO CENTRAL) ---
        principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(Color.white);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        TituloPrincipal = new JLabel("Hola, Administrador de Peluqueria Bienvenid@ a N E X U S GO");
        TituloPrincipal.setForeground(COLOR_DORADO);
        TituloPrincipal.setFont(new Font("SansSerif", Font.BOLD, 32));
        TituloPrincipal.setAlignmentX(CENTER_ALIGNMENT);

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

        lblEstado = new JLabel("Estado del Sistema");
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblEstado.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlEstado = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pnlEstado.setBackground(Color.WHITE);

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

        principal.add(TituloPrincipal);
        principal.add(Box.createVerticalStrut(40));
        principal.add(pnlTarjeta);
        principal.add(Box.createVerticalStrut(10));
        principal.add(lblEstado);
        principal.add(Box.createVerticalStrut(20));
        principal.add(pnlEstado);

        // Ensamblar todo el contenido directo en el JFrame
        this.add(titulo, BorderLayout.NORTH);
        this.add(menuLateral, BorderLayout.WEST);
        this.add(principal, BorderLayout.CENTER);

        // Ajustes de la ventana autónoma
        this.setSize(1250, 780);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public VistaBarraLateral getMenuLateral() {
        return menuLateral;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }

    private void configurarBotonLateral(JButton boton) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 16));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

   

}
