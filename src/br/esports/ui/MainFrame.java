package br.esports.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("E-Sports Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        abas.setBackground(new Color(40, 40, 55));
        abas.setForeground(Color.WHITE);

        abas.addTab("Times",    icon("🏆"), new TimePanel());
        abas.addTab("Jogadores", icon("🎮"), new JogadorPanel());
        abas.addTab("Partidas",  icon("⚔"), new PartidaPanel());
        abas.addTab("Consultas", icon("🔍"), new ConsultaPanel());

        add(abas);

        JLabel rodape = new JLabel("  Logado como: admin", SwingConstants.LEFT);
        rodape.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rodape.setForeground(Color.GRAY);
        add(rodape, BorderLayout.SOUTH);
    }

    private Icon icon(String emoji) {
        return new ImageIcon();
    }
}
