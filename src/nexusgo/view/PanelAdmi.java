/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author INGRID
 */
public class PanelAdmi extends JFrame { 

    // --- Componentes Visuales Principales ---
    private JPanel principal;
    private JButton btnCerrar;
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
        bServicios = new JButton(new ImageIcon("src/nexusgo/img/aggServicios.png"));  // quita el fondo gris
        bServicios.setFocusPainted(false);
        bServicios.setOpaque(false);
        bServicios.setFocusPainted(false);
        bServicios.setContentAreaFilled(false);
        bServicios.setBorderPainted(false);

        bPromociones = new JButton(new ImageIcon("src/nexusgo/img/aggProm.png"));
        bPromociones.setFont(new Font("SansSerif", Font.BOLD, 14));
        bPromociones.setBorderPainted(false);
        bPromociones.setContentAreaFilled(false);
        bPromociones.setFocusPainted(false);
        bPromociones.setOpaque(false);

        btnReporte = new JButton("Reportes Financieros");
        btnReporte.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnReporte.setContentAreaFilled(false);
        btnReporte.setBorderPainted(false);
        btnReporte.setFocusPainted(false);
        btnReporte.setOpaque(false);

        menuLateral.add(Box.createVerticalStrut(10));
        menuLateral.add(btnReporte);


        // Agregamos los botones al contenedor de la barra lateral si no estaban añadidos
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(bServicios);
        menuLateral.add(Box.createVerticalStrut(10));
        menuLateral.add(bPromociones);

        // 3. Inicialización del Panel Central de Trabajo
        // 3. Inicialización del Panel Central de Trabajo
        principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setOpaque(false);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

// --- Aquí agregas la vista de reportes desde el inicio ---
        ReportesFinancieros vistaReportes = new ReportesFinancieros();
        principal.add(vistaReportes.VistaRF());



        

        // Ensamble final en la ventana
        this.getContentPane().add(menuLateral, BorderLayout.WEST);
        this.getContentPane().add(principal, BorderLayout.CENTER);

        // Ajustes del Frame
        this.setSize(1250, 780);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    

    // --- Getters de control ---
    public VistaBarraLateral getMenuLateral() {
        return menuLateral;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }
    
    public JButton getBtnReporte() {
        return btnReporte;
    }

    public JPanel getContenidoCentral() {
        return principal;
    }
}
