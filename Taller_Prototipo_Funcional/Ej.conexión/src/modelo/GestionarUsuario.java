package modelo;

import java.sql.*;
import java.util.UUID;
import javax.swing.JOptionPane;

/**
 * Capa de acceso a datos para el módulo de Usuarios.
 * RF001 (comprador), RF002 (vendedor), RF003 (confirmación email simulada),
 * RF004 (autenticación), RF005 (validación identidad vendedor).
 */
public class GestionarUsuario {

    // ─────────────────────────────────────────────────────────────────────────
    // RF001 — Registrar comprador
    // ─────────────────────────────────────────────────────────────────────────
    public String registrarComprador(Usuario u, String password) {
        if (existeDocumento(u.getDocumento())) return "Ya existe una cuenta con ese documento.";
        if (existeCorreo(u.getCorreo()))       return "Ya existe una cuenta con ese correo.";

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            int idUsuario = insertarUsuario(conn, u, password);

            String sqlComp = "INSERT INTO compradores (id_comprador) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlComp)) {
                ps.setInt(1, idUsuario);
                ps.executeUpdate();
            }

            String token = generarToken();
            guardarToken(conn, idUsuario, token);

            conn.commit();

            // RF003 simulado: mostrar token en popup
            mostrarTokenSimulado(u.getCorreo(), token);

            return "Registro exitoso. Ingresa el código que apareció en pantalla.";

        } catch (Exception e) {
            rollback(conn);
            return "Error al registrar: " + e.getMessage();
        } finally {
            cerrar(conn);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RF002 — Registrar vendedor
    // ─────────────────────────────────────────────────────────────────────────
    public String registrarVendedor(Usuario u, String password,
                                    String cuentaBancaria, boolean contratoAceptado) {
        if (!contratoAceptado) return "Debes aceptar el contrato de comisión para registrarte.";
        if (existeDocumento(u.getDocumento())) return "Ya existe una cuenta con ese documento.";
        if (existeCorreo(u.getCorreo()))       return "Ya existe una cuenta con ese correo.";

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            int idUsuario = insertarUsuario(conn, u, password);

            String sqlVend = "INSERT INTO vendedores (id_vendedor, cuenta_bancaria, contrato_aceptado, validado) "
                           + "VALUES (?, ?, ?, FALSE)";
            try (PreparedStatement ps = conn.prepareStatement(sqlVend)) {
                ps.setInt(1, idUsuario);
                ps.setString(2, cuentaBancaria);
                ps.setBoolean(3, contratoAceptado);
                ps.executeUpdate();
            }

            String token = generarToken();
            guardarToken(conn, idUsuario, token);

            conn.commit();

            mostrarTokenSimulado(u.getCorreo(), token);

            return "Registro de vendedor exitoso. Ingresa el código que apareció en pantalla.";

        } catch (Exception e) {
            rollback(conn);
            return "Error al registrar vendedor: " + e.getMessage();
        } finally {
            cerrar(conn);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RF003 — Confirmar correo (simulado con popup)
    // ─────────────────────────────────────────────────────────────────────────
    public String confirmarCorreo(String correo, String tokenIngresado) {
        String sql = "SELECT ce.id_confirmacion, ce.token, ce.confirmado "
                   + "FROM confirmaciones_email ce "
                   + "JOIN usuarios u ON ce.id_usuario = u.id_usuario "
                   + "WHERE u.correo = ? ORDER BY ce.fecha_envio DESC LIMIT 1";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())                  return "No se encontró un código para ese correo.";
            if (rs.getBoolean("confirmado")) return "Este correo ya fue confirmado anteriormente.";
            if (!rs.getString("token").equalsIgnoreCase(tokenIngresado))
                                             return "Código incorrecto. Verifica e intenta de nuevo.";

            int idConf = rs.getInt("id_confirmacion");

            String upConf = "UPDATE confirmaciones_email SET confirmado = TRUE WHERE id_confirmacion = ?";
            try (PreparedStatement up = conn.prepareStatement(upConf)) {
                up.setInt(1, idConf);
                up.executeUpdate();
            }

            String upUser = "UPDATE usuarios u "
                          + "JOIN confirmaciones_email ce ON ce.id_usuario = u.id_usuario "
                          + "SET u.estado = 'ACTIVO' WHERE u.correo = ?";
            try (PreparedStatement up = conn.prepareStatement(upUser)) {
                up.setString(1, correo);
                up.executeUpdate();
            }

            return "¡Cuenta confirmada! Ya puedes iniciar sesión.";

        } catch (SQLException e) {
            return "Error al confirmar: " + e.getMessage();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RF004 — Autenticar usuario
    // ─────────────────────────────────────────────────────────────────────────
    public Usuario autenticar(String correo, String password) {
        String sql = "SELECT u.id_usuario, u.nombre_completo, u.documento, u.correo, "
                   + "u.celular, u.direccion, u.id_rol, u.estado "
                   + "FROM usuarios u "
                   + "JOIN credenciales c ON c.id_usuario = u.id_usuario "
                   + "WHERE u.correo = ? AND c.password = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre_completo"),
                rs.getString("documento"),
                rs.getString("correo"),
                rs.getString("celular"),
                rs.getString("direccion"),
                rs.getInt("id_rol"),
                rs.getString("estado")
            );
        } catch (SQLException e) {
            System.err.println("Error autenticando: " + e.getMessage());
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RF005 — Validar identidad del vendedor
    // ─────────────────────────────────────────────────────────────────────────
    public String subirDocumentoVendedor(int idVendedor, String rutaArchivo) {
        String sql = "INSERT INTO documentos_vendedor (id_vendedor, archivo, estado) VALUES (?, ?, 'PENDIENTE')";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVendedor);
            ps.setString(2, rutaArchivo);
            ps.executeUpdate();
            return "Documento enviado. Será revisado por el administrador.";
        } catch (SQLException e) {
            return "Error al subir documento: " + e.getMessage();
        }
    }

    public String resolverDocumentoVendedor(int idDocumento, boolean aprobar) {
        String nuevoEstado = aprobar ? "APROBADO" : "RECHAZADO";
        String sqlDoc  = "UPDATE documentos_vendedor SET estado = ? WHERE id_documento = ?";
        String sqlVend = "UPDATE vendedores SET validado = TRUE "
                       + "WHERE id_vendedor = (SELECT id_vendedor FROM documentos_vendedor WHERE id_documento = ?)";

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlDoc)) {
                ps.setString(1, nuevoEstado);
                ps.setInt(2, idDocumento);
                ps.executeUpdate();
            }
            if (aprobar) {
                try (PreparedStatement ps = conn.prepareStatement(sqlVend)) {
                    ps.setInt(1, idDocumento);
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return aprobar ? "Vendedor validado correctamente." : "Documento rechazado.";

        } catch (SQLException e) {
            rollback(conn);
            return "Error al procesar documento: " + e.getMessage();
        } finally {
            cerrar(conn);
        }
    }

    public boolean vendedorHabilitado(int idVendedor) {
        String sql = "SELECT validado FROM vendedores WHERE id_vendedor = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVendedor);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getBoolean("validado");
        } catch (SQLException e) {
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers privados
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Simula el envío de correo mostrando el token en un popup.
     * En producción se reemplazaría por el envío real con JavaMail.
     */
    private void mostrarTokenSimulado(String correo, String token) {
        JOptionPane.showMessageDialog(
            null,
            "📧  Simulación de correo electrónico\n\n"
            + "Para: " + correo + "\n"
            + "Asunto: Confirma tu cuenta en MercadoRed\n\n"
            + "Tu código de confirmación es:\n\n"
            + "          " + token + "\n\n"
            + "Cópialo e ingrésalo en la pantalla de confirmación.",
            "Código de confirmación",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String generarToken() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private int insertarUsuario(Connection conn, Usuario u, String password) throws SQLException {
        String sqlU = "INSERT INTO usuarios (nombre_completo, documento, correo, celular, direccion, id_rol, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, 'PENDIENTE')";
        try (PreparedStatement ps = conn.prepareStatement(sqlU, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getDocumento());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getCelular());
            ps.setString(5, u.getDireccion());
            ps.setInt(6, u.getIdRol());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (!keys.next()) throw new SQLException("No se obtuvo el ID del usuario.");
            int idUsuario = keys.getInt(1);

            String sqlCred = "INSERT INTO credenciales (id_usuario, password) VALUES (?, ?)";
            try (PreparedStatement psCred = conn.prepareStatement(sqlCred)) {
                psCred.setInt(1, idUsuario);
                psCred.setString(2, password);
                psCred.executeUpdate();
            }
            return idUsuario;
        }
    }

    private void guardarToken(Connection conn, int idUsuario, String token) throws SQLException {
        String sql = "INSERT INTO confirmaciones_email (id_usuario, token) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, token);
            ps.executeUpdate();
        }
    }

    private boolean existeDocumento(String doc)   { return existeCampo("documento", doc); }
    private boolean existeCorreo(String correo)    { return existeCampo("correo", correo); }

    private boolean existeCampo(String campo, String valor) {
        String sql = "SELECT 1 FROM usuarios WHERE " + campo + " = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, valor);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    private void rollback(Connection conn) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
    }

    private void cerrar(Connection conn) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
}
