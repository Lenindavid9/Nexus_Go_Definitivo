/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nexusgo;

import javax.swing.JFrame;
import nexusgo.controller.ControladorInicioSesion;
import nexusgo.view.VistaInicioSesion;

/**
 *
 * @author USUARIO
 */
public class NexusGo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        VistaInicioSesion login = new VistaInicioSesion();

        /*Se crea el controlador encargado de administrar el funcionamiento
        de la ventana de inicio de sesión.*/
        ControladorInicioSesion controlador = new ControladorInicioSesion(login);

        //Se establece el tamaño inicial que tendrá la ventana.
        login.setSize(450, 450);

        /*Se posiciona la ventana en el centro de la pantalla del usuario.
        Al utilizar null, se calcula automáticamente
        la ubicación para centrar la ventana.*/
        login.setLocationRelativeTo(null);

        /*Se define el comportamiento de la aplicación cuando el usuario
        presiona el botón de cerrar (X) de la ventana.
        EXIT_ON_CLOSE cerrará completamente la aplicación y finalizará la continuacion del sistema.*/
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Finalmente, se hace visible la ventana de inicio de sesión.
        login.setVisible(true);
    }
    
}