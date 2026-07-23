package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.Tecnico;

import java.sql.*;
import java.util.Properties;

public class TecnicoRepository {
    public Tecnico cria(Tecnico tecnico) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "INSERT INTO tecnicos (nome, telefone, especialidade) VALUES (?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            comando.setString(1, tecnico.getNome());
            comando.setString(2, tecnico.getTelefone());
            comando.setString(3, tecnico.getEspecialidade());

            comando.executeUpdate();

            try(ResultSet chaveGerada = comando.getGeneratedKeys()){
                if (chaveGerada.next()) {
                    int id = chaveGerada.getInt(1);
                    tecnico.setId(id);
                }
            }
            return tecnico;
        }catch (SQLException e) {
            System.err.println("Erro ao criar tecnico: " + e.getMessage());
        }
        return null;
    }
    public Tecnico buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "SELECT * FROM tecnicos WHERE id = ? ";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            comando.executeQuery();

            try (ResultSet resultado = comando.getResultSet()) {
                if (resultado.next()) {
                    Tecnico tecnico = new Tecnico();

                    tecnico.setId(id);
                    tecnico.setNome(resultado.getString("nome"));
                    tecnico.setTelefone(resultado.getString("telefone"));
                    tecnico.getEspecialidade(resultado.getString("especialidade"));

                    return tecnico;
                }
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tecnico: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "DELETE FROM tecnicos WHERE id = ? ";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int linhasDeletada = comando.executeUpdate();

            return linhasDeletada > 0;

        }catch (SQLException e) {
            System.err.println("Erro ao deletar tecnico: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(Tecnico tecnico) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "UPDATE tecnicos SET nome = ?, telefone = ?, especialidade = ? WHERE id = ? ";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, tecnico.getNome());
            comando.setString(2, tecnico.getTelefone());
            comando.setString(3, tecnico.getEspecialidade());
            comando.setInt(4, tecnico.getId());

            int linhasAtualizada = comando.executeUpdate();

            return linhasAtualizada > 0;

        }catch (SQLException e) {
            System.err.println("Erro ao atualizar tecnico: " + e.getMessage());
        }
        return false;
    }
}
