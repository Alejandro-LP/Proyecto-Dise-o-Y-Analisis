package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionarCatalogo {

    // RF009 + RF010 — Buscar y filtrar productos (CA020, CA022, CA023)
    public List<Publicacion> buscarProductos(String textoBusqueda, int idCategoria,
            String estadoProducto, double precioMin,
            double precioMax) {
        List<Publicacion> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.nombre AS nombre_categoria, i.ruta_archivo "
                + "FROM publicaciones p "
                + "JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "JOIN vendedores v ON p.id_vendedor = v.id_vendedor "
                + "LEFT JOIN imagenes_publicacion i ON i.id_publicacion = p.id_publicacion "
                + "WHERE v.validado = TRUE AND p.stock > 0 "
        );

        // CA022: filtros dinámicos
        if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
            sql.append("AND (p.titulo LIKE ? OR p.descripcion LIKE ?) ");
        }
        if (idCategoria > 0) {
            sql.append("AND p.id_categoria = ? ");
        }
        if (estadoProducto != null && !estadoProducto.equals("TODOS")) {
            sql.append("AND p.estado_producto = ? ");
        }
        if (precioMin >= 0 && precioMax > 0) {
            sql.append("AND p.precio BETWEEN ? AND ? ");
        }

        sql.append("ORDER BY p.fecha_creacion DESC");

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
                String like = "%" + textoBusqueda + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            if (idCategoria > 0) {
                ps.setInt(idx++, idCategoria);
            }
            if (estadoProducto != null && !estadoProducto.equals("TODOS")) {
                ps.setString(idx++, estadoProducto);
            }
            if (precioMin >= 0 && precioMax > 0) {
                ps.setDouble(idx++, precioMin);
                ps.setDouble(idx++, precioMax);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Publicacion(
                        rs.getInt("id_publicacion"),
                        rs.getInt("id_vendedor"),
                        rs.getInt("id_categoria"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("estado_producto"),
                        rs.getString("politicas_devolucion"),
                        rs.getString("ruta_archivo"),
                        rs.getString("nombre_categoria")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }
        return lista;
    }

    // Precio máximo del catálogo para el slider
    public double obtenerPrecioMaximo() {
        String sql = "SELECT MAX(precio) FROM publicaciones";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo precio máximo: " + e.getMessage());
        }
        return 1000000;
    }
}
