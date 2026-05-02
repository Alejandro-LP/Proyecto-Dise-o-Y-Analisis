package controlador;

import modelo.Usuario;
import vista.*;

import javax.swing.*;
import java.awt.*;

public class ControladorPrincipal {

    private static final String PANEL_INICIO = "INICIO";
    private static final String PANEL_LOGIN = "LOGIN";
    private static final String PANEL_SELECCION_REGISTRO = "SELECCION_REGISTRO";
    private static final String PANEL_REGISTRO_COMPRADOR = "REGISTRO_COMPRADOR";
    private static final String PANEL_REGISTRO_VENDEDOR = "REGISTRO_VENDEDOR";
    private static final String PANEL_CONFIRMACION_CORREO = "CONFIRMACION_CORREO";

    private final JFrame ventana;
    private final JPanel contenedor;
    private final CardLayout cardLayout;

    private PanelInicio panelInicio;
    private PanelLogin panelLogin;
    private PanelSeleccionRegistro panelSeleccionRegistro;
    private PanelRegistroComprador panelRegistroComprador;
    private PanelRegistroVendedor panelRegistroVendedor;
    private PanelConfirmacionCorreo panelConfirmacionCorreo;

    public ControladorPrincipal(JFrame ventana) {
        this.ventana = ventana;
        this.cardLayout = new CardLayout();
        this.contenedor = new JPanel(cardLayout);

        inicializarPaneles();
        inicializarControladores();

        ventana.setContentPane(contenedor);
        ventana.setSize(800, 600);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        mostrarInicio();
    }

    private void inicializarPaneles() {
        panelInicio = new PanelInicio();
        panelLogin = new PanelLogin();
        panelSeleccionRegistro = new PanelSeleccionRegistro();
        panelRegistroComprador = new PanelRegistroComprador();
        panelRegistroVendedor = new PanelRegistroVendedor();
        panelConfirmacionCorreo = new PanelConfirmacionCorreo();

        contenedor.add(panelInicio, PANEL_INICIO);
        contenedor.add(panelLogin, PANEL_LOGIN);
        contenedor.add(panelSeleccionRegistro, PANEL_SELECCION_REGISTRO);
        contenedor.add(panelRegistroComprador, PANEL_REGISTRO_COMPRADOR);
        contenedor.add(panelRegistroVendedor, PANEL_REGISTRO_VENDEDOR);
        contenedor.add(panelConfirmacionCorreo, PANEL_CONFIRMACION_CORREO);
    }

    private void inicializarControladores() {
        // Inicio
        panelInicio.btnLogin.addActionListener(e -> mostrarLogin());
        panelInicio.btnRegistro.addActionListener(e -> mostrarSeleccionRegistro());

        // Selección
        panelSeleccionRegistro.btnComprador.addActionListener(e -> mostrarRegistroComprador());
        panelSeleccionRegistro.btnVendedor.addActionListener(e -> mostrarRegistroVendedor());
        panelSeleccionRegistro.btnVolver.addActionListener(e -> mostrarInicio());

        // Subcontroladores
        new ControladorLogin(panelLogin, this);
        new ControladorRegistroComprador(panelRegistroComprador, this);
        new ControladorRegistroVendedor(panelRegistroVendedor, this);
        new ControladorConfirmacionCorreo(panelConfirmacionCorreo, this);
    }

    // 🔥 MÉTODO CLAVE (SOLUCIÓN REAL)
    private void cambiarPanel(String nombrePanel) {
        cardLayout.show(contenedor, nombrePanel);

        // Forzar refresco real
        contenedor.setVisible(false);
        contenedor.setVisible(true);

        contenedor.revalidate();
        contenedor.repaint();

        ventana.revalidate();
        ventana.repaint();
    }

    // ─── NAVEGACIÓN ─────────────────────────
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

    public void sesionIniciada(Usuario usuario) {
        JOptionPane.showMessageDialog(ventana,
                "¡Bienvenido, " + usuario.getNombreCompleto() + "!\n"
                + "Rol: " + (usuario.getIdRol() == 1 ? "Comprador"
                : usuario.getIdRol() == 2 ? "Vendedor" : "Administrador"),
                "Acceso exitoso",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
