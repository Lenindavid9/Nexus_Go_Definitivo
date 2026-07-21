/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import nexusgo.model.Usuario;

/**
 *
 * @author USUARIO
 */
public class VistaPrincipalOperario extends JFrame {

    public VistaBarraLateral sidebar;
    public JPanel contenido, panelSuperiorDerecho, panelDerechoCompleto;
    public JButton btnCerrarSesion;

    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public VistaPrincipalOperario() {
        super("Sistema NexusGO - Panel de Gestión");

        // 1. Fondo con imagen
        JLabel fondo = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // 2. Contenedor principal
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setOpaque(false);

        // 3. Sidebar a la izquierda (ocupa toda la altura)
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(200, Integer.MAX_VALUE));
        sidebar.setBackground(Color.WHITE);
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

    public VistaBarraLateral getsidebar() {
        return sidebar;
    }

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