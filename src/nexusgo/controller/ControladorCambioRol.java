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
    private final VistaCambioRol vista;
    private final UsuarioDao usuarioDAO;
    private boolean cargando = false; // Bandera para evitar bucles de eventos al repoblar la tabla

    public ControladorCambioRol(VistaCambioRol vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDao();

        // 1. Cargar los usuarios desde la BD a la JTable
        cargarUsuarios();

        // 2. Escuchar la edición directa en la celda del ROL
        this.vista.getModelo().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Si la tabla se está poblando o no es un evento de actualización, ignorar
                if (cargando || e.getType() != TableModelEvent.UPDATE) {
                    return;
                }

                // La columna 3 corresponde al ROL en la JTable
                if (e.getColumn() == 3) {
                    int fila = e.getFirstRow();

                    String numIdentificacion = vista.getModelo().getValueAt(fila, 0).toString();
                    String nuevoRol = vista.getModelo().getValueAt(fila, 3).toString();
                    String nombreCompleto = vista.getModelo().getValueAt(fila, 1).toString() + " " 
                                          + vista.getModelo().getValueAt(fila, 2).toString();

                    int opcion = JOptionPane.showConfirmDialog(
                        vista,
                        "¿Desea cambiar el rol de " + nombreCompleto + " a '" + nuevoRol + "'?",
                        "Confirmar Cambio de Rol",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );

                    if (opcion == JOptionPane.YES_OPTION) {
                        // Llama al DAO con la subconsulta de la base de datos
                        boolean ok = usuarioDAO.actualizarRol(numIdentificacion, nuevoRol);

                        if (ok) {
                            JOptionPane.showMessageDialog(vista, 
                                    "Rol actualizado con éxito en la base de datos.", 
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(vista, 
                                    "No se pudo actualizar el rol en la base de datos.", 
                                    "Error BD", JOptionPane.ERROR_MESSAGE);
                            cargarUsuarios(); // Revertir a los valores originales de la BD
                        }
                    } else {
                        cargarUsuarios(); // Cancelado por el usuario, reestablece el valor previo
                    }
                }
            }
        });
    }

    /**
     * Consulta los usuarios en la base de datos a través de UsuarioDao
     * y llena la JTable en la vista.
     */
    public final void cargarUsuarios() {
        cargando = true; // Activa la bandera para silenciar el listener temporalmente
        vista.getModelo().setRowCount(0); // Limpia la tabla

        List<Usuario> lista = usuarioDAO.listarUsuarios();

        if (lista != null && !lista.isEmpty()) {
            for (Usuario u : lista) {
                Object[] fila = new Object[]{
                    u.getIdentificacion(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getRol(),
                    u.getCorreo()
                };
                vista.getModelo().addRow(fila);
            }
        }
        cargando = false; // Desactiva la bandera para reanudar la escucha de eventos
    }
}
