package br.esports.model;

public class Jogador {

    private int id;
    private String nickname;
    private int elo;
    private Integer fkTime;
    private String nomeTime;

    public Jogador() {}

    public Jogador(int id, String nickname, int elo, Integer fkTime) {
        this.id = id;
        this.nickname = nickname;
        this.elo = elo;
        this.fkTime = fkTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getElo() { return elo; }
    public void setElo(int elo) { this.elo = elo; }

    public Integer getFkTime() { return fkTime; }
    public void setFkTime(Integer fkTime) { this.fkTime = fkTime; }

    public String getNomeTime() { return nomeTime; }
    public void setNomeTime(String nomeTime) { this.nomeTime = nomeTime; }

    @Override
    public String toString() {
        String time = nomeTime != null ? nomeTime : (fkTime != null ? "ID " + fkTime : "Sem time");
        return String.format("[%d] %-20s | ELO: %d | Time: %s", id, nickname, elo, time);
    }
}
