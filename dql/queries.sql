-- DQL: Consultas principais utilizadas no sistema

-- 1. Todos os times ordenados por pontuação decrescente
SELECT id_time, nome, tag, data_fundacao, pontuacao_ranking
FROM time
ORDER BY pontuacao_ranking DESC;

-- 2. Jogadores de um time específico
SELECT j.id_jogador, j.nickname, j.elo, t.nome AS time
FROM jogador j
JOIN time t ON j.fk_time = t.id_time
WHERE LOWER(t.nome) LIKE LOWER('%furia%');

-- 3. Todas as partidas ordenadas por data (mais recente primeiro)
SELECT id_partida, data_partida, duracao_minutos,
       fk_time_casa, fk_time_visitante, resultado
FROM partida
ORDER BY data_partida DESC;

-- 4. Jogadores ordenados por elo (maior para menor)
SELECT id_jogador, nickname, elo, fk_time
FROM jogador
ORDER BY elo DESC;

-- 5. INNER JOIN: detalhes de cada partida com nomes dos times
SELECT p.id_partida,
       p.data_partida,
       p.duracao_minutos,
       tc.nome  AS time_casa,
       tv.nome  AS time_visitante,
       p.resultado
FROM partida p
INNER JOIN time tc ON p.fk_time_casa      = tc.id_time
INNER JOIN time tv ON p.fk_time_visitante = tv.id_time
ORDER BY p.data_partida DESC;

-- 6. LEFT JOIN: todos os times e quantidade de partidas como mandante
SELECT t.id_time,
       t.nome,
       t.tag,
       t.pontuacao_ranking,
       COUNT(p.id_partida) AS partidas_em_casa
FROM time t
LEFT JOIN partida p ON p.fk_time_casa = t.id_time
GROUP BY t.id_time, t.nome, t.tag, t.pontuacao_ranking
ORDER BY partidas_em_casa DESC, t.pontuacao_ranking DESC;
