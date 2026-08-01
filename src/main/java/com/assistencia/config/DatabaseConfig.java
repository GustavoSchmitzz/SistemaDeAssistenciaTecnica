package com.assistencia.config;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static Properties propriedades;
    private DatabaseConfig(){};
    public static Properties getCredenciais() {
        if (propriedades == null) {
            propriedades = new Properties();
        }
        try (FileInputStream arquivo = new FileInputStream("database.properties")) {
            propriedades.load(arquivo);
            Class.forName("org.postgresql.Driver");
        } catch (IOException e) {
            System.err.println("Erro na leitura do arquivo: " + e.getMessage());
        }
         catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do PostgreSQL não encontrado no classpath: " + e.getMessage());
         }
        return propriedades;
    }
}
