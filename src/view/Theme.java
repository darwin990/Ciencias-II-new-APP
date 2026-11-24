package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Tema visual único de la aplicación.
 *
 * Centraliza la paleta, las tipografías y la aplicación del estilo a
 * cualquier ventana Swing (JFrame o JDialog):
 *
 *  - Cabecera azul oscuro con título/subtítulo centrados.
 *  - Pie de página rojo teja con los créditos.
 *  - Tablas con encabezado mostaza, filas blancas y cuadrícula negra.
 *  - Campos de texto blancos con borde rojo.
 *  - Botones coloreados según su acción.
 *  - Panel de controles a la derecha y visualizador a la izquierda.
 *
 * Uso mínimo en una ventana:
 *
 *     public class MiVista extends ThemedFrame { ... }
 *
 * o, si no se desea heredar:
 *
 *     Theme.aplicarEstiloVentana(this); // al final del constructor
 */
public final class Theme {

    // ======================= Paleta =======================
    public static final Color AZUL_OSCURO = new Color(0x1A, 0x36, 0x5D);
    public static final Color ROJO_TEJA = new Color(0xB8, 0x3A, 0x2A);
    public static final Color MOSTAZA = new Color(0xC5, 0x96, 0x1A);
    public static final Color GRIS_OSCURO = new Color(0x2F, 0x2F, 0x2F);
    public static final Color BLANCO = Color.WHITE;
    public static final Color TEXTO = new Color(0x22, 0x22, 0x22);
    public static final Color TEXTO_SUAVE = new Color(0x5F, 0x6B, 0x7A);
    public static final Color SUBTITULO = new Color(0xC9, 0xD6, 0xE8);
    public static final Color FILA_BUSQUEDA = new Color(0xFF, 0xE0, 0xB2);
    public static final Color FILA_ENCONTRADA = new Color(0xC8, 0xE6, 0xC9);
    public static final Color BORDE_CAMPO = ROJO_TEJA;

    // ======================= Tipografías =======================
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.ITALIC, 13);
    public static final Font FUENTE_ETIQUETA = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FUENTE_CAMPO = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_TABLA = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_ENCABEZADO_TABLA = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_PIE = new Font("Segoe UI", Font.PLAIN, 12);

    // ======================= Medidas =======================
    /** Ancho inicial del lateral de controles (se ajusta al 38 % de la ventana). */
    public static final int ANCHO_CONTROLES = 420;
    public static final int ANCHO_CONTROLES_MINIMO = 340;
    public static final double FRACCION_CONTROLES = 0.38;
    public static final int ALTO_BOTON = 34;
    public static final int ANCHO_BOTON = 150;

    // ======================= Textos =======================
    public static final String CREDITOS =
            "© 2026 - Nestor Morales - 20212020065 / Santiago Pineda - 20222020055"
            + " - 2026 grupo 2";

    private static final String PROP_HEADER = "theme.header";
    private static final String PROP_FOOTER = "theme.footer";
    private static final String PROP_BOTON = "theme.button";
    private static final String PROP_CAMPO = "theme.field";
    private static final String PROP_BORDER = "theme.border";
    private static final String PROP_SPLIT = "theme.split";
    private static final String PROP_CONTROLES = "theme.controls";

    private static boolean temaGlobalInstalado = false;
    private static final Map<Window, Boolean> VENTANAS_CON_LISTENER =
            new IdentityHashMap<>();

    private Theme() {
        // Clase de utilidades
    }

    // ==================================================================
    //  API principal
    // ==================================================================

    /** Aplica el tema completo a una ventana principal. */
    public static void aplicarEstiloVentana(JFrame ventana) {
        instalarTemaGlobal();
        Container root = ventana.getContentPane();
        root.setBackground(BLANCO);

        // Solo las ventanas con visualizador de datos necesitan el tamaño amplio.
        // Los menús conservan sus dimensiones y disposición originales.
        boolean conDatos = contieneAreaDeDatos(root);
        if (conDatos) {
            ventana.setMinimumSize(new Dimension(980, 600));
        }

        if (root.getLayout() instanceof BorderLayout) {
            estilizarCabecera(root, ventana.getTitle());
            moverControlesALaDerecha(root);
        }

        asegurarFooter(root);
        estilizarComponentes(root);

        // Garantiza espacio suficiente para el layout datos/controles, sin
        // superar el área visible: el scroll interno hace llegar a los botones.
        if (conDatos) {
            Rectangle area = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getMaximumWindowBounds();
            int ancho = Math.min(Math.max(ventana.getWidth(), 1100), area.width);
            int alto = Math.min(Math.max(ventana.getHeight(), 680), area.height);
            ventana.setSize(ancho, alto);
            ventana.setLocation(
                    area.x + (area.width - ancho) / 2,
                    area.y + (area.height - alto) / 2);
        }

        ajustarAnchoControles(ventana);
        instalarListenerAnchoControles(ventana);

        root.revalidate();
        root.repaint();
        SwingUtilities.invokeLater(() -> ajustarSplits(root));
    }

    /** Aplica el tema completo a un diálogo. */
    public static void aplicarEstiloVentana(JDialog dialogo) {
        instalarTemaGlobal();
        Container root = dialogo.getContentPane();
        root.setBackground(BLANCO);
        if (dialogo.getMinimumSize().width == 0) {
            dialogo.setMinimumSize(new Dimension(620, 480));
        }

        if (root.getLayout() instanceof BorderLayout) {
            estilizarCabecera(root, dialogo.getTitle());
        }
        asegurarFooter(root);
        estilizarComponentes(root);

        root.revalidate();
        root.repaint();
        SwingUtilities.invokeLater(() -> ajustarSplits(root));
    }

    /** Instala valores globales de Swing coherentes con la paleta. */
    public static void instalarTemaGlobal() {
        if (temaGlobalInstalado) {
            return;
        }
        temaGlobalInstalado = true;

        UIManager.put("Panel.background", BLANCO);
        UIManager.put("OptionPane.background", BLANCO);
        UIManager.put("OptionPane.messageForeground", TEXTO);
        UIManager.put("Button.background", MOSTAZA);
        UIManager.put("Button.foreground", BLANCO);
        UIManager.put("Button.font", FUENTE_BOTON);
        UIManager.put("Label.foreground", TEXTO);
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("TextField.background", BLANCO);
        UIManager.put("TextField.foreground", TEXTO);
        UIManager.put("TextField.caretForeground", TEXTO);
        UIManager.put("TextArea.background", BLANCO);
        UIManager.put("TextArea.foreground", TEXTO);
        UIManager.put("Table.background", BLANCO);
        UIManager.put("Table.foreground", TEXTO);
        UIManager.put("Table.gridColor", Color.BLACK);
        UIManager.put("Table.selectionBackground", new Color(0xBB, 0xDE, 0xFB));
        UIManager.put("Table.selectionForeground", TEXTO);
        UIManager.put("TitledBorder.titleColor", TEXTO);
        UIManager.put("ToolTip.background", MOSTAZA);
        UIManager.put("ToolTip.foreground", BLANCO);
    }

    // ==================================================================
    //  Cabecera y pie
    // ==================================================================

    public static JPanel crearHeader(String titulo, String subtitulo) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(AZUL_OSCURO);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));
        header.putClientProperty(PROP_HEADER, Boolean.TRUE);

        JLabel lblTitle = new JLabel(titulo == null ? "" : titulo);
        lblTitle.setFont(FUENTE_TITULO);
        lblTitle.setForeground(BLANCO);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblTitle);

        if (subtitulo != null && !subtitulo.trim().isEmpty()) {
            JLabel lblSubtitle = new JLabel(subtitulo);
            lblSubtitle.setFont(FUENTE_SUBTITULO);
            lblSubtitle.setForeground(SUBTITULO);
            lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            header.add(Box.createRigidArea(new Dimension(0, 6)));
            header.add(lblSubtitle);
        }

        return header;
    }

    public static JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        footer.setBackground(ROJO_TEJA);
        footer.putClientProperty(PROP_FOOTER, Boolean.TRUE);

        JLabel lbl = new JLabel(CREDITOS);
        lbl.setFont(FUENTE_PIE);
        lbl.setForeground(BLANCO);
        footer.add(lbl);

        return footer;
    }

    // ==================================================================
    //  Layout: datos a la izquierda, controles a la derecha
    // ==================================================================

    /** Construye el cuerpo responsive: datos (CENTRO) + controles (ESTE). */
    public static JPanel crearCuerpoDatosControles(JComponent datos, JComponent controles) {
        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setBackground(BLANCO);
        cuerpo.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setBackground(BLANCO);
        if (datos != null) {
            izquierda.add(datos, BorderLayout.CENTER);
        }

        cuerpo.add(izquierda, BorderLayout.CENTER);
        cuerpo.add(envolverControles(controles), BorderLayout.EAST);
        return cuerpo;
    }

    /**
     * Envuelve un panel de controles para el lateral derecho. El contenido
     * conserva su tamaño natural: si hay muchos elementos aparecen barras de
     * desplazamiento vertical u horizontal, sin comprimir textos ni botones.
     */
    public static JComponent envolverControles(JComponent controles) {
        controles.setBackground(BLANCO);

        // Algunos paneles fijan un alto preferido de 0; eso oculta el contenido
        // al JScrollPane. Se restaura el tamaño natural para que aparezca la
        // barra de desplazamiento cuando haga falta.
        Dimension pref = controles.getPreferredSize();
        if (pref == null || pref.height <= 0) {
            controles.setPreferredSize(null);
        }

        JPanel caja = new JPanel(new BorderLayout());
        caja.setBackground(BLANCO);
        caja.add(controles, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(caja,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BLANCO);
        scroll.setBackground(BLANCO);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getHorizontalScrollBar().setUnitIncrement(18);
        scroll.setPreferredSize(new Dimension(ANCHO_CONTROLES, 0));
        scroll.setMinimumSize(new Dimension(ANCHO_CONTROLES_MINIMO, 0));
        scroll.putClientProperty(PROP_CONTROLES, Boolean.TRUE);
        return scroll;
    }

    /** Ajusta los laterales de controles a un porcentaje del ancho de la ventana. */
    private static void ajustarAnchoControles(Window ventana) {
        if (!(ventana instanceof RootPaneContainer) || ventana.getWidth() <= 0) {
            return;
        }
        int ancho = Math.max(ANCHO_CONTROLES_MINIMO,
                (int) Math.round(ventana.getWidth() * FRACCION_CONTROLES));
        ajustarAnchoControles(
                ((RootPaneContainer) ventana).getContentPane(), ancho);
    }

    private static void ajustarAnchoControles(Component componente, int ancho) {
        if (componente instanceof JComponent
                && Boolean.TRUE.equals(((JComponent) componente)
                        .getClientProperty(PROP_CONTROLES))) {
            JComponent panel = (JComponent) componente;
            panel.setPreferredSize(new Dimension(ancho, 0));
            panel.setMinimumSize(new Dimension(Math.min(ancho, ANCHO_CONTROLES_MINIMO), 0));
            panel.setMaximumSize(new Dimension(ancho, Integer.MAX_VALUE));
            panel.revalidate();
        }
        if (componente instanceof Container) {
            for (Component hijo : ((Container) componente).getComponents()) {
                ajustarAnchoControles(hijo, ancho);
            }
        }
    }

    /** Mantiene el porcentaje del lateral cuando se redimensiona la ventana. */
    private static void instalarListenerAnchoControles(Window ventana) {
        if (VENTANAS_CON_LISTENER.containsKey(ventana)) {
            return;
        }
        VENTANAS_CON_LISTENER.put(ventana, Boolean.TRUE);
        ventana.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarAnchoControles(ventana);
            }
        });
    }

    /**
     * Convierte un contenedor que ya tiene los datos en CENTER y los controles
     * en SOUTH en el layout definitivo datos-izquierda / controles-derecha.
     */
    public static void controlesALaDerecha(JPanel contenedor, JComponent controles) {
        if (!(contenedor.getLayout() instanceof BorderLayout)) {
            contenedor.add(envolverControles(controles), BorderLayout.EAST);
            return;
        }

        BorderLayout layout = (BorderLayout) contenedor.getLayout();
        Component centro = layout.getLayoutComponent(BorderLayout.CENTER);

        contenedor.remove(controles);
        if (centro != null) {
            contenedor.remove(centro);
        }

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setBackground(BLANCO);
        if (centro != null) {
            cuerpo.add(centro, BorderLayout.CENTER);
        }

        cuerpo.add(envolverControles(controles), BorderLayout.EAST);

        contenedor.add(cuerpo, BorderLayout.CENTER);
    }

    /** Panel blanco pensado para alojar los controles del lado derecho. */
    public static JPanel crearPanelControles() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BLANCO);
        panel.setBorder(new EmptyBorder(6, 12, 6, 6));
        panel.setPreferredSize(new Dimension(ANCHO_CONTROLES, 0));
        return panel;
    }

    public static JPanel crearFilaControl(String etiqueta, JComponent campo, JButton boton) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        fila.setBackground(BLANCO);

        if (etiqueta != null) {
            JLabel lbl = new JLabel(etiqueta);
            lbl.setFont(FUENTE_ETIQUETA);
            lbl.setForeground(TEXTO);
            fila.add(lbl);
        }
        if (campo != null) {
            fila.add(campo);
        }
        if (boton != null) {
            fila.add(boton);
        }
        return fila;
    }

    // ==================================================================
    //  Componentes
    // ==================================================================

    public static JTextField crearTextField(int columnas) {
        JTextField campo = new JTextField(columnas);
        estilizarCampo(campo);
        return campo;
    }

    public static JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        estilizarBoton(boton);
        return boton;
    }

    public static void estilizarBoton(JButton boton) {
        if (Boolean.TRUE.equals(boton.getClientProperty(PROP_BOTON))) {
            return;
        }
        boton.putClientProperty(PROP_BOTON, Boolean.TRUE);

        String texto = boton.getText() == null ? "" : boton.getText();
        Color base = colorParaBoton(texto);

        boton.setUI(new BasicButtonUI());
        boton.setBackground(base);
        boton.setForeground(BLANCO);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(8, 12, 8, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        float tam = boton.getFont() == null ? 13f : boton.getFont().getSize2D();
        boton.setFont(new Font("Segoe UI", Font.BOLD, Math.max(13, Math.round(tam))));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(aclarar(base, 22));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(base);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boton.setBackground(oscurecer(base, 22));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boton.setBackground(aclarar(base, 22));
            }
        });
    }

    /** Regla de color de botones solicitada en el diseño. */
    public static Color colorParaBoton(String texto) {
        String t = texto == null ? "" : texto.toLowerCase();

        if (t.contains("volver") || t.contains("atrás") || t.contains("atras")
                || t.contains("salir") || t.contains("cancelar") || t.contains("cerrar")) {
            return GRIS_OSCURO;
        }
        if (t.contains("buscar") || t.contains("consultar") || t.contains("localizar")) {
            return AZUL_OSCURO;
        }
        if (t.contains("eliminar") || t.contains("borrar") || t.contains("remover")
                || t.contains("insertar") || t.contains("agregar") || t.contains("añadir")
                || t.contains("anadir")) {
            return ROJO_TEJA;
        }
        return MOSTAZA;
    }

    public static void estilizarCampo(JTextField campo) {
        if (Boolean.TRUE.equals(campo.getClientProperty(PROP_CAMPO))) {
            return;
        }
        campo.putClientProperty(PROP_CAMPO, Boolean.TRUE);
        campo.setFont(FUENTE_CAMPO);
        campo.setBackground(BLANCO);
        campo.setForeground(TEXTO);
        campo.setCaretColor(TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_CAMPO, 1),
                new EmptyBorder(4, 8, 4, 8)));
        if (campo.getPreferredSize().width < 80) {
            campo.setPreferredSize(new Dimension(120, 32));
        }
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_TABLA);
        tabla.setForeground(TEXTO);
        tabla.setBackground(BLANCO);
        tabla.setGridColor(Color.BLACK);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setFillsViewportHeight(true);
        if (tabla.getRowHeight() < 24) {
            tabla.setRowHeight(26);
        }
        if (tabla.getAutoResizeMode() == JTable.AUTO_RESIZE_OFF) {
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }

        JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setReorderingAllowed(false);
            if (!(header.getDefaultRenderer() instanceof ThemeHeaderRenderer)) {
                header.setDefaultRenderer(new ThemeHeaderRenderer());
            }
            if (header.getPreferredSize().height < 30) {
                header.setPreferredSize(new Dimension(0, 32));
            }
        }
    }

    public static void estilizarPanel(JPanel panel) {
        panel.setBackground(BLANCO);
        restilizarTitledBorder(panel);
    }

    // ==================================================================
    //  Recorrido recursivo de componentes
    // ==================================================================

    private static void estilizarComponentes(Component componente) {
        if (componente instanceof JPanel) {
            JPanel panel = (JPanel) componente;
            if (esCabecera(panel) || esPie(panel)) {
                return;
            }
            panel.setBackground(BLANCO);
            restilizarTitledBorder(panel);
        }

        if (componente instanceof JTable) {
            estilizarTabla((JTable) componente);
        } else if (componente instanceof JTextField) {
            estilizarCampo((JTextField) componente);
        } else if (componente instanceof JButton) {
            estilizarBoton((JButton) componente);
        } else if (componente instanceof JCheckBox) {
            JCheckBox check = (JCheckBox) componente;
            check.setBackground(BLANCO);
            check.setForeground(TEXTO);
            check.setFont(FUENTE_ETIQUETA);
            check.setFocusPainted(false);
        } else if (componente instanceof JTextArea) {
            JTextArea area = (JTextArea) componente;
            area.setBackground(BLANCO);
            area.setForeground(TEXTO);
            area.setCaretColor(TEXTO);
            restilizarTitledBorder(area);
        } else if (componente instanceof JTextPane) {
            JTextPane area = (JTextPane) componente;
            area.setBackground(BLANCO);
            area.setForeground(TEXTO);
            restilizarTitledBorder(area);
        } else if (componente instanceof JSplitPane) {
            JSplitPane split = (JSplitPane) componente;
            split.setBackground(BLANCO);
            split.setBorder(new EmptyBorder(0, 0, 0, 0));
            split.setDividerSize(6);
        } else if (componente instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) componente;
            combo.setBackground(BLANCO);
            combo.setForeground(TEXTO);
            combo.setFont(FUENTE_CAMPO);
        } else if (componente instanceof JSpinner) {
            JSpinner spinner = (JSpinner) componente;
            spinner.setBackground(BLANCO);
            spinner.setForeground(TEXTO);
            spinner.setFont(FUENTE_CAMPO);
        } else if (componente instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) componente;
            tabs.setFont(FUENTE_ETIQUETA);
            tabs.setBackground(BLANCO);
            tabs.setForeground(AZUL_OSCURO);
        } else if (componente instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) componente;
            scroll.getViewport().setBackground(BLANCO);
            scroll.setBackground(BLANCO);
            Component vista = scroll.getViewport().getView();
            if (tieneTitulo(scroll.getBorder())) {
                restilizarTitledBorder(scroll);
            } else if (vista instanceof JTable) {
                scroll.setBorder(BorderFactory.createLineBorder(MOSTAZA, 2));
            }
        }

        if (componente instanceof Container) {
            for (Component hijo : ((Container) componente).getComponents()) {
                estilizarComponentes(hijo);
            }
        }
    }

    private static void restilizarTitledBorder(JComponent componente) {
        if (Boolean.TRUE.equals(componente.getClientProperty(PROP_BORDER))) {
            return;
        }

        Border actual = componente.getBorder();
        if (actual == null) {
            return;
        }

        TitledBorder titulo = null;
        Border interior = null;

        if (actual instanceof TitledBorder) {
            titulo = (TitledBorder) actual;
        } else if (actual instanceof CompoundBorder) {
            CompoundBorder compuesto = (CompoundBorder) actual;
            if (compuesto.getOutsideBorder() instanceof TitledBorder) {
                titulo = (TitledBorder) compuesto.getOutsideBorder();
                interior = compuesto.getInsideBorder();
            } else if (compuesto.getInsideBorder() instanceof TitledBorder) {
                titulo = (TitledBorder) compuesto.getInsideBorder();
                interior = compuesto.getOutsideBorder();
            }
        }

        if (titulo == null) {
            return;
        }

        String texto = titulo.getTitle();
        if (texto == null || texto.trim().isEmpty()) {
            return;
        }

        int justificacion = titulo.getTitleJustification();
        if (justificacion == TitledBorder.DEFAULT_JUSTIFICATION) {
            justificacion = TitledBorder.LEADING;
        }

        int posicion = titulo.getTitlePosition();
        if (posicion == TitledBorder.DEFAULT_POSITION) {
            posicion = TitledBorder.TOP;
        }

        TitledBorder nuevo = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MOSTAZA, 1),
                texto,
                justificacion,
                posicion,
                FUENTE_ETIQUETA,
                AZUL_OSCURO);

        Border relleno = (interior instanceof EmptyBorder)
                ? interior
                : new EmptyBorder(8, 10, 8, 10);

        componente.setBorder(BorderFactory.createCompoundBorder(nuevo, relleno));
        componente.putClientProperty(PROP_BORDER, Boolean.TRUE);
    }

    // ==================================================================
    //  Cabecera automática
    // ==================================================================

    private static void estilizarCabecera(Container root, String tituloVentana) {
        JPanel header = buscarCabecera(root);

        if (header == null) {
            JPanel creado = crearHeader(tituloVentana == null ? "" : tituloVentana, "");
            Component norte = ((BorderLayout) root.getLayout())
                    .getLayoutComponent(BorderLayout.NORTH);
            if (norte != null) {
                root.remove(norte);
                JPanel wrapper = new JPanel(new BorderLayout());
                wrapper.setBackground(BLANCO);
                wrapper.add(creado, BorderLayout.NORTH);
                wrapper.add(norte, BorderLayout.CENTER);
                root.add(wrapper, BorderLayout.NORTH);
            } else {
                root.add(creado, BorderLayout.NORTH);
            }
            return;
        }

        header.putClientProperty(PROP_HEADER, Boolean.TRUE);
        header.setBackground(AZUL_OSCURO);
        header.setOpaque(true);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));
        pintarCabecera(header);
        estilizarEtiquetasCabecera(header);
    }

    private static void pintarCabecera(Container header) {
        for (Component c : header.getComponents()) {
            if (c instanceof JPanel) {
                ((JPanel) c).setBackground(AZUL_OSCURO);
                ((JPanel) c).setOpaque(true);
                pintarCabecera((Container) c);
            }
        }
    }

    private static void estilizarEtiquetasCabecera(Container header) {
        for (Component c : header.getComponents()) {
            if (c instanceof JLabel) {
                JLabel lbl = (JLabel) c;
                float tam = lbl.getFont() == null ? 12f : lbl.getFont().getSize2D();
                if (tam >= 17f) {
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, Math.max(22, Math.round(tam))));
                    lbl.setForeground(BLANCO);
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                } else {
                    lbl.setFont(FUENTE_SUBTITULO);
                    lbl.setForeground(SUBTITULO);
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                }
            } else if (c instanceof JPanel) {
                estilizarEtiquetasCabecera((Container) c);
            }
        }
    }

    private static JPanel buscarCabecera(Container root) {
        ArrayDeque<Container> cola = new ArrayDeque<>();
        Map<Container, Integer> profundidad = new IdentityHashMap<>();
        cola.add(root);
        profundidad.put(root, 0);

        while (!cola.isEmpty()) {
            Container actual = cola.poll();
            int nivel = profundidad.get(actual);

            for (Component hijo : actual.getComponents()) {
                if (hijo instanceof JPanel) {
                    JPanel panel = (JPanel) hijo;
                    if (esPanelDeTitulo(panel)) {
                        return panel;
                    }
                    if (nivel < 3) {
                        cola.add(panel);
                        profundidad.put(panel, nivel + 1);
                    }
                }
            }
        }
        return null;
    }

    private static boolean esPanelDeTitulo(JPanel panel) {
        if (esPie(panel)) {
            return false;
        }
        if (!contieneTituloGrande(panel)) {
            return false;
        }
        // No confundir un contenedor de contenido con la cabecera
        return !contieneDatosOControles(panel);
    }

    private static boolean contieneTituloGrande(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JLabel) {
                Font f = ((JLabel) c).getFont();
                if (f != null && f.getSize2D() >= 17f) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean contieneDatosOControles(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JTable || c instanceof JScrollPane || c instanceof JSplitPane
                    || c instanceof JTabbedPane || c instanceof JTextArea
                    || c instanceof JTextPane || c instanceof JTextField
                    || c instanceof JButton || c instanceof JComboBox
                    || c instanceof JSpinner || c instanceof JCheckBox) {
                return true;
            }
            if (c instanceof Container && contieneDatosOControles((Container) c)) {
                return true;
            }
        }
        return false;
    }

    // ==================================================================
    //  Pie automático
    // ==================================================================

    private static void asegurarFooter(Container root) {
        JLabel creditos = buscarCreditos(root);
        if (creditos != null) {
            creditos.setText(CREDITOS);
            creditos.setFont(FUENTE_PIE);
            creditos.setForeground(BLANCO);
            creditos.setHorizontalAlignment(SwingConstants.CENTER);
            Container padre = creditos.getParent();
            if (padre instanceof JPanel) {
                JPanel pie = (JPanel) padre;
                pie.putClientProperty(PROP_FOOTER, Boolean.TRUE);
                pie.setBackground(ROJO_TEJA);
                pie.setOpaque(true);
                pie.setBorder(new EmptyBorder(8, 10, 8, 10));
            }
            return;
        }

        if (!(root.getLayout() instanceof BorderLayout)) {
            return;
        }

        BorderLayout layout = (BorderLayout) root.getLayout();
        Component sur = layout.getLayoutComponent(BorderLayout.SOUTH);

        if (sur instanceof JPanel && !esPie((JPanel) sur)) {
            root.remove(sur);
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(BLANCO);
            wrapper.add(sur, BorderLayout.CENTER);
            wrapper.add(crearFooter(), BorderLayout.SOUTH);
            root.add(wrapper, BorderLayout.SOUTH);
        } else if (sur == null) {
            root.add(crearFooter(), BorderLayout.SOUTH);
        }
    }

    private static JLabel buscarCreditos(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JLabel) {
                String texto = ((JLabel) c).getText();
                if (texto != null && (texto.contains("©")
                        || texto.toLowerCase().contains("nestor morales"))) {
                    return (JLabel) c;
                }
            }
            if (c instanceof Container) {
                JLabel encontrado = buscarCreditos((Container) c);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }

    // ==================================================================
    //  Reubicación automática de controles (SOUTH -> EAST)
    // ==================================================================

    private static void moverControlesALaDerecha(Container contenedor) {
        if (contenedor instanceof JPanel) {
            JPanel panel = (JPanel) contenedor;
            if (panel.getLayout() instanceof BorderLayout) {
                BorderLayout bl = (BorderLayout) panel.getLayout();
                Component centro = bl.getLayoutComponent(BorderLayout.CENTER);
                Component sur = bl.getLayoutComponent(BorderLayout.SOUTH);

                if (sur instanceof JPanel && !esPie((JPanel) sur)
                        && contieneControles((Container) sur)
                        && esVisualizador(centro)) {
                    panel.remove(sur);
                    panel.add(envolverControles((JComponent) sur), BorderLayout.EAST);
                }
            }
        }

        if (contenedor instanceof Container) {
            for (Component hijo : ((Container) contenedor).getComponents()) {
                if (hijo instanceof Container) {
                    moverControlesALaDerecha((Container) hijo);
                }
            }
        }
    }

    private static boolean contieneControles(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            // Las barras de desplazamiento incluyen botones internos de la L&F
            // (MetalScrollButton, WindowsArrowButton...): no son controles.
            if (c instanceof JScrollBar) {
                continue;
            }
            if (c instanceof JTextField) {
                return true;
            }
            if (c instanceof JButton) {
                String texto = ((JButton) c).getText();
                if (texto != null && !texto.trim().isEmpty()) {
                    return true;
                }
            }
            if (c instanceof Container && contieneControles((Container) c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean esVisualizador(Component componente) {
        if (componente instanceof JTable || componente instanceof JScrollPane
                || componente instanceof JTabbedPane || componente instanceof JSplitPane) {
            return true;
        }
        if (componente instanceof Container) {
            Container contenedor = (Container) componente;
            for (Component c : contenedor.getComponents()) {
                if (c instanceof JTable) {
                    return true;
                }
                if (c instanceof Container && esVisualizador(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Indica si la ventana muestra datos (tablas, listas, árboles o áreas). */
    private static boolean contieneAreaDeDatos(Component componente) {
        if (componente instanceof JTable || componente instanceof JScrollPane
                || componente instanceof JSplitPane || componente instanceof JTabbedPane
                || componente instanceof JList || componente instanceof JTree
                || componente instanceof JTextArea || componente instanceof JTextPane) {
            return true;
        }
        if (componente instanceof Container) {
            for (Component hijo : ((Container) componente).getComponents()) {
                if (contieneAreaDeDatos(hijo)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ==================================================================
    //  Utilidades
    // ==================================================================

    private static boolean esCabecera(JPanel panel) {
        return Boolean.TRUE.equals(panel.getClientProperty(PROP_HEADER));
    }

    private static boolean esPie(JPanel panel) {
        return Boolean.TRUE.equals(panel.getClientProperty(PROP_FOOTER));
    }

    /** ¿El borde es (o contiene) un TitledBorder? */
    private static boolean tieneTitulo(Border borde) {
        if (borde instanceof TitledBorder) {
            return true;
        }
        if (borde instanceof CompoundBorder) {
            CompoundBorder compuesto = (CompoundBorder) borde;
            return tieneTitulo(compuesto.getOutsideBorder())
                    || tieneTitulo(compuesto.getInsideBorder());
        }
        return false;
    }

    /**
     * Mantiene los JSplitPane en una proporción cómoda (60/40) y la conserva
     * al maximizar o redimensionar la ventana.
     */
    private static void ajustarSplits(Container contenedor) {
        for (Component hijo : contenedor.getComponents()) {
            if (hijo instanceof JSplitPane) {
                JSplitPane split = (JSplitPane) hijo;
                if (!Boolean.TRUE.equals(split.getClientProperty(PROP_SPLIT))) {
                    split.putClientProperty(PROP_SPLIT, Boolean.TRUE);
                    split.setResizeWeight(0.6);
                    split.setDividerLocation(0.6);
                    split.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentResized(ComponentEvent e) {
                            split.setDividerLocation(0.6);
                        }
                    });
                }
            }
            if (hijo instanceof Container) {
                ajustarSplits((Container) hijo);
            }
        }
    }
    private static Color aclarar(Color color, int cantidad) {
        return new Color(
                Math.min(255, color.getRed() + cantidad),
                Math.min(255, color.getGreen() + cantidad),
                Math.min(255, color.getBlue() + cantidad));
    }

    private static Color oscurecer(Color color, int cantidad) {
        return new Color(
                Math.max(0, color.getRed() - cantidad),
                Math.max(0, color.getGreen() - cantidad),
                Math.max(0, color.getBlue() - cantidad));
    }

    private static final class ThemeHeaderRenderer extends DefaultTableCellRenderer
            implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setOpaque(true);
            label.setBackground(MOSTAZA);
            label.setForeground(BLANCO);
            label.setFont(FUENTE_ENCABEZADO_TABLA);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            return label;
        }
    }
}
