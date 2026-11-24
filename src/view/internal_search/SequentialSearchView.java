package view.internal_search;

import view.ThemedFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista del algoritmo de Búsqueda Secuencial.
 *
 * Estructura responsive:
 *  - NORTH : cabecera (azul oscuro).
 *  - CENTER: panel dividido con BorderLayout -> tabla (CENTER) y controles (EAST).
 *  - SOUTH : pie de página (rojo teja).
 *
 * La tabla se expande con la ventana mientras el panel de controles conserva
 * su ancho preferido y sus componentes alineados (GridBagLayout).
 */
public class SequentialSearchView extends ThemedFrame {

    private JButton btnSearch;
    private JButton btnGenerateArray;
    private JButton btnDeleteValue;
    private JButton btnInsertValue;
    private JButton btnLoadFromFile;
    private JButton btnClearArray;
    private JButton btnBack;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JTextField txtValueToSearch;
    private JTextField txtArraySize;
    private JTextField txtInsertValue;
    private JTextField txtValueToDelete;
    private JTextField txtDigitLimit;
    private JCheckBox chkVisualizeProcess;
    private JLabel lblResult;
    private int currentSearchIndex = -1;
    private int foundIndex = -1;

    // ===================== Paleta de colores =====================
    private static final Color NAVY_HEADER = new Color(0x1A, 0x36, 0x5D); // Azul oscuro
    private static final Color MUSTARD = new Color(0xC5, 0x96, 0x1A);    // Mostaza / amarillo oscuro
    private static final Color BRICK_RED = new Color(0xB8, 0x3A, 0x2A);  // Rojo teja
    private static final Color DARK_GRAY = new Color(0x2F, 0x2F, 0x2F);  // Gris oscuro / negro
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(0x22, 0x22, 0x22);
    private static final Color SUBTITLE = new Color(0xC9, 0xD6, 0xE8);
    private static final Color ROW_SEARCH = new Color(0xFF, 0xE0, 0xB2); // Ámbar claro (búsqueda actual)
    private static final Color ROW_FOUND = new Color(0xC8, 0xE6, 0xC9);  // Verde claro (encontrado)

    // ===================== Tipografías =====================
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.ITALIC, 13);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);

    // Dimensiones fijas para que el panel derecho no se deforme
    private static final int CONTROLS_WIDTH = 500;
    private static final int BUTTON_WIDTH = 135;
    private static final int BUTTON_HEIGHT = 34;

    public SequentialSearchView() {
        setTitle("Búsqueda Secuencial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(WHITE);
        setMinimumSize(new Dimension(1080, 640));
        setSize(1400, 820);
        setLocationRelativeTo(null);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    // ===================== Cabecera =====================
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(NAVY_HEADER);
        header.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel lblTitle = new JLabel("Algoritmo de Búsqueda Secuencial");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Visualización y prueba del algoritmo");
        lblSubtitle.setFont(FONT_SUBTITLE);
        lblSubtitle.setForeground(SUBTITLE);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblTitle);
        header.add(Box.createRigidArea(new Dimension(0, 6)));
        header.add(lblSubtitle);

        return header;
    }

    // ===================== Cuerpo: tabla + controles =====================
    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout(18, 0));
        main.setBackground(WHITE);
        main.setBorder(new EmptyBorder(16, 18, 16, 18));

        // El panel central (tabla) crece; el panel EAST conserva su ancho.
        main.add(createTablePanel(), BorderLayout.CENTER);
        main.add(createControlsPanel(), BorderLayout.EAST);

        return main;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);

        tableModel = new DefaultTableModel(new Object[]{"Posición", "Clave"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        dataTable = new JTable(tableModel);
        dataTable.setFont(FONT_TABLE);
        dataTable.setForeground(TEXT_DARK);
        dataTable.setBackground(WHITE);
        dataTable.setRowHeight(27);
        dataTable.setShowGrid(true);
        dataTable.setGridColor(Color.BLACK);
        dataTable.setIntercellSpacing(new Dimension(1, 1));
        dataTable.setFillsViewportHeight(true);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dataTable.setSelectionBackground(new Color(0xBB, 0xDE, 0xFB));

        // Encabezado mostaza con texto blanco y líneas negras
        JTableHeader header = dataTable.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 32));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setOpaque(true);
                label.setBackground(MUSTARD);
                label.setForeground(WHITE);
                label.setFont(FONT_TABLE_HEADER);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                return label;
            }
        });

        // Renderer para resaltar la búsqueda y el elemento encontrado
        dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (row == foundIndex && foundIndex != -1) {
                    label.setBackground(ROW_FOUND);
                } else if (row == currentSearchIndex && currentSearchIndex != -1) {
                    label.setBackground(ROW_SEARCH);
                } else {
                    label.setBackground(WHITE);
                }

                label.setForeground(TEXT_DARK);
                label.setOpaque(true);
                label.setBorder(new EmptyBorder(0, 8, 0, 8));
                return label;
            }
        });

        // Anchos iniciales proporcionales; el layout se encarga del resto
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(320);
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(480);

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(MUSTARD, 2));
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createControlsPanel() {
        JPanel controls = new JPanel(new GridBagLayout());
        controls.setBackground(WHITE);
        controls.setBorder(new EmptyBorder(4, 14, 4, 4));
        controls.setPreferredSize(new Dimension(CONTROLS_WIDTH, 0));

        // ---------- Componentes ----------
        txtDigitLimit = createStyledTextField();
        txtDigitLimit.setText("2");

        txtArraySize = createStyledTextField();
        txtInsertValue = createStyledTextField();
        txtValueToSearch = createStyledTextField();
        txtValueToDelete = createStyledTextField();

        btnGenerateArray = createStyledButton("Generar Arreglo", MUSTARD);
        btnLoadFromFile = createStyledButton("Seleccionar Arc...", MUSTARD);
        btnInsertValue = createStyledButton("Insertar", BRICK_RED);
        btnSearch = createStyledButton("Buscar", NAVY_HEADER);
        btnDeleteValue = createStyledButton("Eliminar", BRICK_RED);
        btnBack = createStyledButton("Volver", DARK_GRAY);

        chkVisualizeProcess = new JCheckBox("Visualizar proceso de búsqueda");
        chkVisualizeProcess.setFont(FONT_LABEL);
        chkVisualizeProcess.setBackground(WHITE);
        chkVisualizeProcess.setForeground(TEXT_DARK);
        chkVisualizeProcess.setFocusPainted(false);
        chkVisualizeProcess.setSelected(true);
        chkVisualizeProcess.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblResult = new JLabel(" ");
        lblResult.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblResult.setHorizontalAlignment(SwingConstants.CENTER);

        // ---------- Distribución con GridBagLayout ----------
        int row = 0;
        row = addControlRow(controls, row, "Límite de dígitos:", txtDigitLimit, null);
        row = addControlRow(controls, row, "Tamaño del arreglo:", txtArraySize, btnGenerateArray);
        row = addControlRow(controls, row, "Cargar datos desde archivo:", null, btnLoadFromFile);
        row = addControlRow(controls, row, "Insertar una clave:", txtInsertValue, btnInsertValue);
        row = addControlRow(controls, row, "Clave a buscar:", txtValueToSearch, btnSearch);

        // Checkbox alineado a la derecha, ocupando las tres columnas
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(6, 6, 6, 6);
        JPanel checkWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        checkWrapper.setBackground(WHITE);
        checkWrapper.add(chkVisualizeProcess);
        controls.add(checkWrapper, gbc);

        row = addControlRow(controls, row, "Eliminar una clave:", txtValueToDelete, btnDeleteValue);

        // Etiqueta de resultados
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        controls.add(lblResult, gbc);

        // Espacio flexible: empuja el botón Volver al fondo sin deformar el resto
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel glue = new JPanel();
        glue.setOpaque(false);
        controls.add(glue, gbc);

        // Botón Volver (columna de botones)
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(6, 6, 6, 6);
        controls.add(btnBack, gbc);

        return controls;
    }

    /**
     * Agrega una fila "etiqueta | campo | botón". Cualquiera de los dos últimos
     * puede ser null (por ejemplo, "Límite de dígitos" no tiene botón).
     */
    private int addControlRow(JPanel panel, int row, String labelText,
                              JComponent input, JButton button) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Columna 0: etiqueta
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_DARK);
        panel.add(label, gbc);

        // Columna 1: campo de texto (o espaciador si no aplica)
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        if (input != null) {
            panel.add(input, gbc);
        } else {
            JPanel spacer = new JPanel();
            spacer.setOpaque(false);
            panel.add(spacer, gbc);
        }

        // Columna 2: botón de acción (o espacio vacío)
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        if (button != null) {
            panel.add(button, gbc);
        } else {
            JPanel spacer = new JPanel();
            spacer.setOpaque(false);
            spacer.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            panel.add(spacer, gbc);
        }

        return row + 1;
    }

    // ===================== Pie de página =====================
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        footer.setBackground(BRICK_RED);

        JLabel lblInfo = new JLabel(view.Theme.CREDITOS);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(WHITE);

        footer.add(lblInfo);
        return footer;
    }

    // ===================== Fábricas de componentes =====================
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(10);
        textField.setFont(FONT_FIELD);
        textField.setBackground(WHITE);
        textField.setForeground(TEXT_DARK);
        textField.setCaretColor(TEXT_DARK);
        textField.setPreferredSize(new Dimension(120, 32));
        textField.setMinimumSize(new Dimension(90, 32));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BRICK_RED, 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return textField;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        final Color base = backgroundColor;
        JButton button = new JButton(text);
        // BasicButtonUI garantiza que el color de fondo de la paleta se pinte
        // igual sin importar el Look & Feel del sistema operativo.
        button.setUI(new BasicButtonUI());
        button.setFont(FONT_BUTTON);
        button.setBackground(base);
        button.setForeground(WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Dimension size = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(brighten(base, 22));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(base);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(darken(base, 22));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(brighten(base, 22));
            }
        });

        return button;
    }

    private Color brighten(Color color, int amount) {
        return new Color(
                Math.min(255, color.getRed() + amount),
                Math.min(255, color.getGreen() + amount),
                Math.min(255, color.getBlue() + amount)
        );
    }

    private Color darken(Color color, int amount) {
        return new Color(
                Math.max(0, color.getRed() - amount),
                Math.max(0, color.getGreen() - amount),
                Math.max(0, color.getBlue() - amount)
        );
    }

    // ===================== Acciones (usadas por el controlador) =====================
    public void addGenerateArrayListener(ActionListener listener) {
        btnGenerateArray.addActionListener(listener);
    }

    public void addInsertValueListener(ActionListener listener) {
        btnInsertValue.addActionListener(listener);
    }

    public void addSearchListener(ActionListener listener) {
        btnSearch.addActionListener(listener);
    }

    public void addDeleteValueListener(ActionListener listener) {
        btnDeleteValue.addActionListener(listener);
    }

    public void addLoadFromFileListener(ActionListener listener) {
        btnLoadFromFile.addActionListener(listener);
    }

    public void addClearArrayListener(ActionListener listener) {
        if (btnClearArray != null) {
            btnClearArray.addActionListener(listener);
        }
    }

    public void addBackListener(ActionListener listener) {
        btnBack.addActionListener(listener);
    }

    // ===================== Getters =====================
    public String getSearchValue() {
        return txtValueToSearch.getText().trim();
    }

    public String getArraySize() {
        return txtArraySize.getText().trim();
    }

    public String getInsertValue() {
        return txtInsertValue.getText().trim();
    }

    public String getDeleteValue() {
        return txtValueToDelete.getText().trim();
    }

    public String getDigitLimit() {
        return txtDigitLimit.getText().trim();
    }

    public void setResultMessage(String message, boolean isSuccess) {
        lblResult.setText(message);
        lblResult.setForeground(isSuccess ? new Color(46, 125, 50) : new Color(183, 28, 28));
    }

    // ===================== Tabla =====================
    public void setTableData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    public void highlightRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dataTable.getRowCount()) {
            dataTable.setRowSelectionInterval(rowIndex, rowIndex);
            dataTable.scrollRectToVisible(dataTable.getCellRect(rowIndex, 0, true));
        }
    }

    public void highlightSearchProgress(int rowIndex) {
        currentSearchIndex = rowIndex;
        foundIndex = -1;
        dataTable.repaint();

        if (rowIndex >= 0 && rowIndex < dataTable.getRowCount()) {
            dataTable.scrollRectToVisible(dataTable.getCellRect(rowIndex, 0, true));
        }
    }

    public void highlightFoundItem(int rowIndex) {
        currentSearchIndex = -1;
        foundIndex = rowIndex;
        dataTable.repaint();

        if (rowIndex >= 0 && rowIndex < dataTable.getRowCount()) {
            dataTable.scrollRectToVisible(dataTable.getCellRect(rowIndex, 0, true));
        }
    }

    public void clearHighlights() {
        currentSearchIndex = -1;
        foundIndex = -1;
        dataTable.repaint();
    }

    public boolean isVisualizationEnabled() {
        return chkVisualizeProcess.isSelected();
    }

    public void showWindow() {
        setVisible(true);
    }
}
