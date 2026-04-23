package br.esports.dao;

import br.esports.model.Jogador;
import java.util.List;

public interface JogadorDAO {
    void inserir(Jogador jogador);
    void atualizar(Jogador jogador);
    void excluir(int id);
    Jogador buscarPorId(int id);
    List<Jogador> listarTodos();
    List<Jogador> listarPorTime(String nomeTime);
    List<Jogador> listarOrdenadosPorElo();
}
