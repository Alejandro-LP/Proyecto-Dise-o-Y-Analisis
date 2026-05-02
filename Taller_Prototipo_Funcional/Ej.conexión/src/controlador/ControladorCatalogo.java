package controlador;

import modelo.GestionarCatalogo;
import modelo.GestionarPublicacion;
import modelo.Publicacion;
import vista.PanelCatalogo;
import vista.PanelDetallePublicacion;

import java.util.List;
import java.util.Map;

public class ControladorCatalogo {

    private final PanelCatalogo panelCatalogo;
    private final PanelDetallePublicacion panelDetalle;
    private final GestionarCatalogo modelo;
    private final GestionarPublicacion modeloPub;
    private final ControladorPrincipal principal;
    private final Map<String, Integer> categorias;

    public ControladorCatalogo(PanelCatalogo panelCatalogo,
            PanelDetallePublicacion panelDetalle,
            ControladorPrincipal principal) {
        this.panelCatalogo = panelCatalogo;
        this.panelDetalle = panelDetalle;
        this.principal = principal;
        this.modelo = new GestionarCatalogo();
        this.modeloPub = new GestionarPublicacion();

        categorias = modeloPub.obtenerCategorias();
        panelCatalogo.cargarCategorias(categorias);

        panelDetalle.setModoComprador();

        conectarEventos();
        buscar("", "", "TODOS", 0, 0);
    }

    private void conectarEventos() {
        // RF009 — búsqueda por texto
        panelCatalogo.btnBuscar.addActionListener(e -> ejecutarBusqueda());

        // Enter en la barra de búsqueda
        panelCatalogo.txtBusqueda.addActionListener(e -> ejecutarBusqueda());

        // RF010 — aplicar filtros
        panelCatalogo.btnFiltrar.addActionListener(e -> ejecutarBusqueda());

        // Limpiar filtros
        panelCatalogo.btnLimpiar.addActionListener(e -> {
            panelCatalogo.limpiarFiltros();
            buscar("", "", "TODOS", 0, 0);
        });

        // Click en tarjeta → ver detalle
        panelCatalogo.setOnVerDetalle(() -> {
            int id = panelCatalogo.getIdPublicacionSeleccionada();
            if (id < 0) {
                return;
            }
            Publicacion p = modeloPub.consultarDetalle(id);
            if (p != null) {
                panelDetalle.cargarPublicacion(p);
                principal.mostrarDetalleCatalogo();
            }
        });

        // Botón agregar al carrito desde el detalle
        panelDetalle.btnAgregarCarrito.addActionListener(e -> {
            int id = panelDetalle.getIdPublicacionActual();
            int cant = panelDetalle.getCantidadSeleccionada();
            if (id > 0) {
                principal.agregarAlCarritoDesdeDetalle(id, cant);
            }
        });

        // Botón ir al carrito desde el catálogo
        panelCatalogo.btnCarrito.addActionListener(e -> principal.mostrarCarrito());

        // Volver desde detalle al catálogo
        panelDetalle.btnVolver.addActionListener(e -> principal.mostrarCatalogo());

        // Volver desde catálogo al inicio
        panelCatalogo.btnVolver.addActionListener(e -> principal.mostrarDashboardComprador());
    }

    private void ejecutarBusqueda() {
        buscar(
                panelCatalogo.getTextoBusqueda(),
                panelCatalogo.getCategoriaSeleccionada(),
                panelCatalogo.getEstadoSeleccionado(),
                panelCatalogo.getPrecioMin(),
                panelCatalogo.getPrecioMax()
        );
    }

    // CA020, CA021, CA022, CA023
    private void buscar(String texto, String categoria, String estado,
            double precioMin, double precioMax) {
        int idCategoria = (categoria == null || categoria.isEmpty())
                ? -1
                : categorias.getOrDefault(categoria, -1);

        List<Publicacion> resultados = modelo.buscarProductos(
                texto, idCategoria, estado, precioMin, precioMax);

        panelCatalogo.mostrarResultados(resultados);
    }
}
