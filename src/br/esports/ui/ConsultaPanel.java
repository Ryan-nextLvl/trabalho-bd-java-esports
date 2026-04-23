package br.esports.ui;

import br.esports.model.Jogador;
import br.esports.model.Partida;
import br.esports.model.Time;
import br.esports.service.JogadorService;
import br.esports.service.PartidaService;
import br.esports.service.TimeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsultaPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final TimeService    timeService    = new TimeService();
    private final JogadorService jogadorService = new JogadorService();
    private final PartidaService partidaService = new PartidaService();

    private final JTable tabela = new JTable();
    private final JTextField fFiltroTime = new JTextField(16);

    public ConsultaPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildBotoes(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private JPanel buildBotoes() {
        JPanel externo = new JPanel(new BorderLayout());

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        linha1.setBorder(BorderFactory.createTitledBorder("Consultas disponíveis"));

        JButton b1 = botao("Times por Ranking",     new Color(80, 60, 140));
        JButton b2 = botao("Top ELO (jogadores)",   new Color(80, 60, 140));
        JButton b3 = botao("Partidas recentes",     new Color(80, 60, 140));
        JButton b4 = botao("INNER JOIN (partidas)", new Color(30, 100, 160));
        JButton b5 = botao("LEFT JOIN (mandantes)", new Color(30, 100, 160));

        linha1.add(b1); linha1.add(b2); linha1.add(b3);
        linha1.add(b4); linha1.add(b5);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        linha2.add(new JLabel("Filtrar jogadores por time:"));
        linha2.add(fFiltroTime);
        JButton bFiltrar = botao("Buscar Jogadores", new Color(100, 130, 0));
        linha2.add(bFiltrar);

        b1.addActionListener(e -> consultaTimesPorRanking());
        b2.addActionListener(e -> consultaJogadoresPorElo());
        b3.addActionListener(e -> consultaPartidasRecentes());
        b4.addActionListener(e -> consultaInnerJoin());
        b5.addActionListener(e -> consultaLeftJoin());
        bFiltrar.addActionListener(e -> consultaJogadoresPorTime());

        externo.add(linha1, BorderLayout.NORTH);
        externo.add(linha2, BorderLayout.SOUTH);
        return externo;
    }

    private void consultaTimesPorRanking() {
        List<Time> lista = timeService.listarTodos();
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID", "Nome", "Tag", "Fundação", "Pontuação"}, 0);
        for (Time t : lista)
            m.addRow(new Object[]{t.getId(), t.getNome(), t.getTag(),
                    t.getDataFundacao() != null ? t.getDataFundacao().toString() : "—",
                    t.getPontuacaoRanking()});
        tabela.setModel(m);
    }

    private void consultaJogadoresPorElo() {
        List<Jogador> lista = jogadorService.listarOrdenadosPorElo();
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"#", "Nickname", "ELO", "Time"}, 0);
        int pos = 1;
        for (Jogador j : lista)
            m.addRow(new Object[]{pos++, j.getNickname(), j.getElo(),
                    j.getNomeTime() != null ? j.getNomeTime() : "—"});
        tabela.setModel(m);
    }

    private void consultaPartidasRecentes() {
        List<Partida> lista = partidaService.listarTodas();
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID", "Data/Hora", "Casa", "Visitante", "Duração", "Resultado"}, 0);
        for (Partida p : lista)
            m.addRow(new Object[]{p.getId(),
                    p.getDataPartida() != null ? p.getDataPartida().format(FMT) : "—",
                    p.getNomeTimeCasa(), p.getNomeTimeVisitante(),
                    p.getDuracaoMinutos() + " min", p.getResultado()});
        tabela.setModel(m);
    }

    private void consultaInnerJoin() {
        List<Partida> lista = partidaService.listarComDetalhe();
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID", "Data/Hora", "Time Casa", "Time Visitante", "Duração (min)", "Resultado"}, 0);
        for (Partida p : lista)
            m.addRow(new Object[]{p.getId(),
                    p.getDataPartida() != null ? p.getDataPartida().format(FMT) : "—",
                    p.getNomeTimeCasa(), p.getNomeTimeVisitante(),
                    p.getDuracaoMinutos(), p.getResultado()});
        tabela.setModel(m);
        JOptionPane.showMessageDialog(this,
                "Consulta executada com INNER JOIN entre partida e time (casa e visitante).",
                "INNER JOIN", JOptionPane.INFORMATION_MESSAGE);
    }

    private void consultaLeftJoin() {
        // LEFT JOIN executado no DAO — capturamos o print via modelo dedicado
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID", "Nome", "Tag", "Pontuação", "Partidas em Casa"}, 0);

        // Executa a consulta LEFT JOIN diretamente
        try (java.sql.Connection con = br.esports.db.ConexaoBD.getConexao();
             java.sql.PreparedStatement ps = con.prepareStatement(
                "SELECT t.id_time, t.nome, t.tag, t.pontuacao_ranking, " +
                "COUNT(p.id_partida) AS partidas_em_casa " +
                "FROM time t " +
                "LEFT JOIN partida p ON p.fk_time_casa = t.id_time " +
                "GROUP BY t.id_time, t.nome, t.tag, t.pontuacao_ranking " +
                "ORDER BY partidas_em_casa DESC, t.pontuacao_ranking DESC");
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                m.addRow(new Object[]{
                        rs.getInt("id_time"),
                        rs.getString("nome"),
                        rs.getString("tag"),
                        rs.getInt("pontuacao_ranking"),
                        rs.getInt("partidas_em_casa")
                });
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro SQL", JOptionPane.ERROR_MESSAGE);
        }
        tabela.setModel(m);
        JOptionPane.showMessageDialog(this,
                "Consulta executada com LEFT JOIN — inclui times sem partidas em casa (quantidade zero).",
                "LEFT JOIN", JOptionPane.INFORMATION_MESSAGE);
    }

    private void consultaJogadoresPorTime() {
        String filtro = fFiltroTime.getText().trim();
        if (filtro.isBlank()) { JOptionPane.showMessageDialog(this, "Digite o nome do time."); return; }
        List<Jogador> lista = jogadorService.listarPorTime(filtro);
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID", "Nickname", "ELO", "Time"}, 0);
        for (Jogador j : lista)
            m.addRow(new Object[]{j.getId(), j.getNickname(), j.getElo(),
                    j.getNomeTime() != null ? j.getNomeTime() : "—"});
        if (lista.isEmpty())
            JOptionPane.showMessageDialog(this, "Nenhum jogador encontrado para \"" + filtro + "\".");
        tabela.setModel(m);
    }

    private JButton botao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }
}
