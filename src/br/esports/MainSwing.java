package br.esports;

import br.esports.ui.LoginFrame;

import javax.swing.*;

public class MainSwing {
    public static void main(String[] args) {
        // Aplica Nimbus como base e sobrepõe com o tema escuro customizado
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Sobrescreve cores globais do Nimbus com o tema escuro
        UIManager.put("control",           new java.awt.Color(18, 18, 35));
        UIManager.put("info",              new java.awt.Color(18, 18, 35));
        UIManager.put("nimbusBase",        new java.awt.Color(10, 10, 25));
        UIManager.put("nimbusBlueGrey",    new java.awt.Color(30, 30, 55));
        UIManager.put("nimbusLightBackground", new java.awt.Color(25, 25, 50));
        UIManager.put("text",              new java.awt.Color(220, 220, 235));
        UIManager.put("nimbusSelectionBackground", new java.awt.Color(100, 40, 200));
        UIManager.put("nimbusSelectedText", java.awt.Color.WHITE);
        UIManager.put("OptionPane.background",     new java.awt.Color(18, 18, 35));
        UIManager.put("Panel.background",          new java.awt.Color(18, 18, 35));

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
