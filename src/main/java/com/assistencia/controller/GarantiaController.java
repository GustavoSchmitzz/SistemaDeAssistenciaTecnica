package com.assistencia.controller;
import com.assistencia.dto.GarantiaCadastroDTO;
import com.assistencia.dto.GarantiaResponseDTO;
import com.assistencia.entity.Garantia;
import com.assistencia.service.GarantiaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GarantiaController implements HttpHandler {
    private final GarantiaService garantiaService;
    public GarantiaController(GarantiaService garantiaService) {
        this.garantiaService = garantiaService;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String requestPath = httpExchange.getRequestURI().getPath();
        Integer id = getIdDaURL(requestPath);
        try {
            switch (requestMethod) {
                case "POST":
                    processarCadastro(httpExchange);
                    break;
                case "GET":
                    if(id == null) {throw new RuntimeException("id nao pode ser nulo.");}
                    processarBuscaPeloId(httpExchange, id);
                    break;
                case "DELETE":
                    if(id == null) {throw new RuntimeException("id nao pode ser nulo.");}
                    processarBuscaPeloId(httpExchange, id);
                    break;
                default:
                    enviarResposta(httpExchange, 405, "{\"erro\": \"metodo nao permitido.\"}");
                    break;
            }
        }catch (IllegalArgumentException e) {
            enviarResposta(httpExchange, 400, "\"erro\": " + e.getMessage() + ".\"}");
        }catch (Exception e) {
            enviarResposta(httpExchange, 500, "{\"erro\": \"erro do servidor.\"}");
        }
    }
    public void processarRemocao(HttpExchange httpExchange, int id) throws IOException {
        boolean deletou = garantiaService.removerGarantia(id);
        if(deletou) {
            enviarResposta(httpExchange, 200, "{\"mensagem\": \"deletado com sucesso.\"}");
        }else{
            enviarResposta(httpExchange, 404, "{\"erro\": \"garantia nao encontrada.\"}");
        }
    }
    public void processarCadastro(HttpExchange httpExchange) throws IOException {
        InputStream requestBody = httpExchange.getRequestBody();
        GarantiaCadastroDTO requestDTO = new ObjectMapper().readValue(requestBody, GarantiaCadastroDTO.class);

        Garantia garantia = new Garantia();
        garantia.setDiasDeGarantia(requestDTO.diasDeGarantia());

        Garantia novaGarantia = garantiaService.adicionaGarantia(garantia);

        GarantiaResponseDTO responseDTO = responseDTO(novaGarantia);
        String json = new ObjectMapper().writeValueAsString(responseDTO);
        enviarResposta(httpExchange, 201, json);
    }
    public void processarBuscaPeloId(HttpExchange httpExchange, int id) throws IOException {
        Garantia novaGarantia = garantiaService.buscaPorId(id);
        GarantiaResponseDTO response = new GarantiaResponseDTO(novaGarantia.getId(), novaGarantia.getDiasDeGarantia());
        String json = new ObjectMapper().writeValueAsString(response);
        enviarResposta(httpExchange, 200, json);
    }
    private void enviarResposta(HttpExchange httpExchange, int codigoHttp, String json) throws IOException {
        httpExchange.sendResponseHeaders(codigoHttp, json.length());
        httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        OutputStream os = httpExchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
    private GarantiaResponseDTO responseDTO(Garantia garantia) {
        return new GarantiaResponseDTO(
                garantia.getId(),
                garantia.getDiasDeGarantia()
        );
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
}
