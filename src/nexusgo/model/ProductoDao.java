/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class ProductoDao implements Crud<Producto> {
    
    private final Conexion conexion = new Conexion();

    // R - READ: LISTAR TODOS LOS PRODUCTOS
    @Override
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setStockActual(rs.getInt("stock_actual"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));
                producto.setPrecioCompra(rs.getDouble("precio_compra"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setUrlImagen(rs.getString("url_imagen"));

                lista.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // C - CREATE: AGREGAR PRODUCTO
    @Override
    public int agregar(Producto producto) {
        String sql = """
                     INSERT INTO productos 
                     (nombre_producto, descripcion, stock_actual, stock_minimo, precio_compra, precio_venta, url_imagen, proveedor) 
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                     """;

        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombreProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setInt(3, producto.getStockActual());
            ps.setInt(4, producto.getStockMinimo());
            ps.setDouble(5, producto.getPrecioCompra());
            ps.setDouble(6, producto.getPrecioVenta());
            ps.setString(7, producto.getUrlImagen());
            ps.setString(8, producto.getProveedor());

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar producto: " + e.getMessage());
        }
        return 0;
    }

    // U - UPDATE: EDITAR PRODUCTO
    @Override
    public int editar(Producto producto) {
        String sql = """
                     UPDATE productos 
                     SET nombre_producto = ?, descripcion = ?, stock_actual = ?, stock_minimo = ?, precio_compra = ?, precio_venta = ?, url_imagen = ? 
                     WHERE id_producto = ?
                     """;

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombreProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setInt(3, producto.getStockActual());
            ps.setInt(4, producto.getStockMinimo());
            ps.setDouble(5, producto.getPrecioCompra());
            ps.setDouble(6, producto.getPrecioVenta());
            ps.setString(7, producto.getUrlImagen());
            ps.setInt(8, producto.getIdProducto());

            return ps.executeUpdate(); 

        } catch (SQLException e) {
            System.err.println("❌ Error al editar producto: " + e.getMessage());
        }
        return 0;
    }

    // D - DELETE: ELIMINAR PRODUCTO
    @Override
    public int eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate(); 

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
        }
        return 0;
    }

    // MÉTODO PROPIO: Buscar un único producto por ID
    public Producto buscarPorId(int id) {
        Producto producto = null;
        String sql = "SELECT * FROM productos WHERE id_producto = ?";

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto")); 
                    producto.setNombreProducto(rs.getString("nombre_producto"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setStockActual(rs.getInt("stock_actual"));
                    producto.setStockMinimo(rs.getInt("stock_minimo"));
                    producto.setPrecioCompra(rs.getDouble("precio_compra"));
                    producto.setPrecioVenta(rs.getDouble("precio_venta"));
                    producto.setUrlImagen(rs.getString("url_imagen"));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar producto por ID: " + e.getMessage());
        }
        return producto;
    }

    // MÉTODO PROPIO: Descontar stock validando disponibilidad suficiente
    public boolean registrarSalidaStock(int idProducto, int cantidad) {
        String sql = """
                     UPDATE productos 
                     SET stock_actual = stock_actual - ? 
                     WHERE id_producto = ? AND stock_actual >= ?
                     """;

        try (Connection con = conexion.getConection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad); // Condición para no permitir stock negativo

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar salida de stock en DAO: " + e.getMessage());
            return false;
        }
    }

    // MÉTODO PROPIO: Listar productos vista cliente
    public List<Producto> listarProductosCliente() {
        List<Producto> listaProds = new ArrayList<>();
        String sql = "SELECT id_producto, nombre_producto, precio_compra, precio_venta, stock_actual, stock_minimo, url_imagen FROM productos";

        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto prod = new Producto();
                prod.setIdProducto(rs.getInt("id_producto"));
                prod.setNombreProducto(rs.getString("nombre_producto"));
                prod.setPrecioCompra(rs.getDouble("precio_compra"));
                prod.setPrecioVenta(rs.getDouble("precio_venta"));
                prod.setStockActual(rs.getInt("stock_actual"));
                prod.setStockMinimo(rs.getInt("stock_minimo"));
                prod.setUrlImagen(rs.getString("url_imagen"));

                listaProds.add(prod);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar productos para el cliente: " + e.getMessage());
        }
        return listaProds;
    }

    // MÉTODO PROPIO: Listar productos en promoción
    public List<Producto> listarPromociones() {
        List<Producto> listaPromo = new ArrayList<>();
        
        // Consulta limpia utilizando únicamente columnas existentes en la tabla "productos"
        String sql = """
                     SELECT id_producto, nombre_producto, precio_compra, precio_venta, descripcion, url_imagen, stock_actual, stock_minimo 
                     FROM productos
                     """;

        try (Connection con = conexion.getConection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setPrecioCompra(rs.getDouble("precio_compra"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setUrlImagen(rs.getString("url_imagen"));
                producto.setStockActual(rs.getInt("stock_actual"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));

                listaPromo.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar promociones desde ProductoDao: " + e.getMessage());
        }

        return listaPromo;
    }

}
