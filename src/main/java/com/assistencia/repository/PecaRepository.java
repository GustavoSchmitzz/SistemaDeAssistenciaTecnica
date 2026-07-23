package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.Fornecedor;
import com.assistencia.entity.Peca;

import java.sql.*;
import java.util.Properties;

public class PecaRepository {
    public Peca cria(Peca peca) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "INSERT INTO pecas (nome, valor, estoque, fornecedor) VALUES (?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            comando.setString(1, peca.getNome());
            comando.setDouble(2, peca.getValor());
            comando.setInt(3, peca.getEstoque());
            comando.setInt(4, peca.getFornecedor().getId());

            comando.executeUpdate();

            try (ResultSet chaveGerada = comando.getGeneratedKeys()) {
                if (chaveGerada.next()) {
                    int id = chaveGerada.getInt(1);
                    peca.setId(id);
                }
            }
            return peca;
        }catch (SQLException e){
            System.err.println("Erro ao criar Peca: " + e.getMessage());
        }
        return null;
    }
    public Peca buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "SELECT * FROM pecas WHERE id_ = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try(ResultSet resultado = comando.executeQuery()) {
                if(resultado.next()) {
                    Peca peca = new Peca();
                    peca.setId(id);
                    peca.setNome(resultado.getString("nome"));
                    peca.setValor(resultado.getDouble("valor"));

                    Fornecedor fornecedor = new Fornecedor();

                    fornecedor.setId(resultado.getInt("id_fornecedor"));
                    peca.setFornecedor(fornecedor);
                    return peca;
                }
            }
        }catch (SQLException e){
            System.err.println("Erro ao buscar Peca: " + e.getMessage());
        }
        return null;
    }
    public boolean deletar(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "DELETE FROM pecas WHERE id_ = ?";
        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int linhasDeletadas = comando.executeUpdate();

            return linhasDeletadas > 0;

        }catch (SQLException e){
            System.err.println("Erro ao deletar Peca: " + e.getMessage());
        }
        return false;
    }
    public boolean atualizar(Peca peca) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "UPDATE tecnicos SET nome = ?, telefone = ?, estoque = ?, fornecedor = ? WHERE id_ = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, peca.getNome());
            comando.setDouble(2, peca.getValor());
            comando.setInt(3, peca.getEstoque());
            comando.setInt(4, peca.getFornecedor().getId());
            comando.setInt(5, peca.getId());

            int linhasAtualizadas = comando.executeUpdate();

            return linhasAtualizadas > 0;

        }catch (SQLException e){
            System.err.println("Erro ao atualizar Peca: " + e.getMessage());
        }
        return false;
    }
}
