package com.assistencia.service;

import com.assistencia.entity.Peca;
import com.assistencia.repository.PecaRepository;

public class PecaService {
    private PecaRepository pecaRepository;
    public PecaService(PecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }
    public Peca cria(Peca peca) {
        if (peca == null) {
            throw new NullPointerException("peca nao pode ser nula.");
        }
        if (peca.getNome() == null || peca.getNome().trim().isEmpty()) {
            throw new NullPointerException("nome nbao pode ser nulo ou vazio.");
        }
        if (peca.getFornecedor() == null) {
            throw new NullPointerException("fornecedor nao pode ser nulo.");
        }
        if (peca.getEstoque() == null || peca.getEstoque() < 0) {
            throw new NullPointerException("estoque nao pode ser nulo nem negativo.");
        }
        if (peca.getValor() <= 0) {
            throw new NullPointerException("valor nao pode ser negativo ou melhor que 0.");
        }

        peca.setNome(peca.getNome().trim().toLowerCase());

        return pecaRepository.cria(peca);
    }
    public Peca buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser igual ou menor que zero");
        }
        Peca peca = pecaRepository.buscaOID(id);
        if (peca == null) {
            throw new IllegalArgumentException("Peca nao encontrada");
        }
        return peca;
    }
    public boolean deletarPeca(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser igual ou menor que zero");
        }
        Peca peca = pecaRepository.buscaOID(id);
        if (peca.getEstoque() > 0) {
            throw new IllegalArgumentException("Nao é possivel deletar uma peça que tem no estoque");
        }
        if (peca == null) {
            throw new IllegalArgumentException("Peca nao encontrada");
        }
        return pecaRepository.deletar(id);
    }
    public Peca adicionarAoEstoque(int id, int quant) {
        if (id <= 0 || quant <= 0) {
            throw new IllegalArgumentException("Id e quantidade nao pode ser igual ou menor que zero");
        }
        Peca peca = pecaRepository.buscaOID(id);
        if (peca == null) {
            throw new IllegalArgumentException("Peca nao encontrada");
        }
        peca.setEstoque(peca.getEstoque() + quant);
        pecaRepository.atualizar(peca);
        return peca;
    }
}
