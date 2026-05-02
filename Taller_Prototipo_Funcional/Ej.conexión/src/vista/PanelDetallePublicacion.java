package vista;

import modelo.Publicacion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class PanelDetallePublicacion extends JPanel {

    public JButton btnVolver;
    public JButton btnEditar;
    public JButton btnAgregarCarrito;
    public JSpinner spinCantidad;

    private JLabel lblTitulo;
    private JLabel lblCategoria;
    private JLabel lblPrecio;
    private JLabel lblStock;
    private JLabel lblEstado;
    private JTextArea txtDescripcion;
    private JTextArea txtPoliticas;
    private JLabel lblImagen;

    private int idPublicacionActual = -1;
    private boolean modoVendedor = false;

    public PanelDetallePublicacion() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(30, 45, 30, 45)
        ));

        // Imagen CA019
        lblImagen = new JLabel();
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setPreferredSize(new Dimension(300, 200));
        lblImagen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        lblImagen.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setBackground(new Color(248, 248, 248));
        lblImagen.setOpaque(true);
        card.add(lblImagen);
        card.add(Box.createRigidArea(new Dimension(0, 18)));

        lblTitulo = campoDetalle(card, "Título");
        lblCategoria = campoDetalle(card, "Categoría");
        lblPrecio = campoDetalle(card, "Precio");
        lblStock = campoDetalle(card, "Stock disponible");
        lblEstado = campoDetalle(card, "Estado del producto");

        // Descripción
        card.add(etiqueta("Descripción"));
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        txtDescripcion = areaTexto(3);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(scrollDesc);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        // Políticas de devolución
        card.add(etiqueta("Políticas de devolución"));
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        txtPoliticas = areaTexto(2);
        JScrollPane scrollPol = new JScrollPane(txtPoliticas);
        scrollPol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        scrollPol.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(scrollPol);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // ── Selector de cantidad + botón carrito (solo comprador) ──
        JPanel panelCant = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelCant.setOpaque(false);
        panelCant.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinCantidad.setPreferredSize(new Dimension(65, 32));
        panelCant.add(lblCant);
        panelCant.add(spinCantidad);
        card.add(panelCant);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        btnAgregarCarrito = new JButton("🛒  Agregar al carrito");
        btnAgregarCarrito.setBackground(new Color(52, 152, 219));
        btnAgregarCarrito.setForeground(Color.WHITE);
        btnAgregarCarrito.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregarCarrito.setFocusPainted(false);
        btnAgregarCarrito.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarCarrito.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnAgregarCarrito.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(btnAgregarCarrito);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // ── Botón editar (solo vendedor) ──
        btnEditar = new JButton("✏  Editar publicación");
        btnEditar.setBackground(new Color(243, 156, 18));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnEditar.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(btnEditar);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        btnVolver = new JButton("← Volver");
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setForeground(new Color(52, 152, 219));
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)));
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(btnVolver);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(520, 560));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(scroll, gbc);
    }

    // ── Cargar datos CA018 + CA019 ───────────────────────────────
    public void cargarPublicacion(Publicacion p) {
        this.idPublicacionActual = p.getIdPublicacion();

        lblTitulo.setText(p.getTitulo());
        lblCategoria.setText(p.getNombreCategoria());
        lblPrecio.setText(String.format("$%,.2f", p.getPrecio()));
        lblStock.setText(String.valueOf(p.getStock()));
        lblEstado.setText(p.getEstadoProducto());
        txtDescripcion.setText(p.getDescripcion());
        txtPoliticas.setText(p.getPoliticasDevolucion() != null
                ? p.getPoliticasDevolucion() : "Sin políticas registradas.");

        // Spinner máximo = stock real
        spinCantidad.setModel(new SpinnerNumberModel(1, 1,
                Math.max(1, p.getStock()), 1));

        // CA019: cargar imagen
        if (p.getRutaImagen() != null && !p.getRutaImagen().isEmpty()) {
            File archivo = new File(p.getRutaImagen());
            if (archivo.exists()) {
                ImageIcon img = new ImageIcon(p.getRutaImagen());
                Image scaled = img.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(scaled));
                lblImagen.setText("");
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("Imagen no disponible");
            }
        } else {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
        }
    }

    // Modo comprador: muestra carrito, oculta editar
    public void setModoComprador() {
        modoVendedor = false;
        btnAgregarCarrito.setVisible(true);
        spinCantidad.setVisible(true);
        btnEditar.setVisible(false);
    }

    // Modo vendedor: muestra editar, oculta carrito
    public void setModoVendedor() {
        modoVendedor = true;
        btnAgregarCarrito.setVisible(false);
        spinCantidad.setVisible(false);
        btnEditar.setVisible(true);
    }

    public int getIdPublicacionActual() {
        return idPublicacionActual;
    }

    public int getCantidadSeleccionada() {
        return (int) spinCantidad.getValue();
    }

    // ── Helpers ──────────────────────────────────────────────────
    private JLabel campoDetalle(JPanel panel, String etiquetaTexto) {
        panel.add(etiqueta(etiquetaTexto));
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        JLabel lbl = new JLabel("-");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        return lbl;
    }

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(120, 120, 120));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextArea areaTexto(int filas) {
        JTextArea ta = new JTextArea(filas, 20);
        ta.setEditable(false);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }
}
