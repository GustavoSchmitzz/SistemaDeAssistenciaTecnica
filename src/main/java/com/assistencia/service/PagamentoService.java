package com.assistencia.service;

import com.assistencia.entity.Pagamento;
import com.assistencia.repository.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public Pagamento buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        Pagamento pagamento = pagamentoRepository.findById(id).orElse(null);
        if (pagamento == null) {
            throw new RuntimeException("forma de pagamento nao encontrada.");
        }
        return pagamento;
    }

    public List<Pagamento> listar() {
        return pagamentoRepository.findAll();
    }
}