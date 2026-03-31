# AT - Refatoração de Sistemas Legados

Este projeto responde as 6 questões propostas sobre refatoração, code smells, legibilidade, modularidade, encapsulamento e polimorfismo.

## Tecnologias

- Java 17
- Maven
- JUnit 5 (Jupiter)

## Estrutura do Projeto

- `src/main/java/br/infnet/at/q1` - Questão 1
- `src/main/java/br/infnet/at/q2` - Questão 2
- `src/main/java/br/infnet/at/q3` - Questão 3
- `src/main/java/br/infnet/at/q4` - Questão 4
- `src/main/java/br/infnet/at/q5` - Questão 5
- `src/main/java/br/infnet/at/q6` - Questão 6
- `src/test/java/br/infnet/at/q1` - Testes automatizados da Questão 1

## Como Executar

### Rodar testes

```bash
mvn test
```

### Rodar demo da Questão 6 (polimorfismo)

```bash
mvn -DskipTests compile
java -cp target/classes br.infnet.at.q6.DocumentApplication
```

---

## 1) Refatoracao pratica de codigo simples

### Problemas (bad smells) no codigo original

1. **Nomes semanticos ruins (Obscure Naming)**: classe `X`, metodo `y`, parametro `z`.
2. **Codigo morto / inutil (Dead Code)**: `int temp = z * 0 + 42` nao contribui para regra de negocio.
3. **Duplicacao de logica (Duplicated Code)**: `if (z > 10)` aparece duas vezes, com `z > 10 && z > 5` redundante.
4. **Mistura de responsabilidades (Large Method / SRP violado)**: classificacao, log de debug e impressao tudo no mesmo metodo.

### Como a refatoracao resolveu

- Renomeacao para nomes de dominio: `ClassificationService`, `classifyLevel(int value)`.
- Extracao de metodos de regra: `isRareCase`, `isHigh`, `isMedium`.
- Separacao da decisao de negocio da IO: regra em metodo puro (`classifyLevel`) e impressao em `printClassification`.
- Inclusao do novo comportamento solicitado: retorna **"MÉDIO"** quando valor for **10**.

### Trecho de codigo (Questao 1)

Arquivo: `src/main/java/br/infnet/at/q1/ClassificationService.java`

```java
public String classifyLevel(int value) {
    if (isRareCase(value)) {
        return "CASO RARO";
    }
    if (isHigh(value)) {
        return "ALTO";
    }
    if (isMedium(value)) {
        return "MÉDIO";
    }
    return "BAIXO";
}
```

### Teste automatizado

Arquivo: `src/test/java/br/infnet/at/q1/ClassificationServiceTest.java`

```java
@Test
void shouldClassifyAsHighWhenValueIsGreaterThanTen() {
    assertEquals("ALTO", service.classifyLevel(11));
}

@Test
void shouldClassifyAsMediumWhenValueIsExactlyTen() {
    assertEquals("MÉDIO", service.classifyLevel(10));
}

@Test
void shouldClassifyAsLowWhenValueIsLessThanTen() {
    assertEquals("BAIXO", service.classifyLevel(9));
}
```

### Evidencia de execucao dos testes (saida de terminal)

```text
[INFO] Running br.infnet.at.q1.ClassificationServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 2) Identificando Bad Smells (Invoice legado)

### Bad smells encontrados (maximo relevante)

1. **Data Class**: muitos campos publicos sem encapsulamento.
2. **Primitive Obsession / Type Code**: uso de `int type` para representar tipos de nota.
3. **Magic Numbers**: `1`, `2`, `-1` sem semantica explicita.
4. **Long Method**: `process()` concentra validacao, regra, impressao e envio.
5. **Duplicated Code**: decisao do tipo de nota e montagem de texto repetidas.
6. **Divergent Change (SRP violado)**: qualquer mudanca de regra, layout da nota ou envio altera o mesmo metodo.
7. **Dead Code / Speculative Generality**: ramo `type == -1` marcado como "nunca ocorre".
8. **Bug por condicional incorreta**: `if (clientEmail == null && !clientEmail.contains("@"))` pode gerar `NullPointerException`.
9. **Baixa coesao**: classe mistura modelo de dados e infraestrutura de envio de email.

### Refatoracao escolhida

Problema escolhido: **Primitive Obsession / Type Code + Long Method**.

Solucao aplicada:

- Troca de `int type` por `InvoiceType` (enum com semantica).
- Extracao de responsabilidades para classes separadas:
  - `Invoice` (dados)
  - `InvoiceProcessor` (orquestracao)
  - `EmailSender`/`ConsoleEmailSender` (infraestrutura)
- Correcao da validacao de email com `||` (email nulo ou sem `@`).

Trecho ilustrativo:

```java
private void validateEmail(String email) {
    if (email == null || !email.contains("@")) {
        throw new IllegalArgumentException("Email invalido. Falha no envio.");
    }
}
```

E no enum:

```java
public enum InvoiceType {
    SIMPLE(1, "Simples"),
    TAXED(2, "Com imposto"),
    UNKNOWN(0, "Desconhecido");
}
```

---

## 3) Refatorando para legibilidade

Implementacao em: `src/main/java/br/infnet/at/q3/PriceCalculator.java`

Melhorias aplicadas:

- Variaveis explicativas (`customerDiscountRate`, `holidayDiscountRate`, `totalDiscountRate`).
- Metodos auxiliares para separar regras (`getCustomerDiscountRate`, `getHolidayDiscountRate`).
- Sobrecarga para manter compatibilidade com codigo baseado em `int customerTypeCode`.

Trecho principal:

```java
public double calculatePrice(double basePrice, CustomerType customerType, boolean holidayPurchase) {
    double customerDiscountRate = getCustomerDiscountRate(customerType);
    double holidayDiscountRate = getHolidayDiscountRate(holidayPurchase);
    double totalDiscountRate = customerDiscountRate + holidayDiscountRate;
    double discountMultiplier = 1 - totalDiscountRate;

    return basePrice * discountMultiplier;
}
```

---

## 4) Refatorando para modularidade e encapsulamento

Implementacao em:

- `src/main/java/br/infnet/at/q4/User.java`
- `src/main/java/br/infnet/at/q4/Address.java`

Melhorias aplicadas:

- Campos privados (`name`, `email`, `addresses`).
- Lista tipada com objeto de dominio (`List<Address>`).
- Metodo de controle `addAddress(Address address)` para ocultar acesso direto.
- Getter de enderecos retorna lista imutavel (`Collections.unmodifiableList`).

Trecho:

```java
public void addAddress(Address address) {
    addresses.add(Objects.requireNonNull(address, "address nao pode ser nulo"));
}

public List<Address> getAddresses() {
    return Collections.unmodifiableList(addresses);
}
```

---

## 5) Refatorando condicional complexa com polimorfismo

Implementacao em: `src/main/java/br/infnet/at/q5`

Estrutura:

- `NotificationChannel` (interface)
- `EmailNotificationChannel`, `SmsNotificationChannel`, `PushNotificationChannel` (implementacoes)
- `NotificationService` usando a abstracao

Trecho:

```java
public class NotificationService {
    public void notifyUser(NotificationChannel channel, String message) {
        Objects.requireNonNull(channel, "channel nao pode ser nulo");
        channel.send(message);
    }
}
```

Beneficio: para adicionar novo canal, basta criar nova classe que implementa `NotificationChannel`, sem alterar `NotificationService`.

---

## 6) Substituindo tipos por hierarquias e melhorando coesao

### Problemas da abordagem com `String type`

1. **Type Code** com comparacoes por string fragiliza manutencao.
2. **Condicional crescente** (`if/else`) em cada novo formato.
3. **Violacao do Open/Closed Principle**: toda extensao exige modificar classe existente.
4. **Baixa coesao**: comportamento de impressao de todos os tipos centralizado.

### Refatoracao aplicada

Implementacao em: `src/main/java/br/infnet/at/q6`

- Classe abstrata `Document` com metodo `print()`.
- Subclasses: `PdfDocument`, `HtmlDocument`, `MarkdownDocument`.
- Classe principal `DocumentApplication` instancia 3 documentos e chama `print()` polimorficamente.

Trecho da classe principal:

```java
public static void main(String[] args) {
    List<Document> documents = List.of(
        new PdfDocument(),
        new HtmlDocument(),
        new MarkdownDocument()
    );

    for (Document document : documents) {
        document.print();
    }
}
```

### Resultado esperado da execucao

```text
Printing PDF
Printing HTML
Printing MARKDOWN
```

### Por que melhora o design

- **Maior coesao**: cada classe conhece apenas seu proprio formato.
- **Menos duplicacao**: evita repetir condicionais em varios pontos.
- **Extensibilidade simples**: para novo formato, cria-se nova subclasse sem alterar as existentes.
- **Menor risco de erro**: elimina dependencia de strings soltas para controlar comportamento.

---
