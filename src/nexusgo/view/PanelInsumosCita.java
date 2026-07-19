/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HOME
 */

public class PanelInsumosCita extends JPanel {
   // Componentes de la interfaz
    private JTable tablaProductos;
    private JButton btnCerrarSesion;
    private JButton btnGuardar;
    private JScrollPane scrollTabla;
    private JPanel tarjetaBlanca;

    public PanelInsumosCita() {
        // Uso un layout nulo para encajar cada elemento con coordenadas exactas como en tus mockups
        setLayout(null);
        
        // Desactivo la opacidad para que se alcance a ver de fondo tu textura de mármol
        setOpaque(false); 

        // Inicializo mi botón de "cerrar sesion" con el degradado amarillo/naranja corporativo
        btnCerrarSesion = new JButton("cerrar sesion") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Activo el suavizado de bordes para que las esquinas se vean perfectas
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Aplico mi degradado característico de izquierda a derecha
                g2.setPaint(new java.awt.GradientPaint(0, 0, new Color(255, 230, 100), getWidth(), 0, new Color(255, 180, 0)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCerrarSesion.setBounds(680, 20, 140, 35);
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setFocusPainted(false);
        add(btnCerrarSesion);

        // Instancio mi contenedor blanco redondeado que sirve como marco principal
        tarjetaBlanca = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Le doy el color blanco suave ligeramente traslúcido de NexusGO
                g2.setColor(new Color(255, 255, 255, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        tarjetaBlanca.setBounds(30, 70, 800, 470);
        tarjetaBlanca.setLayout(null);
        tarjetaBlanca.setOpaque(false);
        add(tarjetaBlanca);

        // Coloco el título principal de la gestión de herramientas
        JLabel lblTitulo = new JLabel("Gestión de herramientas y productos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setBounds(40, 20, 720, 35);
        tarjetaBlanca.add(lblTitulo);

        // Inicializo mi JTable. En este punto la creo vacía porque la base de datos la llenará después
        tablaProductos = new JTable();
        tablaProductos.setRowHeight(38);
        tablaProductos.setShowGrid(false); // Mantengo el diseño plano y moderno sin molestas líneas de rejilla
        tablaProductos.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tablaProductos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tablaProductos.getTableHeader().setBackground(Color.WHITE);

        // Configuro el JScrollPane donde vivirá mi tabla
        scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBounds(40, 75, 720, 310);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        tarjetaBlanca.add(scrollTabla);

        // Inicializo el botón "Guardar" en la parte inferior de la tarjeta
        btnGuardar = new JButton("Guardar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new java.awt.GradientPaint(0, 0, new Color(255, 230, 100), getWidth(), 0, new Color(255, 180, 0)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnGuardar.setBounds(300, 405, 200, 35);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        tarjetaBlanca.add(btnGuardar);
    }

    /**
     * Este método lo creé especialmente para ti. Lo llamarás desde tu controlador.
     * Te permite inyectar directamente el modelo con los datos recién traídos de la base de datos.
     */
    public void setDatosTabla(DefaultTableModel modeloBD) {
        // Asocio el modelo que trajiste de tu base de datos a mi tabla
        tablaProductos.setModel(modeloBD);

        // Vuelvo a aplicar el renderizador personalizado para centrar el texto de las columnas
        DefaultTableCellRenderer renderizadorCentrado = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setBackground(Color.WHITE);
                c.setForeground(new Color(50, 50, 50));
                
                // Si el peluquero hace clic en la fila, le doy un toque sutil de selección gris suave
                if (isSelected) {
                    c.setBackground(new Color(245, 245, 245));
                }
                return c;
            }
        };

        // Aplico este renderizador estético en todas las columnas excepto la última (que suele ser el Checkbox o acción)
        if (tablaProductos.getColumnCount() > 0) {
            for (int i = 0; i < tablaProductos.getColumnCount() - 1; i++) {
                tablaProductos.getColumnModel().getColumn(i).setCellRenderer(renderizadorCentrado);
            }
        }

        // Refresco el panel para asegurar que los datos se muestren al instante en la pantalla
        revalidate();
        repaint();
    }

    // --- Mis métodos getters para que interactúes desde tu Controlador ---
    public JTable getTablaProductos() { return tablaProductos; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnGuardar() { return btnGuardar; }
}
