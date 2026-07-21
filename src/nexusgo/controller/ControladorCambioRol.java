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
    private boolean cargando = false; // Bandera para omitir eventos cuando la tabla se está poblando

    public ControladorCambioRol(VistaCambioRol vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDao();

        // 1. Cargar usuarios al inicializar el controlador
        cargarUsuarios();

        // 2. Escuchar la edición directa en la celda de la columna ROL (Índice 3)
        this.vista.getModelo().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Ignorar si la tabla apenas se está llenando o si no es un evento de actualización
                if (cargando || e.getType() != TableModelEvent.UPDATE) {
                    return;
                }

                // Columna 3 = ROL
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
                        boolean ok = usuarioDAO.actualizarRol(numIdentificacion, nuevoRol);

                        if (ok) {
                            JOptionPane.showMessageDialog(vista, 
                                    "Rol actualizado con éxito.", 
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(vista, 
                                    "No se pudo actualizar el rol en la base de datos.", 
                                    "Error BD", JOptionPane.ERROR_MESSAGE);
                            cargarUsuarios(); // Restaurar valor previo
                        }
                    } else {
                        cargarUsuarios(); // Restaurar valor previo en caso de cancelación
                    }
                }
            }
        });
    }

    /**
     * Consulta los usuarios a través de UsuarioDao y actualiza el DefaultTableModel de la vista.
     */
    public final void cargarUsuarios() {
        cargando = true; // Desactiva temporalmente el listener
        vista.getModelo().setRowCount(0); // Limpia filas viejas

        List<Usuario> lista = usuarioDAO.listarUsuarios();

        // Mensaje de depuración en consola para verificar qué retorna el DAO
        if (lista == null) {
            System.err.println("⚠️ [ControladorCambioRol]: La consulta retornó NULL. Revisa la conexión o la sintaxis SQL.");
        } else if (lista.isEmpty()) {
            System.out.println("ℹ️ [ControladorCambioRol]: La base de datos no devolvió registros.");
        } else {
            System.out.println("✅ [ControladorCambioRol]: Cargados " + lista.size() + " usuarios correctamente.");
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
        
        cargando = false; // Reactiva la escucha de eventos
    }
    
}
