package com.assistencia.service;

import com.assistencia.entity.Funcionario;
import com.assistencia.repository.FuncionarioRepository;
import org.mindrot.jbcrypt.BCrypt;

public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Funcionario buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        Funcionario funcionario = funcionarioRepository.buscaOID(id);
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao encontrado.");
        }
        return funcionario;
    }
    public Funcionario cadastraFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        if (funcionario.getEmail() == null || funcionario.getEmail().trim()
                .matches("^[a-zA-Z0-9À-ÿ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email invalido.");
        }
        if (funcionario.getSenha() == null || !funcionario.getSenha()
                .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$")) {
            throw new IllegalArgumentException(
                    "Senha nao pode ser vazia e deve conter letras maiusculas," +
                            " minusculas, numeros e caracteres especiais.");
        }
        if (funcionario.getEspecialidade() == null || funcionario.getEspecialidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade nao pode ser vazio.");
        }
        if (funcionario.getTelefone() == null || !funcionario.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("Telefone nao pode ser vazio ou com mais de 11 digitos.");
        }

        funcionario.setNome(funcionario.getNome().trim().toLowerCase());
        funcionario.setEspecialidade(funcionario.getEspecialidade().trim().toLowerCase());
        funcionario.setTelefone(funcionario.getTelefone().trim().toLowerCase());
        //gera o hash da senha, e salva no banco de dados
        funcionario.setSenha(BCrypt.hashpw(funcionario.getSenha(),BCrypt.gensalt()));

        return funcionarioRepository.cria(funcionario);
    }
    public boolean deletaFuncionario(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        Funcionario funcionario = funcionarioRepository.buscaOID(id);
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao encontrado.");
        }

        return funcionarioRepository.deleta(id);
    }
    public boolean atualizaFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (funcionarioRepository.buscaOID(funcionario.getId()) == null) {
            throw new RuntimeException("Tecnico nao encontrado.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        if (funcionario.getEspecialidade() == null || funcionario.getEspecialidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade nao pode ser vazio.");
        }
        if (funcionario.getTelefone() == null || !funcionario.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("Telefone nao pode ser vazio ou com mais de 11 digitos.");
        }

        funcionario.setNome(funcionario.getNome().trim().toLowerCase());
        funcionario.setEspecialidade(funcionario.getEspecialidade().trim().toLowerCase());
        funcionario.setTelefone(funcionario.getTelefone().trim());

        return funcionarioRepository.atualiza(funcionario);
    }
    public Funcionario loginFuncionario(String email, String senha) {
        if (email == null || senha == null) {
            throw new IllegalArgumentException("Email e senha precisam ser preenchido.");
        }
        if (!email.matches("^[a-zA-Z0-9À-ÿ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email ou Senha incorreta.");
        }
        Funcionario funcionario = funcionarioRepository.buscaOEmail(email);
        if (funcionario == null) {
            throw new IllegalArgumentException("Email ou Senha incorreta.");
        }
        boolean igual = BCrypt.checkpw(senha, funcionario.getSenha());
        if (!igual) {
            return null;
        }
        return funcionario;
    }
}
