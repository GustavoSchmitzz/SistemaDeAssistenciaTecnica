package com.assistencia.controller;

import com.assistencia.dto.*;
import com.assistencia.entity.Funcionario;
import com.assistencia.service.FuncionarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {
    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscaPorID(@PathVariable int id) {
        Funcionario funcionario = funcionarioService.buscaPorId(id);
        FuncionarioResponseDTO resposta = responseDTO(funcionario);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<FuncionarioListaResponseDTO> listarFuncionarios(
            @Valid PaginacaoDTO paginacao) {

        List<Funcionario> lista = funcionarioService.listar(
                paginacao.pagina(),
                paginacao.limite());
        List<FuncionarioResponseDTO> listaDTO = lista.stream()
                .map(this::responseDTO)
                .toList();
        FuncionarioListaResponseDTO response = new FuncionarioListaResponseDTO(
                paginacao.pagina(),
                paginacao.limite(),
                listaDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<FuncionarioResponseDTO> login(@RequestBody @Valid FuncionarioLoginDTO dto) {
        Funcionario funcionario = funcionarioService.loginFuncionario(dto.email(), dto.senha());

        if (funcionario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        FuncionarioResponseDTO response = responseDTO(funcionario);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> cadastro(@RequestBody @Valid FuncionarioCadastroDTO dto) throws IOException {
        Funcionario dados = new Funcionario();
        dados.setEmail(dto.email());
        dados.setSenha(dto.senha());
        dados.setNome(dto.nome());
        dados.setTelefone(dto.telefone());
        dados.setEspecialidade(dto.especialidade());

        Funcionario novoFuncionario = funcionarioService.cadastraFuncionario(dados);
        FuncionarioResponseDTO response = responseDTO(novoFuncionario);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private FuncionarioResponseDTO responseDTO(Funcionario funcionario) {
        return new FuncionarioResponseDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getTelefone(),
                funcionario.getEspecialidade()
        );
    }
}