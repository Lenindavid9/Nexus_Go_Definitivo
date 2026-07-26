/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author USUARIO
 */
public class VistaPrincipalAdminSoftware extends JFrame {

    public VistaBarraLateral sidebar;

    // Este panel será el contenedor dinámico donde se meterán los módulos
    private JPanel contenido;

    public VistaPrincipalAdminSoftware() {
        super("Sistema NexusGO - Administrador de Software");
        setLayout(new BorderLayout());
        
        JLabel fondoConImagen = new JLabel(new ImageIcon("src/nexusgo/img/fondoprincipal.jpg"));
        // Le damos un layout para poder añadir otros paneles encima
        fondoConImagen.setLayout(new BorderLayout());
        this.setContentPane(fondoConImagen);

        // 1. Inicializar y posicionar la barra lateral a la izquierda (WEST)
        sidebar = new VistaBarraLateral();

        // Configuramos la barra lateral para dejar solo los 2 botones que necesitas
        sidebar.bCasa.setText("");
        sidebar.bInventario.setText(""); // Redirigido a la clase VistaCambioRol
        sidebar.bCasa.setVisible(true);       // Inicio
        sidebar.bInventario.setVisible(true); // Ventas / Inventario
        sidebar.misCitas.setVisible(false);    // Gestión de Citas

        add(sidebar, BorderLayout.WEST);

        // Inicializar el panel 'contenido' explícitamente con BorderLayout
        contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.BLACK);

        add(contenido, BorderLayout.CENTER);

        // Dimensiones de la ventana optimizadas
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

}
