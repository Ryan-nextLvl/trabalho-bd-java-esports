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

    private final JTextField fNick = new JTextField(15);
    private final JTextField fElo  = new JTextField(8);
    private final JComboBox<String> cbTime = new JComboBox<>();
    private List<Time> times;

    public JogadorPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(buildBotoes(), BorderLayout.SOUTH);
        carregarTimes();
        carregarTabela();
    }

    private JPanel buildFormulario() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setBorder(BorderFactory.createTitledBorder("Dados do Jogador"));
        p.add(new JLabel("Nickname:")); p.add(fNick);
        p.add(new JLabel("ELO:"));      p.add(fElo);
        p.add(new JLabel("Time:"));     p.add(cbTime);
        return p;
    }

    private JPanel buildBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        JButton bCadastrar = botao("Cadastrar", new Color(0, 150, 80));
        JButton bAtualizar = botao("Atualizar", new Color(0, 100, 180));
        JButton bExcluir   = botao("Excluir",   new Color(180, 40, 40));
        JButton bLimpar    = botao("Limpar",     Color.DARK_GRAY);

        bCadastrar.addActionListener(e -> cadastrar());
        bAtualizar.addActionListener(e -> atualizar());
        bExcluir.addActionListener(e -> excluir());
        bLimpar.addActionListener(e -> limpar());

        p.add(bCadastrar); p.add(bAtualizar); p.add(bExcluir); p.add(bLimpar);

        tabela.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) preencherFormulario();
        });
        return p;
    }

    private void cadastrar() {
        try {
            Jogador j = lerFormulario(-1);
            service.cadastrar(j);
            carregarTabela();
            limpar();
        } catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void atualizar() {
        int id = idSelecionado();
        if (id < 0) return;
        try {
            Jogador j = lerFormulario(id);
            service.atualizar(j);
            carregarTabela();
            limpar();
        } catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void excluir() {
        int id = idSelecionado();
        if (id < 0) return;
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir jogador selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            service.excluir(id);
            carregarTabela();
            limpar();
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
        int row = tabela.getSelectedRow();
        if (row < 0) return;
        fNick.setText((String) modelo.getValueAt(row, 1));
        fElo.setText(String.valueOf(modelo.getValueAt(row, 2)));
        String nomeTime = (String) modelo.getValueAt(row, 3);
        for (int i = 0; i < cbTime.getItemCount(); i++) {
            if (cbTime.getItemAt(i).contains(nomeTime)) { cbTime.setSelectedIndex(i); break; }
        }
    }

    private Jogador lerFormulario(int id) {
        String nick = fNick.getText().trim();
        String eloStr = fElo.getText().trim();
        if (nick.isBlank()) throw new IllegalArgumentException("Nickname é obrigatório.");
        Jogador j = new Jogador();
        j.setId(id);
        j.setNickname(nick);
        j.setElo(eloStr.isBlank() ? 1000 : Integer.parseInt(eloStr));
        int idx = cbTime.getSelectedIndex();
        if (idx > 0) j.setFkTime(times.get(idx - 1).getId());
        return j;
    }

    private int idSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { erro("Selecione um jogador na tabela."); return -1; }
        return (int) modelo.getValueAt(row, 0);
    }

    private void limpar() {
        fNick.setText(""); fElo.setText(""); cbTime.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }
}
