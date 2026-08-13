package com.assistencia.service;

import com.assistencia.entity.Cliente;
import com.assistencia.repository.ClienteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(Cliente cliente) {

        cliente.setNome(cliente.getNome().trim().toLowerCase());
        cliente.setEmail(cliente.getEmail().trim().toLowerCase());
        cliente.setTelefone(cliente.getTelefone().trim().toLowerCase());

        return clienteRepository.save(cliente);
    }

    public Cliente buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser menor ou igual a zero.");
        }
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            throw new IllegalArgumentException("cliente nao encontrado.");
        }
        return cliente;
    }

    public boolean remover(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser menor ou igual a zero.");
        }
        clienteRepository.deleteById(id);
        return true;
    }

    public boolean atualizar(Cliente cliente) {

        cliente.setNome(cliente.getNome().trim().toLowerCase());
        cliente.setEmail(cliente.getEmail().trim().toLowerCase());
        cliente.setTelefone(cliente.getTelefone().trim());

        clienteRepository.save(cliente);
        return true;
    }

    public List<Cliente> listar(int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return clienteRepository.findAll(pageable).getContent();
    }
}