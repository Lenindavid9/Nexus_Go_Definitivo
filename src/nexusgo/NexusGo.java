/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nexusgo;

import javax.swing.JFrame;
import nexusgo.controller.ControladorInicioSesion;
import nexusgo.controller.ControladorInventarioOperario;
import nexusgo.controller.ControladorInventarioSupervisor;
import nexusgo.controller.ControladorMdP;
import nexusgo.controller.ControladorPrincipalCliente;
import nexusgo.controller.ControladorPrincipalOperario;
import nexusgo.model.Conexion;
import nexusgo.model.Usuario;

//import nexusgo.view.PanelAdmi;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaBarraLateral;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaPrincipalOperario;
import nexusgo.view.VistaPrincipalSupervisor;
import nexusgo.view.MetododePago;
/**
 *
 * @author USUARIO
 */
public class NexusGo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        ////        // 1. Se crea la Vista
        VistaInicioSesion login = new VistaInicioSesion();
     // 2. Se crea el Controlador y se le vincula la vista
        ControladorInicioSesion controlador = new ControladorInicioSesion(login);

       // 3. Se hace visible el Login en el centro de la pantalla
        login.setSize(450, 450); // Ajusta las dimensiones según tu fondito.jpg
        login.setLocationRelativeTo(null);
        login.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        login.setVisible(true);
        
        JFrame ventana = new JFrame("Probando Panel de Administración");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 600); // Ajusta tamaño según tu diseño
        ventana.setLocationRelativeTo(null);

        MetododePago ventanaAdmin = new MetododePago();
        ControladorMdP controladorMdP = new ControladorMdP(ventanaAdmin);
        ventana.add(ventanaAdmin); // 👉 Se agrega el JPanel al JFrame

        ventana.setVisible(true);
    }
    
}