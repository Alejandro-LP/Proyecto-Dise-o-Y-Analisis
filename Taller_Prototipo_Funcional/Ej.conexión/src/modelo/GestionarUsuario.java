package modelo;

import java.sql.SQLException;

public class GestionarUsuario {

    public String registrar(Usuario usuario) {
        String mensaje = "";
        ConexionBD conx = new ConexionBD();

        try {
            String consulta = "SELECT id_usuario FROM usuarios WHERE documento = ? OR correo = ?";
            var psValidar = conx.getConexion().prepareStatement(consulta);
            psValidar.setString(1, usuario.getDocumento());
            psValidar.setString(2, usuario.getCorreo());

            var rs = psValidar.executeQuery();

            if (rs.next()) {
                return "Error: El documento o correo ya existen.";
            }

            String inst = "INSERT INTO usuarios (nombre_completo, documento, correo, celular, direccion, id_rol, estado) VALUES (?,?,?,?,?,?,?)";

            var ps = conx.getConexion().prepareStatement(inst);
            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, usuario.getDocumento());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getDireccion());
            ps.setInt(6, usuario.getIdRol());
            ps.setString(7, usuario.getEstado());

            ps.executeUpdate();

            mensaje = "Registro exitoso. Pendiente de confirmación.";
            conx.getConexion().close();

        } catch (Exception e) {
            mensaje = "Error: " + e.getMessage();
        }

        return mensaje;
    }
}
