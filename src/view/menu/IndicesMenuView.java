package view.menu;

import view.ThemedFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class IndicesMenuView extends ThemedFrame {

    private JButton btnPrimaryIndex;
    private JButton btnSecondaryIndex;
    private JButton btnMultilevelPrimary;
    private JButton btnMultilevelSecondary;
    private JButton btnBack;

    public IndicesMenuView() {
        // Basic window configuration
        setTitle("Algoritmos de Búsqueda - Índices");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Background color consistente con los demás menús
        getContentPane().setBackground(new Color(250, 248, 245));

        // Top panel with title and subtitle
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Back button action placeholder
        btnBack.addActionListener(e -> {
        });
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        // Header consistente con los demás menús
        titlePanel.setBackground(new Color(0, 1, 13));
        titlePanel.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblTitle = new JLabel("Índices");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Seleccione un tipo de índice para continuar");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        // Dorado claro para el subtítulo, consistente con los demás menús
        lblSubtitle.setForeground(new Color(242, 202, 153));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titlePanel.add(lblSubtitle);

        return titlePanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 248, 245));
        mainPanel.setBorder(new EmptyBorder(30, 50, 20, 50));

        // Color estándar para el nivel 2 de menús
        Color level2Color = new Color(166, 133, 93);

        // Create buttons with level 2 color
        btnPrimaryIndex = createStyledButton("Índice Principal", level2Color);
        btnSecondaryIndex = createStyledButton("Índice Secundario", level2Color);
        btnMultilevelPrimary = createStyledButton("Multinivel Principal", level2Color);
        btnMultilevelSecondary = createStyledButton("Multinivel Dinámico", level2Color);
        btnBack = createStyledButton("Volver", new Color(0, 1, 13)); // Color especial para botón volver

        // Main buttons grid (2x2)
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setBackground(new Color(250, 248, 245));
        gridPanel.add(btnPrimaryIndex);
        gridPanel.add(btnSecondaryIndex);
        gridPanel.add(btnMultilevelPrimary);
        gridPanel.add(btnMultilevelSecondary);

        // Back button panel (centered)
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBackground(new Color(250, 248, 245));
        backPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Botón de volver con tamaño más pequeño
        btnBack.setPreferredSize(new Dimension(120, 40));
        btnBack.setMinimumSize(new Dimension(120, 40));
        backPanel.add(btnBack);

        // Combine all panels
        JPanel buttonsContainer = new JPanel(new BorderLayout(0, 0));
        buttonsContainer.setBackground(new Color(250, 248, 245));
        buttonsContainer.add(gridPanel, BorderLayout.CENTER);
        buttonsContainer.add(backPanel, BorderLayout.SOUTH);

        mainPanel.add(buttonsContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Footer consistente con los demás menús
        bottomPanel.setBackground(new Color(245, 240, 235));
        bottomPanel.setBorder(new EmptyBorder(12, 10, 12, 10));

        JLabel lblInfo = new JLabel(view.Theme.CREDITOS);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(39, 89, 80));

        bottomPanel.add(lblInfo);
        return bottomPanel;
    }

    // Method to create a styled button
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tamaño optimizado para los botones principales
        button.setPreferredSize(new Dimension(260, 65));
        button.setMinimumSize(new Dimension(260, 65));
        button.setMaximumSize(new Dimension(260, 65));

        // Efecto hover consistente con los demás menús
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = backgroundColor;
            Color hoverColor = brightenColor(backgroundColor, 20);
            Color pressedColor = darkenColor(backgroundColor, 15);

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
                button.repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
        });

        return button;
    }

    // Métodos auxiliares para manipular colores de forma más sutil
    private Color brightenColor(Color color, int amount) {
        int r = Math.min(255, color.getRed() + amount);
        int g = Math.min(255, color.getGreen() + amount);
        int b = Math.min(255, color.getBlue() + amount);
        return new Color(r, g, b);
    }

    private Color darkenColor(Color color, int amount) {
        int r = Math.max(0, color.getRed() - amount);
        int g = Math.max(0, color.getGreen() - amount);
        int b = Math.max(0, color.getBlue() - amount);
        return new Color(r, g, b);
    }

    // Methods to assign external actions to buttons
    public void addPrimaryIndexListener(ActionListener listener) {
        btnPrimaryIndex.addActionListener(listener);
    }

    public void addSecondaryIndexListener(ActionListener listener) {
        btnSecondaryIndex.addActionListener(listener);
    }

    public void addMultilevelPrimaryListener(ActionListener listener) {
        btnMultilevelPrimary.addActionListener(listener);
    }

    public void addMultilevelSecondaryListener(ActionListener listener) {
        btnMultilevelSecondary.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        for (ActionListener al : btnBack.getActionListeners()) {
            btnBack.removeActionListener(al);
        }
        btnBack.addActionListener(listener);
    }

    // Method to show the window
    public void showWindow() {
        setVisible(true);
    }
}