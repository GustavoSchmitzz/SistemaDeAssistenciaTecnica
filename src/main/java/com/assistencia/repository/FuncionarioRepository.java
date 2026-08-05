package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FuncionarioRepository {
    public Funcionario cria(Funcionario funcionario) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "INSERT INTO funcionarios (nome, telefone, especialidade, email, senha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            comando.setString(1, funcionario.getNome());
            comando.setString(2, funcionario.getTelefone());
            comando.setString(3, funcionario.getEspecialidade());
            comando.setString(4, funcionario.getEmail());
            comando.setString(5, funcionario.getSenha());

            comando.executeUpdate();

            try(ResultSet chaveGerada = comando.getGeneratedKeys()){
                if (chaveGerada.next()) {
                    int id = chaveGerada.getInt(1);
                    funcionario.setId(id);
                }
            }
            return funcionario;
        }catch (SQLException e) {
            System.err.println("Erro ao criar tecnico: " + e.getMessage());
        }
        return null;
    }
    public Funcionario buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "SELECT * FROM funcionarios WHERE id = ? ";
        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            comando.executeQuery();

            try (ResultSet resultado = comando.getResultSet()) {
                if (resultado.next()) {
                    Funcionario funcionario = new Funcionario();

                    funcionario.setId(id);
                    funcionario.setNome(resultado.getString("nome"));
                    funcionario.setTelefone(resultado.getString("telefone"));
                    funcionario.setEspecialidade(resultado.getString("especialidade"));
                    funcionario.setEmail(resultado.getString("email"));
                    funcionario.setSenha(resultado.getString("senha"));

                    return funcionario;
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

        String sql = "DELETE FROM funcionarios WHERE id = ? ";

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
    public boolean atualiza(Funcionario funcionario) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");

        String sql = "UPDATE funcionarios SET nome = ?, telefone = ?, especialidade = ? WHERE id = ? ";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, funcionario.getNome());
            comando.setString(2, funcionario.getTelefone());
            comando.setString(3, funcionario.getEspecialidade());
            comando.setInt(4, funcionario.getId());

            int linhasAtualizada = comando.executeUpdate();

            return linhasAtualizada > 0;

        }catch (SQLException e) {
            System.err.println("Erro ao atualizar tecnico: " + e.getMessage());
        }
        return false;
    }
    public Funcionario buscaOEmail(String email) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM funcionarios WHERE email = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setString(1, email);

            comando.executeQuery();

            try(ResultSet resultado = comando.getResultSet()) {
                if (resultado.next()) {
                    Funcionario funcionario = new Funcionario();
                    funcionario.setId(resultado.getInt("id"));
                    funcionario.setNome(resultado.getString("nome"));
                    funcionario.setEmail(resultado.getString("email"));
                    funcionario.setTelefone(resultado.getString("telefone"));
                    funcionario.setEspecialidade(resultado.getString("especialidade"));
                    funcionario.setSenha(resultado.getString("senha"));

                    return funcionario;
                }
            }
        }catch (SQLException e) {
            System.err.println("Erro ao logar: " + e.getMessage());
        }
        return null;
    }
    public List<Funcionario> buscaFuncionariosDaPagina(int limite, int offset) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM funcionarios ORDER BY id LIMIT ? OFFSET ?";

        try(Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, limite);
            comando.setInt(2, offset);

            try (ResultSet resultado = comando.executeQuery()) {
                List<Funcionario> funcionarios = new ArrayList<>();
                while (resultado.next()) {
                    Funcionario funcionario = new Funcionario();
                    funcionario.setId(resultado.getInt("id"));
                    funcionario.setNome(resultado.getString("nome"));
                    funcionario.setEmail(resultado.getString("email"));
                    funcionario.setTelefone(resultado.getString("telefone"));
                    funcionario.setEspecialidade(resultado.getString("especialidade"));
                }
                return funcionarios;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionarios da pagina: " + e.getMessage());
        }
        return null;
    }
}