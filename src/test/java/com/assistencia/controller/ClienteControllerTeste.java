package com.assistencia.controller;


import com.assistencia.dto.ClienteCadastroDTO;
import com.assistencia.entity.Cliente;
import com.assistencia.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTeste {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @Test
    void testaSeCadastraClienteERetorna201() throws Exception {
        ClienteCadastroDTO dtoValido = new ClienteCadastroDTO(
                "Gustavo",
                "65999999999",
                "Gustavo@teste.com");

        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("gustavo");
        cliente.setTelefone("65999999999");
        cliente.setEmail("gustavo@teste.com");

        when(clienteService.cadastrar(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("gustavo"))
                .andExpect(jsonPath("$.email").value("gustavo@teste.com"));
    }

    @Test
    void testaSeOCadastroNaoAceitaEmailInvalidoERetorna400() throws Exception {
        ClienteCadastroDTO dto = new ClienteCadastroDTO(
                "gustavo",
                "65999999999",
                "djhdh.com"
        );
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testaSeOCadastroNaoAceitaTelefoneInvalidoERetorna400() throws Exception {
        ClienteCadastroDTO dto = new ClienteCadastroDTO(
                "gustavo",
                "1234",
                "gustavo@teste.com"
        );

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testaSeOCadastroNaoAceitaEmailVazioERetorna400() throws Exception {
        ClienteCadastroDTO dto = new ClienteCadastroDTO(
                "",
                "65999999999",
                "gustavo@teste.com"
        );

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testaSeAtualizaClienteERetorna201() throws Exception {
        ClienteCadastroDTO dto = new ClienteCadastroDTO(
                "gustavo",
                "65999999999",
                "gustavo@teste.com"
        );

        when(clienteService.atualizar(any(Cliente.class))).thenReturn(true);

        mockMvc.perform(put("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testaSeNaoAtualizaClienteComEmailErradoERetornaErro400() throws Exception {
        ClienteCadastroDTO dto = new ClienteCadastroDTO(
                "gustavo",
                "65999999999",
                "gustavoteste.com"
        );

        mockMvc.perform(put("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testaSeNaoAtualizaClienteComNomeErradoERetornaErro400() throws Exception {
        ClienteCadastroDTO dto = new ClienteCadastroDTO(
                "",
                "65999999999",
                "gustavo@teste.com"
        );

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void testaSeNaoAtualizaClienteComTelefoneErradoERetornaErro400() throws Exception {
        ClienteCadastroDTO dto = new ClienteCadastroDTO(
                "gustavo",
                "999999",
                "gustavo@teste.com"
        );

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarClientePorIdComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("gustavo");
        cliente.setEmail("gustavo@teste.com");
        cliente.setTelefone("65999999999");

        when(clienteService.buscaPorId(1)).thenReturn(cliente);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("gustavo"));
    }

    @Test
    void deveListarClientesComPaginacaoPadraoERetornar200() throws Exception {
        when(clienteService.listar(1, 20)).thenReturn(List.of());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagina").value(1))
                .andExpect(jsonPath("$.limiteDaPagina").value(20));
    }

    @Test
    void deveRetornar400AoListarComPaginaInvalida() throws Exception {
        mockMvc.perform(get("/clientes?pagina=0"))
                .andExpect(status().isBadRequest());
    }
}
