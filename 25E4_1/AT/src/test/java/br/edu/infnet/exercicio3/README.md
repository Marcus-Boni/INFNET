# Exercício 3 - Teste de API ViaCEP

## Tabela de Decisão - Consulta por Endereço

| Caso | UF Válida | Cidade Válida | Logradouro Válido | Cidade com Acentuação | Resultado Esperado |
|------|-----------|---------------|-------------------|----------------------|-------------------|
| 1    | Sim       | Sim           | Sim               | Não                  | Retorna resultados |
| 2    | Sim       | Sim           | Sim               | Sim                  | Retorna resultados |
| 3    | Sim       | Sim           | Não               | Não                  | Array vazio |
| 4    | Sim       | Não           | Sim               | Não                  | Array vazio |
| 5    | Não       | Sim           | Sim               | Não                  | Array vazio ou erro |
| 6    | Não       | Não           | Não               | Não                  | Array vazio ou erro |

## Partições de Equivalência

### Consulta por CEP

#### Partições Válidas:
1. CEP válido existente (8 dígitos)
2. CEP válido com hífen (formato: 12345-678)
3. CEPs de diferentes estados (01xxx-xxx a 99xxx-xxx)

#### Partições Inválidas:
1. CEP com letras
2. CEP incompleto (menos de 8 dígitos)
3. CEP com mais de 8 dígitos
4. CEP vazio
5. CEP null
6. CEP com caracteres especiais

### Consulta por Endereço

#### Partições Válidas:
1. UF válida (2 caracteres)
2. Cidade existente
3. Logradouro existente
4. Cidade com acentuação correta
5. Cidade sem acentuação

#### Partições Inválidas:
1. UF inexistente
2. Cidade inexistente
3. Logradouro inexistente
4. Parâmetros vazios
5. Parâmetros null

## Análise de Valor Limite

### CEP
- **Limite Inferior:** 01000-000 (primeiro CEP válido)
- **Limite Superior:** 99999-999 (último CEP teórico)
- **Valores de Teste:**
  - 00000-000 (abaixo do limite)
  - 01000-000 (limite inferior)
  - 01000-001 (logo acima do limite inferior)
  - 99999-998 (logo abaixo do limite superior)
  - 99999-999 (limite superior)

### UF
- **Válidas:** AC, AL, AM, AP, BA, CE, DF, ES, GO, MA, MG, MS, MT, PA, PB, PE, PI, PR, RJ, RN, RO, RR, RS, SC, SE, SP, TO
- **Inválidas:** XX, ZZ, AA, 12, @#

### Tamanho de Logradouro
- **Mínimo:** 1 caractere
- **Máximo:** limite do servidor (geralmente aceita strings grandes)
- **Valores de Teste:**
  - 0 caracteres (vazio)
  - 1 caractere
  - Nome típico (10-30 caracteres)
  - Nome muito longo (>100 caracteres)

## Justificativa dos Testes

### Testes de Formato de CEP
**Objetivo:** Garantir que a API trata adequadamente entradas malformadas.
**Importância:** Previne erros de servidor e garante validação adequada.

### Testes de Estados Diferentes
**Objetivo:** Verificar que a API funciona corretamente para todas as regiões do Brasil.
**Importância:** CEPs têm faixas específicas por estado, importante testar a cobertura geográfica.

### Testes com e sem Acentuação
**Objetivo:** Validar que a API aceita tanto nomes com acentuação correta quanto normalizados.
**Importância:** Usuários podem digitar de diferentes formas, a API deve ser flexível.

### Testes de Combinações Inválidas
**Objetivo:** Verificar tratamento de erro para combinações impossíveis.
**Importância:** A API deve retornar erro apropriado, não falhar silenciosamente.

### Testes de Valor Limite
**Objetivo:** Identificar comportamento nas extremidades das faixas válidas.
**Importância:** Bugs frequentemente ocorrem em valores limite.

### Tabela de Decisão
**Objetivo:** Cobrir todas as combinações lógicas de parâmetros válidos/inválidos.
**Importância:** Garante que todas as regras de negócio estão sendo testadas.

## Riscos Identificados

1. **Timeout de Rede:** API externa pode estar indisponível
2. **Rate Limiting:** Muitas requisições podem ser bloqueadas
3. **Dados Inconsistentes:** CEPs podem ser adicionados/removidos
4. **Formato de Retorno:** API pode mudar estrutura JSON sem aviso
5. **Caracteres Especiais:** Problemas com encoding UTF-8
