package controlador;

import modelo.Usuario;
import modelo.GestionarUsuario;
import vista.Registro_usuarios;

import javax.swing.*;

public class ControladorRegistro {

    private Registro_usuarios vista;
    private GestionarUsuario modelo;

    public ControladorRegistro(Registro_usuarios vista) {
        this.vista = vista;
        this.modelo = new GestionarUsuario();

        // Evento del botón
        this.vista.btnRegistrar.addActionListener(e -> registrar());
    }

    private void registrar() {

        String nombre = vista.getNombre();
        String doc = vista.getDocumento();
        String correo = vista.getCorreo();
        String cel = vista.getCelular();
        String dir = vista.getDireccion();
        int rol = vista.getRol();

        // Validación lógica (CA001)
        if (nombre.isEmpty() || doc.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Campos obligatorios vacíos");
            return;
        }

        Usuario usuario = new Usuario(
                nombre, doc, correo, cel, dir, rol, "PENDIENTE"
        );

        String mensaje = modelo.registrar(usuario);

        JOptionPane.showMessageDialog(vista, mensaje);
    }
}