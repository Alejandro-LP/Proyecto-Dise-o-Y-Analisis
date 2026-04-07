package controlador;

import vista.*;
import javax.swing.*;

public class ControladorPrincipal {

    private JFrame frame;
    private ControladorUsuario ctrlUsuario = new ControladorUsuario();

    public ControladorPrincipal(JFrame frame) {
        this.frame = frame;
    }

    public void iniciar() {
        mostrarInicio();
    }

    public void mostrarInicio() {
        PanelInicio vista = new PanelInicio();

        vista.btnRegistro.addActionListener(e -> mostrarRegistro());
        vista.btnLogin.addActionListener(e -> mostrarLogin());

        cambiarPanel(vista);
    }

    public void mostrarRegistro() {
        PanelRegistro vista = new PanelRegistro();

        vista.btnRegistrar.addActionListener(e -> {
            String msg = ctrlUsuario.registrar(
                    vista.getNombre(),
                    vista.getDocumento(),
                    vista.getCorreo(),
                    vista.getCelular(),
                    vista.getDireccion(),
                    vista.getRol()
            );

            JOptionPane.showMessageDialog(frame, msg);
        });

        cambiarPanel(vista);
    }

    public void mostrarLogin() {
        JOptionPane.showMessageDialog(frame, "login");
    }

    private void cambiarPanel(JPanel panel) {
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }
}