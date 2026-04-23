package br.esports.dao;

import br.esports.model.Partida;
import java.util.List;

public interface PartidaDAO {
    void inserir(Partida partida);
    void atualizar(Partida partida);
    void excluir(int id);
    Partida buscarPorId(int id);
    List<Partida> listarTodas();
    List<Partida> listarComDetalhe();
    boolean possuiPartidasPorTime(int idTime);
}
