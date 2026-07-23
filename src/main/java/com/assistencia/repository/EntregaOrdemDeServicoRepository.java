package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;

import com.assistencia.entity.EntregaOrdemDeServico;
import com.assistencia.entity.OrdemDeServico;

import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class EntregaOrdemDeServicoRepository {
    public EntregaOrdemDeServico cria(EntregaOrdemDeServico entregaOS) {
        Properties credenciais = DatabaseConfig.getCredenciais();

        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.password");

        String sql = "INSERT INTO entrega_ordens_de_servico (dataEntrega, ordemDeServico) VALUES (?, ?)";

        try(Connection conexao = DriverManager.getConnection(url, user,password);
            PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setDate(1, (Date) entregaOS.getDataEntrega());
            comando.setInt(2, entregaOS.getOrdemDeServico().getId());

            comando.executeUpdate();

            try (ResultSet chaveGerada = comando.getGeneratedKeys()) {
                if (chaveGerada.next()) {
                    entregaOS.setId(chaveGerada.getInt(1));
                }
            }
            return entregaOS;
        }catch(SQLException e){
            System.err.println("Erro ao salvar a entrega: " + e.getMessage());
        }
        return null;
    }
    public EntregaOrdemDeServico buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.password");

        String sql = "SELECT * FROM entrega_ordens_de_servico WHERE id = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, id);

            try(ResultSet resultado = comando.executeQuery()){
                if(resultado.next()) {
                    EntregaOrdemDeServico entregaOS = new EntregaOrdemDeServico();

                    entregaOS.setId(id);
                    entregaOS.setDataEntrega(resultado.getDate("data_entrega"));

                    OrdemDeServico ordemDeServico = new OrdemDeServico();
                    ordemDeServico.setId(resultado.getInt("id_ordem_servico"));

                    entregaOS.setOrdemDeServico(ordemDeServico);

                    return entregaOS;
                }
            }
        }catch(SQLException e){
            System.err.println("Erro ao salvar a entrega: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.password");

        String sql = "DELETE FROM entrega_ordens_de_servico WHERE id = ?";
        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int linhasDeletadas = comando.executeUpdate();

            return linhasDeletadas > 0;

        }catch(SQLException e){
            System.err.println("Erro ao salvar a entrega: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(EntregaOrdemDeServico entregaOS) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.password");

        String sql = "UPDATE entrega_ordens_de_servico SET data_entrega = ?, id_ordem_servico WHERE id = ?";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setDate(1, (Date) entregaOS.getDataEntrega());
            comando.setInt(2, entregaOS.getOrdemDeServico().getId());
            comando.setInt(3, entregaOS.getId());

            int linhasAtualizadas = comando.executeUpdate();

            return linhasAtualizadas > 0;
        }catch(SQLException e){
            System.err.println("Erro ao salvar a entrega: " + e.getMessage());
        }
        return false;
    }
}