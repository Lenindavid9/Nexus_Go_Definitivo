/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
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
    public FlowLayout miflow;
    public JLabel titulo, mensaje;
    public JButton breporte, barranque;
    public PanelBienvenida bienvenida;

    // Este panel será el contenedor dinámico donde se meterán los módulos
    private JPanel contenido;

    public VistaPrincipalOperario() {
        super("Sistema NexusGO - Panel de Gestión");
        setLayout(new BorderLayout());

        // 1. Inicializar y posicionar la barra lateral a la izquierda (WEST)
        sidebar = new VistaBarraLateral();
        add(sidebar, BorderLayout.WEST);
        

        // 2. Inicializar el panel 'contenido' explícitamente con BorderLayout.
        contenido = new JPanel(new BorderLayout());

        // 3. Agregar el contenedor general 'contenido' al centro del JFrame
        // NOTA: Ya no metemos aquí la bienvenida de forma estática ni vacía.
        // El ControladorInventarioOperario se encargará de inyectar el panel con los datos de la BD.
        add(contenido, BorderLayout.CENTER);

        // Dimensiones de la ventana optimizadas para que quepan bien las tablas de inventario
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
