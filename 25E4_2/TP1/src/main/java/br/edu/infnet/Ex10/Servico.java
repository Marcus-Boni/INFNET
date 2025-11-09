public class Servico {

    public void processar(String dado) {
        if (dado == null) {
            throw new IllegalArgumentException("O dado fornecido para processamento não pode ser nulo.");
        }
        
        System.out.println("Processando: " + dado.toUpperCase());
    }

    public static void main(String[] args) {
        Servico servico = new Servico();

        // 1. Caso de sucesso
        System.out.println("Tentando processar 'teste'...");
        servico.processar("teste");

        // 2. Caso de falha (controlada)
        System.out.println("\nTentando processar 'null'...");
        try {
            servico.processar(null);
        } catch (IllegalArgumentException e) {
            System.err.println("ERRO TRATADO: " + e.getMessage());
        }
        
        System.out.println("\nA aplicação continua executando após o erro.");
    }
}