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

    private final JTextField fNome  = new JTextField(18);
    private final JTextField fTag   = new JTextField(8);
    private final JTextField fData  = new JTextField(10);
    private final JTextField fPonts = new JTextField(8);

    public TimePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(buildBotoes(), BorderLayout.SOUTH);
        carregarTabela();
    }

    private JPanel buildFormulario() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setBorder(BorderFactory.createTitledBorder("Dados do Time"));
        p.add(new JLabel("Nome:")); p.add(fNome);
        p.add(new JLabel("Tag:"));  p.add(fTag);
        p.add(new JLabel("Fundação (dd/MM/yyyy):")); p.add(fData);
        p.add(new JLabel("Pontuação:")); p.add(fPonts);
        return p;
    }

    private JPanel buildBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        JButton bCadastrar  = botao("Cadastrar",  new Color(0, 150, 80));
        JButton bAtualizar  = botao("Atualizar",  new Color(0, 100, 180));
        JButton bExcluir    = botao("Excluir",    new Color(180, 40, 40));
        JButton bAtualPonts = botao("Atualizar Pontuação", new Color(140, 90, 0));
        JButton bLimpar     = botao("Limpar",     Color.DARK_GRAY);

        bCadastrar.addActionListener(e -> cadastrar());
        bAtualizar.addActionListener(e -> atualizar());
        bExcluir.addActionListener(e -> excluir());
        bAtualPonts.addActionListener(e -> atualizarPontuacao());
        bLimpar.addActionListener(e -> limpar());

        p.add(bCadastrar); p.add(bAtualizar); p.add(bAtualPonts);
        p.add(bExcluir);   p.add(bLimpar);

        tabela.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) preencherFormulario();
        });
        return p;
    }

    private void cadastrar() {
        try {
            Time t = lerFormulario(-1);
            service.cadastrar(t);
            carregarTabela();
            limpar();
        } catch (Exception ex) {
            erro(ex.getMessage());
        }
    }

    private void atualizar() {
        int id = idSelecionado();
        if (id < 0) return;
        try {
            Time t = lerFormulario(id);
            service.cadastrar(t); // reutiliza validação; DAO fará UPDATE com o id
            // para atualização completa usa DAO direto via service
            br.esports.dao.impl.TimeDAOImpl dao = new br.esports.dao.impl.TimeDAOImpl();
            dao.atualizar(t);
            carregarTabela();
            limpar();
        } catch (Exception ex) {
            erro(ex.getMessage());
        }
    }

    private void atualizarPontuacao() {
        int id = idSelecionado();
        if (id < 0) return;
        String val = JOptionPane.showInputDialog(this, "Nova pontuação:", "Atualizar Pontuação",
                JOptionPane.QUESTION_MESSAGE);
        if (val == null || val.isBlank()) return;
        try {
            service.atualizarPontuacao(id, Integer.parseInt(val.trim()));
            carregarTabela();
        } catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void excluir() {
        int id = idSelecionado();
        if (id < 0) return;
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir o time selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try { service.excluir(id); carregarTabela(); limpar(); }
            catch (Exception ex) { erro(ex.getMessage()); }
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        List<Time> lista = service.listarTodos();
        for (Time t : lista)
            modelo.addRow(new Object[]{
                    t.getId(), t.getNome(), t.getTag(),
                    t.getDataFundacao() != null ? t.getDataFundacao().format(FMT) : "",
                    t.getPontuacaoRanking()
            });
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;
        fNome.setText((String) modelo.getValueAt(row, 1));
        fTag.setText((String)  modelo.getValueAt(row, 2));
        fData.setText((String) modelo.getValueAt(row, 3));
        fPonts.setText(String.valueOf(modelo.getValueAt(row, 4)));
    }

    private Time lerFormulario(int id) {
        String nome = fNome.getText().trim();
        String tag  = fTag.getText().trim().toUpperCase();
        String data = fData.getText().trim();
        String pts  = fPonts.getText().trim();
        if (nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (tag.isBlank())  throw new IllegalArgumentException("Tag é obrigatória.");
        Time t = new Time();
        t.setId(id);
        t.setNome(nome);
        t.setTag(tag);
        if (!data.isBlank()) {
            try { t.setDataFundacao(LocalDate.parse(data, FMT)); }
            catch (DateTimeParseException e) { throw new IllegalArgumentException("Data inválida (use dd/MM/yyyy)."); }
        }
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

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }
}
