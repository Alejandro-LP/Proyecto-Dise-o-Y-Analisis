package vista;

import javax.swing.*;
import java.awt.*;

public class PanelRegistro extends JPanel {

    public JTextField txtNombre, txtDocumento, txtCorreo, txtCelular, txtDireccion;
    public JComboBox<String> cmbRol;
    public JButton btnRegistrar;

    public PanelRegistro() {

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new GridLayout(7, 2, 10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        card.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        card.add(txtNombre);

        card.add(new JLabel("Documento:"));
        txtDocumento = new JTextField();
        card.add(txtDocumento);

        card.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        card.add(txtCorreo);

        card.add(new JLabel("Celular:"));
        txtCelular = new JTextField();
        card.add(txtCelular);

        card.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        card.add(txtDireccion);

        card.add(new JLabel("Rol:"));
        cmbRol = new JComboBox<>(new String[]{"Comprador", "Vendedor"});
        card.add(cmbRol);

        btnRegistrar = new JButton("Registrar");
        card.add(new JLabel(""));
        card.add(btnRegistrar);

        add(card);
    }

  
    public String getNombre() { return txtNombre.getText(); }
    public String getDocumento() { return txtDocumento.getText(); }
    public String getCorreo() { return txtCorreo.getText(); }
    public String getCelular() { return txtCelular.getText(); }
    public String getDireccion() { return txtDireccion.getText(); }
    public int getRol() {
        return cmbRol.getSelectedItem().toString().equals("Comprador") ? 1 : 2;
    }
}