package org.example.banco.cli;

import org.example.banco.entity.Conta;
import org.example.banco.exceptions.ContaNotFoundException;
import org.example.banco.exceptions.DadosInvalidosException;
import org.example.banco.exceptions.OperacaoInvalidaException;
import org.example.banco.service.ContaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de linha de comando para o sistema bancário.
 * Fornece um menu interativo para realizar operações CRUD em contas bancárias.
 * 
 * Características:
 * - Menu intuitivo e organizado
 * - Tratamento robusto de erros
 * - Validação de entrada do usuário
 * - Mensagens claras e informativas
 */
@Component
public class ContaBancariaCLI {

    private static final String SEPARADOR = "=====================================";
    private static final String TITULO_SISTEMA = "Sistema de Gerenciamento de Contas Bancárias";
    
    private final ContaService contaService;
    private final Scanner scanner;
    private boolean sistemaAtivo;

    public ContaBancariaCLI(final ContaService contaService) {
        this.contaService = contaService;
        this.scanner = new Scanner(System.in);
        this.sistemaAtivo = true;
    }

    /**
     * Inicia a interface de linha de comando.
     */
    public void iniciar() {
        exibirBoasVindas();
        
        while (sistemaAtivo) {
            try {
                exibirMenuPrincipal();
                processarOpcaoMenu();
            } catch (Exception e) {
                exibirErroInesperado(e);
            }
        }
        
        encerrarSistema();
    }

    private void exibirBoasVindas() {
        limparTela();
        System.out.println(SEPARADOR);
        System.out.println(TITULO_SISTEMA);
        System.out.println(SEPARADOR);
        System.out.println();
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n" + SEPARADOR);
        System.out.println("MENU PRINCIPAL");
        System.out.println(SEPARADOR);
        System.out.println("1 - Criar Nova Conta");
        System.out.println("2 - Listar Todas as Contas");
        System.out.println("3 - Consultar Conta por ID");
        System.out.println("4 - Depositar");
        System.out.println("5 - Sacar");
        System.out.println("6 - Atualizar Saldo");
        System.out.println("7 - Alterar Nome do Titular");
        System.out.println("8 - Excluir Conta");
        System.out.println("9 - Estatísticas do Sistema");
        System.out.println("0 - Sair");
        System.out.println(SEPARADOR);
        System.out.print("Escolha uma opção: ");
    }

    private void processarOpcaoMenu() {
        try {
            final String entrada = scanner.nextLine().trim();
            
            if (entrada.isEmpty()) {
                exibirErro("Por favor, digite uma opção válida.");
                return;
            }

            final OpcaoMenu opcao = OpcaoMenu.fromString(entrada);
            executarOpcao(opcao);
            
        } catch (IllegalArgumentException e) {
            exibirErro("Opção inválida. Por favor, escolha uma opção entre 0 e 9.");
        }
    }

    private void executarOpcao(final OpcaoMenu opcao) {
        switch (opcao) {
            case CRIAR_CONTA -> criarNovaConta();
            case LISTAR_CONTAS -> listarTodasContas();
            case CONSULTAR_CONTA -> consultarContaPorId();
            case DEPOSITAR -> realizarDeposito();
            case SACAR -> realizarSaque();
            case ATUALIZAR_SALDO -> atualizarSaldo();
            case ALTERAR_TITULAR -> alterarNomeTitular();
            case EXCLUIR_CONTA -> excluirConta();
            case ESTATISTICAS -> exibirEstatisticas();
            case SAIR -> solicitarSaida();
        }
    }

    // ==================== OPERAÇÕES DO MENU ====================

    private void criarNovaConta() {
        exibirTituloOperacao("Criar Nova Conta");
        
        try {
            System.out.print("Digite o nome do titular: ");
            final String nome = scanner.nextLine().trim();
            
            System.out.print("Digite o saldo inicial (ou pressione Enter para saldo zero): ");
            final String saldoStr = scanner.nextLine().trim();
            
            final Long contaId;
            if (saldoStr.isEmpty()) {
                contaId = contaService.criarContaComSaldoZero(nome);
            } else {
                final Double saldo = Double.parseDouble(saldoStr);
                contaId = contaService.criarConta(nome, saldo);
            }
            
            exibirSucesso("Conta criada com sucesso! ID: " + contaId);
            
        } catch (NumberFormatException e) {
            exibirErro("Valor de saldo inválido. Digite um número válido.");
        } catch (DadosInvalidosException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro ao criar conta: " + e.getMessage());
        }
    }

    private void listarTodasContas() {
        exibirTituloOperacao("Lista de Contas");
        
        try {
            final List<Conta> contas = contaService.listarTodasContas();
            
            if (contas.isEmpty()) {
                System.out.println("Nenhuma conta cadastrada no sistema.");
                return;
            }
            
            System.out.println(String.format("\nTotal de contas: %d\n", contas.size()));
            System.out.println(String.format("%-6s | %-30s | %15s", "ID", "Titular", "Saldo"));
            System.out.println("-".repeat(55));
            
            for (Conta conta : contas) {
                System.out.println(String.format(
                    "%-6d | %-30s | %15s",
                    conta.getId(),
                    conta.getNomeTitularString(),
                    conta.getSaldo()
                ));
            }
            
        } catch (Exception e) {
            exibirErro("Erro ao listar contas: " + e.getMessage());
        }
    }

    private void consultarContaPorId() {
        exibirTituloOperacao("Consultar Conta");
        
        try {
            final Long contaId = solicitarIdConta();
            final Conta conta = contaService.buscarContaPorId(contaId);
            
            System.out.println("\nDetalhes da Conta:");
            System.out.println("-".repeat(40));
            System.out.println("ID: " + conta.getId());
            System.out.println("Titular: " + conta.getNomeTitularString());
            System.out.println("Saldo: " + conta.getSaldo());
            System.out.println("Saldo zerado: " + (conta.isSaldoZerado() ? "Sim" : "Não"));
            
        } catch (ContaNotFoundException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro ao consultar conta: " + e.getMessage());
        }
    }

    private void realizarDeposito() {
        exibirTituloOperacao("Realizar Depósito");
        
        try {
            final Long contaId = solicitarIdConta();
            
            System.out.print("Digite o valor do depósito: ");
            final String valorStr = scanner.nextLine().trim();
            final BigDecimal valor = new BigDecimal(valorStr);
            
            contaService.depositar(contaId, valor);
            exibirSucesso("Depósito realizado com sucesso!");
            
            // Exibir saldo atualizado
            final Conta conta = contaService.buscarContaPorId(contaId);
            System.out.println("Novo saldo: " + conta.getSaldo());
            
        } catch (NumberFormatException e) {
            exibirErro("Valor inválido. Digite um número válido.");
        } catch (ContaNotFoundException | OperacaoInvalidaException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro ao realizar depósito: " + e.getMessage());
        }
    }

    private void realizarSaque() {
        exibirTituloOperacao("Realizar Saque");
        
        try {
            final Long contaId = solicitarIdConta();
            
            // Mostrar saldo atual
            final Conta conta = contaService.buscarContaPorId(contaId);
            System.out.println("Saldo disponível: " + conta.getSaldo());
            
            System.out.print("Digite o valor do saque: ");
            final String valorStr = scanner.nextLine().trim();
            final BigDecimal valor = new BigDecimal(valorStr);
            
            contaService.sacar(contaId, valor);
            exibirSucesso("Saque realizado com sucesso!");
            
            // Exibir saldo atualizado
            final Conta contaAtualizada = contaService.buscarContaPorId(contaId);
            System.out.println("Novo saldo: " + contaAtualizada.getSaldo());
            
        } catch (NumberFormatException e) {
            exibirErro("Valor inválido. Digite um número válido.");
        } catch (ContaNotFoundException | OperacaoInvalidaException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro ao realizar saque: " + e.getMessage());
        }
    }

    private void atualizarSaldo() {
        exibirTituloOperacao("Atualizar Saldo");
        
        try {
            final Long contaId = solicitarIdConta();
            
            // Mostrar saldo atual
            final Conta conta = contaService.buscarContaPorId(contaId);
            System.out.println("Saldo atual: " + conta.getSaldo());
            
            System.out.print("Digite o novo saldo: ");
            final String saldoStr = scanner.nextLine().trim();
            final Double novoSaldo = Double.parseDouble(saldoStr);
            
            contaService.atualizarSaldo(contaId, novoSaldo);
            exibirSucesso("Saldo atualizado com sucesso!");
            
        } catch (NumberFormatException e) {
            exibirErro("Valor inválido. Digite um número válido.");
        } catch (ContaNotFoundException | DadosInvalidosException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro ao atualizar saldo: " + e.getMessage());
        }
    }

    private void alterarNomeTitular() {
        exibirTituloOperacao("Alterar Nome do Titular");
        
        try {
            final Long contaId = solicitarIdConta();
            
            // Mostrar nome atual
            final Conta conta = contaService.buscarContaPorId(contaId);
            System.out.println("Nome atual: " + conta.getNomeTitularString());
            
            System.out.print("Digite o novo nome: ");
            final String novoNome = scanner.nextLine().trim();
            
            contaService.alterarNomeTitular(contaId, novoNome);
            exibirSucesso("Nome do titular alterado com sucesso!");
            
        } catch (ContaNotFoundException | DadosInvalidosException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro ao alterar nome: " + e.getMessage());
        }
    }

    private void excluirConta() {
        exibirTituloOperacao("Excluir Conta");
        
        try {
            final Long contaId = solicitarIdConta();
            
            // Mostrar informações da conta
            final Conta conta = contaService.buscarContaPorId(contaId);
            System.out.println("\nConta a ser excluída:");
            System.out.println("ID: " + conta.getId());
            System.out.println("Titular: " + conta.getNomeTitularString());
            System.out.println("Saldo: " + conta.getSaldo());
            
            System.out.print("\nConfirma a exclusão? (S/N): ");
            final String confirmacao = scanner.nextLine().trim().toUpperCase();
            
            if (confirmacao.equals("S") || confirmacao.equals("SIM")) {
                contaService.excluirConta(contaId);
                exibirSucesso("Conta excluída com sucesso!");
            } else {
                System.out.println("Exclusão cancelada.");
            }
            
        } catch (ContaNotFoundException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro ao excluir conta: " + e.getMessage());
        }
    }

    private void exibirEstatisticas() {
        exibirTituloOperacao("Estatísticas do Sistema");
        
        try {
            final long totalContas = contaService.contarContas();
            final List<Conta> contas = contaService.listarTodasContas();
            
            BigDecimal saldoTotal = BigDecimal.ZERO;
            int contasComSaldoZero = 0;
            
            for (Conta conta : contas) {
                saldoTotal = saldoTotal.add(conta.getSaldo().getValor());
                if (conta.isSaldoZerado()) {
                    contasComSaldoZero++;
                }
            }
            
            System.out.println("\nTotal de contas: " + totalContas);
            System.out.println("Contas com saldo zero: " + contasComSaldoZero);
            System.out.println("Saldo total do sistema: R$ " + String.format("%.2f", saldoTotal));
            
            if (totalContas > 0) {
                final BigDecimal saldoMedio = saldoTotal.divide(
                    BigDecimal.valueOf(totalContas), 
                    2, 
                    java.math.RoundingMode.HALF_UP
                );
                System.out.println("Saldo médio por conta: R$ " + String.format("%.2f", saldoMedio));
            }
            
        } catch (Exception e) {
            exibirErro("Erro ao calcular estatísticas: " + e.getMessage());
        }
    }

    private void solicitarSaida() {
        System.out.print("\nDeseja realmente sair? (S/N): ");
        final String confirmacao = scanner.nextLine().trim().toUpperCase();
        
        if (confirmacao.equals("S") || confirmacao.equals("SIM")) {
            sistemaAtivo = false;
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Long solicitarIdConta() {
        System.out.print("Digite o ID da conta: ");
        final String idStr = scanner.nextLine().trim();
        return Long.parseLong(idStr);
    }

    private void exibirTituloOperacao(final String titulo) {
        System.out.println("\n" + SEPARADOR);
        System.out.println(titulo);
        System.out.println(SEPARADOR);
    }

    private void exibirSucesso(final String mensagem) {
        System.out.println("\n✓ " + mensagem);
    }

    private void exibirErro(final String mensagem) {
        System.err.println("\n✗ ERRO: " + mensagem);
    }

    private void exibirErroInesperado(final Exception e) {
        System.err.println("\n✗ ERRO INESPERADO: " + e.getMessage());
        System.err.println("Por favor, tente novamente.");
    }

    private void encerrarSistema() {
        System.out.println("\n" + SEPARADOR);
        System.out.println("Sistema encerrado. Obrigado por usar!");
        System.out.println(SEPARADOR);
        scanner.close();
    }

    private void limparTela() {
        // Método para limpar a tela (funciona melhor em terminais Unix/Linux)
        // Em Windows pode não funcionar perfeitamente
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Enum que representa as opções do menu principal.
     * Uso de enum garante type safety e elimina valores mágicos.
     */
    private enum OpcaoMenu {
        SAIR("0"),
        CRIAR_CONTA("1"),
        LISTAR_CONTAS("2"),
        CONSULTAR_CONTA("3"),
        DEPOSITAR("4"),
        SACAR("5"),
        ATUALIZAR_SALDO("6"),
        ALTERAR_TITULAR("7"),
        EXCLUIR_CONTA("8"),
        ESTATISTICAS("9");

        private final String codigo;

        OpcaoMenu(final String codigo) {
            this.codigo = codigo;
        }

        public static OpcaoMenu fromString(final String codigo) {
            for (OpcaoMenu opcao : OpcaoMenu.values()) {
                if (opcao.codigo.equals(codigo)) {
                    return opcao;
                }
            }
            throw new IllegalArgumentException("Código de opção inválido: " + codigo);
        }
    }
}
