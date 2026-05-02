package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelLogin extends JPanel {

    public JTextField txtCorreo;
    public JPasswordField txtPassword;
    public JButton btnIngresar;
    public JButton btnVolver;
    public JLabel lblMensaje;

    public PanelLogin() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(40, 50, 40, 50)
        ));
        card.setMaximumSize(new Dimension(420, 500));

        // Título
        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Accede a tu cuenta de MercadoRed");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(120, 120, 120));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titulo);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(subtitulo);
        card.add(Box.createRigidArea(new Dimension(0, 28)));

        // Correo
        card.add(etiqueta("Correo electrónico"));
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        txtCorreo = new JTextField();
        estiloCampo(txtCorreo);
        card.add(txtCorreo);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        // Contraseña
        card.add(etiqueta("Contraseña"));
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        txtPassword = new JPasswordField();
        estiloCampo(txtPassword);
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 24)));

        // Botón ingresar
        btnIngresar = new JButton("Ingresar");
        estiloBotonPrimario(btnIngresar);
        card.add(btnIngresar);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botón volver
        btnVolver = new JButton("← Volver");
        estiloBotonSecundario(btnVolver);
        card.add(btnVolver);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        // Mensaje de error/info
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setForeground(new Color(200, 50, 50));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblMensaje);

        add(card);
    }

    public String getCorreo()    { return txtCorreo.getText().trim(); }
    public String getPassword()  { return new String(txtPassword.getPassword()); }

    public void mostrarMensaje(String msg, boolean esError) {
        lblMensaje.setText(msg);
        lblMensaje.setForeground(esError ? new Color(200, 50, 50) : new Color(39, 174, 96));
    }

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void estiloCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
