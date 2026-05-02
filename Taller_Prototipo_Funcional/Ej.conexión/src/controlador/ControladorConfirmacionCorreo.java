package controlador;

import modelo.GestionarUsuario;
import vista.PanelConfirmacionCorreo;

/**
 * RF003 — Confirmar correo electrónico.
 * CA007: el token fue enviado al correo en el registro (ServicioEmail).
 * CA008: sin confirmación la cuenta queda PENDIENTE y no puede operar.
 */
public class ControladorConfirmacionCorreo {

    private final PanelConfirmacionCorreo vista;
    private final GestionarUsuario modelo;
    private final ControladorPrincipal principal;

    public ControladorConfirmacionCorreo(PanelConfirmacionCorreo vista, ControladorPrincipal principal) {
        this.vista = vista;
        this.modelo = new GestionarUsuario();
        this.principal = principal;

        vista.btnConfirmar.addActionListener(e -> confirmar());
        vista.btnVolver.addActionListener(e -> principal.mostrarInicio());
    }

    private void confirmar() {
        String correo = vista.getCorreo();
        String token  = vista.getToken();

        if (correo.isEmpty() || token.isEmpty()) {
            vista.mostrarMensaje("Ingresa tu correo y el código recibido.", true);
            return;
        }

        String resultado = modelo.confirmarCorreo(correo, token);
        boolean exito = resultado.startsWith("¡Correo confirmado!");
        vista.mostrarMensaje(resultado, !exito);

        if (exito) {
            // Redirigir al login tras confirmación exitosa
            principal.mostrarLogin();
        }
    }
}
