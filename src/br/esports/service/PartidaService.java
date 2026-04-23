package br.esports.service;

import br.esports.dao.PartidaDAO;
import br.esports.dao.impl.PartidaDAOImpl;
import br.esports.model.Partida;

import java.util.List;

public class PartidaService {

    private final PartidaDAO dao = new PartidaDAOImpl();

    public void registrar(Partida p) {
        if (p.getFkTimeCasa() == p.getFkTimeVisitante())
            throw new IllegalArgumentException("Time casa e visitante não podem ser o mesmo.");
        if (p.getDuracaoMinutos() <= 0)
            throw new IllegalArgumentException("Duração deve ser maior que zero.");
        if (!List.of("CASA","VISITANTE","EMPATE").contains(p.getResultado()))
            throw new IllegalArgumentException("Resultado deve ser CASA, VISITANTE ou EMPATE.");
        dao.inserir(p);
    }

    public void atualizarResultado(int id, String novoResultado) {
        if (!List.of("CASA","VISITANTE","EMPATE").contains(novoResultado))
            throw new IllegalArgumentException("Resultado deve ser CASA, VISITANTE ou EMPATE.");
        Partida p = dao.buscarPorId(id);
        if (p == null) throw new IllegalArgumentException("Partida com ID " + id + " não encontrada.");
        p.setResultado(novoResultado);
        dao.atualizar(p);
    }

    public void excluir(int id) {
        dao.excluir(id);
    }

    public Partida buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Partida> listarTodas() {
        return dao.listarTodas();
    }

    public List<Partida> listarComDetalhe() {
        return dao.listarComDetalhe();
    }
}
