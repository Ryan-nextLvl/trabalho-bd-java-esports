package br.esports.ui;

import br.esports.model.Time;
import br.esports.service.TimeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TimePanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TimeService service = new TimeService();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nome", "Tag", "Fundação", "Pontuação"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    private final JTextField fNome  = Tema.campo(18);
    private final JTextField fTag   = Tema.campo(8);
    private final JTextField fData  = Tema.campo(10);
    private final JTextField fPonts = Tema.campo(8);

    public TimePanel() {
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
        carregarTabela();
    }

    private JPanel buildFormulario() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        p.setBackground(Tema.BG_CARD);
        p.setBorder(Tema.bordaCard("Dados do Time"));

        p.add(Tema.label("Nome:"));       p.add(fNome);
        p.add(Tema.label("Tag:"));        p.add(fTag);
        p.add(Tema.label("Fundação:"));   p.add(fData);
        fData.setToolTipText("Formato: dd/MM/yyyy");
        p.add(Tema.label("Pontuação:")); p.add(fPonts);
        return p;
    }

    private JPanel buildBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        p.setBackground(Tema.BG_PAINEL);

        JButton bCadastrar  = Tema.botao("＋ Cadastrar",        Tema.NEON_GREEN.darker());
        JButton bAtualizar  = Tema.botao("✎ Atualizar",         new Color(0, 100, 200));
        JButton bPontos     = Tema.botao("▲ Atualizar Pontos",  new Color(160, 100, 0));
        JButton bExcluir    = Tema.botao("✕ Excluir",           Tema.NEON_RED.darker());
        JButton bLimpar     = Tema.botao("↺ Limpar",            new Color(50, 50, 80));

        bCadastrar.addActionListener(e -> cadastrar());
        bAtualizar.addActionListener(e -> atualizar());
        bPontos.addActionListener(e -> atualizarPontuacao());
        bExcluir.addActionListener(e -> excluir());
        bLimpar.addActionListener(e -> limpar());

        p.add(bCadastrar); p.add(bAtualizar); p.add(bPontos);
        p.add(bExcluir);   p.add(bLimpar);
        return p;
    }

    private void cadastrar() {
        try {
            service.cadastrar(lerFormulario(-1));
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void atualizar() {
        int id = idSelecionado(); if (id < 0) return;
        try {
            Time t = lerFormulario(id);
            new br.esports.dao.impl.TimeDAOImpl().atualizar(t);
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void atualizarPontuacao() {
        int id = idSelecionado(); if (id < 0) return;
        String val = JOptionPane.showInputDialog(this, "Nova pontuação:");
        if (val == null || val.isBlank()) return;
        try { service.atualizarPontuacao(id, Integer.parseInt(val.trim())); carregarTabela(); }
        catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void excluir() {
        int id = idSelecionado(); if (id < 0) return;
        if (confirmar("Excluir o time selecionado?")) {
            try { service.excluir(id); carregarTabela(); limpar(); }
            catch (Exception ex) { erro(ex.getMessage()); }
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        for (Time t : service.listarTodos())
            modelo.addRow(new Object[]{t.getId(), t.getNome(), t.getTag(),
                    t.getDataFundacao() != null ? t.getDataFundacao().format(FMT) : "—",
                    t.getPontuacaoRanking()});
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow(); if (row < 0) return;
        fNome.setText((String) modelo.getValueAt(row, 1));
        fTag.setText((String)  modelo.getValueAt(row, 2));
        fData.setText((String) modelo.getValueAt(row, 3));
        fPonts.setText(String.valueOf(modelo.getValueAt(row, 4)));
    }

    private Time lerFormulario(int id) {
        String nome = fNome.getText().trim();
        String tag  = fTag.getText().trim().toUpperCase();
        if (nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (tag.isBlank())  throw new IllegalArgumentException("Tag é obrigatória.");
        Time t = new Time(); t.setId(id); t.setNome(nome); t.setTag(tag);
        String data = fData.getText().trim();
        if (!data.isBlank() && !data.equals("—")) {
            try { t.setDataFundacao(LocalDate.parse(data, FMT)); }
            catch (DateTimeParseException e) { throw new IllegalArgumentException("Data inválida (dd/MM/yyyy)."); }
        }
        String pts = fPonts.getText().trim();
        t.setPontuacaoRanking(pts.isBlank() ? 0 : Integer.parseInt(pts));
        return t;
    }

    private int idSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { erro("Selecione um time na tabela."); return -1; }
        return (int) modelo.getValueAt(row, 0);
    }

    private void limpar() {
        fNome.setText(""); fTag.setText(""); fData.setText(""); fPonts.setText("");
        tabela.clearSelection();
    }

    private boolean confirmar(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
