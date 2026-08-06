package com.assistencia.controller;

import com.assistencia.dto.FornecedorAtualizarDTO;
import com.assistencia.dto.FornecedorCadastroDTO;
import com.assistencia.dto.FornecedorListaResponseDTO;
import com.assistencia.dto.FornecedorResponseDTO;
import com.assistencia.entity.Fornecedor;
import com.assistencia.service.FornecedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class FornecedorController implements HttpHandler {
    private final FornecedorService fornecedorService;
    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String requestPath = httpExchange.getRequestURI().getPath();
        String requestQuery = httpExchange.getRequestURI().getQuery();
        int[] parametros = getQuery(requestQuery);
        Integer id = getIdURL(requestPath);

        try{
            switch (requestMethod) {
                case "GET":
                    if (id == null) {
                        processaListagem(httpExchange, parametros[0], parametros[1]);
                    } else {
                        processarBuscaPeloId(httpExchange, id);
                    }
                    break;
                case "POST":
                    processarCadastro(httpExchange);
                    break;
                case "PATCH":
                    if (id == null) {throw new IllegalArgumentException("id nao pode ser nulo");}
                    processarAtualizacao(httpExchange, id);
                    break;
                default:
                    enviarResposta(httpExchange, 405, "{\"erro\": \"metodo invalido\"}");
                    break;
            }
        } catch (IllegalArgumentException e) {
            enviarResposta(httpExchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            enviarResposta(httpExchange, 500, "{\"erro\": \"erro do servidor: " + e.getMessage() + "\"}");
        }
    }
    public void processarCadastro(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        FornecedorCadastroDTO dto = new ObjectMapper().readValue(requestBody, FornecedorCadastroDTO.class);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dto.nome());
        fornecedor.setTelefone(dto.telefone());

        Fornecedor novoFornecedor = fornecedorService.cadastrar(fornecedor);

        FornecedorResponseDTO response = responseDTO(novoFornecedor);
        String json = new ObjectMapper().writeValueAsString(response);

        enviarResposta(exchange, 201, json);
    }
    public void processarBuscaPeloId(HttpExchange exchange, int id) throws IOException {
        Fornecedor fornecedor = fornecedorService.buscaPorId(id);

        FornecedorResponseDTO response = responseDTO(fornecedor);
        String json = new ObjectMapper().writeValueAsString(response);

        enviarResposta(exchange, 200, json);
    }
    public void processaListagem(HttpExchange exchange, int pagina, int limite) throws IOException {
        List<Fornecedor> lista = fornecedorService.listar(pagina, limite);
        List<FornecedorResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        FornecedorListaResponseDTO response = new FornecedorListaResponseDTO(pagina, limite, listaDTO);
        String json = new ObjectMapper().writeValueAsString(response);

        enviarResposta(exchange, 200, json);
    }
    public void processarAtualizacao(HttpExchange exchange, int id) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        FornecedorAtualizarDTO requestDTO = new ObjectMapper().readValue(requestBody, FornecedorAtualizarDTO.class);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(id);
        fornecedor.setNome(requestDTO.nome());
        fornecedor.setTelefone(requestDTO.telefone());

        boolean salvou = fornecedorService.atualizar(fornecedor);

        if (salvou) {
            enviarResposta(exchange, 200, "\"mensagem\": \"salvo com sucesso\"");
        }else {
            enviarResposta(exchange, 404, "{\"erro\": \"o fornecedor nao existe.\"}");
        }
    }
    private void enviarResposta(HttpExchange exchange, int codigoHttp, String json) throws IOException {
        exchange.sendResponseHeaders(codigoHttp, json.length());
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
    private FornecedorResponseDTO responseDTO(Fornecedor fornecedor) {
        return new FornecedorResponseDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getTelefone()
        );
    }
    private Integer getIdURL(String exchange) {
        String[] path = exchange.split("/");

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

        if (query == null || query.trim().isEmpty()) {
            return new int[]{pagina, limite};
        }

        String[] params = query.split("&");
        for (String param : params) {
            String[] par = param.split("=");
            if (par.length == 2) {
                if (par[0].equals("pagina")) {
                    pagina = Integer.valueOf(par[1]);
                } else if (par[0].equals("limite")) {
                    limite = Integer.valueOf(par[1]);
                }
            }
        }
        return new int[]{pagina, limite};
    }
}