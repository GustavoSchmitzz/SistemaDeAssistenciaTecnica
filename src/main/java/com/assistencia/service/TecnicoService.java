package com.assistencia.service;

import com.assistencia.entity.Tecnico;
import com.assistencia.repository.TecnicoRepository;

public class TecnicoService {
    private final TecnicoRepository tecnicoRepository;
    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public Tecnico buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        Tecnico tecnico = tecnicoRepository.buscaOID(id);
        if (tecnico == null) {
            throw new IllegalArgumentException("Tecnico nao encontrado.");
        }
        return tecnico;
    }
    public Tecnico cadastraTecnico(Tecnico tecnico) {
        if (tecnico == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (tecnico.getNome() == null || tecnico.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        if (tecnico.getEspecialidade() == null || tecnico.getEspecialidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade nao pode ser vazio.");
        }
        if (tecnico.getTelefone() == null || !tecnico.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("Telefone nao pode ser vazio ou com mais de 11 digitos.");
        }

        tecnico.setNome(tecnico.getNome().trim().toLowerCase());
        tecnico.setEspecialidade(tecnico.getEspecialidade().trim().toLowerCase());

        return tecnicoRepository.cria(tecnico);
    }
    public boolean deletaTecnico(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        Tecnico tecnico = tecnicoRepository.buscaOID(id);
        if (tecnico == null) {
            throw new IllegalArgumentException("Tecnico nao encontrado.");
        }

        return tecnicoRepository.deleta(id);
    }
    public boolean atualizaTecnico(Tecnico tecnico) {
        if (tecnico == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (tecnicoRepository.buscaOID(tecnico.getId()) == null) {
            throw new RuntimeException("Tecnico nao encontrado.");
        }
        if (tecnico.getNome() == null || tecnico.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        if (tecnico.getEspecialidade() == null || tecnico.getEspecialidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade nao pode ser vazio.");
        }
        if (tecnico.getTelefone() == null || !tecnico.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("Telefone nao pode ser vazio ou com mais de 11 digitos.");
        }

        tecnico.setNome(tecnico.getNome().trim().toLowerCase());
        tecnico.setEspecialidade(tecnico.getEspecialidade().trim().toLowerCase());
        tecnico.setTelefone(tecnico.getTelefone().trim());

        return tecnicoRepository.atualiza(tecnico);
    }
}
