package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * RF002 — Registro de vendedor.
 * CA004: cuenta bancaria obligatoria
 * CA005: checkbox de aceptación de contrato
 */
public class PanelRegistroVendedor extends JPanel {

    public JTextField txtNombre;
    public JTextField txtDocumento;
    public JTextField txtCorreo;
    public JTextField txtCelular;
    public JTextField txtDireccion;
    public JTextField txtCuentaBancaria;
    public JPasswordField txtPassword;
    public JPasswordField txtConfirmPassword;
    public JCheckBox chkContrato;
    public JButton btnRegistrar;
    public JButton btnVolver;
    public JLabel lblMensaje;

    public PanelRegistroVendedor() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(35, 50, 35, 50)
        ));

        JLabel titulo = new JLabel("Crear cuenta de vendedor");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titulo);

        JLabel aviso = new JLabel("Tu cuenta será revisada antes de poder publicar.");
        aviso.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        aviso.setForeground(new Color(150, 100, 0));
        aviso.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(aviso);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        txtNombre          = campo(card, "Nombre completo (*)");
        txtDocumento       = campo(card, "Documento de identidad (*)");
        txtCorreo          = campo(card, "Correo electrónico (*)");
        txtCelular         = campo(card, "Número de celular (*)");
        txtDireccion       = campo(card, "Dirección física (*)");
        txtCuentaBancaria  = campo(card, "Cuenta bancaria (*)");
        txtPassword        = campoPassword(card, "Contraseña (*)");
        txtConfirmPassword = campoPassword(card, "Confirmar contraseña (*)");

        // CA005: contrato de comisión
        chkContrato = new JCheckBox("Acepto el contrato de comisión de MercadoRed");
        chkContrato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkContrato.setBackground(Color.WHITE);
        chkContrato.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(chkContrato);
        card.add(Box.createRigidArea(new Dimension(0, 18)));

        btnRegistrar = new JButton("Registrarme como vendedor");
        estiloBotonPrimario(btnRegistrar);
        card.add(btnRegistrar);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        btnVolver = new JButton("← Volver");
        estiloBotonSecundario(btnVolver);
        card.add(btnVolver);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setForeground(new Color(200, 50, 50));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblMensaje);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(500, 620));
        add(scroll);
    }

    public String getNombre()          { return txtNombre.getText().trim(); }
    public String getDocumento()       { return txtDocumento.getText().trim(); }
    public String getCorreo()          { return txtCorreo.getText().trim(); }
    public String getCelular()         { return txtCelular.getText().trim(); }
    public String getDireccion()       { return txtDireccion.getText().trim(); }
    public String getCuentaBancaria()  { return txtCuentaBancaria.getText().trim(); }
    public String getPassword()        { return new String(txtPassword.getPassword()); }
    public String getConfirmPassword() { return new String(txtConfirmPassword.getPassword()); }
    public boolean contratoAceptado()  { return chkContrato.isSelected(); }

    public void mostrarMensaje(String msg, boolean esError) {
        lblMensaje.setText(msg);
        lblMensaje.setForeground(esError ? new Color(200, 50, 50) : new Color(39, 174, 96));
    }

    private JTextField campo(JPanel panel, String etiqueta) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        JTextField tf = new JTextField();
        estiloCampo(tf);
        panel.add(tf);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        return tf;
    }

    private JPasswordField campoPassword(JPanel panel, String etiqueta) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        JPasswordField pf = new JPasswordField();
        estiloCampo(pf);
        panel.add(pf);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        return pf;
    }

    private void estiloCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
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
