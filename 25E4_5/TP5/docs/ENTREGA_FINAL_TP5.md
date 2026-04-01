# Entrega Final - TP5

## 1. Visao Geral

O TP5 encerra o ciclo do projeto integrado com foco em entrega formal, automacao completa e suporte operacional para manutencao futura. A solucao foi organizada para garantir rastreabilidade de build, seguranca, deploy e validacao funcional pos-deploy.

## 2. Arquitetura Final do Sistema

### 2.1 Componentes principais

- Aplicacao Spring Boot monolitica modular.
- Canais integrados:
  - MVC (`/produtos`)
  - REST (`/api/produtos`)
- Persistencia H2 para execucao local e pipelines.

### 2.2 Estrutura de codigo

- `ProdutoService` manteve orquestracao transacional.
- `ProdutoValidador` aplica regras de negocio.
- `ProdutoSanitizador` higieniza entrada.
- `ProdutoMutator` aplica mudancas de estado.
- `ProdutoComando` introduz fluxo imutavel para entrada.

### 2.3 Principios aplicados

- Fail-early em validacoes.
- Encapsulamento de colecoes e objetos de valor.
- Reducao de mutabilidade no fluxo de escrita.
- Separacao clara entre consulta e modificacao.

## 3. Workflows e Automacao

## 3.1 Workflow principal

Arquivo: `.github/workflows/tp5-ci-cd.yml`

Jobs implementados:

1. `ci`: build, testes, cobertura e artefatos.
2. `deploy-dev`: deploy e validacao pos-deploy em `tp5-dev`.
3. `deploy-test`: deploy e validacao pos-deploy em `tp5-test`.
4. `dast`: analise dinamica com OWASP ZAP.
5. `deploy-prod`: deploy final em `tp5-prod` (release/manual).

Triggers:

- Push em `main`
- Pull Request
- Release publicada
- Workflow manual com selecao de ambiente

## 3.2 Workflow de seguranca estatica

Arquivo: `.github/workflows/tp5-sast-codeql.yml`

- Inicializacao CodeQL para Java.
- Build para indexacao.
- Publicacao de achados na aba Security.

## 4. Estrategia de Deploy por Ambiente

Ambientes declarados:

- `tp5-dev`
- `tp5-test`
- `tp5-prod`

Modelo adotado:

- Deploy ephemeral no runner para validacao automatizada confiavel.
- Trigger opcional para deploy remoto gratuito via hooks de Render.
- Permissao `id-token: write` habilitada para federacao OIDC com cloud provider.

Recomendacoes operacionais:

1. Proteger `tp5-prod` com aprovacao manual.
2. Limitar producao a branches controladas.
3. Isolar secrets por ambiente.

## 5. Testes e Qualidade

### 5.1 Build e testes de regressao

- Unitarios, integracao e Selenium de interface.
- Cobertura minima por JaCoCo.

### 5.2 Testes pos-deploy

Classe dedicada:

- `PostDeploySmokeSeleniumTest`

Validacoes:

1. Disponibilidade da listagem apos deploy.
2. Fluxo de cadastro funcional apos deploy.

### 5.3 Seguranca

- SAST: CodeQL.
- DAST: OWASP ZAP Baseline.

## 6. Monitoramento e Depuracao

Padrao de observabilidade no pipeline:

- Logs customizados por contexto de job.
- Agrupamento visual com `::group::`.
- Sumario em Markdown no `GITHUB_STEP_SUMMARY`.
- Upload de artefatos para analise forense (`app-*.log`, surefire, jacoco, zap).

## 7. Resultado da Entrega

O sistema foi finalizado com:

- Codigo organizado com foco em modularidade, imutabilidade e manutencao.
- CI/CD completo com seguranca e deploy automatizado.
- Validacao funcional pos-deploy com Selenium.
- Documentacao formal de arquitetura, operacao e estrategia de qualidade.
