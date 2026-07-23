/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;
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
import nexusgo.model.DetalleCarrito;

/**
 *
 * @author INGRID
 */
public class VistaPdV extends JPanel {

    private JPanel principal, panelproductos, panelBusqueda;
    private JLabel TituloPrincipal, estado, seccion;
    private JButton facturar, btnReiniciar;

    public VistaPdV() {
        setLayout(new BorderLayout()); //Aqui definimosel layout del panel principal
        setOpaque(false);// setOpaque es para que se elimine el fondo que viene por dfecto y se vea la imagen
        VistaNexus(); //Aqui se llama al metodo que construye toooda la interfaz
    }

    public JPanel VistaNexus() {
        this.setLayout(new BorderLayout());  //aqui volvemos a "re afirmar" el layout principal
        
        //Panel principal donde se va almacenar toda la interfaz
        principal = new JPanel();
        // setOpaque es para que no se va el fondo predeterminado y asi que se pueda ver la imagen asignada en 
        //la vista principal operario
        principal.setOpaque(false);
        
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS)); // Con esto se organizan todos los componentes en la columna
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); //Aqui se editan los margenes internos

        //Siguen todos los titulos y subtitulos
        TituloPrincipal = new JLabel("Punto de Venta");
        //Aqui se cambia el color del texto con Foreground
        TituloPrincipal.setForeground(Color.WHITE);
        //
        TituloPrincipal.setFont(new Font("SansSerif", Font.BOLD, 35));
        TituloPrincipal.setAlignmentX(CENTER_ALIGNMENT);

        estado = new JLabel("Estado: Ventas Habilitadas");
        estado.setForeground(Color.WHITE);
        estado.setFont(new Font("SansSerif", Font.PLAIN, 14));
        estado.setAlignmentX(CENTER_ALIGNMENT);

        seccion = new JLabel("Productos / Servicios");
        seccion.setForeground(Color.WHITE);
        seccion.setFont(new Font("SansSerif", Font.BOLD, 26));
        seccion.setAlignmentX(CENTER_ALIGNMENT);

        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBusqueda.setOpaque(false);

        facturar = new JButton("Facturar 0 Productos/Servicios");
        facturar.setFont(new Font("SansSerif", Font.BOLD, 15));
        facturar.setBackground(new Color(245, 238, 213));
        facturar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        facturar.setPreferredSize(new Dimension(280, 40));
        facturar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnReiniciar = new JButton(new ImageIcon("eliminar.png"));
        btnReiniciar.setPreferredSize(new Dimension(40, 40));
        btnReiniciar.setContentAreaFilled(false);
        btnReiniciar.setBorderPainted(false);
        btnReiniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    // Método que crea y devuelve la tarjeta junto con sus componentes accesibles
    public TarjetaProductoComponentes agregarTarjetaComponentes(String nombre, String precio, int stockActual, String imagenArchivo) {
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

        // Panel de acciones (Spinner + Botón Suma)
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        acciones.setOpaque(false);
        acciones.setAlignmentX(LEFT_ALIGNMENT);

        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, Math.max(1, stockActual), 1));
        spinnerCantidad.setPreferredSize(new Dimension(50, 30));

        // Botón con el símbolo '+' para agregar al carrito
        JButton btnAgregar = new JButton("+");
        btnAgregar.setPreferredSize(new Dimension(40, 30));
        btnAgregar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnAgregar.setBackground(new Color(245, 238, 213)); // Mismo estilo cálido del sistema
        btnAgregar.setForeground(Color.DARK_GRAY);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.setToolTipText("Agregar al carrito");

        // Primero va el Spinner y luego el botón de suma (+)
        acciones.add(spinnerCantidad);
        acciones.add(btnAgregar);

        tarjeta.add(lblImagen);
        tarjeta.add(lblTitulo);
        tarjeta.add(lblPrecio);
        tarjeta.add(acciones);

        panelproductos.add(tarjeta);
        panelproductos.revalidate();
        panelproductos.repaint();

        return new TarjetaProductoComponentes(btnAgregar, spinnerCantidad);
    }

    public JButton getFacturarButton() {
        return facturar;
    }

    public JButton getReiniciarButton() {
        return btnReiniciar;
    }

    public void actualizarTextoFacturar(int contador) {
        facturar.setText("Facturar " + contador + " Productos/Servicios");
    }

    // Clase interna auxiliar para retornar referencias de botones y spinners al controlador
    public static class TarjetaProductoComponentes {

        private final JButton btnAgregar;
        private final JSpinner spinner;

        public TarjetaProductoComponentes(JButton btnAgregar, JSpinner spinner) {
            this.btnAgregar = btnAgregar;
            this.spinner = spinner;
        }

        public JButton getBtnAgregar() {
            return btnAgregar;
        }
        public JSpinner getSpinner() {
            return spinner;
        }
    }

    public void agregarTarjeta(String nombre, String precio, int stockActual, String imagenArchivo) {
        agregarTarjetaComponentes(nombre, precio, stockActual, imagenArchivo);
    }
}