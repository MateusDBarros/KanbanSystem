# Kanban System

Este projeto é um **sistema de gerenciamento de pessoas** simples e eficiente, que utiliza um banco de dados PostgreSQL. O sistema permite adicionar, visualizar, atualizar e excluir registros de pessoas, contendo informações como nome e nível (junior, pleno, senior). Desenvolvido inicialmente com **Java** e **JDBC**, este projeto tem como objetivo consolidar meus conhecimentos em desenvolvimento backend e interações com bancos de dados.

## Funcionalidades

- **Adicionar Novo Registro**: Permite adicionar uma nova pessoa ao banco de dados, incluindo o nome e o nível.
- **Visualizar Registros**: Exibe todos os registros cadastrados no banco de dados.
- **Atualizar Registro**: Permite atualizar o nome ou o nível de uma pessoa no banco de dados.
- **Excluir Registro**: Permite excluir uma pessoa do banco de dados.
- **Resetar ID**: Ao excluir todos os registros, o próximo ID gerado será resetado para 1.
- **Validação de Entrada**: O sistema garante que os dados inseridos estejam corretos e evita registros vazios ou inválidos.

## Tecnologias

- **Java**: Linguagem utilizada para a lógica de backend.
- **JDBC**: API para interação com o banco de dados PostgreSQL.
- **PostgreSQL**: Sistema de gerenciamento de banco de dados utilizado para armazenar os registros.

## Como Usar

1. Clone este repositório para sua máquina local.
2. Configure o banco de dados PostgreSQL com o script fornecido no repositório.
3. Execute o projeto em sua IDE (por exemplo, IntelliJ IDEA ou Eclipse).
4. Interaja com o sistema via terminal, onde será possível adicionar, visualizar, atualizar e excluir registros de pessoas.

## Visão de Futuro

Este projeto está sendo desenvolvido com a intenção de escalar para a web utilizando **Spring Boot** e outras ferramentas modernas, como **Spring Data JPA** e **Thymeleaf**. Com isso, busco aprimorar minhas habilidades e entregar soluções mais robustas e escaláveis. Este é apenas o início, e o objetivo é transformar essa aplicação em um sistema web completo, utilizando conceitos como **APIs RESTful**, autenticação e autorização, além de melhorar a interação com o banco de dados.

Atualmente, estou praticando e estudando as melhores práticas em **Spring Boot** para refatorar e expandir esse projeto, tornando-o adequado para produção e de fácil manutenção.

## Como Contribuir

Contribuições são bem-vindas! Se você encontrar um bug ou quiser adicionar um novo recurso, sinta-se à vontade para abrir um **issue** ou enviar um **pull request**.

## Pré-requisitos

- **Java 8 ou superior**.
- **Banco de Dados PostgreSQL** (local ou em nuvem).
- **Driver JDBC para PostgreSQL**.
- **IDE para Java** (IntelliJ IDEA, Eclipse, etc.).
