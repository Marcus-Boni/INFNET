package org.example.banco.service;

import org.example.banco.entity.Conta;
import org.example.banco.exceptions.ContaNotFoundException;
import org.example.banco.exceptions.DadosInvalidosException;
import org.example.banco.exceptions.OperacaoInvalidaException;
import org.example.banco.repository.ContaRepository;
import org.example.banco.valueobjects.NomeTitular;
import org.example.banco.valueobjects.Saldo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar operações relacionadas a contas bancárias.
 * 
 * Este serviço aplica o princípio de Command Query Separation (CQS):
 * - Comandos (Commands): modificam o estado do sistema e não retornam valores
 * - Consultas (Queries): retornam valores sem modificar o estado
 * 
 * Todas as operações são transacionais e incluem validações robustas.
 */
@Service
@Transactional(readOnly = true)
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(final ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    // ==================== COMMANDS ====================
    // Operações que modificam o estado do sistema

    /**
     * Command: cria uma nova conta bancária.
     *
     * @param nomeTitular o nome do titular da conta
     * @param saldoInicial o saldo inicial da conta
     * @return o ID da conta criada
     * @throws DadosInvalidosException se os dados forem inválidos
     */
    @Transactional
    public Long criarConta(final String nomeTitular, final Double saldoInicial) {
        try {
            validarDadosNaoCriacao(nomeTitular, saldoInicial);
            
            final NomeTitular nome = NomeTitular.de(nomeTitular);
            final Saldo saldo = Saldo.de(saldoInicial);
            
            final Conta novaConta = Conta.criar(nome, saldo);
            final Conta contaSalva = contaRepository.save(novaConta);
            
            return contaSalva.getId();
            
        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException(
                "Erro ao criar conta: " + e.getMessage(), e
            );
        }
    }

    /**
     * Command: cria uma nova conta com saldo inicial zero.
     *
     * @param nomeTitular o nome do titular da conta
     * @return o ID da conta criada
     * @throws DadosInvalidosException se o nome for inválido
     */
    @Transactional
    public Long criarContaComSaldoZero(final String nomeTitular) {
        try {
            validarNomeTitular(nomeTitular);
            
            final NomeTitular nome = NomeTitular.de(nomeTitular);
            final Conta novaConta = Conta.criarComSaldoZero(nome);
            final Conta contaSalva = contaRepository.save(novaConta);
            
            return contaSalva.getId();
            
        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException(
                "Erro ao criar conta: " + e.getMessage(), e
            );
        }
    }

    /**
     * Command: atualiza o saldo de uma conta existente.
     *
     * @param contaId o ID da conta
     * @param novoSaldo o novo saldo da conta
     * @throws ContaNotFoundException se a conta não for encontrada
     * @throws DadosInvalidosException se o saldo for inválido
     */
    @Transactional
    public void atualizarSaldo(final Long contaId, final Double novoSaldo) {
        validarContaId(contaId);
        validarSaldo(novoSaldo);

        try {
            final Conta conta = buscarContaOuLancarExcecao(contaId);
            final Saldo saldo = Saldo.de(novoSaldo);
            
            conta.atualizarSaldo(saldo);
            contaRepository.save(conta);
            
        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException(
                "Erro ao atualizar saldo: " + e.getMessage(), e
            );
        }
    }

    /**
     * Command: realiza um depósito em uma conta.
     *
     * @param contaId o ID da conta
     * @param valor o valor a ser depositado
     * @throws ContaNotFoundException se a conta não for encontrada
     * @throws OperacaoInvalidaException se o valor for inválido
     */
    @Transactional
    public void depositar(final Long contaId, final BigDecimal valor) {
        validarContaId(contaId);
        validarValorOperacao(valor);

        try {
            final Conta conta = buscarContaOuLancarExcecao(contaId);
            conta.depositar(valor);
            contaRepository.save(conta);
            
        } catch (IllegalArgumentException e) {
            throw new OperacaoInvalidaException(
                "Erro ao realizar depósito: " + e.getMessage(), e
            );
        }
    }

    /**
     * Command: realiza um saque de uma conta.
     *
     * @param contaId o ID da conta
     * @param valor o valor a ser sacado
     * @throws ContaNotFoundException se a conta não for encontrada
     * @throws OperacaoInvalidaException se o valor for inválido ou saldo insuficiente
     */
    @Transactional
    public void sacar(final Long contaId, final BigDecimal valor) {
        validarContaId(contaId);
        validarValorOperacao(valor);

        try {
            final Conta conta = buscarContaOuLancarExcecao(contaId);
            conta.sacar(valor);
            contaRepository.save(conta);
            
        } catch (IllegalArgumentException e) {
            throw new OperacaoInvalidaException(
                "Erro ao realizar saque: " + e.getMessage(), e
            );
        }
    }

    /**
     * Command: altera o nome do titular de uma conta.
     *
     * @param contaId o ID da conta
     * @param novoNome o novo nome do titular
     * @throws ContaNotFoundException se a conta não for encontrada
     * @throws DadosInvalidosException se o nome for inválido
     */
    @Transactional
    public void alterarNomeTitular(final Long contaId, final String novoNome) {
        validarContaId(contaId);
        validarNomeTitular(novoNome);

        try {
            final Conta conta = buscarContaOuLancarExcecao(contaId);
            final NomeTitular nome = NomeTitular.de(novoNome);
            
            conta.alterarNomeTitular(nome);
            contaRepository.save(conta);
            
        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException(
                "Erro ao alterar nome do titular: " + e.getMessage(), e
            );
        }
    }

    /**
     * Command: exclui uma conta do sistema.
     *
     * @param contaId o ID da conta a ser excluída
     * @throws ContaNotFoundException se a conta não for encontrada
     */
    @Transactional
    public void excluirConta(final Long contaId) {
        validarContaId(contaId);

        if (!contaRepository.existsById(contaId)) {
            throw new ContaNotFoundException(contaId);
        }

        try {
            contaRepository.deleteById(contaId);
        } catch (Exception e) {
            throw new OperacaoInvalidaException(
                "Erro ao excluir conta: " + e.getMessage(), e
            );
        }
    }

    // ==================== QUERIES ====================
    // Operações que apenas consultam dados sem modificar o estado

    /**
     * Query: busca uma conta por ID.
     *
     * @param contaId o ID da conta
     * @return a conta encontrada
     * @throws ContaNotFoundException se a conta não for encontrada
     */
    public Conta buscarContaPorId(final Long contaId) {
        validarContaId(contaId);
        return buscarContaOuLancarExcecao(contaId);
    }

    /**
     * Query: busca uma conta por ID, retornando Optional.
     *
     * @param contaId o ID da conta
     * @return Optional contendo a conta, ou vazio se não encontrada
     */
    public Optional<Conta> buscarContaOpcional(final Long contaId) {
        validarContaId(contaId);
        return contaRepository.findById(contaId);
    }

    /**
     * Query: retorna todas as contas cadastradas.
     *
     * @return lista de todas as contas
     */
    public List<Conta> listarTodasContas() {
        return contaRepository.findAll();
    }

    /**
     * Query: verifica se uma conta existe.
     *
     * @param contaId o ID da conta
     * @return true se a conta existe, false caso contrário
     */
    public boolean contaExiste(final Long contaId) {
        if (contaId == null) {
            return false;
        }
        return contaRepository.existsById(contaId);
    }

    /**
     * Query: retorna o total de contas cadastradas.
     *
     * @return o número total de contas
     */
    public long contarContas() {
        return contaRepository.count();
    }

    // ==================== MÉTODOS PRIVADOS DE VALIDAÇÃO ====================

    private Conta buscarContaOuLancarExcecao(final Long contaId) {
        return contaRepository.findById(contaId)
            .orElseThrow(() -> new ContaNotFoundException(contaId));
    }

    private void validarContaId(final Long contaId) {
        if (contaId == null) {
            throw new IllegalArgumentException("ID da conta não pode ser nulo");
        }
        if (contaId <= 0) {
            throw new IllegalArgumentException("ID da conta deve ser positivo");
        }
    }

    private void validarNomeTitular(final String nomeTitular) {
        if (nomeTitular == null || nomeTitular.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do titular não pode ser nulo ou vazio");
        }
    }

    private void validarSaldo(final Double saldo) {
        if (saldo == null) {
            throw new IllegalArgumentException("Saldo não pode ser nulo");
        }
        if (saldo < 0) {
            throw new IllegalArgumentException("Saldo não pode ser negativo");
        }
    }

    private void validarValorOperacao(final BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor da operação não pode ser nulo");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da operação deve ser positivo");
        }
    }

    private void validarDadosNaoCriacao(final String nomeTitular, final Double saldo) {
        validarNomeTitular(nomeTitular);
        validarSaldo(saldo);
    }
}
