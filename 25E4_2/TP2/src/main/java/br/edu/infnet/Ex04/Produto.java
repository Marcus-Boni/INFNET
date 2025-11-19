public class Produto {
    private final String nome;
    private final double preco;

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public double getPreco() { return preco; }
    public String getNome() { return nome; }
    
    // Método para criar uma nova instância com preço modificado (Imutabilidade)
    public Produto comDesconto(double valorDesconto) {
        return new Produto(this.nome, this.preco - valorDesconto);
    }
}

public class Ajuste {
    public double calcularNovoPrecoComDesconto(Produto produto) {
        final double DESCONTO = 10.0;
        // Explicação: O método calcula e retorna o NOVO valor, preservando o objeto original.
        return produto.getPreco() - DESCONTO; 
    }

    public Produto aplicarDesconto(Produto produto) {
        final double DESCONTO = 10.0;
        // Explicação: Retorna um novo objeto Produto com o preço alterado. 
        // O objeto original permanece intacto.
        return produto.comDesconto(DESCONTO); 
    }
}

/*
Explicação Detalhada da Importância de Evitar Mudanças Diretas:
O princípio de **evitar a mutação de parâmetros** (e promover a **imutabilidade** dos objetos) é crucial para a **previsibilidade** e **segurança** do código. Ao retornar um novo valor ou um novo objeto (em vez de modificar o original), garantimos que:
1.  **O Objeto Original é Preservado:** Sua referência em outros locais do sistema não é afetada.
2.  **Transparência Referencial:** O método não tem **efeitos colaterais** ocultos, tornando-o mais fácil de entender e testar.
3.  **Segurança em Concorrência:** Objetos imutáveis são intrinsecamente seguros para threads, pois seu estado não pode ser alterado após a criação.
*/