package com.assistencia.controller;

import com.assistencia.dto.ClienteAtualizarInfoDTO;
import com.assistencia.dto.ClienteCadastroDTO;
import com.assistencia.dto.ClienteListaResponseDTO;
import com.assistencia.dto.ClienteResponseDTO;
import com.assistencia.entity.Cliente;
import com.assistencia.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ClienteController  implements HttpHandler {
    private final ClienteService clienteService;
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String requestQuery = exchange.getRequestURI().getQuery();
        Integer id = extrairIdDaURL(requestPath);
        //parametro[0] é a pagina
        //parametro[1] é o limite
        int[] parametros = getQuery(requestQuery);
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
                    if (id == null) {
                        processarListarClientes(exchange, parametros[0], parametros[1]);
                    } else {
                        processaBuscaPorId(exchange, id);
                    }
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
    public void processarListarClientes(HttpExchange exchange, int pagina, int limite) throws IOException {
        List<Cliente> listaClientes = clienteService.listar(pagina, limite);
        //processa cada cliente da lista e coloca ele na lista de DTOs
        List<ClienteResponseDTO> listaDTO = listaClientes.stream().map(cliente ->
                new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone()))
                .toList();

        ClienteListaResponseDTO listaResponseDTO = new ClienteListaResponseDTO(pagina, limite, listaDTO);
        String json = new ObjectMapper().writeValueAsString(listaResponseDTO);

        enviarResposta(exchange, 200, json);

    }
    public void processarCadastroCliente(HttpExchange exchange) throws IOException {
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
    public void processarAtualizacao(HttpExchange exchange, int id) throws IOException {
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
    public void processaBuscaPorId(HttpExchange exchange, int id) throws IOException {
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
