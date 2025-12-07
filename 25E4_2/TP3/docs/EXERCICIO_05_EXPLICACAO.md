# Exercício 5: Arquitetura de Classes, Injeção de Dependência e Clean Code

## Visão Geral da Arquitetura

O sistema foi arquitetado seguindo princípios de Clean Architecture e SOLID, com separação clara de responsabilidades em camadas.

## 1. Injeção de Dependência

### Implementação via Construtor

Todas as classes de serviço recebem suas dependências através do construtor:

```java
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = Objects.requireNonNull(projectRepository, 
            "ProjectRepository não pode ser nulo");
    }
}
```

### Por que Injeção via Construtor?

#### 1. Imutabilidade
- Dependências são `final`
- Não podem ser alteradas após inicialização
- Objeto sempre em estado válido

#### 2. Testabilidade
```java
// Fácil mockar dependências em testes
@Test
void deveCriarProjeto() {
    ProjectRepository mockRepo = mock(ProjectRepository.class);
    ProjectService service = new ProjectServiceImpl(mockRepo);
    
    service.criarProjeto("Nome", "Descrição");
    
    verify(mockRepo).save(any(Project.class));
}
```

#### 3. Obrigatoriedade Explícita
- Compilador garante que dependências sejam fornecidas
- Impossível criar objeto sem dependências necessárias

#### 4. Thread-Safety
- Dependências imutáveis são thread-safe
- Sem modificações concorrentes

### Baixo Acoplamento

#### Antes (Alto Acoplamento) ❌
```java
public class ProjectService {
    private ProjectRepository repo = new InMemoryProjectRepository();
    // Acoplado a implementação específica
    // Difícil testar
    // Impossível trocar implementação
}
```

#### Depois (Baixo Acoplamento) ✓
```java
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    // Acoplado apenas à interface
    // Fácil testar (mock)
    // Fácil trocar implementação
}
```

### Facilita Substituição de Implementações

```java
// Desenvolvimento: usar repositório em memória
ProjectRepository devRepo = new InMemoryProjectRepository();
ProjectService devService = new ProjectServiceImpl(devRepo);

// Produção: usar repositório com banco de dados
ProjectRepository prodRepo = new DatabaseProjectRepository(dataSource);
ProjectService prodService = new ProjectServiceImpl(prodRepo);

// Testes: usar mock
ProjectRepository mockRepo = mock(ProjectRepository.class);
ProjectService testService = new ProjectServiceImpl(mockRepo);
```

### Facilita Manutenção

1. **Mudança de implementação**: Apenas trocar no ponto de injeção
2. **Sem quebra**: Código cliente não precisa mudar
3. **Evolução gradual**: Pode coexistir múltiplas implementações

---

## 2. Interfaces - Contratos Essenciais

### Interface Genérica: Repository<T, ID>

```java
public interface Repository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(ID id);
    boolean existsById(ID id);
}
```

#### Benefícios:

1. **Reutilização**: Mesmo contrato para todas as entidades
2. **Consistência**: Operações padronizadas
3. **Genericidade**: Type-safe sem duplicação de código

### Interfaces Específicas

#### ProjectRepository
```java
public interface ProjectRepository extends Repository<Project, String> {
    List<Project> findByNome(String nome);
    List<Project> findProjectsWithSprints();
}
```

#### TaskRepository
```java
public interface TaskRepository extends Repository<Task, String> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByResponsavelId(String responsavelId);
}
```

#### Vantagens:

1. **Contrato Claro**: Define o que é esperado
2. **Múltiplas Implementações**: In-memory, Database, Cache, etc.
3. **Testabilidade**: Fácil criar mocks/stubs
4. **Segregação**: Cada repositório tem métodos específicos do domínio

### Service Interfaces

#### ProjectService
```java
public interface ProjectService {
    Project criarProjeto(String nome, String descricao);
    Optional<Project> buscarPorId(String id);
    List<Project> listarTodos();
    Project adicionarSprint(String projectId, Sprint sprint);
    // ...
}
```

#### Por que Interfaces para Serviços?

1. **Abstração**: Cliente não depende de implementação
2. **Proxy/Decorators**: Fácil adicionar cross-cutting concerns
3. **Testes**: Mock de serviços em camadas superiores

```java
// Exemplo: Adicionar logging transparentemente
public class LoggingProjectService implements ProjectService {
    private final ProjectService delegate;
    private final Logger logger;
    
    public Project criarProjeto(String nome, String descricao) {
        logger.info("Criando projeto: {}", nome);
        Project result = delegate.criarProjeto(nome, descricao);
        logger.info("Projeto criado: {}", result.getId());
        return result;
    }
}
```

---

## 3. Herança vs Composição

### Herança Utilizada: BaseEntity

```java
public abstract class BaseEntity {
    private final String id;
    private final List<String> tags;
    
    protected BaseEntity(String id) {
        this(id, new ArrayList<>());
    }
}

public final class ProjetoComplexo extends BaseEntity {
    private final String nome;
    private final String descricao;
    // ...
}
```

#### Quando Usar Herança?

**Adequado para**:
- Relação "é-um" genuína
- Comportamento compartilhado comum
- Hierarquia estável

**Exemplo válido**: Todas as entidades têm ID e tags

#### Riscos da Herança

##### 1. Acoplamento Forte
```java
// Subclasse depende de implementação da superclasse
public class ProjetoComplexo extends BaseEntity {
    // Se BaseEntity mudar, pode quebrar ProjetoComplexo
}
```

##### 2. Hierarquias Rígidas
```java
// Difícil mudar hierarquia depois
// Se ProjetoComplexo precisar herdar de outra classe
// Java não permite herança múltipla
```

##### 3. Violação de Encapsulamento
```java
// Subclasse pode acessar detalhes de implementação da superclasse
protected List<String> getTags() {
    // Expõe implementação interna
}
```

### Composição: Alternativa Preferível

#### Exemplo com Composição

```java
// Ao invés de herança
public final class ProjetoComplexo {
    private final Identificador id;  // Composição
    private final Etiquetas tags;    // Composição
    private final String nome;
    private final String descricao;
    
    public ProjetoComplexo(Identificador id, Etiquetas tags, ...) {
        this.id = id;
        this.tags = tags;
        // ...
    }
}

// Classes auxiliares
public final class Identificador {
    private final String valor;
    // ...
}

public final class Etiquetas {
    private final List<String> valores;
    // ...
}
```

#### Vantagens da Composição:

1. **Flexibilidade**: Pode compor comportamentos em runtime
2. **Baixo Acoplamento**: Não depende de hierarquia
3. **Reutilização**: Componentes podem ser usados em múltiplas classes
4. **Testabilidade**: Testar componentes isoladamente

#### Quando Preferir Composição?

1. Relação "tem-um" ou "usa-um"
2. Comportamento pode variar dinamicamente
3. Múltiplas fontes de funcionalidade
4. Evitar hierarquias profundas

```java
// Composição permite flexibilidade
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;      // Composição
    private final NotificationService notifier;      // Composição
    private final AuditService auditor;              // Composição
    
    // Pode combinar funcionalidades de várias fontes
    // Pode trocar implementações facilmente
}
```

---

## 4. Separação de Responsabilidades

### Arquitetura em Camadas

```
┌─────────────────────────────────┐
│    Camada de Apresentação       │ (Controller - não implementado)
├─────────────────────────────────┤
│    Camada de Serviço/Negócio    │ (Services)
├─────────────────────────────────┤
│    Camada de Repositório        │ (Repositories)
├─────────────────────────────────┤
│    Camada de Domínio            │ (Entities)
└─────────────────────────────────┘
```

### Responsabilidade de Cada Camada

#### 1. Domínio (Domain)

**Classes**: `Project`, `Sprint`, `Task`, `User`

**Responsabilidades**:
- Representar entidades do negócio
- Encapsular regras de negócio da entidade
- Garantir invariantes (validações)
- Manter estado consistente

**O que NÃO faz**:
- ❌ Acessar banco de dados
- ❌ Lógica de aplicação
- ❌ Tratamento de HTTP/UI

```java
public final class Project {
    // Apenas lógica de domínio
    public Project adicionarSprint(Sprint sprint) {
        // Validação de negócio
        Objects.requireNonNull(sprint);
        
        List<Sprint> novasSprints = new ArrayList<>(this.sprints);
        novasSprints.add(sprint);
        return new Project(this.id, this.nome, this.descricao, novasSprints);
    }
}
```

#### 2. Repositório (Repository)

**Classes**: `ProjectRepository`, `TaskRepository`, `InMemoryProjectRepository`

**Responsabilidades**:
- Persistência e recuperação de dados
- Abstração da fonte de dados
- Queries específicas do domínio

**O que NÃO faz**:
- ❌ Lógica de negócio
- ❌ Validação de regras de negócio
- ❌ Orquestração de operações

```java
public class InMemoryProjectRepository implements ProjectRepository {
    private final Map<String, Project> storage;
    
    @Override
    public Project save(Project entity) {
        // Apenas persistência
        storage.put(entity.getId(), entity);
        return entity;
    }
    
    @Override
    public List<Project> findByNome(String nome) {
        // Apenas query
        return storage.values().stream()
                .filter(p -> p.getNome().contains(nome))
                .collect(Collectors.toList());
    }
}
```

#### 3. Serviço (Service)

**Classes**: `ProjectServiceImpl`, `TaskServiceImpl`

**Responsabilidades**:
- Orquestração de operações de negócio
- Coordenação entre repositórios
- Aplicação de regras de negócio transversais
- Transformação de dados

**O que NÃO faz**:
- ❌ Persistência direta
- ❌ Regras de domínio da entidade

```java
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    
    @Override
    public Project adicionarSprint(String projectId, Sprint sprint) {
        // Orquestração: buscar, validar, atualizar, salvar
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
        
        // Delega lógica de domínio para entidade
        Project projetoAtualizado = project.adicionarSprint(sprint);
        
        // Persiste resultado
        return projectRepository.save(projetoAtualizado);
    }
}
```

### Single Responsibility Principle (SRP)

Cada classe tem uma única razão para mudar:

| Classe | Responsabilidade | Razão para Mudar |
|--------|------------------|------------------|
| `Project` | Representar projeto | Regras de negócio de projeto |
| `ProjectRepository` | Persistir projetos | Tecnologia de persistência |
| `ProjectService` | Orquestrar operações | Fluxos de negócio |

### Exemplo de Violação de SRP ❌

```java
// RUIM: Classe faz tudo
public class Project {
    private String id;
    private String nome;
    private Connection dbConnection;  // ❌
    
    public void save() {  // ❌ Persistência na entidade
        String sql = "INSERT INTO projects...";
        // Lógica de banco de dados
    }
    
    public void sendEmail() {  // ❌ Notificação na entidade
        // Lógica de email
    }
    
    public void generateReport() {  // ❌ Geração de relatório
        // Lógica de relatório
    }
}
```

**Problemas**:
- Difícil testar (depende de DB e email)
- Múltiplas razões para mudar
- Alto acoplamento
- Violação de SRP

### Correção com Separação ✓

```java
// Domínio: apenas lógica de projeto
public final class Project {
    private final String id;
    private final String nome;
    // Sem dependências externas
}

// Repositório: apenas persistência
public class ProjectRepository {
    public Project save(Project project) { /* ... */ }
}

// Serviço de notificação: apenas emails
public class NotificationService {
    public void notifyProjectCreated(Project project) { /* ... */ }
}

// Serviço de relatório: apenas relatórios
public class ReportService {
    public Report generateProjectReport(Project project) { /* ... */ }
}

// Serviço de aplicação: orquestra tudo
public class ProjectService {
    private final ProjectRepository repository;
    private final NotificationService notifier;
    private final ReportService reporter;
    
    public Project criarProjeto(String nome, String descricao) {
        Project project = new Project(UUID.randomUUID().toString(), nome, descricao);
        Project saved = repository.save(project);
        notifier.notifyProjectCreated(saved);
        return saved;
    }
}
```

---

## 5. Coesão: Atributos e Comportamentos Agrupados

### Classe Coesa: Project

```java
public final class Project {
    // Atributos relacionados
    private final String id;
    private final String nome;
    private final String descricao;
    private final List<Sprint> sprints;
    
    // Comportamentos que operam sobre esses atributos
    public Project adicionarSprint(Sprint sprint) {
        // Usa: sprints
    }
    
    public Project removerSprint(String sprintId) {
        // Usa: sprints
    }
    
    public void listarSprints() {
        // Usa: nome, sprints
    }
}
```

**Coesão**: Todos os métodos trabalham com os atributos da classe.

### Exemplo de Baixa Coesão ❌

```java
public class ProjectManager {
    private String projectId;
    private String projectName;
    private String userName;      // ❌ Não relacionado
    private String userEmail;     // ❌ Não relacionado
    private DatabaseConfig dbConfig;  // ❌ Não relacionado
    
    public void createProject() {
        // Usa: projectId, projectName
    }
    
    public void sendUserEmail() {
        // Usa: userName, userEmail
        // ❌ Não usa atributos de projeto
    }
    
    public void connectToDatabase() {
        // Usa: dbConfig
        // ❌ Não relacionado a projeto
    }
}
```

**Problema**: Classe com responsabilidades não relacionadas.

### Refatoração para Alta Coesão ✓

```java
// Classes coesas
public final class Project {
    private final String id;
    private final String nome;
    private final String descricao;
    // Métodos relacionados a projeto
}

public final class User {
    private final String nome;
    private final String email;
    // Métodos relacionados a usuário
}

public final class DatabaseConfig {
    private final String url;
    private final String username;
    // Métodos relacionados a configuração
}
```

### Benefícios da Alta Coesão

1. **Clareza**: Propósito da classe é óbvio
2. **Manutenibilidade**: Mudanças são localizadas
3. **Reutilização**: Classes focadas são mais reutilizáveis
4. **Testabilidade**: Testes mais simples e focados

---

## Conclusão: Benefícios da Arquitetura Implementada

### 1. Manutenibilidade
- Mudanças isoladas em camadas específicas
- Fácil localizar onde fazer alterações
- Refactoring seguro

### 2. Testabilidade
- Cada camada testável isoladamente
- Fácil criar mocks/stubs
- Testes unitários rápidos

### 3. Escalabilidade
- Adicionar funcionalidades sem quebrar existentes
- Substituir implementações (ex: banco de dados)
- Evolução gradual

### 4. Legibilidade
- Código auto-explicativo
- Responsabilidades claras
- Navegação intuitiva

### 5. Flexibilidade
- Adaptar a mudanças de requisitos
- Trocar tecnologias
- Suportar múltiplas implementações

### 6. Qualidade
- Menos bugs
- Código mais confiável
- Facilita code review

### Princípios Aplicados

✓ **SOLID**:
- **S**ingle Responsibility: Cada classe tem uma responsabilidade
- **O**pen/Closed: Aberto para extensão, fechado para modificação
- **L**iskov Substitution: Interfaces podem ser substituídas
- **I**nterface Segregation: Interfaces específicas
- **D**ependency Inversion: Depender de abstrações

✓ **Clean Architecture**:
- Separação de camadas
- Fluxo de dependência correto
- Domínio no centro

✓ **Clean Code**:
- Nomes significativos
- Funções pequenas e focadas
- Código auto-explicativo
