/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author INGRID
 */
public class PanelAdmi extends JFrame { 
    private JPanel principal;
    private JButton btnCerrar;
    private JLabel fondo;
    
    private VistaBarraLateral sidebar;
    public JButton btnReporte;

    // Botones explícitos para la Sidebar
    public JButton bServicios;
    public JButton bPromociones;

    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public PanelAdmi() {
    super("Panel de Administración - N E X U S GO");
    
    // 1. Fondo con imagen
    fondo = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
    fondo.setLayout(new BorderLayout());
    this.setContentPane(fondo);

    // 2. Contenedor principal
    JPanel panelContenedor = new JPanel(new BorderLayout());
    panelContenedor.setOpaque(false);
    fondo.add(panelContenedor, BorderLayout.CENTER);

    // 3. Sidebar a la izquierda
    sidebar = new VistaBarraLateral();
    sidebar.setBackground(Color.WHITE);
    sidebar.setPreferredSize(new Dimension(250, 0));
    sidebar.setBorder(BorderFactory.createEmptyBorder(30, 15, 10, 15));
    sidebar.bInventario.setVisible(false);
    sidebar.misCitas.setVisible(false);

    // Botones en la sidebar
    bServicios = new JButton(new ImageIcon("src/nexusgo/img/aggServicios.png"));
    bServicios.setFocusPainted(false);
    bServicios.setOpaque(false);
    bServicios.setContentAreaFilled(false);
    bServicios.setBorderPainted(false);

    bPromociones = new JButton(new ImageIcon("src/nexusgo/img/aggProm.png"));
    bPromociones.setBorderPainted(false);
    bPromociones.setContentAreaFilled(false);
    bPromociones.setFocusPainted(false);
    bPromociones.setOpaque(false);

    btnReporte = new JButton(new ImageIcon("src/nexusgo/img/grafica.png"));
    btnReporte.setContentAreaFilled(false);
    btnReporte.setBorderPainted(false);
    btnReporte.setFocusPainted(false);
    btnReporte.setOpaque(false);

    sidebar.add(Box.createVerticalStrut(10));
    sidebar.add(btnReporte);
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(bServicios);
    sidebar.add(Box.createVerticalStrut(10));
    sidebar.add(bPromociones);

    panelContenedor.add(sidebar, BorderLayout.WEST);

    // 4. Panel derecho completo
    JPanel panelDerechoCompleto = new JPanel(new BorderLayout());
    panelDerechoCompleto.setOpaque(false);

    // Barra superior derecha
    JPanel panelSuperiorDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
    panelSuperiorDerecho.setOpaque(false);

    btnCerrar = new JButton("Cerrar Sesión");
    btnCerrar.setBackground(Color.white);
    btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 20));
    btnCerrar.setForeground(COLOR_DORADO);
    btnCerrar.setPreferredSize(new Dimension(190, 50));

    panelSuperiorDerecho.add(btnCerrar);
    panelDerechoCompleto.add(panelSuperiorDerecho, BorderLayout.NORTH);

    // 5. Panel central dinámico
    JPanel contenido = new JPanel(new BorderLayout());
    contenido.setOpaque(false);

    principal = new JPanel();
    principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
    principal.setOpaque(false);
    principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    ReportesFinancieros vistaReportes = new ReportesFinancieros();
    principal.add(vistaReportes.VistaRF());

    contenido.add(principal, BorderLayout.CENTER);
    panelDerechoCompleto.add(contenido, BorderLayout.CENTER);

    panelContenedor.add(panelDerechoCompleto, BorderLayout.CENTER);

    this.setSize(1250, 780);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    }
    
    public VistaBarraLateral getMenuLateral() {
        return sidebar;
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
