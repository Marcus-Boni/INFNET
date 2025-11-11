# Especificação Técnica do Sistema

## 1. Visão Geral do Sistema

### 1.1 Propósito
Sistema de gerenciamento de contas bancárias que permite operações CRUD completas, mantendo a integridade dos dados e aplicando regras de negócio rigorosas.

### 1.2 Escopo
- Criação, leitura, atualização e exclusão de contas bancárias
- Operações de depósito e saque
- Validações automáticas de dados
- Interface de linha de comando interativa

## 2. Requisitos Funcionais

### RF01 - Criar Conta Bancária
**Descrição**: O sistema deve permitir a criação de novas contas bancárias.

**Pré-condições**:
- Nome do titular deve ser fornecido
- Saldo inicial deve ser fornecido (ou assumir zero)

**Pós-condições**:
- Conta criada com ID único
- Dados validados e persistidos

**Cenários de Teste**:
1. Criar conta com nome válido e saldo positivo
2. Criar conta com nome válido e saldo zero
3. Criar conta com nome inválido (deve falhar)
4. Criar conta com saldo negativo (deve falhar)

**Regras de Negócio**:
- RN01: Nome deve ter entre 3 e 100 caracteres
- RN02: Saldo inicial não pode ser negativo

### RF02 - Consultar Conta
**Descrição**: O sistema deve permitir consultar uma conta por ID.

**Pré-condições**:
- ID da conta deve ser fornecido

**Pós-condições**:
- Dados da conta retornados ou exceção lançada

**Cenários de Teste**:
1. Consultar conta existente
2. Consultar conta inexistente (deve lançar exceção)
3. Consultar com ID nulo (deve lançar exceção)
4. Consultar com ID negativo (deve lançar exceção)

### RF03 - Listar Todas as Contas
**Descrição**: O sistema deve retornar lista de todas as contas cadastradas.

**Pré-condições**: Nenhuma

**Pós-condições**:
- Lista de contas retornada (pode ser vazia)

**Cenários de Teste**:
1. Listar quando há contas cadastradas
2. Listar quando não há contas (retorna lista vazia)

### RF04 - Depositar
**Descrição**: O sistema deve permitir adicionar valor ao saldo de uma conta.

**Pré-condições**:
- Conta deve existir
- Valor deve ser positivo

**Pós-condições**:
- Saldo da conta incrementado
- Mudança persistida

**Cenários de Teste**:
1. Depositar valor válido em conta existente
2. Depositar em conta inexistente (deve falhar)
3. Depositar valor zero (deve falhar)
4. Depositar valor negativo (deve falhar)

**Regras de Negócio**:
- RN03: Valor do depósito deve ser maior que zero
- RN04: Operação deve ser atômica

### RF05 - Sacar
**Descrição**: O sistema deve permitir retirar valor do saldo de uma conta.

**Pré-condições**:
- Conta deve existir
- Valor deve ser positivo
- Saldo deve ser suficiente

**Pós-condições**:
- Saldo da conta decrementado
- Mudança persistida

**Cenários de Teste**:
1. Sacar valor válido com saldo suficiente
2. Sacar com saldo insuficiente (deve falhar)
3. Sacar de conta inexistente (deve falhar)
4. Sacar valor zero (deve falhar)
5. Sacar valor negativo (deve falhar)
6. Sacar exatamente o saldo disponível (saldo = 0)

**Regras de Negócio**:
- RN05: Valor do saque deve ser maior que zero
- RN06: Saldo não pode ficar negativo
- RN07: Operação deve ser atômica

### RF06 - Atualizar Saldo
**Descrição**: O sistema deve permitir atualizar diretamente o saldo de uma conta.

**Pré-condições**:
- Conta deve existir
- Novo saldo não pode ser negativo

**Pós-condições**:
- Saldo atualizado
- Mudança persistida

**Cenários de Teste**:
1. Atualizar para saldo positivo
2. Atualizar para saldo zero
3. Atualizar para saldo negativo (deve falhar)
4. Atualizar conta inexistente (deve falhar)

**Regras de Negócio**:
- RN08: Novo saldo não pode ser negativo

### RF07 - Alterar Nome do Titular
**Descrição**: O sistema deve permitir alterar o nome do titular.

**Pré-condições**:
- Conta deve existir
- Novo nome deve ser válido

**Pós-condições**:
- Nome atualizado
- Mudança persistida

**Cenários de Teste**:
1. Alterar para nome válido
2. Alterar para nome inválido (deve falhar)
3. Alterar conta inexistente (deve falhar)

**Regras de Negócio**:
- RN01: Nome deve ter entre 3 e 100 caracteres

### RF08 - Excluir Conta
**Descrição**: O sistema deve permitir excluir uma conta.

**Pré-condições**:
- Conta deve existir

**Pós-condições**:
- Conta removida do sistema

**Cenários de Teste**:
1. Excluir conta existente
2. Excluir conta inexistente (deve falhar)
3. Tentar consultar conta excluída (deve falhar)

**Regras de Negócio**:
- RN09: Exclusão deve ser confirmada pelo usuário (na CLI)

## 3. Requisitos Não Funcionais

### RNF01 - Testabilidade
- Cobertura mínima de 80% (linha e branch)
- Testes unitários para todas as classes de negócio
- Testes de propriedades para Value Objects
- Testes de integração para serviços

### RNF02 - Manutenibilidade
- Código organizado em pacotes lógicos
- Classes com responsabilidade única
- Métodos com no máximo 20 linhas
- Complexidade ciclomática < 10

### RNF03 - Confiabilidade
- Validações em todas as entradas
- Tratamento consistente de exceções
- Estados inválidos impossíveis por design
- Transações ACID para operações de banco

### RNF04 - Usabilidade
- Interface CLI intuitiva
- Mensagens de erro claras
- Feedback visual para operações
- Confirmação para operações destrutivas

### RNF05 - Performance
- Operações CRUD em < 100ms (banco local)
- Pool de conexões configurado
- Índices em campos de busca

### RNF06 - Segurança
- Validação de entrada rigorosa
- Prevenção de SQL injection (JPA)
- Tratamento seguro de exceções

## 4. Regras de Validação Detalhadas

### Validação de Nome do Titular

```
Entrada: String nome
Restrições:
  - não nulo
  - não vazio após trim
  - tamanho >= 3
  - tamanho <= 100
Transformação:
  - trim() aplicado
Saída: NomeTitular (Value Object)
Exceção: IllegalArgumentException
```

**Partições Equivalentes**:
- PE1: Nome válido (3-100 chars) → Aceito
- PE2: Nome muito curto (< 3) → Exceção
- PE3: Nome muito longo (> 100) → Exceção
- PE4: Nome nulo → Exceção
- PE5: Nome vazio/espaços → Exceção

**Valores Limite**:
- 0 caracteres → Exceção
- 2 caracteres → Exceção
- 3 caracteres → Aceito
- 100 caracteres → Aceito
- 101 caracteres → Exceção

### Validação de Saldo

```
Entrada: Double/BigDecimal valor
Restrições:
  - não nulo
  - >= 0
Transformação:
  - setScale(2, HALF_UP)
Saída: Saldo (Value Object)
Exceção: IllegalArgumentException
```

**Partições Equivalentes**:
- PE1: Valor positivo → Aceito
- PE2: Valor zero → Aceito
- PE3: Valor negativo → Exceção
- PE4: Valor nulo → Exceção

**Valores Limite**:
- -0.01 → Exceção
- 0.00 → Aceito
- 0.01 → Aceito
- 999999999.99 → Aceito

### Validação de ID de Conta

```
Entrada: Long id
Restrições:
  - não nulo
  - > 0
Saída: Long id
Exceção: IllegalArgumentException
```

**Partições Equivalentes**:
- PE1: ID positivo → Aceito
- PE2: ID zero → Exceção
- PE3: ID negativo → Exceção
- PE4: ID nulo → Exceção

## 5. Modelo de Dados

### Entidade: Conta

```java
Conta {
  id: Long (PK, auto_increment)
  nomeTitular: String(100) NOT NULL
  valorSaldo: Double NOT NULL, >= 0
  
  Constraints:
    - PK on id
    - CHECK (valorSaldo >= 0)
    - CHECK (LENGTH(nomeTitular) BETWEEN 3 AND 100)
}
```

### Índices

```sql
PRIMARY KEY (id)
INDEX idx_nome_titular (nomeTitular) -- Para buscas futuras
```

## 6. Fluxos de Operação

### Fluxo: Criar Conta

```
1. Usuário fornece nome e saldo
2. Sistema valida nome (NomeTitular)
3. Sistema valida saldo (Saldo)
4. Sistema cria entidade Conta
5. Sistema persiste no banco
6. Sistema retorna ID da conta criada
7. [EXCEÇÃO] Se validação falhar, lança DadosInvalidosException
```

### Fluxo: Depositar

```
1. Usuário fornece ID da conta e valor
2. Sistema valida ID
3. Sistema valida valor (> 0)
4. Sistema busca conta (lança exceção se não existir)
5. Sistema adiciona valor ao saldo
6. Sistema persiste mudança
7. Sistema confirma operação
8. [EXCEÇÃO] ContaNotFoundException se conta não existir
9. [EXCEÇÃO] OperacaoInvalidaException se valor inválido
```

### Fluxo: Sacar

```
1. Usuário fornece ID da conta e valor
2. Sistema valida ID
3. Sistema valida valor (> 0)
4. Sistema busca conta
5. Sistema verifica saldo suficiente
6. Sistema subtrai valor do saldo
7. Sistema persiste mudança
8. Sistema confirma operação
9. [EXCEÇÃO] ContaNotFoundException se conta não existir
10. [EXCEÇÃO] OperacaoInvalidaException se saldo insuficiente
```

## 7. Matriz de Rastreabilidade

| Requisito | Classe Principal | Testes | Cobertura |
|-----------|-----------------|--------|-----------|
| RF01 | ContaService.criarConta() | ContaServiceTest | 100% |
| RF02 | ContaService.buscarContaPorId() | ContaServiceTest | 100% |
| RF03 | ContaService.listarTodasContas() | ContaServiceTest | 100% |
| RF04 | ContaService.depositar() | ContaServiceTest | 100% |
| RF05 | ContaService.sacar() | ContaServiceTest | 100% |
| RF06 | ContaService.atualizarSaldo() | ContaServiceTest | 100% |
| RF07 | ContaService.alterarNomeTitular() | ContaServiceTest | 100% |
| RF08 | ContaService.excluirConta() | ContaServiceTest | 100% |
| RN01 | NomeTitular | NomeTitularTest, NomeTitularPropertyTest | 100% |
| RN02-08 | Saldo | SaldoTest, SaldoPropertyTest | 100% |

## 8. Casos de Teste Críticos

### CT01 - Saque com Saldo Exato
```
Pré-condição: Conta com saldo = R$ 100,00
Ação: Sacar R$ 100,00
Resultado Esperado: Saldo = R$ 0,00, sem exceção
```

### CT02 - Saque com Saldo Insuficiente (Limite)
```
Pré-condição: Conta com saldo = R$ 100,00
Ação: Sacar R$ 100,01
Resultado Esperado: OperacaoInvalidaException
```

### CT03 - Nome no Limite Mínimo
```
Ação: Criar conta com nome "Ana" (3 chars)
Resultado Esperado: Conta criada com sucesso
```

### CT04 - Nome Abaixo do Limite
```
Ação: Criar conta com nome "AB" (2 chars)
Resultado Esperado: IllegalArgumentException
```

### CT05 - Imutabilidade de Saldo
```
Pré-condição: Saldo s1 = R$ 100,00
Ação: s2 = s1.adicionar(R$ 50,00)
Verificação: s1.getValor() == R$ 100,00 E s2.getValor() == R$ 150,00
```

## 9. Glossário

- **Value Object**: Objeto imutável definido por seus valores, sem identidade
- **CQS**: Command Query Separation - separação entre comandos e consultas
- **Fail-Fast**: Princípio de falhar imediatamente ao detectar erro
- **Imutabilidade**: Propriedade de objetos que não podem ser modificados após criação
- **Type Safety**: Segurança de tipos garantida pelo compilador
- **Partition Equivalence**: Técnica de teste que divide entradas em classes equivalentes

---

**Versão**: 1.0  
**Data**: Novembro 2025  
**Status**: Completo
