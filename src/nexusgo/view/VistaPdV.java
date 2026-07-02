package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
 * @author INGRID
 */
public class VistaPdV extends JPanel {

    private JPanel titulo, menu, principal, panelproductos, panelBusqueda;
    private JLabel logoyNombre, TituloPrincipal, estado, seccion;
    private JButton btnInicioTitulo, btnReportes, btnInicio, btnProductos, btnServicios, facturar, btnReiniciar;
    private int contadorProductos = 0;
    private final Color COLOR_CAFE_OSCURO = new Color(62, 58, 46);
    private final Color COLOR_DORADO = new Color(223, 205, 141);

    public JPanel VistaNexus() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);

        titulo = new JPanel(new BorderLayout());
        titulo.setBackground(COLOR_CAFE_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Icon iconLogo = new ImageIcon("logo.png");
        logoyNombre = new JLabel("Punto de Venta - N E X U S", iconLogo, SwingConstants.LEFT);
        logoyNombre.setForeground(Color.WHITE);
        logoyNombre.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.add(logoyNombre, BorderLayout.WEST);

        JPanel OpcTitulo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        OpcTitulo.setOpaque(false);

        btnInicioTitulo = new JButton("Inicio");
        btnInicioTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnInicioTitulo.setForeground(Color.WHITE);
        btnInicioTitulo.setContentAreaFilled(false);
        btnInicioTitulo.setBorderPainted(false);

        btnReportes = new JButton("Reportes");
        btnReportes.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnReportes.setForeground(Color.WHITE);
        btnReportes.setContentAreaFilled(false);
        btnReportes.setBorderPainted(false);

        OpcTitulo.add(btnInicioTitulo);
        OpcTitulo.add(btnReportes);
        titulo.add(OpcTitulo, BorderLayout.EAST);

        menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(COLOR_DORADO);
        menu.setPreferredSize(new Dimension(200, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(30, 15, 10, 15));

        btnInicio = new JButton("Inicio");
        btnProductos = new JButton("Productos");
        btnServicios = new JButton("Servicios");
        BotonMenu(btnInicio);
        BotonMenu(btnProductos);
        BotonMenu(btnServicios);

        menu.add(btnInicio);
        menu.add(Box.createVerticalStrut(20));
        menu.add(btnProductos);
        menu.add(Box.createVerticalStrut(20));
        menu.add(btnServicios);

        principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(Color.white);
        principal.setBorder(BorderFactory.createEmptyBorder(0, 40, 50, 40));

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

        this.add(titulo, BorderLayout.NORTH);
        this.add(menu, BorderLayout.WEST);
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
            contadorProductos += 1; 
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

    public void reiniciarContador() {
        contadorProductos = 0;
        facturar.setText("Facturar 0 Productos/Servicios");
    }
}