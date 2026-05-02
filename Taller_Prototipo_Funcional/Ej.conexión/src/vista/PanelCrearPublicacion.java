package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class PanelCrearPublicacion extends JPanel {

    public JTextField txtTitulo;
    public JTextArea txtDescripcion;
    public JTextField txtPrecio;
    public JTextField txtStock;
    public JComboBox<String> cmbCategoria;
    public JComboBox<String> cmbEstado;   // NUEVO / USADO
    public JTextArea txtPoliticas;
    public JTextField txtRutaImagen;
    public JButton btnSeleccionarImagen;
    public JButton btnGuardar;
    public JButton btnVolver;
    public JLabel lblMensaje;

    public PanelCrearPublicacion() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(30, 45, 30, 45)
        ));

        JLabel titulo = new JLabel("Nueva publicación");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        txtTitulo = campo(card, "Título del producto (*)");

        // Descripción
        card.add(etiqueta("Descripción (*)"));
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(scrollDesc);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        txtPrecio = campo(card, "Precio (*)");
        txtStock = campo(card, "Stock disponible (*)");

        // Categoría
        card.add(etiqueta("Categoría (*)"));
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        cmbCategoria = new JComboBox<>();
        cmbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCategoria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cmbCategoria);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        // Estado producto CA014
        card.add(etiqueta("Estado del producto (*)"));
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        cmbEstado = new JComboBox<>(new String[]{"NUEVO", "USADO"});
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cmbEstado);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        // Políticas de devolución
        card.add(etiqueta("Políticas de devolución"));
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        txtPoliticas = new JTextArea(2, 20);
        txtPoliticas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPoliticas.setLineWrap(true);
        txtPoliticas.setWrapStyleWord(true);
        JScrollPane scrollPol = new JScrollPane(txtPoliticas);
        scrollPol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        scrollPol.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(scrollPol);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        // Imagen
        card.add(etiqueta("Imagen del producto"));
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        JPanel panelImg = new JPanel(new BorderLayout(8, 0));
        panelImg.setBackground(Color.WHITE);
        panelImg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        panelImg.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtRutaImagen = new JTextField();
        txtRutaImagen.setEditable(false);
        txtRutaImagen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSeleccionarImagen = new JButton("Examinar...");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelImg.add(txtRutaImagen, BorderLayout.CENTER);
        panelImg.add(btnSeleccionarImagen, BorderLayout.EAST);
        card.add(panelImg);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        btnGuardar = new JButton("Publicar producto");
        estiloBotonPrimario(btnGuardar);
        card.add(btnGuardar);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        btnVolver = new JButton("← Volver");
        estiloBotonSecundario(btnVolver);
        card.add(btnVolver);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblMensaje);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(520, 540));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(scroll, gbc);
    }

    public void cargarCategorias(Map<String, Integer> categorias) {
        cmbCategoria.removeAllItems();
        for (String nombre : categorias.keySet()) {
            cmbCategoria.addItem(nombre);
        }
    }

    public String getTitulo() {
        return txtTitulo.getText().trim();
    }

    public String getDescripcion() {
        return txtDescripcion.getText().trim();
    }

    public String getPrecio() {
        return txtPrecio.getText().trim();
    }

    public String getStock() {
        return txtStock.getText().trim();
    }

    public String getCategoria() {
        return (String) cmbCategoria.getSelectedItem();
    }

    public String getEstado() {
        return (String) cmbEstado.getSelectedItem();
    }

    public String getPoliticas() {
        return txtPoliticas.getText().trim();
    }

    public String getRutaImagen() {
        return txtRutaImagen.getText().trim();
    }

    public void mostrarMensaje(String msg, boolean esError) {
        lblMensaje.setText(msg);
        lblMensaje.setForeground(esError ? new Color(200, 50, 50) : new Color(39, 174, 96));
    }

    public void limpiar() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtPoliticas.setText("");
        txtRutaImagen.setText("");
        cmbEstado.setSelectedIndex(0);
        lblMensaje.setText(" ");
    }

    private JTextField campo(JPanel panel, String etiqueta) {
        panel.add(etiqueta(etiqueta));
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(tf);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        return tf;
    }

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void estiloBotonPrimario(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void estiloBotonSecundario(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(52, 152, 219));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
