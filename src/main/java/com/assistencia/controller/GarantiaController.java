package com.assistencia.controller;
import com.assistencia.dto.GarantiaCadastroDTO;
import com.assistencia.dto.GarantiaListaResponseDTO;
import com.assistencia.dto.GarantiaResponseDTO;
import com.assistencia.entity.Garantia;
import com.assistencia.service.GarantiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garantias")
public class GarantiaController {
    private final GarantiaService garantiaService;

    public GarantiaController(GarantiaService garantiaService) {
        this.garantiaService = garantiaService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable int id) {
        boolean deletou = garantiaService.removerGarantia(id);
        if (deletou) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GarantiaResponseDTO> cadastro(@RequestBody GarantiaCadastroDTO dto) {
        Garantia garantia = new Garantia();
        garantia.setDiasDeGarantia(dto.diasDeGarantia());

        Garantia novaGarantia = garantiaService.adicionaGarantia(garantia);

        GarantiaResponseDTO responseDTO = responseDTO(novaGarantia);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarantiaResponseDTO> buscaPeloId(@PathVariable int id) {
        Garantia garantia = garantiaService.buscaPorId(id);
        GarantiaResponseDTO response =
                new GarantiaResponseDTO(garantia.getId(), garantia.getDiasDeGarantia());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GarantiaListaResponseDTO> listarGarantias() {
        List<Garantia> lista = garantiaService.listar();
        List<GarantiaResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        GarantiaListaResponseDTO response = new GarantiaListaResponseDTO(listaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private GarantiaResponseDTO responseDTO(Garantia garantia) {
        return new GarantiaResponseDTO(
                garantia.getId(),
                garantia.getDiasDeGarantia()
        );
    }
}