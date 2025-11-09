public class ContaBancaria {
    private String titular;
    private double saldo;

    public ContaBancaria(String titular, double saldo) {
        if (titular == null || titular.trim().isEmpty()) {
            throw new IllegalArgumentException("Titular não pode ser nulo ou vazio.");
        }
        if (saldo < 0) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo.");
        }
        this.titular = titular;
        this.saldo = saldo;
    }

    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo.");
        }
        this.saldo += valor;
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo.");
        }
        if (this.saldo < valor) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        this.saldo -= valor;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTitular() {
        return titular;
    }

    public static void main(String[] args) {
        ContaBancaria conta1 = new ContaBancaria("Alice", 100.0);
        System.out.println("Conta de " + conta1.getTitular() + " criada com saldo: " + conta1.getSaldo());

        conta1.depositar(50.0);
        System.out.println("Saldo após depósito: " + conta1.getSaldo());

        conta1.sacar(30.0);
        System.out.println("Saldo após saque: " + conta1.getSaldo());

        try {
            System.out.println("Tentando sacar 200.0...");
            conta1.sacar(200.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println("Saldo final (Alice): " + conta1.getSaldo());

        try {
            System.out.println("Tentando depositar -50.0...");
            conta1.depositar(-50.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println("Saldo final (Alice): " + conta1.getSaldo());

        try {
            System.out.println("Tentando criar conta com saldo -100...");
            new ContaBancaria("Bob", -100.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}