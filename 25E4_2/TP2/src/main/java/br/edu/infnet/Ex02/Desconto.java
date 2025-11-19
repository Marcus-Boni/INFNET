public class CalculadoraDesconto {
    // Explicação: Constantes nomeadas eliminam valores mágicos e documentam a regra de negócio.
    private static final double VALOR_MINIMO_PARA_DESCONTO = 1000.0;
    private static final double PERCENTUAL_DESCONTO = 0.10; 
    private static final double FATOR_MULTIPLICADOR = 1 - PERCENTUAL_DESCONTO; 

    public double calcularPrecoComDesconto(double preco) {
        // Explicação: A regra de negócio foi corrigida para usar ">=" 
        // para incluir o valor exato, resolvendo o bug.
        // O uso da constante deixa a condição mais clara.
        if (preco >= VALOR_MINIMO_PARA_DESCONTO) {
            // Explicação: Usar FATOR_MULTIPLICADOR (ou 1 - PERCENTUAL_DESCONTO) 
            // torna o cálculo mais legível.
            return preco * FATOR_MULTIPLICADOR; 
        }
        return preco;
    }
}

/*
Explicação das Melhorias Aplicadas:
1.  **Eliminação de Valores Mágicos:** Os números literais `1000` e `0.9` foram substituídos por constantes nomeadas (`VALOR_MINIMO_PARA_DESCONTO` e `FATOR_MULTIPLICADOR`). Isso torna a **regra de negócio** evidente: o desconto é de 10% (0.9 de fator) e aplica-se a compras de R$ 1000,00 ou mais.
2.  **Clareza e Correção da Regra:** A condição de desconto foi alterada de `preco > 1000` para `preco >= VALOR_MINIMO_PARA_DESCONTO`. Isso corrige o bug em que clientes com compras de **exatos R$ 1000,00** não recebiam o desconto, garantindo que o código reflita corretamente a intenção da equipe de vendas.
3.  **Método Claro:** O método foi renomeado para `calcularPrecoComDesconto`, descrevendo o que de fato é retornado.
*/