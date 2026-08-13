package com.assistencia.controller;

import com.assistencia.dto.EntregaOrdemDeServicoResponseDTO;
import com.assistencia.dto.EntregaOrdemDeServicoResponseListDTO;
import com.assistencia.dto.PaginacaoDTO;
import com.assistencia.entity.EntregaOrdemDeServico;
import com.assistencia.service.EntregaOrdemDeServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entregasOS")
public class EntregaOrdemDeServicoController {

    private final EntregaOrdemDeServicoService entregaOrdemDeServicoService;

    public EntregaOrdemDeServicoController(EntregaOrdemDeServicoService entregaOrdemDeServicoService) {
        this.entregaOrdemDeServicoService = entregaOrdemDeServicoService;
    }

    @PostMapping("/{idOS}")
    public ResponseEntity<EntregaOrdemDeServicoResponseDTO> entregaOS(@PathVariable int idOS) {
        EntregaOrdemDeServico entregaServico = entregaOrdemDeServicoService.entregaAOrdemDeServico(idOS);
        EntregaOrdemDeServicoResponseDTO response = responseDTO(entregaServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> reverterEntrega(@PathVariable int id) {
        boolean reverteu = entregaOrdemDeServicoService.reverteAEntrega(id);
        if (reverteu) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<EntregaOrdemDeServicoResponseListDTO> listar(
            @Valid PaginacaoDTO paginacao) {

        List<EntregaOrdemDeServico> entregaList = entregaOrdemDeServicoService.listar(
                paginacao.pagina(),
                paginacao.limite());
        List<EntregaOrdemDeServicoResponseDTO> dtoList = entregaList.stream()
                .map(this::responseDTO)
                .toList();
        EntregaOrdemDeServicoResponseListDTO response = new EntregaOrdemDeServicoResponseListDTO(
                paginacao.pagina(),
                paginacao.limite(),
                dtoList);
        return ResponseEntity.ok(response);
    }

    private EntregaOrdemDeServicoResponseDTO responseDTO(EntregaOrdemDeServico entregaOS) {
        return new EntregaOrdemDeServicoResponseDTO(
                entregaOS.getId(),
                entregaOS.getDataEntrega(),
                entregaOS.getOrdemDeServico().getId()
        );
    }
}