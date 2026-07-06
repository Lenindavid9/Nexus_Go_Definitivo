/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class VistaCambioRol extends JFrame{
    private Container contenedor;
    public DefaultTableModel modelo;
    //private FlowLayout miflow;
    private TitledBorder titulo;
    private JPanel datos;
    private JScrollPane miscroll;
    public JTable tabla;
    private GridLayout grid;
    
    public VistaCambioRol(){
        
        super("Cambio de rol");
        contenedor = getContentPane();
        contenedor.setLayout(new BorderLayout(20, 20));
        //miflow = new FlowLayout();
        //contenedor.setLayout(miflow);
        
        titulo = new TitledBorder("Administración de Usuarios");
        datos = new JPanel();
        datos.setBorder(titulo);
        datos.setBackground(Color.WHITE);
        grid = new GridLayout(6, 3, 2, 3);
        datos.setLayout(grid);
        
        modelo = new DefaultTableModel();
        modelo.addColumn("NUMERO DE IDENTIDAD");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("ROL");
        modelo.addColumn("CORREO ELECTRONICO");
        
        tabla = new JTable(modelo);
        miscroll = new JScrollPane(tabla);
        miscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        miscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        contenedor.add(datos);
    }
}