package controlador;

import modelo.GestionarUsuario;
import modelo.Usuario;
import vista.PanelLogin;

import javax.swing.*;

/**
 * RF004 — Autenticar usuario.
 * CA009: solo usuarios registrados acceden.
 * CA010: mensaje informativo si las credenciales son incorrectas.
 */
public class ControladorLogin {

    private final PanelLogin vista;
    private final GestionarUsuario modelo;
    private final ControladorPrincipal principal;

    public ControladorLogin(PanelLogin vista, ControladorPrincipal principal) {
        this.vista = vista;
        this.modelo = new GestionarUsuario();
        this.principal = principal;

        vista.btnIngresar.addActionListener(e -> autenticar());
        vista.btnVolver.addActionListener(e -> principal.mostrarInicio());
    }

    private void autenticar() {
        String correo   = vista.getCorreo();
        String password = vista.getPassword();

        if (correo.isEmpty() || password.isEmpty()) {
            vista.mostrarMensaje("Ingresa tu correo y contraseña.", true);
            return;
        }

        Usuario usuario = modelo.autenticar(correo, password);

        if (usuario == null) {
            // CA010: mensaje informativo
            vista.mostrarMensaje("Credenciales incorrectas. Verifica tu correo y contraseña.", true);
            return;
        }

        // CA008 (RF003): correo no confirmado → estado PENDIENTE
        if ("PENDIENTE".equals(usuario.getEstado())) {
            vista.mostrarMensaje("Debes confirmar tu correo antes de ingresar.", true);
            int opc = JOptionPane.showConfirmDialog(vista,
                    "Tu cuenta está pendiente de confirmación.\n¿Ir a confirmar correo ahora?",
                    "Confirmar correo", JOptionPane.YES_NO_OPTION);
            if (opc == JOptionPane.YES_OPTION) {
                principal.mostrarConfirmacionCorreo(correo);
            }
            return;
        }

        if ("BLOQUEADO".equals(usuario.getEstado())) {
            vista.mostrarMensaje("Tu cuenta está bloqueada. Contacta al soporte.", true);
            return;
        }

        // Acceso exitoso → redirigir según rol
        principal.sesionIniciada(usuario);
    }
}
