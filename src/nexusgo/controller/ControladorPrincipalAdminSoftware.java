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

        // Listeners para la barra lateral
        this.vista.getsidebar().bCasa.addActionListener(this);
        this.vista.getsidebar().bInventario.addActionListener(this);

        cargarInicioPorDefecto();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Clic en INICIO
        if (e.getSource() == vista.getsidebar().bCasa) {
            PanelBienvenida bienvenida = new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol());
            cambiarPanel(bienvenida);
        } 
        // Clic en CAMBIO DE ROL (Inyecta el JPanel en el centro)
        else if (e.getSource() == vista.getsidebar().bInventario) {
            VistaCambioRol panelCambioRol = new VistaCambioRol();
            cambiarPanel(panelCambioRol); // ✅ Enrutamiento correcto para JPanel
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
