package controlador;

import modelo.Usuario;
import vista.*;

import javax.swing.*;
import java.awt.*;

public class ControladorPrincipal {

    // ── Constantes de navegación ────────────────────────────────
    private static final String PANEL_INICIO = "INICIO";
    private static final String PANEL_LOGIN = "LOGIN";
    private static final String PANEL_SELECCION_REGISTRO = "SELECCION_REGISTRO";
    private static final String PANEL_REGISTRO_COMPRADOR = "REGISTRO_COMPRADOR";
    private static final String PANEL_REGISTRO_VENDEDOR = "REGISTRO_VENDEDOR";
    private static final String PANEL_CONFIRMACION_CORREO = "CONFIRMACION_CORREO";
    private static final String PANEL_MIS_PUBLICACIONES = "MIS_PUBLICACIONES";
    private static final String PANEL_CREAR_PUBLICACION = "CREAR_PUBLICACION";
    private static final String PANEL_DETALLE_PUBLICACION = "DETALLE_PUBLICACION";
    private static final String PANEL_CATALOGO = "CATALOGO";
    private static final String PANEL_DETALLE_CATALOGO = "DETALLE_CATALOGO";
    private static final String PANEL_CARRITO = "CARRITO";
    private static final String PANEL_CHECKOUT = "CHECKOUT";

    // ── Ventana y layout ─────────────────────────────────────────
    private final JFrame ventana;
    private final JPanel contenedor;
    private final CardLayout cardLayout;

    // ── Vistas ───────────────────────────────────────────────────
    private PanelInicio panelInicio;
    private PanelLogin panelLogin;
    private PanelSeleccionRegistro panelSeleccionRegistro;
    private PanelRegistroComprador panelRegistroComprador;
    private PanelRegistroVendedor panelRegistroVendedor;
    private PanelConfirmacionCorreo panelConfirmacionCorreo;
    private PanelMisPublicaciones panelMisPublicaciones;
    private PanelCrearPublicacion panelCrearPublicacion;
    private PanelDetallePublicacion panelDetallePublicacion; // modo vendedor
    private PanelCatalogo panelCatalogo;
    private PanelDetallePublicacion panelDetalleCatalogo;    // modo comprador
    private PanelCarrito panelCarrito;
    private PanelCheckout panelCheckout;

    // ── Controladores de módulo ───────────────────────────────────
    private ControladorPublicaciones controladorPublicaciones;
    private ControladorCatalogo controladorCatalogo;
    private ControladorCarrito controladorCarrito;

    // ── Constructor ──────────────────────────────────────────────
    public ControladorPrincipal(JFrame ventana) {
        this.ventana = ventana;
        this.cardLayout = new CardLayout();
        this.contenedor = new JPanel(cardLayout);

        inicializarPaneles();
        inicializarControladores();

        ventana.setContentPane(contenedor);
        ventana.setSize(900, 680);
        ventana.setMinimumSize(new Dimension(800, 600));
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);

        mostrarInicio();
    }

    // ── Inicializar paneles ──────────────────────────────────────
    private void inicializarPaneles() {
        panelInicio = new PanelInicio();
        panelLogin = new PanelLogin();
        panelSeleccionRegistro = new PanelSeleccionRegistro();
        panelRegistroComprador = new PanelRegistroComprador();
        panelRegistroVendedor = new PanelRegistroVendedor();
        panelConfirmacionCorreo = new PanelConfirmacionCorreo();
        panelMisPublicaciones = new PanelMisPublicaciones();
        panelCrearPublicacion = new PanelCrearPublicacion();
        panelDetallePublicacion = new PanelDetallePublicacion();
        panelCatalogo = new PanelCatalogo();
        panelDetalleCatalogo = new PanelDetallePublicacion();
        panelCarrito = new PanelCarrito();
        panelCheckout = new PanelCheckout();

        contenedor.add(panelInicio, PANEL_INICIO);
        contenedor.add(panelLogin, PANEL_LOGIN);
        contenedor.add(panelSeleccionRegistro, PANEL_SELECCION_REGISTRO);
        contenedor.add(panelRegistroComprador, PANEL_REGISTRO_COMPRADOR);
        contenedor.add(panelRegistroVendedor, PANEL_REGISTRO_VENDEDOR);
        contenedor.add(panelConfirmacionCorreo, PANEL_CONFIRMACION_CORREO);
        contenedor.add(panelMisPublicaciones, PANEL_MIS_PUBLICACIONES);
        contenedor.add(panelCrearPublicacion, PANEL_CREAR_PUBLICACION);
        contenedor.add(panelDetallePublicacion, PANEL_DETALLE_PUBLICACION);
        contenedor.add(panelCatalogo, PANEL_CATALOGO);
        contenedor.add(panelDetalleCatalogo, PANEL_DETALLE_CATALOGO);
        contenedor.add(panelCarrito, PANEL_CARRITO);
        contenedor.add(panelCheckout, PANEL_CHECKOUT);
    }

    // ── Inicializar controladores base ───────────────────────────
    private void inicializarControladores() {
        panelInicio.btnLogin.addActionListener(e -> mostrarLogin());
        panelInicio.btnRegistro.addActionListener(e -> mostrarSeleccionRegistro());

        panelSeleccionRegistro.btnComprador.addActionListener(e -> mostrarRegistroComprador());
        panelSeleccionRegistro.btnVendedor.addActionListener(e -> mostrarRegistroVendedor());
        panelSeleccionRegistro.btnVolver.addActionListener(e -> mostrarInicio());

        new ControladorLogin(panelLogin, this);
        new ControladorRegistroComprador(panelRegistroComprador, this);
        new ControladorRegistroVendedor(panelRegistroVendedor, this);
        new ControladorConfirmacionCorreo(panelConfirmacionCorreo, this);

        // Modo inicial de los paneles de detalle
        panelDetallePublicacion.setModoVendedor();
        panelDetalleCatalogo.setModoComprador();
    }

    // ── Cambio de panel con refresco ─────────────────────────────
    private void cambiarPanel(String nombrePanel) {
        cardLayout.show(contenedor, nombrePanel);
        contenedor.setVisible(false);
        contenedor.setVisible(true);
        contenedor.revalidate();
        contenedor.repaint();
        ventana.revalidate();
        ventana.repaint();
    }

    // ════════════════════════════════════════════════════════════
    // NAVEGACIÓN
    // ════════════════════════════════════════════════════════════
    public void mostrarInicio() {
        cambiarPanel(PANEL_INICIO);
        ventana.setTitle("MercadoRed");
    }

    public void mostrarLogin() {
        cambiarPanel(PANEL_LOGIN);
        ventana.setTitle("MercadoRed — Iniciar sesión");
    }

    public void mostrarSeleccionRegistro() {
        cambiarPanel(PANEL_SELECCION_REGISTRO);
        ventana.setTitle("MercadoRed — Registro");
    }

    public void mostrarRegistroComprador() {
        cambiarPanel(PANEL_REGISTRO_COMPRADOR);
        ventana.setTitle("MercadoRed — Registro de comprador");
    }

    public void mostrarRegistroVendedor() {
        cambiarPanel(PANEL_REGISTRO_VENDEDOR);
        ventana.setTitle("MercadoRed — Registro de vendedor");
    }

    public void mostrarConfirmacionCorreo(String correoPreCargado) {
        panelConfirmacionCorreo.precargarCorreo(correoPreCargado);
        cambiarPanel(PANEL_CONFIRMACION_CORREO);
        ventana.setTitle("MercadoRed — Confirmar correo");
    }

    // ── Vendedor ─────────────────────────────────────────────────
    public void mostrarMisPublicaciones() {
        if (controladorPublicaciones != null) {
            controladorPublicaciones.refrescarLista();
        }
        cambiarPanel(PANEL_MIS_PUBLICACIONES);
        ventana.setTitle("MercadoRed — Mis publicaciones");
    }

    public void mostrarCrearPublicacion() {
        cambiarPanel(PANEL_CREAR_PUBLICACION);
        ventana.setTitle("MercadoRed — Nueva publicación");
    }

    public void mostrarDetallePublicacion() {
        cambiarPanel(PANEL_DETALLE_PUBLICACION);
        ventana.setTitle("MercadoRed — Detalle de publicación");
    }

    public void mostrarDashboardVendedor() {
        mostrarMisPublicaciones();
    }

    // ── Comprador ─────────────────────────────────────────────────
    public void mostrarCatalogo() {
        cambiarPanel(PANEL_CATALOGO);
        ventana.setTitle("MercadoRed — Catálogo");
    }

    public void mostrarDetalleCatalogo() {
        cambiarPanel(PANEL_DETALLE_CATALOGO);
        ventana.setTitle("MercadoRed — Detalle del producto");
    }

    public void mostrarCarrito() {
        if (controladorCarrito != null) {
            controladorCarrito.refrescarCarrito();
        }
        cambiarPanel(PANEL_CARRITO);
        ventana.setTitle("MercadoRed — Mi carrito");
    }

    public void mostrarCheckout() {
        cambiarPanel(PANEL_CHECKOUT);
        ventana.setTitle("MercadoRed — Finalizar compra");
    }

    public void mostrarDashboardComprador() {
        mostrarCatalogo();
    }

    // ── Agregar al carrito desde detalle del catálogo ────────────
    public void agregarAlCarritoDesdeDetalle(int idPublicacion, int cantidad) {
        if (controladorCarrito != null) {
            controladorCarrito.agregarAlCarrito(idPublicacion, cantidad);
        }
    }

    // ════════════════════════════════════════════════════════════
    // SESIÓN INICIADA
    // ════════════════════════════════════════════════════════════
    public void sesionIniciada(Usuario usuario) {
        JOptionPane.showMessageDialog(ventana,
                "¡Bienvenido, " + usuario.getNombreCompleto() + "!\n"
                + "Rol: " + (usuario.getIdRol() == 1 ? "Comprador"
                : usuario.getIdRol() == 2 ? "Vendedor"
                : "Administrador"),
                "Acceso exitoso",
                JOptionPane.INFORMATION_MESSAGE);

        if (usuario.getIdRol() == 1) {
            // Comprador
            iniciarModuloCatalogo();
            iniciarModuloCarrito(usuario);
            mostrarCatalogo();

        } else if (usuario.getIdRol() == 2) {
            // Vendedor
            iniciarModuloPublicaciones(usuario);
            mostrarMisPublicaciones();
        }
    }

    // ════════════════════════════════════════════════════════════
    // INICIALIZACIÓN DE MÓDULOS
    // ════════════════════════════════════════════════════════════
    private void iniciarModuloPublicaciones(Usuario usuario) {
        controladorPublicaciones = new ControladorPublicaciones(
                panelMisPublicaciones,
                panelCrearPublicacion,
                panelDetallePublicacion,
                this,
                usuario);
    }

    private void iniciarModuloCatalogo() {
        controladorCatalogo = new ControladorCatalogo(
                panelCatalogo,
                panelDetalleCatalogo,
                this);
    }

    private void iniciarModuloCarrito(Usuario usuario) {
        controladorCarrito = new ControladorCarrito(
                panelCarrito,
                panelCheckout,
                this,
                usuario);
    }
}
