package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GestionarCarrito {

    // ─────────────────────────────────────────────────────────────
    // Obtener o crear carrito del comprador
    // ─────────────────────────────────────────────────────────────
    private int obtenerOCrearCarrito(Connection conn, int idComprador) throws SQLException {
        String sql = "SELECT id_carrito FROM carritos WHERE id_comprador = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idComprador);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_carrito");
            }
        }
        String ins = "INSERT INTO carritos (id_comprador) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idComprador);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        throw new SQLException("No se pudo crear el carrito.");
    }

    // ─────────────────────────────────────────────────────────────
    // CA024 — Agregar producto al carrito
    // ─────────────────────────────────────────────────────────────
    public String agregarProducto(int idComprador, int idPublicacion, int cantidad) {
        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            // Verificar stock
            int stock = obtenerStock(conn, idPublicacion);
            if (stock <= 0) {
                return "Este producto no tiene stock disponible.";
            }
            if (cantidad > stock) {
                return "Solo hay " + stock + " unidades disponibles.";
            }

            int idCarrito = obtenerOCrearCarrito(conn, idComprador);

            // Si ya existe el item, sumar cantidad
            String chk = "SELECT id_item, cantidad FROM carrito_items "
                    + "WHERE id_carrito = ? AND id_publicacion = ?";
            try (PreparedStatement ps = conn.prepareStatement(chk)) {
                ps.setInt(1, idCarrito);
                ps.setInt(2, idPublicacion);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int nuevaCant = rs.getInt("cantidad") + cantidad;
                    if (nuevaCant > stock) {
                        return "No puedes agregar más. Stock disponible: " + stock;
                    }
                    String upd = "UPDATE carrito_items SET cantidad = ? WHERE id_item = ?";
                    try (PreparedStatement pu = conn.prepareStatement(upd)) {
                        pu.setInt(1, nuevaCant);
                        pu.setInt(2, rs.getInt("id_item"));
                        pu.executeUpdate();
                    }
                } else {
                    String ins = "INSERT INTO carrito_items (id_carrito, id_publicacion, cantidad) "
                            + "VALUES (?, ?, ?)";
                    try (PreparedStatement pi = conn.prepareStatement(ins)) {
                        pi.setInt(1, idCarrito);
                        pi.setInt(2, idPublicacion);
                        pi.setInt(3, cantidad);
                        pi.executeUpdate();
                    }
                }
            }
            conn.commit();
            return "Producto agregado al carrito.";
        } catch (Exception e) {
            rollback(conn);
            return "Error al agregar producto: " + e.getMessage();
        } finally {
            cerrar(conn);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // CA025 — Modificar cantidad de un item
    // ─────────────────────────────────────────────────────────────
    public String modificarCantidad(int idItem, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            return eliminarItem(idItem);
        }
        try (Connection conn = ConexionBD.obtenerConexion()) {
            // Verificar stock antes de actualizar
            String sqlStock = "SELECT p.stock FROM publicaciones p "
                    + "JOIN carrito_items ci ON ci.id_publicacion = p.id_publicacion "
                    + "WHERE ci.id_item = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlStock)) {
                ps.setInt(1, idItem);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && nuevaCantidad > rs.getInt("stock")) {
                    return "Stock disponible: " + rs.getInt("stock");
                }
            }
            String sql = "UPDATE carrito_items SET cantidad = ? WHERE id_item = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, nuevaCantidad);
                ps.setInt(2, idItem);
                ps.executeUpdate();
            }
            return "Cantidad actualizada.";
        } catch (SQLException e) {
            return "Error al modificar: " + e.getMessage();
        }
    }

    // CA025 — Eliminar item del carrito
    public String eliminarItem(int idItem) {
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM carrito_items WHERE id_item = ?")) {
            ps.setInt(1, idItem);
            ps.executeUpdate();
            return "Producto eliminado del carrito.";
        } catch (SQLException e) {
            return "Error al eliminar: " + e.getMessage();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // CA026 — Obtener contenido actualizado del carrito
    // ─────────────────────────────────────────────────────────────
    public List<ItemCarrito> obtenerItems(int idComprador) {
        List<ItemCarrito> lista = new ArrayList<>();
        String sql = "SELECT ci.id_item, ci.id_carrito, ci.id_publicacion, ci.cantidad, "
                + "p.titulo, p.precio, p.stock, "
                + "COALESCE(i.ruta_archivo, '') AS ruta_archivo "
                + "FROM carritos c "
                + "JOIN carrito_items ci ON ci.id_carrito = c.id_carrito "
                + "JOIN publicaciones p  ON p.id_publicacion = ci.id_publicacion "
                + "LEFT JOIN imagenes_publicacion i ON i.id_publicacion = p.id_publicacion "
                + "WHERE c.id_comprador = ?";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idComprador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ItemCarrito(
                        rs.getInt("id_item"),
                        rs.getInt("id_carrito"),
                        rs.getInt("id_publicacion"),
                        rs.getInt("cantidad"),
                        rs.getString("titulo"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("ruta_archivo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error cargando carrito: " + e.getMessage());
        }
        return lista;
    }

    // ─────────────────────────────────────────────────────────────
    // Métodos de pago disponibles (CA029)
    // ─────────────────────────────────────────────────────────────
    public Map<String, Integer> obtenerMetodosPago() {
        Map<String, Integer> metodos = new LinkedHashMap<>();
        String sql = "SELECT id_metodo, nombre FROM metodos_pago ORDER BY id_metodo";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                metodos.put(rs.getString("nombre"), rs.getInt("id_metodo"));
            }
        } catch (SQLException e) {
            System.err.println("Error cargando métodos de pago: " + e.getMessage());
        }
        return metodos;
    }

    // ─────────────────────────────────────────────────────────────
    // RF012 + RF013 — Confirmar compra
    // ─────────────────────────────────────────────────────────────
    public String confirmarCompra(int idComprador, String direccionEnvio, int idMetodoPago) {
        if (direccionEnvio == null || direccionEnvio.trim().isEmpty()) {
            return "CA028:Debes ingresar una dirección de envío.";
        }
        if (idMetodoPago <= 0) {
            return "CA030:Debes seleccionar un método de pago.";
        }

        List<ItemCarrito> items = obtenerItems(idComprador);
        if (items.isEmpty()) {
            return "El carrito está vacío.";
        }

        double total = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            // Crear orden
            String sqlOrden = "INSERT INTO ordenes (id_comprador, direccion_envio, "
                    + "id_metodo_pago, total) VALUES (?, ?, ?, ?)";
            int idOrden;
            try (PreparedStatement ps = conn.prepareStatement(
                    sqlOrden, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idComprador);
                ps.setString(2, direccionEnvio.trim());
                ps.setInt(3, idMetodoPago);
                ps.setDouble(4, total);
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (!keys.next()) {
                    throw new SQLException("No se generó ID de orden.");
                }
                idOrden = keys.getInt(1);
            }

            // Insertar items de la orden y descontar stock
            for (ItemCarrito item : items) {
                String sqlItem = "INSERT INTO orden_items "
                        + "(id_orden, id_publicacion, cantidad, precio_unitario) "
                        + "VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                    ps.setInt(1, idOrden);
                    ps.setInt(2, item.getIdPublicacion());
                    ps.setInt(3, item.getCantidad());
                    ps.setDouble(4, item.getPrecioUnitario());
                    ps.executeUpdate();
                }
                // Descontar stock
                String sqlStock = "UPDATE publicaciones SET stock = stock - ? "
                        + "WHERE id_publicacion = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlStock)) {
                    ps.setInt(1, item.getCantidad());
                    ps.setInt(2, item.getIdPublicacion());
                    ps.executeUpdate();
                }
            }

            // Vaciar carrito
            String sqlVaciar = "DELETE ci FROM carrito_items ci "
                    + "JOIN carritos c ON ci.id_carrito = c.id_carrito "
                    + "WHERE c.id_comprador = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlVaciar)) {
                ps.setInt(1, idComprador);
                ps.executeUpdate();
            }

            conn.commit();
            return "OK:Compra confirmada. Orden #" + idOrden
                    + " — Total: $" + String.format("%,.2f", total);

        } catch (Exception e) {
            rollback(conn);
            return "Error al procesar la compra: " + e.getMessage();
        } finally {
            cerrar(conn);
        }
    }

    private int obtenerStock(Connection conn, int idPublicacion) throws SQLException {
        String sql = "SELECT stock FROM publicaciones WHERE id_publicacion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPublicacion);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("stock") : 0;
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
