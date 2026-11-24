package view.external_search;

import view.ThemedFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Adaptador para usar HashExpansionView con el título de expansión parcial.
 *
 * El estilo visual ya no se define aquí: lo aplica centralizadamente
 * {@link view.Theme} a través de {@link ThemedFrame}.
 */
public class PartialExpansionView extends JFrame {
    private HashExpansionView view;

    public PartialExpansionView() {
        this.view = new HashExpansionView();

        // Cambiar el título para diferenciar la variante parcial
        this.view.setTitle("Expansión y Reducción Parcial - Algoritmo de Hashing Dinámico");

        // Configurar esta ventana como un proxy invisible
        this.setVisible(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public HashExpansionView getView() {
        return view;
    }
}
