# Exercício 2: Refatoração de Métodos e Controle de Efeitos Colaterais

## Método Identificado: calcularTotal()

O método `calcularTotal()` foi implementado em duas versões para demonstrar o problema dos efeitos colaterais e sua solução.

## Versão Problemática: CalculadoraPedidoComEfeitosColaterais

### Código Problemático

```java
public BigDecimal calcularTotal(List<ItemPedido> itens, DescontoAplicavel desconto) {
    BigDecimal total = calcularSubtotal(itens);
    
    if (desconto != null) {
        desconto.setValorDesconto(total.multiply(desconto.getPercentual()).divide(BigDecimal.valueOf(100)));
        desconto.setAplicado(true);
        total = total.subtract(desconto.getValorDesconto());
    }
    
    return total;
}
```

### Por que essa Implementação é Problemática

#### 1. Efeitos Colaterais Não Previstos

O método altera diretamente o objeto `desconto` passado como parâmetro:
- Define `valorDesconto`
- Marca `aplicado` como `true`

Isso viola o princípio de que métodos devem ter comportamento previsível e explícito.

#### 2. Dificuldade de Teste

```java
// Problema em testes
Desconto desconto = new Desconto(BigDecimal.valueOf(10));
calculadora.calcularTotal(itens, desconto);
// desconto foi modificado! Testes subsequentes podem falhar
```

#### 3. Risco de Inconsistência com Múltiplos Acessos

Cenário problemático:
```java
// Thread 1
Desconto descontoCompartilhado = new Desconto(BigDecimal.valueOf(10));
BigDecimal total1 = calculadora.calcularTotal(itens1, descontoCompartilhado);

// Thread 2 (simultaneamente)
BigDecimal total2 = calculadora.calcularTotal(itens2, descontoCompartilhado);

// descontoCompartilhado pode ter estado inconsistente
// valorDesconto pode ser de itens1 ou itens2
// aplicado pode mudar entre as operações
```

#### 4. Violação do Princípio de Responsabilidade Única

O método faz duas coisas:
- Calcula o total
- Modifica o estado do desconto

#### 5. Acoplamento Temporal

O objeto `desconto` só é válido APÓS a chamada do método, criando dependência temporal.

## Versão Refatorada: CalculadoraPedidoRefatorada

### Código Refatorado

```java
public ResultadoCalculo calcularTotal(List<ItemPedido> itens, Desconto desconto) {
    BigDecimal subtotal = calcularSubtotal(itens);
    
    if (desconto == null) {
        return new ResultadoCalculo(subtotal, BigDecimal.ZERO, subtotal, null);
    }
    
    BigDecimal valorDesconto = calcularValorDesconto(subtotal, desconto);
    BigDecimal total = subtotal.subtract(valorDesconto);
    
    return new ResultadoCalculo(subtotal, valorDesconto, total, desconto);
}
```

### Melhorias Implementadas

#### 1. Sem Efeitos Colaterais

- O método não altera nenhum parâmetro de entrada
- `Desconto` é imutável
- Toda informação é retornada em um novo objeto `ResultadoCalculo`

#### 2. Retorno Explícito e Completo

A classe `ResultadoCalculo` encapsula todas as informações:
- `subtotal`: Valor antes do desconto
- `valorDesconto`: Valor do desconto aplicado
- `total`: Valor final
- `descontoAplicado`: Referência ao desconto (imutável)

#### 3. Testabilidade Aprimorada

```java
@Test
void deveCalcularTotalComDesconto() {
    Desconto desconto = new Desconto(BigDecimal.TEN, "DESC10", "10% off");
    ResultadoCalculo resultado = calculadora.calcularTotal(itens, desconto);
    
    // desconto permanece inalterado
    // resultado contém todas as informações
    assertEquals(esperado, resultado.getTotal());
}
```

#### 4. Thread-Safety

```java
// Thread 1
Desconto desconto = new Desconto(BigDecimal.TEN, "DESC10", "10% off");
ResultadoCalculo resultado1 = calculadora.calcularTotal(itens1, desconto);

// Thread 2 (simultaneamente)
ResultadoCalculo resultado2 = calculadora.calcularTotal(itens2, desconto);

// desconto permanece inalterado
// Cada thread tem seu próprio ResultadoCalculo
// Sem condições de corrida
```

## Benefícios da Refatoração

### 1. Confiabilidade do Sistema

- Comportamento determinístico e previsível
- Redução de bugs relacionados a estados compartilhados
- Facilita rastreamento de problemas

### 2. Melhor Performance em Cenários Concorrentes

- Sem necessidade de sincronização
- Objetos imutáveis podem ser compartilhados livremente
- Reduz contenção de recursos

### 3. Composabilidade

```java
ResultadoCalculo resultado = calculadora.calcularTotal(itens, desconto);

// Pode ser facilmente composto com outras operações
RelatorioVenda relatorio = gerarRelatorio(resultado);
NotaFiscal nf = emitirNota(resultado);
```

### 4. Auditoria e Logging

```java
ResultadoCalculo resultado = calculadora.calcularTotal(itens, desconto);

// Todas as informações disponíveis para log
logger.info("Cálculo realizado: {}", resultado);
// Subtotal: 100.00, Desconto: 10.00, Total: 90.00
```

### 5. Alto Volume de Operações Simultâneas

Em sistemas com alta concorrência:
- Múltiplas requisições podem usar o mesmo objeto `Desconto`
- Cada requisição obtém seu próprio `ResultadoCalculo`
- Sem locks ou sincronização
- Escalabilidade horizontal facilitada

## Conclusão

A refatoração eliminou efeitos colaterais transformando:
- Objetos mutáveis em imutáveis
- Modificações in-place em criação de novos objetos
- Estado compartilhado em dados isolados

Isso resulta em código mais confiável, testável e adequado para ambientes concorrentes.
