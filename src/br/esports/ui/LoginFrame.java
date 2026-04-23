package br.esports.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    private final JTextField    campUsuario = Tema.campo(16);
    private final JPasswordField campSenha  = Tema.campSenha(16);

    public LoginFrame() {
        setTitle("E-Sports Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setSize(420, 340);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, 420, 340, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(10, 10, 25),
                    getWidth(), getHeight(), new Color(25, 10, 50));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        root.setBorder(BorderFactory.createLineBorder(Tema.NEON_PURPLE, 1));

        // ── Barra de arrastar ────────────────────────────────────
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        barra.setOpaque(false);
        JButton btnFechar = new JButton("✕");
        btnFechar.setForeground(Tema.TEXTO_DIM);
        btnFechar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnFechar.setFocusPainted(false);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> System.exit(0));
        btnFechar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnFechar.setForeground(Tema.NEON_RED); }
            public void mouseExited(MouseEvent e)  { btnFechar.setForeground(Tema.TEXTO_DIM); }
        });
        barra.add(btnFechar);
        final Point[] dragPt = {null};
        barra.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { dragPt[0] = e.getPoint(); }
        });
        barra.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - dragPt[0].x, loc.y + e.getY() - dragPt[0].y);
            }
        });

        // ── Logo / Título ────────────────────────────────────────
        JPanel topoPanel = new JPanel();
        topoPanel.setOpaque(false);
        topoPanel.setLayout(new BoxLayout(topoPanel, BoxLayout.Y_AXIS));
        topoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel icone = new JLabel("⚡", SwingConstants.CENTER);
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        icone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("E-SPORTS MANAGER", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Tema.NEON_GREEN);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Sistema de Torneios", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(Tema.TEXTO_DIM);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        topoPanel.add(icone);
        topoPanel.add(Box.createVerticalStrut(4));
        topoPanel.add(titulo);
        topoPanel.add(Box.createVerticalStrut(2));
        topoPanel.add(sub);

        // ── Formulário ───────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 20, 6, 20);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        form.add(topoPanel, g);

        g.gridwidth = 1; g.gridy = 1; g.gridx = 0; g.weightx = 0;
        form.add(Tema.label("Usuário"), g);
        g.gridx = 1; g.weightx = 1;
        form.add(campUsuario, g);

        g.gridy = 2; g.gridx = 0; g.weightx = 0;
        form.add(Tema.label("Senha"), g);
        g.gridx = 1; g.weightx = 1;
        form.add(campSenha, g);

        JButton btnEntrar = Tema.botao("  ENTRAR  ", Tema.NEON_PURPLE);
        btnEntrar.setPreferredSize(new Dimension(200, 36));
        g.gridy = 3; g.gridx = 0; g.gridwidth = 2; g.weightx = 0;
        g.insets = new Insets(14, 80, 10, 80);
        form.add(btnEntrar, g);

        root.add(barra, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        setContentPane(root);

        campSenha.addActionListener(e -> tentarLogin());
        btnEntrar.addActionListener(e -> tentarLogin());
        campUsuario.requestFocusInWindow();
    }

    private void tentarLogin() {
        String u = campUsuario.getText().trim();
        String s = new String(campSenha.getPassword()).trim();
        if ("admin".equals(u) && "123".equals(s)) {
            dispose();
            new MainFrame().setVisible(true);
        } else {
            campSenha.setText("");
            campSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.NEON_RED, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            JOptionPane.showMessageDialog(this,
                "Usuário ou senha incorretos.", "Acesso negado",
                JOptionPane.ERROR_MESSAGE);
            campSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDA, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        }
    }
}
