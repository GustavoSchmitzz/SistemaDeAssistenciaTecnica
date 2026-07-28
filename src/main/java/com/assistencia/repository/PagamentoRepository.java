package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.entity.Pagamento;

import java.sql.*;
import java.util.Properties;

public class PagamentoRepository {
    public Pagamento cria(Pagamento pagamento) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "INSERT INTO pagamentos (forma_pagamento) VALUES (?)";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, pagamento.getFormaPagamento());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    pagamento.setId(resultado.getInt("id"));
                }
            }
            return  pagamento;
        }catch (SQLException e) {
            System.err.println("Erro ao criar pagamento: " + e.getMessage());
        }
        return null;
    }
    public Pagamento buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM pagamentos WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setId(id);
                    pagamento.setFormaPagamento(resultado.getString("forma_pagamento"));

                    return pagamento;
                }
            }
        }catch (SQLException e) {
            System.err.println("Erro ao buscar pagamento: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "DELETE FROM pagamentos WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int resultado = comando.executeUpdate();

            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar pagamento: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(Pagamento pagamento) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "UPDATE pagamentos SET forma_pagamento = ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, pagamento.getFormaPagamento());
            comando.setInt(2, pagamento.getId());

            int resultado = comando.executeUpdate();

            return resultado > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pagamento: " + e.getMessage());
        }
        return false;
    }
}
