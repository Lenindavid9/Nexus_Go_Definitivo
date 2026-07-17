package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import nexusgo.model.Usuario;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.view.VistaPdV;
import nexusgo.view.VistaPrincipalOperario;

public class ControladorPrincipalOperario implements ActionListener {
    
    private final VistaPrincipalOperario vistaMenu;
    private final JPanel contenedorCentral;
    private final Usuario usuarioLogueado;

    public ControladorPrincipalOperario(VistaPrincipalOperario vistaMenu, Usuario usuarioLogueado) {
        this.vistaMenu = vistaMenu;
        this.usuarioLogueado = usuarioLogueado;
        
        // Obtenemos la referencia limpia al panel del centro
        this.contenedorCentral = vistaMenu.getContenedorCentral();

        // Inicializamos la barra lateral
        this.vistaMenu.getsidebar().bCasa.addActionListener(this);       
        this.vistaMenu.getsidebar().bInventario.addActionListener(this); 
        this.vistaMenu.getsidebar().misCitas.addActionListener(this);    

        // Cargamos la pantalla de bienvenida por defecto
        cambiarPanel(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
    }

    // Método auxiliar limpio para reemplazar el contenido del centro
    private void cambiarPanel(JPanel nuevoPanel) {
        contenedorCentral.removeAll();
        contenedorCentral.setLayout(new BorderLayout());
        contenedorCentral.add(nuevoPanel, BorderLayout.CENTER);
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == vistaMenu.getsidebar().bCasa) {
            cambiarPanel(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
        }
        else if (e.getSource() == vistaMenu.getsidebar().bInventario) {
            VistaPdV panelPdV = new VistaPdV();
            panelPdV.VistaNexus();
            new ControladorPdV(panelPdV);
            cambiarPanel(panelPdV);
        }
        else if (e.getSource() == vistaMenu.getsidebar().misCitas) {
            VistaOperarioInventario panelInventario = new VistaOperarioInventario();
            
            // ¡Aquí está el truco! Le pasamos el "contenedorCentral" al controlador del inventario
            ControladorInventarioOperario controlador = new ControladorInventarioOperario(panelInventario, usuarioLogueado, contenedorCentral);
            
            cambiarPanel(panelInventario);
        }
    }

  
}
