# Exercício 5 - Análise Estrutural de Código

## Funcionalidade Escolhida: Algoritmos de Ordenação

Foram escolhidos dois algoritmos clássicos de ordenação do repositório TheAlgorithms/Java:
- **BubbleSort**: Algoritmo simples de ordenação por comparação
- **QuickSort**: Algoritmo eficiente de ordenação por divisão e conquista

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
