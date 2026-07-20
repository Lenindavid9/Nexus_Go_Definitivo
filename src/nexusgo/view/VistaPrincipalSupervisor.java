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
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USUARIO
 */
public class VistaPrincipalSupervisor extends JFrame {
    
   private JLabel fondo;
    private JPanel panelContenedorFlotante, contenidoCentralDinamico;
    private JScrollPane scrollContenido;

    // Componentes modulares reutilizados
    public VistaBarraLateral sidebar;
    public PanelBienvenida panelBienvenida;

    // Botones específicos del Supervisor
    public JButton btnCaja, btnCerrarSesion;

    public VistaPrincipalSupervisor(String nombreUsuario, String rolUsuario) {
        super("Nexus GO - Panel de Supervisor");

        
        // 1. Reutilización del fondo de mármol
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setLayout(new GridBagLayout());
        this.setContentPane(fondo);

        // 2. Contenedor flotante dinámico (Ocupa todo el espacio de la ventana)
        panelContenedorFlotante = new JPanel(new BorderLayout());
        panelContenedorFlotante.setOpaque(false);

        // 3. BARRA LATERAL (Full a la izquierda)
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(180, Integer.MAX_VALUE));
        sidebar.setMinimumSize(new Dimension(180, 0));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(40, 10, 20, 10));

        // Inyección del botón Caja
        btnCaja = new JButton("Caja");
        sidebar.add(btnCaja);

        // Configuración de visibilidad según permisos
        sidebar.bCasa.setVisible(true);       // Inicio
        sidebar.bInventario.setVisible(true); // Ventas / Inventario
        sidebar.misCitas.setVisible(true);    // Gestión de Citas

        // 4. PANEL CENTRAL DINÁMICO
        contenidoCentralDinamico = new JPanel();
        contenidoCentralDinamico.setLayout(new BoxLayout(contenidoCentralDinamico, BoxLayout.Y_AXIS));
        contenidoCentralDinamico.setOpaque(false);
        contenidoCentralDinamico.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Inyección modular del panel de bienvenida (Mayor espacio disponible)
        panelBienvenida = new PanelBienvenida(nombreUsuario, rolUsuario);
        panelBienvenida.setPreferredSize(new Dimension(900, 180));
        panelBienvenida.setMaximumSize(new Dimension(Short.MAX_VALUE, 180)); // Permite expandirse horizontalmente
        panelBienvenida.setOpaque(false);

        // Botón de cierre de sesión
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(Color.decode("#EFB810"));
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setPreferredSize(new Dimension(160, 40));
        btnCerrarSesion.setMaximumSize(new Dimension(160, 40));
        btnCerrarSesion.setAlignmentX(CENTER_ALIGNMENT);

        // Armamos el estado inicial del panel central
        restaurarVistaInicial();

        // Soporte de Scroll Optimizado (Solo aparece si el contenido supera la pantalla)
        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setOpaque(false);
        scrollContenido.getViewport().setOpaque(false);
        scrollContenido.setBorder(null);
        scrollContenido.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContenido.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollContenido.getVerticalScrollBar().setUnitIncrement(16); // Scroll suave

        // 5. Ensamblaje final
        panelContenedorFlotante.add(sidebar, BorderLayout.WEST);
        panelContenedorFlotante.add(scrollContenido, BorderLayout.CENTER);

        // Añadir contenedor a la ventana completa
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panelContenedorFlotante, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    /**
     * Limpia el contenedor central dinámico y reestablece el estado inicial
     * del Supervisor (Bienvenida y cierre de sesión).
     */
    public void restaurarVistaInicial() {
        contenidoCentralDinamico.removeAll();
        contenidoCentralDinamico.add(panelBienvenida);
        contenidoCentralDinamico.add(Box.createVerticalStrut(25));
        contenidoCentralDinamico.add(btnCerrarSesion);
        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
        
        // Configuración básica del Frame para Pantalla Completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizado a pantalla completa
        setMinimumSize(new Dimension(1024, 600));  // Tamaño mínimo seguro para pantallas pequeñas

    }

    /**
     * Getter para que los controladores inyecten vistas (Tablas, Formularios)
     * en el centro de manera dinámica.
     */
    public JPanel getContenidoCentralDinamico() {
        return contenidoCentralDinamico;
    }
    
    
    
    
   
}
