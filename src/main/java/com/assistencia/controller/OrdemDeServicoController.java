package com.assistencia.controller;

import com.assistencia.dto.OrdemDeServicoAtualizaDTO;
import com.assistencia.dto.OrdemDeServicoListaResponseDTO;
import com.assistencia.dto.OrdemDeServicoResponseDTO;
import com.assistencia.dto.OrdemDeServicoServiceCadastraDTO;
import com.assistencia.entity.*;
import com.assistencia.service.OrdemDeServicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class OrdemDeServicoController implements HttpHandler {
    private final OrdemDeServicoService ordemDeServicoService;
    public OrdemDeServicoController(OrdemDeServicoService ordemDeServicoService) {
        this.ordemDeServicoService = ordemDeServicoService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String requestQuery = exchange.getRequestURI().getQuery();
        int[] parametros = getQuery(requestQuery);
        Integer id = getIdDaURL(requestPath);

        try {
            switch (requestMethod) {
                case "POST":
                    processarCadastro(exchange);
                    break;
                case "GET":
                    if (id == null) {
                        processarListagem(exchange, parametros[0], parametros[1] );
                    } else {
                        processarBuscaPeloId(exchange, id);
                    }
                    break;
                case "PUT":
                    if (id == null) {throw new IllegalArgumentException("id nao pode ser nulo.");}
                    processarAtualizacao(exchange, id);
                    break;
                default:
                    enviarResposta(exchange, 405, "{\"erro\": \"metodo nao permitido.\"}");
                    break;
            }
        }catch (IllegalArgumentException e) {
            enviarResposta(exchange, 400, "{\"erro\": \""+e.getMessage()+"\"}");
        }catch (Exception e) {
            enviarResposta(exchange, 500, "{\"erro\": \"erro interno do servidor: "+e.getMessage()+"\"}");
        }
    }
    public void processarCadastro(HttpExchange exchange) throws IOException {
        InputStream request = exchange.getRequestBody();
        OrdemDeServicoServiceCadastraDTO dto = new ObjectMapper().readValue(request, OrdemDeServicoServiceCadastraDTO.class);

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
        String json = new ObjectMapper().writeValueAsString(novaOS);
        enviarResposta(exchange, 201, json);
    }
    public void processarBuscaPeloId(HttpExchange exchange, int id) throws IOException {
        OrdemDeServico os = ordemDeServicoService.buscaOId(id);
        OrdemDeServicoResponseDTO resposta = responseDTO(os);
        String json = new ObjectMapper().writeValueAsString(resposta);
        enviarResposta(exchange, 200, json);
    }
    public void processarListagem(HttpExchange exchange, int pagina, int limite) throws IOException {
        List<OrdemDeServico> lista = ordemDeServicoService.listar(pagina, limite);
        List<OrdemDeServicoResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        OrdemDeServicoListaResponseDTO response = new OrdemDeServicoListaResponseDTO(pagina, limite, listaDTO);
        String json = new ObjectMapper().writeValueAsString(response);

        enviarResposta(exchange, 200, json);
    }
    public void processarAtualizacao(HttpExchange exchange, int id) throws IOException {
        InputStream request = exchange.getRequestBody();
        OrdemDeServicoAtualizaDTO dto = new ObjectMapper().readValue(request, OrdemDeServicoAtualizaDTO.class);

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
            String json = new ObjectMapper().writeValueAsString(resposta);
            enviarResposta(exchange, 200, json);
        }  else {
            enviarResposta(exchange, 400, "{\"erro\": \"campo vazio ou registro existente.\"}");
        }
    }
    private OrdemDeServicoResponseDTO responseDTO(OrdemDeServico os){
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
    private void enviarResposta(HttpExchange exchange, int codigoHttp, String json) throws IOException {
        exchange.sendResponseHeaders(codigoHttp, json.length());
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
    private Integer getIdDaURL(String url) {
        String[] path = url.split("/");
        if (path.length > 2) {
            try {
                return Integer.parseInt(path[2]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    private int[] getQuery(String query) {
        int pagina = 1;
        int limite = 20;

        if(query == null || query.trim().isEmpty()) {
            return new int[] {pagina, limite};
        }

        String[] params = query.split("&");
        for(String param : params) {
            String[] par = param.split("=");
            if(par.length == 2) {
                if(par[0].equals("pagina")) {
                    pagina = Integer.valueOf(par[1]);
                }else if(par[0].equals("limite")) {
                    limite = Integer.valueOf(par[1]);
                }
            }
        }
        return new int[] {pagina, limite};
    }
}