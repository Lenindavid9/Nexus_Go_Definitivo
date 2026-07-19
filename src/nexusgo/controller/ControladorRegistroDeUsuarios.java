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
import nexusgo.view.VistaRegistroDeUsuario;
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
        String texIdentificacion = vistaRegistro.tNroIdentificacion.getText().trim();

        // Se obtiene el correo electrónico ingresado
        String correo = vistaRegistro.tCorreo.getText().trim();

        // Se obtiene la contraseña
        String contrasena = new String(vistaRegistro.tContrasena.getPassword()).trim();

        // Se obtiene la contraseña de confirmación para comprobar que el usuario escribió lo mismo en los dos campos.
        String confirmarContrasena = new String(vistaRegistro.tConfirmar.getPassword()).trim();

        // Se verifica que todos los campos obligatorios del formulario contengan información.
        if (nombre.isEmpty() || apellido.isEmpty() || texIdentificacion.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            // Se informa que se debe completar todos los campos Obligatorios
            JOptionPane.showMessageDialog(vistaRegistro,
                    "Por favor, complete todos los campos obligatorios del formulario.",
                    "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        /*Se verifica que la opcion de "seleccionar un tipo de documento" si contenga aplicada una opcion
        
        El metodo getSelectedIndex() devueve la posicion de la opcion elegida
        por el usuario, si el resultado es igual a 0 significa que sigue en la
        primera opcion en la lista que es "Seleccione un tipo de documento"
        y no es una opcion permitida*/
        if (vistaRegistro.miTipoDocumento.getSelectedIndex() == 0) {

            //Se le informa que debe seleccionar un tipo de documento ,
            JOptionPane.showMessageDialog(vistaRegistro, "Debes seleccionar un tipo de documento.",
                    "Error de seleccion", JOptionPane.ERROR_MESSAGE);
            return;
        }

        /*Se verica el número de identificación ingresado.
        Integer.parseInt() solo acepta cadenas que contengan
        exclusivamente números. Si el usuario escribe letras,
        símbolos o cualquier otro carácter que no sea numérico,
        se producirá una excepción.*/
        try {

            /*Se obtiene el texto del campo de identificación,
            si tiene, se eliminan los espacios en blanco al inicio y al final
            que por error pudo el usuario haber ingresado y se verificar qque el valor sea un número entero.*/
            Integer.parseInt(vistaRegistro.tNroIdentificacion.getText().trim());
        } catch (NumberFormatException e) {

            /*Si falla, significa que el usuario ingresó un valor que no es 
            un número(puede ser un . o una , o cualquier otra cosa)*/
            JOptionPane.showMessageDialog(vistaRegistro, "El numero de identificacion solo puede contener datos numericos.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        /*Se verifica que el número de identificación cumpla con la cantidad de dígitos
        permitida.
        La exprecion regular se LEE asi:
        ^ = Indica el inicio del texto.
        \\d = Representa un digito numero, osea que tiene que ser un numero del 0 al 9.
        {5,10] = Indica que deben existir coo minimo 5 digitos y maximo 10
        $ =  Indica que el texto debe terminar aqui*/
        if(!texIdentificacion.matches("^\\d{5,10}$")){
            
            //se informa que el numero de identidad no tiene la cantidad necesaria
            JOptionPane.showMessageDialog(vistaRegistro,"El numero de documento debe tener entre 5 y 10 digitos.",
                    "Error de cantidad de numeros", JOptionPane.ERROR_MESSAGE);
            return;
        }

        /*Después de comprobar que es válido, se convierte definitivamente el String 
        y se almacena en una variable int para utilizarla mas a delant edonde obliga el formato int*/
        int identificacion = Integer.parseInt(texIdentificacion);

        /*Se utiliza el método matches() para comprobar si el correo cumple con una dirección de correo.
        La expresión regular LEE asi:
        ^ = Indica el inicio del texto.
        \\w+ = Debe comenzar con una o más letras,números o el guion bajo _ 
        ([.-]?\\w+)* = Permite que después del inicio, puedan aparecer opcionalmente puntos o guiones seguidos de más letras o números.
        @ = Verifica que exista el símbolo @, lo cual es re obligatorio en toda dirección de correo.
        \\w+ = Corresponde al nombre del dominio, por ejp: gmail, outlook o sena.
        ([.-]?\\w+)* = Permite subdominios o dominios compuestos, como "mail.google".
        (\\.\\w{2,3})+ = Comprueba la extensión del dominio, el punto debe estar presente y debe ir
        seguido de entre 2 y 3 caracteres, como .com, .co, .net, .org, entre otros.
        $ = Indica que el texto debe terminar aquí.
        Si toda la cadena cumple con este patrón, el método matches() devolverá true.*/
        if (!correo.matches("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")) {

            //se muestra que el correo no tiene un formato valido.
            JOptionPane.showMessageDialog(vistaRegistro, "El correo ingresado no es valido, por favor verifiquelo y vuelva a intentarlo",
                    "Error de expresion regular", JOptionPane.ERROR_MESSAGE);
            return;
        }

        /*Se verifica que la contraseña cumpla con los requisitos mínimos de seguridad
        
        La exprecion se LEE asi:
        ^ = Indica el inicio del texto.
        (?=.*[A-Z]) = Comprueba que exista al menos una letra mayúscula (A-Z).
        (?=.*\\d) = Comprueba que exista al menos un número del 0 al 9.
        (?=.*[^A-Za-z0-9]) = Comprueba que exista al menos un símbolo o carácter especial
        como @, #, %, &, !, etc.
        .{8,} = Indica que la contraseña debe tener como mínimo 8 caracteres.
        $ = Indica que el texto debe terminar aquí.
        Si la contraseña no cumple alguno de estos requisitos,
        el método matches() devolverá false.*/
        if (!contrasena.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")) {
            
            //se informa que la contraseña no cumple con las condiciones establecidas
            JOptionPane.showMessageDialog(
                    vistaRegistro, "La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un símbolo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
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
