package br.esports.dao.impl;

import br.esports.dao.JogadorDAO;
import br.esports.db.ConexaoBD;
import br.esports.model.Jogador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAOImpl implements JogadorDAO {

    @Override
    public void inserir(Jogador j) {
        String sql = "INSERT INTO jogador (nickname, elo, fk_time) VALUES (?, ?, ?)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, j.getNickname());
            ps.setInt(2, j.getElo());
            if (j.getFkTime() != null) ps.setInt(3, j.getFkTime());
            else ps.setNull(3, Types.INTEGER);
            ps.executeUpdate();
            System.out.println("Jogador inserido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir jogador: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Jogador j) {
        String sql = "UPDATE jogador SET nickname=?, elo=?, fk_time=? WHERE id_jogador=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, j.getNickname());
            ps.setInt(2, j.getElo());
            if (j.getFkTime() != null) ps.setInt(3, j.getFkTime());
            else ps.setNull(3, Types.INTEGER);
            ps.setInt(4, j.getId());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Jogador atualizado com sucesso!" : "Jogador não encontrado.");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar jogador: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM jogador WHERE id_jogador=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Jogador excluído com sucesso!" : "Jogador não encontrado.");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir jogador: " + e.getMessage());
        }
    }

    @Override
    public Jogador buscarPorId(int id) {
        String sql = "SELECT j.*, t.nome AS nome_time FROM jogador j " +
                     "LEFT JOIN time t ON j.fk_time = t.id_time " +
                     "WHERE j.id_jogador=?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar jogador: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Jogador> listarTodos() {
        List<Jogador> lista = new ArrayList<>();
        String sql = "SELECT j.*, t.nome AS nome_time FROM jogador j " +
                     "LEFT JOIN time t ON j.fk_time = t.id_time " +
                     "ORDER BY j.id_jogador";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Erro ao listar jogadores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Jogador> listarPorTime(String nomeTime) {
        List<Jogador> lista = new ArrayList<>();
        String sql = "SELECT j.*, t.nome AS nome_time FROM jogador j " +
                     "INNER JOIN time t ON j.fk_time = t.id_time " +
                     "WHERE LOWER(t.nome) LIKE LOWER(?) " +
                     "ORDER BY j.elo DESC";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nomeTime + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar jogadores por time: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Jogador> listarOrdenadosPorElo() {
        List<Jogador> lista = new ArrayList<>();
        String sql = "SELECT j.*, t.nome AS nome_time FROM jogador j " +
                     "LEFT JOIN time t ON j.fk_time = t.id_time " +
                     "ORDER BY j.elo DESC";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Erro ao listar jogadores por elo: " + e.getMessage());
        }
        return lista;
    }

    private Jogador mapear(ResultSet rs) throws SQLException {
        Jogador j = new Jogador();
        j.setId(rs.getInt("id_jogador"));
        j.setNickname(rs.getString("nickname"));
        j.setElo(rs.getInt("elo"));
        int fk = rs.getInt("fk_time");
        j.setFkTime(rs.wasNull() ? null : fk);
        j.setNomeTime(rs.getString("nome_time"));
        return j;
    }
}
