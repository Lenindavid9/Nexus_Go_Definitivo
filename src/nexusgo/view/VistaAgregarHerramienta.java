/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 *
 * @author USUARIO
 */
public class VistaAgregarHerramienta extends JPanel{
    
    // Componentes públicos para que el controlador tenga acceso directo
    public JTextField txtIdHerramienta;
    public JTextField txtNombre;
    public JLabel lblNombreImagen;
    public JButton btnImagen;
    public JButton btnEditar; // Hace la función de "Guardar" o "Editar" según el flujo
    public JButton btnVolver;
    
    // Componentes de adorno visual
    private JLabel lblTitulo;
    private JLabel lblIdHerramienta;
    private JLabel lblNombre;
    private JLabel lblFotoTexto;
    
    private final Color COLOR_DORADO = new Color(184, 134, 11);

    /**
     * Constructor de la vista. Configura el diseño visual del formulario.
     */
    public VistaAgregarHerramienta() {
        // Configuración básica del contenedor (Mantiene la línea estética de NexusGO)
        setOpaque(false); // Gris claro limpio
        setLayout(null); // Diseño absoluto para posicionamiento preciso de componentes

        // 1. TÍTULO DEL MÓDULO
        lblTitulo = new JLabel("REGISTRO DE HERRAMIENTAS Y ACTIVOS");
        lblTitulo.setFont(new Font("Poppins", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE); // Gris oscuro industrial
        lblTitulo.setBounds(500, 100, 500, 35);
        add(lblTitulo);

        // 2. CAMPO: ID / REFERENCIA DE LA HERRAMIENTA
        lblIdHerramienta = new JLabel("Número de Referencia (ID): *");
        lblIdHerramienta.setFont(new Font("Lato", Font.BOLD, 14));
        lblIdHerramienta.setForeground(Color.WHITE);
        lblIdHerramienta.setBounds(500, 170, 250, 20);
        add(lblIdHerramienta);

        txtIdHerramienta = new JTextField();
        txtIdHerramienta.setFont(new Font("Lato", Font.PLAIN, 14));
        txtIdHerramienta.setBorder(new LineBorder(new Color(203, 213, 225), 1));
        txtIdHerramienta.setBounds(500, 195, 320, 35);
        add(txtIdHerramienta);

        // 3. CAMPO: NOMBRE DE LA HERRAMIENTA
        lblNombre = new JLabel("Nombre de la Herramienta: *");
        lblNombre.setFont(new Font("Lato", Font.BOLD, 14));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setBounds(500, 245, 250, 20);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Lato", Font.PLAIN, 14));
        txtNombre.setBorder(new LineBorder(new Color(203, 213, 225), 1));
        txtNombre.setBounds(500, 280, 320, 35);
        add(txtNombre);

        // 4. MÓDULO DE ADICIÓN DE IMAGEN
        lblFotoTexto = new JLabel("Fotografía del Estado Físico:");
        lblFotoTexto.setFont(new Font("Lato", Font.BOLD, 14));
        lblFotoTexto.setForeground(Color.WHITE);
        lblFotoTexto.setBounds(500, 350, 250, 20);
        add(lblFotoTexto);

        btnImagen = new JButton("Subir Imagen");
        btnImagen.setFont(new Font("Poppins", Font.BOLD, 12));
        btnImagen.setBackground(COLOR_DORADO);
        btnImagen.setForeground(Color.WHITE);
        btnImagen.setFocusable(false);
        btnImagen.setBounds(500, 375, 140, 35);
        add(btnImagen);

        lblNombreImagen = new JLabel("ningún archivo seleccionado");
        lblNombreImagen.setFont(new Font("Lato", Font.ITALIC, 13));
        lblNombreImagen.setForeground(Color.white);
        lblNombreImagen.setBounds(650, 382, 250, 20);
        add(lblNombreImagen);

        // 5. BOTÓN: ACCIÓN DE GUARDAR / EDITAR
        btnEditar = new JButton("Guardar");
        btnEditar.setFont(new Font("Poppins", Font.BOLD, 14));
        btnEditar.setBackground(Color.WHITE);
        btnEditar.setForeground(COLOR_DORADO);
        btnEditar.setFocusable(false);
        btnEditar.setBounds(500, 450, 140, 40);
        add(btnEditar);

        // 6. BOTÓN: CANCELAR Y VOLVER
        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Poppins", Font.BOLD, 14));
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setForeground(COLOR_DORADO);
        btnVolver.setFocusable(false);
        btnVolver.setBounds(660, 450, 140, 40);
        add(btnVolver);
    }
    
}
