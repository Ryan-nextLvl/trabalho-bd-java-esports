package br.esports.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Tema {

    // ── Paleta ──────────────────────────────────────────────────
    public static final Color BG_ESCURO   = new Color(10,  10,  20);
    public static final Color BG_PAINEL   = new Color(18,  18,  35);
    public static final Color BG_CARD     = new Color(25,  25,  50);
    public static final Color BG_INPUT    = new Color(30,  30,  55);
    public static final Color BG_LINHA_ALT = new Color(22, 22, 44);

    public static final Color NEON_GREEN  = new Color(0,   255, 136);
    public static final Color NEON_PURPLE = new Color(140, 60,  255);
    public static final Color NEON_CYAN   = new Color(0,   210, 255);
    public static final Color NEON_ORANGE = new Color(255, 120, 30);
    public static final Color NEON_RED    = new Color(255, 55,  90);

    public static final Color TEXTO       = new Color(220, 220, 235);
    public static final Color TEXTO_DIM   = new Color(120, 120, 150);
    public static final Color BORDA       = new Color(50,  50,  90);

    // ── Fontes ──────────────────────────────────────────────────
    public static final Font FONTE_TITULO  = new Font("Segoe UI", Font.BOLD,  20);
    public static final Font FONTE_LABEL   = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONTE_INPUT   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONTE_BTN     = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONTE_TABELA  = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONTE_HEADER  = new Font("Segoe UI", Font.BOLD,  13);

    // ── Bordas ──────────────────────────────────────────────────
    public static Border bordaCard(String titulo) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA, 1, true),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(4, 8, 4, 8),
                titulo,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                FONTE_LABEL, NEON_CYAN)
        );
    }

    public static Border bordaVazia(int v, int h) {
        return BorderFactory.createEmptyBorder(v, h, v, h);
    }

    // ── Fábrica de componentes ───────────────────────────────────
    public static JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(cor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(cor.brighter());
                } else {
                    g2.setColor(cor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(cor.brighter());
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);
        b.setFont(FONTE_BTN);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(b.getPreferredSize().width + 20, 32));
        return b;
    }

    public static JTextField campo(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXTO);
        f.setCaretColor(NEON_GREEN);
        f.setFont(FONTE_INPUT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return f;
    }

    public static JPasswordField campSenha(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXTO);
        f.setCaretColor(NEON_GREEN);
        f.setFont(FONTE_INPUT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return f;
    }

    public static JComboBox<String> combo() {
        JComboBox<String> c = new JComboBox<>();
        c.setBackground(BG_INPUT);
        c.setForeground(TEXTO);
        c.setFont(FONTE_INPUT);
        c.setBorder(BorderFactory.createLineBorder(BORDA, 1));
        ((JComponent) c.getRenderer()).setOpaque(true);
        return c;
    }

    public static JLabel label(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(TEXTO_DIM);
        l.setFont(FONTE_LABEL);
        return l;
    }

    public static void estilizarTabela(JTable tabela) {
        tabela.setBackground(BG_PAINEL);
        tabela.setForeground(TEXTO);
        tabela.setFont(FONTE_TABELA);
        tabela.setRowHeight(28);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setSelectionBackground(NEON_PURPLE.darker());
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(BORDA);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(BG_CARD);
        header.setForeground(NEON_CYAN);
        header.setFont(FONTE_HEADER);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, NEON_PURPLE));
        header.setReorderingAllowed(false);

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? BG_PAINEL : BG_LINHA_ALT);
                    setForeground(TEXTO);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
    }

    public static JScrollPane scrollPane(JTable tabela) {
        JScrollPane sp = new JScrollPane(tabela);
        sp.setBackground(BG_PAINEL);
        sp.getViewport().setBackground(BG_PAINEL);
        sp.setBorder(BorderFactory.createLineBorder(BORDA, 1));
        return sp;
    }
}
