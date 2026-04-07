package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRegistroComprador extends JPanel {

    public JTextField txtNombre, txtDocumento, txtCorreo, txtCelular, txtDireccion;
    public JButton btnRegistrar;

    public PanelRegistroComprador() {

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250)); // fondo suave

        // 🔹 Card (contenedor central)
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                new EmptyBorder(30, 40, 30, 40)
        ));

        // 🔹 Título
        JLabel titulo = new JLabel("Registro de Comprador");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titulo);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // 🔹 Campos
        txtNombre = new JTextField();
        txtDocumento = new JTextField();
        txtCorreo = new JTextField();
        txtCelular = new JTextField();
        txtDireccion = new JTextField();

        card.add(crearCampo("Nombre completo", txtNombre));
        card.add(crearCampo("Documento", txtDocumento));
        card.add(crearCampo("Correo electrónico", txtCorreo));
        card.add(crearCampo("Celular", txtCelular));
        card.add(crearCampo("Dirección", txtDireccion));

        // 🔹 Botón moderno
        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBackground(new Color(52, 152, 219));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Hover
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrar.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrar.setBackground(new Color(52, 152, 219));
            }
        });

        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(btnRegistrar);

        add(card);
    }

    // 🔹 Método reutilizable para inputs
    private JPanel crearCampo(String label, JTextField campo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 0, 10, 0));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(80, 80, 80));

        campo.setPreferredSize(new Dimension(200, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200), 1, true),
                new EmptyBorder(5,10,5,10)
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }
}