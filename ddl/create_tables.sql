-- DDL: Criação das tabelas do sistema de torneios de e-sports

CREATE TABLE time (
    id_time             SERIAL PRIMARY KEY,
    nome                VARCHAR(100) NOT NULL UNIQUE,
    tag                 VARCHAR(10)  NOT NULL UNIQUE,
    data_fundacao       DATE,
    pontuacao_ranking   INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE jogador (
    id_jogador  SERIAL PRIMARY KEY,
    nickname    VARCHAR(50) NOT NULL,
    elo         INTEGER NOT NULL DEFAULT 1000,
    fk_time     INTEGER REFERENCES time(id_time)
);

CREATE TABLE partida (
    id_partida          SERIAL PRIMARY KEY,
    data_partida        TIMESTAMP   NOT NULL,
    duracao_minutos     INTEGER     NOT NULL,
    fk_time_casa        INTEGER     NOT NULL REFERENCES time(id_time),
    fk_time_visitante   INTEGER     NOT NULL REFERENCES time(id_time),
    resultado           VARCHAR(20) CHECK (resultado IN ('CASA','VISITANTE','EMPATE'))
);
