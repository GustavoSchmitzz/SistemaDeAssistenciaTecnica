# Sistema de Gerenciamento para Assistência Técnica (API REST)

## 📌 Sobre o Projeto

Este projeto é uma API REST backend desenvolvida para o gerenciamento de uma assistência técnica. O projeto nasceu de uma disciplina de Banco de Dados (Ciência da Computação - UFMT) e evoluiu significativamente.

O que começou como uma aplicação focada nos fundamentos web com JDBC puro, agora conta com a robustez do ecossistema Spring na camada de dados e uma suíte completa de testes automatizados, marcando a transição de um projeto acadêmico para uma arquitetura com padrões e boas práticas de mercado.

## 🚀 Tecnologias Utilizadas

*   **Linguagem:** Java 17+
*   **Persistência & Core:** Spring Data JPA, Hibernate
*   **Testes:** JUnit 5, Mockito
*   **Banco de Dados:** PostgreSQL
*   **Servidor HTTP:** `com.sun.net.httpserver.HttpServer` (Nativo do Java - *Transição para Spring Web em andamento*)
*   **Segurança:** BCrypt (Hash de senhas)
*   **Serialização JSON:** Jackson (`ObjectMapper`)
*   **Outros:** Lombok (redução de boilerplate), Java Records

## 🏗️ Arquitetura

A aplicação segue o padrão de **Arquitetura em Camadas** (Layered Architecture), estruturada da seguinte forma:

1.  **Controllers (`HttpHandlers`):** Responsáveis por interceptar as requisições HTTP (`GET`, `POST`, `PUT`, `DELETE`), rotear e retornar as respostas em formato JSON.
2.  **Services (`@Service`):** Contêm toda a lógica de negócio, tratamento de exceções e paginação nativa (`Pageable`). As dependências são injetadas de forma segura via construtor.
3.  **Repositories:** Interfaces baseadas no `JpaRepository` do Spring Data, eliminando o boilerplate de SQL e facilitando operações de banco de dados.
4.  **Entities:** Classes de domínio mapeadas diretamente com o PostgreSQL utilizando anotações JPA (`@Entity`, `@Table`, `@OneToOne`, etc.).
5.  **DTOs (Data Transfer Objects):** Implementados utilizando **Java Records** para garantir a transferência segura e imutável de dados, ocultando informações sensíveis da resposta final.

## ⚙️ Funcionalidades

O sistema permite o gerenciamento completo de uma assistência técnica, incluindo:

*   **Gestão de Clientes e Funcionários:** Cadastro, atualização, listagem e deleção (CRUD completo).
*   **Gestão de Estoque:** Controle inteligente de Peças e Fornecedores.
*   **Ordens de Serviço (OS):**
    *   Abertura de OS vinculada a um cliente, peça com defeito, técnico responsável e status.
    *   Controle de Garantias e Pagamentos.
*   **Listagem Paginada:** Todos os endpoints de listagem implementam paginação eficiente utilizando a interface `Pageable` do Spring Data.

## 🧪 Qualidade de Código e Testes

A integridade das regras de negócio é garantida por uma suíte de **Testes Unitários** desenvolvidos com **JUnit 5** e **Mockito**. A lógica dos `Services` é testada isoladamente, garantindo que o comportamento esperado (como tratamento de hashes de senha, formatação de dados e validações) ocorra corretamente antes de tocar no banco de dados.

## 🗺️ Próximos Passos (Roadmap)

Este projeto está em contínua evolução. As próximas etapas incluem:

1.  **Conclusão da Migração para Spring Boot:** Substituição do servidor HTTP nativo (`HttpHandler`) pela camada web do Spring (`@RestController`).
2.  **Testes de Integração:** Expandir a cobertura de testes para validar o fluxo completo entre o banco de dados e os endpoints.
3.  **Front-end:** Criação de uma interface de usuário consumindo esta API (HTML, CSS, JS).

## 🛠️ Como Executar o Projeto

1.  **Pré-requisitos:**
    *   Java JDK 17+ instalado.
    *   PostgreSQL rodando localmente ou em container.
2.  **Configuração do Banco de Dados:**
    *   Crie um banco de dados no PostgreSQL.
    *   Configure as credenciais (URL, usuário e senha) no arquivo `database.properties` localizado na raiz do projeto ou na pasta `resources`.