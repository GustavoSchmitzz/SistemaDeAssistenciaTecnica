package com.assistencia.config;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static Properties propriedades;
    private DatabaseConfig(){};
    public static Properties getCredenciais() throws IOException {
        if (propriedades == null) {
            propriedades = new Properties();
        }
        try (FileInputStream arquivo = new FileInputStream("config.properties")) {
            propriedades.load(arquivo);
        } catch (IOException e) {
            throw new IOException("Arquivo não encontrado.");
        }
        return propriedades;
    }
}
