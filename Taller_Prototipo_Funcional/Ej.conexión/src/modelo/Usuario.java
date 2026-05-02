package modelo;

public class Usuario {

    private int idUsuario;
    private String nombreCompleto;
    private String documento;
    private String correo;
    private String celular;
    private String direccion;
    private int idRol;
    private String estado;

    // Constructor para registro (sin id)
    public Usuario(String nombreCompleto, String documento, String correo,
            String celular, String direccion, int idRol, String estado) {
        this.nombreCompleto = nombreCompleto;
        this.documento = documento;
        this.correo = correo;
        this.celular = celular;
        this.direccion = direccion;
        this.idRol = idRol;
        this.estado = estado;
    }

    // Constructor completo (con id, para login/consultas)
    public Usuario(int idUsuario, String nombreCompleto, String documento, String correo,
            String celular, String direccion, int idRol, String estado) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.documento = documento;
        this.correo = correo;
        this.celular = celular;
        this.direccion = direccion;
        this.idRol = idRol;
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDocumento() {
        return documento;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCelular() {
        return celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
