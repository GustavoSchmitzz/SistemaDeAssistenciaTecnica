# Sistema de Gerenciamento para Assistência Técnica (API REST)

## 📌 Sobre o Projeto

Este projeto é uma API REST backend desenvolvida do zero em **Java puro** para o gerenciamento de uma assistência técnica. O objetivo principal foi ir além do uso de frameworks prontos, focando na construção e entendimento dos fundamentos da web, como gerenciamento de rotas HTTP, conexão com banco de dados via JDBC e arquitetura de software limpa.

O projeto nasceu de uma disciplina de Banco de Dados (Ciência da Computação - UFMT) e evoluiu durante as férias para uma API completa, sendo a primeira etapa de um ecossistema maior.

## 🚀 Tecnologias Utilizadas

*   **Linguagem:** Java (Puro)
*   **Servidor HTTP:** `com.sun.net.httpserver.HttpServer` (Nativo do Java)
*   **Banco de Dados:** PostgreSQL
*   **Persistência:** JDBC (Java Database Connectivity) com `PreparedStatement` para segurança contra SQL Injection.
*   **Segurança:** BCrypt (Hash de senhas)
*   **Serialização JSON:** Jackson (`ObjectMapper`)
*   **Gerenciamento de Dependências/Build:** Maven / Gradle (Adapte conforme o projeto)
*   **Outros:** Lombok (para redução de boilerplate)

## 🏗️ Arquitetura

A aplicação foi estruturada seguindo o padrão de **Arquitetura em Camadas** (Layered Architecture), uma variação do MVC voltada para APIs REST:

1.  **Controllers (`HttpHandlers`):** Responsáveis por interceptar as requisições HTTP (`GET`, `POST`, `PUT`, `DELETE`), extrair parâmetros, rotas e payloads, e retornar as respostas em formato JSON.
2.  **Services:** Contêm toda a lógica de negócio e validações (ex: validação de e-mail, formato de telefone, cálculo de paginação).
3.  **Repositories:** Responsáveis pela comunicação direta com o PostgreSQL via JDBC.
4.  **Entities:** Classes de domínio que espelham as tabelas do banco de dados (ex: `Cliente`, `OrdemDeServico`).
5.  **DTOs (Data Transfer Objects):** Implementados utilizando **Java Records** para garantir a transferência segura e imutável de dados entre as camadas, ocultando informações sensíveis da resposta final.

## ⚙️ Funcionalidades

O sistema permite o gerenciamento completo de uma assistência técnica, incluindo:

*   **Gestão de Clientes e Funcionários:** Cadastro, atualização, listagem e deleção (CRUD completo).
*   **Gestão de Estoque:** Controle de Peças e Fornecedores.
*   **Ordens de Serviço (OS):** 
    *   Abertura de OS vinculada a um cliente, peça com defeito, técnico responsável e status.
    *   Controle de Garantias e Pagamentos.
*   **Listagem Paginada:** Todos os endpoints de listagem implementam paginação eficiente utilizando cálculo de `OFFSET` e `LIMIT` no banco de dados.

## 🗺️ Próximos Passos (Roadmap)

Este projeto está em contínua evolução. As próximas etapas incluem:

1.  **Front-end:** Criação de uma interface de usuário consumindo esta API (HTML, CSS, JS puro).
2.  **Testes Automatizados:** Implementação de testes unitários e de integração para garantir a integridade das regras de negócio.
3.  **Migração para Spring Boot:** Refatoração do projeto utilizando o ecossistema Spring para comparar a abstração do framework com a implementação manual.

## 🛠️ Como Executar o Projeto

1.  **Pré-requisitos:**
    *   Java JDK 17+ instalado.
    *   PostgreSQL rodando localmente ou em container.
2.  **Configuração do Banco de Dados:**
    *   Crie um banco de dados no PostgreSQL.
    *   Configure as credenciais (URL, usuário e senha) no arquivo `database.properties` localizado na raiz do projeto ou na pasta `resources`.
