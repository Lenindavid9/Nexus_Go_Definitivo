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

/*
TableModelListener es una interfaz de Java Swing que se utiliza para "escuchar" o detectar cualquier cambio en los 
datos de un modelo de tabla (TableModel). Permite que tu aplicación reaccione automáticamente (por ejemplo, recalculando totales
o guardando cambios) cuando un usuario edita, agrega o elimina una celda en un JTable
*/
/**
 *
 * @author USUARIO
 */
public class ControladorCambioRol implements TableModelListener{
    
   private final VistaCambioRol vista;
    private final UsuarioDao usuarioDAO;
    private boolean cargando = false; 

    public ControladorCambioRol(VistaCambioRol vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDao();

        // 1. Escuchar eventos de modificación en la tabla
        if (this.vista.getModelo() != null) {
            this.vista.getModelo().addTableModelListener(this);
        }

        // 2. Poblar la tabla de inmediato al iniciar
        cargarUsuarios();
    }

    public final void cargarUsuarios() {
        cargando = true; // Pausa el listener para que no salte durante el llenado

        try {
            vista.getModelo().setRowCount(0); // Limpia datos previos

            List<Usuario> lista = usuarioDAO.listarUsuarios();

            if (lista != null && !lista.isEmpty()) {
                for (Usuario u : lista) {
                    Object[] fila = new Object[]{
                        String.valueOf(u.getIdentificacion()),
                        u.getNombre(),
                        u.getApellido(),
                        u.getRol(),
                        u.getCorreo()
                    };
                    vista.getModelo().addRow(fila);
                }
                System.out.println("✅ [Controlador] " + lista.size() + " usuarios cargados.");
            } else {
                System.out.println("ℹ️ [Controlador] Sin datos devueltos por la BD.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al popular tabla: " + e.getMessage());
        } finally {
            cargando = false; // Reactiva el listener
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        // Ignora eventos si estamos cargando o si no son de edición (UPDATE)
        if (cargando || e.getType() != TableModelEvent.UPDATE) {
            return;
        }

        // Solo actua si se cambió la columna ROL (Índice 3)
        if (e.getColumn() == 3) {
            int fila = e.getFirstRow();

            String doc = vista.getModelo().getValueAt(fila, 0).toString();
            String nuevoRol = vista.getModelo().getValueAt(fila, 3).toString();
            String nombre = vista.getModelo().getValueAt(fila, 1).toString() + " " + vista.getModelo().getValueAt(fila, 2).toString();

            int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas cambiar el rol de " + nombre + " a '" + nuevoRol + "'?",
                "Confirmar Cambio",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = usuarioDAO.actualizarRol(doc, nuevoRol);

                if (exito) {
                    JOptionPane.showMessageDialog(vista, "Rol actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar en Base de Datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    cargarUsuarios(); // Revertir cambio
                }
            } else {
                cargarUsuarios(); // Revertir selección
            }
        }
    }
}
