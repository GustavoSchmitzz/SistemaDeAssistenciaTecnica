package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.StatusServico;

import java.sql.*;
import java.util.Properties;

public class StatusServicoRepository {
    public StatusServico cria(StatusServico statusServico) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "INSERT INTO StatusServico (StatusServico) VALUES (?)";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, statusServico.getStatus());

            try(ResultSet chaveGerada = comando.getGeneratedKeys()) {
                if(chaveGerada.next()) {
                    statusServico.setId(chaveGerada.getInt(1));
                }
            }
            return  statusServico;
        }catch (SQLException e) {
            System.err.println("Erro ao criar StatusServico: " + e.getMessage());
        }
        return null;
    }
    public StatusServico buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "SELECT * FROM StatusServico WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()){
                    StatusServico statusServico = new StatusServico();

                    statusServico.setStatus(resultado.getString("status"));
                    statusServico.setId(id);

                    return statusServico;
                }
            }
        }catch (SQLException e) {
            System.err.println("Erro ao buscar StatusServico: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "DELETE FROM StatusServico WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int linhasDeletadas = comando.executeUpdate();

            return linhasDeletadas > 0;

        }catch (SQLException e) {
            System.err.println("Erro ao deletar StatusServico: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(StatusServico statusServico) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "UPDATE StatusServico SET Status = ? WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, statusServico.getStatus());
            comando.setInt(2, statusServico.getId());

            int linhasAtualizadas = comando.executeUpdate();

            return linhasAtualizadas > 0;

        }catch (SQLException e) {
            System.err.println("Erro ao atualizar StatusServico: " + e.getMessage());
        }
        return false;
    }
}