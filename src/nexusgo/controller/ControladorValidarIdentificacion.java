/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionListener;

/**
 *
 * @author USUARIO
 */
public class ControladorValidarIdentificacion implements ActionListener{ 
    
    private final ValidarIdentificacion vista;
    private final UsuarioDao usuarioDao;
    
    public ControladorValidarIdentificacionion(ValidarIdentificacion vista) {
        this.vista = vista;
        this.usuarioDao = new UsuarioDao();        
        // con esta se hace la escucha del botón de tu vista
        this.vista.confirmar.addActionListener(this);

}   
}
