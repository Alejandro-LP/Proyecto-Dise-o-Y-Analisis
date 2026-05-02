package vista;

import modelo.ItemCarrito;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelCarrito extends JPanel {

    public JButton btnProceder;
    public JButton btnVolver;
    private JPanel panelItems;
    private JLabel lblTotal;
    private JLabel lblVacio;

    // Callbacks hacia el controlador
    private java.util.function.Consumer<Integer> onEliminar;
    private java.util.function.BiConsumer<Integer, Integer> onCambiarCantidad;

    public PanelCarrito() {
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 247, 250));

        // ── Encabezado
        JPanel enc = new JPanel(new BorderLayout());
        enc.setBackground(new Color(52, 152, 219));
        enc.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel("🛒  Mi carrito");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        enc.add(titulo, BorderLayout.WEST);
        add(enc, BorderLayout.NORTH);

        // ── Lista de items
        panelItems = new JPanel();
        panelItems.setLayout(new BoxLayout(panelItems, BoxLayout.Y_AXIS));
        panelItems.setBackground(new Color(245, 247, 250));
        panelItems.setBorder(new EmptyBorder(10, 15, 10, 15));

        lblVacio = new JLabel("Tu carrito está vacío.");
        lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblVacio.setForeground(new Color(150, 150, 150));
        lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scroll = new JScrollPane(panelItems);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // ── Footer
        JPanel footer = new JPanel(new BorderLayout(10, 0));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                new EmptyBorder(14, 20, 14, 20)
        ));

        lblTotal = new JLabel("Total: $0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(39, 174, 96));
        footer.add(lblTotal, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        btnVolver = new JButton("← Seguir comprando");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setFocusPainted(false);
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setForeground(new Color(52, 152, 219));
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnProceder = new JButton("Proceder al pago →");
        btnProceder.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProceder.setFocusPainted(false);
        btnProceder.setBackground(new Color(39, 174, 96));
        btnProceder.setForeground(Color.WHITE);
        btnProceder.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPanel.add(btnVolver);
        btnPanel.add(btnProceder);
        footer.add(btnPanel, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
    }

    // CA026 — Mostrar contenido actualizado del carrito
    public void cargarItems(List<ItemCarrito> items) {
        panelItems.removeAll();
        if (items.isEmpty()) {
            panelItems.add(Box.createRigidArea(new Dimension(0, 30)));
            panelItems.add(lblVacio);
            lblTotal.setText("Total: $0");
            btnProceder.setEnabled(false);
        } else {
            btnProceder.setEnabled(true);
            double total = 0;
            for (ItemCarrito item : items) {
                panelItems.add(crearFilaItem(item));
                panelItems.add(Box.createRigidArea(new Dimension(0, 10)));
                total += item.getSubtotal();
            }
            lblTotal.setText("Total: $" + String.format("%,.2f", total));
        }
        panelItems.revalidate();
        panelItems.repaint();
    }

    private JPanel crearFilaItem(ItemCarrito item) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(12, 14, 12, 14)
        ));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Imagen pequeña
        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(65, 65));
        lblImg.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        if (item.getRutaImagen() != null && !item.getRutaImagen().isEmpty()
                && new java.io.File(item.getRutaImagen()).exists()) {
            ImageIcon icon = new ImageIcon(item.getRutaImagen());
            lblImg.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH)));
        } else {
            lblImg.setText("📦");
            lblImg.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        }
        fila.add(lblImg, BorderLayout.WEST);

        // Info centro
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel lblNombre = new JLabel(item.getTituloProducto());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrecio = new JLabel(String.format("$%,.2f c/u", item.getPrecioUnitario()));
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrecio.setForeground(new Color(100, 100, 100));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtotal = new JLabel(String.format("Subtotal: $%,.2f", item.getSubtotal()));
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSubtotal.setForeground(new Color(39, 174, 96));
        lblSubtotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(lblNombre);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(lblPrecio);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(lblSubtotal);
        fila.add(info, BorderLayout.CENTER);

        // Controles derecha — CA025
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        controles.setOpaque(false);

        JButton btnMenos = botonCantidad("−");
        JLabel lblCant = new JLabel(String.valueOf(item.getCantidad()));
        lblCant.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCant.setPreferredSize(new Dimension(28, 28));
        lblCant.setHorizontalAlignment(SwingConstants.CENTER);
        JButton btnMas = botonCantidad("+");

        JButton btnElim = new JButton("✕");
        btnElim.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnElim.setForeground(new Color(200, 50, 50));
        btnElim.setBackground(Color.WHITE);
        btnElim.setBorder(BorderFactory.createLineBorder(new Color(200, 50, 50)));
        btnElim.setFocusPainted(false);
        btnElim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnElim.setPreferredSize(new Dimension(32, 32));

        btnMenos.addActionListener(e -> {
            if (onCambiarCantidad != null) {
                onCambiarCantidad.accept(item.getIdItem(), item.getCantidad() - 1);
            }
        });
        btnMas.addActionListener(e -> {
            if (onCambiarCantidad != null) {
                onCambiarCantidad.accept(item.getIdItem(), item.getCantidad() + 1);
            }
        });
        btnElim.addActionListener(e -> {
            if (onEliminar != null) {
                onEliminar.accept(item.getIdItem());
            }
        });

        controles.add(btnMenos);
        controles.add(lblCant);
        controles.add(btnMas);
        controles.add(Box.createRigidArea(new Dimension(8, 0)));
        controles.add(btnElim);
        fila.add(controles, BorderLayout.EAST);

        return fila;
    }

    private JButton botonCantidad(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(245, 247, 250));
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void setOnEliminar(java.util.function.Consumer<Integer> cb) {
        this.onEliminar = cb;
    }

    public void setOnCambiarCantidad(java.util.function.BiConsumer<Integer, Integer> cb) {
        this.onCambiarCantidad = cb;
    }
}
