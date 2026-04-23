# Sistema de Gerenciamento de Torneios de E-Sports

Trabalho prático de Banco de Dados — aplicação Java + PostgreSQL com interface gráfica (Swing) e versão console.

---

## Pré-requisitos

- [Java 17+](https://www.oracle.com/java/technologies/downloads/)
- [PostgreSQL 14+](https://www.postgresql.org/download/)
- Driver JDBC: [`postgresql-42.7.3.jar`](https://jdbc.postgresql.org/download/)

---

## 1. Configurar o Banco de Dados

Abra o **SQL Shell (psql)** e execute:

```sql
CREATE DATABASE esports_db;
\c esports_db
```

Depois rode os scripts SQL na ordem:

```sql
-- 1. Criar as tabelas
\i ddl/create_tables.sql

-- 2. Inserir dados de exemplo
\i dml/insert_data.sql
```

> Se preferir pelo terminal Windows:
> ```cmd
> psql -U postgres -d esports_db -f ddl/create_tables.sql
> psql -U postgres -d esports_db -f dml/insert_data.sql
> ```

---

## 2. Adicionar o Driver JDBC

Crie a pasta `lib/` dentro do projeto e coloque o arquivo `postgresql-42.7.3.jar` lá:

```
trabalho-bd-java-esports/
└── lib/
    └── postgresql-42.7.3.jar
```

---

## 3. Compilar o Projeto

Abra o **CMD** ou **PowerShell** dentro da pasta do projeto e execute:

```cmd
mkdir out

javac -cp "lib/postgresql-42.7.3.jar" -sourcepath src -d out src/br/esports/MainSwing.java
```

> Isso compila automaticamente todos os arquivos `.java` do projeto.

---

## 4. Executar

### Interface Gráfica (Swing) — recomendado

```cmd
java -cp "out;lib/postgresql-42.7.3.jar" br.esports.MainSwing
```

### Versão Console

```cmd
java -cp "out;lib/postgresql-42.7.3.jar" br.esports.Main
```

---

## 5. Login

| Campo   | Valor   |
|---------|---------|
| Usuário | `admin` |
| Senha   | `123`   |

---

## 6. Configuração da Conexão

Se o seu PostgreSQL usar usuário ou senha diferentes de `postgres/postgres`, edite o arquivo:

```
src/br/esports/db/ConexaoBD.java
```

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/esports_db";
private static final String USUARIO = "postgres";   // altere aqui
private static final String SENHA   = "postgres";   // altere aqui
```

Após editar, recompile com o comando do passo 3.

---

## Estrutura de Pastas

```
trabalho-bd-java-esports/
├── diagrama/
│   └── diagrama_er.md          → Diagrama Entidade-Relacionamento
├── ddl/
│   └── create_tables.sql       → Criação das tabelas (time, jogador, partida)
├── dml/
│   └── insert_data.sql         → Dados iniciais para teste
├── dql/
│   └── queries.sql             → Consultas SQL (INNER JOIN, LEFT JOIN, filtros)
├── lib/
│   └── postgresql-42.7.3.jar   → Driver JDBC (adicionar manualmente)
├── out/                        → Bytecode compilado (gerado pelo javac)
├── src/br/esports/
│   ├── Main.java               → Entry point versão console
│   ├── MainSwing.java          → Entry point versão gráfica (Swing)
│   ├── db/
│   │   └── ConexaoBD.java      → Fábrica de conexão JDBC
│   ├── model/
│   │   ├── Time.java
│   │   ├── Jogador.java
│   │   └── Partida.java
│   ├── dao/
│   │   ├── TimeDAO.java
│   │   ├── JogadorDAO.java
│   │   ├── PartidaDAO.java
│   │   └── impl/
│   │       ├── TimeDAOImpl.java
│   │       ├── JogadorDAOImpl.java
│   │       └── PartidaDAOImpl.java
│   ├── service/
│   │   ├── TimeService.java
│   │   ├── JogadorService.java
│   │   └── PartidaService.java
│   └── ui/
│       ├── Menu.java           → Interface console
│       ├── LoginFrame.java     → Tela de login (Swing)
│       ├── MainFrame.java      → Janela principal com abas
│       ├── TimePanel.java      → Aba de Times
│       ├── JogadorPanel.java   → Aba de Jogadores
│       ├── PartidaPanel.java   → Aba de Partidas
│       └── ConsultaPanel.java  → Aba de Consultas
└── README.md
```

---

## Funcionalidades

| Aba | Operações |
|-----|-----------|
| **Times** | Cadastrar, atualizar pontuação, atualizar dados completos, excluir (com validação de dependências) |
| **Jogadores** | Cadastrar, atualizar nickname/elo/time, excluir |
| **Partidas** | Registrar nova partida, alterar resultado, excluir |
| **Consultas** | Times por ranking, Top ELO, Partidas recentes, INNER JOIN, LEFT JOIN, filtro por time |
