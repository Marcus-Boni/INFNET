# Sistema de Gestão de Projetos Ágil - TP3

## Descrição do Projeto

Sistema desenvolvido para gerenciamento de projetos ágeis, permitindo a organização de tarefas, acompanhamento de progresso e gestão de colaboradores através de sprints e projetos.

## Ambiente de Desenvolvimento

- **Java**: 21.0.9 LTS
- **Maven**: 3.9.11
- **Encoding**: UTF-8

## Estrutura do Projeto

```
TP3/
├── pom.xml
├── src/
│   └── main/
│       └── java/
│           └── br/
│               └── edu/
│                   └── infnet/
│                       ├── domain/
│                       │   ├── Project.java
│                       │   ├── Sprint.java
│                       │   ├── Task.java
│                       │   ├── User.java
│                       │   └── enums/
│                       │       └── TaskStatus.java
│                       ├── exercicio1/
│                       │   ├── Pedido.java
│                       │   ├── ItemPedido.java
│                       │   └── StatusPedido.java
│                       ├── exercicio2/
│                       │   ├── CalculadoraPedidoComEfeitosColaterais.java
│                       │   ├── DescontoAplicavel.java
│                       │   ├── CalculadoraPedidoRefatorada.java
│                       │   ├── Desconto.java
│                       │   └── ResultadoCalculo.java
│                       ├── exercicio3/
│                       │   └── ProdutoItem.java
│                       ├── exercicio4/
│                       │   ├── RegistroTransacao.java
│                       │   ├── StatusTransacao.java
│                       │   └── CategoriaTransacao.java
│                       └── exercicio5/
│                           ├── repository/
│                           │   ├── Repository.java
│                           │   ├── ProjectRepository.java
│                           │   ├── TaskRepository.java
│                           │   └── impl/
│                           │       ├── InMemoryProjectRepository.java
│                           │       └── InMemoryTaskRepository.java
│                           ├── service/
│                           │   ├── ProjectService.java
│                           │   ├── TaskService.java
│                           │   └── impl/
│                           │       ├── ProjectServiceImpl.java
│                           │       └── TaskServiceImpl.java
│                           └── domain/
│                               ├── BaseEntity.java
│                               └── ProjetoComplexo.java
```

## Classes do Domínio Principal

### 1. Project (Projeto)
- **Atributos**: id, nome, descrição, lista de sprints
- **Métodos**: 
  - `adicionarSprint(Sprint)`: Adiciona uma sprint ao projeto
  - `removerSprint(String)`: Remove sprint pelo ID
  - `listarSprints()`: Exibe todas as sprints
- **Características**: Imutável, retorna novas instâncias em modificações

### 2. Sprint
- **Atributos**: id, nome, dataInicio, dataFim, lista de tarefas
- **Métodos**:
  - `adicionarTarefa(Task)`: Adiciona tarefa à sprint
  - `removerTarefa(String)`: Remove tarefa pelo ID
  - `listarTarefas()`: Exibe todas as tarefas
- **Validações**: Data de fim não pode ser anterior à data de início

### 3. Task (Tarefa)
- **Atributos**: id, título, descrição, status (enum), responsável (User)
- **Métodos**:
  - `atribuirResponsavel(User)`: Atribui usuário responsável
  - `alterarStatus(TaskStatus)`: Atualiza status da tarefa
  - `exibirDetalhes()`: Mostra informações detalhadas
- **Status**: TODO, IN_PROGRESS, DONE

### 4. User (Usuário)
- **Atributos**: id, nome, email, cargo
- **Métodos**:
  - `atualizarEmail(String)`: Atualiza email do usuário
  - `definirCargo(String)`: Define cargo do usuário

## Exercícios Implementados

### Exercício 1: Modelagem e Imutabilidade
- **Classe**: `Pedido`
- **Conceito**: Entidade central imutável
- **Documentação**: `docs/EXERCICIO_01_EXPLICACAO.md`
- **Características**:
  - Todos os atributos `private final`
  - Sem setters
  - Métodos retornam novas instâncias
  - Thread-safe por design

### Exercício 2: Refatoração e Controle de Efeitos Colaterais
- **Classes**: 
  - `CalculadoraPedidoComEfeitosColaterais` (versão problemática)
  - `CalculadoraPedidoRefatorada` (versão corrigida)
- **Conceito**: Eliminação de efeitos colaterais
- **Documentação**: `docs/EXERCICIO_02_EXPLICACAO.md`
- **Melhorias**:
  - Métodos puros sem side-effects
  - Retorno de objetos imutáveis
  - Thread-safety garantida

### Exercício 3: Objetos Menores Imutáveis e Validação
- **Classe**: `ProdutoItem`
- **Conceito**: Validação e imutabilidade em objetos menores
- **Documentação**: `docs/EXERCICIO_03_EXPLICACAO.md`
- **Validações**:
  - Quantidade não negativa
  - Preço não negativo
  - Campos obrigatórios

### Exercício 4: Escolha de Tipos de Dados
- **Classe**: `RegistroTransacao`
- **Conceito**: Tipos adequados para clareza e precisão
- **Documentação**: `docs/EXERCICIO_04_EXPLICACAO.md`
- **Tipos Utilizados**:
  - `BigDecimal` para valores monetários
  - `LocalDateTime` para datas e horas
  - `Enum` para status e categorias
  - `int` para quantidades

### Exercício 5: Arquitetura e Injeção de Dependência
- **Pacotes**: `exercicio5.repository`, `exercicio5.service`, `exercicio5.domain`
- **Conceito**: Clean Architecture e SOLID
- **Documentação**: `docs/EXERCICIO_05_EXPLICACAO.md`
- **Padrões**:
  - Injeção de dependência via construtor
  - Interfaces para abstrações
  - Separação em camadas
  - Repository Pattern
  - Service Layer

## Como Compilar

```bash
mvn clean compile
```

## Como Testar

```bash
mvn test
```

## Características Técnicas

### Imutabilidade
Todas as classes de domínio e exercícios são imutáveis:
- Atributos `final`
- Sem setters
- Cópias defensivas de coleções
- Métodos retornam novas instâncias

### Thread-Safety
- Objetos imutáveis são naturalmente thread-safe
- Repositórios usam `ConcurrentHashMap`
- Sem estado compartilhado mutável

### Validação
- Validação no construtor (fail-fast)
- `Objects.requireNonNull()` para campos obrigatórios
- Regras de negócio validadas

### Separação de Responsabilidades
- **Domain**: Lógica de negócio e entidades
- **Repository**: Persistência e queries
- **Service**: Orquestração e regras de aplicação
