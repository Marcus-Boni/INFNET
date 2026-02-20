# TP3 — Sistema CRUD de Produtos com Interface Web

Sistema CRUD completo em Java com Spring Boot, Thymeleaf e H2. Inclui testes unitários, de integração, Selenium e fuzz testing, com cobertura de código via JaCoCo.

---

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/org/example/
│   │   ├── Main.java                          # Ponto de entrada Spring Boot
│   │   ├── model/Produto.java                 # Entidade JPA com Bean Validation
│   │   ├── repository/ProdutoRepository.java  # Spring Data JPA
│   │   ├── service/ProdutoService.java        # Lógica de negócio (fail-early)
│   │   ├── controller/
│   │   │   ├── ProdutoController.java         # Controller MVC (fail-gracefully)
│   │   │   └── GlobalExceptionHandler.java    # Handler global de exceções
│   │   └── exception/
│   │       ├── ProdutoNotFoundException.java
│   │       └── NegocioException.java
│   └── resources/
│       ├── application.properties             # Configurações da aplicação
│       ├── static/css/style.css               # Estilos da interface
│       └── templates/
│           ├── index.html                     # Redirect para /produtos
│           ├── erro.html                      # Página de erro genérica
│           └── produtos/
│               ├── lista.html                 # Listagem com busca
│               ├── formulario.html            # Cadastro e edição
│               └── detalhe.html              # Visualização de produto
└── test/
    └── java/org/example/
        ├── unit/
        │   ├── ProdutoServiceTest.java        # 30 testes unitários do Service
        │   └── ProdutoControllerTest.java     # 16 testes unitários do Controller
        ├── integration/
        │   └── ProdutoCrudIntegrationTest.java # 18 testes de integração (MockMvc)
        ├── selenium/
        │   └── ProdutoSeleniumTest.java       # 13 testes de UI com HtmlUnitDriver
        └── fuzz/
            └── FuzzTest.java                  # 100+ testes fuzz (XSS, SQLi, aleatórios)
```

---

## Pré-Requisitos

| Ferramenta | Versão mínima |
|------------|--------------|
| Java (JDK) | 21           |
| Maven      | 3.9+         |

> Nenhuma instalação de banco de dados ou navegador é necessária. O sistema usa H2 (in-memory) e HtmlUnitDriver (headless Selenium).

---

## Como Iniciar o Sistema

### 1. Compilar e iniciar o servidor

```bash
mvn spring-boot:run
```

O servidor estará disponível em: **http://localhost:8080**

### 2. Acessar a interface

| Endereço                      | Descrição                         |
|-------------------------------|-----------------------------------|
| http://localhost:8080/produtos | Listagem de produtos              |
| http://localhost:8080/produtos/novo | Cadastro de novo produto    |
| http://localhost:8080/h2-console   | Console H2 (desenvolvimento) |

> Credenciais do H2 Console: JDBC URL = `jdbc:h2:mem:produtosdb`, User = `sa`, Password = *(vazio)*

---

## Como Executar os Testes

### Executar todos os testes

```bash
mvn test
```

### Executar apenas os testes unitários

```bash
mvn test -Dtest="org.example.unit.*"
```

### Executar apenas os testes de integração

```bash
mvn test -Dtest="org.example.integration.*"
```

### Executar apenas os testes Selenium

```bash
mvn test -Dtest="org.example.selenium.*"
```

### Executar apenas o fuzz testing

```bash
mvn test -Dtest="org.example.fuzz.*"
```

### Gerar relatório de cobertura JaCoCo

```bash
mvn test jacoco:report
```

O relatório HTML é gerado em:
```
target/site/jacoco/index.html
```

---

## Interpretando os Resultados dos Testes

### Saída no Terminal

```
Tests run: 177, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

| Campo     | Significado                              |
|-----------|------------------------------------------|
| `Tests run` | Total de casos executados              |
| `Failures`  | Asserções que falharam (lógica errada) |
| `Errors`    | Exceções não tratadas durante o teste  |
| `Skipped`   | Testes ignorados                       |

### Relatório JaCoCo (Cobertura)

Abra `target/site/jacoco/index.html` no navegador. As métricas são:

| Métrica          | Significado                                       |
|------------------|---------------------------------------------------|
| **Instructions** | % de bytecodes executados                        |
| **Branches**     | % de decisões (if/else/switch) cobertas          |
| **Lines**        | % de linhas de código executadas                 |
| **Methods**      | % de métodos chamados durante os testes          |

> Cores: 🟢 Verde = coberto | 🟡 Amarelo = parcialmente coberto | 🔴 Vermelho = não coberto

### Relatórios Surefire

Relatórios XML individuais por classe de teste em:
```
target/surefire-reports/
```

---

## Estratégias de Teste Implementadas

### 1. Testes Unitários (`unit/`)
- **Mockito** para isolar dependências (repositório, service)
- `@ParameterizedTest` com `@ValueSource`, `@CsvSource`, `@NullAndEmptySource`
- Cobertura de todos os branches de `ProdutoService` e `ProdutoController`
- Simulação de falhas de repositório (timeout, DB unavailable)

### 2. Testes de Integração (`integration/`)
- Spring Boot Test + **MockMvc** (stack completo com H2)
- `@DirtiesContext` para isolamento entre testes
- Testes parametrizados de campos inválidos
- Verificação de redirecionamentos, views e conteúdo HTML

### 3. Testes Selenium (`selenium/`)
- **HtmlUnitDriver** (headless, sem instalação de browser)
- Fluxo CRUD completo via interface (criar → listar → editar → deletar)
- Validação de campos de formulário, tabelas e mensagens de feedback
- Verificação de ausência de stacktraces em mensagens de erro

### 4. Fuzz Testing (`fuzz/`)
- Payloads XSS, SQL Injection, Path Traversal, CRLF Injection
- Strings longas (até 10.000 caracteres)
- Unicode, caracteres especiais e nulos
- 50 requisições com campos completamente aleatórios
- Teste de sobrecarga (100 requisições consecutivas)
- Garantia: **nenhuma entrada causa status 500**

### 5. Fail-Early
- `ProdutoService` valida pré-condições antes de qualquer operação
- IDs nulos/negativos são rejeitados imediatamente
- Termos de busca > 200 caracteres são bloqueados

### 6. Fail-Gracefully
- `GlobalExceptionHandler` centraliza todos os erros
- Mensagens amigáveis sem exposição de internals
- `server.error.include-stacktrace=never` nas configurações

---

## Tecnologias Utilizadas

| Tecnologia          | Versão  | Uso                              |
|---------------------|---------|----------------------------------|
| Spring Boot         | 3.2.3   | Framework principal              |
| Spring Data JPA     | 3.2.3   | Persistência                     |
| H2 Database         | runtime | Banco in-memory                  |
| Thymeleaf           | 3.1     | Templates HTML                   |
| Bean Validation     | 3.0     | Validação de campos              |
| JUnit 5             | 5.10    | Framework de testes              |
| Mockito             | 5.7     | Mocking para testes unitários    |
| Selenium HtmlUnit   | 4.13    | Testes de UI headless            |
| JaCoCo              | 0.8.11  | Cobertura de código              |

