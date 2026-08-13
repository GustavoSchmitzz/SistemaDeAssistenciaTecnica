package com.assistencia.service;

import com.assistencia.entity.Fornecedor;
import com.assistencia.repository.FornecedorRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public Fornecedor cadastrar(Fornecedor fornecedor) {
        fornecedor.setNome(fornecedor.getNome().trim().toLowerCase());
        fornecedor.setTelefone(fornecedor.getTelefone().trim().toLowerCase());

        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser menor ou igual a zero.");
        }
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElse(null);
        if (fornecedor == null) {
            throw new IllegalArgumentException("cliente nao encontrado.");
        }
        return fornecedor;
    }

    public boolean remover(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser menor ou igual a zero.");
        }
        fornecedorRepository.deleteById(id);
        return true;
    }

    public boolean atualizar(Fornecedor fornecedor) {
        fornecedor.setNome(fornecedor.getNome().trim().toLowerCase());
        fornecedor.setTelefone(fornecedor.getTelefone().trim());

        fornecedorRepository.save(fornecedor);
        return true;
    }

    public List<Fornecedor> listar(int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return fornecedorRepository.findAll(pageable).getContent();
    }
}