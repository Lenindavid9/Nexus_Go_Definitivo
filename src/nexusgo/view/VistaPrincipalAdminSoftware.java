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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 *
 * @author USUARIO
 */
public class VistaPrincipalAdminSoftware extends JFrame {

    public VistaBarraLateral sidebar;
    public JPanel contenido, panelSuperiorDerecho, panelDerechoCompleto,panelContenedor;
    public JLabel fondo;
    public JButton btnCerrarSesion;
    
    private final Color COLOR_DORADO = new Color(184, 134, 11);


    public VistaPrincipalAdminSoftware() {
        super("Sistema NexusGO - Administrador de Software");
        // 1. Fondo con imagen
        fondo = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // 2. Contenedor principal
        panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setOpaque(false);

        // 3. Sidebar a la izquierda (ocupa toda la altura)
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(200, 550));
        sidebar.setBackground(Color.WHITE);
        sidebar.misCitas.setVisible(false);
        panelContenedor.add(sidebar, BorderLayout.WEST);

        // 4. Panel derecho completo
        panelDerechoCompleto = new JPanel(new BorderLayout());
        panelDerechoCompleto.setOpaque(false);

        // Barra superior derecha con botón
        panelSuperiorDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        panelSuperiorDerecho.setOpaque(false);

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(Color.white);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnCerrarSesion.setForeground(COLOR_DORADO);
        btnCerrarSesion.setPreferredSize(new Dimension(190, 50));
        

        panelSuperiorDerecho.add(btnCerrarSesion);
        panelDerechoCompleto.add(panelSuperiorDerecho, BorderLayout.NORTH);

        // Panel central dinámico
        contenido = new JPanel(new BorderLayout());
        contenido.setOpaque(false);
        panelDerechoCompleto.add(contenido, BorderLayout.CENTER);

        // Ensamblaje final
        panelContenedor.add(panelDerechoCompleto, BorderLayout.CENTER);
        fondo.add(panelContenedor, BorderLayout.CENTER);

        // Configuración de la ventana
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Getter público para que el controlador pueda escuchar los botones del
     * menú lateral.
     */
    public VistaBarraLateral getsidebar() {
        return sidebar;
    }

    /**
     * Getter público para el panel de contenido central dinámico.
     */
    public JPanel getContenido() {
        return contenido;
    }

    public JPanel getContenedorCentral() {
        return this.contenido;
    }
    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

}
