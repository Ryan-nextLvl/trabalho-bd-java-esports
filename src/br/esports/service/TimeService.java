package br.esports.service;

import br.esports.dao.TimeDAO;
import br.esports.dao.impl.TimeDAOImpl;
import br.esports.model.Time;

import java.util.List;

public class TimeService {

    private final TimeDAO dao = new TimeDAOImpl();

    public void cadastrar(Time t) {
        if (t.getNome() == null || t.getNome().isBlank())
            throw new IllegalArgumentException("Nome do time não pode ser vazio.");
        if (t.getTag() == null || t.getTag().isBlank())
            throw new IllegalArgumentException("Tag do time não pode ser vazia.");
        dao.inserir(t);
    }

    public void atualizarPontuacao(int id, int novaPontuacao) {
        Time t = dao.buscarPorId(id);
        if (t == null) throw new IllegalArgumentException("Time com ID " + id + " não encontrado.");
        t.setPontuacaoRanking(novaPontuacao);
        dao.atualizar(t);
    }

    public void excluir(int id) {
        if (dao.possuiJogadoresOuPartidas(id))
            throw new IllegalStateException("Time possui jogadores ou partidas vinculadas. Remova-os antes.");
        dao.excluir(id);
    }

    public Time buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Time> listarTodos() {
        return dao.listarTodos();
    }

    public void listarComPartidasEmCasa() {
        dao.listarComPartidasEmCasa();
    }
}
