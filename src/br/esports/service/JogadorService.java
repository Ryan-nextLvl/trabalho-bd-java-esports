package br.esports.service;

import br.esports.dao.JogadorDAO;
import br.esports.dao.impl.JogadorDAOImpl;
import br.esports.model.Jogador;

import java.util.List;

public class JogadorService {

    private final JogadorDAO dao = new JogadorDAOImpl();

    public void cadastrar(Jogador j) {
        if (j.getNickname() == null || j.getNickname().isBlank())
            throw new IllegalArgumentException("Nickname não pode ser vazio.");
        if (j.getElo() < 0)
            throw new IllegalArgumentException("ELO não pode ser negativo.");
        dao.inserir(j);
    }

    public void atualizar(Jogador j) {
        if (dao.buscarPorId(j.getId()) == null)
            throw new IllegalArgumentException("Jogador com ID " + j.getId() + " não encontrado.");
        dao.atualizar(j);
    }

    public void excluir(int id) {
        dao.excluir(id);
    }

    public Jogador buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Jogador> listarTodos() {
        return dao.listarTodos();
    }

    public List<Jogador> listarPorTime(String nomeTime) {
        return dao.listarPorTime(nomeTime);
    }

    public List<Jogador> listarOrdenadosPorElo() {
        return dao.listarOrdenadosPorElo();
    }
}
