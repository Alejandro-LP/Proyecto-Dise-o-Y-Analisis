package vista;

import javax.swing.JOptionPane;

public class Registro_usuarios extends javax.swing.JFrame {

    public Registro_usuarios() {
        initComponents();
    }

    // ==========================
    // GETTERS (MVC)
    // ==========================
    public String getNombre() {
        return txtNombre.getText();
    }

    public String getDocumento() {
        return txtDocumento.getText();
    }

    public String getCorreo() {
        return txtCorreo.getText();
    }

    public String getCelular() {
        return txtCelular.getText();
    }

    public String getDireccion() {
        return txtDireccion.getText();
    }

    public int getRol() {
        return cmbRol.getSelectedItem().toString().equals("Comprador") ? 1 : 2;
    }

    // ==========================
    // BOTÓN (SIN LÓGICA MVC)
    // ==========================
    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtDocumento.getText().isEmpty() || txtNombre.getText().isEmpty()
                || txtCorreo.getText().isEmpty() || txtCelular.getText().isEmpty()
                || txtDireccion.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
        }
    }

    // ==========================
    // INIT COMPONENTS (CORRECTO)
    // ==========================
    private void initComponents() {

        Titulo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblDocumento = new javax.swing.JLabel();
        lblCorreo = new javax.swing.JLabel();
        lblCelular = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        lblRol = new javax.swing.JLabel();

        txtNombre = new javax.swing.JTextField();
        txtDocumento = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        txtCelular = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();

        cmbRol = new javax.swing.JComboBox<>();
        btnRegistrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registro de usuarios");

        Titulo.setText("Registro de usuarios");

        lblNombre.setText("Nombre:");
        lblDocumento.setText("Documento:");
        lblCorreo.setText("Correo:");
        lblCelular.setText("Celular:");
        lblDireccion.setText("Dirección:");
        lblRol.setText("Rol:");

        cmbRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Comprador", "Vendedor" }));

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        // Layout simple (para evitar errores del .form)
        setLayout(new java.awt.GridLayout(8, 2, 10, 10));

        add(Titulo);
        add(new javax.swing.JLabel(""));

        add(lblNombre);
        add(txtNombre);

        add(lblDocumento);
        add(txtDocumento);

        add(lblCorreo);
        add(txtCorreo);

        add(lblCelular);
        add(txtCelular);

        add(lblDireccion);
        add(txtDireccion);

        add(lblRol);
        add(cmbRol);

        add(new javax.swing.JLabel(""));
        add(btnRegistrar);

        pack();
        setLocationRelativeTo(null);
    }

    public javax.swing.JButton btnRegistrar;

    private javax.swing.JComboBox<String> cmbRol;
    private javax.swing.JLabel Titulo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblCelular;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblRol;

    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JTextField txtDireccion;
}