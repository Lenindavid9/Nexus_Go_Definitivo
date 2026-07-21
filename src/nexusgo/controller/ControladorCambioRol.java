/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaCambioRol;

/**
 *
 * @author USUARIO
 */
public class ControladorCambioRol {
    private VistaCambioRol vista;
    private UsuarioDao usuarioDAO;
    private boolean cargando = false; // Bandera para evitar bucles infinitos al cargar la tabla

    public ControladorCambioRol(VistaCambioRol vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDao();

        // 1. Cargar datos iniciales
        cargarUsuarios();

        // 2. Escuchar la edición del rol en la tabla
        this.vista.modelo.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Si la tabla se está poblando o el cambio no es una actualización de celda, omitir
                if (cargando || e.getType() != TableModelEvent.UPDATE) {
                    return;
                }

                // Columna 3 corresponde a ROL en tu DefaultTableModel
                if (e.getColumn() == 3) {
                    int fila = e.getFirstRow();

                    String numIdentificacion = vista.modelo.getValueAt(fila, 0).toString();
                    String nuevoRol = vista.modelo.getValueAt(fila, 3).toString();
                    String nombreCompleto = vista.modelo.getValueAt(fila, 1).toString() + " " + vista.modelo.getValueAt(fila, 2).toString();

                    int opcion = JOptionPane.showConfirmDialog(
                        vista,
                        "¿Desea cambiar el rol de " + nombreCompleto + " a '" + nuevoRol + "'?",
                        "Confirmar Cambio de Rol",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );

                    if (opcion == JOptionPane.YES_OPTION) {
                        boolean ok = usuarioDAO.actualizarRol(numIdentificacion, nuevoRol);

                        if (ok) {
                            JOptionPane.showMessageDialog(vista, "Rol actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(vista, "No se pudo actualizar el rol en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
                            cargarUsuarios(); // Recargar datos originales
                        }
                    } else {
                        cargarUsuarios(); // Revertir cambio en caso de cancelar
                    }
                }
            }
        });
    }

    // Llena la tabla consultando directamente el UsuarioDAO
    public void cargarUsuarios() {
        cargando = true;
        vista.modelo.setRowCount(0); // Limpiar la JTable

        List<Usuario> lista = usuarioDAO.listarUsuarios();

        if (lista != null) {
            for (Usuario u : lista) {
                Object[] fila = new Object[]{
                    u.getIdentificacion(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getRol(),
                    u.getCorreo()
                };
                vista.modelo.addRow(fila);
            }
        }
        cargando = false;
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}
