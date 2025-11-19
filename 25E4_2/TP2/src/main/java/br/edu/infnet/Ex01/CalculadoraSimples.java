public class CalculadoraMatematica {
    public static int calcularSomaDobrada(int primeiroNumero, int segundoNumero) {
        // Explicação: Variável 'soma' é clara e descreve o seu propósito imediato.
        int soma = primeiroNumero + segundoNumero; 
        
        // Explicação: O método retorna o dobro da soma calculada.
        return soma * 2; 
    }
}

/*
Explicação da Escolha dos Nomes:
* **Classe `A` para `CalculadoraMatematica`:** Nomeia a classe pelo seu **propósito** (realizar cálculos matemáticos).
* **Método `a` para `calcularSomaDobrada`:** Descreve a **ação** realizada pelo método (calcular a soma e dobrar o resultado). Nomes de métodos devem ser verbos ou frases verbais.
* **Parâmetros `x` e `y` para `primeiroNumero` e `segundoNumero`:** Indicam claramente a **função** de cada parâmetro na operação.
* **Variável local `z` para `soma`:** Descreve o **conteúdo** ou resultado temporário armazenado na variável, facilitando o rastreio da lógica.
*/