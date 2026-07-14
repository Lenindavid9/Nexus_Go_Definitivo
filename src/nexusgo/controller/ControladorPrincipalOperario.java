
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.view.VistaPdV;
import nexusgo.view.VistaPrincipalOperario;

public class ControladorPrincipalOperario implements ActionListener {

   private VistaPrincipalOperario vista;
    private VistaOperarioInventario panelInventario;
    private VistaPdV panelPdV;

    public ControladorPrincipalOperario(VistaPrincipalOperario vista) {
        this.vista = vista;

        // 1. Inicializamos los paneles que se van a intercambiar
        this.panelInventario = new VistaOperarioInventario();
        this.panelPdV = new VistaPdV();
        this.panelPdV.VistaNexus(); // Construye los botones e interfaz del PdV

        // 2. Enlazamos los listeners de la barra lateral
        this.vista.getsidebar().bInventario.addActionListener(this); // Botón Ventas
        this.vista.getsidebar().bCasa.addActionListener(this);       // Botón Inicio
        this.vista.getsidebar().misCitas.addActionListener(this);    // Botón Inventario

        // 3. Vista inicial por defecto (Panel de Bienvenida)
        // Nota: Si no tienes los datos del usuario aquí, puedes pasar textos fijos temporalmente
        cambiarPanelCentral(new PanelBienvenida("Operario", "Operario"));
    }
    // Método genérico para intercambiar los paneles en el centro sin romper la ventana
    private void cambiarPanelCentral(JPanel panelNuevo) {
        vista.getContenido().removeAll(); // Limpia lo que esté en el centro
        vista.getContenido().add(panelNuevo, BorderLayout.CENTER); // Inserta el nuevo panel
        vista.revalidate(); // Re-calcula el layout
        vista.repaint();    // Redibuja la interfaz
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // CLIC EN VENTAS (bInventario) -> Nos lleva al Punto de Venta
        if (e.getSource() == vista.getsidebar().bInventario) {
            // NOTA: Si usas este controlador, aquí deberías llamar a un método 
            // que cargue las tarjetas desde tu base de datos si es necesario.
            cambiarPanelCentral(this.panelPdV);
        }

        // CLIC EN INVENTARIO (misCitas) -> Nos lleva a las tablas de stock
        if (e.getSource() == vista.getsidebar().misCitas) {
            cambiarPanelCentral(this.panelInventario);
        }

        // CLIC EN CASA (bCasa) -> Regresa a la bienvenida
        if (e.getSource() == vista.getsidebar().bCasa) {
            cambiarPanelCentral(new PanelBienvenida("Operario", "Operario"));
        }
        
        if (e.getSource() == vistaOperario.getsidebar().btnPuntoVenta) { // Ajusta al botón real de tu sidebar

        // 1. Instanciamos la vista del Punto de Venta
        VistaPdV panelPdV = new VistaPdV();

        // 2. Ejecutamos tu método que arma el diseño (VistaNexus)
        javax.swing.JPanel graficoPdV = panelPdV.VistaNexus(); 

        // 3. Le pasamos la vista a TU controlador existente para activar "Facturar" y "Reiniciar"
        new ControladorPdV(panelPdV); 

        // 4. Limpiamos el contenedor dinámico del operario e inyectamos el Punto de Venta
        javax.swing.JPanel contenedorCentral = vistaOperario.getContenido();
        contenedorCentral.removeAll();
        contenedorCentral.add(graficoPdV, java.awt.BorderLayout.CENTER);

        // 5. Refrescamos la interfaz gráfica
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }
}
    }

