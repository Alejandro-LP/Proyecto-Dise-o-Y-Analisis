package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * RF003 — Confirmación de correo electrónico.
 * CA007: el sistema envía un token al correo registrado.
 * CA008: sin confirmación la cuenta queda en estado PENDIENTE.
 */
public class PanelConfirmacionCorreo extends JPanel {

    public JTextField txtCorreo;
    public JTextField txtToken;
    public JButton btnConfirmar;
    public JButton btnVolver;
    public JLabel lblMensaje;

    public PanelConfirmacionCorreo() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(40, 50, 40, 50)
        ));

        JLabel titulo = new JLabel("Confirmar correo electrónico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel info = new JLabel("<html><center>Ingresa el código de 8 caracteres<br>que enviamos a tu correo.</center></html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(new Color(100, 100, 100));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titulo);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(info);
        card.add(Box.createRigidArea(new Dimension(0, 28)));

        // Correo
        JLabel lblCorreo = new JLabel("Tu correo electrónico");
        lblCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCorreo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblCorreo);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        txtCorreo = new JTextField();
        estiloCampo(txtCorreo);
        card.add(txtCorreo);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        // Token
        JLabel lblToken = new JLabel("Código de confirmación");
        lblToken.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblToken.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblToken);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        txtToken = new JTextField();
        txtToken.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtToken.setHorizontalAlignment(JTextField.CENTER);
        estiloCampo(txtToken);
        card.add(txtToken);
        card.add(Box.createRigidArea(new Dimension(0, 24)));

        // Botones
        btnConfirmar = new JButton("Confirmar cuenta");
        estiloBotonPrimario(btnConfirmar);
        card.add(btnConfirmar);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        btnVolver = new JButton("← Volver al inicio");
        estiloBotonSecundario(btnVolver);
        card.add(btnVolver);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblMensaje);

        add(card);
    }

    public String getCorreo() { return txtCorreo.getText().trim(); }
    public String getToken()  { return txtToken.getText().trim(); }

    /** Rellena el correo automáticamente tras el registro. */
    public void precargarCorreo(String correo) {
        txtCorreo.setText(correo);
    }

    public void mostrarMensaje(String msg, boolean esError) {
        lblMensaje.setText(msg);
        lblMensaje.setForeground(esError ? new Color(200, 50, 50) : new Color(39, 174, 96));
    }

    private void estiloCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void estiloBotonPrimario(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(41, 128, 185)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(new Color(52, 152, 219)); }
        });
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
