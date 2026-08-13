package com.assistencia.controller;

import com.assistencia.dto.OrdemPecaListaResponseDTO;
import com.assistencia.dto.OrdemPecaResponseDTO;
import com.assistencia.dto.PaginacaoDTO;
import com.assistencia.entity.OrdemPeca;
import com.assistencia.service.OrdemPecaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ordens-peca")
public class OrdemPecaController {
    private final OrdemPecaService ordemPecaService;

    public OrdemPecaController(OrdemPecaService ordemPecaService) {
        this.ordemPecaService = ordemPecaService;
    }

    @GetMapping
    public ResponseEntity<OrdemPecaListaResponseDTO> listarOS(@Valid PaginacaoDTO paginacao) {
        List<OrdemPeca> lista = ordemPecaService.listar(
                paginacao.pagina(),
                paginacao.limite());
        List<OrdemPecaResponseDTO> listaDTO = lista.stream()
                .map(this::responseDTO)
                .toList();

        OrdemPecaListaResponseDTO response = new OrdemPecaListaResponseDTO(
                paginacao.pagina(),
                paginacao.limite(),
                listaDTO);

        return ResponseEntity.ok(response);
    }

    private OrdemPecaResponseDTO responseDTO(OrdemPeca ordemPeca) {
        return new OrdemPecaResponseDTO(
                ordemPeca.getId(),
                ordemPeca.getQuantidade(),
                ordemPeca.getOrdemDeServico().getId(),
                ordemPeca.getPeca().getId()
        );
    }
}
