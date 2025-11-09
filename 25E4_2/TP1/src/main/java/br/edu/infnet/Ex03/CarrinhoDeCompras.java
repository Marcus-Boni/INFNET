import java.util.ArrayList;
import java.util.List;

public class CarrinhoDeCompras {
    private final List<ItemCarrinho> itens;

    public CarrinhoDeCompras() {
        this.itens = new ArrayList<>();
    }

    public void adicionarProduto(String nome, double preco, int quantidade) {
        ItemCarrinho novoItem = new ItemCarrinho(nome, preco, quantidade);
        this.itens.add(novoItem);
    }

    public double getValorTotal() {
        double total = 0.0;
        
        for (ItemCarrinho item : this.itens) {
            total += item.getSubtotal();
        }
        return total;
    }
    
}