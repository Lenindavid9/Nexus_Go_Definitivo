/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.view.VistaPrincipalAdminSoftware;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaCambioRol;
import nexusgo.model.Usuario;

/**
 *
 * @author USUARIO
 */
public class ControladorPrincipalAdminSoftware implements ActionListener {

    private final VistaPrincipalAdminSoftware vista;
    private final Usuario usuarioLogueado;

    public ControladorPrincipalAdminSoftware(VistaPrincipalAdminSoftware vista, JPanel panelAdmi, Usuario usuarioLogueado) {
        this.vista = vista;
        this.usuarioLogueado = usuarioLogueado;

        // 1. Escuchamos los clics de los dos botones de tu sidebar
        this.vista.getsidebar().bCasa.addActionListener(this);
        this.vista.getsidebar().bInventario.addActionListener(this);

        // 2. Cargamos el panel de bienvenida por defecto al centro de la ventana principal
        cargarInicioPorDefecto();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Clic en INICIO (Cargamos tu PanelBienvenida que es un JPanel en el centro)
        if (e.getSource() == vista.getsidebar().bCasa) {
            PanelBienvenida bienvenida = new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol());
            cambiarPanel(bienvenida);
        } 
        
        // Clic en CAMBIO ROL (Abrimos la ventana JFrame independiente)
        else if (e.getSource() == vista.getsidebar().bInventario) {
            try {
                // Instanciamos el JFrame
                VistaCambioRol ventanaCambioRol = new VistaCambioRol();
                
                // Centramos la ventana en la pantalla
                ventanaCambioRol.setLocationRelativeTo(null);
                
                // La hacemos visible
                ventanaCambioRol.setVisible(true);
                
                // OPCIONAL: Si quieres ocultar la ventana principal mientras está abierto el cambio de rol:
                 vista.setVisible(false);
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(vista, "Error al abrir la ventana de cambio de rol: " + ex.getMessage(), 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarInicioPorDefecto() {
        PanelBienvenida bienvenida = new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol());
        cambiarPanel(bienvenida);
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        JPanel contenedorCentral = vista.getContenedorCentral();
        contenedorCentral.removeAll();
        contenedorCentral.setLayout(new BorderLayout());
        contenedorCentral.add(nuevoPanel, BorderLayout.CENTER);
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }

}
