package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VistaCambioRol extends JPanel {

    public DefaultTableModel modelo;
    private JPanel datos;
    private JScrollPane miscroll;
    public JTable tabla;
    public JButton btnCerrarSesion;
    public JComboBox<String> unTipoRol;
    private Image imagenFondo;

    // Lista de roles disponibles
    private String tipoRol[] = {"Cliente", "Peluquero", "Admin. Software", "Admin. Peluqueria", "Supervisor", "Operario"};

    public VistaCambioRol() {
        // Cargar imagen de fondo
        ImageIcon icon = new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg");
        this.imagenFondo = icon.getImage();

        // Configuración del layout del JPanel principal
        this.setLayout(new BorderLayout(20, 20));

        datos = new JPanel();
        datos.setBackground(Color.WHITE);
        datos.setLayout(new BorderLayout(10, 10));

        // Modelo de la tabla
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna ROL (índice 3) es editable
                return column == 3;
            }
        };
        modelo.addColumn("NUMERO DE IDENTIDAD");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("ROL");
        modelo.addColumn("CORREO ELECTRONICO");

        // Tabla
        tabla = new JTable(modelo);

        // ComboBox para cambiar el rol en la columna 3
        JComboBox<String> comboRol = new JComboBox<>(tipoRol);
        tabla.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboRol));

        tabla.setRowHeight(45);
        tabla.setSelectionBackground(Color.decode("#EFB810"));

        // Personalización del encabezado
        tabla.getTableHeader().setBackground(Color.WHITE);
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        // ScrollPane
        miscroll = new JScrollPane(tabla);
        miscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Panel Central transparente para ver el fondo
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelCentro.setOpaque(false);
        panelCentro.add(miscroll, BorderLayout.CENTER);

        this.add(panelCentro, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public JTable getTabla() {
        return tabla;
    }
}