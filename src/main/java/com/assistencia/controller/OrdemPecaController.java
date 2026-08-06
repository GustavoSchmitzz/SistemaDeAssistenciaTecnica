package com.assistencia.controller;

import com.assistencia.dto.OrdemPecaListaResponseDTO;
import com.assistencia.dto.OrdemPecaResponseDTO;
import com.assistencia.entity.OrdemPeca;
import com.assistencia.service.OrdemPecaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class OrdemPecaController implements HttpHandler {
    private final OrdemPecaService ordemPecaService;
    public OrdemPecaController(OrdemPecaService ordemPecaService) {
        this.ordemPecaService = ordemPecaService;
    }

    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestQuery = exchange.getRequestURI().getQuery();
        int[] parametros = getQuery(requestQuery);
        try {
            switch (requestMethod) {
                case "GET":
                    processarListagem(exchange, parametros[0], parametros[1]);
                    break;
                default:
                    enviarResposta(exchange, 405, "{\"erro\": \"metodo nao permitido\"}");
            }
        }catch(IllegalArgumentException e) {
            enviarResposta(exchange, 400, "{\"erro\": \"" + e.getMessage()+ "\"}");
        }catch (Exception e) {
            enviarResposta(exchange, 500, "{\"erro\": \"erro do servidor\"}");
        }
    }
    public void processarListagem(HttpExchange exchange, int pagina, int limite) throws IOException {
        List<OrdemPeca> lista = ordemPecaService.listar(pagina, limite);
        List<OrdemPecaResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        OrdemPecaListaResponseDTO response = new OrdemPecaListaResponseDTO(pagina, limite, listaDTO);
        String json = new ObjectMapper().writeValueAsString(response);

        enviarResposta(exchange, 200, json);
    }

    private void enviarResposta(HttpExchange exchange, int codigo, String json) throws IOException {
        exchange.sendResponseHeaders(codigo, json.length());
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
    private OrdemPecaResponseDTO responseDTO(OrdemPeca ordemPeca) {
        return new OrdemPecaResponseDTO(
                ordemPeca.getId(),
                ordemPeca.getQuantidade(),
                ordemPeca.getOrdemDeServico().getId(),
                ordemPeca.getPeca().getId()
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
        int limite = 20;
        int pagina = 1;
        if(query == null || query.isEmpty()) {
            return new int[] {pagina, limite};
        }

        String[] params = query.split("&");

        for(String param : params) {
            String[] par = param.split("=");
            if(par.length == 2) {
                String chave = par[0];
                String valor = par[1];

                if(chave.equals("pagina")) {
                    pagina = Integer.valueOf(valor);
                } else if(chave.equals("limite")) {
                    limite = Integer.valueOf(valor);
                }
            }
        }
        return new int[] {pagina, limite};
    }
}
