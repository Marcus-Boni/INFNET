# Exercício 1 - Teste Exploratório e Análise de Comportamento Esperado

## Especificação Funcional

### Objetivo
Sistema para cálculo do Índice de Massa Corporal (IMC) que recebe peso e altura do usuário e retorna o valor do IMC junto com sua classificação.

### Requisitos Funcionais
1. Receber peso em quilogramas (número decimal)
2. Receber altura em metros (número decimal)
3. Calcular IMC usando fórmula: peso / (altura²)
4. Classificar IMC de acordo com tabela da OMS

### Classificação do IMC
- Menor que 16.0: Magreza grave
- 16.0 a 16.9: Magreza moderada
- 17.0 a 18.4: Magreza leve
- 18.5 a 24.9: Saudável
- 25.0 a 29.9: Sobrepeso
- 30.0 a 34.9: Obesidade Grau I
- 35.0 a 39.9: Obesidade Grau II
- 40.0 ou mais: Obesidade Grau III

## Casos de Teste

### Partições de Equivalência

#### Peso
- Válido: 1.0 a 500.0 kg
- Inválido: <= 0, > 500, não numérico

#### Altura
- Válido: 0.5 a 3.0 m
- Inválido: <= 0, > 3.0, não numérico

### Análise de Valor Limite

#### Limites de Classificação
- 15.9 (Magreza grave - limite superior)
- 16.0 (Magreza moderada - limite inferior)
- 16.9 (Magreza moderada - limite superior)
- 17.0 (Magreza leve - limite inferior)
- 18.4 (Magreza leve - limite superior)
- 18.5 (Saudável - limite inferior)
- 24.9 (Saudável - limite superior)
- 25.0 (Sobrepeso - limite inferior)
- 29.9 (Sobrepeso - limite superior)
- 30.0 (Obesidade I - limite inferior)
- 34.9 (Obesidade I - limite superior)
- 35.0 (Obesidade II - limite inferior)
- 39.9 (Obesidade II - limite superior)
- 40.0 (Obesidade III - limite inferior)

## Falhas Encontradas

### 1. Falta de Validação de Entrada
- Sistema não valida se peso e altura são positivos
- Sistema aceita valores absurdos (altura negativa, peso zero)
- Não trata divisão por zero adequadamente

### 2. Tratamento de Exceções
- Scanner pode lançar exceção se entrada não for numérica
- Sem tratamento de NumberFormatException

### 3. Gerenciamento de Recursos
- Scanners não são fechados corretamente
- Possível vazamento de recursos

### 4. Comportamento com Valores Extremos
- Aceita valores fisicamente impossíveis
- Não há limites mínimos ou máximos definidos

## Justificativa dos Cenários de Teste

### Testes de Valor Limite
Críticos para garantir que as transições entre classificações ocorram exatamente nos pontos especificados. Pequenas variações podem resultar em classificação incorreta.

### Testes de Partição de Equivalência
Garantem que cada faixa de classificação está funcionando corretamente, testando valores representativos de cada categoria.

### Testes de Valores Extremos
Identificam comportamentos inesperados com entradas fora do esperado, importantes para robustez do sistema.

### Testes de Precisão
Verificam se os cálculos mantêm precisão adequada, especialmente importante para valores próximos aos limites.
