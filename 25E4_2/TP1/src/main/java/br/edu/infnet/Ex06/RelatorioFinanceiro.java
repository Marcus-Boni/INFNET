import java.util.List;
import java.util.ArrayList;

public class RelatorioFinanceiro {

    public void gerarRelatorio(List<String> clientes, List<Double> saldos) {
        this.imprimirCabecalho();
        
        this.imprimirCorpo(clientes, saldos);
        
        this.imprimirRodape();
    }

    private void imprimirCabecalho() {
        System.out.println("=== Relatório Financeiro ===");
    }

    private void imprimirCorpo(List<String> clientes, List<Double> saldos) {
        for (int i = 0; i < clientes.size(); i++) {
            this.imprimirLinha(clientes.get(i), saldos.get(i));
        }
    }

    private void imprimirLinha(String cliente, double saldo) {
        System.out.println("Cliente: " + cliente + " - Saldo: R$ " + saldo);
    }

    private void imprimirRodape() {
        System.out.println("===========================");
        System.out.println("Fim do Relatório");
    }

    public static void main(String[] args) {
        List<String> nomesClientes = new ArrayList<>();
        List<Double> saldosClientes = new ArrayList<>();
        
        nomesClientes.add("Alice");
        saldosClientes.add(1200.50);
        
        nomesClientes.add("Bob");
        saldosClientes.add(850.00);
        
        nomesClientes.add("Carlos");
        saldosClientes.add(3250.75);

        RelatorioFinanceiro relatorio = new RelatorioFinanceiro();
        relatorio.gerarRelatorio(nomesClientes, saldosClientes);
    }
}