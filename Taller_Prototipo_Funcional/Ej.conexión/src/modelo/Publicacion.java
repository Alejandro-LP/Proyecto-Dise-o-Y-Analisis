package modelo;

public class Publicacion {

    private int idPublicacion;
    private int idVendedor;
    private int idCategoria;
    private String titulo;
    private String descripcion;
    private double precio;
    private int stock;
    private String estadoProducto; // "NUEVO" o "USADO"
    private String politicasDevolucion;
    private String rutaImagen;
    private String nombreCategoria;

    // Constructor para crear nueva publicación
    public Publicacion(int idVendedor, int idCategoria, String titulo,
            String descripcion, double precio, int stock,
            String estadoProducto, String politicasDevolucion,
            String rutaImagen) {
        this.idVendedor = idVendedor;
        this.idCategoria = idCategoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.estadoProducto = estadoProducto;
        this.politicasDevolucion = politicasDevolucion;
        this.rutaImagen = rutaImagen;
    }

    // Constructor completo (para consultas)
    public Publicacion(int idPublicacion, int idVendedor, int idCategoria,
            String titulo, String descripcion, double precio,
            int stock, String estadoProducto,
            String politicasDevolucion, String rutaImagen,
            String nombreCategoria) {
        this.idPublicacion = idPublicacion;
        this.idVendedor = idVendedor;
        this.idCategoria = idCategoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.estadoProducto = estadoProducto;
        this.politicasDevolucion = politicasDevolucion;
        this.rutaImagen = rutaImagen;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getEstadoProducto() {
        return estadoProducto;
    }

    public String getPoliticasDevolucion() {
        return politicasDevolucion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setEstadoProducto(String estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public void setPoliticasDevolucion(String p) {
        this.politicasDevolucion = p;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
