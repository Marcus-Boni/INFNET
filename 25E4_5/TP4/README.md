# TP4 - Refatoracao, Integracao e CI/CD

Este TP4 foi construido a partir do TP3 e evolui o sistema para um modelo mais manutenivel, com integracao entre dois canais de uso (MVC e API), build em Maven, cobertura minima obrigatoria e pipeline no GitHub Actions.

## 1) Principais Mudancas de Refatoracao

### 1.1 Organizacao por responsabilidade (SRP)

O antigo `ProdutoService` foi dividido em componentes menores:

- `ProdutoValidador`: validacoes de negocio (fail-early)
- `ProdutoSanitizador`: higienizacao de entradas
- `ProdutoMutator`: aplicacao de alteracoes de estado na entidade
- `ProdutoService`: orquestracao transacional e persistencia

### 1.2 Abstracao reutilizavel

Foi criada a interface `ProdutoCatalogo` para abstrair operacoes comuns de catalogo, permitindo reuso entre canais distintos do sistema.

### 1.3 Substituicao de primitivo por objeto de valor

A busca por nome ganhou objeto dedicado:

- `TermoBusca`: encapsula normalizacao e regra de tamanho maximo

### 1.4 Encapsulamento de colecao + imutabilidade

- `ProdutoCollection` encapsula `List<Produto>` de forma imutavel para reduzir acoplamento.

## 2) Integracao dos Sistemas (TP3 + TP4)

A integracao foi feita mantendo uma base unica de dados e regras compartilhadas:

- Canal 1: interface web MVC (`/produtos`)
- Canal 2: API REST integrada (`/api/produtos`)

Ambos usam a mesma abstracao de catalogo (`ProdutoCatalogo`) e a mesma persistencia, garantindo consistencia.

### Endpoints da API integrada

- `GET /api/produtos`
- `GET /api/produtos/{id}`
- `POST /api/produtos`

## 3) Build e Testes com Maven

### Pre-requisitos

- Java 21
- Maven 3.9+

### Executar localmente

```bash
cd TP4
mvn spring-boot:run
```

Aplicacao: `http://localhost:8080/produtos`

### Executar testes e cobertura

```bash
cd TP4
mvn clean verify
```

Relatorios:

- Testes: `TP4/target/surefire-reports`
- Cobertura JaCoCo (HTML): `TP4/target/site/jacoco/index.html`
- XML cobertura: `TP4/target/site/jacoco/jacoco.xml`

### Gate de cobertura minima

O build falha automaticamente se cobertura de linhas ficar abaixo de `85%`.

## 4) Workflows GitHub Actions

Arquivo principal:

- `.github/workflows/tp4-ci.yml`

### Triggers configurados

- `push` (somente alteracoes em TP4)
- `pull_request` (somente alteracoes em TP4)
- `workflow_dispatch` (execucao manual)

### O que o workflow executa

1. Checkout do repositorio
2. Setup Java 21
3. Cache e setup de dependencias Maven
4. Build + testes + cobertura (`mvn -B clean verify --no-transfer-progress`)
5. Upload de artefatos (relatorios de testes e cobertura)

## 5) Runners: Hosted vs Self-Hosted

### Escolha padrao

Foi adotado `ubuntu-latest` (GitHub-hosted) como padrao para maior previsibilidade e simplicidade de manutencao.

### Opcao self-hosted

No `workflow_dispatch` existe a entrada booleana `runSelfHosted`.
Quando habilitada, o job `self-hosted-validation` executa em `runs-on: self-hosted`.

Isso permite comparar desempenho/custo sem perder o baseline confiavel do runner hospedado pelo GitHub.

## 6) Evidencias de Integracao e Regressao

Novos testes adicionados:

- `SistemasIntegradosConsistencyTest`
  - produto criado via API aparece na listagem MVC
  - produto criado via MVC aparece na API
  - atualizacao preserva integridade no repositorio compartilhado
- `TermoBuscaTest`
  - valida comportamento do objeto de valor de busca

Os testes legados do TP3 foram preservados e adaptados ao novo desenho interno.

## 7) Estrutura (resumo)

```text
TP4/
  pom.xml
  src/main/java/org/example/
    controller/
    controller/api/
    service/
    service/catalogo/
    service/mutation/
    service/sanitization/
    service/support/
    service/validation/
  src/test/java/org/example/
    unit/
    integration/
```

## 8) Notas de Depuracao

- O pipeline publica artefatos de teste e cobertura para facilitar analise de falhas na aba Actions.
- Para diagnostico mais detalhado em CI, execute localmente `mvn -e -X clean verify`.
