package com.assistencia.controller;

import com.assistencia.dto.*;
import com.assistencia.entity.Fornecedor;
import com.assistencia.entity.Peca;
import com.assistencia.service.PecaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    private final PecaService pecaService;

    public PecaController(PecaService pecaService) {
        this.pecaService = pecaService;
    }

    @PostMapping
    public ResponseEntity<PecaResponseDTO> cadastro(@RequestBody @Valid PecaCadastroDTO request) {
        Peca peca = new Peca();
        peca.setNome(request.nome());
        peca.setValor(request.valor());
        peca.setEstoque(request.estoque());

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(request.idFornecedor());
        peca.setFornecedor(fornecedor);

        Peca pecaCriada = pecaService.cria(peca);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO(pecaCriada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PecaResponseDTO> buscaPorId(@PathVariable int id) {
        Peca peca = pecaService.buscaPorId(id);
        return ResponseEntity.ok(responseDTO(peca));
    }

    @GetMapping
    public ResponseEntity<PecaListaResponseDTO> listar(
            @Valid PaginacaoDTO paginacao) {

        List<Peca> lista = pecaService.listar(
                paginacao.pagina(),
                paginacao.limite());
        List<PecaResponseDTO> listaDTO = lista.stream()
                .map(this::responseDTO)
                .toList();
        PecaListaResponseDTO response = new PecaListaResponseDTO(
                paginacao.pagina(),
                paginacao.limite(),
                listaDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PecaResponseDTO> adicionarAoEstoque(
            @PathVariable int id, @RequestBody @Valid PecaAdicionarAoEstoqueDTO dto) {

        Peca pecaAtualizada = pecaService.adicionarAoEstoque(id, dto.estoque());
        return ResponseEntity.ok(responseDTO(pecaAtualizada));
    }

    private PecaResponseDTO responseDTO(Peca peca) {
        return new PecaResponseDTO(
                peca.getId(),
                peca.getNome(),
                peca.getValor(),
                peca.getFornecedor().getId(),
                peca.getEstoque()
        );
    }
}