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
        if (cliente == null) {
            throw new IllegalArgumentException("cliente nao pode ser nulo.");
        }
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("nome nao pode ser nulo, vazio ou ter mais de 100 caracteres.");
        }
        if (cliente.getEmail() == null || !cliente.getEmail().trim()
                .matches("^[a-zA-Z0-9 ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("email nao pode ser nulo, vazio ou ter mais de 100 caracteres.");
        }
        if (cliente.getTelefone() == null ||
                !cliente.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("telefone nao pode ser nulo, vazio ou ter mais de 11 caracteres.");
        }
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
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("cliente nao encontrado.");
        }
        clienteRepository.deleteById(id);
        return true;
    }

    public boolean atualizar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("cliente nao pode ser nulo.");
        }
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("id nao pode ser nulo.");
        }
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("nome nao pode ser nulo.");
        }
        if (cliente.getEmail() == null || !cliente.getEmail().trim()
                .matches("^[a-zA-Z0-9 ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("email nao pode ser nulo.");
        }
        if (cliente.getTelefone() == null || !cliente.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("telefone nao pode ser nulo.");
        }
        cliente.setNome(cliente.getNome().trim().toLowerCase());
        cliente.setEmail(cliente.getEmail().trim().toLowerCase());
        cliente.setTelefone(cliente.getTelefone().trim());

        clienteRepository.save(cliente);
        return true;
    }

    public List<Cliente> listar(int pagina, int limite) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("pagina nao pode ser igual ou menor a zero.");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("limite nao pode ser igual ou menor a zero");
        }
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return clienteRepository.findAll(pageable).getContent();
    }
}