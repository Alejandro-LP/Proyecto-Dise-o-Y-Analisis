package modelo;

public class ItemCarrito {

    private int idItem;
    private int idCarrito;
    private int idPublicacion;
    private int cantidad;
    private String tituloProducto;
    private double precioUnitario;
    private int stockDisponible;
    private String rutaImagen;

    public ItemCarrito(int idItem, int idCarrito, int idPublicacion,
            int cantidad, String tituloProducto,
            double precioUnitario, int stockDisponible,
            String rutaImagen) {
        this.idItem = idItem;
        this.idCarrito = idCarrito;
        this.idPublicacion = idPublicacion;
        this.cantidad = cantidad;
        this.tituloProducto = tituloProducto;
        this.precioUnitario = precioUnitario;
        this.stockDisponible = stockDisponible;
        this.rutaImagen = rutaImagen;
    }

    public int getIdItem() {
        return idItem;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getTituloProducto() {
        return tituloProducto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    public void setCantidad(int c) {
        this.cantidad = c;
    }
}
