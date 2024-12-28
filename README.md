# Kanban System

Este projeto é um sistema simples de gerenciamento de pessoas utilizando um banco de dados PostgreSQL. Ele permite adicionar, visualizar, atualizar e excluir registros de pessoas com informações como nome e nível (junior, pleno, senior). O projeto foi desenvolvido em Java utilizando JDBC para interação com o banco de dados.

## Funcionalidades

- **Adicionar Novo Registro**: Permite adicionar uma nova pessoa ao banco de dados.
- **Visualizar Registros**: Exibe todos os registros cadastrados no banco de dados.
- **Atualizar Registro**: Permite atualizar o nome ou o nível de uma pessoa.
- **Excluir Registro**: Permite excluir uma pessoa do banco de dados.
- **Resetar ID**: Ao excluir todos os registros, o próximo ID é resetado para 1.

## Tecnologias

- Java
- JDBC
- PostgreSQL

## Pré-requisitos

- Java 8 ou superior
- Banco de Dados PostgreSQL
- Driver JDBC para PostgreSQL

## Configuração do Banco de Dados

1. **Criar Banco de Dados**:
   
   Crie um banco de dados PostgreSQL chamado `KanbanDatabase` ou altere a URL de conexão no código.

   ```sql
   CREATE DATABASE KanbanDatabase;
