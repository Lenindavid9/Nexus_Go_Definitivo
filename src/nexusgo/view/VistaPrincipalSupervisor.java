/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USUARIO
 */
public class VistaPrincipalSupervisor extends JFrame {
    
    private JLabel fondo;
    private JPanel panelContenedorFlotante,contenidoCentralDinamico;
    private JScrollPane scrollContenido;
    
    // Componentes modulares reutilizados
    public VistaBarraLateral sidebar;
    public PanelBienvenida panelBienvenida;
    
    // Botones específicos del Supervisor
    public JButton btnCaja,btnCerrarSesion;
    
    public VistaPrincipalSupervisor(String nombreUsuario, String rolUsuario) {
        super("Nexus GO - Panel de Supervisor");
        
        // 1. Reutilización del fondo de mármol con centrado GridBagLayout
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setLayout(new GridBagLayout());
        this.setContentPane(fondo);

        // 2. Contenedor flotante tipo "tarjeta"
        panelContenedorFlotante = new JPanel(new BorderLayout());
        panelContenedorFlotante.setPreferredSize(new Dimension(1000, 680));
        panelContenedorFlotante.setOpaque(false);
        panelContenedorFlotante.setBackground(Color.red);

        // 3. BARRA LATERAL (Ajustada para Supervisor)
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(140, 680));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(80, 10, 20, 10));

        // Inyección del nuevo botón solicitado: Caja
        btnCaja = new JButton("Caja");
        sidebar.add(btnCaja);

        // Configuración de visibilidad de los botones base según permisos del Supervisor
        sidebar.bCasa.setVisible(true);       // Inicio
        sidebar.bInventario.setVisible(true); // Ventas / Inventario
        sidebar.misCitas.setVisible(true);    // Gestión de Citas

        // 4. PANEL CENTRAL DINÁMICO
        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setOpaque(false);
        contenidoCentralDinamico.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Inyección modular del panel de bienvenida
        panelBienvenida = new PanelBienvenida(nombreUsuario, rolUsuario);
        panelBienvenida.setPreferredSize(new Dimension(650, 150));
        panelBienvenida.setMaximumSize(new Dimension(650, 150));
        panelBienvenida.setOpaque(false);

        // Botón de cierre de sesión
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(Color.decode("#EFB810"));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setAlignmentX(CENTER_ALIGNMENT);

        // Armamos el estado inicial del panel central
        restaurarVistaInicial();

        // Soporte de scroll para resoluciones bajas o listados extensos
        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setOpaque(false);
        scrollContenido.getViewport().setOpaque(false);
        scrollContenido.setBorder(null);

        // 5. Ensamblaje final
        panelContenedorFlotante.add(sidebar, BorderLayout.WEST);
        panelContenedorFlotante.add(scrollContenido, BorderLayout.CENTER);

        this.add(panelContenedorFlotante);

        // Ajustes del marco principal
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Limpia el contenedor central dinámico y reestablece el estado inicial
     * del Supervisor (Bienvenida y cierre de sesión).
     */
    public void restaurarVistaInicial() {
        contenidoCentralDinamico.removeAll();
        contenidoCentralDinamico.add(panelBienvenida);
        contenidoCentralDinamico.add(Box.createVerticalStrut(15));
        contenidoCentralDinamico.add(btnCerrarSesion);
        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }

    /**
     * Getter para que los controladores inyecten vistas (Tablas, Formularios)
     * en el centro de manera dinámica.
     */
    public JPanel getContenidoCentralDinamico() {
        return contenidoCentralDinamico;
    }

    
    
   
}
