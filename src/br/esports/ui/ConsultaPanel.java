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

    private final JTable     tabela   = new JTable();
    private final JTextField fFiltro  = Tema.campo(16);
    private final JLabel     lInfo    = new JLabel(" ");

    public ConsultaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.BG_PAINEL);
        setBorder(Tema.bordaVazia(12, 14));
        Tema.estilizarTabela(tabela);
        add(buildBotoes(),           BorderLayout.NORTH);
        add(Tema.scrollPane(tabela), BorderLayout.CENTER);
        add(buildInfo(),             BorderLayout.SOUTH);
    }

    private JPanel buildBotoes() {
        JPanel externo = new JPanel(new BorderLayout(0, 6));
        externo.setBackground(Tema.BG_PAINEL);

        // Linha 1 — consultas gerais
        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        linha1.setBackground(Tema.BG_CARD);
        linha1.setBorder(Tema.bordaCard("Consultas"));

        JButton b1 = Tema.botao("🏆 Times / Ranking",      new Color(80, 40, 140));
        JButton b2 = Tema.botao("🎮 Top ELO",               new Color(0, 110, 130));
        JButton b3 = Tema.botao("⚔ Partidas Recentes",      new Color(130, 60, 0));
        JButton b4 = Tema.botao("🔗 INNER JOIN (Partidas)", new Color(0, 80, 160));
        JButton b5 = Tema.botao("◀ LEFT JOIN (Mandantes)",  new Color(60, 0, 130));

        b1.addActionListener(e -> consultaTimesPorRanking());
        b2.addActionListener(e -> consultaJogadoresPorElo());
        b3.addActionListener(e -> consultaPartidasRecentes());
        b4.addActionListener(e -> consultaInnerJoin());
        b5.addActionListener(e -> consultaLeftJoin());

        linha1.add(b1); linha1.add(b2); linha1.add(b3);
        linha1.add(b4); linha1.add(b5);

        // Linha 2 — filtro por time
        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        linha2.setBackground(Tema.BG_CARD);
        linha2.setBorder(Tema.bordaCard("Filtro por Time"));

        JButton bFiltrar = Tema.botao("🔍 Buscar Jogadores", new Color(0, 120, 60));
        bFiltrar.addActionListener(e -> consultaJogadoresPorTime());

        linha2.add(Tema.label("Nome do time (parcial):"));
        linha2.add(fFiltro);
        linha2.add(bFiltrar);

        externo.add(linha1, BorderLayout.NORTH);
        externo.add(linha2, BorderLayout.SOUTH);
        return externo;
    }

    private JPanel buildInfo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Tema.BG_PAINEL);
        lInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lInfo.setForeground(Tema.NEON_CYAN);
        lInfo.setBorder(Tema.bordaVazia(4, 0));
        p.add(lInfo, BorderLayout.WEST);
        return p;
    }

    private void consultaTimesPorRanking() {
        List<Time> lista = timeService.listarTodos();
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID", "Nome", "Tag", "Fundação", "Pontuação"}, 0);
        for (Time t : lista)
            m.addRow(new Object[]{t.getId(), t.getNome(), t.getTag(),
                    t.getDataFundacao() != null ? t.getDataFundacao().toString() : "—",
                    t.getPontuacaoRanking()});
        aplicar(m, "Times ordenados por pontuação (decrescente)  —  " + lista.size() + " registro(s)");
    }

    private void consultaJogadoresPorElo() {
        List<Jogador> lista = jogadorService.listarOrdenadosPorElo();
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"#", "Nickname", "ELO", "Time"}, 0);
        int pos = 1;
        for (Jogador j : lista)
            m.addRow(new Object[]{pos++, j.getNickname(), j.getElo(),
                    j.getNomeTime() != null ? j.getNomeTime() : "—"});
        aplicar(m, "Ranking de jogadores por ELO  —  " + lista.size() + " registro(s)");
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
        aplicar(m, "Partidas ordenadas por data (mais recentes primeiro)  —  " + lista.size() + " registro(s)");
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
        aplicar(m, "INNER JOIN: partida ⟶ time_casa + time_visitante  —  " + lista.size() + " registro(s)");
    }

    private void consultaLeftJoin() {
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID", "Nome", "Tag", "Pontuação", "Partidas em Casa"}, 0);
        try (java.sql.Connection con = br.esports.db.ConexaoBD.getConexao();
             java.sql.PreparedStatement ps = con.prepareStatement(
                "SELECT t.id_time, t.nome, t.tag, t.pontuacao_ranking, " +
                "COUNT(p.id_partida) AS partidas_em_casa " +
                "FROM time t " +
                "LEFT JOIN partida p ON p.fk_time_casa = t.id_time " +
                "GROUP BY t.id_time, t.nome, t.tag, t.pontuacao_ranking " +
                "ORDER BY partidas_em_casa DESC, t.pontuacao_ranking DESC");
             java.sql.ResultSet rs = ps.executeQuery()) {
            int total = 0;
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id_time"), rs.getString("nome"),
                        rs.getString("tag"), rs.getInt("pontuacao_ranking"),
                        rs.getInt("partidas_em_casa")});
                total++;
            }
            aplicar(m, "LEFT JOIN: time ⟵ partidas em casa (inclui zeros)  —  " + total + " registro(s)");
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultaJogadoresPorTime() {
        String filtro = fFiltro.getText().trim();
        if (filtro.isBlank()) { JOptionPane.showMessageDialog(this, "Digite o nome do time."); return; }
        List<Jogador> lista = jogadorService.listarPorTime(filtro);
        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID", "Nickname", "ELO", "Time"}, 0);
        for (Jogador j : lista)
            m.addRow(new Object[]{j.getId(), j.getNickname(), j.getElo(),
                    j.getNomeTime() != null ? j.getNomeTime() : "—"});
        aplicar(m, "Jogadores do time \"" + filtro + "\"  —  " + lista.size() + " registro(s)");
        if (lista.isEmpty())
            JOptionPane.showMessageDialog(this, "Nenhum jogador encontrado para \"" + filtro + "\".");
    }

    private void aplicar(DefaultTableModel m, String info) {
        tabela.setModel(m);
        Tema.estilizarTabela(tabela);
        lInfo.setText("  " + info);
    }
}
