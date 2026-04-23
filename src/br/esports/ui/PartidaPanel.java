package br.esports.ui;

import br.esports.model.Partida;
import br.esports.model.Time;
import br.esports.service.PartidaService;
import br.esports.service.TimeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PartidaPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final PartidaService service     = new PartidaService();
    private final TimeService    timeService = new TimeService();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Data/Hora", "Casa", "Visitante", "Duração (min)", "Resultado"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    private final JComboBox<String> cbCasa      = new JComboBox<>();
    private final JComboBox<String> cbVisitante = new JComboBox<>();
    private final JTextField fData      = new JTextField("dd/MM/yyyy HH:mm", 16);
    private final JTextField fDuracao   = new JTextField(6);
    private final JComboBox<String> cbResultado = new JComboBox<>(
            new String[]{"CASA", "VISITANTE", "EMPATE"});
    private List<Time> times;

    public PartidaPanel() {
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
        p.setBorder(BorderFactory.createTitledBorder("Dados da Partida"));
        p.add(new JLabel("Time Casa:"));      p.add(cbCasa);
        p.add(new JLabel("Time Visitante:")); p.add(cbVisitante);
        p.add(new JLabel("Data/Hora:"));      p.add(fData);
        p.add(new JLabel("Duração (min):"));  p.add(fDuracao);
        p.add(new JLabel("Resultado:"));      p.add(cbResultado);
        return p;
    }

    private JPanel buildBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        JButton bRegistrar  = botao("Registrar",        new Color(0, 150, 80));
        JButton bAltResult  = botao("Alterar Resultado", new Color(0, 100, 180));
        JButton bExcluir    = botao("Excluir",           new Color(180, 40, 40));
        JButton bLimpar     = botao("Limpar",             Color.DARK_GRAY);

        bRegistrar.addActionListener(e -> registrar());
        bAltResult.addActionListener(e -> alterarResultado());
        bExcluir.addActionListener(e -> excluir());
        bLimpar.addActionListener(e -> limpar());

        p.add(bRegistrar); p.add(bAltResult); p.add(bExcluir); p.add(bLimpar);

        tabela.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) preencherFormulario();
        });
        return p;
    }

    private void registrar() {
        try {
            Partida pt = lerFormulario(-1);
            service.registrar(pt);
            carregarTabela();
            limpar();
        } catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void alterarResultado() {
        int id = idSelecionado();
        if (id < 0) return;
        String novo = (String) cbResultado.getSelectedItem();
        try {
            service.atualizarResultado(id, novo);
            carregarTabela();
        } catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void excluir() {
        int id = idSelecionado();
        if (id < 0) return;
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir partida selecionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            service.excluir(id);
            carregarTabela();
            limpar();
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        for (Partida p : service.listarTodas())
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getDataPartida() != null ? p.getDataPartida().format(FMT) : "",
                    p.getNomeTimeCasa(),
                    p.getNomeTimeVisitante(),
                    p.getDuracaoMinutos(),
                    p.getResultado()
            });
    }

    private void carregarTimes() {
        times = timeService.listarTodos();
        cbCasa.removeAllItems();
        cbVisitante.removeAllItems();
        for (Time t : times) {
            String item = "[" + t.getId() + "] " + t.getNome();
            cbCasa.addItem(item);
            cbVisitante.addItem(item);
        }
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;
        String casa  = (String) modelo.getValueAt(row, 2);
        String visit = (String) modelo.getValueAt(row, 3);
        selecionarCombo(cbCasa, casa);
        selecionarCombo(cbVisitante, visit);
        fData.setText((String) modelo.getValueAt(row, 1));
        fDuracao.setText(String.valueOf(modelo.getValueAt(row, 4)));
        cbResultado.setSelectedItem(modelo.getValueAt(row, 5));
    }

    private void selecionarCombo(JComboBox<String> cb, String nome) {
        for (int i = 0; i < cb.getItemCount(); i++)
            if (cb.getItemAt(i).contains(nome)) { cb.setSelectedIndex(i); return; }
    }

    private Partida lerFormulario(int id) {
        int idxCasa  = cbCasa.getSelectedIndex();
        int idxVisit = cbVisitante.getSelectedIndex();
        if (idxCasa < 0 || idxVisit < 0) throw new IllegalArgumentException("Selecione os dois times.");
        if (idxCasa == idxVisit) throw new IllegalArgumentException("Times casa e visitante devem ser diferentes.");
        String dataStr = fData.getText().trim();
        LocalDateTime dt;
        try { dt = LocalDateTime.parse(dataStr, FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Data inválida (dd/MM/yyyy HH:mm)."); }
        String durStr = fDuracao.getText().trim();
        if (durStr.isBlank()) throw new IllegalArgumentException("Informe a duração.");
        Partida p = new Partida();
        p.setId(id);
        p.setFkTimeCasa(times.get(idxCasa).getId());
        p.setFkTimeVisitante(times.get(idxVisit).getId());
        p.setDataPartida(dt);
        p.setDuracaoMinutos(Integer.parseInt(durStr));
        p.setResultado((String) cbResultado.getSelectedItem());
        return p;
    }

    private int idSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { erro("Selecione uma partida na tabela."); return -1; }
        return (int) modelo.getValueAt(row, 0);
    }

    private void limpar() {
        fData.setText("dd/MM/yyyy HH:mm"); fDuracao.setText("");
        cbCasa.setSelectedIndex(0); cbVisitante.setSelectedIndex(0);
        cbResultado.setSelectedIndex(0); tabela.clearSelection();
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
