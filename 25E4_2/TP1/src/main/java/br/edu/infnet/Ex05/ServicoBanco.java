class ServicoBanco {

    private final Map<String, ContaBancaria> contas;

    public ServicoBanco() {
        this.contas = new HashMap<>();
    }

    public boolean criarConta(String titular, double saldoInicial) {
        if (titular == null || titular.trim().isEmpty() || contas.containsKey(titular)) {
            return false;
        }
        
        ContaBancaria novaConta = new ContaBancaria(titular, saldoInicial);
        contas.put(titular, novaConta);
        return true;
    }

    public boolean transferir(String titularOrigem, String titularDestino, double valor) {
        ContaBancaria origem = contas.get(titularOrigem);
        ContaBancaria destino = contas.get(titularDestino);

        if (origem == null || destino == null) {
            return false;
        }
        
        boolean sucessoSaque = origem.sacar(valor);

        if (sucessoSaque) {
            destino.depositar(valor);
            return true;
        }

        return false;
    }

    public double consultarSaldo(String titular) {
        ContaBancaria conta = contas.get(titular);
        if (conta != null) {
            return conta.getSaldo();
        }
        return -1;
    }
}