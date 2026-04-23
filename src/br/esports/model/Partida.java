package br.esports.model;

import java.time.LocalDateTime;

public class Partida {

    private int id;
    private LocalDateTime dataPartida;
    private int duracaoMinutos;
    private int fkTimeCasa;
    private int fkTimeVisitante;
    private String resultado;

    private String nomeTimeCasa;
    private String nomeTimeVisitante;

    public Partida() {}

    public Partida(int id, LocalDateTime dataPartida, int duracaoMinutos,
                   int fkTimeCasa, int fkTimeVisitante, String resultado) {
        this.id = id;
        this.dataPartida = dataPartida;
        this.duracaoMinutos = duracaoMinutos;
        this.fkTimeCasa = fkTimeCasa;
        this.fkTimeVisitante = fkTimeVisitante;
        this.resultado = resultado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDataPartida() { return dataPartida; }
    public void setDataPartida(LocalDateTime dataPartida) { this.dataPartida = dataPartida; }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    public int getFkTimeCasa() { return fkTimeCasa; }
    public void setFkTimeCasa(int fkTimeCasa) { this.fkTimeCasa = fkTimeCasa; }

    public int getFkTimeVisitante() { return fkTimeVisitante; }
    public void setFkTimeVisitante(int fkTimeVisitante) { this.fkTimeVisitante = fkTimeVisitante; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getNomeTimeCasa() { return nomeTimeCasa; }
    public void setNomeTimeCasa(String nomeTimeCasa) { this.nomeTimeCasa = nomeTimeCasa; }

    public String getNomeTimeVisitante() { return nomeTimeVisitante; }
    public void setNomeTimeVisitante(String nomeTimeVisitante) { this.nomeTimeVisitante = nomeTimeVisitante; }

    @Override
    public String toString() {
        String casa = nomeTimeCasa != null ? nomeTimeCasa : "ID " + fkTimeCasa;
        String visit = nomeTimeVisitante != null ? nomeTimeVisitante : "ID " + fkTimeVisitante;
        return String.format("[%d] %s | %s vs %s | %d min | Resultado: %s",
                id,
                dataPartida != null ? dataPartida.toString().replace("T", " ") : "N/A",
                casa, visit, duracaoMinutos,
                resultado != null ? resultado : "N/A");
    }
}
