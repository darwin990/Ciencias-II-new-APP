package view;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.awt.Frame;

/**
 * Diálogo base de la aplicación.
 *
 * Igual que {@link ThemedFrame} pero para {@link JDialog}: aplica el tema
 * visual automáticamente al hacerse visible.
 */
public class ThemedDialog extends JDialog {

    private boolean temaProgramado = false;
    private boolean temaAplicado = false;

    public ThemedDialog() {
        super();
        setModal(true);
    }

    public ThemedDialog(Frame propietario, String titulo, boolean modal) {
        super(propietario, titulo, modal);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        programarTema();
    }

    protected void programarTema() {
        if (temaProgramado) {
            return;
        }
        temaProgramado = true;
        SwingUtilities.invokeLater(this::aplicarTema);
    }

    public void aplicarTema() {
        if (temaAplicado) {
            return;
        }
        temaAplicado = true;
        Theme.aplicarEstiloVentana(this);
    }
}
