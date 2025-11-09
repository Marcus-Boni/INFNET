package br.edu.infnet.Ex01;

/**
 * Ponto de entrada da aplicação (Camada de Apresentação/Execução).
 * É responsável por consumir a lógica de negócio e exibir os resultados.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Instancia a camada de serviço
        CarrinhoDeCompras meuCarrinho = new CarrinhoDeCompras();

        // 2. Usa a API de negócio para executar os casos de uso
        try {
            meuCarrinho.adicionarProduto("Notebook Gamer", 5500.00, 1);
            meuCarrinho.adicionarProduto("Mouse Óptico", 120.50, 2);
            // meuCarrinho.adicionarProduto("Teclado", -10.00, 1); // <-- Iria falhar, como esperado

        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
            return;
        }

        // 3. Solicita o resultado do cálculo
        double total = meuCarrinho.getValorTotal();

        // 4. A camada de apresentação decide como formatar e exibir o resultado.
        // O CarrinhoDeCompras não tem conhecimento desta lógica.
        System.out.println("=== Recibo da Compra ===");
        System.out.println("Valor total a pagar: R$ " + String.format("%.2f", total));
    }
}