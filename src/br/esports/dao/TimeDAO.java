package br.esports.dao;

import br.esports.model.Time;
import java.util.List;

public interface TimeDAO {
    void inserir(Time time);
    void atualizar(Time time);
    void excluir(int id);
    Time buscarPorId(int id);
    List<Time> listarTodos();
    List<Time> listarComPartidasEmCasa();
    boolean possuiJogadoresOuPartidas(int id);
}
