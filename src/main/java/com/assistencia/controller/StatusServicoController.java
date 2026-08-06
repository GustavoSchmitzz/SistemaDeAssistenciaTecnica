package com.assistencia.controller;

import com.assistencia.dto.StatusServicoListaResponseDTO;
import com.assistencia.dto.StatusServicoResponseDTO;
import com.assistencia.entity.StatusServico;
import com.assistencia.service.StatusServicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class StatusServicoController implements HttpHandler {
    private final StatusServicoService statusServicoService;
    public StatusServicoController(StatusServicoService statusServicoService) {
        this.statusServicoService = statusServicoService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        try {
            switch (requestMethod) {
                case "GET":
                    processarListagem(exchange);
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
    public void processarListagem(HttpExchange exchange) throws IOException {
        List<StatusServico> lista = statusServicoService.listar();
        List<StatusServicoResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        StatusServicoListaResponseDTO response = new StatusServicoListaResponseDTO(listaDTO);
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
    private StatusServicoResponseDTO responseDTO(StatusServico statusServico) {
        return new StatusServicoResponseDTO(
                statusServico.getId(),
                statusServico.getStatus()
        );
    }
}
