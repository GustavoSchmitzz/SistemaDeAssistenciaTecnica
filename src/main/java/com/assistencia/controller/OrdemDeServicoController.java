package com.assistencia.controller;

import com.assistencia.dto.*;
import com.assistencia.entity.*;
import com.assistencia.service.OrdemDeServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ordens-de-servico")
public class OrdemDeServicoController {
    private final OrdemDeServicoService ordemDeServicoService;

    public OrdemDeServicoController(OrdemDeServicoService ordemDeServicoService) {
        this.ordemDeServicoService = ordemDeServicoService;
    }

    @PostMapping
    public ResponseEntity<OrdemDeServicoResponseDTO> cadastro(
            @RequestBody @Valid OrdemDeServicoCadastraDTO dto) {

        OrdemDeServico os = new OrdemDeServico();
        os.setValorServico(dto.valor());

        StatusServico statusServico = new StatusServico();
        statusServico.setId(dto.idStatusServico());
        os.setStatusServico(statusServico);

        Garantia garantia = new Garantia();
        garantia.setId(dto.idGarantia());
        os.setGarantia(garantia);

        PecaComDefeito peca = new PecaComDefeito();
        peca.setId(dto.idPecaComDefeito());
        os.setPeca(peca);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(dto.idFuncionario());
        os.setFuncionario(funcionario);

        OrdemDeServico novaOS = ordemDeServicoService.abrirOrdem(os);
        OrdemDeServicoResponseDTO response = responseDTO(novaOS);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServicoResponseDTO> buscaPeloId(@PathVariable int id) {
        OrdemDeServico os = ordemDeServicoService.buscaOId(id);
        OrdemDeServicoResponseDTO resposta = responseDTO(os);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<OrdemDeServicoListaResponseDTO> listarOS(
            @Valid PaginacaoDTO paginacao) {

        List<OrdemDeServico> lista = ordemDeServicoService.listar(
                paginacao.pagina(),
                paginacao.limite());
        List<OrdemDeServicoResponseDTO> listaDTO = lista.stream()
                .map(this::responseDTO)
                .toList();
        OrdemDeServicoListaResponseDTO response = new OrdemDeServicoListaResponseDTO(
                paginacao.pagina(),
                paginacao.limite(),
                listaDTO);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemDeServicoResponseDTO> atualizar(
            @RequestBody @Valid OrdemDeServicoAtualizaDTO dto, @PathVariable int id) {

        OrdemDeServico os = new OrdemDeServico();
        os.setId(id);
        os.setValorServico(dto.valor());

        StatusServico status = new StatusServico();
        status.setId(dto.idStatusServico());
        os.setStatusServico(status);

        Garantia garantia = new Garantia();
        garantia.setId(dto.idGarantia());
        os.setGarantia(garantia);

        Pagamento pagamento = new Pagamento();
        pagamento.setId(dto.idPagamento());
        os.setPagamento(pagamento);

        boolean atualizou = ordemDeServicoService.atualiza(os);

        if (atualizou) {
            OrdemDeServicoResponseDTO resposta = responseDTO(os);
            return ResponseEntity.ok(resposta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private OrdemDeServicoResponseDTO responseDTO(OrdemDeServico os) {
        return new OrdemDeServicoResponseDTO(
                os.getId(),
                os.getValorServico(),
                os.getDataInicio(),
                os.getFuncionario().getId(),
                os.getGarantia().getId(),
                os.getPeca().getId(),
                os.getPagamento().getId(),
                os.getStatusServico().getId()
        );
    }
}