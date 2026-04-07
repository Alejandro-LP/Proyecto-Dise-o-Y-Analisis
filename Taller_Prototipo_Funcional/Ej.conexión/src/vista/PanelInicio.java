package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelInicio extends JPanel {

    public JButton btnLogin;
    public JButton btnRegistro;

    public PanelInicio() {

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                new EmptyBorder(40, 50, 40, 50)
        ));

        // 🔹 Título
        JLabel titulo = new JLabel("Bienvenido a MercadoRed");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Compra y vende fácilmente");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(120,120,120));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titulo);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(subtitulo);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        // 🔹 Botón Login
        btnLogin = new JButton("Iniciar sesión");
        estiloBotonPrimario(btnLogin);

        // 🔹 Botón Registro
        btnRegistro = new JButton("Registrarse");
        estiloBotonSecundario(btnRegistro);

        card.add(btnLogin);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(btnRegistro);

        add(card);
    }

    private void estiloBotonPrimario(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }
        });
    }

    private void estiloBotonSecundario(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(52, 152, 219));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createLineBorder(new Color(52,152,219)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }
}