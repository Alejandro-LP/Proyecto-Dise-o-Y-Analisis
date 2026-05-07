package vista;

import modelo.Publicacion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class PanelCatalogo extends JPanel {

    // Filtros
    public JTextField txtBusqueda;
    public JButton btnBuscar;
    public JComboBox<String> cmbCategoria;
    public JComboBox<String> cmbEstado;
    public JTextField txtPrecioMin;
    public JTextField txtPrecioMax;
    public JButton btnFiltrar;
    public JButton btnLimpiar;
    public JButton btnVolver;
    public JButton btnCarrito;

    // Resultados
    private JPanel panelResultados;
    private JLabel lblResultados;

    // Callback para ver detalle
    private Runnable onVerDetalle;
    private int idPublicacionSeleccionada = -1;

    public PanelCatalogo() {
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 247, 250));

        add(construirEncabezado(), BorderLayout.NORTH);
        add(construirCuerpo(), BorderLayout.CENTER);
    }

    // ── Encabezado con búsqueda ──────────────────────────────────
    private JPanel construirEncabezado() {
        JPanel enc = new JPanel(new BorderLayout(10, 8));
        enc.setBackground(new Color(52, 152, 219));
        enc.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("🛒  Catálogo de productos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        enc.add(titulo, BorderLayout.WEST);

        // Barra de búsqueda
        JPanel barraBusqueda = new JPanel(new BorderLayout(6, 0));
        barraBusqueda.setOpaque(false);

        txtBusqueda = new JTextField();
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBusqueda.setPreferredSize(new Dimension(280, 34));
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                new EmptyBorder(4, 10, 4, 10)
        ));
        txtBusqueda.putClientProperty("JTextField.placeholderText", "Buscar productos...");

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(39, 174, 96));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setPreferredSize(new Dimension(90, 34));

        barraBusqueda.add(txtBusqueda, BorderLayout.CENTER);
        barraBusqueda.add(btnBuscar, BorderLayout.EAST);
        enc.add(barraBusqueda, BorderLayout.EAST);

        btnCarrito = new JButton("🛒 Mi carrito");
        btnCarrito.setBackground(Color.WHITE);
        btnCarrito.setForeground(new Color(52, 152, 219));
        btnCarrito.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCarrito.setFocusPainted(false);
        btnCarrito.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCarrito.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        derecha.add(barraBusqueda);
        derecha.add(btnCarrito);
        enc.add(derecha, BorderLayout.EAST);

        return enc;
    }

    // ── Cuerpo: filtros izquierda + resultados derecha ────────────
    private JSplitPane construirCuerpo() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirPanelFiltros(), construirPanelResultados());
        split.setDividerLocation(220);
        split.setDividerSize(4);
        split.setBorder(null);
        return split;
    }

    // ── Panel de filtros ─────────────────────────────────────────
    private JPanel construirPanelFiltros() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 15, 20, 15));
        panel.setPreferredSize(new Dimension(220, 0));

        JLabel lblFiltros = new JLabel("Filtros");
        lblFiltros.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFiltros.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblFiltros);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Categoría
        panel.add(etiquetaFiltro("Categoría"));
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        cmbCategoria = new JComboBox<>();
        cmbCategoria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        cmbCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(cmbCategoria);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));

        // Estado
        panel.add(etiquetaFiltro("Estado del producto"));
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        cmbEstado = new JComboBox<>(new String[]{"TODOS", "NUEVO", "USADO"});
        cmbEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        cmbEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(cmbEstado);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));

        // Precio mínimo
        panel.add(etiquetaFiltro("Precio mínimo ($)"));
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        txtPrecioMin = campoPrecio("0");
        panel.add(txtPrecioMin);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Precio máximo
        panel.add(etiquetaFiltro("Precio máximo ($)"));
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        txtPrecioMax = campoPrecio("0");
        panel.add(txtPrecioMax);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botones
        btnFiltrar = boton("Aplicar filtros", new Color(52, 152, 219), Color.WHITE);
        panel.add(btnFiltrar);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        btnLimpiar = boton("Limpiar filtros", Color.WHITE, new Color(52, 152, 219));
        btnLimpiar.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)));
        panel.add(btnLimpiar);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        btnVolver = boton("← Volver", Color.WHITE, new Color(150, 150, 150));
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(btnVolver);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    // ── Panel de resultados ──────────────────────────────────────
    private JScrollPane construirPanelResultados() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(245, 247, 250));

        // Contador de resultados
        lblResultados = new JLabel("  Mostrando todos los productos");
        lblResultados.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblResultados.setForeground(new Color(120, 120, 120));
        lblResultados.setBorder(new EmptyBorder(10, 10, 6, 10));
        contenedor.add(lblResultados, BorderLayout.NORTH);

        panelResultados = new JPanel();
        panelResultados.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        panelResultados.setBackground(new Color(245, 247, 250));
        panelResultados.setBorder(new EmptyBorder(0, 8, 8, 8));
        contenedor.add(panelResultados, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setUnitIncrement(12);

        scroll.getHorizontalScrollBar().setUnitIncrement(4);
        scroll.getHorizontalScrollBar().setBlockIncrement(20);

        scroll.getHorizontalScrollBar().setPreferredSize(
                new Dimension(0, 14)
        );
        return scroll;
    }

    // ── Mostrar resultados (CA021, CA023) ────────────────────────
    public void mostrarResultados(List<Publicacion> lista) {
        panelResultados.removeAll();

        if (lista.isEmpty()) {
            // CA021: mensaje cuando no hay resultados
            JLabel sinRes = new JLabel("No se encontraron productos con esos criterios.");
            sinRes.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            sinRes.setForeground(new Color(150, 150, 150));
            panelResultados.add(sinRes);
            lblResultados.setText("  Sin resultados");
        } else {
            lblResultados.setText("  " + lista.size() + " producto(s) encontrado(s)");
            for (Publicacion p : lista) {
                panelResultados.add(crearTarjeta(p));
            }
        }

        panelResultados.revalidate();
        panelResultados.repaint();
    }

    // ── Tarjeta de producto ──────────────────────────────────────
    private JPanel crearTarjeta(Publicacion p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(12, 12, 12, 12)
        ));
        card.setPreferredSize(new Dimension(200, 280));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Imagen
        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(176, 130));
        lblImg.setMaximumSize(new Dimension(176, 130));
        lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        lblImg.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        if (p.getRutaImagen() != null && new File(p.getRutaImagen()).exists()) {
            ImageIcon icon = new ImageIcon(p.getRutaImagen());
            Image scaled = icon.getImage().getScaledInstance(176, 130, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(scaled));
        } else {
            lblImg.setText("Sin imagen");
            lblImg.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblImg.setForeground(new Color(180, 180, 180));
            lblImg.setBackground(new Color(248, 248, 248));
            lblImg.setOpaque(true);
        }
        card.add(lblImg);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        // Título
        JLabel lblTitulo = new JLabel("<html><b>" + truncar(p.getTitulo(), 22) + "</b></html>");
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 4)));

        // Categoría
        JLabel lblCat = new JLabel(p.getNombreCategoria() + "  ·  " + p.getEstadoProducto());
        lblCat.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCat.setForeground(new Color(130, 130, 130));
        lblCat.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblCat);
        card.add(Box.createRigidArea(new Dimension(0, 6)));

        // Precio
        JLabel lblPrecio = new JLabel(String.format("$%,.0f", p.getPrecio()));
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPrecio.setForeground(new Color(39, 174, 96));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblPrecio);
        card.add(Box.createRigidArea(new Dimension(0, 6)));

        // Stock
        JLabel lblStock = new JLabel("Stock: " + p.getStock());
        lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStock.setForeground(new Color(150, 150, 150));
        lblStock.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblStock);

        // Click para ver detalle
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                idPublicacionSeleccionada = p.getIdPublicacion();
                if (onVerDetalle != null) {
                    onVerDetalle.run();
                }
            }

            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 248, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219)),
                        new EmptyBorder(12, 12, 12, 12)
                ));
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        new EmptyBorder(12, 12, 12, 12)
                ));
            }
        });

        return card;
    }

    // ── Helpers ──────────────────────────────────────────────────
    public void cargarCategorias(Map<String, Integer> categorias) {
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem("TODAS");
        for (String nombre : categorias.keySet()) {
            cmbCategoria.addItem(nombre);
        }
    }

    public void setOnVerDetalle(Runnable callback) {
        this.onVerDetalle = callback;
    }

    public int getIdPublicacionSeleccionada() {
        return idPublicacionSeleccionada;
    }

    public String getTextoBusqueda() {
        return txtBusqueda.getText().trim();
    }

    public String getCategoriaSeleccionada() {
        String s = (String) cmbCategoria.getSelectedItem();
        return (s == null || s.equals("TODAS")) ? "" : s;
    }

    public String getEstadoSeleccionado() {
        return (String) cmbEstado.getSelectedItem();
    }

    public double getPrecioMin() {
        try {
            return Double.parseDouble(txtPrecioMin.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getPrecioMax() {
        try {
            return Double.parseDouble(txtPrecioMax.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void limpiarFiltros() {
        txtBusqueda.setText("");
        cmbCategoria.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        txtPrecioMin.setText("0");
        txtPrecioMax.setText("0");
    }

    private String truncar(String texto, int max) {
        return texto.length() > max ? texto.substring(0, max) + "..." : texto;
    }

    private JLabel etiquetaFiltro(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(80, 80, 80));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField campoPrecio(String defecto) {
        JTextField tf = new JTextField(defecto);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(3, 8, 3, 8)
        ));
        return tf;
    }

    private JButton boton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
}
