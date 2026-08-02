package com.assistencia.controller;

import com.assistencia.dto.FuncionarioLoginDTO;
import com.assistencia.dto.FuncionarioResponseDTO;
import com.assistencia.entity.Funcionario;
import com.assistencia.service.FuncionarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FuncionarioController implements HttpHandler {
    private final FuncionarioService funcionarioService;
    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        Integer id = getIdURL(exchange);

        try {
            switch (requestMethod) {
                case "POST":
                    processarLogin(exchange);
                    break;
                default:
                    enviarResposta(exchange, 405, "{\"erro\": \"metodo nao permitido\"}");
            }
        } catch (IllegalArgumentException e) {
            enviarResposta(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            enviarResposta(exchange, 500, "{\"erro\": \"erro do servidor: " + e.getMessage() + "\"}");
        }
    }
    public void processarLogin(HttpExchange exchange) throws IOException {
        InputStream entrada = exchange.getRequestBody();
        FuncionarioLoginDTO dto = new ObjectMapper().readValue(entrada, FuncionarioLoginDTO.class);
        Funcionario funcionario = funcionarioService.loginFuncionario(dto.email(), dto.senha());

        if (funcionario == null) {
            enviarResposta(exchange, 401, "{\"erro\": \"funcionario inexistente\"}");
            return;
        }

        FuncionarioResponseDTO reponse = responseDTO(funcionario);
        String json = new ObjectMapper().writeValueAsString(reponse);

        enviarResposta(exchange, 200, json);
    }
    public void enviarResposta(HttpExchange exchange, int codigoHTTP, String respostaJson) throws IOException {
        InputStream entrada = exchange.getRequestBody();
        exchange.sendResponseHeaders(codigoHTTP, respostaJson.length());
        OutputStream os = exchange.getResponseBody();
        os.write(respostaJson.getBytes());
        os.close();
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
    private Integer getIdURL(HttpExchange exchange) throws IOException {
        InputStream entrada = exchange.getRequestBody();
        String[] path = exchange.getRequestURI().getPath().split("/");

        if (path.length > 2) {
            try {
                return Integer.parseInt(path[2]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
