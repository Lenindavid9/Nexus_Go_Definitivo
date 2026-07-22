package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CajaDao {

    private final Conexion cn = new Conexion();

    /*Este método registra la apertura de una nueva caja en la base
    de datos y devuelve el identificador generado para ese registro.
    
    Recibe como parámetros el monto inicial con el que se abre
    la caja y el identificador del Supervisor responsable de la apertura.*/
    public int guardarApertura(double montoApertura, int idOperador) {

        // Se define la consulta SQL que insertará un nuevo registro en la tabla "cajas".
        String sql = "INSERT INTO cajas (id_operador, fecha_apertura, monto_apertura, estado_caja) "
                + "VALUES (?, NOW(), ?, 'ABIERTA')";

        // Se utiliza un try para abrir la conexión con la base de datos
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Se asigna el identificador
            ps.setInt(1, idOperador);

            // Se asigna el monto con el que se abrirá la caja
            ps.setDouble(2, montoApertura);

            // Se ejecuta la consulta para guardar el registro en la base de datos.
            ps.executeUpdate();

            /*Después de realizar la inserción, se obtiene el
            identificador generado automáticamente por la base
            de datos para el nuevo registro.*/
            try (ResultSet rs = ps.getGeneratedKeys()) {

                // Se verifica si realmente se obtuvo una clave generada.
                if (rs.next()) {

                    // Se devuelve el identificador del nuevo registro
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {

            // Si ocurre algún problema se imprime un error
            e.printStackTrace();
        }
        return 0;
    }

    /* Este método registra el cierre de una caja abierta.
    
    Recibe como parámetros el identificador de la caja, el monto
    contado físicamente por el operador y el monto calculado por
    el sistema. Al finalizar, devuelve true si la actualización
    se realizó correctamente o false si ocurrió algún problema.*/
    public boolean guardarCierre(int idCaja, double montoCierreEfectivo, double montoCierreCalculado) {

        /*Se define la consulta SQL.
        
        La fecha de cierre se registra automáticamente con la función
        NOW(), se almacenan los montos de cierre y el estado de la
        caja cambia de "ABIERTA" a "CERRADA".*/
        String sql = "UPDATE cajas SET fecha_cierre = NOW(), monto_cierre_efectivo = ?, "
                + "monto_cierre_calculado = ?, estado_caja = 'CERRADA' WHERE id_caja = ?";

        // Se establece la conexión con la base de datos
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se asigna el monto de efectivo contado por el operador
            ps.setDouble(1, montoCierreEfectivo);

            // Se asigna el monto calculado por el sistema
            ps.setDouble(2, montoCierreCalculado);

            // Se indica cuál es la caja que será actualizada
            ps.setInt(3, idCaja);

            /*// Se ejecuta la actualización.
        //
        // executeUpdate() devuelve la cantidad de registros
        // modificados. Si el resultado es mayor que cero,
        // significa que la caja fue actualizada correctamente,
        // por lo que el método devolverá true.*/
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {

            // Si ocurre algún error sale en la consola
            e.printStackTrace();
            System.out.println("A ocurrido un error en CajaDao guardando el cierre");
            return false;
        }
    }

    /*Este método busca en la base de datos si actualmente existe
    una caja que se encuentre abierta*/
    public int obtenerCajaAbierta() {

        //Se define la consulta SQL que busca el identificador de la última caja que tenga el estado "ABIERTA".
        String sql = "SELECT id_caja FROM cajas WHERE estado_caja = 'ABIERTA' ORDER BY id_caja DESC LIMIT 1";

        /*Se establece la conexión con la base de datos, se prepara
        la consulta y se ejecuta automáticamente.
        
        El resultado obtenido se almacena en un ResultSet,
        el cual permite recorrer los registros devueltos
        por la consulta.*/
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            
            // Se verifica si la consulta devolvió al menos un registro.
            if (rs.next()) {
                
                // Si existe una caja abierta, se obtiene el valor de la primera columna del resultado
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            
            // Si ocurre algún error se imprime la información en la consola
            e.printStackTrace();
            System.out.println("Error en CajaDao en obtenerCajaAbierta");
        }
        return 0;
    }

    //Este método verifica si la caja se encuentra actualmente abierta
    public boolean verificarCajaAbierta(int idCaja) {
        
        /*e define la consulta SQL que busca la caja donde identificador coincida
        con el estado sea "ABIERTA".*/
        String sql = "SELECT id_caja FROM cajas WHERE id_caja = ? AND estado_caja = 'ABIERTA'";
        
        // Se establece la conexión con la base de datos
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Se establece la conexión con la base de datos
            ps.setInt(1, idCaja);
            
             // Se ejecuta la consulta y el resultado obtenido se almacena en un ResultSet.
            try (ResultSet rs = ps.executeQuery()) {
                
                /* El método next() devuelve true si la consulta
                encontró al menos un registro que cumpla
                las condiciones establecidas*/
                return rs.next();
            }
        } catch (SQLException e) {
            
            // Si ocurre algún error durante la consulta se muestra un mensaje
            e.printStackTrace();
            System.out.println("Error en CajaDao en verificarCajaAbierta");
            return false;
        }
    }

    public double obtenerMontoApertura(int idCaja) {
            /* Se define la consulta SQL que obtiene únicamente
            el campo monto_apertura de la caja indicada.*/
        String sql = "SELECT monto_apertura FROM cajas WHERE id_caja = ?";
        
        // Se establece la conexión con la base de datos
        try (Connection con = cn.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Se asigna el identificador de la caja
            ps.setInt(1, idCaja);
            
            // Se ejecuta la consulta y el resultado obtenido se almacena en un ResultSet.
            try (ResultSet rs = ps.executeQuery()) {
                
                // Se verifica si la consulta encontró un registro.
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            // Si ocurre algún error durante la consulta saldra este error
            e.printStackTrace();
            System.out.println("Error en CajaDao en obtenerMontoApertura");
        }
        return 0;
    }
}