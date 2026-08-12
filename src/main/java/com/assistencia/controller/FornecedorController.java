package com.assistencia.controller;

import com.assistencia.dto.*;
import com.assistencia.entity.Fornecedor;
import com.assistencia.service.FornecedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {
    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping()
    public ResponseEntity<FornecedorResponseDTO> cadastro(
            @RequestBody FornecedorCadastroDTO dto){

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dto.nome());
        fornecedor.setTelefone(dto.telefone());

        Fornecedor novoFornecedor = fornecedorService.cadastrar(fornecedor);

        FornecedorResponseDTO response = responseDTO(novoFornecedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscaPeloId(@PathVariable int id) {

        Fornecedor fornecedor = fornecedorService.buscaPorId(id);
        FornecedorResponseDTO response = responseDTO(fornecedor);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping
    public ResponseEntity<FornecedorListaResponseDTO> listarFornecedores(
            @RequestParam int pagina, @RequestParam int limite) {

        List<Fornecedor> lista = fornecedorService.listar(pagina, limite);
        List<FornecedorResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        FornecedorListaResponseDTO response = new FornecedorListaResponseDTO(pagina, limite, listaDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@RequestBody FornecedorAtualizarDTO dto,
                                          @PathVariable int id) {

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(id);
        fornecedor.setNome(dto.nome());
        fornecedor.setTelefone(dto.telefone());

        fornecedorService.atualizar(fornecedor);

        return ResponseEntity.noContent().build();
    }

    private FornecedorResponseDTO responseDTO(Fornecedor fornecedor) {
        return new FornecedorResponseDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getTelefone()
        );
    }
}