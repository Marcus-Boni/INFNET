/**
 * Representa uma entidade de Pedido no sistema.
 *
 * Esta classe é projetada para ser imutável: uma vez que um Pedido
 * é criado, seu estado (ID, descrição e valor) não pode ser alterado.
 */
public class Pedido {

    // O uso de 'final' torna a intenção de imutabilidade explícita
    private final int id;
    private final String descricao;
    private final double valor;

    public Pedido(int id, String descricao, double valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }
}