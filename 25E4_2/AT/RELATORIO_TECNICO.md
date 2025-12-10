# RELATÓRIO TÉCNICO - REFATORAÇÃO DO SISTEMA DE LOGÍSTICA

## 1. ANÁLISE CRÍTICA DO CÓDIGO LEGADO

### 1.1 Ausência de Encapsulamento
Todos os atributos da classe Pedido são públicos, permitindo acesso e modificação diretos sem validação. Isso viola o princípio de ocultação de informações e permite criação de estados inválidos.

### 1.2 Acoplamento entre Lógica e Apresentação
Os métodos gerarEtiqueta() e gerarResumoPedido() misturam regras de negócio com formatação de saída, dificultando reutilização e manutenção.

### 1.3 Duplicação de Lógica
O método calcularFrete() é chamado repetidamente nos métodos de geração de etiqueta e resumo, causando recálculo desnecessário e potencial inconsistência.

### 1.4 Uso de Valores Mágicos
Constantes numéricas (1.5, 1.2, 1.1, 10, 5) estão espalhadas pelo código sem significado explícito, dificultando compreensão e manutenção.

### 1.5 Falta de Validações
Não há verificação de valores nulos, negativos ou vazios, permitindo criação de objetos em estado inconsistente.

### 1.6 Baixa Coesão
A classe Pedido mistura múltiplas responsabilidades: armazenamento de dados, cálculo de frete, formatação de saídas e aplicação de promoções.

### 1.7 Má Nomenclatura
Abreviações como "EXP", "PAD", "ECO" não são autoexplicativas. O método aplicarFretePromocional() altera peso, não frete.

### 1.8 Ausência de Abstrações
Estrutura if-else rígida impede extensão. Adicionar novo tipo de frete requer modificação do código existente, violando o princípio Aberto-Fechado.

---

## 2. DESCRIÇÃO DAS ALTERAÇÕES REALIZADAS

### 2.1 Criação da Classe Entrega Imutável

Substituímos a classe Pedido por Entrega, uma classe imutável com validações no construtor. Todos os atributos são final e privados, com validações explícitas:

- Validação de destinatário e endereço não nulos/vazios
- Validação de peso positivo
- Validação de tipo de frete não nulo

A imutabilidade garante thread-safety e elimina efeitos colaterais. Métodos que alteram estado retornam novas instâncias.

**Justificativa técnica**: Objetos imutáveis são mais seguros, simples de entender e livres de bugs relacionados a mutação de estado compartilhado.

### 2.2 Implementação do Padrão Strategy para Cálculo de Frete

Criamos a interface CalculadoraFrete com três implementações concretas:
- FreteExpresso: peso × 1.5 + 10
- FretePadrao: peso × 1.2
- FreteEconomico: peso × 1.1 - 5

Cada classe encapsula uma estratégia específica com constantes nomeadas, eliminando valores mágicos.

**Justificativa técnica**: O padrão Strategy substitui estruturas if-else rígidas por polimorfismo, permitindo adicionar novos tipos de frete sem modificar código existente (princípio Aberto-Fechado).

### 2.3 Criação da FabricaCalculadoraFrete

Implementamos uma fábrica que gerencia o mapeamento entre TipoFrete e CalculadoraFrete usando EnumMap, oferecendo:
- Método obterCalculadora() para recuperar estratégia
- Método registrarCalculadora() para extensibilidade
- Método calcularFrete() como ponto de entrada único

**Justificativa técnica**: Centraliza a lógica de seleção de estratégia, permitindo extensão dinâmica e reduzindo acoplamento.

### 2.4 Criação do EtiquetaService

Separamos a responsabilidade de formatação em um serviço dedicado com injeção de dependência da FabricaCalculadoraFrete. Métodos:
- gerarEtiqueta(): formata dados completos da entrega
- gerarResumoPedido(): formata resumo com tipo de frete

**Justificativa técnica**: Separa apresentação de lógica de negócio, facilitando testes e permitindo diferentes formatos de saída sem alterar domínio.

### 2.5 Criação do PromocaoService

Extraímos a lógica promocional para um serviço específico:
- aplicarPromocaoPesoPesado(): aplica desconto para pacotes > 10kg
- isFreteGratis(): verifica elegibilidade

**Justificativa técnica**: Isola regras promocionais, facilitando modificação e extensão sem impactar outras partes do sistema.

### 2.6 Implementação de Exceções Personalizadas

Criamos EntregaInvalidaException para comunicar erros de validação de forma explícita e específica ao domínio.

**Justificativa técnica**: Exceções específicas melhoram rastreabilidade de erros e permitem tratamento diferenciado por tipo de falha.

### 2.7 Uso de Enum para TipoFrete

Substituímos Strings por enum TipoFrete (EXPRESSO, PADRAO, ECONOMICO), garantindo type-safety e eliminando valores inválidos.

**Justificativa técnica**: Enums fornecem validação em tempo de compilação e eliminam erros de digitação.

---

## 3. RESPOSTAS ÀS QUESTÕES ANALÍTICAS

### 3.1 Abstrações e Distribuição em Camadas

#### Camada de Domínio (br.edu.infnet.logistica.dominio)
Contém as entidades centrais do negócio:

**Entrega**: Representa o conceito central do sistema. É imutável e autovalidável:

```java
public final class Entrega {
    private final String destinatario;
    private final String endereco;
    private final double pesoEmKg;
    private final TipoFrete tipoFrete;

    public Entrega(String destinatario, String endereco, double pesoEmKg, TipoFrete tipoFrete) {
        validarDestinatario(destinatario);
        validarEndereco(endereco);
        validarPeso(pesoEmKg);
        validarTipoFrete(tipoFrete);
        
        this.destinatario = destinatario;
        this.endereco = endereco;
        this.pesoEmKg = pesoEmKg;
        this.tipoFrete = tipoFrete;
    }
}
```

A validação no construtor garante que objetos Entrega só existam em estados válidos, eliminando verificações duplicadas em outros pontos do sistema.

**TipoFrete**: Enum que encapsula os tipos válidos de frete, substituindo strings mágicas e proporcionando segurança de tipos.

#### Camada de Serviços (br.edu.infnet.logistica.servico)

**Interface CalculadoraFrete**: Define o contrato para cálculo de frete:

```java
public interface CalculadoraFrete {
    double calcular(Entrega entrega);
}
```

**Implementações concretas**: FreteExpresso, FretePadrao, FreteEconomico. Cada classe encapsula uma estratégia específica:

```java
public class FreteExpresso implements CalculadoraFrete {
    private static final double MULTIPLICADOR_PESO = 1.5;
    private static final double TAXA_FIXA = 10.0;

    @Override
    public double calcular(Entrega entrega) {
        return entrega.getPesoEmKg() * MULTIPLICADOR_PESO + TAXA_FIXA;
    }
}
```

Esta abstração substitui a estrutura if-else do código original:

**Código Original:**
```java
public double calcularFrete() {
    if (tipoFrete.equals("EXP")) {
        return peso * 1.5 + 10;
    } else if (tipoFrete.equals("PAD")) {
        return peso * 1.2;
    } else if (tipoFrete.equals("ECO")) {
        return peso * 1.1 - 5;
    } else {
        return 0;
    }
}
```

**Código Refatorado:**
```java
public class FabricaCalculadoraFrete {
    private final Map<TipoFrete, CalculadoraFrete> calculadoras;

    public double calcularFrete(Entrega entrega) {
        CalculadoraFrete calculadora = obterCalculadora(entrega.getTipoFrete());
        return calculadora.calcular(entrega);
    }
}
```

**Vantagens:**
- Novos tipos de frete são adicionados criando novas classes, não modificando código existente
- Cada estratégia pode ter testes isolados
- Eliminação completa de condicionais para seleção de algoritmo
- Constantes nomeadas substituem valores mágicos

**EtiquetaService e PromocaoService**: Serviços de aplicação que orquestram operações do domínio com injeção de dependência.

#### Camada de Exceções (br.edu.infnet.logistica.excecao)
EntregaInvalidaException comunica violações de regras de negócio de forma específica ao domínio.

**Benefícios da Separação:**
- **Entendimento**: Cada camada tem responsabilidade clara
- **Manutenção**: Alterações em formatação não afetam cálculos
- **Testabilidade**: Componentes podem ser testados isoladamente

### 3.2 Contratos e Integridade dos Objetos

#### Validações no Construtor

A classe Entrega implementa o padrão "fail-fast", validando todas as entradas no momento da construção:

```java
public Entrega(String destinatario, String endereco, double pesoEmKg, TipoFrete tipoFrete) {
    validarDestinatario(destinatario);
    validarEndereco(endereco);
    validarPeso(pesoEmKg);
    validarTipoFrete(tipoFrete);
    
    this.destinatario = destinatario;
    this.endereco = endereco;
    this.pesoEmKg = pesoEmKg;
    this.tipoFrete = tipoFrete;
}

private void validarPeso(double peso) {
    if (peso <= 0) {
        throw new EntregaInvalidaException("Peso deve ser maior que zero");
    }
}
```

**Benefícios:**
- **Impossibilidade de estados inválidos**: Não existe Entrega com peso negativo
- **Falha rápida**: Erros são detectados imediatamente, não durante uso
- **Mensagens claras**: Exceções comunicam exatamente o problema

#### Imutabilidade como Contrato

Todos os campos são final, garantindo que uma vez criada, a Entrega não pode ser alterada:

```java
private final String destinatario;
private final double pesoEmKg;
```

Operações que "modificam" retornam novas instâncias:

```java
public Entrega aplicarDescontoPeso(double descontoEmKg) {
    if (descontoEmKg < 0) {
        throw new EntregaInvalidaException("Desconto não pode ser negativo");
    }
    double novoPeso = Math.max(0.1, pesoEmKg - descontoEmKg);
    return new Entrega(destinatario, endereco, novoPeso, tipoFrete);
}
```

**Benefícios:**
- **Thread-safety**: Objetos imutáveis são seguros para uso concorrente
- **Rastreabilidade**: Estado original é preservado
- **Previsibilidade**: Métodos não têm efeitos colaterais

#### Uso de Tipos Fortes

Substituição de String por TipoFrete elimina valores inválidos:

```java
public enum TipoFrete {
    EXPRESSO,
    PADRAO,
    ECONOMICO
}
```

Compilador garante que apenas valores válidos são aceitos, eliminando verificações em tempo de execução.

**Resultado:** O sistema previne erros ao invés de tratá-los, reduzindo bugs em produção e simplificando lógica de negócio.

### 3.3 Boas Práticas de Nomenclatura e Estrutura

#### Nomenclatura de Classes

**Substantivos que representam conceitos do domínio:**
- `Entrega`: Conceito central do negócio
- `CalculadoraFrete`: Responsabilidade clara de calcular
- `FabricaCalculadoraFrete`: Padrão Factory explícito no nome
- `EtiquetaService`: Sufixo Service identifica camada de serviço

**Evita abreviações:**
- `FreteExpresso` ao invés de `FreteExp`
- `TipoFrete` ao invés de `TpFrete`

#### Nomenclatura de Métodos

**Verbos que descrevem ações:**
- `calcular()`: Ação de cálculo
- `validarPeso()`: Ação de validação
- `gerarEtiqueta()`: Ação de geração

**Métodos booleanos com prefixos is/has:**
- `isElegivelFreteGratis()`: Retorna elegibilidade
- Prefixo "is" indica retorno booleano sem necessidade de documentação

#### Nomenclatura de Variáveis e Constantes

**Constantes com significado explícito:**
```java
private static final double MULTIPLICADOR_PESO = 1.5;
private static final double TAXA_FIXA = 10.0;
private static final double PESO_MINIMO_DESCONTO_KG = 10.0;
```

Substitui valores mágicos por constantes autoexplicativas.

**Variáveis descritivas:**
```java
double pesoEmKg
String destinatario
TipoFrete tipoFrete
```

Unidades e contexto estão no nome, eliminando ambiguidade.

#### Organização de Pacotes

```
br.edu.infnet.logistica
├── dominio
│   ├── Entrega.java
│   └── TipoFrete.java
├── servico
│   ├── EtiquetaService.java
│   ├── PromocaoService.java
│   └── frete
│       ├── CalculadoraFrete.java
│       ├── FreteExpresso.java
│       ├── FretePadrao.java
│       ├── FreteEconomico.java
│       └── FabricaCalculadoraFrete.java
└── excecao
    └── EntregaInvalidaException.java
```

**Princípios aplicados:**
- **Separação por responsabilidade**: dominio, servico, excecao
- **Subpacotes para coesão**: servico.frete agrupa estratégias relacionadas
- **Nomes refletem hierarquia**: Estrutura física espelha lógica conceitual

**Coesão e Navegabilidade:**
- Classes relacionadas estão no mesmo pacote
- Hierarquia clara facilita localização
- Nomenclatura consistente reduz curva de aprendizado

**Redução de Comentários:**

Código autoexplicativo através de nomenclatura adequada:

```java
public boolean isElegivelFreteGratis() {
    return tipoFrete == TipoFrete.ECONOMICO && pesoEmKg < 2;
}
```

Não requer comentário pois nome do método e lógica são claros. Compare com o original:

```java
public boolean isFreteGratis() {
    return tipoFrete.equals("ECO") && peso < 2;
}
```

Versão refatorada explicita elegibilidade e usa enum, tornando intenção cristalina sem documentação adicional.

---

## CONCLUSÃO

A refatoração transformou um código monolítico e rígido em uma arquitetura modular, extensível e robusta. Principais conquistas:

1. **Separação de Responsabilidades**: Cada classe tem propósito único e bem definido
2. **Extensibilidade**: Novos tipos de frete ou promoções podem ser adicionados sem modificar código existente
3. **Segurança**: Validações impedem estados inválidos em tempo de compilação e execução
4. **Clareza**: Nomenclatura significativa e estrutura coesa eliminam necessidade de documentação extensiva
5. **Testabilidade**: Componentes isolados permitem testes unitários abrangentes

O sistema está preparado para evolução sustentável, com fundações sólidas em princípios de Engenharia de Software e Clean Code.
