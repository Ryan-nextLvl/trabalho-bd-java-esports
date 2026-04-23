package br.esports.dao.impl;

import br.esports.dao.PartidaDAO;
import br.esports.db.ConexaoBD;
import br.esports.model.Partida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaDAOImpl implements PartidaDAO {

    @Override
    public void inserir(Partida p) {
        String sql = "INSERT INTO partida (data_partida, duracao_minutos, fk_time_casa, fk_time_visitante, resultado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(p.getDataPartida()));
            ps.setInt(2, p.getDuracaoMinutos());
            ps.setInt(3, p.getFkTimeCasa());
            ps.setInt(4, p.getFkTimeVisitante());
            ps.setString(5, p.getResultado());
            ps.executeUpdate();
            System.out.println("Partida registrada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar partida: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Partida p) {
        String sql = "UPDATE partida SET data_partida=?, duracao_minutos=?, " +
                     "fk_time_casa=?, fk_time_visitante=?, resultado=? WHERE id_partida=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(p.getDataPartida()));
            ps.setInt(2, p.getDuracaoMinutos());
            ps.setInt(3, p.getFkTimeCasa());
            ps.setInt(4, p.getFkTimeVisitante());
            ps.setString(5, p.getResultado());
            ps.setInt(6, p.getId());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Partida atualizada com sucesso!" : "Partida não encontrada.");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar partida: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM partida WHERE id_partida=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Partida excluída com sucesso!" : "Partida não encontrada.");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir partida: " + e.getMessage());
        }
    }

    @Override
    public Partida buscarPorId(int id) {
        String sql = "SELECT p.*, tc.nome AS nome_casa, tv.nome AS nome_visitante " +
                     "FROM partida p " +
                     "INNER JOIN time tc ON p.fk_time_casa      = tc.id_time " +
                     "INNER JOIN time tv ON p.fk_time_visitante = tv.id_time " +
                     "WHERE p.id_partida=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar partida: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Partida> listarTodas() {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT p.*, tc.nome AS nome_casa, tv.nome AS nome_visitante " +
                     "FROM partida p " +
                     "INNER JOIN time tc ON p.fk_time_casa      = tc.id_time " +
                     "INNER JOIN time tv ON p.fk_time_visitante = tv.id_time " +
                     "ORDER BY p.data_partida DESC";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Erro ao listar partidas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Partida> listarComDetalhe() {
        return listarTodas();
    }

    @Override
    public boolean possuiPartidasPorTime(int idTime) {
        String sql = "SELECT EXISTS(SELECT 1 FROM partida WHERE fk_time_casa=? OR fk_time_visitante=?)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTime);
            ps.setInt(2, idTime);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar partidas: " + e.getMessage());
        }
        return true;
    }

    private Partida mapear(ResultSet rs) throws SQLException {
        Partida p = new Partida();
        p.setId(rs.getInt("id_partida"));
        Timestamp ts = rs.getTimestamp("data_partida");
        if (ts != null) p.setDataPartida(ts.toLocalDateTime());
        p.setDuracaoMinutos(rs.getInt("duracao_minutos"));
        p.setFkTimeCasa(rs.getInt("fk_time_casa"));
        p.setFkTimeVisitante(rs.getInt("fk_time_visitante"));
        p.setResultado(rs.getString("resultado"));
        p.setNomeTimeCasa(rs.getString("nome_casa"));
        p.setNomeTimeVisitante(rs.getString("nome_visitante"));
        return p;
    }
}
