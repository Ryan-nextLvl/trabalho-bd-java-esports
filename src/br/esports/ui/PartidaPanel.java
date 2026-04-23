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

    private final JComboBox<String> cbCasa      = Tema.combo();
    private final JComboBox<String> cbVisitante = Tema.combo();
    private final JTextField fData    = Tema.campo(16);
    private final JTextField fDuracao = Tema.campo(6);
    private final JComboBox<String> cbResultado = Tema.combo();
    private List<Time> times;

    public PartidaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.BG_PAINEL);
        setBorder(Tema.bordaVazia(12, 14));
        Tema.estilizarTabela(tabela);

        cbResultado.addItem("CASA");
        cbResultado.addItem("VISITANTE");
        cbResultado.addItem("EMPATE");
        fData.setToolTipText("Formato: dd/MM/yyyy HH:mm");

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
        p.setBorder(Tema.bordaCard("Dados da Partida"));
        p.add(Tema.label("Time Casa:"));      p.add(cbCasa);
        p.add(Tema.label("Time Visitante:")); p.add(cbVisitante);
        p.add(Tema.label("Data/Hora:"));      p.add(fData);
        p.add(Tema.label("Duração (min):"));  p.add(fDuracao);
        p.add(Tema.label("Resultado:"));      p.add(cbResultado);
        return p;
    }

    private JPanel buildBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        p.setBackground(Tema.BG_PAINEL);

        JButton bRegistrar = Tema.botao("＋ Registrar",         Tema.NEON_ORANGE.darker());
        JButton bAlterar   = Tema.botao("✎ Alterar Resultado",  new Color(0, 100, 200));
        JButton bExcluir   = Tema.botao("✕ Excluir",            Tema.NEON_RED.darker());
        JButton bLimpar    = Tema.botao("↺ Limpar",             new Color(50, 50, 80));

        bRegistrar.addActionListener(e -> registrar());
        bAlterar.addActionListener(e -> alterarResultado());
        bExcluir.addActionListener(e -> excluir());
        bLimpar.addActionListener(e -> limpar());

        p.add(bRegistrar); p.add(bAlterar); p.add(bExcluir); p.add(bLimpar);
        return p;
    }

    private void registrar() {
        try { service.registrar(lerFormulario(-1)); carregarTabela(); limpar(); }
        catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void alterarResultado() {
        int id = idSelecionado(); if (id < 0) return;
        try { service.atualizarResultado(id, (String) cbResultado.getSelectedItem()); carregarTabela(); }
        catch (Exception ex) { erro(ex.getMessage()); }
    }

    private void excluir() {
        int id = idSelecionado(); if (id < 0) return;
        if (JOptionPane.showConfirmDialog(this, "Excluir partida?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.excluir(id); carregarTabela(); limpar();
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        for (Partida p : service.listarTodas())
            modelo.addRow(new Object[]{p.getId(),
                    p.getDataPartida() != null ? p.getDataPartida().format(FMT) : "—",
                    p.getNomeTimeCasa(), p.getNomeTimeVisitante(),
                    p.getDuracaoMinutos(), p.getResultado()});
    }

    private void carregarTimes() {
        times = timeService.listarTodos();
        cbCasa.removeAllItems(); cbVisitante.removeAllItems();
        for (Time t : times) {
            String item = "[" + t.getId() + "] " + t.getNome();
            cbCasa.addItem(item); cbVisitante.addItem(item);
        }
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow(); if (row < 0) return;
        selecionarCombo(cbCasa,      (String) modelo.getValueAt(row, 2));
        selecionarCombo(cbVisitante, (String) modelo.getValueAt(row, 3));
        fData.setText((String) modelo.getValueAt(row, 1));
        fDuracao.setText(String.valueOf(modelo.getValueAt(row, 4)));
        cbResultado.setSelectedItem(modelo.getValueAt(row, 5));
    }

    private void selecionarCombo(JComboBox<String> cb, String nome) {
        for (int i = 0; i < cb.getItemCount(); i++)
            if (cb.getItemAt(i).contains(nome)) { cb.setSelectedIndex(i); return; }
    }

    private Partida lerFormulario(int id) {
        int ic = cbCasa.getSelectedIndex(), iv = cbVisitante.getSelectedIndex();
        if (ic < 0 || iv < 0) throw new IllegalArgumentException("Selecione os dois times.");
        if (ic == iv) throw new IllegalArgumentException("Times casa e visitante devem ser diferentes.");
        LocalDateTime dt;
        try { dt = LocalDateTime.parse(fData.getText().trim(), FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Data inválida (dd/MM/yyyy HH:mm)."); }
        if (fDuracao.getText().isBlank()) throw new IllegalArgumentException("Informe a duração.");
        Partida p = new Partida(); p.setId(id);
        p.setFkTimeCasa(times.get(ic).getId());
        p.setFkTimeVisitante(times.get(iv).getId());
        p.setDataPartida(dt);
        p.setDuracaoMinutos(Integer.parseInt(fDuracao.getText().trim()));
        p.setResultado((String) cbResultado.getSelectedItem());
        return p;
    }

    private int idSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { erro("Selecione uma partida."); return -1; }
        return (int) modelo.getValueAt(row, 0);
    }

    private void limpar() {
        fData.setText(""); fDuracao.setText("");
        if (cbCasa.getItemCount() > 0) cbCasa.setSelectedIndex(0);
        if (cbVisitante.getItemCount() > 0) cbVisitante.setSelectedIndex(0);
        cbResultado.setSelectedIndex(0); tabela.clearSelection();
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
