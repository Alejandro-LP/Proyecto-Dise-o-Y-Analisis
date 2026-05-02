package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class PanelCheckout extends JPanel {

    public JTextField txtDireccion;
    public JComboBox<String> cmbMetodoPago;
    public JLabel lblResumen;
    public JButton btnConfirmar;
    public JButton btnVolver;
    public JLabel lblMensaje;

    public PanelCheckout() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(35, 50, 35, 50)
        ));

        JLabel titulo = new JLabel("Finalizar compra");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createRigidArea(new Dimension(0, 6)));

        // Resumen del total
        lblResumen = new JLabel("Total a pagar: $0");
        lblResumen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblResumen.setForeground(new Color(39, 174, 96));
        lblResumen.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblResumen);
        card.add(Box.createRigidArea(new Dimension(0, 28)));

        // RF012 — Dirección de envío (CA027, CA028)
        card.add(etiqueta("📍  Dirección de envío (*)"));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        txtDireccion = new JTextField();
        txtDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDireccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtDireccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDireccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        card.add(txtDireccion);
        card.add(Box.createRigidArea(new Dimension(0, 22)));

        // RF013 — Método de pago (CA029, CA030)
        card.add(etiqueta("💳  Método de pago (*)"));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        cmbMetodoPago = new JComboBox<>();
        cmbMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbMetodoPago.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        cmbMetodoPago.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cmbMetodoPago);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        // Botón confirmar
        btnConfirmar = new JButton("✔  Confirmar compra");
        btnConfirmar.setBackground(new Color(39, 174, 96));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnConfirmar.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(btnConfirmar);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        btnVolver = new JButton("← Volver al carrito");
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setForeground(new Color(52, 152, 219));
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)));
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(btnVolver);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblMensaje);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(card, gbc);
    }

    public void cargarMetodosPago(Map<String, Integer> metodos) {
        cmbMetodoPago.removeAllItems();
        cmbMetodoPago.addItem("-- Selecciona un método --");
        for (String nombre : metodos.keySet()) {
            cmbMetodoPago.addItem(nombre);
        }
    }

    public void setTotal(double total) {
        lblResumen.setText("Total a pagar: $" + String.format("%,.2f", total));
    }

    public String getDireccion() {
        return txtDireccion.getText().trim();
    }

    public String getMetodoPago() {
        return (String) cmbMetodoPago.getSelectedItem();
    }

    public void mostrarMensaje(String msg, boolean esError) {
        lblMensaje.setText(msg);
        lblMensaje.setForeground(esError ? new Color(200, 50, 50) : new Color(39, 174, 96));
    }

    public void precargarDireccion(String direccion) {
        txtDireccion.setText(direccion);
    }

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}
