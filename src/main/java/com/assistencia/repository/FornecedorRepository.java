package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.Fornecedor;

import java.sql.*;
import java.util.Properties;

public class FornecedorRepository {
    public Fornecedor cria(Fornecedor fornecedor) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "INSERT INTO fornecedores (nome, telefone) VALUES (?, ?)";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, fornecedor.getNome());
            comando.setString(2, fornecedor.getTelefone());

            try(ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    fornecedor.setId(resultado.getInt("id_"));
                }
            }
            return fornecedor;
        }catch (SQLException e) {
            System.err.println("Erro ao criar Fornecedor: " + e.getMessage());
        }
        return null;
    }
    public Fornecedor buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "SELECT * FROM fornecedor WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setId(id);
                    fornecedor.setNome(resultado.getString("nome"));
                    fornecedor.setTelefone(resultado.getString("telefone"));

                    return fornecedor;
                }
            }
        }catch (SQLException e) {
            System.err.println("Erro ao buscar Fornecedor: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "DELETE FROM fornecedor WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int linhasAfetadas = comando.executeUpdate();

            return linhasAfetadas > 1;

        }catch (SQLException e) {
            System.err.println("Erro ao deletar Fornecedor: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(Fornecedor fornecedor) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "UPDATE fornecedores SET nome = ?, telefone = ? WHERE id_ = ?";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, fornecedor.getNome());
            comando.setString(2, fornecedor.getTelefone());
            comando.setInt(3, fornecedor.getId());

            int linhasAtualizadas = comando.executeUpdate();

            return linhasAtualizadas > 1;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Fornecedor: " + e.getMessage());
        }
        return false;
    }
}
