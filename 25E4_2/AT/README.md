# Sistema de Logística - Refatoração

Sistema refatorado de gerenciamento de entregas aplicando princípios de Clean Code e Engenharia de Software.

## Estrutura do Projeto

```
src/
├── main/java/br/edu/infnet/logistica/
│   ├── dominio/
│   │   ├── Entrega.java
│   │   └── TipoFrete.java
│   ├── servico/
│   │   ├── EtiquetaService.java
│   │   ├── PromocaoService.java
│   │   └── frete/
│   │       ├── CalculadoraFrete.java
│   │       ├── FreteExpresso.java
│   │       ├── FretePadrao.java
│   │       ├── FreteEconomico.java
│   │       └── FabricaCalculadoraFrete.java
│   └── excecao/
│       └── EntregaInvalidaException.java
└── test/java/br/edu/infnet/logistica/
    ├── dominio/
    │   └── EntregaTest.java
    └── servico/
        ├── EtiquetaServiceTest.java
        ├── PromocaoServiceTest.java
        └── frete/
            ├── FreteExpressoTest.java
            ├── FretePadraoTest.java
            ├── FreteEconomicoTest.java
            └── FabricaCalculadoraFreteTest.java
```

## Requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

## Compilação e Testes

### Compilar o projeto
```bash
mvn clean compile
```

### Executar testes
```bash
mvn test
```

### Gerar pacote
```bash
mvn package
```

## Principais Melhorias

1. **Encapsulamento**: Classe Entrega imutável com validações
2. **Separação de Responsabilidades**: Camadas de domínio, serviço e exceção
3. **Padrão Strategy**: Implementações polimórficas de cálculo de frete
4. **Extensibilidade**: Novos tipos de frete sem modificar código existente
5. **Validações Robustas**: Exceções personalizadas e fail-fast
6. **Testes Automatizados**: Cobertura completa de funcionalidades

## Documentação

Consulte `RELATORIO_TECNICO.md` para análise detalhada da refatoração, decisões de design e justificativas técnicas.
