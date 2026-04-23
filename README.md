# Sistema de Gerenciamento de Torneios de E-Sports

Trabalho prático de Banco de Dados — aplicação Java + PostgreSQL rodando no console.

## Pré-requisitos

- Java 17+
- PostgreSQL 14+
- Driver JDBC: `postgresql-42.x.x.jar` (baixar em https://jdbc.postgresql.org/download/)

## Configuração do Banco de Dados

```sql
CREATE DATABASE esports_db;
```

Conecte ao banco e execute os scripts na ordem:

```bash
psql -U postgres -d esports_db -f ddl/create_tables.sql
psql -U postgres -d esports_db -f dml/insert_data.sql
```

## Compilação e Execução

```bash
# Compilar (com o driver na pasta lib/)
javac -cp lib/postgresql-42.7.3.jar -sourcepath src -d out \
      src/br/esports/Main.java

# Executar
java -cp "out:lib/postgresql-42.7.3.jar" br.esports.Main
```

> No Windows substitua `:` por `;` no classpath.

## Credenciais de Login

| Campo   | Valor   |
|---------|---------|
| Usuário | `admin` |
| Senha   | `123`   |

## Estrutura de Pastas

```
/diagrama   → Diagrama ER (diagrama_er.md)
/ddl        → Criação das tabelas (create_tables.sql)
/dml        → Dados de exemplo (insert_data.sql)
/dql        → Consultas SQL (queries.sql)
/src        → Código-fonte Java
  br/esports/
    db/        → ConexaoBD.java
    model/     → Time, Jogador, Partida
    dao/       → Interfaces DAO
    dao/impl/  → Implementações DAO
    service/   → Regras de negócio
    ui/        → Menu console
    Main.java
README.md
```

## Funcionalidades

- **Cadastrar** → time, jogador, partida
- **Atualizar** → pontuação do time, dados do jogador, resultado da partida
- **Excluir** → com validação de dependências (time só é removido se não tiver jogadores/partidas)
- **Consultar** → 6 opções incluindo INNER JOIN e LEFT JOIN
