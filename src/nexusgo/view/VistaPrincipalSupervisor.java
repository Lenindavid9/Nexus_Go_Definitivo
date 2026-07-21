package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
    private JPanel panelContenedorFlotante, contenidoCentralDinamico, panelSuperiorDerecho, panelDerechoCompleto;
    private JScrollPane scrollContenido;

    // Componentes modulares reutilizados
    public VistaBarraLateral sidebar;
    public PanelBienvenida panelBienvenida;

    // Botones específicos del Supervisor
    public JButton btnCaja, btnCerrarSesion;

    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public VistaPrincipalSupervisor(String nombreUsuario, String rolUsuario) {
        super("Nexus GO - Panel de Supervisor");

        // Configuración básica para pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 600));

        // 1. Fondo de mármol
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
        this.fondo.setLayout(new GridBagLayout());
        this.setContentPane(fondo);

        // 2. Contenedor flotante dinámico
        panelContenedorFlotante = new JPanel(new BorderLayout());
        panelContenedorFlotante.setOpaque(false);

        // 3. BARRA LATERAL (Va directamente a la WEST para ocupar el 100% de la altura)
        sidebar = new VistaBarraLateral();
        sidebar.setPreferredSize(new Dimension(180, Integer.MAX_VALUE));
        sidebar.setMinimumSize(new Dimension(180, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        btnCaja = new JButton(new ImageIcon("src/nexusgo/img/caja.png"));
        btnCaja.setBorderPainted(false);   // Quita el borde
        btnCaja.setContentAreaFilled(false); // Quita el fondo
        btnCaja.setFocusPainted(false);    // Quita el resaltado al hacer clic
        btnCaja.setOpaque(false);
        sidebar.add(btnCaja);

        sidebar.bCasa.setVisible(true);       // Inicio
        sidebar.bInventario.setVisible(true); // Ventas / Inventario
        sidebar.misCitas.setVisible(true);    // Gestión de Citas

        // 4. PANEL DERECHO COMPLETO (Contendrá la barra superior con el botón + el contenido central)
        panelDerechoCompleto = new JPanel(new BorderLayout());
        panelDerechoCompleto.setOpaque(false);

        // Header solo para el lado derecho (Botón en la esquina superior derecha)
        panelSuperiorDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        panelSuperiorDerecho.setOpaque(false);

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(Color.white);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnCerrarSesion.setForeground(COLOR_DORADO);
        btnCerrarSesion.setPreferredSize(new Dimension(190, 50));

        panelSuperiorDerecho.add(btnCerrarSesion);

        // 5. PANEL CENTRAL DINÁMICO
        contenidoCentralDinamico = new JPanel(new BorderLayout());
        contenidoCentralDinamico.setOpaque(false);
        contenidoCentralDinamico.setBorder(new EmptyBorder(10, 40, 30, 40));
        contenidoCentralDinamico.setOpaque(false);
        contenidoCentralDinamico.setBorder(new EmptyBorder(10, 40, 30, 40));

        panelBienvenida = new PanelBienvenida(nombreUsuario, rolUsuario);
        panelBienvenida.setMaximumSize(new Dimension(Short.MAX_VALUE, 400));
        panelBienvenida.setOpaque(false);

        restaurarVistaInicial();

        scrollContenido = new JScrollPane(contenidoCentralDinamico);
        scrollContenido.setOpaque(false);
        scrollContenido.getViewport().setOpaque(false);
        scrollContenido.setBorder(null);
        scrollContenido.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContenido.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollContenido.getVerticalScrollBar().setUnitIncrement(16);

        // 6. ENSAMBLAJE DE LA COLUMNA DERECHA
        panelDerechoCompleto.add(panelSuperiorDerecho, BorderLayout.NORTH);
        panelDerechoCompleto.add(scrollContenido, BorderLayout.CENTER);

        // 7. ENSAMBLAJE FINAL DE LA VENTANA
        panelContenedorFlotante.add(sidebar, BorderLayout.WEST);              // Sidebar abarca arriba-abajo en la izquierda
        panelContenedorFlotante.add(panelDerechoCompleto, BorderLayout.CENTER); // Resto del contenido a la derecha

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panelContenedorFlotante, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 600));
        setLocationRelativeTo(null);
    }

    /**
     * Limpia el contenedor central dinámico y reestablece el estado inicial del
     * Supervisor (Bienvenida y cierre de sesión).
     */
    public void restaurarVistaInicial() {

        contenidoCentralDinamico.removeAll();

        JPanel panelInicio = new JPanel();
        panelInicio.setOpaque(false);
        panelInicio.setLayout(new BoxLayout(panelInicio, BoxLayout.Y_AXIS));

        panelBienvenida.setAlignmentX(CENTER_ALIGNMENT);
        btnCerrarSesion.setAlignmentX(CENTER_ALIGNMENT);

        panelInicio.add(panelBienvenida);
        panelInicio.add(Box.createVerticalStrut(25));
        

        contenidoCentralDinamico.add(panelInicio, BorderLayout.CENTER);

        contenidoCentralDinamico.revalidate();
        contenidoCentralDinamico.repaint();
    }

    public JPanel getContenidoCentralDinamico() {
        return contenidoCentralDinamico;
    }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }

}
