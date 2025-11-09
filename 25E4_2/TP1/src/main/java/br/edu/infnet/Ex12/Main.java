public class Main {

    public static void main(String[] args) {
        Pagamento servicoPagamento = new Pagamento();

        try {
            System.out.println("Tentando pagar 100.00 com 'cartao'...");
            servicoPagamento.processarPagamento(100.00, "cartao");
        
        } catch (MetodoPagamentoInvalidoException e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }

        System.out.println("---");

        try {
            System.out.println("Tentando pagar 50.00 com 'pix'...");
            servicoPagamento.processarPagamento(50.00, "pix");
        
        } catch (MetodoPagamentoInvalidoException e) {
            System.err.println("FALHA NO PAGAMENTO: " + e.getMessage());
        }

        System.out.println("\nO programa continua executando após a falha.");
    }
}