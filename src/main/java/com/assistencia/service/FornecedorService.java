package com.assistencia.service;

import com.assistencia.entity.Fornecedor;
import com.assistencia.repository.FornecedorRepository;

public class FornecedorService {
    private FornecedorRepository fornecedorRepository;
    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public Fornecedor cadastrar(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("cliente nao pode ser nulo.");
        }
        if(fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("nome nao pode ser nulo, vazio ou ter mais de 100 caracteres.");
        }
        if(fornecedor.getTelefone() == null ||
                !fornecedor.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("telefone nao pode ser nulo, vazio ou ter mais de 11 caracteres.");
        }

        fornecedor.setNome(fornecedor.getNome().trim().toLowerCase());
        fornecedor.setTelefone(fornecedor.getTelefone().trim().toLowerCase());

        return fornecedorRepository.cria(fornecedor);
    }
    public Fornecedor buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser menor ou igual a zero.");
        }
        Fornecedor fornecedor = fornecedorRepository.buscaOID(id);
        if (fornecedor == null) {
            throw new IllegalArgumentException("cliente nao encontrado.");
        }
        return fornecedor;
    }
    public boolean remover(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser menor ou igual a zero.");
        }
        Fornecedor fornecedor = fornecedorRepository.buscaOID(id);
        if (fornecedor == null) {
            throw new IllegalArgumentException("cliente nao encontrado.");
        }
        return fornecedorRepository.deleta(id);
    }
    public boolean atualizar(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("fornecedor nao pode ser nulo.");
        }
        if (fornecedor.getId() == null) {
            throw new IllegalArgumentException("id nao pode ser nulo.");
        }
        if (fornecedorRepository.buscaOID(fornecedor.getId()) == null) {
            throw new IllegalArgumentException("O fornecedor nao existe no banco de dados.");
        }
        if(fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("nome nao pode ser nulo.");
        }
        if(fornecedor.getTelefone() == null || fornecedor.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("telefone nao pode ser nulo.");
        }

        fornecedor.setNome(fornecedor.getNome().trim().toLowerCase());
        fornecedor.setTelefone(fornecedor.getTelefone().trim());

        return fornecedorRepository.atualiza(fornecedor);
    }
}


