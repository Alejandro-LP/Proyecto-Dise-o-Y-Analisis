package vista;

import modelo.Publicacion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelMisPublicaciones extends JPanel {

    public JTable tabla;
    public JButton btnNueva;
    public JButton btnEditar;
    public JButton btnVer;
    public JButton btnVolver;
    private DefaultTableModel modeloTabla;

    public PanelMisPublicaciones() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Encabezado
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(new Color(245, 247, 250));
        JLabel titulo = new JLabel("Mis publicaciones");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        encabezado.add(titulo, BorderLayout.WEST);

        btnNueva = new JButton("+ Nueva publicación");
        btnNueva.setBackground(new Color(52, 152, 219));
        btnNueva.setForeground(Color.WHITE);
        btnNueva.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNueva.setFocusPainted(false);
        btnNueva.setCursor(new Cursor(Cursor.HAND_CURSOR));
        encabezado.add(btnNueva, BorderLayout.EAST);
        add(encabezado, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Título", "Categoría", "Precio", "Stock", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones inferiores
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setBackground(new Color(245, 247, 250));

        btnVer = new JButton("Ver detalle");
        btnEditar = new JButton("Editar");
        btnVolver = new JButton("← Volver");

        for (JButton b : new JButton[]{btnVer, btnEditar, btnVolver}) {
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        btnVer.setBackground(new Color(39, 174, 96));
        btnVer.setForeground(Color.WHITE);
        btnEditar.setBackground(new Color(243, 156, 18));
        btnEditar.setForeground(Color.WHITE);
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setForeground(new Color(52, 152, 219));
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)));

        botones.add(btnVolver);
        botones.add(btnVer);
        botones.add(btnEditar);
        add(botones, BorderLayout.SOUTH);
    }

    public void cargarDatos(List<Publicacion> lista) {
        modeloTabla.setRowCount(0);
        for (Publicacion p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdPublicacion(),
                p.getTitulo(),
                p.getNombreCategoria(),
                String.format("$%,.2f", p.getPrecio()),
                p.getStock(),
                p.getEstadoProducto()
            });
        }
    }

    public int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return -1;
        }
        return (int) modeloTabla.getValueAt(fila, 0);
    }
}
