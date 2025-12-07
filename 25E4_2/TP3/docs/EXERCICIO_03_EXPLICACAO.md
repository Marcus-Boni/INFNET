# Exercício 3: Criação de Objetos Menores Imutáveis e Validação de Dados

## Elemento Menor do Domínio: ProdutoItem

A classe `ProdutoItem` representa um item de produto individual que pode estar contido em um pedido, carrinho de compras, ou estoque.

## Estrutura da Classe

### Atributos

1. **id** (String): Identificador único gerado automaticamente (UUID)
2. **sku** (String): Stock Keeping Unit - código único do produto
3. **nome** (String): Nome descritivo do produto
4. **quantidade** (int): Quantidade do item
5. **precoUnitario** (BigDecimal): Preço por unidade

## Validação de Dados no Construtor

### Regras de Validação Implementadas

#### 1. Campos Obrigatórios (Not Null)

```java
this.sku = Objects.requireNonNull(sku, "SKU não pode ser nulo");
this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
this.precoUnitario = Objects.requireNonNull(precoUnitario, "Preço unitário não pode ser nulo");
```

**Benefício**: Garante que objetos sempre sejam criados em estado válido.

#### 2. Quantidade Não Negativa

```java
if (quantidade < 0) {
    throw new IllegalArgumentException("Quantidade não pode ser negativa");
}
this.quantidade = quantidade;
```

**Benefício**: Previne estados inconsistentes (estoque negativo impossível).

#### 3. Preço Não Negativo

```java
if (precoUnitario.compareTo(BigDecimal.ZERO) < 0) {
    throw new IllegalArgumentException("Preço unitário não pode ser negativo");
}
```

**Benefício**: Evita valores monetários inválidos.

## Garantia de Imutabilidade

### 1. Atributos Final

Todos os atributos são declarados como `final`:

```java
private final String id;
private final String sku;
private final String nome;
private final int quantidade;
private final BigDecimal precoUnitario;
```

### 2. Métodos de "Atualização" que Retornam Novos Objetos

#### atualizarQuantidade(int novaQuantidade)

```java
public ProdutoItem atualizarQuantidade(int novaQuantidade) {
    if (novaQuantidade < 0) {
        throw new IllegalArgumentException("Quantidade não pode ser negativa");
    }
    return new ProdutoItem(this.id, this.sku, this.nome, novaQuantidade, this.precoUnitario);
}
```

**Importante**: Usa construtor privado para manter o mesmo ID.

#### atualizarPreco(BigDecimal novoPreco)

Retorna novo objeto com preço atualizado, mantendo ID original.

#### incrementarQuantidade(int incremento)

Adiciona à quantidade atual retornando novo objeto.

#### decrementarQuantidade(int decremento)

Subtrai da quantidade atual com validação para evitar valores negativos.

## Teste Conceitual de Imutabilidade

### Cenário de Teste 1: Atualização de Quantidade

```
DADO: Um ProdutoItem original com quantidade = 10
QUANDO: Chamo atualizarQuantidade(20)
ENTÃO: 
  - Um novo objeto é retornado com quantidade = 20
  - O objeto original ainda tem quantidade = 10
  - Os IDs são idênticos (mesmo item, versão diferente)
```

**Teste Conceitual**:
1. Criar objeto original e armazenar referência
2. Chamar método de atualização
3. Comparar objeto original com novo objeto
4. Verificar que objeto original não foi modificado
5. Verificar que novo objeto tem os valores atualizados

### Cenário de Teste 2: Atualização de Preço

```
DADO: ProdutoItem com preço = 100.00
QUANDO: Chamo atualizarPreco(150.00)
ENTÃO:
  - Novo objeto com preço = 150.00
  - Objeto original mantém preço = 100.00
  - Todos os outros atributos permanecem iguais
```

### Cenário de Teste 3: Operações Múltiplas

```
DADO: ProdutoItem item1 com quantidade = 5
QUANDO: 
  - item2 = item1.incrementarQuantidade(3)  // quantidade = 8
  - item3 = item2.decrementarQuantidade(2)  // quantidade = 6
ENTÃO:
  - item1 ainda tem quantidade = 5
  - item2 ainda tem quantidade = 8
  - item3 tem quantidade = 6
  - Todos compartilham o mesmo ID
```

## Por que esse Padrão Facilita Auditoria

### 1. Rastreamento de Histórico

Cada modificação cria uma nova versão do objeto:

```
t0: ProdutoItem(id=1, sku="PROD-001", quantidade=10, preco=100.00)
t1: ProdutoItem(id=1, sku="PROD-001", quantidade=15, preco=100.00)  // +5 unidades
t2: ProdutoItem(id=1, sku="PROD-001", quantidade=15, preco=120.00)  // reajuste preço
t3: ProdutoItem(id=1, sku="PROD-001", quantidade=12, preco=120.00)  // -3 unidades
```

**Benefício**: Histórico completo de todas as alterações.

### 2. Event Sourcing

```java
// Cada mudança pode gerar um evento
AuditoriaEvento evento1 = new AuditoriaEvento(
    "QUANTIDADE_ALTERADA", 
    itemOriginal, 
    itemAtualizado,
    timestamp,
    usuario
);
```

**Benefício**: Auditoria automática e completa.

### 3. Comparação de Versões

```java
public DiferencaItem compararVersoes(ProdutoItem versaoAntiga, ProdutoItem versaoNova) {
    List<Alteracao> diferencas = new ArrayList<>();
    
    if (versaoAntiga.getQuantidade() != versaoNova.getQuantidade()) {
        diferencas.add(new Alteracao("quantidade", 
            versaoAntiga.getQuantidade(), 
            versaoNova.getQuantidade()));
    }
    
    if (!versaoAntiga.getPrecoUnitario().equals(versaoNova.getPrecoUnitario())) {
        diferencas.add(new Alteracao("precoUnitario", 
            versaoAntiga.getPrecoUnitario(), 
            versaoNova.getPrecoUnitario()));
    }
    
    return new DiferencaItem(diferencas);
}
```

## Controle de Concorrência

### 1. Concorrência Otimista

```java
// Thread 1
ProdutoItem item = repositorio.buscar("PROD-001");
ProdutoItem itemAtualizado = item.atualizarQuantidade(15);
repositorio.salvarSeVersaoCorreta(itemAtualizado, item);

// Thread 2 (simultaneamente)
ProdutoItem item2 = repositorio.buscar("PROD-001");
ProdutoItem itemAtualizado2 = item2.atualizarPreco(BigDecimal.valueOf(120));
repositorio.salvarSeVersaoCorreta(itemAtualizado2, item2);

// Repository detecta conflito e resolve ou rejeita
```

### 2. Sem Race Conditions

Como cada thread trabalha com sua própria versão imutável:
- Não há leituras sujas
- Não há atualizações perdidas
- Conflitos são detectáveis e gerenciáveis

### 3. Cache Seguro

```java
// Múltiplos usuários podem ter o mesmo objeto em cache
ProdutoItem itemCache = cache.get("PROD-001");

// Thread 1 faz alteração
ProdutoItem novo1 = itemCache.atualizarQuantidade(20);

// Thread 2 simultaneamente
ProdutoItem novo2 = itemCache.atualizarPreco(BigDecimal.valueOf(150));

// itemCache permanece inalterado
// Cada thread tem sua própria versão
```

## Vantagens para Múltiplos Usuários Simultâneos

### 1. Isolamento de Transações

Cada usuário trabalha com sua cópia imutável, sem interferência.

### 2. Detecção de Conflitos

Sistema pode comparar versões e identificar mudanças conflitantes.

### 3. Rollback Simplificado

Fácil reverter para versão anterior (basta manter referência).

### 4. Auditoria Granular

```
Usuário A, 10:00: Alterou quantidade de 10 para 15
Usuário B, 10:01: Alterou preço de 100.00 para 120.00
Usuário A, 10:02: Alterou quantidade de 15 para 12
```

### 5. Compliance e Regulamentação

- Trilha de auditoria completa
- Impossível alterar histórico
- Rastreabilidade total de mudanças
- Atende requisitos de SOX, GDPR, LGPD, etc.

## Conclusão

A imutabilidade de objetos menores como `ProdutoItem`:
- Facilita auditoria através de versionamento natural
- Melhora controle de concorrência eliminando condições de corrida
- Simplifica testes e debugging
- Aumenta confiabilidade em ambientes multi-usuário
- Permite implementação de padrões avançados como Event Sourcing e CQRS
