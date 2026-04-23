package br.esports.dao.impl;

import br.esports.dao.TimeDAO;
import br.esports.db.ConexaoBD;
import br.esports.model.Time;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimeDAOImpl implements TimeDAO {

    @Override
    public void inserir(Time t) {
        String sql = "INSERT INTO time (nome, tag, data_fundacao, pontuacao_ranking) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getTag());
            ps.setObject(3, t.getDataFundacao());
            ps.setInt(4, t.getPontuacaoRanking());
            ps.executeUpdate();
            System.out.println("Time inserido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir time: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Time t) {
        String sql = "UPDATE time SET nome=?, tag=?, data_fundacao=?, pontuacao_ranking=? WHERE id_time=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getTag());
            ps.setObject(3, t.getDataFundacao());
            ps.setInt(4, t.getPontuacaoRanking());
            ps.setInt(5, t.getId());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Time atualizado com sucesso!" : "Time não encontrado.");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar time: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM time WHERE id_time=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Time excluído com sucesso!" : "Time não encontrado.");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir time: " + e.getMessage());
        }
    }

    @Override
    public Time buscarPorId(int id) {
        String sql = "SELECT * FROM time WHERE id_time=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar time: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Time> listarTodos() {
        List<Time> lista = new ArrayList<>();
        String sql = "SELECT * FROM time ORDER BY pontuacao_ranking DESC";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Erro ao listar times: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Time> listarComPartidasEmCasa() {
        List<Time> lista = new ArrayList<>();
        String sql =
            "SELECT t.id_time, t.nome, t.tag, t.data_fundacao, t.pontuacao_ranking, " +
            "       COUNT(p.id_partida) AS partidas_em_casa " +
            "FROM time t " +
            "LEFT JOIN partida p ON p.fk_time_casa = t.id_time " +
            "GROUP BY t.id_time, t.nome, t.tag, t.data_fundacao, t.pontuacao_ranking " +
            "ORDER BY partidas_em_casa DESC, t.pontuacao_ranking DESC";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Time t = mapear(rs);
                int qtd = rs.getInt("partidas_em_casa");
                System.out.printf("  %-25s | %-6s | Pts: %-5d | Partidas em casa: %d%n",
                        t.getNome(), t.getTag(), t.getPontuacaoRanking(), qtd);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar times: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean possuiJogadoresOuPartidas(int id) {
        String sql =
            "SELECT (EXISTS(SELECT 1 FROM jogador WHERE fk_time=?)) AS tem_jogador, " +
            "       (EXISTS(SELECT 1 FROM partida WHERE fk_time_casa=? OR fk_time_visitante=?)) AS tem_partida";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.setInt(3, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBoolean("tem_jogador") || rs.getBoolean("tem_partida");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar dependências: " + e.getMessage());
        }
        return true;
    }

    private Time mapear(ResultSet rs) throws SQLException {
        Time t = new Time();
        t.setId(rs.getInt("id_time"));
        t.setNome(rs.getString("nome"));
        t.setTag(rs.getString("tag"));
        Date d = rs.getDate("data_fundacao");
        if (d != null) t.setDataFundacao(d.toLocalDate());
        t.setPontuacaoRanking(rs.getInt("pontuacao_ranking"));
        return t;
    }
}
