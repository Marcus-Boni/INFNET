# TP2 — Relatório de Refatoração: BuildPipeline Refactoring Kata

**Aluno:** Marcus Galvão  
**Disciplina:** Engenharia de Software Disciplinada — Período 6  
**Data:** Março de 2026

---

## 1. Contexto e Objetivo

Este trabalho consistiu em assumir a manutenção de um sistema legado Java que
implementa um pipeline de build (CI/CD) simplificado. O código original era
funcional, porém apresentava sérios problemas de clareza, coesão e
testabilidade. O objetivo foi aplicar técnicas clássicas de refatoração —
com suporte de testes automatizados — para melhorar a qualidade interna do
código sem alterar seu comportamento externo.

---

## 2. Análise do Código Original

### 2.1 Estrutura Inicial

O projeto consistia em:

- `Pipeline.java` — classe central com um método `run()` de ~50 linhas
- Interfaces de dependência (`Config`, `Emailer`, `Logger`, `Project`)
- `PipelineTest.java` — arquivo de teste **vazio** (continha apenas `// TODO`)

### 2.2 Problemas Identificados

**a) Método `run()` com múltiplas responsabilidades**

O método misturava três fases distintas em uma única função:
executar testes, realizar deploy e enviar notificação por e-mail. Isso
violava o Princípio da Responsabilidade Única (SRP) e tornava difícil
compreender, testar ou modificar qualquer parte isoladamente.

**b) Aninhamento excessivo de condicionais**

O `if/else` triplo aninhado para determinar qual e-mail enviar era
difícil de rastrear logicamente, especialmente para novos membros da equipe.

**c) Strings mágicas como resultado de operações**

Os métodos `project.runTests()` e `project.deploy()` retornavam `String`
(`"success"` ou `"failure"`). As comparações `"success".equals(...)` são
frágeis — um erro de digitação não seria detectado pelo compilador.

**d) Ausência total de testes**

Sem cobertura de testes, qualquer modificação era de alto risco. Não havia
forma segura de verificar se o comportamento foi preservado.

---

## 3. Processo de Decisão

### 3.1 Regra de Ouro: Testes Primeiro

Antes de qualquer modificação estrutural, foram escritos testes que cobrissem
todos os fluxos do sistema. Somente com esses testes "passando no original"
era seguro prosseguir com a refatoração.

Foram identificados 11 cenários relevantes (3 situações de teste × 2 resultados
de deploy × 2 configurações de e-mail, com exceção de testes falhando que não
tentam deploy).

### 3.2 Sequência de Refatorações

As refatorações foram aplicadas em sequência incremental, confirmando que os
testes permaneciam verdes a cada passo:

1. **Escrever testes** → garante uma rede de proteção
2. **Adicionar métodos booleanos a `Project`** → elimina strings mágicas
3. **Extrair `PipelineNotifier`** → separar a preocupação de notificação
4. **Extrair `runTestsPhase()` e `runDeploymentPhase()` em `Pipeline`** → simplificar `run()`

### 3.3 Critérios de Decisão por Tarefa

| Decisão                                               | Justificativa                                                                                           |
| ----------------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| Criar `PipelineNotifier`                              | Notificação é uma responsabilidade separada (SRP). `Pipeline` deve orquestrar, não saber montar e-mails |
| Usar `testsPass()` ao invés de refatorar `runTests()` | Manter compatibilidade retroativa da API existente enquanto expõe semântica clara                       |
| Não criar `PipelineResult` (value object)             | Seria over-engineering para apenas dois booleanos nesse contexto                                        |
| Manter `run(Project)` como ponto de entrada público   | API externa não deveria mudar                                                                           |

---

## 4. Detalhamento das Mudanças

### 4.1 Seção 1 — Ambiente e Verificação Inicial

**Problema:** Gradle 6.2.2 era incompatível com Java 21. Os testes travavam com
`GroovyBugError` ao inicializar o daemon.

**Solução:** Atualização do Gradle wrapper para 8.8 e ajuste do `build.gradle`:

- Substituição de `testCompile` (removido no Gradle 7) por `testImplementation`
- Remoção do repositório `jcenter()` (descontinuado)
- Adição de `useJUnitPlatform()` no bloco `test`

O projeto também possui `pom.xml` com Maven 3.9.11, que foi ajustado de
`source/target 25` para `21` (compatível com o JDK disponível).

> **Observação:** O Gradle ainda apresenta `ClassNotFoundException` nos workers
> de teste em sistemas Windows com caracteres acentuados no caminho. O Maven
> não possui esse problema e é o build tool recomendado neste ambiente.

### 4.2 Seção 2 — Reestruturando Métodos Complexos

**Antes:**

```java
public void run(Project project) {
    boolean testsPassed;
    boolean deploySuccessful;
    // ~45 linhas de if/else aninhados
}
```

**Depois:**

```java
public void run(Project project) {
    boolean testsPassed = runTestsPhase(project);
    boolean deploySuccessful = testsPassed && runDeploymentPhase(project);
    notifier.sendNotification(testsPassed, deploySuccessful);
}
```

O método `run()` agora lê como uma descrição em prosa do processo. Cada fase
tem um nome que comunica sua intenção.

### 4.3 Seção 3 — Expressividade com Variáveis e Métodos

**Antes:**

```java
if ("success".equals(project.runTests())) { ... }
if ("success".equals(project.deploy())) { ... }
```

**Depois (Project.java com novos métodos):**

```java
boolean passed = project.testsPass();
boolean successful = project.deploysToProductionSuccessfully();
```

A semântica agora é capturada no tipo retornado (`boolean`) e no nome do
método, eliminando a necessidade de interpretar strings.

### 4.4 Seção 4 — Encapsulamento e Assinaturas

O método `buildEmailMessage(boolean, boolean)` em `PipelineNotifier` substitui
um `if/else` aninhado confuso por uma lógica linear com early return:

```java
private String buildEmailMessage(boolean testsPassed, boolean deploySuccessful) {
    if (!testsPassed) {
        return "Tests failed";
    }
    return deploySuccessful ? "Deployment completed successfully" : "Deployment failed";
}
```

A lógica "se os testes falharam, o e-mail é sobre testes" é exposta imediatamente,
sem precisar rastrear a lógica de deploy.

### 4.5 Seção 5 — Reorganizando Classes

A criação de `PipelineNotifier` aplica o Princípio da Responsabilidade Única:

- `Pipeline` → orquestra: determina se testes passaram, se deploy ocorreu
- `PipelineNotifier` → notifica: decide qual e-mail enviar e registra o log

Essa separação torna cada classe menor, mais fácil de testar isoladamente e
mais fácil de modificar. Por exemplo, mudar o meio de notificação (Slack, SMS)
exigiria modificar apenas `PipelineNotifier`, sem tocar em `Pipeline`.

---

## 5. Dificuldades Encontradas

### 5.1 Incompatibilidade de Ferramentas

A dificuldade mais imediata foi a incompatibilidade entre Gradle 6.2.2 e Java 21.
Gradle 6.x usa Groovy e APIs internas que foram removidas no Java 17+. A solução
foi atualizar para Gradle 8.8, o que exigiu também atualizar as configurações de
dependências e adicionar `useJUnitPlatform()`.

### 5.2 `ClassNotFoundException` do Gradle no Windows

Mesmo após atualizar o Gradle, os testes falhavam com `ClassNotFoundException`
no worker de testes. O diagnóstico apontou para o caminho do projeto conter
o caractere acentuado `í` (em "Período"), que o processo filho do Gradle
não conseguia resolver corretamente no Windows. A solução de contorno foi usar
Maven, que não apresenta esse comportamento.

### 5.3 Nomear Método com Mesmo Nome que Campo

Ao tentar adicionar `public boolean deploysSuccessfully()` em `Project.java`,
havia uma ambiguidade de legibilidade com o campo `private final boolean deploysSuccessfully`.
Embora Java permita isso (campo e método com mesmo nome), optou-se por
`deploysToProductionSuccessfully()` — nome mais longo, mas sem ambiguidade.

---

## 6. Aprendizados

1. **Testes são a rede de proteção da refatoração.** Sem os 11 testes escritos
   antes de qualquer mudança, não haveria como saber se o comportamento foi
   preservado. O ciclo "escrever teste → refatorar → confirmar verde" é
   fundamental.

2. **Nomes são o principal meio de comunicação do código.** Renomear
   `"success".equals(...)` para `testsPass()` não mudou o comportamento, mas
   mudou radicalmente a legibilidade — qualquer desenvolvedor entende o segundo
   instantaneamente.

3. **Extrair classes não é sempre over-engineering.** `PipelineNotifier` é uma
   classe simples com ~30 linhas, mas ela remove uma responsabilidade inteira de
   `Pipeline`, tornando ambas as classes mais coesas e mais fáceis de testar.

4. **Compatibilidade de ferramentas é um risco real em projetos legados.** Em um
   sistema legado real, a primeira tarefa pode ser justamente atualizar o
   ambiente de build para que seja possível trabalhar com segurança.

5. **Early return elimina aninhamento.** Substituir `if/else` aninhado por
   retorno antecipado (`if (!testsPassed) return "Tests failed"`) lineariza a
   lógica e torna o código mais fácil de ler de cima para baixo.

---

## 7. Resumo das Alterações por Arquivo

| Arquivo                     | Tipo       | Alteração                                                                                                                |
| --------------------------- | ---------- | ------------------------------------------------------------------------------------------------------------------------ |
| `Pipeline.java`             | Modificado | Método `run()` refatorado; `runTestsPhase()` e `runDeploymentPhase()` extraídos; delega notificação a `PipelineNotifier` |
| `PipelineNotifier.java`     | **Criado** | Nova classe responsável pela notificação por e-mail                                                                      |
| `Project.java`              | Modificado | Adicionados `testsPass()` e `deploysToProductionSuccessfully()`                                                          |
| `PipelineTest.java`         | Modificado | Substituído stub vazio por 11 testes abrangentes                                                                         |
| `build.gradle`              | Modificado | Gradle 8.8, `testImplementation`, `useJUnitPlatform()`, removido `jcenter()`                                             |
| `gradle-wrapper.properties` | Modificado | Atualizado para Gradle 8.8                                                                                               |
| `pom.xml`                   | Modificado | `source/target` de `25` para `21`                                                                                        |
| `java/README.md`            | Modificado | Documentação das melhorias e como executar                                                                               |
