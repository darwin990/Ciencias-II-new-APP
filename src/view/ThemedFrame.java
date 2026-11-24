package view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Ventana base de la aplicación.
 *
 * Hereda de {@link JFrame} y aplica automáticamente el tema visual
 * ({@link Theme}) cuando la ventana se hace visible. De este modo todas las
 * pantallas comparten cabecera azul, pie rojo, tabla mostaza, campos con borde
 * rojo y el layout datos-izquierda / controles-derecha sin repetir código.
 *
 * Para migrar una vista basta con cambiar:
 *
 *     public class MiVista extends JFrame { ... }
 *
 * por:
 *
 *     import view.ThemedFrame;
 *
 *     public class MiVista extends ThemedFrame { ... }
 */
public class ThemedFrame extends JFrame {

    private boolean temaProgramado = false;
    private boolean temaAplicado = false;

    public ThemedFrame() {
        super();
    }

    public ThemedFrame(String titulo) {
        super(titulo);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        programarTema();
    }

    /**
     * Programa la aplicación del tema después de que la ventana quede
     * visible, para no interferir con el constructor de cada vista.
     */
    protected void programarTema() {
        if (temaProgramado) {
            return;
        }
        temaProgramado = true;
        SwingUtilities.invokeLater(this::aplicarTema);
    }

    /** Aplica el tema una sola vez (es idempotente). */
    public void aplicarTema() {
        if (temaAplicado) {
            return;
        }
        temaAplicado = true;
        Theme.aplicarEstiloVentana(this);
    }
}
