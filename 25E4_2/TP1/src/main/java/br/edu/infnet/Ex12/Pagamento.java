class Pagamento {

    public void processarPagamento(double valor, String metodo) 
            throws MetodoPagamentoInvalidoException { 

        if (metodo == null || metodo.trim().isEmpty()) {
            throw new MetodoPagamentoInvalidoException("Método de pagamento não pode ser nulo ou vazio.");
        }

        if (metodo.equalsIgnoreCase("cartao")) {
            System.out.println("Pagamento de R$" + valor + " realizado via cartão.");
        
        } else if (metodo.equalsIgnoreCase("boleto")) {
            System.out.println("Pagamento de R$" + valor + " realizado via boleto.");
        
        } else {
            throw new MetodoPagamentoInvalidoException(
                "Erro: O método de pagamento '" + metodo + "' não é válido ou suportado."
            );
        }
    }
}