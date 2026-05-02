package controlador;

import modelo.GestionarUsuario;
import modelo.Usuario;
import vista.PanelRegistroComprador;

/**
 * RF001 — Registrar comprador.
 * CA001: valida campos obligatorios.
 * CA002: GestionarUsuario verifica duplicados.
 * CA003: GestionarUsuario envía token y deja cuenta PENDIENTE.
 */
public class ControladorRegistroComprador {

    private final PanelRegistroComprador vista;
    private final GestionarUsuario modelo;
    private final ControladorPrincipal principal;

    public ControladorRegistroComprador(PanelRegistroComprador vista, ControladorPrincipal principal) {
        this.vista = vista;
        this.modelo = new GestionarUsuario();
        this.principal = principal;

        vista.btnRegistrar.addActionListener(e -> registrar());
        vista.btnVolver.addActionListener(e -> principal.mostrarSeleccionRegistro());
    }

    private void registrar() {
        String nombre   = vista.getNombre();
        String doc      = vista.getDocumento();
        String correo   = vista.getCorreo();
        String celular  = vista.getCelular();
        String dir      = vista.getDireccion();
        String pass     = vista.getPassword();
        String passConf = vista.getConfirmPassword();

        // CA001: campos obligatorios
        if (nombre.isEmpty() || doc.isEmpty() || correo.isEmpty()
                || celular.isEmpty() || dir.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Todos los campos marcados con (*) son obligatorios.", true);
            return;
        }

        if (!pass.equals(passConf)) {
            vista.mostrarMensaje("Las contraseñas no coinciden.", true);
            return;
        }

        // id_rol 1 = COMPRADOR
        Usuario usuario = new Usuario(nombre, doc, correo, celular, dir, 1, "PENDIENTE");
        String resultado = modelo.registrarComprador(usuario, pass);

        boolean exito = resultado.startsWith("Registro exitoso");
        vista.mostrarMensaje(resultado, !exito);

        if (exito) {
            // Ir directo a confirmar correo (CA003)
            principal.mostrarConfirmacionCorreo(correo);
        }
    }
}
