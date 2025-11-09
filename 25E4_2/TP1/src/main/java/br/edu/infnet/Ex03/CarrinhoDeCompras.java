package br.edu.infnet.Ex01;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia uma coleção de itens e calcula o valor total.
 * Esta classe não sabe como o resultado será exibido (console, API, app).
 */
public class CarrinhoDeCompras {

    // A implementação interna (ArrayList) está oculta.
    // Gerenciamos UMA lista de objetos coesos, não três listas de primitivos.
    private final List<ItemCarrinho> itens;

    public CarrinhoDeCompras() {
        this.itens = new ArrayList<>();
    }

    /**
     * API clara para o requisito funcional "Cadastrar itens".
     * A complexidade de criar o objeto ItemCarrinho é encapsulada aqui.
     */
    public void adicionarProduto(String nome, double preco, int quantidade) {
        // A lógica de validação está delegada ao construtor de ItemCarrinho
        ItemCarrinho novoItem = new ItemCarrinho(nome, preco, quantidade);
        this.itens.add(novoItem);
    }

    /**
     * API clara para o requisito "Calcular o total da compra".
     * Este método RETORNA o valor, cumprindo o requisito de desacoplamento.
     * É legível, modular e perfeitamente testável.
     */
    public double getValorTotal() {
        double total = 0.0;

        // A lógica lê como prosa: "Para cada item no carrinho..."
        for (ItemCarrinho item : this.itens) {
            // Delega a responsabilidade do cálculo do subtotal para o próprio item
            total += item.getSubtotal();
        }
        return total;
    }

    // Outros métodos poderiam ser adicionados (ex: removerItem, getQuantidadeItens)
}