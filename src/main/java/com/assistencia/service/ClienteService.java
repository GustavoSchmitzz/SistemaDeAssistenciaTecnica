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
        if(cliente.getNome() == null || cliente.getNome().trim().isEmpty() ||
                cliente.getNome().trim().length() > 100) {
            throw new IllegalArgumentException("nome nao pode ser nulo, vazio ou ter mais de 100 caracteres.");
        }
        if(cliente.getEmail() == null || cliente.getEmail().trim().isEmpty() ||
                cliente.getEmail().trim().length() > 100) {
            throw new IllegalArgumentException("email nao pode ser nulo, vazio ou ter mais de 100 caracteres.");
        }
        if(cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty() ||
                cliente.getTelefone().trim().length() > 11) {
            throw new IllegalArgumentException("telefone nao pode ser nulo, vazio ou ter mais de 11 caracteres.");
        }

        cliente.setNome(cliente.getNome().trim());
        cliente.setEmail(cliente.getEmail().trim());
        cliente.setTelefone(cliente.getTelefone().trim());

        return clienteRepository.cria(cliente);
    }
}
