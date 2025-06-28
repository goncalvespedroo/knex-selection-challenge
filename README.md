# API de Despesas de Deputados

Este projeto implementa uma API RESTful em Java com Spring Boot para gerenciar e consultar as despesas da Cota para o Exercício da Atividade Parlamentar (CEAP) de deputados federais, utilizando dados abertos da Câmara dos Deputados.

## Tabela de Conteúdo

1. [Visão Geral](#visão-geral)
2. [Requisitos](#requisitos)
3. [Tecnologias Utilizadas](#tecnologias-utilizadas)
4. [Estrutura do Projeto](#estrutura-do-projeto)
5. [Como Executar a Aplicação](#como-executar-a-aplicação)
6. [Endpoints da API](#endpoints-da-api)
7. [Modelo de Dados](#modelo-de-dados)
8. [Considerações Finais](#considerações-finais)

## Visão Geral

Este projeto visa criar uma solução que permite:

- Baixar dados de despesas de deputados federais diretamente da API de Dados Abertos da Câmara dos Deputados (ano de 2025).
- Parsear e persistir esses dados em um banco de dados relacional.
- Oferecer uma API RESTful para consultar e obter relatórios sobre essas despesas, como somatórios totais por deputado ou geral, e listar despesas com filtros e paginação.

## Requisitos

Para executar esta aplicação, você precisará ter instalado:

- Java Development Kit (JDK) 17 ou superior.
- Apache Maven (para gerenciamento de dependências e construção do projeto).

## Tecnologias Utilizadas

- Linguagem: Java 17
- Framework: Spring Boot 3.3.1
- Banco de Dados: H2 Database (em memória, para desenvolvimento e testes)
- ORM: Spring Data JPA com Hibernate
- Servidor Web: Apache Tomcat (embarcado no Spring Boot)
- Construção: Apache Maven
- Outras Bibliotecas: Lombok (para reduzir boilerplate code)

## Estrutura do Projeto

A estrutura de diretórios da aplicação é a seguinte:

.
├── src
│ └── main
│ └── java
│ └── com
│ └── despesasdeputados
│ ├── config # Classes de configuração da aplicação
│ ├── controller # Endpoints da API REST
│ ├── model # Entidades de dados (Deputado, Despesa)
│ ├── repository # Interfaces de acesso a dados (Spring Data JPA)
│ ├── service # Lógica de negócios e processamento de CSV
│ └── DespesasDeputadosApplication.java # Classe principal da aplicação
│ └── resources
│ └── application.properties # Configurações do Spring Boot e do banco de dados
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
└── pom.xml # Arquivo de configuração do Maven


Endpoints da API
A API expõe os seguintes endpoints RESTful para interagir com os dados:

Upload/Processamento de Dados do CEAP

URL: /upload-ceap

Método: POST

Descrição: Inicia o processo de download e importação das despesas de 2025.

Corpo da Requisição: N/A (o arquivo CSV é baixado internamente pela API).

Exemplo de Requisição (com curl):

bash
Copiar
Editar
curl -X POST http://localhost:8080/upload-ceap
Listagem de Deputados

URL: /deputados

Método: GET

Descrição: Retorna uma lista de deputados.

Parâmetros de Query:

uf (Opcional): Sigla da Unidade Federativa para filtrar os deputados (ex: SP, RJ, MG).

Exemplos de Requisição:

Listar todos os deputados: GET http://localhost:8080/deputados

Listar deputados de São Paulo: GET http://localhost:8080/deputados?uf=SP

Relatórios de Despesas

Somatório de Despesas de um Deputado Específico

URL: /relatorios/deputados/{id}/total-despesas

Método: GET

Descrição: Retorna o somatório total das despesas de um deputado específico.

Parâmetros de Path:

id (Obrigatório): O ID único do deputado.

Exemplo de Requisição:

GET http://localhost:8080/relatorios/deputados/1/total-despesas

Somatório Total de Despesas (Geral)

URL: /relatorios/total-despesas

Método: GET

Descrição: Retorna o somatório total de despesas de todos os deputados combinados.

Exemplo de Requisição:

GET http://localhost:8080/relatorios/total-despesas

Listagem de Despesas

Para listar todas as despesas com filtros e paginação: /despesas

Para listar despesas de um deputado específico: /deputados/{id}/despesas

Método: GET

Descrição: Retorna uma lista paginada de despesas, com a opção de filtrar.

Parâmetros de Query (para /despesas):

page (Opcional): Número da página a ser retornada (padrão: 0).

size (Opcional): Quantidade de itens por página (padrão: 10).

startDate (Opcional): Data de início para filtrar as despesas por período (formato YYYY-MM-DD).

endDate (Opcional): Data de fim para filtrar as despesas por período (formato YYYY-MM-DD).

fornecedor (Opcional): Nome parcial do fornecedor para filtrar as despesas (busca insensível a maiúsculas/minúsculas).

Parâmetros de Path (para /deputados/{id}/despesas):

id (Obrigatório): O ID único do deputado cujas despesas serão listadas.

Exemplos de Requisição:

Listar todas as despesas: GET http://localhost:8080/despesas

Listar despesas paginadas: GET http://localhost:8080/despesas?page=1&size=5

Filtrar por data: GET http://localhost:8080/despesas?startDate=2025-01-01&endDate=2025-01-31

Filtrar por fornecedor: GET http://localhost:8080/despesas?fornecedor=restaurante

Listar despesas de um deputado específico: GET http://localhost:8080/deputados/1/despesas

Modelo de Dados
O banco de dados contém as seguintes entidades e seus relacionamentos:

Deputado
id (Chave Primária): Identificador único para cada deputado.

nome: Nome completo do deputado.

uf: Sigla da Unidade Federativa (estado) do deputado.

cpf: CPF do deputado. Usado como identificador único para garantir a idempotência na importação.

partido: Partido político do deputado.

Despesa
id (Chave Primária): Identificador único para cada despesa.

dataEmissao: Data da emissão do documento fiscal da despes
