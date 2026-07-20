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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author HOME
 */

public class VistaPrincipalPeluquero extends JFrame {
    // Componentes del Sidebar y barra superior accesibles para el controlador
    public JButton btnInicio = new JButton("Casa");
    public JButton btnInventario = new JButton("Inventario");
    public JButton btnCitas = new JButton("Citas");
    public JButton btnCerrarSesion = new JButton("cerrar sesion");
    
    private JPanel contenidoCentralDinamico = new JPanel(new BorderLayout());

    public VistaPrincipalPeluquero() {
        setTitle("NexusGO - Panel de Peluquero");
        

        // 1. CONSTRUCCIÓN DE LA BARRA LATERAL (SIDEBAR)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(80, 550));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        JLabel lblLogo = new JLabel("NX");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(new Color(212, 175, 55));
        lblLogo.setAlignmentX(CENTER_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(20, 0, 40, 0));
        sidebar.add(lblLogo);

        // Configuración rápida de los botones del menú lateral
        JButton[] botones = {btnInicio, btnInventario, btnCitas};
        for (JButton btn : botones) {
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(CENTER_ALIGNMENT);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(15));
        }

        // 2. BARRA SUPERIOR (CERRAR SESIÓN)
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        panelTop.setOpaque(false);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(255, 213, 79));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBorder(new EmptyBorder(5, 15, 5, 15));
        panelTop.add(btnCerrarSesion);

        // 3. TARJETA GRIS DE BIENVENIDA CENTRAL
        JPanel tarjetaBienvenida = new JPanel();
        tarjetaBienvenida.setLayout(new BoxLayout(tarjetaBienvenida, BoxLayout.Y_AXIS));
        tarjetaBienvenida.setBackground(new Color(245, 245, 245));
        tarjetaBienvenida.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel lblSaludo = new JLabel("Hola, Peluquer@ Bienvenido a Nexus GO");
        lblSaludo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSaludo.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel lblTexto1 = new JLabel("Espero que te encuentres super bien,");
        lblTexto1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTexto1.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel lblTexto2 = new JLabel("mira tus citas para estar al tanto de las cosas");
        lblTexto2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTexto2.setAlignmentX(CENTER_ALIGNMENT);

        tarjetaBienvenida.add(lblSaludo);
        tarjetaBienvenida.add(Box.createVerticalStrut(25));
        tarjetaBienvenida.add(lblTexto1);
        tarjetaBienvenida.add(lblTexto2);

        // Centramos la tarjeta usando GridBagLayout en un contenedor intermedio
        JPanel panelContenedorTarjeta = new JPanel(new GridBagLayout());
        panelContenedorTarjeta.setOpaque(false);
        panelContenedorTarjeta.add(tarjetaBienvenida);

        // 4. ENSAMBLAR TODO EN EL MARCO PRINCIPAL
        contenidoCentralDinamico.add(panelTop, BorderLayout.NORTH);
        contenidoCentralDinamico.add(panelContenedorTarjeta, BorderLayout.CENTER);

        this.add(sidebar, BorderLayout.WEST);
        this.add(contenidoCentralDinamico, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Getter para que el controlador limpie e intercambie las subvistas en el centro.
     */
    public JPanel getContenidoCentralDinamico() {
        return contenidoCentralDinamico;
        
        
        
    }
    public void restaurarComponentesPrincipales() {
    // Limpia el panel central dinámico para dejarlo en su estado base
    this.getContenidoCentralDinamico().removeAll();
    this.getContenidoCentralDinamico().revalidate();
    this.getContenidoCentralDinamico().repaint();
}
    
}
