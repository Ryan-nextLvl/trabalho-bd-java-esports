package br.esports.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String URL     = "jdbc:postgresql://localhost:5432/esports_db";
    private static final String USUARIO = "postgres";
    private static final String SENHA   = "";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL não encontrado. Verifique se o .jar está no classpath.", e);
        }
    }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
