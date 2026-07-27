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
import nexusgo.view.VistaInicioSesion;

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

        // Listeners para la barra lateral
        this.vista.getsidebar().bCasa.addActionListener(this);
        this.vista.getsidebar().bInventario.addActionListener(this);
        this.vista.getBtnCerrarSesion().addActionListener(this);

        cargarInicioPorDefecto();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Clic en INICIO
        if (e.getSource() == vista.getsidebar().bCasa) {
            PanelBienvenida bienvenida = new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol());
            cambiarPanel(bienvenida);
        } 
        // Clic en CAMBIO DE ROL
        else if (e.getSource() == vista.getsidebar().bInventario) {
            // 1. Instanciar el JPanel de la vista
            VistaCambioRol panelCambioRol = new VistaCambioRol();
            
            // 2. ⚡ INSTANCIAR Y CONECTAR EL CONTROLADOR ⚡
            // Esto ejecuta la carga de usuarios desde la BD en la tabla
            ControladorCambioRol controladorRol = new ControladorCambioRol(panelCambioRol);
            
            // 3. Renderizar en el contenedor central
            cambiarPanel(panelCambioRol);
        }else if (e.getSource() == vista.getBtnCerrarSesion()) {
            ejecutarCerrarSesion();
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
    
    private void ejecutarCerrarSesion() {
        int confirmar = JOptionPane.showConfirmDialog(
            vista,
            "¿Desea cerrar sesión en NEXUS GO?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            vista.dispose();

            VistaInicioSesion loginVista = new VistaInicioSesion();
            new ControladorInicioSesion(loginVista);
            loginVista.setLocationRelativeTo(null);
            loginVista.setVisible(true);
        }
    }
}
