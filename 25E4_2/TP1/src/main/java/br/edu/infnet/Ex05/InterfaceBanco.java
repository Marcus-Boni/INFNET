
public class InterfaceBanco {

    public static void main(String[] args) {
        ServicoBanco banco = new ServicoBanco();

        banco.criarConta("Alice", 1000.00);
        banco.criarConta("Bob", 500.00);

        System.out.println("--- Estado Inicial ---");
        System.out.println("Saldo Alice: " + banco.consultarSaldo("Alice"));
        System.out.println("Saldo Bob: " + banco.consultarSaldo("Bob"));
        System.out.println("----------------------");

        System.out.println("\nTentando transferir R$ 200 de Alice para Bob...");
        boolean sucesso1 = banco.transferir("Alice", "Bob", 200.00);
        
        if (sucesso1) {
            System.out.println(">> Transferência realizada com sucesso.");
        } else {
            System.out.println(">> Erro na transferência.");
        }

        System.out.println("\n--- Estado Pós-Sucesso ---");
        System.out.println("Saldo Alice: " + banco.consultarSaldo("Alice"));
        System.out.println("Saldo Bob: " + banco.consultarSaldo("Bob"));
        System.out.println("----------------------");

        System.out.println("\nTentando transferir R$ 1000 de Bob para Alice...");
        boolean sucesso2 = banco.transferir("Bob", "Alice", 1000.00);
        
        if (sucesso2) {
            System.out.println(">> Transferência realizada com sucesso.");
        } else {
            System.out.println(">> Erro na transferência (provavelmente saldo insuficiente).");
        }

        System.out.println("\n--- Estado Pós-Falha ---");
        System.out.println("Saldo Alice: " + banco.consultarSaldo("Alice"));
        System.out.println("Saldo Bob: " + banco.consultarSaldo("Bob"));
        System.out.println("----------------------");
    }
}