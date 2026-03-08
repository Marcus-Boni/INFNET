# BuildPipeline Refactoring Kata — Java

## Projeto Original

Este projeto é um kata de refatoração baseado no repositório [BuildPipeline-Refactoring-Kata](https://github.com/emilybache/BuildPipeline-Refactoring-Kata)
de Emily Bache. O código simula um pipeline de build (CI/CD) simples com três etapas:

1. **Testes** — executa os testes de unidade do projeto;
2. **Deploy** — realiza o deploy em produção, caso os testes passem;
3. **Notificação** — envia e-mail com o resultado do pipeline, caso configurado.

A classe central era `Pipeline.java`, com um único método `run()` de ~50 linhas que
misturava as três responsabilidades em blocos `if/else` aninhados e usava comparações
com a string literal `"success"`.

## Problemas Identificados no Código Original

| Problema                       | Descrição                                              |
| ------------------------------ | ------------------------------------------------------ |
| Método longo e confuso         | `run()` concentrava 3 responsabilidades distintas      |
| Aninhamento excessivo          | `if/else` triplo dificultava a leitura da lógica       |
| Comparação com string mágica   | `"success".equals(project.runTests())` — código frágil |
| Baixa testabilidade            | Nenhum teste existia para proteger o comportamento     |
| Responsabilidade única violada | Pipeline orquestrava e notificava ao mesmo tempo       |

## Melhorias Realizadas

### 1. Testes Automatizados Abrangentes (`PipelineTest.java`)

Foram escritos **11 testes** cobrindo todos os cenários possíveis do pipeline:

- projeto com testes passando + deploy bem-sucedido (email ativado/desativado)
- projeto com testes passando + deploy com falha (email ativado/desativado)
- projeto com testes falhando — deploy não é tentado (email ativado/desativado)
- projeto sem testes + deploy bem-sucedido/falhando (email ativado/desativado)

### 2. Extração de Métodos em `Pipeline.java`

O método `run()` foi simplificado para três linhas claras, cada uma expressando
uma fase do pipeline:

```java
public void run(Project project) {
    boolean testsPassed = runTestsPhase(project);
    boolean deploySuccessful = testsPassed && runDeploymentPhase(project);
    notifier.sendNotification(testsPassed, deploySuccessful);
}
```

Os métodos auxiliares `runTestsPhase()` e `runDeploymentPhase()` isolam cada
fase, tornando a lógica autoexplicativa.

### 3. Variáveis e Métodos com Nomes Expressivos (`Project.java`)

Foram adicionados dois métodos com semântica booleana clara:

```java
// Antes (código original):
if ("success".equals(project.runTests())) { ... }

// Depois (refatorado):
boolean passed = project.testsPass();
boolean successful = project.deploysToProductionSuccessfully();
```

### 4. Nova Classe com Responsabilidade Única (`PipelineNotifier.java`)

A lógica de notificação foi extraída para `PipelineNotifier`, que é responsável
exclusivamente por decidir se envia e-mail e qual mensagem compor. `Pipeline`
delega essa responsabilidade sem conhecer os detalhes de notificação.

## Como Executar

**Com Maven (recomendado):**

```bash
mvn test
```

**Com Gradle (requer Java 11+, Gradle 8.8):**

```bash
./gradlew test
```

> Nota: em caminhos com caracteres especiais (ex: `í`) no Windows, o Gradle
> pode apresentar `ClassNotFoundException` no worker de testes. Use Maven
> nesses casos.

## Estrutura de Classes

```
Pipeline           → Orquestra as fases: test → deploy → notify
PipelineNotifier   → Envia notificação por e-mail com a mensagem adequada
Project            → Representa o projeto sendo construído
Config             → Interface: habilita/desabilita e-mail
Emailer            → Interface: envia mensagens
Logger             → Interface: log de informação e de erro
```