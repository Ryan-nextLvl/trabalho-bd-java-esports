package br.esports.ui;

import br.esports.model.Jogador;
import br.esports.model.Time;
import br.esports.service.JogadorService;
import br.esports.service.TimeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JogadorPanel extends JPanel {

    private final JogadorService service     = new JogadorService();
    private final TimeService    timeService = new TimeService();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nickname", "ELO", "Time"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    private final JTextField    fNick = Tema.campo(15);
    private final JTextField    fElo  = Tema.campo(8);
    private final JComboBox<String> cbTime = Tema.combo();
    private List<Time> times;

    public JogadorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.BG_PAINEL);
        setBorder(Tema.bordaVazia(12, 14));
        Tema.estilizarTabela(tabela);
        add(buildFormulario(),        BorderLayout.NORTH);
        add(Tema.scrollPane(tabela),  BorderLayout.CENTER);
        add(buildBotoes(),            BorderLayout.SOUTH);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });
        carregarTimes();
        carregarTabela();
    }

    private JPanel buildFormulario() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        p.setBackground(Tema.BG_CARD);
        p.setBorder(Tema.bordaCard("Dados do Jogador"));
        p.add(Tema.label("Nickname:")); p.add(fNick);
        p.add(Tema.label("ELO:"));      p.add(fElo);
        p.add(Tema.label("Time:"));     p.add(cbTime);
        return p;
    }

    private JPanel buildBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        p.setBackground(Tema.BG_PAINEL);

        JButton bCadastrar = Tema.botao("＋ Cadastrar",  Tema.NEON_CYAN.darker());
        JButton bAtualizar = Tema.botao("✎ Atualizar",  new Color(0, 100, 200));
        JButton bExcluir   = Tema.botao("✕ Excluir",    Tema.NEON_RED.darker());
        JButton bLimpar    = Tema.botao("↺ Limpar",     new Color(50, 50, 80));

        bCadastrar.addActionListener(e -> cadastrar());
        bAtualizar.addActionListener(e -> atualizar());
        bExcluir.addActionListener(e -> excluir());
        bLimpar.addActionListener(e -> limpar());

        p.add(bCadastrar); p.add(bAtualizar); p.add(bExcluir); p.add(bLimpar);
        return p;
    }

    private void cadastrar() {
        try { service.cadastrar(lerFormulario(-1)); carregarTabela(); limpar(); }
        catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void atualizar() {
        int id = idSelecionado(); if (id < 0) return;
        try { service.atualizar(lerFormulario(id)); carregarTabela(); limpar(); }
        catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void excluir() {
        int id = idSelecionado(); if (id < 0) return;
        if (JOptionPane.showConfirmDialog(this, "Excluir jogador?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.excluir(id); carregarTabela(); limpar();
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        for (Jogador j : service.listarTodos())
            modelo.addRow(new Object[]{j.getId(), j.getNickname(), j.getElo(),
                    j.getNomeTime() != null ? j.getNomeTime() : "—"});
    }

    private void carregarTimes() {
        times = timeService.listarTodos();
        cbTime.removeAllItems();
        cbTime.addItem("— Sem time —");
        for (Time t : times) cbTime.addItem("[" + t.getId() + "] " + t.getNome());
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow(); if (row < 0) return;
        fNick.setText((String) modelo.getValueAt(row, 1));
        fElo.setText(String.valueOf(modelo.getValueAt(row, 2)));
        String nt = (String) modelo.getValueAt(row, 3);
        for (int i = 0; i < cbTime.getItemCount(); i++)
            if (cbTime.getItemAt(i).contains(nt)) { cbTime.setSelectedIndex(i); break; }
    }

    private Jogador lerFormulario(int id) {
        String nick = fNick.getText().trim();
        if (nick.isBlank()) throw new IllegalArgumentException("Nickname é obrigatório.");
        Jogador j = new Jogador(); j.setId(id); j.setNickname(nick);
        String eloStr = fElo.getText().trim();
        j.setElo(eloStr.isBlank() ? 1000 : Integer.parseInt(eloStr));
        int idx = cbTime.getSelectedIndex();
        if (idx > 0) j.setFkTime(times.get(idx - 1).getId());
        return j;
    }

    private int idSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { erro("Selecione um jogador."); return -1; }
        return (int) modelo.getValueAt(row, 0);
    }

    private void limpar() {
        fNick.setText(""); fElo.setText(""); cbTime.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
