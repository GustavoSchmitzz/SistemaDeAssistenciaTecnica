package com.assistencia.controller;

import com.assistencia.dto.FuncionarioCadastroDTO;
import com.assistencia.dto.FuncionarioListaResponseDTO;
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
import java.util.List;

public class FuncionarioController implements HttpHandler {
    private final FuncionarioService funcionarioService;
    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String requestQuery = exchange.getRequestURI().getQuery();
        int[] parametros = getQuery(requestQuery);
        Integer id = getIdURL(requestPath);

        try {
            switch (requestMethod) {
                case "POST":
                    processarLogin(exchange);
                    break;
                case "GET":
                    if(id == null) {
                        processarListagem(exchange, parametros[0], parametros[1]);
                    } else {
                        processarBuscaPorID(exchange, id);
                    }
                    break;
                case "PUT":
                    processarCadastro(exchange);
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
    public void processarBuscaPorID(HttpExchange exchange, int id) throws IOException {
        Funcionario funcionario = funcionarioService.buscaPorId(id);
        FuncionarioResponseDTO resposta = responseDTO(funcionario);

        String json = new ObjectMapper().writeValueAsString(resposta);

        enviarResposta(exchange, 200, json);
    }
    public void processarListagem(HttpExchange exchange, int pagina, int limite) throws IOException {
        List<Funcionario> lista = funcionarioService.listar(pagina, limite);
        List<FuncionarioResponseDTO> listaDTO = lista.stream().map(this::responseDTO).toList();

        FuncionarioListaResponseDTO response = new FuncionarioListaResponseDTO(pagina, limite, listaDTO);
        String json = new ObjectMapper().writeValueAsString(response);

        enviarResposta(exchange, 200, json);
    }
    public void processarLogin(HttpExchange exchange) throws IOException {
        InputStream entrada = exchange.getRequestBody();
        FuncionarioLoginDTO dto = new ObjectMapper().readValue(entrada, FuncionarioLoginDTO.class);
        Funcionario funcionario = funcionarioService.loginFuncionario(dto.email(), dto.senha());

        if (funcionario == null) {
            enviarResposta(exchange, 401, "{\"erro\": \"funcionario inexistente\"}");
            return;
        }

        FuncionarioResponseDTO response = responseDTO(funcionario);
        String json = new ObjectMapper().writeValueAsString(response);

        enviarResposta(exchange, 200, json);
    }
    public void processarCadastro(HttpExchange exchange) throws IOException {
        InputStream entrada = exchange.getRequestBody();
        FuncionarioCadastroDTO request = new ObjectMapper().readValue(entrada, FuncionarioCadastroDTO.class);

        Funcionario dados = new Funcionario();
        dados.setEmail(request.email());
        dados.setSenha(request.senha());
        dados.setNome(request.nome());
        dados.setTelefone(request.telefone());
        dados.setEspecialidade(request.especialidade());

        Funcionario novoFuncionario = funcionarioService.cadastraFuncionario(dados);
        FuncionarioResponseDTO response = responseDTO(novoFuncionario);
        String json = new ObjectMapper().writeValueAsString(response);
        enviarResposta(exchange, 201, json);
    }
    private void enviarResposta(HttpExchange exchange, int codigoHTTP, String respostaJson) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
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
    private Integer getIdURL(String requestpath) {
        String[] path = requestpath.split("/");

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
                    pagina = Integer.parseInt(par[1]);
                } else if (par[0].equals("limite")) {
                    limite = Integer.parseInt(par[1]);
                }
            }
        }
        return new int[]{pagina, limite};
    }
}
