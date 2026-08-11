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
        if (fornecedor == null) {
            throw new IllegalArgumentException("cliente nao pode ser nulo.");
        }
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("nome nao pode ser nulo, vazio ou ter mais de 100 caracteres.");
        }
        if (fornecedor.getTelefone() == null ||
                !fornecedor.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("telefone nao pode ser nulo, vazio ou ter mais de 11 caracteres.");
        }
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
        if (!fornecedorRepository.existsById(id)) {
            throw new IllegalArgumentException("cliente nao encontrado.");
        }
        fornecedorRepository.deleteById(id);
        return true;
    }

    public boolean atualizar(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("fornecedor nao pode ser nulo.");
        }
        if (fornecedor.getId() == null) {
            throw new IllegalArgumentException("id nao pode ser nulo.");
        }
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("nome nao pode ser nulo.");
        }
        if (fornecedor.getTelefone() == null || !fornecedor.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("telefone nao pode ser nulo.");
        }
        fornecedor.setNome(fornecedor.getNome().trim().toLowerCase());
        fornecedor.setTelefone(fornecedor.getTelefone().trim());

        fornecedorRepository.save(fornecedor);
        return true;
    }

    public List<Fornecedor> listar(int pagina, int limite) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("pagina nao pode ser igual ou menor a zero.");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("limite nao pode ser igual ou menor a zero");
        }

        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return fornecedorRepository.findAll(pageable).getContent();
    }
}