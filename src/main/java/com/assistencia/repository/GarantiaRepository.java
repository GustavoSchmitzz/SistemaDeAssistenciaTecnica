package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.Garantia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GarantiaRepository {
    public Garantia cria(Garantia garantia) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "INSERT INTO garantia (dias_de_garantia) VALUES (?)";
         try (Connection conexao = DriverManager.getConnection(url, user, password);
              PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

             comando.setInt(1, garantia.getDiasDeGarantia());

             comando.executeUpdate();

             try (ResultSet resultado = comando.getGeneratedKeys()) {
                 if (resultado.next()) {
                     garantia.setId(resultado.getInt(1));
                 }
             }
             return garantia;
         }catch (Exception e) {
             System.err.println("Erro ao criar garantia: " + e.getMessage());
         }
         return null;
    }
    public Garantia buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "SELECT * FROM garantia WHERE id_ordem_de_servico = ?";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try(ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    Garantia garantia = new Garantia();
                    garantia.setId(id);
                    garantia.setDiasDeGarantia(resultado.getInt("dias_de_garantia"));

                    return garantia;
                }
            }
        }catch (Exception e) {
            System.err.println("Erro ao buscar garantia: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "DELETE FROM garantias WHERE id_ = ?";
        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int linhasDeletadas = comando.executeUpdate();

            return linhasDeletadas > 0;
        }catch (Exception e) {
            System.err.println("Erro ao deletar garantia: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(Garantia garantia) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "UPDATE garantia SET dias_de_garantia = ? WHERE id_ = ?";
        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, garantia.getDiasDeGarantia());
            comando.setInt(2, garantia.getId());

            int resultado = comando.executeUpdate();

            return resultado > 0;
        }catch (Exception e) {
            System.err.println("Erro ao atualizar garantia: " + e.getMessage());
        }
        return false;
    }
    public List<Garantia> buscaGarantias() {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM garantias";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            try (ResultSet resultado = comando.executeQuery()) {
                List<Garantia> garantias = new ArrayList<>();
                while (resultado.next()) {
                    Garantia garantia = new Garantia();
                    garantia.setId(resultado.getInt("id"));
                    garantia.setDiasDeGarantia(resultado.getInt("dias_de_garantia"));
                    garantias.add(garantia);
                }
                return garantias;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar garantias: " + e.getMessage());
        }
        return null;
    }
}
