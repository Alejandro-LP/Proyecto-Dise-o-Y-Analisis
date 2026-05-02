package controlador;

import modelo.GestionarCarrito;
import modelo.ItemCarrito;
import modelo.Usuario;
import vista.PanelCarrito;
import vista.PanelCheckout;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class ControladorCarrito {

    private final PanelCarrito panelCarrito;
    private final PanelCheckout panelCheckout;
    private final GestionarCarrito modelo;
    private final ControladorPrincipal principal;
    private final Usuario usuarioSesion;

    private Map<String, Integer> metodosPago;

    public ControladorCarrito(PanelCarrito panelCarrito,
            PanelCheckout panelCheckout,
            ControladorPrincipal principal,
            Usuario usuarioSesion) {
        this.panelCarrito = panelCarrito;
        this.panelCheckout = panelCheckout;
        this.principal = principal;
        this.usuarioSesion = usuarioSesion;
        this.modelo = new GestionarCarrito();

        metodosPago = modelo.obtenerMetodosPago();
        panelCheckout.cargarMetodosPago(metodosPago);
        panelCheckout.precargarDireccion(usuarioSesion.getDireccion());

        conectarEventos();
    }

    private void conectarEventos() {

        panelCarrito.setOnCambiarCantidad((idItem, nuevaCant) -> {
            String res = modelo.modificarCantidad(idItem, nuevaCant);
            if (res.startsWith("Stock")) {
                JOptionPane.showMessageDialog(null, res, "Stock insuficiente",
                        JOptionPane.WARNING_MESSAGE);
            }
            refrescarCarrito();
        });

        panelCarrito.setOnEliminar(idItem -> {
            int conf = JOptionPane.showConfirmDialog(null,
                    "¿Quitar este producto del carrito?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                modelo.eliminarItem(idItem);
                refrescarCarrito();
            }
        });

        panelCarrito.btnProceder.addActionListener(e -> {
            List<ItemCarrito> items = modelo.obtenerItems(usuarioSesion.getIdUsuario());
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El carrito está vacío.");
                return;
            }
            double total = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
            panelCheckout.setTotal(total);
            principal.mostrarCheckout();
        });

        panelCarrito.btnVolver.addActionListener(e -> principal.mostrarCatalogo());

        panelCheckout.btnConfirmar.addActionListener(e -> confirmarCompra());
        panelCheckout.btnVolver.addActionListener(e -> principal.mostrarCarrito());
    }

    private void confirmarCompra() {
        String direccion = panelCheckout.getDireccion();
        String metodoPago = panelCheckout.getMetodoPago();

        if (direccion.isEmpty()) {
            panelCheckout.mostrarMensaje("Ingresa la dirección de envío.", true);
            return;
        }

        if (metodoPago == null || metodoPago.startsWith("--")) {
            panelCheckout.mostrarMensaje("Selecciona un método de pago.", true);
            return;
        }

        int idMetodo = metodosPago.getOrDefault(metodoPago, -1);
        String resultado = modelo.confirmarCompra(
                usuarioSesion.getIdUsuario(), direccion, idMetodo);

        if (resultado.startsWith("OK:")) {
            String msg = resultado.substring(3);
            JOptionPane.showMessageDialog(null, "✅ " + msg,
                    "¡Compra exitosa!", JOptionPane.INFORMATION_MESSAGE);
            refrescarCarrito();
            principal.mostrarCatalogo();
        } else if (resultado.startsWith("CA028:") || resultado.startsWith("CA030:")) {
            panelCheckout.mostrarMensaje(resultado.substring(6), true);
        } else {
            panelCheckout.mostrarMensaje(resultado, true);
        }
    }

    public void agregarAlCarrito(int idPublicacion, int cantidad) {
        String res = modelo.agregarProducto(
                usuarioSesion.getIdUsuario(), idPublicacion, cantidad);
        boolean exito = res.equals("Producto agregado al carrito.");
        JOptionPane.showMessageDialog(null, res,
                exito ? "Carrito" : "Error",
                exito ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        if (exito) {
            refrescarCarrito();
        }
    }

    public void refrescarCarrito() {
        List<ItemCarrito> items = modelo.obtenerItems(usuarioSesion.getIdUsuario());
        panelCarrito.cargarItems(items);
    }
}
