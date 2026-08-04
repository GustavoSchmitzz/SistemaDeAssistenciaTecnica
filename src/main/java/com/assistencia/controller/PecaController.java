package com.assistencia.controller;

import com.assistencia.dto.PecaAdicionarAoEstoqueDTO;
import com.assistencia.dto.PecaCadastroDTO;
import com.assistencia.dto.PecaResponseDTO;
import com.assistencia.entity.Fornecedor;
import com.assistencia.entity.Peca;
import com.assistencia.service.PecaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PecaController implements HttpHandler {
    private final PecaService pecaService;
    public PecaController(PecaService pecaService) {
        this.pecaService = pecaService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String urlPath = httpExchange.getRequestURI().getPath();
        Integer id = getIdDaURL(urlPath);
        try {
            switch (requestMethod) {
                case "GET":
                    if (id == null) {throw new IllegalArgumentException("Id nao pode ser nulo");}
                    processaBuscaPeloId(httpExchange, id);
                    break;
                case "PUT":
                    if(id == null) {throw new IllegalArgumentException("Id nao pode ser nulo");}
                    processarAdicaoAoEstoque(httpExchange, id);
                    break;
                case "POST":
                    processarCadastro(httpExchange);
                    break;
                default:
                    enviarResposta(httpExchange, 405, "{\"erro\": \"metodo nao permitido\"}");
                    break;
            }
        } catch (IllegalArgumentException e) {
            enviarResposta(httpExchange, 400, "{\"erro\": " + e.getMessage() +".}");
        } catch (Exception e) {
            enviarResposta(httpExchange, 500, "\"erro\": \"erro do servidor: " + e.getMessage() + ".\"");
        }
    }
    public void processarCadastro(HttpExchange httpExchange) throws IOException {
        InputStream requestBody = httpExchange.getRequestBody();
        PecaCadastroDTO request = new ObjectMapper().readValue(requestBody, PecaCadastroDTO.class);

        Peca peca = new Peca();
        peca.setNome(request.nome());
        peca.setValor(request.valor());
        peca.setEstoque(request.estoque());

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(request.idFornecedor());
        peca.setFornecedor(fornecedor);

        Peca pecaCriada = pecaService.cria(peca);
        String json = new ObjectMapper().writeValueAsString(pecaCriada);
        enviarResposta(httpExchange, 201, json);
    }
    public void processarAdicaoAoEstoque(HttpExchange exchange, int id) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        PecaAdicionarAoEstoqueDTO dto = new ObjectMapper().readValue(requestBody, PecaAdicionarAoEstoqueDTO.class);

        Peca pecaAtualizada = pecaService.adicionarAoEstoque(id, dto.estoque());

        PecaResponseDTO response = responseDTO(pecaAtualizada);
        String json = new ObjectMapper().writeValueAsString(response);
        enviarResposta(exchange, 200, json);
    }
    public void processaBuscaPeloId(HttpExchange exchange, int id) throws IOException {
        Peca peca = pecaService.buscaPorId(id);
        PecaResponseDTO response = responseDTO(peca);
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
    private PecaResponseDTO responseDTO(Peca peca){
        return new PecaResponseDTO(
                peca.getId(),
                peca.getNome(),
                peca.getValor(),
                peca.getEstoque(),
                peca.getFornecedor().getId()
        );
    }
    private Integer getIdDaURL(String url){
        String[] split = url.split("/");
        if(split.length > 2){
            try {
                return Integer.parseInt(split[2]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
