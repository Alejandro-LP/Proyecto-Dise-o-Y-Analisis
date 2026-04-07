package controlador;

import modelo.Usuario;
import modelo.GestionarUsuario;

public class ControladorUsuario {

    private GestionarUsuario modelo;

    public ControladorUsuario() {
        modelo = new GestionarUsuario();
    }

    public String registrar(String nombre, String documento, String correo,
                            String celular, String direccion, int idRol) {

      
        if (nombre.isEmpty() || documento.isEmpty() || correo.isEmpty()
                || celular.isEmpty() || direccion.isEmpty()) {
            return "Error: Todos los campos son obligatorios.";
        }

       
        Usuario usuario = new Usuario(
                nombre,
                documento,
                correo,
                celular,
                direccion,
                idRol,
                "PENDIENTE"
        );

       
        return modelo.registrar(usuario);
    }
}