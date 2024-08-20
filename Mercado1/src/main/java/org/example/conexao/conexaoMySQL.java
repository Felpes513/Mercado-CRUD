package org.example.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexaoMySQL {
    // Essa classe vai gerenciar o banco de dados

    public static Connection getConnection() throws SQLException{
        try {
            return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.SENHA);
        }catch (SQLException e){
            System.out.println("Erro ao conectar ao banco de dados:" + e.getMessage());
            throw e;
        }
    }

}
