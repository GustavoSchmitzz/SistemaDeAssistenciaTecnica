package com.assistencia.controller;

import com.assistencia.dto.ClienteAtualizarInfoDTO;
import com.assistencia.dto.ClienteCadastroDTO;
import com.assistencia.dto.ClienteResponseDTO;
import com.assistencia.entity.Cliente;
import com.assistencia.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClienteController  implements HttpHandler {
    private final ClienteService clienteService;
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        Integer id = extrairIdDaURL(requestPath);
        try {
            switch (requestMethod) {
                case "POST":
                    processarCadastroCliente(exchange);
                    break;
                case "PUT":
                    if (id == null) throw new IllegalArgumentException("id nao informado.");
                    processarAtualizacao(exchange, id);
                    break;
                case "GET":
                    if (id == null) throw new IllegalArgumentException("id nao informado.");
                    processaBuscaPorId(exchange, id);
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

    private void processarCadastroCliente(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        ClienteCadastroDTO dto = new ObjectMapper().readValue(requestBody,
                ClienteCadastroDTO.class);

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());

        Cliente salvo = clienteService.cadastrar(cliente);
        ClienteResponseDTO resposta = responseDTO(salvo);

        String json = new ObjectMapper().writeValueAsString(resposta);
        enviarResposta(exchange, 201, json);
    }
    private void processarAtualizacao(HttpExchange exchange, int id) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        ClienteAtualizarInfoDTO dto = new ObjectMapper().readValue(requestBody,
                ClienteAtualizarInfoDTO.class);

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());

        boolean salvo = clienteService.atualizar(cliente);
        if (salvo) {
            enviarResposta(exchange, 200, "{\"mensagem\":\"Cliente atualizado com sucesso\"}");
        } else {
            enviarResposta(exchange,404, "{\"erro\": \"Falha. Cliente nao encontrado.\" }");
        }
    }
    private void processaBuscaPorId(HttpExchange exchange, int id) throws IOException {
        Cliente cliente = clienteService.buscaPorId(id);

        ClienteResponseDTO resposta = responseDTO(cliente);

        String json = new ObjectMapper().writeValueAsString(resposta);
        enviarResposta(exchange, 200, json);
    }
    private ClienteResponseDTO responseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone()
        );
    }
    private void enviarResposta(HttpExchange exchange, int codigoHTTP, String respostaJson) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(codigoHTTP, respostaJson.length());
        OutputStream os = exchange.getResponseBody();
        os.write(respostaJson.getBytes());
        os.close();
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
