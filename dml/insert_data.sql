-- DML: Dados iniciais para teste

INSERT INTO time (nome, tag, data_fundacao, pontuacao_ranking) VALUES
    ('FURIA Esports',  'FUR',  '2017-01-22', 1500),
    ('LOUD',           'LOUD', '2020-04-01', 1400),
    ('paiN Gaming',    'PNG',  '2011-06-10', 1200),
    ('RED Canids',     'RED',  '2014-07-14', 1100),
    ('Team Liquid BR', 'LQBR', '2019-03-05', 900);

INSERT INTO jogador (nickname, elo, fk_time) VALUES
    ('arT',      2100, 1),
    ('yuurih',   2050, 1),
    ('saffee',   2000, 1),
    ('kscerato',  1950, 1),
    ('chelo',    1900, 1),
    ('cauanzin', 2000, 2),
    ('tuyz',     1950, 2),
    ('gbb',      1850, 2),
    ('meyern',   1800, 3),
    ('hardzao',  1750, 3);

INSERT INTO partida (data_partida, duracao_minutos, fk_time_casa, fk_time_visitante, resultado) VALUES
    ('2024-03-10 14:00:00', 45, 1, 2, 'CASA'),
    ('2024-03-12 16:30:00', 52, 2, 3, 'VISITANTE'),
    ('2024-03-15 18:00:00', 38, 1, 3, 'CASA'),
    ('2024-03-20 19:00:00', 61, 3, 4, 'EMPATE'),
    ('2024-03-25 20:00:00', 44, 4, 5, 'CASA');
