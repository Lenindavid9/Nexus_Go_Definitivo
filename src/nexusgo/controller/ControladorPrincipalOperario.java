/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.view.VistaPdV;
import nexusgo.view.VistaPrincipalOperario;

/**
 *
 * @author USUARIO
 */
public class ControladorPrincipalOperario implements ActionListener {

    private VistaPrincipalOperario vista;

    public ControladorPrincipalOperario(VistaPrincipalOperario vista) {

        this.vista = vista;
        this.vista.getsidebar().bInventario.addActionListener(this);
        this.vista.getsidebar().bCasa.addActionListener(this);
        this.vista.getsidebar().misCitas.addActionListener(this);

    }

  

    public void abrirInventario() {
        //aqui crea el inventario
        VistaOperarioInventario inventario = new VistaOperarioInventario();

        inventario.setVisible(true);
        //Cierra y guarda la variable vista
        vista.dispose();

    }
    
     public void abrirPuntodeVenta() {
        //aqui crea el inventario
        VistaPdV VistaNexus = new VistaPdV();

        VistaNexus.setVisible(true);
        //Cierra y guarda la variable vista
        vista.dispose();

    }

    //funcionamiento del boton de inventario
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getsidebar().bInventario) {
            abrirInventario();
        }
        
        if (e.getSource() == vista.getsidebar().misCitas) {
            abrirPuntodeVenta();
        }
      
        
    }

}
