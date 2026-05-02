package controlador;

import modelo.GestionarPublicacion;
import modelo.Publicacion;
import modelo.Usuario;
import vista.PanelCrearPublicacion;
import vista.PanelDetallePublicacion;
import vista.PanelMisPublicaciones;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class ControladorPublicaciones {

    private final PanelMisPublicaciones panelLista;
    private final PanelCrearPublicacion panelCrear;
    private final PanelDetallePublicacion panelDetalle;
    private final GestionarPublicacion modelo;
    private final ControladorPrincipal principal;
    private final Usuario usuarioSesion;

    private Map<String, Integer> categorias;
    private int idPublicacionActual = -1;

    public ControladorPublicaciones(PanelMisPublicaciones panelLista,
            PanelCrearPublicacion panelCrear,
            PanelDetallePublicacion panelDetalle,
            ControladorPrincipal principal,
            Usuario usuarioSesion) {
        this.panelLista = panelLista;
        this.panelCrear = panelCrear;
        this.panelDetalle = panelDetalle;
        this.principal = principal;
        this.usuarioSesion = usuarioSesion;
        this.modelo = new GestionarPublicacion();

        categorias = modelo.obtenerCategorias();
        panelCrear.cargarCategorias(categorias);

        conectarEventos();
    }

    private void conectarEventos() {
        // Lista
        panelLista.btnNueva.addActionListener(e -> {
            panelCrear.limpiar();
            idPublicacionActual = -1;
            principal.mostrarCrearPublicacion();
        });

        panelLista.btnVer.addActionListener(e -> {
            int id = panelLista.getIdSeleccionado();
            if (id < 0) {
                avisar("Selecciona una publicación primero.");
                return;
            }
            Publicacion p = modelo.consultarDetalle(id);
            if (p != null) {
                panelDetalle.cargarPublicacion(p);
                idPublicacionActual = id;
                principal.mostrarDetallePublicacion();
            }
        });

        panelLista.btnEditar.addActionListener(e -> {
            int id = panelLista.getIdSeleccionado();
            if (id < 0) {
                avisar("Selecciona una publicación primero.");
                return;
            }
            cargarParaEditar(id);
        });

        panelLista.btnVolver.addActionListener(e -> principal.mostrarDashboardVendedor());

        // Crear / Editar
        panelCrear.btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());

        panelCrear.btnGuardar.addActionListener(e -> {
            if (idPublicacionActual < 0) {
                guardarNueva();
            } else {
                guardarEdicion();
            }
        });

        panelCrear.btnVolver.addActionListener(e -> principal.mostrarMisPublicaciones());

        // Detalle
        panelDetalle.btnVolver.addActionListener(e -> principal.mostrarMisPublicaciones());
        panelDetalle.btnEditar.addActionListener(e -> {
            if (idPublicacionActual > 0) {
                cargarParaEditar(idPublicacionActual);
            }
        });
    }

    // RF006 — guardar nueva publicación
    private void guardarNueva() {
        if (!validar()) {
            return;
        }

        Publicacion p = new Publicacion(
                usuarioSesion.getIdUsuario(),
                categorias.get(panelCrear.getCategoria()),
                panelCrear.getTitulo(),
                panelCrear.getDescripcion(),
                Double.parseDouble(panelCrear.getPrecio()),
                Integer.parseInt(panelCrear.getStock()),
                panelCrear.getEstado(),
                panelCrear.getPoliticas(),
                panelCrear.getRutaImagen()
        );

        String resultado = modelo.crearPublicacion(p);
        boolean exito = resultado.startsWith("Publicación creada");
        panelCrear.mostrarMensaje(resultado, !exito);

        if (exito) {
            refrescarLista();
            principal.mostrarMisPublicaciones();
        }
    }

    // RF007 — guardar edición
    private void guardarEdicion() {
        if (!validar()) {
            return;
        }

        Publicacion p = new Publicacion(
                idPublicacionActual,
                usuarioSesion.getIdUsuario(),
                categorias.get(panelCrear.getCategoria()),
                panelCrear.getTitulo(),
                panelCrear.getDescripcion(),
                Double.parseDouble(panelCrear.getPrecio()),
                Integer.parseInt(panelCrear.getStock()),
                panelCrear.getEstado(),
                panelCrear.getPoliticas(),
                panelCrear.getRutaImagen(),
                panelCrear.getCategoria()
        );

        String resultado = modelo.actualizarPublicacion(p, usuarioSesion.getIdUsuario());
        boolean exito = resultado.startsWith("Publicación actualizada");
        panelCrear.mostrarMensaje(resultado, !exito);

        if (exito) {
            refrescarLista();
            principal.mostrarMisPublicaciones();
        }
    }

    private void cargarParaEditar(int idPublicacion) {
        Publicacion p = modelo.consultarDetalle(idPublicacion);
        if (p == null) {
            avisar("No se pudo cargar la publicación.");
            return;
        }

        idPublicacionActual = idPublicacion;
        panelCrear.txtTitulo.setText(p.getTitulo());
        panelCrear.txtDescripcion.setText(p.getDescripcion());
        panelCrear.txtPrecio.setText(String.valueOf(p.getPrecio()));
        panelCrear.txtStock.setText(String.valueOf(p.getStock()));
        panelCrear.cmbEstado.setSelectedItem(p.getEstadoProducto());
        panelCrear.txtPoliticas.setText(p.getPoliticasDevolucion());
        panelCrear.txtRutaImagen.setText(p.getRutaImagen() != null ? p.getRutaImagen() : "");
        panelCrear.btnGuardar.setText("Guardar cambios");

        // Seleccionar categoría en el combo
        for (int i = 0; i < panelCrear.cmbCategoria.getItemCount(); i++) {
            if (panelCrear.cmbCategoria.getItemAt(i).equals(p.getNombreCategoria())) {
                panelCrear.cmbCategoria.setSelectedIndex(i);
                break;
            }
        }
        principal.mostrarCrearPublicacion();
    }

    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        if (chooser.showOpenDialog(panelCrear) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            panelCrear.txtRutaImagen.setText(archivo.getAbsolutePath());
        }
    }

    private boolean validar() {
        // CA013
        if (panelCrear.getTitulo().isEmpty()
                || panelCrear.getDescripcion().isEmpty()
                || panelCrear.getPrecio().isEmpty()
                || panelCrear.getStock().isEmpty()) {
            panelCrear.mostrarMensaje("Completa todos los campos obligatorios (*).", true);
            return false;
        }
        try {
            double precio = Double.parseDouble(panelCrear.getPrecio());
            int stock = Integer.parseInt(panelCrear.getStock());
            if (precio <= 0 || stock < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            panelCrear.mostrarMensaje("Precio debe ser mayor a 0 y stock un número entero >= 0.", true);
            return false;
        }
        return true;
    }

    public void refrescarLista() {
        List<Publicacion> lista = modelo.listarPorVendedor(usuarioSesion.getIdUsuario());
        panelLista.cargarDatos(lista);
    }

    private void avisar(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
