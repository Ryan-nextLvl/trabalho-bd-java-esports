package br.esports.model;

import java.time.LocalDate;

public class Time {

    private int id;
    private String nome;
    private String tag;
    private LocalDate dataFundacao;
    private int pontuacaoRanking;

    public Time() {}

    public Time(int id, String nome, String tag, LocalDate dataFundacao, int pontuacaoRanking) {
        this.id = id;
        this.nome = nome;
        this.tag = tag;
        this.dataFundacao = dataFundacao;
        this.pontuacaoRanking = pontuacaoRanking;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public LocalDate getDataFundacao() { return dataFundacao; }
    public void setDataFundacao(LocalDate dataFundacao) { this.dataFundacao = dataFundacao; }

    public int getPontuacaoRanking() { return pontuacaoRanking; }
    public void setPontuacaoRanking(int pontuacaoRanking) { this.pontuacaoRanking = pontuacaoRanking; }

    @Override
    public String toString() {
        return String.format("[%d] %-25s (%-6s) | Fundação: %-12s | Pontos: %d",
                id, nome, tag,
                dataFundacao != null ? dataFundacao.toString() : "N/A",
                pontuacaoRanking);
    }
}
