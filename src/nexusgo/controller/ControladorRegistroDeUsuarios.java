/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaInicioSesion;
import registro.VistaRegistroDeUsuario;
import nexusgo.view.VistaPrincipalCliente;

/**
 *
 * @author USUARIO
 */
public class ControladorRegistroDeUsuarios implements ActionListener {

    private final VistaRegistroDeUsuario vistaRegistro;
    private final UsuarioDao usuarioDao;

    public ControladorRegistroDeUsuarios(VistaRegistroDeUsuario vistaRegistro) {

        // Se guarda la referencia de la vista recibida.
        this.vistaRegistro = vistaRegistro;

        // Instancia del DAO que contiene las consultas SQL
        this.usuarioDao = new UsuarioDao();

        //Se llama al método de registrar todos los eventos de los botones
        inicializarListeners();
    }

    /* Este método registra los botones que serán escuchados
       por este controlador. 
       De esta manera, cuando el usuario haga click en alguno
       de ellos, el método actionPerformed() será ejecutado
       automáticamente.
     */
    private void inicializarListeners() {

        // Se registra el botón "Registrarse".
        this.vistaRegistro.btnRegistrarse.addActionListener(this);

        // Se registra el botón "Volver".
        this.vistaRegistro.btnVolver.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Usamos estructura else if limpia para evitar que hilos duplicados cancelen el evento

        // Se verifica si el evento fue generado por el botón "Registrarse".
        if (e.getSource() == vistaRegistro.btnRegistrarse) {
            System.out.println("Botón Registrarse detectado correctamente");
            ejecutarRegistro();
        } else if (e.getSource() == vistaRegistro.btnVolver) {
            System.out.println("Botón Volver detectado correctamente");
            regresarAlLogin();
        }
    }

    //Captura, valida y procesa la inserción del nuevo usuario en el sistema.
    private void ejecutarRegistro() {
        /*Se obtiene el nombre ingresado por el usuario.
        El método trim() es para eliminar los espacios en blanco al inicio
        y al final del nombre para evitar almacenar datos con
        espacios innecesarios.*/
        String nombre = vistaRegistro.tNombre.getText().trim();

        // Se obtiene el apellido ingresado por el usuario.
        String apellido = vistaRegistro.tApellido.getText().trim();

        /*Se obtiene la posición del tipo de documento seleccionado en el JComboBox,
        con este valor permitira verificar más adelante
        si el usuario seleccionó una opción válida*/
        int indiceDocumento = vistaRegistro.miTipoDocumento.getSelectedIndex();

        // Se obtiene el nombre del tipo de documento
        String tipoDocumento = vistaRegistro.miTipoDocumento.getSelectedItem().toString();

        // Se obtiene el número de identificación
        String identificacion = vistaRegistro.tNroIdentificacion.getText().trim();

        // Se obtiene el correo electrónico ingresado
        String correo = vistaRegistro.tCorreo.getText().trim();

        // Se obtiene la contraseña
        String contrasena = new String(vistaRegistro.tContrasena.getPassword()).trim();

        // Se obtiene la contraseña de confirmación para comprobar que el usuario escribió lo mismo en los dos campos.
        String confirmarContrasena = new String(vistaRegistro.tConfirmar.getPassword()).trim();

        // Se verifica que todos los campos obligatorios del formulario contengan información.
        if (nombre.isEmpty() || apellido.isEmpty() || identificacion.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            // Se informa que se debe completar todos los campos Obligatorios
            JOptionPane.showMessageDialog(vistaRegistro,
                    "Por favor, complete todos los campos obligatorios del formulario.",
                    "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        /*Se verifica si se selecciono el primer elemento
        del JComboBox. en esa esta la posición "Seleccione un tipo de documento", pero esa
        no es válida para realizar el registro.*/
        if (indiceDocumento == 0) {
            // Se muestra un mensaje indicando que es obligatorio seleccionar  una opcion
            JOptionPane.showMessageDialog(vistaRegistro,
                    "Por favor, seleccione un tipo de documento.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        /*Se comparan la contraseña y la confirmación de contraseña para verificar que ambas sean iguales.
        Si uno de los dos valores son diferentes, enotnces... */
        if (!contrasena.equals(confirmarContrasena)) {
            
            // Se informa que las contraseñas no coinciden y que debe verificarlas antes de continuar.
            JOptionPane.showMessageDialog(vistaRegistro,
                    "Las contraseñas ingresadas no coinciden. Verifíquelas nuevamente.",
                    "Error de Seguridad", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se crea un nuevo objeto de tipo Usuario.
        Usuario nuevoUsuario = new Usuario();
        
        // Se asigna al objeto el nombre.
        nuevoUsuario.setNombre(nombre);
        
        // Se asigna el apellido ingresado.
        nuevoUsuario.setApellido(apellido);
        
        // Se asigna el tipo de documento que el usuario seleccionó
        nuevoUsuario.setTipoDocumento(tipoDocumento);
        
        // Se asigna el número de identificación del usuario.
        nuevoUsuario.setIdentificacion(identificacion);
        
        // Se asigna el correo electrónico que utilizará para mandar las recuperacioes y PDF
        nuevoUsuario.setCorreo(correo);
        
        // Se asigna la contraseña
        nuevoUsuario.setContrasena(contrasena);

        /*El valor devuelto se almacena en la variable resultado.
        Si el valor es mayor que cero, significa que el registro
        se realizó correctamente.*/
        int resultado = usuarioDao.registrar(nuevoUsuario);

        // Se verifica si el proceso de registro fue exitoso.
        if (resultado > 0) {
            
            // Se informa al usuario que su cuenta fue creada con exito
            JOptionPane.showMessageDialog(vistaRegistro,
                    "El usuario se ha registrado con éxito, Bienvenido a Nexus Go!",
                    "Nexus Go Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Se cierra la ventana de registro,
            this.vistaRegistro.dispose();

            // Se crea la vista principal correspondiente al cliente.
            VistaPrincipalCliente vistaCliente = new VistaPrincipalCliente("", "");//////////POR SOLUCIONAR///////////
            
            // Se llama el controlador encargado de administrar la lógica de la vista del cliente 
            ControladorPrincipalCliente controlCliete = new ControladorPrincipalCliente(vistaCliente);
            
            //Se hace visible la ventana principal del cliente
            vistaCliente.setVisible(true);

        } else {
            
            /* Si el resultado no es mayor que cero, significa que el
            registro no pudo completarse correctamente.
    
            Esto puede ocurrir si la identificación
            o el correo electrónico ya se encuentran registrados en la base de datos.*/
            JOptionPane.showMessageDialog(vistaRegistro,
                    "No se pudo completar el registro. Es posible que la identificación o el correo ya existan.",
                    "Error de Inserción", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Método encargado de cerrar la vista de registro y restablecer el flujo del login inicial.
    private void regresarAlLogin() {
        
        // Se cierra la ventana de registro
        this.vistaRegistro.dispose();

        // Se crea la instancia de la ventana de inicio de sesión.
        VistaInicioSesion loginVista = new VistaInicioSesion();
        
        // Se crea el controlador de la ventana de inicio de sesión
        ControladorInicioSesion controlInicioSesion = new ControladorInicioSesion(loginVista);

        //se hace visible la ventana de inicio de sesión,
        loginVista.setVisible(true);
    }
}
