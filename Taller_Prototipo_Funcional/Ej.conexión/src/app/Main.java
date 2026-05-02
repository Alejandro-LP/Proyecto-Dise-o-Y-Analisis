package app;

import controlador.ControladorPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("MercadoRed");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(700, 650);
            ventana.setLocationRelativeTo(null);
            ventana.setResizable(false);

            new ControladorPrincipal(ventana);

            ventana.setVisible(true);
        });
    }
}
