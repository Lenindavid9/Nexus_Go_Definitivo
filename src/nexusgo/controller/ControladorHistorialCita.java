/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaHistorialCita;
import nexusgo.view.VistaPrincipalCliente;
/**
 *
 * @author HOME
 */
public class ControladorHistorialCita implements ActionListener {
    
    private VistaHistorialCita vistaHistorial;
    private VistaPrincipalCliente vistaPrincipal; // Para poder manejar la restauración del centro
    private int idClienteLogueado;

 
    public ControladorHistorialCita(VistaHistorialCita vistaHistorial, VistaPrincipalCliente vistaPrincipal, int idClienteLogueado) {
        this.vistaHistorial = vistaHistorial;
        this.vistaPrincipal = vistaPrincipal;
        this.idClienteLogueado = idClienteLogueado;
        this.UsuarioDao = new UsuarioDao();

        // Escuchamos el botón Volver de la vista simplificada
        this.vistaHistorial.btnVolver.addActionListener(this);
    }

    /**
     * Método encargado de limpiar la tabla, consultar a MySQL y rellenar la interfaz
     */
    public void cargarHistorial() {
        // 1. Limpiamos cualquier residuo visual en el JTable
        vistaHistorial.limpiarTabla();

        try {
            // 2. Consultamos al CitaDAO pasándole el ID real del cliente logueado
            List<Object[]> registrosCitas = UsuarioDao.listarCitasPorCliente(this.idClienteLogueado);

            // 3. Si no hay citas, podemos informarlo; si hay, las agregamos una a una
            if (registrosCitas.isEmpty()) {
                vistaHistorial.limpiarYAgregarFila(new Object[]{"No registra citas vigentes", "-", "-"});
            } else {
                for (Object[] fila : registrosCitas) {
                    vistaHistorial.limpiarYAgregarFila(fila);
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en ControladorHistorialCitas: " + ex.getMessage());
            JOptionPane.showMessageDialog(vistaHistorial, "Error al conectar con la base de datos de citas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Al presionar el botón "< Volver" desde el historial
        if (e.getSource() == vistaHistorial.btnVolver) {
            // Limpiamos el centro dinámico y restauramos el catálogo principal
            JPanel contenedorCentral = vistaPrincipal.getContenidoCentralDinamico();
            contenedorCentral.removeAll();
            
            // Aquí puedes llamar al método que restaura la bienvenida y los productos en tu Vista Principal
            // Por ejemplo:
            // vistaPrincipal.restaurarComponentesIniciales(); 
            
            contenedorCentral.revalidate();
            contenedorCentral.repaint();
        }
    }
}
