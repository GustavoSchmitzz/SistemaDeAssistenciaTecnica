package com.assistencia.controller;

import com.assistencia.dto.PecaComDefeitoListaResponseDTO;
import com.assistencia.dto.PecaComDefeitoResponseDTO;
import com.assistencia.entity.PecaComDefeito;
import com.assistencia.service.PecaComDefeitoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PecaComDefeitoController implements HttpHandler {
    private final PecaComDefeitoService pecaComDefeitoService;
    public PecaComDefeitoController(PecaComDefeitoService pecaComDefeitoService) {
        this.pecaComDefeitoService = pecaComDefeitoService;
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
        List<PecaComDefeito> lista = pecaComDefeitoService.listar(pagina, limite);
        List<PecaComDefeitoResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        PecaComDefeitoListaResponseDTO response = new PecaComDefeitoListaResponseDTO(pagina, limite, listaDTO);
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
    private PecaComDefeitoResponseDTO responseDTO(PecaComDefeito peca) {
        return new PecaComDefeitoResponseDTO(
                peca.getId(),
                peca.getTipoPeca(),
                peca.getMarca(),
                peca.getModelo(),
                peca.getDescricao(),
                peca.getCliente().getId()
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
