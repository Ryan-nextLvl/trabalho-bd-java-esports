package br.esports.ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField campUsuario = new JTextField(15);
    private final JPasswordField campSenha = new JPasswordField(15);

    public LoginFrame() {
        setTitle("E-Sports Manager – Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(360, 220);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(30, 30, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("E-SPORTS MANAGER", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(0, 200, 130));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        painel.add(titulo, g);

        g.gridwidth = 1;
        g.gridy = 1; g.gridx = 0;
        JLabel lUser = new JLabel("Usuário:");
        lUser.setForeground(Color.WHITE);
        painel.add(lUser, g);
        g.gridx = 1;
        painel.add(campUsuario, g);

        g.gridy = 2; g.gridx = 0;
        JLabel lSenha = new JLabel("Senha:");
        lSenha.setForeground(Color.WHITE);
        painel.add(lSenha, g);
        g.gridx = 1;
        painel.add(campSenha, g);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(0, 180, 100));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEntrar.setFocusPainted(false);
        g.gridy = 3; g.gridx = 0; g.gridwidth = 2;
        painel.add(btnEntrar, g);

        add(painel);

        campSenha.addActionListener(e -> tentarLogin());
        btnEntrar.addActionListener(e -> tentarLogin());
    }

    private void tentarLogin() {
        String usuario = campUsuario.getText().trim();
        String senha = new String(campSenha.getPassword()).trim();
        if ("admin".equals(usuario) && "123".equals(senha)) {
            dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuário ou senha incorretos.", "Erro de Login",
                    JOptionPane.ERROR_MESSAGE);
            campSenha.setText("");
        }
    }
}
