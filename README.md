# 🛠️ Sistema de Gestão de Assistência Técnica

Uma API REST desenvolvida em **Java** com o ecossistema **Spring Boot**, projetada para gerenciar o fluxo operacional de uma assistência técnica: desde o cadastro de clientes, fornecedores e peças, até a abertura, precificação, aplicação de garantia e entrega de Ordens de Serviço (OS).

---

## 📌 Visão Geral e Arquitetura

O projeto adota uma **Arquitetura em Camadas (Layered Architecture)** com separação estrita de responsabilidades, garantindo baixo acoplamento e alta manutenibilidade:

* **Controller Layer (`com.assistencia.controller`):** Exposição dos endpoints REST, recepção de payloads, mapeamento para DTOs e controle de status HTTP.
* **Service Layer (`com.assistencia.service`):** Concentração das regras de negócio, sanitização de dados (remoção de espaços e conversão para caixa baixa) e controle de integridade.
* **Repository Layer (`com.assistencia.repository`):** Interfaces herdando de `JpaRepository` para operações de persistência otimizadas e seguras contra SQL Injection.
* **DTOs & Bean Validation (`com.assistencia.dto`):** Contratos de entrada e saída imutáveis utilizando **Java Records**, validados na porta de entrada da API via anotações do **Jakarta Validation**.
* **Security & Hashes:** Criptografia de senhas utilizando hash seguro com a biblioteca **BCrypt** no cadastro e autenticação de funcionários.

```text
src/
├── main/java/com/assistencia/
│   ├── controller/      # Endpoints REST e controle de requisições HTTP
│   ├── dto/             # Java Records imutáveis com validações do Jakarta
│   ├── entity/          # Entidades JPA mapeadas para o banco de dados
│   ├── repository/      # Interfaces Spring Data JPA
│   └── service/         # Camada de lógica de negócio e regras do domínio
└── test/java/com/assistencia/
    ├── controller/      # Testes de Integração da camada Web (@WebMvcTest + MockMvc)
    └── service/         # Testes Unitários de regras de negócio (JUnit 5 + Mockito)
```

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 17+ / 21
* **Framework:** Spring Boot 3 (Spring Web, Spring Data JPA)
* **Persistência / Banco de Dados:** PostgreSQL / Supabase, Hibernate
* **Validação de Dados:** Jakarta Bean Validation
* **Utilitários & Produtividade:** Lombok, jBCrypt, Jackson
* **Testes Automatizados:** JUnit 5, Mockito, Spring Test (`MockMvc`)

---

## 🚦 Endpoints da API

### 👥 Clientes (`/clientes`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/clientes` | Cadastra um novo cliente com validação de dados |
| `GET` | `/clientes` | Lista clientes com suporte à paginação (`?pagina=1&limite=20`) |
| `GET` | `/clientes/{id}` | Busca os dados de um cliente por ID |
| `PUT` | `/clientes/{id}` | Atualiza informações de contato do cliente |

### 👨‍🔧 Funcionários (`/funcionarios`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/funcionarios` | Cadastra funcionário com hash de senha via BCrypt |
| `POST` | `/funcionarios/login` | Autentica funcionário e valida credenciais criptografadas |
| `GET` | `/funcionarios` | Lista funcionários cadastrados (paginado) |
| `GET` | `/funcionarios/{id}` | Busca funcionário por ID |

### 🏭 Fornecedores (`/fornecedores`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/fornecedores` | Cadastra um novo fornecedor |
| `GET` | `/fornecedores` | Lista fornecedores cadastrados (paginado) |
| `GET` | `/fornecedores/{id}` | Busca fornecedor por ID |
| `PUT` | `/fornecedores/{id}` | Atualiza nome e telefone do fornecedor |

### 📦 Peças & Estoque (`/pecas`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/pecas` | Cadastra uma nova peça vinculada a um fornecedor |
| `GET` | `/pecas` | Lista todas as peças em catálogo (paginado) |
| `GET` | `/pecas/{id}` | Busca peça por ID |
| `PUT` | `/pecas/{id}` | Incrementa a quantidade de itens no estoque |

### 🔍 Peças com Defeito (`/pecas-com-defeito`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/pecas-com-defeito` | Lista as peças e equipamentos com defeito cadastrados (paginado) |
| `GET` | `/pecas-com-defeito/{id}` | Busca detalhes da peça com defeito por ID |

### 📋 Ordens de Serviço (`/ordens-de-servico`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/ordens-de-servico` | Abre uma nova OS vinculando peça com defeito, técnico, status e garantia |
| `GET` | `/ordens-de-servico` | Lista ordens de serviço cadastradas (paginado) |
| `GET` | `/ordens-de-servico/{id}` | Busca os detalhes completos de uma OS específica |
| `PUT` | `/ordens-de-servico/{id}` | Atualiza status, forma de pagamento e valor da OS |

### 🧩 Peças Utilizadas na OS (`/ordens-peca`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/ordens-peca` | Lista os vínculos de peças utilizadas nas ordens de serviço (paginado) |

### 🚚 Entregas de OS (`/entregasOS`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/entregasOS/{idOS}` | Finaliza a OS, altera o status para concluído e registra a entrega |
| `DELETE` | `/entregasOS/{id}` | Reverte a entrega da OS e restaura o status anterior |
| `GET` | `/entregasOS` | Lista o histórico de entregas realizadas (paginado) |

### 🛡️ Garantias (`/garantias`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/garantias` | Cadastra um novo período de garantia em dias |
| `GET` | `/garantias` | Lista todas as garantias configuradas |
| `GET` | `/garantias/{id}` | Busca garantia por ID |
| `DELETE` | `/garantias/{id}` | Remove uma opção de garantia |

### 💳 Pagamentos (`/pagamentos`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/pagamentos` | Lista todas as formas de pagamento disponíveis |

### 📊 Status de Serviço (`/status-servico`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/status-servico` | Lista todos os status operacionais de serviço |

---

## 🧪 Estratégia de Testes Automatizados

O sistema conta com ampla cobertura de testes cobrindo todas as camadas críticas da aplicação:

* **Testes Unitários de Serviços (Mockito + JUnit 5):**
    * Validação de fluxos de negócio, sanitização de texto, consistência de IDs e tratamento de exceções de domínio (`PecaServiceTeste`, `FuncionarioServiceTeste`, `OrdemDeServicoServiceTeste`, etc.).
* **Testes de Integração de Controllers (Spring Boot MockMvc):**
    * Validação das anotações de Bean Validation (`@Valid`), rejeição de payloads com formato incorreto (`400 Bad Request`) e validação da resposta JSON (`200 OK`, `201 Created`).

Para executar todos os testes da aplicação:

```bash
mvn test
```

---

## ⚙️ Configuração e Execução

### Pré-requisitos

* Java JDK 17 ou superior instalado
* Maven 3.8+
* Instância do PostgreSQL ou conta no Supabase

### 1. Clonar o repositório

```bash
git clone [https://github.com/SEU_USUARIO/sistemaAssitenciaTecnica.git](https://github.com/SEU_USUARIO/sistemaAssitenciaTecnica.git)
cd sistemaAssitenciaTecnica
```

### 2. Configurar o Banco de Dados

Edite o arquivo `src/main/resources/application.properties` com suas credenciais do banco:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/assistencia_db
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Rodar a aplicação

```bash
mvn spring-boot:run
```

A API estará acessível em `http://localhost:8080`.

---

## 🗺️ Próximos Passos

- [x] Refatoração completa da API para arquitetura em camadas com Spring Boot
- [x] Criação de DTOs e validações com Jakarta Bean Validation
- [x] Cobertura de testes unitários nos Services
- [ ] Conclusão dos testes integrados WebMvc para todos os Controllers
- [ ] Construção da interface Frontend com JavaScript