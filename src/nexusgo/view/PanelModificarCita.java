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
import javax.swing.JComboBox;
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
public class PanelModificarCita extends JPanel{
    // Declaro todos mis componentes a nivel de clase para que puedas acceder a ellos desde el controlador
    private JComboBox<String> comboServicios;
    private JTable tablaHorarios;
    private JButton btnCerrarSesion;
    private JButton btnGuardar;
    private JButton btnImagen;
    private JLabel lblNombreImagen;
    private JPanel tarjetaBlanca;

    public PanelModificarCita() {
        // Uso layout nulo para tener control absoluto de las posiciones mediante setBounds
        setLayout(null);
        // Hago mi panel transparente para que la textura de mármol del fondo principal sea visible
        setOpaque(false); 

        // Inicializo mi botón "cerrar sesion" con el degradado amarillo/naranja
        btnCerrarSesion = new JButton("cerrar sesion") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Activo el suavizado de bordes para que las esquinas redondeadas se vean nítidas
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new java.awt.GradientPaint(0, 0, new Color(255, 230, 100), getWidth(), 0, new Color(255, 180, 0)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        // Lo ubico en la esquina superior derecha, justo como en tus otras pantallas
        btnCerrarSesion.setBounds(680, 20, 140, 35);
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setFocusPainted(false);
        add(btnCerrarSesion);

        // Inicializo mi tarjeta blanca redondeada que contiene todo el formulario
        tarjetaBlanca = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Le doy un color blanco puro con un toque muy sutil de transparencia
                g2.setColor(new Color(255, 255, 255, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        tarjetaBlanca.setBounds(30, 70, 800, 470);
        tarjetaBlanca.setLayout(null);
        tarjetaBlanca.setOpaque(false);
        add(tarjetaBlanca);

        // Agrego el título "Modificar citas" en la parte superior izquierda de la tarjeta
        JLabel lblTitulo = new JLabel("Modificar varias citas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setBounds(40, 25, 250, 30);
        tarjetaBlanca.add(lblTitulo);

       
        // Creo la etiqueta indicadora para el selector de servicios
        JLabel lblServicio = new JLabel("Servicio a seleccionar para cambiar");
        lblServicio.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblServicio.setBounds(40, 75, 300, 15);
        tarjetaBlanca.add(lblServicio);

        // Inicializo el JComboBox para los servicios disponibles
        comboServicios = new JComboBox<>(new String[]{"Seleccione un servicio", "Alisado", "Corte", "Tinte", "Repolarización"});
        comboServicios.setBounds(40, 95, 200, 30);
        comboServicios.setBackground(Color.WHITE);
        tarjetaBlanca.add(comboServicios);

        // Creo la etiqueta para el bloque de espacio/precio
        

        // Configuro la cabecera de las columnas para mi cuadrícula semanal de horarios
        String[] columnas = {"Hora", "Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        // Agrego algunos datos de ejemplo con citas ocupadas para que la vista renderice los bloques de colores de inmediato
        Object[][] datos = {
            {"6:00 AM", "", "Corte", "", "", "Tinte", "Alisado", ""},
            {"7:00 AM", "", "", "", "Corte", "", "", ""},
            {"8:00 AM", "", "", "Alisado", "", "Tinte", "", ""},
            {"9:00 AM", "", "Tinte", "", "", "", "Corte", ""},
            {"10:00 AM", "", "Corte", "", "Tinte", "", "", ""},
            {"11:00 AM", "", "", "Alisado", "", "Alisado", "", ""}
        };

        // Defino mi modelo de tabla asegurándome de que las celdas no puedan ser editadas directamente escribiendo
        DefaultTableModel modeloTabla = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        // Instancio la JTable y la personalizo para que se asimile al diseño limpio del mockup
        tablaHorarios = new JTable(modeloTabla);
        tablaHorarios.setRowHeight(25);
        tablaHorarios.setShowGrid(true);
        // Le doy un color de rejilla sumamente claro para que no sature la vista
        tablaHorarios.setGridColor(new Color(230, 230, 230));
        tablaHorarios.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 10));
        tablaHorarios.getTableHeader().setBackground(Color.WHITE);
        
        // Creo un renderizador de celdas personalizado para pintar los bloques de citas con colores pastel
        DefaultTableCellRenderer renderizadorCeldas = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Centro el texto dentro de cada celda
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setFont(new Font("SansSerif", Font.PLAIN, 9));
                
                String val = (value != null) ? value.toString() : "";
                
                if (column == 0) {
                    // La columna 0 (las horas) siempre tendrá fondo blanco y texto gris oscuro
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.DARK_GRAY);
                } else if (!val.isEmpty()) {
                    // Si la celda contiene una cita agendada, le asigno su respectivo color pastel corporativo
                    c.setForeground(Color.WHITE);
                    switch (val) {
                        case "Alisado":
                            c.setBackground(new Color(41, 128, 185)); // Azul elegante
                            break;
                        case "Corte":
                            c.setBackground(new Color(155, 89, 182)); // Morado suave
                            break;
                        case "Tinte":
                            c.setBackground(new Color(192, 57, 43)); // Rojo opaco
                            break;
                        default:
                            c.setBackground(new Color(39, 174, 96)); // Verde esmeralda por defecto
                            break;
                    }
                } else {
                    // Si la celda está vacía, mantengo el color de fondo blanco
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                
                // Si el peluquero hace clic en una celda que no sea de la hora, la pinto de amarillo para resaltar su selección
                if (isSelected && column != 0) {
                    c.setBackground(new Color(241, 196, 15)); 
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
        };
        
        // Asocio mi renderizador estético a cada una de las columnas de la tabla
        for (int i = 0; i < tablaHorarios.getColumnCount(); i++) {
            tablaHorarios.getColumnModel().getColumn(i).setCellRenderer(renderizadorCeldas);
        }

        // Meto mi tabla en un JScrollPane y lo posiciono justo debajo de los campos de texto
        JScrollPane scrollTabla = new JScrollPane(tablaHorarios);
        scrollTabla.setBounds(40, 200, 720, 180);
        tarjetaBlanca.add(scrollTabla);

        // Inicializo el botón "Imagen producto/servicio" con bordes amarillos redondeados y fondo blanco
        btnImagen = new JButton("Imagen producto/servicio") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                // Le dibujo un borde fino de color naranja/amarillo
                g2.setColor(new Color(230, 160, 0));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnImagen.setBounds(40, 395, 200, 30);
        btnImagen.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnImagen.setForeground(new Color(230, 160, 0));
        btnImagen.setContentAreaFilled(false);
        btnImagen.setBorderPainted(false);
        btnImagen.setFocusPainted(false);
        tarjetaBlanca.add(btnImagen);

        // Agrego una etiqueta al lado de mi botón de imagen para indicar el archivo cargado por defecto
        lblNombreImagen = new JLabel("producto/servicio.png");
        lblNombreImagen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblNombreImagen.setForeground(Color.GRAY);
        lblNombreImagen.setBounds(255, 402, 200, 20);
        tarjetaBlanca.add(lblNombreImagen);

        // Diseño el botón "Guardar" en la esquina inferior izquierda con nuestro degradado corporativo
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
        btnGuardar.setBounds(40, 430, 200, 32);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        tarjetaBlanca.add(btnGuardar);
    }

    // --- Mis métodos getters para que manipules la información desde el Controlador ---
    public JComboBox<String> getComboServicios() { return comboServicios; }
    public JTable getTablaHorarios() { return tablaHorarios; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnImagen() { return btnImagen; }
    public JLabel getLblNombreImagen() { return lblNombreImagen; }
}
