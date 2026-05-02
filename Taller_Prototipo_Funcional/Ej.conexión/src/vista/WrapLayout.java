package vista;

import java.awt.*;

/**
 * Layout que acomoda componentes en filas y baja a la siguiente cuando no hay
 * más espacio horizontal — necesario para las tarjetas del catálogo.
 */
public class WrapLayout extends FlowLayout {

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target, false);
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getSize().width;
            if (targetWidth == 0) {
                targetWidth = Integer.MAX_VALUE;
            }

            int hgap = getHgap(), vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = targetWidth - insets.left - insets.right - hgap * 2;

            int x = 0, y = insets.top + vgap, rowHeight = 0;
            int count = target.getComponentCount();

            for (int i = 0; i < count; i++) {
                Component c = target.getComponent(i);
                if (!c.isVisible()) {
                    continue;
                }
                Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
                if (x == 0 || (x + d.width) <= maxWidth) {
                    x += d.width + hgap;
                } else {
                    y += rowHeight + vgap;
                    x = d.width + hgap;
                    rowHeight = 0;
                }
                rowHeight = Math.max(rowHeight, d.height);
            }
            y += rowHeight + vgap + insets.bottom;
            return new Dimension(targetWidth, y);
        }
    }
}
