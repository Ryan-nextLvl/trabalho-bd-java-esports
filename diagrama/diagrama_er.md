# Diagrama Entidade-Relacionamento

```
┌─────────────────────────┐
│          TIME           │
├─────────────────────────┤
│ PK  id_time   SERIAL    │
│     nome      VARCHAR   │◄────────────────────────┐
│     tag       VARCHAR   │                         │
│     data_fundacao DATE  │◄──────────┐             │
│     pontuacao INTEGER   │           │             │
└─────────────────────────┘           │             │
           │ 1                        │             │
           │                          │             │
           │ N                        │             │
┌──────────▼──────────────┐           │             │
│         JOGADOR         │   ┌───────▼─────────────▼──────────────┐
├─────────────────────────┤   │             PARTIDA                 │
│ PK  id_jogador SERIAL   │   ├────────────────────────────────────-┤
│     nickname   VARCHAR  │   │ PK  id_partida        SERIAL        │
│     elo        INTEGER  │   │     data_partida       TIMESTAMP    │
│ FK  fk_time    INTEGER──┼──►│     duracao_minutos    INTEGER      │
└─────────────────────────┘   │ FK  fk_time_casa      INTEGER ──►TIME│
                              │ FK  fk_time_visitante INTEGER ──►TIME│
                              │     resultado          VARCHAR       │
                              └─────────────────────────────────────┘
```

## Relacionamentos

| De          | Para   | Cardinalidade | Descrição                              |
|-------------|--------|---------------|----------------------------------------|
| jogador     | time   | N:1           | Jogador pertence a um único time       |
| partida     | time   | N:1 (casa)    | Time mandante da partida               |
| partida     | time   | N:1 (visit.)  | Time visitante da partida              |
