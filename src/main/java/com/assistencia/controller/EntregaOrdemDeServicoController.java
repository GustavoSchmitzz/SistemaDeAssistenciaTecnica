package com.assistencia.controller;

import com.assistencia.dto.EntregaOrdemDeServicoCriarDTO;
import com.assistencia.dto.EntregaOrdemDeServicoResponseDTO;
import com.assistencia.entity.EntregaOrdemDeServico;
import com.assistencia.service.EntregaOrdemDeServicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EntregaOrdemDeServicoController implements HttpHandler {
    private final EntregaOrdemDeServicoService entregaOrdemDeServicoService;
    public EntregaOrdemDeServicoController(EntregaOrdemDeServicoService entregaOrdemDeServicoService) {
        this.entregaOrdemDeServicoService = entregaOrdemDeServicoService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }
    public void processarEntregaOS(HttpExchange exchange, int idOS) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        EntregaOrdemDeServicoCriarDTO dto = new ObjectMapper()
                .readValue(requestBody, EntregaOrdemDeServicoCriarDTO.class);

        EntregaOrdemDeServico entregaOS = new EntregaOrdemDeServico();
        entregaOS.setDataEntrega(dto.dataEntrega());

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
                entregaOS.getDataEntrega()
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
}
