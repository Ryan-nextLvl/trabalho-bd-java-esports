# Sistema de Gerenciamento de Torneios de E-Sports

Trabalho prático de Banco de Dados — aplicação Java + PostgreSQL com interface gráfica (Swing) tema dark neon e versão console.

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

Depois rode os scripts na ordem:

```sql
\i ddl/create_tables.sql
\i dml/insert_data.sql
```

> Ou pelo terminal Windows:
> ```cmd
> psql -U postgres -d esports_db -f ddl/create_tables.sql
> psql -U postgres -d esports_db -f dml/insert_data.sql
> ```

---

## 2. Adicionar o Driver JDBC

Crie a pasta `lib/` dentro do projeto e coloque o `.jar` lá:

```
trabalho-bd-java-esports/
└── lib/
    └── postgresql-42.7.3.jar
```

---

## 3. Compilar

Abra o **CMD ou PowerShell** dentro da pasta do projeto:

```cmd
mkdir out

javac -cp "lib/postgresql-42.7.3.jar" -sourcepath src -d out src/br/esports/MainSwing.java

xcopy /E /Y src\br\esports\ui\icons out\br\esports\ui\icons\
```

> O `xcopy` copia as imagens para a pasta de saída — necessário para o Swing encontrá-las.

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

Edite `src/br/esports/db/ConexaoBD.java` se necessário:

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/esports_db";
private static final String USUARIO = "postgres";
private static final String SENHA   = "";          // vazio se não usa senha
```

Após editar, recompile com o comando do passo 3.

---

## Estrutura de Pastas

```
trabalho-bd-java-esports/
├── diagrama/
│   └── diagrama_er.md               → Diagrama Entidade-Relacionamento
├── ddl/
│   └── create_tables.sql            → Criação das tabelas (time, jogador, partida)
├── dml/
│   └── insert_data.sql              → Dados iniciais para teste
├── dql/
│   └── queries.sql                  → Consultas SQL (INNER JOIN, LEFT JOIN, filtros)
├── lib/
│   └── postgresql-42.7.3.jar        → Driver JDBC (adicionar manualmente)
├── out/                             → Bytecode compilado (gerado pelo javac)
├── src/br/esports/
│   ├── Main.java                    → Entry point versão console
│   ├── MainSwing.java               → Entry point versão gráfica (Swing)
│   ├── db/
│   │   └── ConexaoBD.java           → Fábrica de conexão JDBC
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
│       ├── Tema.java                → Paleta de cores, fontes e fábrica de componentes
│       ├── Menu.java                → Interface console
│       ├── LoginFrame.java          → Tela de login (fundo arena e-sports)
│       ├── MainFrame.java           → Janela principal com abas coloridas
│       ├── TimePanel.java           → Aba Times
│       ├── JogadorPanel.java        → Aba Jogadores
│       ├── PartidaPanel.java        → Aba Partidas
│       ├── ConsultaPanel.java       → Aba Consultas
│       └── icons/
│           ├── fundo.png            → Imagem de fundo da tela de login
│           ├── icone.png            → Ícone do app (barra de título e taskbar)
│           ├── lixeira.png          → Ícone dos botões Excluir
│           └── furia.png            → Logo da FURIA Esports
└── README.md
```

---

## Funcionalidades

| Aba | Operações |
|-----|-----------|
| **Times** | Cadastrar, atualizar pontuação, atualizar dados completos, excluir com validação |
| **Jogadores** | Cadastrar, atualizar nickname/elo/time, excluir |
| **Partidas** | Registrar, alterar resultado, excluir |
| **Consultas** | Ranking de times, Top ELO, Partidas recentes, INNER JOIN, LEFT JOIN, filtro por time |

---

## Interface

- Tema **dark neon** com paleta e-sports (verde, roxo, cyan, laranja)
- Tela de login com fundo de arena, sem bordas, arrastável
- Botões arredondados com efeito hover
- Tabelas com linhas alternadas e header colorido
- Ícone personalizado na barra de título e taskbar
- Ícone de lixeira nos botões Excluir
