package com.assistencia.service;

import com.assistencia.entity.PecaComDefeito;
import com.assistencia.repository.PecaComDefeitoRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PecaComDefeitoService {

    private final PecaComDefeitoRepository pecaComDefeitoRepository;
    public PecaComDefeitoService(PecaComDefeitoRepository pecaComDefeitoRepository) {
        this.pecaComDefeitoRepository = pecaComDefeitoRepository;
    }

    public PecaComDefeito buscaPorID(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O Id nao pode ser menor ou igual a zero.");
        }
        PecaComDefeito pecaComDefeito = pecaComDefeitoRepository.buscaOID(id);
        if (pecaComDefeito == null) {
            throw new IllegalArgumentException("Peca com defeito nao encontrada no banco de dados.");
        }
        return pecaComDefeito;
    }

    public PecaComDefeito adicionaPeca(PecaComDefeito pecaComDefeito) {
        if (pecaComDefeito == null) {
            throw new IllegalArgumentException("Peca com defeito nao pode ser nulo.");
        }
        if (pecaComDefeito.getTipoPeca() == null || pecaComDefeito.getTipoPeca().trim().isEmpty()) {
            throw new IllegalArgumentException("O Tipo de Peca nao pode ser nulo ou vazio.");
        }
        if (pecaComDefeito.getMarca() == null || pecaComDefeito.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("A marca nao pode ser nulo ou vazio.");
        }
        if (pecaComDefeito.getDescricao() == null || pecaComDefeito.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A Descricao nao pode ser nula ou vazia.");
        }
        if (pecaComDefeito.getModelo() == null ||  pecaComDefeito.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo nao pode ser nula ou vazia.");
        }
        if (pecaComDefeito.getCliente() == null || pecaComDefeito.getCliente().getId() <= 0) {
            throw new IllegalArgumentException("O Id nao pode ser nulo ou menor que 1.");
        }

        pecaComDefeito.setTipoPeca(pecaComDefeito.getTipoPeca().trim().toLowerCase());
        pecaComDefeito.setMarca(pecaComDefeito.getMarca().trim().toLowerCase());
        pecaComDefeito.setModelo(pecaComDefeito.getModelo().trim().toLowerCase());
        pecaComDefeito.setDescricao(pecaComDefeito.getDescricao().trim().toLowerCase());

        return pecaComDefeitoRepository.criar(pecaComDefeito);
    }
    public boolean atualizaPecaComDefeito(PecaComDefeito pecaComDefeito) {
        if (pecaComDefeito == null) {
            throw new IllegalArgumentException("Peca com defeito nao pode ser nulo.");
        }
        if (pecaComDefeito.getId() == null) {
            throw new IllegalArgumentException("O Id nao pode ser nulo.");
        }
        if (pecaComDefeitoRepository.buscaOID(pecaComDefeito.getId()) == null) {
            throw new RuntimeException("O produto com defeito nao existe no banco de dados.");
        }
        if (pecaComDefeito.getTipoPeca() == null || pecaComDefeito.getTipoPeca().trim().isEmpty()) {
            throw new IllegalArgumentException("O Tipo de Peca nao pode ser nulo ou vazio.");
        }
        if (pecaComDefeito.getModelo() == null || pecaComDefeito.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("O Modelo nao pode ser nulo ou vazio.");
        }
        if (pecaComDefeito.getMarca() == null || pecaComDefeito.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("A marca nao pode ser nula ou vazia.");
        }
        if (pecaComDefeito.getDescricao() == null ||  pecaComDefeito.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A Descricao nao pode ser nula ou vazia.");
        }
        if (pecaComDefeito.getCliente() == null || pecaComDefeito.getCliente().getId() <= 0) {
            throw new IllegalArgumentException("O Id nao pode ser nulo ou menor que 1.");
        }

        pecaComDefeito.setTipoPeca(pecaComDefeito.getTipoPeca().trim().toLowerCase());
        pecaComDefeito.setMarca(pecaComDefeito.getMarca().trim().toLowerCase());
        pecaComDefeito.setModelo(pecaComDefeito.getModelo().trim().toLowerCase());
        pecaComDefeito.setDescricao(pecaComDefeito.getDescricao().trim().toLowerCase());

        return pecaComDefeitoRepository.atualiza(pecaComDefeito);
    }
    public List<PecaComDefeito> listar(int pagina, int limite) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("pagina nao pode ser igual ou menor a zero.");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("limite nao pode ser igual ou menor a zero");
        }
        int offset = (pagina - 1) * limite;

        return pecaComDefeitoRepository.buscaPecasComDefeitoDaPagina(limite, offset);
    }
}
