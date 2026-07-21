package com.assistencia.repository;

import com.assistencia.entity.Cliente;
import com.assistencia.config.DatabaseConfig;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class ClienteRepository {
    public Cliente salvar(Cliente cliente) throws IOException {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "INSERT INTO cliente (nome, email, telefone) VALUES (?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(url, user, password)) {
            PreparedStatement comando = conexao.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);

            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getEmail());
            comando.setString(3, cliente.getTelefone());

            comando.executeUpdate();

            try (ResultSet chaveGerada = comando.getGeneratedKeys()) {
                if (chaveGerada.next()) {
                    int id_gerado = chaveGerada.getInt(1);
                    cliente.setId(id_gerado);
                }
            }
            return cliente;
        }
        catch (SQLException e)
        {
            System.err.println("Erro ao salvar o cliente: " + e.getMessage());
            return null;
        }
    }

}

