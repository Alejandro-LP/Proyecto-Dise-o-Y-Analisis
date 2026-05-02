package controlador;

import modelo.GestionarUsuario;
import modelo.Usuario;
import vista.PanelRegistroVendedor;

/**
 * RF002 — Registrar vendedor.
 * CA004: valida datos personales + cuenta bancaria.
 * CA005: verifica aceptación del contrato.
 * CA006: GestionarUsuario deja validado = false.
 */
public class ControladorRegistroVendedor {

    private final PanelRegistroVendedor vista;
    private final GestionarUsuario modelo;
    private final ControladorPrincipal principal;

    public ControladorRegistroVendedor(PanelRegistroVendedor vista, ControladorPrincipal principal) {
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
        String cuenta   = vista.getCuentaBancaria();
        String pass     = vista.getPassword();
        String passConf = vista.getConfirmPassword();
        boolean contrato = vista.contratoAceptado();

        // CA004: datos obligatorios
        if (nombre.isEmpty() || doc.isEmpty() || correo.isEmpty()
                || celular.isEmpty() || dir.isEmpty() || cuenta.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Todos los campos marcados con (*) son obligatorios.", true);
            return;
        }

        if (!pass.equals(passConf)) {
            vista.mostrarMensaje("Las contraseñas no coinciden.", true);
            return;
        }

        // CA005: contrato obligatorio
        if (!contrato) {
            vista.mostrarMensaje("Debes aceptar el contrato de comisión para continuar.", true);
            return;
        }

        // id_rol 2 = VENDEDOR
        Usuario usuario = new Usuario(nombre, doc, correo, celular, dir, 2, "PENDIENTE");
        String resultado = modelo.registrarVendedor(usuario, pass, cuenta, contrato);

        boolean exito = resultado.startsWith("Registro de vendedor exitoso");
        vista.mostrarMensaje(resultado, !exito);

        if (exito) {
            principal.mostrarConfirmacionCorreo(correo);
        }
    }
}
