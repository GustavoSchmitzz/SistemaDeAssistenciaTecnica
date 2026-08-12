package com.assistencia.controller;

import com.assistencia.dto.PecaComDefeitoListaResponseDTO;
import com.assistencia.dto.PecaComDefeitoResponseDTO;
import com.assistencia.entity.PecaComDefeito;
import com.assistencia.service.PecaComDefeitoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pecas-com-defeito")
public class PecaComDefeitoController {

    private final PecaComDefeitoService pecaComDefeitoService;

    public PecaComDefeitoController(PecaComDefeitoService pecaComDefeitoService) {
        this.pecaComDefeitoService = pecaComDefeitoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PecaComDefeitoResponseDTO> buscaPorId(@PathVariable int id) {
        PecaComDefeito peca = pecaComDefeitoService.buscaPorID(id);
        return ResponseEntity.ok(responseDTO(peca));
    }

    @GetMapping
    public ResponseEntity<PecaComDefeitoListaResponseDTO> listar(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "20") int limite) {

        List<PecaComDefeito> lista = pecaComDefeitoService.listar(pagina, limite);
        List<PecaComDefeitoResponseDTO> listaDTO = lista.stream()
                .map(this::responseDTO)
                .toList();

        PecaComDefeitoListaResponseDTO response = new PecaComDefeitoListaResponseDTO(pagina, limite, listaDTO);
        return ResponseEntity.ok(response);
    }

    private PecaComDefeitoResponseDTO responseDTO(PecaComDefeito peca) {
        return new PecaComDefeitoResponseDTO(
                peca.getId(),
                peca.getTipoPeca(),
                peca.getMarca(),
                peca.getModelo(),
                peca.getProblema(),
                peca.getCliente().getId()
        );
    }
}