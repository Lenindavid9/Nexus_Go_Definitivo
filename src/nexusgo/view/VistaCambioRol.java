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

    // Lista de roles
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
        modelo = new DefaultTableModel();
        modelo.addColumn("NUMERO DE IDENTIDAD");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("ROL");
        modelo.addColumn("CORREO ELECTRONICO");

        // Tabla
        tabla = new JTable(modelo);

        // ComboBox que permitirá cambiar el rol
        JComboBox<String> comboRol = new JComboBox<>(tipoRol);

        // Asignar ComboBox a la columna "ROL" (Índice 3)
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

        // Panel Central para la tabla
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelCentro.setOpaque(false); // Transparente para ver el fondo de mármol
        panelCentro.add(miscroll, BorderLayout.CENTER);

        // Agregar los componentes al JPanel actual
        this.add(panelCentro, BorderLayout.CENTER);
    }

    // Dibuja la imagen de fondo abarcando todo el panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}