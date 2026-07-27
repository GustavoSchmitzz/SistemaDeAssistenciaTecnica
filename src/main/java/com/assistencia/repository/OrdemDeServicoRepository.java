package com.assistencia.repository;

import com.assistencia.config.DatabaseConfig;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.entity.Peca;
import com.assistencia.entity.StatusServico;
import com.assistencia.entity.Tecnico;

import java.sql.*;
import java.util.Properties;

import static java.sql.Date.valueOf;

public class OrdemDeServicoRepository {
    public OrdemDeServico cria(OrdemDeServico ordemDeServico) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "INSERT INTO ordens_de_servico (valor_servico, data_inicio," +
                " tecnico, peca, status_servico) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            comando.setDouble(1, ordemDeServico.getValorServico());
            comando.setDate(2, valueOf(ordemDeServico.getDataInicio()));
            comando.setInt(3, ordemDeServico.getTecnico().getId());
            comando.setInt(4, ordemDeServico.getPeca().getId());
            comando.setInt(5, ordemDeServico.getStatusServico().getId());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if(resultado.next()) {
                    ordemDeServico.setId(resultado.getInt(1));
                    return ordemDeServico;
                }
            }
        }catch (SQLException e) {
            System.err.println("Erro ao criar ordem de servico: " + e.getMessage());
        }
        return null;
    }
    public OrdemDeServico buscaOID(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "SELECT * FROM ordens_de_servico WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try(ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    OrdemDeServico os = new OrdemDeServico();
                    os.setId(resultado.getInt("id"));
                    os.setDataInicio(resultado.getDate("data_inicio").toLocalDate());

                    Tecnico tecnico = new Tecnico();
                    tecnico.setId(resultado.getInt("id_tecnico"));
                    os.setTecnico(tecnico);

                    Peca peca = new Peca();
                    peca.setId(resultado.getInt("id_peca"));
                    os.setPeca(peca);

                    StatusServico status = new StatusServico();
                    status.setId(resultado.getInt("status_servico"));
                    os.setStatusServico(status);

                    return os;
                }
            }
        }catch (SQLException e) {
            System.err.println("Erro ao buscar ordem de servico: " + e.getMessage());
        }
        return null;
    }
    public boolean deleta(int id) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "DELETE FROM ordens_de_servico WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            int resultado = comando.executeUpdate();

            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar ordem de servico: " + e.getMessage());
        }
        return false;
    }
    public boolean atualiza(OrdemDeServico ordemDeServico) {
        Properties credenciais = DatabaseConfig.getCredenciais();
        String url = credenciais.getProperty("db.url");
        String user = credenciais.getProperty("db.usuario");
        String password = credenciais.getProperty("db.senha");
        String sql = "UPDATE ordens_de_servico SET valor_servico = ?," +
                "data_inicio = ?, tecnico = ?, peca = ?, status_servico = ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, user, password);
            PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setDouble(1, ordemDeServico.getValorServico());
            comando.setDate(2, valueOf(ordemDeServico.getDataInicio()));
            comando.setInt(3, ordemDeServico.getTecnico().getId());
            comando.setInt(4, ordemDeServico.getPeca().getId());
            comando.setInt(5, ordemDeServico.getStatusServico().getId());
            comando.setInt(6, ordemDeServico.getId());

            int resultado = comando.executeUpdate();

            return resultado > 0;

        }catch (SQLException e) {
            System.err.println("Erro ao atualizar ordem de servico: " + e.getMessage());
        }
        return false;
    }
}