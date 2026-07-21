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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HOME
 */
public class PanelInventarioProductosPeluquero extends JPanel {
    // Declaro todos mis elementos a nivel de clase para que los manipules fácilmente desde el controlador
    private JTextField txtBuscar;
    private JTable tablaInventario;
    private JButton btnCerrarSesion;
    private JButton btnVolver;
    private JScrollPane scrollTabla;
    private JPanel tarjetaBlanca;

    public PanelInventarioProductosPeluquero() {
        // Establezco el layout nulo para tener un control milimétrico de la posición de cada componente
        setLayout(null);
        // Hago transparente este panel principal para que se vea tu textura de fondo de mármol
        

        // Inicializo mi barra de búsqueda redondeada de la parte superior
        txtBuscar = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Le pinto un fondo blanco limpio
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        // La coloco de forma idéntica a tu mockup, centrada en la parte superior
        txtBuscar.setBounds(255, 20, 400, 35);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtBuscar.setForeground(Color.GRAY);
        // Coloco el texto de búsqueda simulado "Search"
        txtBuscar.setText("  Q Search");
        // Elimino los bordes por defecto y le doy un margen interno para que la escritura empiece de manera elegante
        txtBuscar.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtBuscar.setOpaque(false);
        add(txtBuscar);

        // Instancio mi botón de cerrar sesión arriba a la derecha con el degradado corporativo
        btnCerrarSesion = new JButton("cerrar sesion") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

        // Inicializo mi tarjeta blanca de esquinas redondeadas que contendrá la tabla
        tarjetaBlanca = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Color blanco suave con una leve transparencia para integrarse con el fondo
                g2.setColor(new Color(255, 255, 255, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        tarjetaBlanca.setBounds(30, 70, 800, 470);
        tarjetaBlanca.setLayout(null);
        tarjetaBlanca.setOpaque(false);
        add(tarjetaBlanca);

        
        // Agrego el botón "< Volver" dentro de la tarjeta, alineado arriba a la derecha
        btnVolver = new JButton("< Volver");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnVolver.setForeground(new Color(100, 100, 100));
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setBounds(680, 15, 90, 25);
        tarjetaBlanca.add(btnVolver);

        // Instancio mi JTable para los productos del peluquero. Nace vacía para que la llenes desde la BD
        tablaInventario = new JTable();
        // Le doy una buena altura a cada fila para que los textos respiren bien
        tablaInventario.setRowHeight(42);
        // Oculto el grid por defecto para pintar yo mismo las líneas personalizadas
        tablaInventario.setShowGrid(false);
        tablaInventario.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaInventario.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tablaInventario.getTableHeader().setBackground(Color.WHITE);

        // Ubico el JScrollPane que soportará mi tabla
        scrollTabla = new JScrollPane(tablaInventario);
        scrollTabla.setBounds(40, 50, 720, 390);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        // Elimino los bordes molestos del scrollbar para que quede impecable
        scrollTabla.setBorder(null);
        tarjetaBlanca.add(scrollTabla);
    }

    /**
     * Con este método yo te permito inyectarle directamente el DefaultTableModel 
     * con los productos traídos desde tu base de datos de MySQL.
     */
    public void setDatosInventario(DefaultTableModel modeloBD) {
        // Enlazo el modelo dinámico que me envías desde el controlador a mi tabla
        tablaInventario.setModel(modeloBD);

        // Defino mi renderizador personalizado para crear las líneas horizontales amarillas divisorias
        DefaultTableCellRenderer renderizadorInventario = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Centro horizontalmente todos los contenidos
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setBackground(Color.WHITE);
                cell.setForeground(new Color(50, 50, 50));

                // Le pinto una línea delgada de color amarillo/dorado en la parte inferior a cada celda
                cell.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(245, 215, 120)));

                // Si seleccionas la fila, le doy un fondo gris sumamente sutil para indicar selección
                if (isSelected) {
                    cell.setBackground(new Color(250, 250, 250));
                }
                return cell;
            }
        };

        // Aplico este diseño de celdas a todas las columnas que el controlador haya inyectado
        for (int i = 0; i < tablaInventario.getColumnCount(); i++) {
            tablaInventario.getColumnModel().getColumn(i).setCellRenderer(renderizadorInventario);
        }

        // Obligo a redibujar el panel para que los productos aparezcan en pantalla de una vez
        revalidate();
        repaint();
    }

    // --- Mis métodos getters para que asocies los listeners en tu Controlador ---
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JTable getTablaInventario() { return tablaInventario; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnVolver() { return btnVolver; }
}
