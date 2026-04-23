package br.esports.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("E-Sports Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Tema.BG_ESCURO);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildAbas(),   BorderLayout.CENTER);
        root.add(buildRodape(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(20, 10, 50),
                    getWidth(), 0, new Color(10, 40, 30));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Tema.NEON_PURPLE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
                g2.dispose();
            }
        };
        h.setPreferredSize(new Dimension(0, 64));
        h.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        esquerda.setOpaque(false);

        JLabel icone = new JLabel("⚡");
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel titulo = new JLabel("E-SPORTS MANAGER");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Tema.NEON_GREEN);

        JLabel versao = new JLabel("v1.0");
        versao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versao.setForeground(Tema.TEXTO_DIM);

        esquerda.add(icone);
        esquerda.add(titulo);
        esquerda.add(versao);

        JLabel usuario = new JLabel("admin  ●");
        usuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usuario.setForeground(Tema.NEON_GREEN);

        h.add(esquerda,  BorderLayout.WEST);
        h.add(usuario,   BorderLayout.EAST);
        return h;
    }

    private JTabbedPane buildAbas() {
        JTabbedPane abas = new JTabbedPane() {
            @Override public void updateUI() {
                setUI(new BasicTabbedPaneUI() {
                    @Override protected void installDefaults() {
                        super.installDefaults();
                        highlight = Tema.BG_CARD;
                        lightHighlight = Tema.BG_CARD;
                        shadow = Tema.BG_ESCURO;
                        darkShadow = Tema.BG_ESCURO;
                        focus = Tema.NEON_PURPLE;
                    }
                    @Override protected int calculateTabHeight(int tp, int ti, int fh) { return 40; }
                    @Override protected void paintTabBackground(Graphics g, int tp, int ti,
                            int x, int y, int w, int h, boolean sel) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(sel ? Tema.BG_CARD : Tema.BG_ESCURO);
                        g2.fillRect(x, y, w, h);
                        if (sel) {
                            g2.setColor(Tema.NEON_PURPLE);
                            g2.setStroke(new BasicStroke(3f));
                            g2.drawLine(x, y+h-1, x+w, y+h-1);
                        }
                        g2.dispose();
                    }
                    @Override protected void paintTabBorder(Graphics g, int tp, int ti,
                            int x, int y, int w, int h, boolean sel) {}
                    @Override protected void paintFocusIndicator(Graphics g, int tp,
                            Rectangle[] rs, int ti, Rectangle ir, boolean sel) {}
                    @Override protected void paintContentBorder(Graphics g, int tp, int si) {
                        g.setColor(Tema.BORDA);
                        g.drawLine(0, rects[0].y + rects[0].height,
                                   tabPane.getWidth(),
                                   rects[0].y + rects[0].height);
                    }
                });
            }
        };
        abas.setBackground(Tema.BG_ESCURO);
        abas.setForeground(Tema.TEXTO);
        abas.setFont(new Font("Segoe UI", Font.BOLD, 13));

        abas.addTab("  Times  ",    new TimePanel());
        abas.addTab("  Jogadores  ", new JogadorPanel());
        abas.addTab("  Partidas  ",  new PartidaPanel());
        abas.addTab("  Consultas  ", new ConsultaPanel());

        abas.setForegroundAt(0, Tema.NEON_GREEN);
        abas.setForegroundAt(1, Tema.NEON_CYAN);
        abas.setForegroundAt(2, Tema.NEON_ORANGE);
        abas.setForegroundAt(3, Tema.NEON_PURPLE);

        return abas;
    }

    private JPanel buildRodape() {
        JPanel r = new JPanel(new BorderLayout());
        r.setBackground(Tema.BG_ESCURO);
        r.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDA),
            BorderFactory.createEmptyBorder(4, 16, 4, 16)));
        JLabel txt = new JLabel("Sistema de Gerenciamento de Torneios de E-Sports  •  Banco de Dados");
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txt.setForeground(Tema.TEXTO_DIM);
        r.add(txt, BorderLayout.WEST);
        return r;
    }
}
