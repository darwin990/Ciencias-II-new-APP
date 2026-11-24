package view.indexes;

import view.ThemedFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista para el simulador de índice primario
 * Interfaz gráfica que permite configurar parámetros y visualizar resultados
 */
public class PrimaryIndexView extends ThemedFrame {

    // Componentes de entrada
    private JTextField txtRegistros;
    private JTextField txtBloque;
    private JTextField txtDato;
    private JTextField txtIndice;
    private JButton btnCalcular;
    private JButton btnLimpiar;
    private JButton btnVolver;
    private JButton btnSimular;

    // Componentes de salida
    private JTextArea areaResultados;
    private JPanel panelEstructura;
    private JPanel panelSimulacion;

    // Componentes para simulación de búsqueda
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTextArea areaSimulacion;

    public PrimaryIndexView() {
        initializeComponents();
        setupLayout();
        setupStyles();
    }

    /**
     * Inicializa todos los componentes de la vista
     */
    private void initializeComponents() {
        setTitle("Simulador de Índice Primario");
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Campos de entrada con valores por defecto
        txtRegistros = new JTextField("5000", 10);
        txtBloque = new JTextField("4096", 10);
        txtDato = new JTextField("128", 10);
        txtIndice = new JTextField("16", 10);

        // Botones principales
        btnCalcular = new JButton("Calcular Índice");
        btnLimpiar = new JButton("Limpiar");
        btnVolver = new JButton("Volver al Menú");
        btnSimular = new JButton("Simular Búsqueda");

        // Campos para simulación
        txtBuscar = new JTextField("1", 8);
        btnBuscar = new JButton("Buscar");

        // Áreas de resultados
        areaResultados = new JTextArea(12, 60);
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Consolas", Font.PLAIN, 12));

        areaSimulacion = new JTextArea(8, 60);
        areaSimulacion.setEditable(false);
        areaSimulacion.setFont(new Font("Consolas", Font.PLAIN, 12));

        // Panel para estructura visual
        panelEstructura = new JPanel();
        panelEstructura.setLayout(new GridLayout(2, 1, 10, 10));

        // Panel para simulación
        panelSimulacion = new JPanel();
        panelSimulacion.setLayout(new BorderLayout(5, 5));
    }

    /**
     * Configura el layout de la ventana
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Panel principal con padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior: título
        JPanel titlePanel = createTitlePanel();

        // Panel de parámetros
        JPanel inputPanel = createInputPanel();

        // Panel de resultados y simulación (usando JTabbedPane)
        JTabbedPane tabbedPane = createTabbedPane();

        // Panel de botones
        JPanel buttonPanel = createButtonPanel();

        // Laterales: parámetros + botones a la derecha
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(inputPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        rightPanel.add(buttonPanel);

        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.add(rightPanel, BorderLayout.NORTH);
        rightContainer.setPreferredSize(new Dimension(560, 0));

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(rightContainer, BorderLayout.EAST);

        add(mainPanel);
    }

    /**
     * Crea el panel del título
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185)); // Azul primario
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Simulador de Índice Primario");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Configure los parámetros y analice la estructura del índice primario");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(240, 248, 255));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.setLayout(new BorderLayout());
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crea el panel de entrada de parámetros
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
                "Parámetros de Configuración",
                0, 0, new Font("Segoe UI", Font.BOLD, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Número de registros
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Número de registros:"), gbc);
        gbc.gridx = 1;
        txtRegistros.setToolTipText("Registros totales ordenados por clave primaria");
        panel.add(txtRegistros, gbc);

        // Fila 2: Tamaño del bloque
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tamaño del bloque:"), gbc);
        gbc.gridx = 1;
        txtBloque.setToolTipText("Bytes por bloque de disco");
        panel.add(txtBloque, gbc);

        // Fila 3: Longitud del dato
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Longitud del registro de datos:"), gbc);
        gbc.gridx = 1;
        txtDato.setToolTipText("Bytes por registro completo");
        panel.add(txtDato, gbc);

        // Fila 4: Longitud del índice
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Longitud de entrada de índice:"), gbc);
        gbc.gridx = 1;
        txtIndice.setToolTipText("Clave + puntero al bloque");
        panel.add(txtIndice, gbc);

        return panel;
    }

    /**
     * Crea el panel con pestañas para resultados y simulación
     */
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Pestaña de resultados
        JPanel resultsTab = createResultsTab();
        tabbedPane.addTab("📊 Resultados del Cálculo", resultsTab);

        // Pestaña de visualización
        JPanel structureTab = createStructureTab();
        tabbedPane.addTab("🏗️ Estructura Visual", structureTab);

        // Pestaña de simulación
        JPanel simulationTab = createSimulationTab();
        tabbedPane.addTab("🔍 Simulación de Búsqueda", simulationTab);

        return tabbedPane;
    }

    /**
     * Crea la pestaña de resultados
     */
    private JPanel createResultsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        areaResultados.setBackground(new Color(245, 245, 245));
        areaResultados.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(areaResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Análisis Detallado"));

        panel.add(scrollPane);
        return panel;
    }

    /**
     * Crea la pestaña de estructura visual
     */
    private JPanel createStructureTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelEstructura.setBackground(Color.WHITE);
        panelEstructura.setBorder(BorderFactory.createTitledBorder("Representación de la Estructura"));

        JScrollPane scrollPane = new JScrollPane(panelEstructura);
        panel.add(scrollPane);
        return panel;
    }

    /**
     * Crea la pestaña de simulación
     */
    private JPanel createSimulationTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de control de simulación
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control de Búsqueda"));
        controlPanel.add(new JLabel("Buscar registro ID:"));
        controlPanel.add(txtBuscar);
        controlPanel.add(btnBuscar);

        // Área de resultados de simulación
        areaSimulacion.setBackground(new Color(245, 245, 245));
        areaSimulacion.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollSim = new JScrollPane(areaSimulacion);
        scrollSim.setBorder(BorderFactory.createTitledBorder("Pasos de la Búsqueda"));

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollSim, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel de botones
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 8));
        panel.setBackground(new Color(240, 248, 255));

        panel.add(btnCalcular);
        panel.add(btnSimular);
        panel.add(btnLimpiar);
        panel.add(btnVolver);

        return panel;
    }

    /**
     * Configura los estilos de los componentes
     */
    private void setupStyles() {
        // Estilo de la ventana
        getContentPane().setBackground(new Color(240, 248, 255));

        // Estilo de los botones
        styleButton(btnCalcular, new Color(41, 128, 185));
        styleButton(btnSimular, new Color(46, 134, 193));
        styleButton(btnBuscar, new Color(52, 152, 219));
        styleButton(btnLimpiar, new Color(231, 76, 60));
        styleButton(btnVolver, new Color(155, 89, 182));

        // Estilo de los campos de texto
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);
        txtRegistros.setFont(fieldFont);
        txtBloque.setFont(fieldFont);
        txtDato.setFont(fieldFont);
        txtIndice.setFont(fieldFont);
        txtBuscar.setFont(fieldFont);
    }

    /**
     * Aplica estilo a un botón
     */
    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = backgroundColor;
            Color hoverColor = backgroundColor.brighter();

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }

    /**
     * Muestra los resultados del cálculo en el área de texto
     */
    public void mostrarResultados(String resultados) {
        areaResultados.setText(resultados);
    }

    /**
     * Muestra los pasos de simulación de búsqueda
     */
    public void mostrarSimulacion(String simulacion) {
        areaSimulacion.setText(simulacion);
    }

    /**
     * Visualiza la estructura del índice primario
     */
    public void mostrarEstructura(int bloquesDatos, int bloquesIndice, int bfr, int bfri) {
        panelEstructura.removeAll();

        // Panel superior: Bloques de Índice
        JPanel panelIndice = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIndice.setBackground(new Color(255, 250, 205)); // Amarillo claro
        panelIndice.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 193, 7), 2),
                "Archivo de Índice Primario (" + bloquesIndice + " bloques)"
        ));

        int bloquesIndiceAMostrar = Math.min(bloquesIndice, 10);
        for (int i = 1; i <= bloquesIndiceAMostrar; i++) {
            JPanel bloque = new JPanel(new GridLayout(Math.min(bfri, 5), 1, 1, 1));
            bloque.setPreferredSize(new Dimension(80, 100));
            bloque.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7), 2));

            int entradasAMostrar = Math.min(bfri, 5);
            for (int j = 1; j <= entradasAMostrar; j++) {
                JLabel celda = new JLabel("K" + j, SwingConstants.CENTER);
                celda.setOpaque(true);
                celda.setBackground(new Color(255, 235, 59));
                celda.setForeground(Color.BLACK);
                celda.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                celda.setFont(new Font("Arial", Font.BOLD, 9));
                celda.setToolTipText("Clave + Puntero a bloque");
                bloque.add(celda);
            }
            panelIndice.add(bloque);
        }

        if (bloquesIndice > bloquesIndiceAMostrar) {
            JLabel masLabel = new JLabel("... +" + (bloquesIndice - bloquesIndiceAMostrar) + " más");
            masLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            panelIndice.add(masLabel);
        }

        // Panel inferior: Bloques de Datos
        JPanel panelDatos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDatos.setBackground(new Color(230, 255, 230)); // Verde claro
        panelDatos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
                "Archivo de Datos Ordenado (" + bloquesDatos + " bloques)"
        ));

        int bloquesDatosAMostrar = Math.min(bloquesDatos, 10);
        for (int i = 1; i <= bloquesDatosAMostrar; i++) {
            JPanel bloque = new JPanel(new GridLayout(Math.min(bfr, 5), 1, 1, 1));
            bloque.setPreferredSize(new Dimension(80, 100));
            bloque.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 2));

            int registrosAMostrar = Math.min(bfr, 5);
            for (int j = 1; j <= registrosAMostrar; j++) {
                JLabel celda = new JLabel("R" + j, SwingConstants.CENTER);
                celda.setOpaque(true);
                celda.setBackground(new Color(165, 214, 167));
                celda.setForeground(Color.BLACK);
                celda.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                celda.setFont(new Font("Arial", Font.BOLD, 9));
                celda.setToolTipText("Registro de datos completo");
                bloque.add(celda);
            }
            panelDatos.add(bloque);
        }

        if (bloquesDatos > bloquesDatosAMostrar) {
            JLabel masLabel = new JLabel("... +" + (bloquesDatos - bloquesDatosAMostrar) + " más");
            masLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            panelDatos.add(masLabel);
        }

        // Agregar explicación
        JPanel explicacionPanel = new JPanel(new BorderLayout());
        explicacionPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel explicacion = new JLabel(
                "<html><center>" +
                        "🔗 <b>Índice Primario:</b> Una entrada por cada bloque de datos<br>" +
                        "📊 <b>Datos:</b> Registros ordenados por clave primaria<br>" +
                        "⚡ <b>Búsqueda:</b> Índice → Bloque específico de datos" +
                        "</center></html>"
        );
        explicacion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        explicacion.setHorizontalAlignment(SwingConstants.CENTER);
        explicacionPanel.add(explicacion);

        panelEstructura.add(panelIndice);
        panelEstructura.add(panelDatos);
        panelEstructura.add(explicacionPanel);

        panelEstructura.revalidate();
        panelEstructura.repaint();
    }

    /**
     * Limpia todos los campos y resultados
     */
    public void limpiarCampos() {
        txtRegistros.setText("");
        txtBloque.setText("");
        txtDato.setText("");
        txtIndice.setText("");
        txtBuscar.setText("");
        areaResultados.setText("");
        areaSimulacion.setText("");
        panelEstructura.removeAll();
        panelEstructura.revalidate();
        panelEstructura.repaint();
    }

    // Getters para los campos de entrada
    public String getRegistros() { return txtRegistros.getText().trim(); }
    public String getBloque() { return txtBloque.getText().trim(); }
    public String getDato() { return txtDato.getText().trim(); }
    public String getIndice() { return txtIndice.getText().trim(); }
    public String getBuscar() { return txtBuscar.getText().trim(); }

    // Métodos para asignar listeners
    public void addCalcularListener(ActionListener listener) {
        btnCalcular.addActionListener(listener);
    }

    public void addSimularListener(ActionListener listener) {
        btnSimular.addActionListener(listener);
    }

    public void addBuscarListener(ActionListener listener) {
        btnBuscar.addActionListener(listener);
    }

    public void addLimpiarListener(ActionListener listener) {
        btnLimpiar.addActionListener(listener);
    }

    public void addVolverListener(ActionListener listener) {
        btnVolver.addActionListener(listener);
    }

    /**
     * Habilita o deshabilita la simulación
     */
    public void habilitarSimulacion(boolean habilitar) {
        btnSimular.setEnabled(habilitar);
        btnBuscar.setEnabled(habilitar);
        txtBuscar.setEnabled(habilitar);
    }

    /**
     * Muestra la ventana
     */
    public void showWindow() {
        setVisible(true);
    }
}
