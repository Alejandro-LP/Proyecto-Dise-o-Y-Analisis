package app;

import controlador.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("MercadoRed");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ControladorPrincipal app = new ControladorPrincipal(frame);
        app.iniciar();

        frame.setVisible(true);
    }
}