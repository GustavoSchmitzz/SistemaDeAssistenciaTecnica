package com.assistencia.controller;

import com.assistencia.dto.*;
import com.assistencia.entity.Cliente;
import com.assistencia.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<ClienteListaResponseDTO> listarClientes(@Valid PaginacaoDTO paginacao) {
        List<Cliente> listaClientes = clienteService.listar(
                paginacao.pagina(),
                paginacao.limite());
        List<ClienteResponseDTO> listaDTO = listaClientes
                .stream()
                .map(this::responseDTO)
                .toList();
        ClienteListaResponseDTO listaResponseDTO = new ClienteListaResponseDTO(
                paginacao.pagina(),
                paginacao.limite(), listaDTO);

        return ResponseEntity.ok(listaResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastroCliente(
            @RequestBody @Valid ClienteCadastroDTO dto) {

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());

        Cliente salvo = clienteService.cadastrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable int id,
                                          @RequestBody @Valid ClienteAtualizarInfoDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());

        clienteService.atualizar(cliente);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscaPorId(@PathVariable int id) {
        Cliente cliente = clienteService.buscaPorId(id);
        ClienteResponseDTO resposta = responseDTO(cliente);
        return ResponseEntity.ok(resposta);
    }

    private ClienteResponseDTO responseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone()
        );
    }
}