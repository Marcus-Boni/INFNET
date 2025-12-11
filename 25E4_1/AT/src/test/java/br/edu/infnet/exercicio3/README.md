# Exercício 3 - Teste de API ViaCEP

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
