# Exercício 4: Escolha de Tipos de Dados e Clareza na Modelagem

## Classe Implementada: RegistroTransacao

A classe `RegistroTransacao` representa um registro de transação financeira ou comercial em um sistema.

## Atributos e Tipos de Dados Escolhidos

### 1. identificador (String - UUID)

```java
private final String identificador;

// Inicialização
this.identificador = UUID.randomUUID().toString();
```

#### Por que String (UUID)?

**Vantagens**:
- **Unicidade global**: UUID garante identificadores únicos sem coordenação centralizada
- **Distribuição**: Permite geração em múltiplos nós sem conflitos
- **Segurança**: Não sequencial, dificulta enumeração e previsão
- **Compatibilidade**: Funciona bem com diferentes bancos de dados e sistemas
- **Portabilidade**: Fácil serialização e transmissão

**Alternativas rejeitadas**:
- `Long`: Sequencial, requer coordenação, revela volume de dados
- `Integer`: Limitado, pode esgotar range
- `Custom ID`: Mais complexo, menos padrão

---

### 2. valorMonetario (BigDecimal)

```java
private final BigDecimal valorMonetario;
```

#### Por que BigDecimal?

**Vantagens CRÍTICAS**:

1. **Precisão Decimal Exata**
   - `double` e `float` usam ponto flutuante binário
   - Causam erros de arredondamento em valores decimais
   
   ```java
   // PROBLEMA com double:
   double valor1 = 0.1 + 0.2;  // Resultado: 0.30000000000000004 ❌
   
   // CORRETO com BigDecimal:
   BigDecimal valor2 = new BigDecimal("0.1").add(new BigDecimal("0.2"));
   // Resultado: 0.3 ✓
   ```

2. **Cálculos Financeiros Precisos**
   ```java
   // Cálculo de juros, taxas, impostos
   BigDecimal principal = new BigDecimal("1000.00");
   BigDecimal taxa = new BigDecimal("0.05");  // 5%
   BigDecimal juros = principal.multiply(taxa);
   // Resultado: 50.00 (exato)
   ```

3. **Controle de Arredondamento**
   ```java
   BigDecimal resultado = valor1.divide(valor2, 2, RoundingMode.HALF_UP);
   // Controle explícito de casas decimais e modo de arredondamento
   ```

4. **Conformidade Legal**
   - Normas contábeis exigem precisão decimal
   - Auditoria e compliance requerem cálculos exatos
   - Evita problemas legais com discrepâncias monetárias

**Exemplo de Problema com double**:
```java
// Simulação de 1000 transações de R$ 0.10
double total = 0.0;
for (int i = 0; i < 1000; i++) {
    total += 0.10;
}
// Esperado: 100.00
// Real: 99.99999999999986 ❌
// Diferença: R$ 0.00000000000014 por transação
// Em milhões de transações: prejuízo significativo
```

---

### 3. quantidade (int)

```java
private final int quantidade;

if (quantidade < 0) {
    throw new IllegalArgumentException("Quantidade não pode ser negativa");
}
```

#### Por que int?

**Vantagens**:
- **Adequado ao domínio**: Quantidades são números inteiros
- **Performance**: Tipo primitivo, mais eficiente que Integer
- **Range suficiente**: -2,147,483,648 a 2,147,483,647
- **Semântica clara**: Não faz sentido 2.5 itens em contexto de transação

**Quando usar long**:
- Sistemas com volumes muito altos (> 2 bilhões)
- Agregações que podem exceder limite de int

**Por que não double**:
- Quantidade é conceito discreto, não contínuo
- Evita problemas de arredondamento desnecessários

---

### 4. dataCriacao e dataAtualizacao (LocalDateTime)

```java
private final LocalDateTime dataCriacao;
private final LocalDateTime dataAtualizacao;

this.dataCriacao = LocalDateTime.now();
this.dataAtualizacao = LocalDateTime.now();
```

#### Por que LocalDateTime?

**Vantagens sobre Date (java.util.Date)**:

1. **API Moderna e Fluente** (Java 8+)
   ```java
   LocalDateTime agora = LocalDateTime.now();
   LocalDateTime amanha = agora.plusDays(1);
   LocalDateTime mesPassado = agora.minusMonths(1);
   ```

2. **Imutabilidade**
   - `Date` é mutável (pode causar bugs)
   - `LocalDateTime` é imutável (thread-safe)

3. **Clareza Semântica**
   - `LocalDateTime`: data e hora sem timezone
   - `ZonedDateTime`: quando timezone é importante
   - `LocalDate`: apenas data
   - `LocalTime`: apenas hora

4. **Precisão e Legibilidade**
   ```java
   LocalDateTime dt = LocalDateTime.of(2024, 12, 7, 14, 30, 0);
   // vs
   Date date = new Date(124, 11, 7, 14, 30, 0);  // Confuso: ano desde 1900, mês 0-based
   ```

5. **Operações Intuitivas**
   ```java
   Duration duracao = Duration.between(dataCriacao, dataAtualizacao);
   Period periodo = Period.between(dataCriacao.toLocalDate(), dataAtualizacao.toLocalDate());
   ```

**Por que LocalDateTime e não ZonedDateTime?**
- Para dados internos do sistema sem necessidade de timezone específico
- Se o sistema opera em múltiplos fusos horários, usar `ZonedDateTime`

**Exemplo de uso**:
```java
// Auditoria
if (dataAtualizacao.isAfter(dataCriacao.plusMinutes(5))) {
    logger.warn("Transação levou mais de 5 minutos para atualizar");
}
```

---

### 5. status (Enum StatusTransacao)

```java
private final StatusTransacao status;

public enum StatusTransacao {
    PENDENTE,
    PROCESSANDO,
    APROVADA,
    RECUSADA,
    CANCELADA,
    ESTORNADA
}
```

#### Por que Enum?

**Vantagens sobre String**:

1. **Type Safety (Segurança de Tipo)**
   ```java
   // Com String: ❌
   transacao.setStatus("APROVIADA");  // Typo não detectado em compile-time
   
   // Com Enum: ✓
   transacao.setStatus(StatusTransacao.APROVADA);  // Erro de compilação se errado
   ```

2. **Autocomplete e IDE Support**
   - IDE sugere valores válidos
   - Refactoring seguro (renomear afeta todo código)

3. **Validação Automática**
   - Impossível criar status inválido
   - Não precisa validar strings

4. **Performance**
   - Enums são mais eficientes que Strings
   - Comparação por referência (`==`) ao invés de `equals()`

5. **Documentação Implícita**
   ```java
   // Todos os status possíveis estão centralizados no enum
   // Desenvolvedores sabem exatamente quais valores são válidos
   ```

6. **Comportamento Associado**
   ```java
   public enum StatusTransacao {
       PENDENTE {
           public boolean podeProcessar() { return true; }
       },
       APROVADA {
           public boolean podeEstornar() { return true; }
       };
       
       public abstract boolean podeProcessar();
       public abstract boolean podeEstornar();
   }
   ```

**Vantagens sobre Integer/Códigos**:
- Mais legível: `APROVADA` vs `3`
- Auto-documentado
- Sem magic numbers

---

### 6. categoria (Enum CategoriaTransacao)

```java
private final CategoriaTransacao categoria;

public enum CategoriaTransacao {
    VENDA,
    COMPRA,
    TRANSFERENCIA,
    PAGAMENTO,
    RECEBIMENTO,
    REEMBOLSO
}
```

#### Por que Enum?

Mesmas vantagens do `StatusTransacao`, além de:

1. **Agrupamento Lógico**
   ```java
   public enum CategoriaTransacao {
       VENDA(TipoFluxo.ENTRADA),
       COMPRA(TipoFluxo.SAIDA),
       TRANSFERENCIA(TipoFluxo.NEUTRO);
       
       private final TipoFluxo fluxo;
       
       public boolean isEntrada() {
           return fluxo == TipoFluxo.ENTRADA;
       }
   }
   ```

2. **Extensibilidade Controlada**
   - Adicionar nova categoria é explícito
   - Compilador força atualização de switches

3. **Consistência**
   - Garante mesma nomenclatura em todo sistema
   - Evita variações: "VENDA" vs "venda" vs "Venda"

---

### 7. descricao (String)

```java
private final String descricao;

this.descricao = Objects.requireNonNull(descricao, "Descrição não pode ser nula");
```

#### Por que String?

- **Flexibilidade**: Texto livre, variável por natureza
- **Padrão**: Tipo universal para texto
- **Validação**: Validação de nulidade suficiente

**Considerações**:
- Para textos longos: considerar `TEXT` em banco de dados
- Para descrições padronizadas: considerar Enum

---

## Resumo: Como a Escolha Facilita Legibilidade e Manutenção

### 1. Legibilidade

```java
// Código auto-explicativo
RegistroTransacao transacao = new RegistroTransacao(
    new BigDecimal("150.50"),    // Obviamente um valor monetário
    10,                          // Quantidade inteira
    StatusTransacao.APROVADA,    // Status claro e válido
    CategoriaTransacao.VENDA,    // Categoria explícita
    "Venda de produto X"         // Descrição textual
);
```

### 2. Manutenção

- **Mudanças são localizadas**: Adicionar status = apenas atualizar enum
- **Refactoring seguro**: IDE detecta todos os usos
- **Menos bugs**: Type system previne erros comuns

### 3. Documentação Viva

Os tipos escolhidos documentam o código:
- `BigDecimal` → "Isso é dinheiro, preciso de precisão"
- `LocalDateTime` → "Isso é data/hora sem timezone"
- `StatusTransacao` → "Apenas estes valores são válidos"

### 4. Prevenção de Bugs

- BigDecimal previne erros de arredondamento
- Enum previne valores inválidos
- int previne quantidades decimais
- LocalDateTime previne bugs de timezone

### 5. Performance

- Tipos apropriados são mais eficientes
- Enums mais rápidos que Strings
- int mais rápido que BigDecimal (quando apropriado)
- BigDecimal onde precisão é necessária

## Conclusão

A escolha cuidadosa de tipos de dados:
- **Expressa intenção**: Código auto-documentado
- **Previne erros**: Type safety e validação
- **Facilita manutenção**: Refactoring seguro
- **Melhora legibilidade**: Semântica clara
- **Garante precisão**: Especialmente em cálculos financeiros
- **Aumenta confiabilidade**: Menos bugs em produção
