package com.assistencia.controller;

import com.assistencia.dto.PagamentoListaResponseDTO;
import com.assistencia.dto.PagamentoResponseDTO;
import com.assistencia.entity.Pagamento;
import com.assistencia.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping
    public ResponseEntity<PagamentoListaResponseDTO> listarPagamentos() {
        List<Pagamento> lista = pagamentoService.listar();
        List<PagamentoResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        PagamentoListaResponseDTO response = new PagamentoListaResponseDTO(listaDTO);

        return ResponseEntity.ok(response);
    }
    private PagamentoResponseDTO responseDTO(Pagamento pagamento) {
        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getFormaPagamento()
        );
    }
}