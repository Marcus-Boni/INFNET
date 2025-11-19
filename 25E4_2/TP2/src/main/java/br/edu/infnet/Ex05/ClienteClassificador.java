public class ClienteClassificador {
    public String classificarCliente(int idade, double renda, int compras, String localizacao) {
        
        final int IDADE_SENIOR = 60;
        final double RENDA_ALTA_SENIOR = 5000;
        final int COMPRAS_PREMIUM_SENIOR = 10;
        final double RENDA_ALTA_JOVEM = 7000;
        final int COMPRAS_PREMIUM_JOVEM = 20;

        // Bloco Sênior
        if (idade > IDADE_SENIOR) {
            // Guard Clause 1: Sênior Baixa Renda
            if (renda <= RENDA_ALTA_SENIOR) {
                return "Cliente Sênior Baixa Renda";
            }
            
            // Guard Clause 2: Sênior Regular (Renda Alta, Compras Baixas)
            if (compras <= COMPRAS_PREMIUM_SENIOR) {
                return "Cliente Regular Sênior";
            }

            // Fallthrough: Sênior Premium (Renda Alta, Compras Altas)
            return "Cliente Premium Sênior";
        } 
        
        // Bloco Jovem (idade <= 60)
        
        // Guard Clause 3: Jovem Baixa Renda
        if (renda <= RENDA_ALTA_JOVEM) {
            return "Cliente Jovem Baixa Renda";
        }
        
        // Guard Clause 4: Jovem Regular (Renda Alta, Compras Baixas)
        if (compras <= COMPRAS_PREMIUM_JOVEM) {
            return "Cliente Regular Jovem";
        }

        return "Cliente Premium Jovem";
    }
}

/*
Explicação da Proposta de Melhoria:
A refatoração utiliza o padrão **Guard Clause (Cláusulas de Guarda)**, que consiste em usar condições `if` seguidas de um `return` imediato para tratar casos excepcionais ou simples o mais cedo possível.
* **Vantagem 1: Redução do Aninhamento:** O código agora é mais plano e linear, seguindo a lógica da esquerda para a direita em vez de se aprofundar, o que reduz a Complexidade Ciclomática.
* **Vantagem 2: Clareza:** Cada bloco de `if` trata de uma classificação específica e retorna imediatamente, facilitando a leitura e o entendimento do fluxo lógico. O leitor não precisa manter múltiplas condições na memória.
* **Vantagem 3: Manutenção:** Se uma regra mudar (ex: a renda para Sênior Baixa Renda), a alteração é localizada e menos propensa a introduzir bugs.
*/