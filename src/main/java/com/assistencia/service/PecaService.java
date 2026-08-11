package com.assistencia.service;

import com.assistencia.entity.Peca;
import com.assistencia.repository.PecaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PecaService {

    private final PecaRepository pecaRepository;

    public PecaService(PecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    public Peca cria(Peca peca) {
        if (peca == null) {
            throw new NullPointerException("peca nao pode ser nula.");
        }
        if (peca.getNome() == null || peca.getNome().trim().isEmpty()) {
            throw new NullPointerException("nome nao pode ser nulo ou vazio.");
        }
        if (peca.getFornecedor() == null) {
            throw new NullPointerException("fornecedor nao pode ser nulo.");
        }
        if (peca.getEstoque() == null || peca.getEstoque() < 0) {
            throw new NullPointerException("estoque nao pode ser nulo nem negativo.");
        }
        if (peca.getValor() <= 0) {
            throw new NullPointerException("valor nao pode ser negativo ou menor que 0.");
        }
        peca.setNome(peca.getNome().trim().toLowerCase());

        return pecaRepository.save(peca);
    }

    public Peca buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser igual ou menor que zero");
        }
        Peca peca = pecaRepository.findById(id).orElse(null);
        if (peca == null) {
            throw new IllegalArgumentException("Peca nao encontrada");
        }
        return peca;
    }

    public boolean deletarPeca(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser igual ou menor que zero");
        }
        Peca peca = pecaRepository.findById(id).orElse(null);
        if (peca == null) {
            throw new IllegalArgumentException("Peca nao encontrada");
        }
        if (peca.getEstoque() > 0) {
            throw new IllegalArgumentException("Nao e possivel deletar uma peca que tem no estoque");
        }

        pecaRepository.deleteById(id);
        return true;
    }

    public Peca adicionarAoEstoque(int id, int quant) {
        if (id <= 0 || quant <= 0) {
            throw new IllegalArgumentException("Id e quantidade nao pode ser igual ou menor que zero");
        }
        Peca peca = pecaRepository.findById(id).orElse(null);
        if (peca == null) {
            throw new IllegalArgumentException("Peca nao encontrada");
        }

        peca.setEstoque(peca.getEstoque() + quant);
        pecaRepository.save(peca);
        return peca;
    }

    public List<Peca> listar(int pagina, int limite) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("pagina nao pode ser igual ou menor a zero.");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("limite nao pode ser igual ou menor a zero");
        }

        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return pecaRepository.findAll(pageable).getContent();
    }
}