/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nexusgo;

import javax.swing.JFrame;
import nexusgo.controller.ControladorInicioSesion;
import nexusgo.controller.ControladorInventarioOperario;
import nexusgo.controller.ControladorPrincipalCliente;
import nexusgo.controller.ControladorPrincipalOperario;
import nexusgo.model.Conexion;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.VistaBarraLateral;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaPrincipalOperario;

/**
 *
 * @author USUARIO
 */
public class NexusGo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Se crea la Vista
        VistaInicioSesion login = new VistaInicioSesion();
        
        // Se crea el Controlador y se le vincula la vista
        ControladorInicioSesion controlador = new ControladorInicioSesion(login);
        
        // Se hace visible el Login en el centro de la pantalla
        login.setSize(450, 450); // Ajusta las dimensiones según fondito.jpg
        login.setLocationRelativeTo(null);
        login.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        login.setVisible(true);
    }
}
