public class ContaBancaria {
    private double saldo;

    public ContaBancaria(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    /**
     * @brief Consulta (Query): Verifica se o saldo é suficiente. 
     * NÃO modifica o estado.
     */
    public boolean podeComprar(double valor) {
        return saldo >= valor;
    }

    /**
     * @brief Comando (Command): Realiza a compra (modifica o estado).
     * Não retorna valor (void) ou retorna o novo estado/sucesso.
     */
    public void realizarCompra(double valor) {
        if (!podeComprar(valor)) {
            // Explicação: Lança uma exceção se a pré-condição não for atendida.
            throw new IllegalStateException("Saldo insuficiente para realizar a compra."); 
        }
        // Explicação: A modificação do estado está isolada neste método.
        saldo -= valor; 
    }
    
    // Getter para o saldo
    public double getSaldo() { return saldo; }
}

/*
Proposta de Refatoração Seguindo CQS:
1.  **`podeComprar(double valor)`:** Foi mantido como uma **Query**. Ele apenas consulta o estado do objeto e retorna um `boolean`, sem alterar o `saldo`.
2.  **`realizarCompra(double valor)`:** É um **Command**. Ele executa a modificação do estado (`saldo -= valor`). A lógica de verificação (`podeComprar`) é chamada internamente como uma **pré-condição**, garantindo que a modificação só ocorra se for válida. A separação garante que a simples verificação não tenha efeitos colaterais.
*/