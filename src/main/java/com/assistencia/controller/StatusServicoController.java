package com.assistencia.controller;

import com.assistencia.dto.StatusServicoListaResponseDTO;
import com.assistencia.dto.StatusServicoResponseDTO;
import com.assistencia.entity.StatusServico;
import com.assistencia.service.StatusServicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status-servico")
public class StatusServicoController {

    private final StatusServicoService statusServicoService;

    public StatusServicoController(StatusServicoService statusServicoService) {
        this.statusServicoService = statusServicoService;
    }

    @GetMapping
    public ResponseEntity<StatusServicoListaResponseDTO> listarStatusServico() {
        List<StatusServico> lista = statusServicoService.listar();
        List<StatusServicoResponseDTO> listaDTO = lista.stream()
                .map(this::responseDTO)
                .toList();

        StatusServicoListaResponseDTO response = new StatusServicoListaResponseDTO(listaDTO);
        return ResponseEntity.ok(response);
    }

    private StatusServicoResponseDTO responseDTO(StatusServico statusServico) {
        return new StatusServicoResponseDTO(
                statusServico.getId(),
                statusServico.getStatus()
        );
    }
}