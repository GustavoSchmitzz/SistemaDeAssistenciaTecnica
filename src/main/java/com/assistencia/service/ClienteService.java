package com.assistencia.service;

import com.assistencia.entity.Cliente;
import com.assistencia.repository.ClienteRepository;

public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("cliente nao pode ser nulo.");
        }
        if(cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("nome nao pode ser nulo, vazio ou ter mais de 100 caracteres.");
        }
        if(cliente.getEmail() == null || !cliente.getEmail().trim()
                .matches("^[a-zA-Z0-9À-ÿ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("email nao pode ser nulo, vazio ou ter mais de 100 caracteres.");
        }
        if(cliente.getTelefone() == null ||
                !cliente.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("telefone nao pode ser nulo, vazio ou ter mais de 11 caracteres.");
        }

        cliente.setNome(cliente.getNome().trim().toLowerCase());
        cliente.setEmail(cliente.getEmail().trim().toLowerCase());
        cliente.setTelefone(cliente.getTelefone().trim().toLowerCase());

        return clienteRepository.cria(cliente);
    }
    public Cliente buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser menor ou igual a zero.");
        }
        Cliente cliente = clienteRepository.buscarOID(id);
        if (cliente == null) {
            throw new IllegalArgumentException("cliente nao encontrado.");
        }
        return cliente;
    }
    public boolean remover(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser menor ou igual a zero.");
        }
        Cliente cliente = clienteRepository.buscarOID(id);
        if (cliente == null) {
            throw new IllegalArgumentException("cliente nao encontrado.");
        }
        return clienteRepository.deleta(id);
    }
    public boolean atualizar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("cliente nao pode ser nulo.");
        }
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("id nao pode ser nulo.");
        }
        if (clienteRepository.buscarOID(cliente.getId()) == null) {
            throw new IllegalArgumentException("O cliente nao existe no banco de dados.");
        }
        if(cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("nome nao pode ser nulo.");
        }
        if(cliente.getEmail() == null || !cliente.getEmail().trim()
                .matches("^[a-zA-Z0-9À-ÿ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("email nao pode ser nulo.");
        }
        if(cliente.getTelefone() == null || cliente.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("telefone nao pode ser nulo.");
        }

        cliente.setNome(cliente.getNome().trim().toLowerCase());
        cliente.setEmail(cliente.getEmail().trim().toLowerCase());
        cliente.setTelefone(cliente.getTelefone().trim());

        return clienteRepository.atualiza(cliente);
    }
}
