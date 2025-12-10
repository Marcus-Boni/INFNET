# Exercício 5 - Análise Estrutural de Código

## Funcionalidade Escolhida: Algoritmos de Ordenação

Foram escolhidos dois algoritmos clássicos de ordenação do repositório TheAlgorithms/Java:
- **BubbleSort**: Algoritmo simples de ordenação por comparação
- **QuickSort**: Algoritmo eficiente de ordenação por divisão e conquista

## Análise de Cobertura

### BubbleSort

#### Estrutura do Código
```
- Loop externo: for (i = 1 até size)
  - Flag swapped = false
  - Loop interno: for (j = 0 até size - i)
    - Decisão: if (array[j] > array[j+1])
      - swap(array, j, j+1)
      - swapped = true
  - Decisão: if (!swapped)
    - break
```

#### Ramificações Testadas:
1. **Array vazio**: Nenhum loop executado
2. **Array com 1 elemento**: Loop externo não executa
3. **Array já ordenado**: Loop executa mas sem trocas, break acionado
4. **Array ordem reversa**: Todas as trocas necessárias, break não acionado
5. **Array parcialmente ordenado**: Algumas trocas, break acionado após ordenação

#### Decisões Cobertas:
- `if (greater(array[j], array[j + 1]))` - verdadeiro e falso
- `if (!swapped)` - verdadeiro (otimização) e falso (continuação)

#### Cobertura Esperada: ~100%
- Todos os loops executados
- Todas as ramificações testadas
- Condições de borda validadas

### QuickSort

#### Estrutura do Código
```
- sort(array)
  - doSort(array, 0, length-1)
    - if (left < right)
      - pivot = randomPartition()
      - doSort(left, pivot-1)
      - doSort(pivot, right)
- randomPartition()
  - randomIndex = random(left, right)
  - swap(randomIndex, right)
  - partition()
- partition()
  - mid = (left + right) / 2
  - pivot = array[mid]
  - while (left <= right)
    - while (array[left] < pivot) left++
    - while (pivot < array[right]) right--
    - if (left <= right) swap e incrementa
```

#### Ramificações Testadas:
1. **Array vazio**: Condição `left < right` falsa imediatamente
2. **Array com 1 elemento**: Condição `left < right` falsa
3. **Array com 2 elementos**: Particionamento simples
4. **Pivot em diferentes posições**: Início, meio, fim
5. **Elementos iguais**: Teste do particionamento com duplicatas

#### Decisões Cobertas:
- `if (left < right)` - verdadeiro e falso
- `while (left <= right)` - múltiplas iterações
- `while (less(array[left], pivot))` - verdadeiro e falso
- `while (less(pivot, array[right]))` - verdadeiro e falso
- `if (left <= right)` no partition - verdadeiro e falso

#### Cobertura Esperada: ~95-100%
- Todas as ramificações principais cobertas
- Recursão testada em diferentes profundidades
- Particionamento em várias configurações

## Estratégia de Testes

### 1. Testes de Caixa Preta
- Entradas variadas (vazio, único elemento, múltiplos)
- Diferentes ordens (ordenado, reverso, aleatório)
- Casos especiais (duplicatas, valores extremos)

### 2. Testes de Caixa Branca
- Cobertura de ramificações
- Cobertura de decisões
- Cobertura de caminhos

### 3. Análise de Valor Limite
- Arrays vazios
- Arrays com 1 elemento
- Arrays com 2 elementos
- Arrays grandes

### 4. Partições de Equivalência
- Arrays ordenados
- Arrays desordenados
- Arrays com duplicatas
- Arrays com valores negativos/positivos

## Trechos Não Cobertos (Análise Esperada)

### BubbleSort
- **Possível não cobertura**: Casos raros de interrupção precoce
- **Justificativa**: A otimização com `break` é determinística

### QuickSort
- **Possível não cobertura**: Combinações específicas de particionamento
- **Justificativa**: A aleatoriedade do pivot dificulta cobertura de 100%
- **Recomendação**: Seed fixa para testes determinísticos

## Como Executar os Testes com Cobertura

```bash
mvn clean test jacoco:report
```

O relatório será gerado em: `target/site/jacoco/index.html`

## Análise de Riscos

### BubbleSort
- **Performance**: O(n²) - inadequado para arrays grandes
- **Estabilidade**: Algoritmo estável
- **Robustez**: Seguro para todos os tipos comparáveis

### QuickSort
- **Performance**: O(n log n) médio, O(n²) pior caso
- **Estabilidade**: Não estável
- **Robustez**: Requer randomização para evitar pior caso
