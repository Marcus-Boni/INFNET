# Exercício 1: Modelagem e Imutabilidade de uma Entidade Central

## Entidade Escolhida: Pedido

A classe `Pedido` representa uma entidade central no contexto de um sistema de e-commerce ou gestão de vendas.

## Características da Implementação

### 1. Atributos Imutáveis (private final)

Todos os atributos da classe `Pedido` foram declarados como `private final`:

- `id`: Identificador único do pedido
- `dataCriacao`: Data e hora de criação do pedido
- `status`: Status atual do pedido (enum StatusPedido)
- `clienteId`: Identificador do cliente
- `itens`: Lista de itens do pedido (cópia defensiva)
- `dataAtualizacao`: Data e hora da última atualização

### 2. Ausência de Setters

A classe não possui métodos setters. Qualquer modificação retorna uma nova instância do objeto.

### 3. Métodos que Retornam Novas Instâncias

#### `atualizarStatus(StatusPedido novoStatus)`
Retorna um novo objeto `Pedido` com o status atualizado, preservando todos os outros atributos e atualizando o timestamp.

#### `adicionarItem(ItemPedido item)`
Retorna um novo objeto `Pedido` com o item adicionado à lista, mantendo a instância original inalterada.

#### `removerItem(String itemId)`
Retorna um novo objeto `Pedido` com o item removido, garantindo que sempre haja ao menos um item.

## Por que essa Abordagem Evita Problemas de Concorrência

### 1. Thread-Safety Inerente
- Objetos imutáveis são naturalmente thread-safe
- Não há necessidade de sincronização ou locks
- Múltiplas threads podem acessar o mesmo objeto simultaneamente sem riscos

### 2. Eliminação de Condições de Corrida (Race Conditions)
- Como o estado não pode ser modificado, não há possibilidade de uma thread alterar dados enquanto outra os lê
- Não existe o problema de "leitura suja" (dirty reads)

### 3. Consistência Garantida
- O estado do objeto é sempre consistente desde sua criação
- Não há estados intermediários ou parcialmente atualizados
- Cada operação cria uma nova versão completa e válida

### 4. Auditoria e Rastreabilidade
- Cada mudança cria um novo objeto com novo timestamp
- É possível manter histórico de todas as versões do pedido
- Facilita implementação de Event Sourcing e CQRS

### 5. Cache Seguro
- Objetos imutáveis podem ser compartilhados livremente entre componentes
- Cache não precisa se preocupar com invalidação por mudanças inesperadas
- Reduz overhead de gerenciamento de memória

## Exemplo de Uso em Cenário Concorrente

```java
// Thread 1: Processamento de pagamento
Pedido pedidoOriginal = repositorio.buscarPedido("123");
Pedido pedidoConfirmado = pedidoOriginal.atualizarStatus(StatusPedido.CONFIRMADO);
repositorio.salvar(pedidoConfirmado);

// Thread 2: Adição de item (simultaneamente)
Pedido pedidoOriginal2 = repositorio.buscarPedido("123");
Pedido pedidoComNovoItem = pedidoOriginal2.adicionarItem(novoItem);
repositorio.salvar(pedidoComNovoItem);

// Não há conflito: cada thread trabalha com sua própria versão imutável
// O repositório pode implementar controle de versão otimista
```

## Vantagens Adicionais

1. **Testabilidade**: Testes são mais simples pois o estado é previsível
2. **Debugging**: Mais fácil rastrear bugs pois objetos não mudam inesperadamente
3. **Manutenibilidade**: Código mais fácil de entender e manter
4. **Composição**: Objetos imutáveis facilitam programação funcional e streams
5. **Segurança**: Reduz vulnerabilidades relacionadas a modificações indevidas
