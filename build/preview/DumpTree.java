import javax.swing.*;
import java.awt.*;

public class DumpTree {
    public static void main(String[] a) throws Exception {
        Class<?> clazz = Class.forName(a[0]);
        Object v = clazz.getConstructor().newInstance();
        Window w = (Window) v;
        w.setSize(1400, 820);
        w.setVisible(true);
        Thread.sleep(1200);
        SwingUtilities.invokeLater(() -> {
            dump(((RootPaneContainer) w).getContentPane(), 0);
            System.exit(0);
        });
        Thread.sleep(3000);
    }

    static void dump(Component c, int depth) {
        String tipo = c.getClass().getSimpleName();
        String extra = "";
        if (c instanceof JLabel) extra = " text=" + ((JLabel) c).getText();
        if (c instanceof JButton) extra = " text=" + ((JButton) c).getText();
        if (c instanceof Container && ((Container) c).getLayout() != null) {
            extra += " layout=" + ((Container) c).getLayout().getClass().getSimpleName();
        }
        System.out.println("  ".repeat(depth) + tipo + " " + c.getBounds() + extra);
        if (c instanceof Container && depth < 7) {
            for (Component h : ((Container) c).getComponents()) {
                dump(h, depth + 1);
            }
        }
    }
}
