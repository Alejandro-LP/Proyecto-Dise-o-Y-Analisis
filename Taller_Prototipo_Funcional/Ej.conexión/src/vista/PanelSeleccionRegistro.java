package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel intermedio para elegir tipo de cuenta antes del registro.
 */
public class PanelSeleccionRegistro extends JPanel {

    public JButton btnComprador;
    public JButton btnVendedor;
    public JButton btnVolver;

    public PanelSeleccionRegistro() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(40, 60, 40, 60)
        ));

        JLabel titulo = new JLabel("¿Cómo quieres unirte?");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Elige el tipo de cuenta que deseas crear");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(120, 120, 120));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titulo);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(subtitulo);
        card.add(Box.createRigidArea(new Dimension(0, 36)));

        btnComprador = new JButton("🛒  Quiero comprar");
        estiloBotonOpcion(btnComprador, new Color(52, 152, 219));
        card.add(btnComprador);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel descComp = new JLabel("Busca, compara y compra productos.");
        descComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descComp.setForeground(new Color(130, 130, 130));
        descComp.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(descComp);
        card.add(Box.createRigidArea(new Dimension(0, 24)));

        btnVendedor = new JButton("🏪  Quiero vender");
        estiloBotonOpcion(btnVendedor, new Color(39, 174, 96));
        card.add(btnVendedor);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel descVend = new JLabel("Publica y vende tus productos en MercadoRed.");
        descVend.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descVend.setForeground(new Color(130, 130, 130));
        descVend.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(descVend);
        card.add(Box.createRigidArea(new Dimension(0, 32)));

        btnVolver = new JButton("← Volver");
        estiloBotonSecundario(btnVolver);
        card.add(btnVolver);

        add(card);
    }

    private void estiloBotonOpcion(JButton btn, Color color) {
        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        Color hover = color.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(color); }
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
