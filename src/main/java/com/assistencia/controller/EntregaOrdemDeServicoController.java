package com.assistencia.controller;

import com.assistencia.dto.EntregaOrdemDeServicoCriarDTO;
import com.assistencia.dto.EntregaOrdemDeServicoResponseDTO;
import com.assistencia.dto.EntregaOrdemDeServicoResponseListDTO;
import com.assistencia.entity.EntregaOrdemDeServico;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.service.EntregaOrdemDeServicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class EntregaOrdemDeServicoController implements HttpHandler {
    private final EntregaOrdemDeServicoService entregaOrdemDeServicoService;
    public EntregaOrdemDeServicoController(EntregaOrdemDeServicoService entregaOrdemDeServicoService) {
        this.entregaOrdemDeServicoService = entregaOrdemDeServicoService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String requestQuery = exchange.getRequestURI().getQuery();
        //parametro[0] é a pagina da request
        //parametro[1] é o limite de dados a serem buscados
        int[] parametros = getQuery(requestQuery);
        Integer id = extrairIdDaURL(requestPath);

        try {
            switch (requestMethod) {
                case "DELETE":
                    if(id == null) {throw new IllegalArgumentException();}
                    processarReversaoOS(exchange, id);
                    break;
                case "POST":
                    if(id == null) {throw new IllegalArgumentException();}
                    processarEntregaOS(exchange, id);
                    break;
                case "GET":
                    processarListagem(exchange, parametros[0], parametros[1] );
                default:
                    enviarResposta(exchange, 405, "{\"erro\": \"metodo nao permitido.\"}");
            }
        } catch (IllegalArgumentException e) {
            enviarResposta(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            enviarResposta(exchange, 500, "{\"erro\": \"erro do servidor: " + e.getMessage() + "\"}");
        }
    }
    public void processarEntregaOS(HttpExchange exchange, int idOS) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        EntregaOrdemDeServicoCriarDTO dto = new ObjectMapper()
                .readValue(requestBody, EntregaOrdemDeServicoCriarDTO.class);

        EntregaOrdemDeServico entregaOS = new EntregaOrdemDeServico();
        entregaOS.setDataEntrega(dto.dataEntrega());

        OrdemDeServico ordemDeServico = new OrdemDeServico();
        ordemDeServico.setId(idOS);
        entregaOS.setOrdemDeServico(ordemDeServico);

        EntregaOrdemDeServico entregaServico = entregaOrdemDeServicoService.entregaAOrdemDeServico(idOS);

        EntregaOrdemDeServicoResponseDTO reponseDTO = responseDTO(entregaServico);
        String json = new ObjectMapper().writeValueAsString(reponseDTO);

        enviarResposta(exchange, 201, json);
    }
    public void processarReversaoOS(HttpExchange exchange, int id) throws IOException {
        boolean resposta = entregaOrdemDeServicoService.reverteAEntrega(id);
        if (resposta) {
            enviarResposta(exchange, 200, "\"mensagem\": \"deleção feita com sucesso\"");
        } else {
            enviarResposta(exchange, 404, "\"mensagem\": \"entrega nao encontrada.\"");
        }
    }
    public void processarListagem(HttpExchange exchange, int pagina, int limite) throws IOException {
        List<EntregaOrdemDeServico> entregaList = entregaOrdemDeServicoService.listar(pagina, limite);

        List<EntregaOrdemDeServicoResponseDTO> dtoList = entregaList.stream().map(entrega ->
                (new EntregaOrdemDeServicoResponseDTO(entrega.getId(), entrega.getDataEntrega(), entrega.getOrdemDeServico().getId())))
                .toList();

        EntregaOrdemDeServicoResponseListDTO lista = new EntregaOrdemDeServicoResponseListDTO(pagina, limite, dtoList);

        String json = new ObjectMapper().writeValueAsString(lista);
        enviarResposta(exchange, 200, json);
    }
    private void enviarResposta(HttpExchange exchange, int status, String json) throws IOException {
        exchange.sendResponseHeaders(status, json.length());
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
    private EntregaOrdemDeServicoResponseDTO responseDTO(EntregaOrdemDeServico entregaOS) {
        return new EntregaOrdemDeServicoResponseDTO(
                entregaOS.getId(),
                entregaOS.getDataEntrega(),
                entregaOS.getOrdemDeServico().getId()
        );
    }
    private Integer extrairIdDaURL(String path) {
        String[] caminho = path.split("/");
        if (caminho.length > 2) {
            try {
                return Integer.parseInt(caminho[2]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    private int[] getQuery(String query) {
        int pagina = 1;
        int limite = 20;

        if (query == null || query.trim().isEmpty()) {
            return new int[] {pagina, limite};
        }

        String[] params = query.split("&");
        for(String param : params) {
            String[] par = param.split("=");
            if(par.length == 2) {
                if(par[0].equals("pagina")) {
                    pagina = Integer.parseInt(par[1]);
                } else if(par[0].equals("limite")) {
                    limite = Integer.parseInt(par[1]);
                }
            }
        }
        return new int[] {pagina, limite};
    }
}
