import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ProbeBorder {
    public static void main(String[] a) {
        JFrame f = new JFrame("probe");
        f.setSize(600, 300);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout());

        TitledBorder orig = new TitledBorder("Original");
        System.out.println("just=" + orig.getTitleJustification() + " pos=" + orig.getTitlePosition());
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(orig);
        p1.setBackground(Color.WHITE);
        p1.add(new JLabel("x"));

        TitledBorder rep = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xC5, 0x96, 0x1A), 1),
                "Reemplazado", orig.getTitleJustification(), orig.getTitlePosition(),
                new Font("Segoe UI", Font.BOLD, 12), new Color(0x22, 0x22, 0x22));
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(rep);
        p2.setBackground(Color.WHITE);
        p2.add(new JLabel("y"));

        TitledBorder rep2 = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xC5, 0x96, 0x1A), 1),
                "Reemplazado TOP", TitledBorder.LEADING, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(0x22, 0x22, 0x22));
        JPanel p3 = new JPanel(new BorderLayout());
        p3.setBorder(rep2);
        p3.setBackground(Color.WHITE);
        p3.add(new JLabel("z"));

        p.add(p1, BorderLayout.NORTH);
        p.add(p2, BorderLayout.CENTER);
        p.add(p3, BorderLayout.SOUTH);
        f.add(p);
        f.setVisible(true);
        System.out.println("ABOVE_TOP=" + TitledBorder.ABOVE_TOP + " TOP=" + TitledBorder.TOP
                + " BELOW_TOP=" + TitledBorder.BELOW_TOP + " DEFAULT_POSITION=" + TitledBorder.DEFAULT_POSITION
                + " LEADING=" + TitledBorder.LEADING + " DEFAULT_JUST=" + TitledBorder.DEFAULT_JUSTIFICATION);
        new Timer(700, e -> {
            try {
                Container cp = f.getContentPane();
                java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                        cp.getWidth(), cp.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
                Graphics2D g = img.createGraphics();
                cp.printAll(g);
                g.dispose();
                javax.imageio.ImageIO.write(img, "png", new java.io.File(System.getenv("TEMP") + "\\sa_probe_border.png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }).start();
    }
}
