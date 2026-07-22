package com.assistencia.repository;

import com.assistencia.entity.Cliente;
import com.assistencia.config.DatabaseConfig;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class ClienteRepository {
    public Cliente cria(Cliente cliente) {
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
    public Cliente buscarOID(Integer id) {
        Properties credenciais = DatabaseConfig.getCredenciais();

        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "SELECT * FROM clientes WHERE id = ?";
        try(Connection conexao = DriverManager.getConnection(url, user, password)){
            PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

             comando.setInt(1, id);

             try (ResultSet resultado = comando.executeQuery()) {
                 if(resultado.next()) {
                     Cliente cliente = new Cliente();
                     cliente.setId(resultado.getInt("id"));
                     cliente.setNome(resultado.getString("nome"));
                     cliente.setEmail(resultado.getString("email"));
                     cliente.setTelefone(resultado.getString("telefone"));

                     return cliente;
                 }
             }

        }catch (SQLException e){
            System.err.println("A busca por id falhou: " +  e.getMessage());
        }
        return null;
    }
    public boolean deleta(Integer id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "DELETE FROM clientes WHERE id = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, password)){
            PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            comando.setInt(1, id);

            int linhasDeletadas = comando.executeUpdate();

            if (linhasDeletadas > 0) {
                return true;
            } else  {
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar o cliente: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(Cliente cliente) {
        Properties credenciais = DatabaseConfig.getCredenciais();

        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "UPDATE clientes SET nome = ?, email = ?, telefone = ? WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(url, user, password)){
            PreparedStatement comando = conexao.prepareStatement(sql);

            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getEmail());
            comando.setString(3, cliente.getTelefone());
            comando.setInt(4, cliente.getId());

            int linhasAtualizadas = comando.executeUpdate();

            return linhasAtualizadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o cliente: " + e.getMessage());
        }
        return false;
    }
}

