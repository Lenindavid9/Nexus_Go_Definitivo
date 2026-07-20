/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 *
 * @author INGRID
 */
public class VistaPdV extends JPanel {

    private JPanel titulo, principal, panelproductos, panelBusqueda, pnlbtnVolver;
    private JLabel logoyNombre, TituloPrincipal, estado, seccion;
    private JButton btnInicioTitulo, btnReportes, btnInicio, btnProductos, btnServicios, facturar, btnReiniciar, btnVolver,btnFacturar;
    private int contadorProductos = 0;
    private double totalVenta = 0.0;
    private final Color COLOR_DORADO = new Color(223, 205, 141);

    
    
    //          Este es el constructor de la vista
    
    public VistaPdV() {
        VistaNexus(); 
    }

    @Override
    protected void paintComponent(Graphics g
    ) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg");
        g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    
    
    public JPanel VistaNexus() {

        this.setLayout(new BorderLayout());
        principal = new JPanel();
        principal.setOpaque(false);
        //Este es para organizar todo hacia abajo, no podemos usar flowlayout ya que se pondria todo a un lado
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));

        //espaci de los bordes del panel principal
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        

        TituloPrincipal = new JLabel("Punto de Venta");
        TituloPrincipal.setFont(new Font("SansSerif", Font.BOLD, 32));
        TituloPrincipal.setAlignmentX(CENTER_ALIGNMENT);

        estado = new JLabel("Estado: Ventas Habilitadas");
        estado.setFont(new Font("SansSerif", Font.PLAIN, 14));
        estado.setAlignmentX(CENTER_ALIGNMENT);

        seccion = new JLabel("Productos / Servicios");
        seccion.setFont(new Font("SansSerif", Font.BOLD, 26));
        seccion.setAlignmentX(CENTER_ALIGNMENT);

        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBusqueda.setOpaque(false);

        facturar = new JButton("Facturar 0 Productos/Servicios");
        facturar.setFont(new Font("SansSerif", Font.BOLD, 15));
        facturar.setBackground(new Color(245, 238, 213));
        facturar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        facturar.setPreferredSize(new Dimension(250, 40));

        btnReiniciar = new JButton(new ImageIcon("eliminar.png"));
        btnReiniciar.setPreferredSize(new Dimension(40, 40));
        btnReiniciar.setContentAreaFilled(false);
        btnReiniciar.setBorderPainted(false);

        panelBusqueda.add(facturar);
        panelBusqueda.add(btnReiniciar);

        panelproductos = new JPanel(new GridLayout(0, 4, 20, 20));
        panelproductos.setOpaque(false);
        panelproductos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(panelproductos);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        principal.add(Box.createVerticalStrut(60));
        principal.add(TituloPrincipal);
        principal.add(Box.createVerticalStrut(5));
        principal.add(estado);
        principal.add(Box.createVerticalStrut(80));
        principal.add(seccion);
        principal.add(panelBusqueda);
        principal.add(Box.createVerticalStrut(80));
        principal.add(scroll);

        this.add(principal, BorderLayout.CENTER);

        return this;
    }

    private void BotonMenu(JButton boton) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 16));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setAlignmentX(LEFT_ALIGNMENT);
    }

    public void agregarTarjeta(String nombre, String precio, int stockActual, String imagenArchivo) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tarjeta.setPreferredSize(new Dimension(220, 320));

        JLabel lblImagen = new JLabel(new ImageIcon(imagenArchivo));
        lblImagen.setPreferredSize(new Dimension(200, 180));
        lblImagen.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(nombre);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 8, 2, 8));
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblPrecio = new JLabel(precio);
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPrecio.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        lblPrecio.setAlignmentX(LEFT_ALIGNMENT);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        acciones.setOpaque(false);
        acciones.setAlignmentX(LEFT_ALIGNMENT);

        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, stockActual, 1));
        spinnerCantidad.setPreferredSize(new Dimension(50, 25));

        JButton btnAgregar = new JButton(new ImageIcon("agg.png"));
        btnAgregar.setPreferredSize(new Dimension(25, 25));
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.setBorderPainted(false);

        btnAgregar.addActionListener(e -> {
            int cantidad = (int) spinnerCantidad.getValue();

            String precioLimpio = precio.replace("$", "").replace(",", "").trim();
            double precioUnitario = Double.parseDouble(precioLimpio);

            double subtotal = precioUnitario * cantidad;
            totalVenta += subtotal;

            contadorProductos += cantidad;
            facturar.setText("Facturar " + contadorProductos + " Productos/Servicios");

            btnAgregar.setEnabled(false);
        });

        acciones.add(btnAgregar);
        acciones.add(spinnerCantidad);

        tarjeta.add(lblImagen);
        tarjeta.add(lblTitulo);
        tarjeta.add(lblPrecio);
        tarjeta.add(acciones);

        panelproductos.add(tarjeta);
        panelproductos.revalidate();
        panelproductos.repaint();
    }

    public JButton getFacturarButton() {
        return facturar;
    }

    public JButton getReiniciarButton() {
        return btnReiniciar;
    }

    public int getContadorProductos() {
        return contadorProductos;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void reiniciarContador() {
        contadorProductos = 0;
        totalVenta = 0.0;
        facturar.setText("Facturar 0 Productos/Servicios");
    }
}
