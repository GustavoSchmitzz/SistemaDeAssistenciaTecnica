package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.Cliente;
import com.assistencia.entity.PecaComDefeito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PecaComDefeitoRepository {
    public PecaComDefeito criar(PecaComDefeito pecaComDefeito) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "INSERT INTO pecas_com_defeito (tipo_peca, marca, modelo, descricao, id_cliente)" +
                " VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            comando.setString(1,pecaComDefeito.getTipoPeca());
            comando.setString(2,pecaComDefeito.getMarca());
            comando.setString(3,pecaComDefeito.getModelo());
            comando.setString(4,pecaComDefeito.getDescricao());
            comando.setInt(5, pecaComDefeito.getCliente().getId());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    pecaComDefeito.setId(resultado.getInt(1));
                }
            }
            return pecaComDefeito;
        } catch (SQLException e) {
            System.err.println("Erro ao criar PecaComDefeito: " + e.getMessage());
        }
        return null;
    }
    public PecaComDefeito buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM pecas_com_defeito WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    PecaComDefeito pecaComDefeito = new PecaComDefeito();
                    pecaComDefeito.setId(id);
                    pecaComDefeito.setTipoPeca(resultado.getString("tipo_peca"));
                    pecaComDefeito.setMarca(resultado.getString("marca"));
                    pecaComDefeito.setModelo(resultado.getString("modelo"));
                    pecaComDefeito.setDescricao(resultado.getString("descricao"));

                    Cliente cliente = new Cliente();
                    cliente.setId(resultado.getInt("id_cliente"));
                    pecaComDefeito.setCliente(cliente);

                    return pecaComDefeito;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar PecaComDefeito: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "DELETE FROM pecas_com_defeito WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int resultado = comando.executeUpdate();

            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar PecaComDefeito: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(PecaComDefeito pecaComDefeito) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "UPDATE pecas_com_defeito SET tipo_peca = ?, marca = ?, modelo = ?," +
                " descricao = ?, id_cliente = ?  WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, pecaComDefeito.getTipoPeca());
            comando.setString(2, pecaComDefeito.getMarca());
            comando.setString(3, pecaComDefeito.getModelo());
            comando.setString(4, pecaComDefeito.getDescricao());
            comando.setInt(5, pecaComDefeito.getCliente().getId());
            comando.setInt(6, pecaComDefeito.getId());

            int resultado = comando.executeUpdate();

            return resultado > 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar PecaComDefeito: " + e.getMessage());
        }
        return false;
    }
    public List<PecaComDefeito> buscaPecasComDefeitoDaPagina(int limite, int offset) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM pecas_com_defeito ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, limite);
            comando.setInt(2, offset);

            try (ResultSet resultado = comando.executeQuery()) {
                List<PecaComDefeito> pecasComDefeito = new ArrayList<>();
                while (resultado.next()) {
                    PecaComDefeito peca = new PecaComDefeito();
                    peca.setId(resultado.getInt("id"));
                    peca.setTipoPeca(resultado.getString("tipo_peca"));
                    peca.setMarca(resultado.getString("marca"));
                    peca.setModelo(resultado.getString("modelo"));
                    peca.setDescricao(resultado.getString("descricao"));

                    Cliente cliente = new Cliente();
                    cliente.setId(resultado.getInt("id_cliente"));
                    peca.setCliente(cliente);

                    pecasComDefeito.add(peca);
                }
                return pecasComDefeito;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pecas com defeito da pagina: " + e.getMessage());
        }
        return null;
    }
}
