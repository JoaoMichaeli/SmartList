# 🛒 SmartList

SmartList é uma aplicação web para gerenciamento de listas de compras, permitindo criar, editar, visualizar e excluir listas e itens de forma simples e intuitiva. O projeto inclui **suporte a internacionalização (i18n)** e **validação de dados** usando Bean Validation, com mensagens em português e inglês.

---

## 🛠 Tecnologias utilizadas

- **Java 17**
- **Spring Boot 3**
  - Spring Web
  - Spring Data JPA
  - Spring Security (autenticação e logout)
  - Thymeleaf (templates)
- **Banco de Dados**: PostgreSQL
- **Frontend**
  - HTML + Thymeleaf
  - Tailwind CSS
  - Material Icons
- **Internacionalização**: Spring i18n (`messages_pt.properties`, `messages_en.properties`)
- **Validação**: Jakarta Bean Validation (`javax.validation` / `jakarta.validation`)

- ## ⚙️ Funcionalidades

- **Listas de Compras**
  - Criar nova lista
  - Editar lista existente
  - Visualizar detalhes da lista
  - Excluir lista
- **Itens da Lista**
  - Adicionar itens à lista
  - Editar itens existentes
  - Marcar itens como checados
  - Excluir itens
- **Internacionalização**
  - Suporte a português (pt-BR) e inglês (en)
  - Troca de idioma via query param `?lang=pt` ou `?lang=en`
- **Validação**
  - Campos obrigatórios: título da lista, nome do item
  - Preço do item deve ser positivo ou zero
  - Mensagens de erro traduzidas via i18n

---

## 🔧 Configuração e execução

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/smartlist.git
cd smartlist
```

2. Rode o Docker Desktop para criar o container do Postgres:
```properies
services:
  postgres:
    image: 'postgres:latest'
    container_name: smartlist
    environment:
      - 'POSTGRES_DB=smartlist'
      - 'POSTGRES_PASSWORD=smartlist'
      - 'POSTGRES_USER=smartlist'
    ports:
      - '5432:5432'
```

3. Build e execute o projeto:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

4. Acesse a aplicação no navegador e faça o login:
```bash
http://localhost:8080/login
```

## 🌐 Troca de idioma
- **Apenas clique no PT ou EN no header**.
- Os textos da interface e mensagens de validação serão exibidos no idioma selecionado.
