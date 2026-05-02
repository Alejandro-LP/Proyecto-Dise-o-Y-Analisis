package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionarPublicacion {

    // ─────────────────────────────────────────────
    // RF006 — Crear publicación (CA013, CA014, CA015)
    // ─────────────────────────────────────────────
    public String crearPublicacion(Publicacion p) {
        // CA015: vendedor debe estar validado
        if (!vendedorHabilitado(p.getIdVendedor())) {
            return "Tu cuenta de vendedor aún no ha sido validada por el administrador.";
        }

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            String sqlPub = "INSERT INTO publicaciones (id_vendedor, id_categoria, titulo, "
                    + "descripcion, precio, stock, estado_producto, politicas_devolucion) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            int idPublicacion;
            try (PreparedStatement ps = conn.prepareStatement(sqlPub, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, p.getIdVendedor());
                ps.setInt(2, p.getIdCategoria());
                ps.setString(3, p.getTitulo());
                ps.setString(4, p.getDescripcion());
                ps.setDouble(5, p.getPrecio());
                ps.setInt(6, p.getStock());
                ps.setString(7, p.getEstadoProducto());
                ps.setString(8, p.getPoliticasDevolucion());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (!keys.next()) {
                    throw new SQLException("No se generó ID de publicación.");
                }
                idPublicacion = keys.getInt(1);
            }

            // Guardar imagen si se proporcionó
            if (p.getRutaImagen() != null && !p.getRutaImagen().isEmpty()) {
                String sqlImg = "INSERT INTO imagenes_publicacion (id_publicacion, ruta_archivo) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlImg)) {
                    ps.setInt(1, idPublicacion);
                    ps.setString(2, p.getRutaImagen());
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return "Publicación creada exitosamente.";

        } catch (Exception e) {
            rollback(conn);
            return "Error al crear publicación: " + e.getMessage();
        } finally {
            cerrar(conn);
        }
    }

    // ─────────────────────────────────────────────
    // RF007 — Actualizar publicación (CA016, CA017)
    // ─────────────────────────────────────────────
    public String actualizarPublicacion(Publicacion p, int idVendedorSesion) {
        // CA016: solo el propietario puede actualizar
        if (!esPropiedad(p.getIdPublicacion(), idVendedorSesion)) {
            return "No tienes permiso para modificar esta publicación.";
        }

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            String sql = "UPDATE publicaciones SET id_categoria=?, titulo=?, descripcion=?, "
                    + "precio=?, stock=?, estado_producto=?, politicas_devolucion=? "
                    + "WHERE id_publicacion=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, p.getIdCategoria());
                ps.setString(2, p.getTitulo());
                ps.setString(3, p.getDescripcion());
                ps.setDouble(4, p.getPrecio());
                ps.setInt(5, p.getStock());
                ps.setString(6, p.getEstadoProducto());
                ps.setString(7, p.getPoliticasDevolucion());
                ps.setInt(8, p.getIdPublicacion());
                ps.executeUpdate();
            }

            // Actualizar imagen si se proporcionó una nueva
            if (p.getRutaImagen() != null && !p.getRutaImagen().isEmpty()) {
                String sqlDel = "DELETE FROM imagenes_publicacion WHERE id_publicacion=?";
                try (PreparedStatement ps = conn.prepareStatement(sqlDel)) {
                    ps.setInt(1, p.getIdPublicacion());
                    ps.executeUpdate();
                }
                String sqlImg = "INSERT INTO imagenes_publicacion (id_publicacion, ruta_archivo) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlImg)) {
                    ps.setInt(1, p.getIdPublicacion());
                    ps.setString(2, p.getRutaImagen());
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return "Publicación actualizada correctamente.";

        } catch (Exception e) {
            rollback(conn);
            return "Error al actualizar: " + e.getMessage();
        } finally {
            cerrar(conn);
        }
    }

    // ─────────────────────────────────────────────
    // RF008 — Consultar detalle de publicación (CA018, CA019)
    // ─────────────────────────────────────────────
    public Publicacion consultarDetalle(int idPublicacion) {
        String sql = "SELECT p.*, c.nombre AS nombre_categoria, "
                + "i.ruta_archivo "
                + "FROM publicaciones p "
                + "JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "LEFT JOIN imagenes_publicacion i ON i.id_publicacion = p.id_publicacion "
                + "WHERE p.id_publicacion = ? LIMIT 1";

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPublicacion);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return new Publicacion(
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
            );
        } catch (SQLException e) {
            System.err.println("Error consultando detalle: " + e.getMessage());
            return null;
        }
    }

    // Listar publicaciones de un vendedor
    public List<Publicacion> listarPorVendedor(int idVendedor) {
        List<Publicacion> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS nombre_categoria, i.ruta_archivo "
                + "FROM publicaciones p "
                + "JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "LEFT JOIN imagenes_publicacion i ON i.id_publicacion = p.id_publicacion "
                + "WHERE p.id_vendedor = ? ORDER BY p.fecha_creacion DESC";

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVendedor);
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
            System.err.println("Error listando publicaciones: " + e.getMessage());
        }
        return lista;
    }

    // Cargar categorías para el combo
    public Map<String, Integer> obtenerCategorias() {
        Map<String, Integer> categorias = new HashMap<>();
        String sql = "SELECT id_categoria, nombre FROM categorias ORDER BY nombre";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categorias.put(rs.getString("nombre"), rs.getInt("id_categoria"));
            }
        } catch (SQLException e) {
            System.err.println("Error cargando categorías: " + e.getMessage());
        }
        return categorias;
    }

    // ─────────────────────────────────────────────
    // Helpers privados
    // ─────────────────────────────────────────────
    private boolean vendedorHabilitado(int idVendedor) {
        String sql = "SELECT validado FROM vendedores WHERE id_vendedor = ?";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVendedor);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getBoolean("validado");
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean esPropiedad(int idPublicacion, int idVendedor) {
        String sql = "SELECT 1 FROM publicaciones WHERE id_publicacion=? AND id_vendedor=?";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idVendedor);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    private void rollback(Connection conn) {
        if (conn != null) try {
            conn.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void cerrar(Connection conn) {
        if (conn != null) try {
            conn.close();
        } catch (SQLException ignored) {
        }
    }
}
