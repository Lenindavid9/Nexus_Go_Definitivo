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
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
    public JButton btnInicio = new JButton(new ImageIcon("src/nexusgo/img/inicio.png"));
    public JButton btnInventario = new JButton(new ImageIcon("src/nexusgo/img/inventario.png"));
    public JButton btnCitas = new JButton(new ImageIcon("src/nexusgo/img/citas.png"));
    public JButton btnCerrarSesion = new JButton("Cerrar Sesión");

    private JPanel contenidoCentralDinamico;
    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public VistaPrincipalPeluquero() {
        super("NexusGO - Panel de Peluquero");

        // 1. Panel de fondo con imagen
        JPanel fondo = new JPanel() {
            private Image imagen = new ImageIcon("src/nexusgo/img/fondoprincipal.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        };
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // 2. Sidebar lateral
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(200, 550));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));


        JButton[] botones = {btnInicio, btnInventario, btnCitas};
        for (JButton btn : botones) {
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(CENTER_ALIGNMENT);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(70));
        }

        // 3. Panel superior con botón cerrar sesión
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        panelTop.setOpaque(false);
        
        
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnCerrarSesion.setForeground(COLOR_DORADO);
        btnCerrarSesion.setBackground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBorder(new EmptyBorder(5, 15, 5, 15));
        panelTop.add(btnCerrarSesion);

        // 4. Panel central dinámico transparente
        contenidoCentralDinamico = new JPanel(new BorderLayout());
        contenidoCentralDinamico.setOpaque(false);

        // Tarjeta de bienvenida
        JPanel tarjetaBienvenida = new JPanel();
        tarjetaBienvenida.setLayout(new BoxLayout(tarjetaBienvenida, BoxLayout.Y_AXIS));
        tarjetaBienvenida.setOpaque(false);
        tarjetaBienvenida.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        JLabel lblSaludo = new JLabel("Hola, Peluquer@ Bienvenido a Nexus GO");
        lblSaludo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblSaludo.setAlignmentX(CENTER_ALIGNMENT);
        lblSaludo.setForeground(Color.WHITE);

        JLabel lblTexto1 = new JLabel("Espero que te encuentres super bien.");
        lblTexto1.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblTexto1.setAlignmentX(CENTER_ALIGNMENT);
        lblTexto1.setForeground(Color.WHITE);

        tarjetaBienvenida.add(lblSaludo);
        tarjetaBienvenida.add(Box.createVerticalStrut(25));
        tarjetaBienvenida.add(lblTexto1);

        JPanel panelContenedorTarjeta = new JPanel(new GridBagLayout());
        panelContenedorTarjeta.setOpaque(false);
        panelContenedorTarjeta.add(tarjetaBienvenida);

        contenidoCentralDinamico.add(panelTop, BorderLayout.NORTH);
        contenidoCentralDinamico.add(panelContenedorTarjeta, BorderLayout.CENTER);

        // Ensamblaje final
        fondo.add(sidebar, BorderLayout.WEST);
        fondo.add(contenidoCentralDinamico, BorderLayout.CENTER);

        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public JPanel getContenidoCentralDinamico() {
        return contenidoCentralDinamico;
    }

    public void restaurarComponentesPrincipales() {
        contenidoCentralDinamico.removeAll();
        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }
}