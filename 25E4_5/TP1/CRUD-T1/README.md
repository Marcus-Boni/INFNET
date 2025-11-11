# Sistema de Gerenciamento de Contas Bancárias

## 📋 Descrição do Projeto

Sistema CRUD (Create, Read, Update, Delete) completo para gerenciamento de contas bancárias, desenvolvido em Java com Spring Boot. O projeto foi desenvolvido seguindo as melhores práticas de engenharia de software, com foco em **Clean Code**, **robustez**, **testabilidade** e **manutenibilidade**.

## 🎯 Objetivos do Projeto

- Implementar um sistema CRUD robusto e bem testado
- Aplicar princípios de Clean Code e boas práticas de desenvolvimento
- Garantir alta cobertura de testes (mínimo 80%)
- Demonstrar tratamento adequado de erros e exceções
- Utilizar testes baseados em propriedades e partições equivalentes
- Criar código imutável e type-safe usando Value Objects

## 🏗️ Arquitetura e Estrutura

### Organização do Código

```
org.example.banco
├── cli/                    # Interface de Linha de Comando
│   └── ContaBancariaCLI
├── entity/                 # Entidades JPA
│   └── Conta
├── exceptions/             # Exceções Customizadas
│   ├── ContaNotFoundException
│   ├── DadosInvalidosException
│   ├── OperacaoInvalidaException
│   └── SaldoInsuficienteException
├── repository/             # Camada de Persistência
│   └── ContaRepository
├── service/                # Lógica de Negócio
│   └── ContaService
└── valueobjects/           # Value Objects Imutáveis
    ├── NomeTitular
    └── Saldo
```

### Princípios de Design Aplicados

#### 1. **Clean Code**
- Nomes descritivos e auto-explicativos
- Funções pequenas com responsabilidade única
- Comentários informativos (não redundantes)
- Organização lógica do código
- Ausência de valores mágicos

#### 2. **Command Query Separation (CQS)**
- **Commands**: Métodos que modificam estado (criarConta, depositar, sacar)
- **Queries**: Métodos que retornam dados (buscarContaPorId, listarTodasContas)
- Separação clara entre operações de leitura e escrita

#### 3. **Imutabilidade**
- Value Objects (NomeTitular, Saldo) são imutáveis
- Operações retornam novos objetos ao invés de modificar existentes
- Previne efeitos colaterais indesejados

#### 4. **Type Safety**
- Uso de Value Objects ao invés de tipos primitivos
- Validações incorporadas nos tipos
- Compilador ajuda a prevenir erros

#### 5. **Fail-Fast**
- Validações imediatas na entrada de dados
- Exceções específicas e informativas
- Estados inválidos são impossíveis

## 📦 Componentes Principais

### Value Objects

#### NomeTitular
- **Propósito**: Encapsular e validar nomes de titulares de contas
- **Regras de Negócio**:
  - Não pode ser nulo ou vazio
  - Deve ter entre 3 e 100 caracteres
  - Espaços em branco são removidos automaticamente
- **Imutável**: Sim
- **Validações**: Automáticas no construtor

#### Saldo
- **Propósito**: Representar valores monetários com precisão
- **Regras de Negócio**:
  - Não pode ser negativo
  - Usa BigDecimal para precisão decimal
  - Operações aritméticas retornam novos objetos
- **Imutável**: Sim
- **Operações**: adicionar(), subtrair(), isSuficientePara()

### Entidades

#### Conta
- **Responsabilidades**:
  - Representar uma conta bancária no sistema
  - Gerenciar operações de depósito e saque
  - Manter integridade dos dados
- **Invariantes**:
  - Todo conta deve ter um titular válido
  - Saldo nunca pode ser negativo
  - ID não pode ser alterado após criação

### Serviços

#### ContaService
- **Padrão**: Service Layer
- **Responsabilidades**:
  - Coordenar operações de negócio
  - Aplicar validações
  - Gerenciar transações
  - Traduzir exceções técnicas em exceções de negócio

**Commands (Modificam Estado)**:
- `criarConta()`: Cria nova conta
- `depositar()`: Adiciona valor ao saldo
- `sacar()`: Remove valor do saldo
- `atualizarSaldo()`: Atualiza saldo diretamente
- `alterarNomeTitular()`: Altera nome do titular
- `excluirConta()`: Remove conta do sistema

**Queries (Apenas Leitura)**:
- `buscarContaPorId()`: Retorna conta específica
- `listarTodasContas()`: Retorna todas as contas
- `contaExiste()`: Verifica existência
- `contarContas()`: Retorna total de contas

### Exceções

#### Hierarquia de Exceções
```
RuntimeException
├── ContaNotFoundException (conta não encontrada)
├── DadosInvalidosException (dados inválidos fornecidos)
├── OperacaoInvalidaException (operação não permitida)
└── SaldoInsuficienteException (saldo insuficiente para operação)
```

Todas as exceções são **unchecked** (RuntimeException) para evitar poluição do código com try-catch desnecessários e permitir que exceções propaguem naturalmente.

## 🧪 Estratégia de Testes

### Tipos de Testes Implementados

#### 1. Testes Unitários (JUnit 5 + AssertJ)
- **Cobertura**: Todas as classes de negócio
- **Técnicas**:
  - Partições equivalentes
  - Análise de valores limites
  - Testes de caminho
  - Testes negativos

#### 2. Testes Baseados em Propriedades (JQwik)
- **Propósito**: Validar invariantes do sistema
- **Casos de Teste**: Centenas gerados automaticamente
- **Propriedades Testadas**:
  - Saldo sempre não-negativo
  - Imutabilidade dos Value Objects
  - Consistência de operações aritméticas
  - Validações sempre aplicadas

#### 3. Testes de Integração (Mockito)
- **Objetivo**: Testar interação entre camadas
- **Isolamento**: Mocks para repositório
- **Validações**: Comportamento de serviços

### Cobertura de Testes

**Meta**: Mínimo 80% de cobertura (linha e branch)

**Ferramenta**: JaCoCo
- Relatórios automáticos em `target/site/jacoco/`
- Build falha se cobertura < 80%
- Métricas: cobertura de linha e branch

## 🎨 Interface de Usuário

### CLI Interativa

A aplicação possui uma interface de linha de comando completa e intuitiva:

**Funcionalidades**:
1. Criar Nova Conta
2. Listar Todas as Contas
3. Consultar Conta por ID
4. Depositar
5. Sacar
6. Atualizar Saldo
7. Alterar Nome do Titular
8. Excluir Conta
9. Estatísticas do Sistema
0. Sair

**Características**:
- Menu intuitivo e organizado
- Validação de entrada do usuário
- Mensagens de erro claras
- Confirmação para operações destrutivas
- Feedback visual de sucesso/erro

## 🔧 Tecnologias Utilizadas

### Core
- **Java 21**: Última versão LTS
- **Spring Boot 3.5.6**: Framework principal
- **Spring Data JPA**: Persistência de dados
- **MySQL 8**: Banco de dados relacional
- **H2 Database**: Banco em memória para testes

### Testes
- **JUnit 5**: Framework de testes unitários
- **JQwik 1.9.0**: Testes baseados em propriedades
- **Mockito 5.8.0**: Framework de mocking
- **AssertJ 3.24.2**: Assertions fluentes
- **JaCoCo 0.8.11**: Cobertura de código

### Qualidade de Código
- **Lombok**: Redução de boilerplate
- **Bean Validation**: Validações declarativas

## 📊 Especificações e Casos de Teste

### Tabela de Decisão - Criação de Conta

| Condição | C1 | C2 | C3 | C4 | C5 |
|----------|----|----|----|----|-----|
| Nome válido (3-100 chars) | S | N | N | S | S |
| Saldo >= 0 | S | S | N | N | S |
| **Ação** | | | | | |
| Criar conta | X | | | | X |
| Exceção nome inválido | | X | | X | |
| Exceção saldo inválido | | | X | | |

### Partições Equivalentes - NomeTitular

| Partição | Classe | Exemplos | Resultado Esperado |
|----------|--------|----------|-------------------|
| PE1 | Nomes válidos (3-100 chars) | "Ana", "João Silva" | Aceito |
| PE2 | Nomes muito curtos (< 3) | "AB", "X" | Exceção |
| PE3 | Nomes muito longos (> 100) | String(101) | Exceção |
| PE4 | Nomes nulos | null | Exceção |
| PE5 | Nomes vazios | "", "   " | Exceção |

### Análise de Limites - Saldo

| Valor | Tipo | Resultado Esperado |
|-------|------|-------------------|
| -0.01 | Abaixo do mínimo | Exceção |
| 0.00 | Limite mínimo | Aceito |
| 0.01 | Logo acima do mínimo | Aceito |
| 999999.99 | Valor normal | Aceito |

## 🚀 Como Executar

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6+
- MySQL 8.0+ (ou usar perfil H2)

### Configuração do Banco de Dados

1. **MySQL** (padrão):
```properties
# src/main/resources/application-mysql.properties
spring.datasource.url=jdbc:mysql://localhost:3306/banco
spring.datasource.username=root
spring.datasource.password=sua_senha
```

2. **H2** (em memória):
```java
// Alterar em CrudT1Application.java
System.setProperty("spring.profiles.active", "h2");
```

### Executar Aplicação

```bash
# Compilar e executar
mvn clean install
mvn spring-boot:run

# Ou usando o JAR gerado
java -jar target/Banco-CRUD-0.0.1-SNAPSHOT.jar
```

### Executar Testes

```bash
# Todos os testes
mvn test

# Com relatório de cobertura
mvn clean test jacoco:report

# Ver relatório
# Abrir: target/site/jacoco/index.html
```

### Verificar Cobertura

```bash
# Build com verificação de cobertura
mvn clean verify

# Build falhará se cobertura < 80%
```

## 📈 Métricas de Qualidade

### Cobertura de Código
- **Linha**: > 80%
- **Branch**: > 80%
- **Método**: > 90%

### Complexidade Ciclomática
- Métodos: < 10
- Classes: < 50

### Princípios SOLID
- ✅ Single Responsibility Principle
- ✅ Open/Closed Principle
- ✅ Liskov Substitution Principle
- ✅ Interface Segregation Principle
- ✅ Dependency Inversion Principle

## 🔍 Boas Práticas Implementadas

### 1. Tratamento de Erros
- Exceções específicas para cada tipo de erro
- Mensagens claras e informativas
- Fail-fast: validações imediatas
- Nunca engolir exceções

### 2. Validações
- Value Objects validam na construção
- Impossible states are impossible
- Type system ajuda a prevenir erros
- Validações explícitas e testadas

### 3. Nomenclatura
- Classes: substantivos (Conta, Saldo)
- Métodos: verbos (depositar, buscar)
- Booleanos: is/has (isSuficiente, hasId)
- Constantes: UPPER_SNAKE_CASE

### 4. Documentação
- Javadoc em APIs públicas
- Comentários explicam "por quê", não "o quê"
- README completo e atualizado
- Exemplos de uso

## 🎓 Conceitos Demonstrados

### Testes
- ✅ Partições equivalentes
- ✅ Análise de valores limites
- ✅ Testes baseados em propriedades
- ✅ Testes negativos e de exceção
- ✅ Tabelas de decisão
- ✅ Testes de imutabilidade

### Clean Code
- ✅ Funções pequenas e focadas
- ✅ Nomes significativos
- ✅ Evitar valores mágicos
- ✅ Command Query Separation
- ✅ Switch statements exaustivos (enums)
- ✅ Fail-fast

### Design
- ✅ Value Objects
- ✅ Domain-Driven Design
- ✅ Repository Pattern
- ✅ Service Layer
- ✅ Dependency Injection
- ✅ Imutabilidade

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais.

## 👥 Autor

Desenvolvido seguindo as melhores práticas de engenharia de software e clean code.

---

**Nota**: Este projeto demonstra a aplicação prática de conceitos avançados de teste de software, design de código e boas práticas de desenvolvimento.
