package br.esports.ui;

import br.esports.model.Jogador;
import br.esports.model.Partida;
import br.esports.model.Time;
import br.esports.service.JogadorService;
import br.esports.service.PartidaService;
import br.esports.service.TimeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static final String USUARIO_FIXO = "admin";
    private static final String SENHA_FIXA   = "123";
    private static final DateTimeFormatter FMT_DATA  = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_DTHORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner sc = new Scanner(System.in);
    private final TimeService    timeService    = new TimeService();
    private final JogadorService jogadorService = new JogadorService();
    private final PartidaService partidaService = new PartidaService();

    public void iniciar() {
        if (!fazerLogin()) {
            System.out.println("Credenciais inválidas. Encerrando o sistema.");
            return;
        }
        menuPrincipal();
    }

    // ─────────────────────────── LOGIN ───────────────────────────

    private boolean fazerLogin() {
        separador("LOGIN");
        System.out.print("  Usuário: ");
        String u = sc.nextLine().trim();
        System.out.print("  Senha  : ");
        String s = sc.nextLine().trim();
        return USUARIO_FIXO.equals(u) && SENHA_FIXA.equals(s);
    }

    // ─────────────────────────── MENU PRINCIPAL ───────────────────────────

    private void menuPrincipal() {
        int op;
        do {
            separador("MENU PRINCIPAL – E-SPORTS MANAGER");
            System.out.println("  1. Cadastrar");
            System.out.println("  2. Atualizar");
            System.out.println("  3. Excluir");
            System.out.println("  4. Consultar / Listar");
            System.out.println("  5. Sair");
            op = lerInt("Opção: ");
            switch (op) {
                case 1 -> menuCadastrar();
                case 2 -> menuAtualizar();
                case 3 -> menuExcluir();
                case 4 -> menuConsultar();
                case 5 -> System.out.println("\n  Até mais!\n");
                default -> System.out.println("  Opção inválida.");
            }
        } while (op != 5);
    }

    // ─────────────────────────── CADASTRAR ───────────────────────────

    private void menuCadastrar() {
        separador("CADASTRAR");
        System.out.println("  1. Novo time");
        System.out.println("  2. Novo jogador");
        System.out.println("  3. Nova partida");
        System.out.println("  0. Voltar");
        switch (lerInt("Opção: ")) {
            case 1 -> cadastrarTime();
            case 2 -> cadastrarJogador();
            case 3 -> cadastrarPartida();
        }
    }

    private void cadastrarTime() {
        separador("CADASTRAR TIME");
        Time t = new Time();
        System.out.print("  Nome completo : ");
        t.setNome(sc.nextLine().trim());
        System.out.print("  Tag (ex: FUR) : ");
        t.setTag(sc.nextLine().trim().toUpperCase());
        System.out.print("  Data fundação (dd/MM/yyyy) [Enter para pular]: ");
        String d = sc.nextLine().trim();
        if (!d.isBlank()) {
            try { t.setDataFundacao(LocalDate.parse(d, FMT_DATA)); }
            catch (DateTimeParseException e) { System.out.println("  Data inválida, campo ignorado."); }
        }
        t.setPontuacaoRanking(lerInt("  Pontuação inicial (padrão 0): "));
        try { timeService.cadastrar(t); }
        catch (IllegalArgumentException e) { System.out.println("  Erro: " + e.getMessage()); }
    }

    private void cadastrarJogador() {
        separador("CADASTRAR JOGADOR");
        listarTimesResumido();
        Jogador j = new Jogador();
        System.out.print("  Nickname : ");
        j.setNickname(sc.nextLine().trim());
        j.setElo(lerInt("  ELO (padrão 1000): "));
        j.setFkTime(lerInt("  ID do time (0 = sem time): "));
        if (j.getFkTime() == 0) j.setFkTime(null);
        try { jogadorService.cadastrar(j); }
        catch (IllegalArgumentException e) { System.out.println("  Erro: " + e.getMessage()); }
    }

    private void cadastrarPartida() {
        separador("REGISTRAR PARTIDA");
        listarTimesResumido();
        Partida p = new Partida();
        p.setFkTimeCasa(lerInt("  ID do time CASA      : "));
        p.setFkTimeVisitante(lerInt("  ID do time VISITANTE : "));
        System.out.print("  Data e hora (dd/MM/yyyy HH:mm): ");
        try {
            p.setDataPartida(LocalDateTime.parse(sc.nextLine().trim(), FMT_DTHORA));
        } catch (DateTimeParseException e) {
            System.out.println("  Data inválida. Usando data atual.");
            p.setDataPartida(LocalDateTime.now());
        }
        p.setDuracaoMinutos(lerInt("  Duração (minutos): "));
        System.out.print("  Resultado (CASA / VISITANTE / EMPATE): ");
        p.setResultado(sc.nextLine().trim().toUpperCase());
        try { partidaService.registrar(p); }
        catch (IllegalArgumentException e) { System.out.println("  Erro: " + e.getMessage()); }
    }

    // ─────────────────────────── ATUALIZAR ───────────────────────────

    private void menuAtualizar() {
        separador("ATUALIZAR");
        System.out.println("  1. Pontuação de um time");
        System.out.println("  2. Dados de um jogador");
        System.out.println("  3. Resultado de uma partida");
        System.out.println("  0. Voltar");
        switch (lerInt("Opção: ")) {
            case 1 -> atualizarTime();
            case 2 -> atualizarJogador();
            case 3 -> atualizarPartida();
        }
    }

    private void atualizarTime() {
        separador("ATUALIZAR PONTUAÇÃO DO TIME");
        listarTimesResumido();
        int id  = lerInt("  ID do time     : ");
        int pts = lerInt("  Nova pontuação : ");
        try { timeService.atualizarPontuacao(id, pts); }
        catch (IllegalArgumentException e) { System.out.println("  Erro: " + e.getMessage()); }
    }

    private void atualizarJogador() {
        separador("ATUALIZAR JOGADOR");
        System.out.print("  ID do jogador: ");
        int id = lerInt("");
        Jogador j = jogadorService.buscarPorId(id);
        if (j == null) { System.out.println("  Jogador não encontrado."); return; }
        System.out.println("  Atual: " + j);
        System.out.print("  Novo nickname (Enter para manter): ");
        String nick = sc.nextLine().trim();
        if (!nick.isBlank()) j.setNickname(nick);
        System.out.print("  Novo ELO (0 para manter): ");
        int elo = lerInt("");
        if (elo > 0) j.setElo(elo);
        try { jogadorService.atualizar(j); }
        catch (IllegalArgumentException e) { System.out.println("  Erro: " + e.getMessage()); }
    }

    private void atualizarPartida() {
        separador("ATUALIZAR RESULTADO DA PARTIDA");
        List<Partida> partidas = partidaService.listarComDetalhe();
        partidas.forEach(p -> System.out.println("  " + p));
        int id = lerInt("  ID da partida: ");
        System.out.print("  Novo resultado (CASA / VISITANTE / EMPATE): ");
        String res = sc.nextLine().trim().toUpperCase();
        try { partidaService.atualizarResultado(id, res); }
        catch (IllegalArgumentException e) { System.out.println("  Erro: " + e.getMessage()); }
    }

    // ─────────────────────────── EXCLUIR ───────────────────────────

    private void menuExcluir() {
        separador("EXCLUIR");
        System.out.println("  1. Time");
        System.out.println("  2. Jogador");
        System.out.println("  3. Partida");
        System.out.println("  0. Voltar");
        switch (lerInt("Opção: ")) {
            case 1 -> excluirTime();
            case 2 -> excluirJogador();
            case 3 -> excluirPartida();
        }
    }

    private void excluirTime() {
        separador("EXCLUIR TIME");
        listarTimesResumido();
        int id = lerInt("  ID do time: ");
        System.out.print("  Confirmar exclusão? (s/n): ");
        if ("s".equalsIgnoreCase(sc.nextLine().trim())) {
            try { timeService.excluir(id); }
            catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("  Erro: " + e.getMessage());
            }
        }
    }

    private void excluirJogador() {
        separador("EXCLUIR JOGADOR");
        jogadorService.listarTodos().forEach(j -> System.out.println("  " + j));
        int id = lerInt("  ID do jogador: ");
        System.out.print("  Confirmar exclusão? (s/n): ");
        if ("s".equalsIgnoreCase(sc.nextLine().trim())) jogadorService.excluir(id);
    }

    private void excluirPartida() {
        separador("EXCLUIR PARTIDA");
        partidaService.listarTodas().forEach(p -> System.out.println("  " + p));
        int id = lerInt("  ID da partida: ");
        System.out.print("  Confirmar exclusão? (s/n): ");
        if ("s".equalsIgnoreCase(sc.nextLine().trim())) partidaService.excluir(id);
    }

    // ─────────────────────────── CONSULTAR ───────────────────────────

    private void menuConsultar() {
        int op;
        do {
            separador("CONSULTAR / LISTAR");
            System.out.println("  1. Times ordenados por pontuação");
            System.out.println("  2. Jogadores de um time específico");
            System.out.println("  3. Todas as partidas (mais recentes primeiro)");
            System.out.println("  4. Jogadores ordenados por ELO");
            System.out.println("  5. Detalhe das partidas (INNER JOIN)");
            System.out.println("  6. Times e partidas em casa (LEFT JOIN)");
            System.out.println("  0. Voltar");
            op = lerInt("Opção: ");
            switch (op) {
                case 1 -> listarTimes();
                case 2 -> listarJogadoresPorTime();
                case 3 -> listarPartidas();
                case 4 -> listarJogadoresPorElo();
                case 5 -> listarPartidasDetalhe();
                case 6 -> listarTimesComPartidasCasa();
            }
        } while (op != 0);
    }

    private void listarTimes() {
        separador("TIMES – RANKING");
        List<Time> lista = timeService.listarTodos();
        if (lista.isEmpty()) { System.out.println("  Nenhum time cadastrado."); return; }
        lista.forEach(t -> System.out.println("  " + t));
    }

    private void listarJogadoresPorTime() {
        separador("JOGADORES POR TIME");
        System.out.print("  Nome do time (parcial): ");
        String nome = sc.nextLine().trim();
        List<Jogador> lista = jogadorService.listarPorTime(nome);
        if (lista.isEmpty()) { System.out.println("  Nenhum jogador encontrado."); return; }
        lista.forEach(j -> System.out.println("  " + j));
    }

    private void listarPartidas() {
        separador("PARTIDAS – MAIS RECENTES");
        List<Partida> lista = partidaService.listarTodas();
        if (lista.isEmpty()) { System.out.println("  Nenhuma partida registrada."); return; }
        lista.forEach(p -> System.out.println("  " + p));
    }

    private void listarJogadoresPorElo() {
        separador("JOGADORES – TOP ELO");
        List<Jogador> lista = jogadorService.listarOrdenadosPorElo();
        if (lista.isEmpty()) { System.out.println("  Nenhum jogador cadastrado."); return; }
        int pos = 1;
        for (Jogador j : lista)
            System.out.printf("  %2d. %s%n", pos++, j);
    }

    private void listarPartidasDetalhe() {
        separador("PARTIDAS DETALHADAS (INNER JOIN)");
        List<Partida> lista = partidaService.listarComDetalhe();
        if (lista.isEmpty()) { System.out.println("  Nenhuma partida registrada."); return; }
        System.out.printf("  %-4s %-20s %-22s %-22s %-5s %-10s%n",
                "ID", "Data/Hora", "Time Casa", "Time Visitante", "Dur.", "Resultado");
        System.out.println("  " + "-".repeat(90));
        for (Partida p : lista) {
            System.out.printf("  %-4d %-20s %-22s %-22s %-5d %-10s%n",
                    p.getId(),
                    p.getDataPartida().format(FMT_DTHORA),
                    p.getNomeTimeCasa(),
                    p.getNomeTimeVisitante(),
                    p.getDuracaoMinutos(),
                    p.getResultado());
        }
    }

    private void listarTimesComPartidasCasa() {
        separador("TIMES E PARTIDAS EM CASA (LEFT JOIN)");
        System.out.printf("  %-25s %-8s %-10s %-20s%n",
                "Time", "Tag", "Pontuação", "Partidas em Casa");
        System.out.println("  " + "-".repeat(68));
        timeService.listarComPartidasEmCasa();
    }

    // ─────────────────────────── HELPERS ───────────────────────────

    private void listarTimesResumido() {
        System.out.println();
        timeService.listarTodos().forEach(t ->
                System.out.printf("    [%d] %s (%s)%n", t.getId(), t.getNome(), t.getTag()));
        System.out.println();
    }

    private int lerInt(String prompt) {
        while (true) {
            if (!prompt.isBlank()) System.out.print("  " + prompt);
            try {
                String linha = sc.nextLine().trim();
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.print("  Valor inválido. Tente novamente: ");
            }
        }
    }

    private void separador(String titulo) {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.printf ("  ║  %-42s║%n", titulo);
        System.out.println("  ╚══════════════════════════════════════════╝");
    }
}
