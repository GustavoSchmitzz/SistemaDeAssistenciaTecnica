package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.entity.OrdemPeca;
import com.assistencia.entity.Peca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OrdemPecaRepository {
    public OrdemPeca cria(OrdemPeca ordemPeca) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "INSERT INTO ordem_peca (quantidade, id_ordem_servico, id_peca) VALUES (?, ?, ?)";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setInt(1, ordemPeca.getQuantidade());
            comando.setInt(2, ordemPeca.getOrdemDeServico().getId());
            comando.setInt(3, ordemPeca.getPeca().getId());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    ordemPeca.setId(resultado.getInt("id"));
                }
            }
            return  ordemPeca;
        }catch (Exception e) {
            System.err.println("Erro ao criar ordem_peca: " + e.getMessage());
        }
        return null;
    }
    public OrdemPeca buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM ordem_peca WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try (ResultSet resultado = comando.executeQuery()){
                if (resultado.next()) {
                    OrdemPeca ordemPeca = new OrdemPeca();
                    ordemPeca.setId(id);
                    ordemPeca.setQuantidade(resultado.getInt("quantidade"));

                    OrdemDeServico os = new OrdemDeServico();
                    os.setId(resultado.getInt("id_ordem_servico"));
                    ordemPeca.setOrdemDeServico(os);

                    Peca peca = new Peca();
                    peca.setId(resultado.getInt("id_peca"));
                    ordemPeca.setPeca(peca);

                    return ordemPeca;
                }
            }
        }catch (Exception e) {
            System.err.println("Erro ao buscar ordem_peca: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "DELETE FROM ordem_peca WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int resultado = comando.executeUpdate();

            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar ordem_peca: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(OrdemPeca ordemPeca) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "UPDATE ordem_peca SET quantidade = ?, id_peca = ?, id_ordem_servico = ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, ordemPeca.getQuantidade());
            comando.setInt(2, ordemPeca.getPeca().getId());
            comando.setInt(3, ordemPeca.getOrdemDeServico().getId());

            int resultado = comando.executeUpdate();

            return resultado > 0;

        }catch (SQLException e) {
            System.err.println("Erro ao atualizar ordem_peca: " + e.getMessage());
        }
        return false;
    }
    public List<OrdemPeca> buscaOrdemPecaDaPagina(int limite, int offset) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM ordem_pecas ORDER BY id LIMIT ? OFFSET ?";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, limite);
            comando.setInt(2, offset);

            try (ResultSet resultado = comando.executeQuery()) {
                List<OrdemPeca> pecas = new ArrayList<>();
                while (resultado.next()) {
                    OrdemPeca ordemPeca = new OrdemPeca();
                    ordemPeca.setId(resultado.getInt("id"));
                    ordemPeca.setQuantidade(resultado.getInt("quantidade"));

                    Peca peca = new Peca();
                    peca.setId(resultado.getInt("id_peca"));
                    ordemPeca.setPeca(peca);

                    pecas.add(ordemPeca);
                }
                return pecas;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ordens de paca da pagina: " + e.getMessage());
        }
        return null;
    }
}
