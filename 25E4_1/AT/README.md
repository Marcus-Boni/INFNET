# Engenharia de Testes de Software - AT

## Estrutura do Projeto

```
AT/
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── br/edu/infnet/
│   │           ├── exercicio1/      # Calculadora IMC
│   │           ├── exercicio2/      # Testes baseados em propriedades
│   │           ├── exercicio3/      # Cliente API ViaCEP
│   │           ├── exercicio4/      # (Testes Selenium)
│   │           └── exercicio5/      # Algoritmos de ordenação
│   └── test/
│       └── java/
│           └── br/edu/infnet/
│               ├── exercicio1/      # Testes IMC
│               ├── exercicio2/      # Testes com jqwik
│               ├── exercicio3/      # Testes API
│               ├── exercicio4/      # Testes Selenium (Page Objects)
│               └── exercicio5/      # Testes de cobertura
└── README.md
```

## Exercícios

### Exercício 1 - Teste Exploratório e Análise de Comportamento Esperado
**Localização:** `src/main/java/br/edu/infnet/exercicio1/`

Sistema de cálculo de IMC (Índice de Massa Corporal) com:
- Especificação funcional completa
- Testes de valor limite
- Testes de partições de equivalência
- Análise de falhas e comportamentos inesperados

**Executar:** 
```bash
mvn test -Dtest=CalculoIMCTest
```

### Exercício 2 - Testes Baseados em Propriedades e Simulação de Dependências
**Localização:** `src/main/java/br/edu/infnet/exercicio2/`

Biblioteca matemática com testes usando jqwik:
- MultiplyByTwo (sempre retorna par)
- GenerateMultiplicationTable (todos múltiplos do número)
- IsPrime (validação de números primos)
- CalculateAverage (média entre min e max)
- Interface MathLogger para injeção de dependência

**Executar:**
```bash
mvn test -Dtest=MathFunctionsPropertyTest
```

### Exercício 3 - Teste de API: Funcionalidade, Robustez e Estratégia de Teste
**Localização:** `src/main/java/br/edu/infnet/exercicio3/`

Cliente para API ViaCEP com:
- Testes de entrada inválida (CEP com letras, vazio, inválido)
- Testes de consulta por endereço (UF, cidade, logradouro)
- Tabela de decisão para combinações
- Partições de equivalência e valor limite

**Executar:**
```bash
mvn test -Dtest=ViaCepTest
```

### Exercício 4 - Automação de Testes Web com Selenium
**Localização:** `src/test/java/br/edu/infnet/exercicio4/`

Automação de testes para https://automationexercise.com:
- Cadastro de novo usuário
- Login com credenciais válidas/inválidas
- Padrão Page Object Model
- Captura de screenshots em falhas
- Integração com JUnit 5 e WebDriverManager

**Executar:**
```bash
mvn test -Dtest=AutomationExerciseTest
```

**Nota:** Requer Chrome instalado

### Exercício 5 - Análise Estrutural de Código
**Localização:** `src/main/java/br/edu/infnet/exercicio5/`

Testes de cobertura para algoritmos de ordenação:
- BubbleSort
- QuickSort
- Cobertura de decisões e ramificações
- Relatório JaCoCo de cobertura de código

**Executar:**
```bash
mvn test -Dtest=BubbleSortTest,QuickSortTest
mvn jacoco:report
```

**Ver relatório:** `target/site/jacoco/index.html`

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Chrome (para testes Selenium)

## Instalação

```bash
git clone <url-do-repositorio>
cd AT
mvn clean install
```

## Executar Todos os Testes

```bash
mvn clean test
```

## Gerar Relatório de Cobertura

```bash
mvn clean test jacoco:report
```

O relatório será gerado em `target/site/jacoco/index.html`

## Tecnologias Utilizadas

- **Java 17**: Linguagem principal
- **Maven**: Gerenciamento de dependências e build
- **JUnit 5**: Framework de testes unitários
- **jqwik**: Testes baseados em propriedades
- **Selenium WebDriver**: Automação de testes web
- **WebDriverManager**: Gerenciamento automático de drivers
- **Apache HttpClient 5**: Cliente HTTP para API
- **Gson**: Parsing JSON
- **JaCoCo**: Cobertura de código
- **Mockito**: Mocking (preparado para uso futuro)

## Dependências Principais

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
    </dependency>
    <dependency>
        <groupId>net.jqwik</groupId>
        <artifactId>jqwik</artifactId>
        <version>1.8.2</version>
    </dependency>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.16.1</version>
    </dependency>
</dependencies>
```

## Notas

- **Exercício 1**: Código original do repositório Wolfterro/Projetos-em-Java
- **Exercício 2**: Refatoração incluindo interface MathLogger para injeção de dependência
- **Exercício 3**: Testes reais contra API ViaCEP (requer conexão internet)
- **Exercício 4**: Testes podem falhar se o site estiver indisponível
- **Exercício 5**: Código baseado em TheAlgorithms/Java

## Troubleshooting

### Testes Selenium falhando
```bash
mvn dependency:purge-local-repository -DmanualInclude=org.seleniumhq.selenium:selenium-java
```

### Erro de versão Java
Verifique se JAVA_HOME aponta para Java 17+:
```bash
java -version
```

### Testes API ViaCEP timeout
Verifique conexão internet ou aguarde alguns segundos entre execuções.