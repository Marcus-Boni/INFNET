# TP5 - Entrega Final com CI/CD, Seguranca e Deploy Automatizado

[![TP5 CI/CD](https://github.com/Marcus-Boni/INFNET/actions/workflows/tp5-ci-cd.yml/badge.svg)](https://github.com/Marcus-Boni/INFNET/actions/workflows/tp5-ci-cd.yml)
[![TP5 SAST CodeQL](https://github.com/Marcus-Boni/INFNET/actions/workflows/tp5-sast-codeql.yml/badge.svg)](https://github.com/Marcus-Boni/INFNET/actions/workflows/tp5-sast-codeql.yml)

Este TP5 consolida o sistema integrado de produtos, finalizando a evolucao iniciada no TP4 com foco em:

- Refatoracao final orientada a imutabilidade, clareza e coesao.
- Pipeline CI/CD completo com build, testes, cobertura, SAST, DAST e deploy por ambiente.
- Testes Selenium de validacao pos-deploy.
- Logs personalizados e resumos em Markdown para depuracao rapida no GitHub Actions.

## 1. Arquitetura Final

### 1.1 Camadas e responsabilidades

- `controller` e `controller/api`: interfaces MVC e REST.
- `service`: orquestracao transacional.
- `service/validation`: regras de validacao de negocio.
- `service/sanitization`: higienizacao de entrada.
- `service/mutation`: aplicacao de alteracoes no agregado.
- `service/support`: objetos de valor imutaveis (`TermoBusca`, `ProdutoCollection`, `ProdutoComando`).

### 1.2 Ajustes finais de refatoracao

- Entrada de dados de negocio agora segue objeto imutavel (`ProdutoComando`) antes de persistencia.
- Sanitizacao nao muta mais o objeto recebido pelo controller.
- Validacao principal passou a operar em comando imutavel.
- Update do agregado foi centralizado no `ProdutoMutator` com dados sanitizados/validados.

## 2. CI/CD no GitHub Actions

### 2.1 Workflows

- `.github/workflows/tp5-ci-cd.yml`
- `.github/workflows/tp5-sast-codeql.yml`

### 2.2 O que o pipeline executa

1. Build Maven (`mvn clean verify`).
2. Testes automatizados (unitarios, integracao e Selenium existentes).
3. Gate de cobertura JaCoCo.
4. Upload de artefatos (jar, surefire, jacoco).
5. Deploy automatizado por ambiente (`tp5-dev`, `tp5-test`, `tp5-prod`).
6. Testes Selenium pos-deploy com tag `post-deploy`.
7. DAST com OWASP ZAP baseline.
8. SAST com CodeQL (workflow dedicado).

### 2.3 Gatilhos configurados

- `push` na branch `main`.
- `pull_request`.
- `release` publicada.
- `workflow_dispatch` com escolha de ambiente e opcao de DAST.

## 3. Ambientes de Deploy

Ambientes declarados no workflow:

- `tp5-dev`
- `tp5-test`
- `tp5-prod`

Sugestao de protecao no GitHub Environments:

1. Exigir aprovadores para `tp5-prod`.
2. Restringir branch para deploy em producao (`main` e `release/*`).
3. Definir secrets por ambiente.

Deploy remoto simples e gratuito (opcional) via hooks:

- `RENDER_DEPLOY_HOOK_DEV`
- `RENDER_DEPLOY_HOOK_TEST`
- `RENDER_DEPLOY_HOOK_PROD`

Quando nao houver hook remoto, o pipeline faz deploy ephemeral no proprio runner para validar integridade com Selenium.

## 4. Seguranca (SAST/DAST)

### 4.1 SAST

- CodeQL para Java (`tp5-sast-codeql.yml`).
- Resultado na aba `Security` do repositorio.

### 4.2 DAST

- OWASP ZAP baseline no job `dast` do workflow principal.
- Relatorios publicados como artefato (`tp5-dast-reports`).

### 4.3 OIDC

- Jobs de deploy usam permissao `id-token: write`.
- Pronto para integracao segura com provedor cloud via federacao OIDC (conforme configuracao institucional).

## 5. Logs e Depuracao

Cada job publica:

- Logs agrupados com `::group::`.
- Resumo do resultado no `GITHUB_STEP_SUMMARY`.
- Artefatos de suporte para troubleshooting (`app-*.log`, `surefire-reports`, `zap-report`).

## 6. Execucao Local

### 6.1 Build e testes

```bash
cd 25E4_5/TP5
mvn clean verify
```

### 6.2 Rodar aplicacao

```bash
cd 25E4_5/TP5
mvn spring-boot:run
```

Aplicacao: `http://localhost:8080/produtos`

### 6.3 Testes Selenium pos-deploy (manual)

```bash
cd 25E4_5/TP5
mvn test -Dtest.groups=post-deploy -Dtest.excludedGroups= -Dtp5.base.url=http://127.0.0.1:8080
```

### 6.4 Deploy local por ambiente com Docker

```bash
cd 25E4_5/TP5

docker compose --profile dev up -d --build
# app em http://127.0.0.1:8081/produtos

docker compose --profile test up -d --build
# app em http://127.0.0.1:8082/produtos

docker compose --profile prod up -d --build
# app em http://127.0.0.1:8083/produtos
```

## 7. Evidencias para Entrega

- Workflow CI/CD com jobs de build, teste, deploy, DAST e pos-deploy Selenium.
- Workflow SAST CodeQL.
- Documentacao tecnica final em `docs/ENTREGA_FINAL_TP5.md`.
- Logs e resumos por job diretamente na interface do GitHub Actions.
