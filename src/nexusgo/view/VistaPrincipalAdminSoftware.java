/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author USUARIO
 */
public class VistaPrincipalAdminSoftware extends JFrame{
    
    public VistaBarraLateral sidebar;
    
    // Este panel será el contenedor dinámico donde se meterán los módulos
    private JPanel contenido;

    public VistaPrincipalAdminSoftware() {
        super("Sistema NexusGO - Administrador de Software");
        setLayout(new BorderLayout());

        // 1. Inicializar y posicionar la barra lateral a la izquierda (WEST)
        sidebar = new VistaBarraLateral();
        
        // Configuramos la barra lateral para dejar solo los 2 botones que necesitas
        sidebar.bCasa.setText("Inicio");
        sidebar.bInventario.setText("Cambio Rol"); // Redirigido a la clase VistaCambioRol
        
        add(sidebar, BorderLayout.WEST);

        // Inicializar el panel 'contenido' explícitamente con BorderLayout
        contenido = new JPanel(new BorderLayout());

        // Agregar el contenedor general 'contenido' al centro del JFrame
        // El Controlador se encargará de inyectar los paneles dinámicamente aquí
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
