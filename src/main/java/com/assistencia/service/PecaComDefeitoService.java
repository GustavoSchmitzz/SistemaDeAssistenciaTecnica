package com.assistencia.service;

import com.assistencia.entity.PecaComDefeito;
import com.assistencia.repository.PecaComDefeitoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PecaComDefeitoService {

    private final PecaComDefeitoRepository pecaComDefeitoRepository;

    public PecaComDefeitoService(PecaComDefeitoRepository pecaComDefeitoRepository) {
        this.pecaComDefeitoRepository = pecaComDefeitoRepository;
    }

    public PecaComDefeito buscaPorID(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O Id nao pode ser menor ou igual a zero.");
        }
        PecaComDefeito pecaComDefeito = pecaComDefeitoRepository.findById(id).orElse(null);
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
        if (pecaComDefeito.getProblema() == null || pecaComDefeito.getProblema().trim().isEmpty()) {
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
        pecaComDefeito.setProblema(pecaComDefeito.getProblema().trim().toLowerCase());

        return pecaComDefeitoRepository.save(pecaComDefeito);
    }

    public boolean atualizaPecaComDefeito(PecaComDefeito pecaComDefeito) {
        if (pecaComDefeito == null) {
            throw new IllegalArgumentException("Peca com defeito nao pode ser nulo.");
        }
        if (pecaComDefeito.getId() == null) {
            throw new IllegalArgumentException("O Id nao pode ser nulo.");
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
        if (pecaComDefeito.getProblema() == null ||  pecaComDefeito.getProblema().trim().isEmpty()) {
            throw new IllegalArgumentException("A Descricao nao pode ser nula ou vazia.");
        }
        if (pecaComDefeito.getCliente() == null || pecaComDefeito.getCliente().getId() <= 0) {
            throw new IllegalArgumentException("O Id nao pode ser nulo ou menor que 1.");
        }

        pecaComDefeito.setTipoPeca(pecaComDefeito.getTipoPeca().trim().toLowerCase());
        pecaComDefeito.setMarca(pecaComDefeito.getMarca().trim().toLowerCase());
        pecaComDefeito.setModelo(pecaComDefeito.getModelo().trim().toLowerCase());
        pecaComDefeito.setProblema(pecaComDefeito.getProblema().trim().toLowerCase());

        pecaComDefeitoRepository.save(pecaComDefeito);
        return true;
    }

    public List<PecaComDefeito> listar(int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return pecaComDefeitoRepository.findAll(pageable).getContent();
    }
}