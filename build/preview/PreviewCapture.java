import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;

/**
 * Herramienta de verificación visual (no forma parte de la aplicación).
 * Uso: java -cp build\classes;build\preview PreviewCapture <claseVista> <salida.png> [ancho] [alto]
 */
public class PreviewCapture {

    public static void main(String[] args) throws Exception {
        String clase = args[0];
        String salida = args[1];
        int ancho = args.length > 2 ? Integer.parseInt(args[2]) : 1400;
        int alto = args.length > 3 ? Integer.parseInt(args[3]) : 820;

        SwingUtilities.invokeLater(() -> {
            try {
                Class<?> clazz = Class.forName(clase);
                Object vista;
                if (JDialog.class.isAssignableFrom(clazz)) {
                    Constructor<?> c = clazz.getConstructor(Frame.class);
                    vista = c.newInstance(new Frame());
                    ((JDialog) vista).setModal(false);
                } else {
                    vista = clazz.getConstructor().newInstance();
                }
                Window ventana = (Window) vista;
                ventana.setSize(ancho, alto);
                ventana.setLocation(60, 40);
                ventana.setVisible(true);
                ventana.toFront();

                Timer timer = new Timer(900, e -> {
                    try {
                        Container cp = ((RootPaneContainer) ventana).getContentPane();
                        cp.doLayout();
                        cp.validate();
                        int w = Math.max(1, cp.getWidth());
                        int h = Math.max(1, cp.getHeight());
                        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = img.createGraphics();
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        cp.printAll(g);
                        g.dispose();
                        ImageIO.write(img, "png", new File(salida));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        ventana.dispose();
                        System.exit(0);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(2);
            }
        });
    }
}
