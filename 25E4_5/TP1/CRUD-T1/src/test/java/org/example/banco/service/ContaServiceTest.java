package org.example.banco.service;

import org.example.banco.entity.Conta;
import org.example.banco.exceptions.ContaNotFoundException;
import org.example.banco.exceptions.DadosInvalidosException;
import org.example.banco.exceptions.OperacaoInvalidaException;
import org.example.banco.repository.ContaRepository;
import org.example.banco.valueobjects.NomeTitular;
import org.example.banco.valueobjects.Saldo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ContaService.
 * 
 * Utiliza Mockito para isolar a lógica de negócio do repositório.
 * Testa Commands e Queries separadamente seguindo CQS.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ContaService - Testes Unitários")
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    private Conta contaTeste;

    @BeforeEach
    void setUp() {
        contaTeste = Conta.criar(
            NomeTitular.de("João Silva"),
            Saldo.de(new BigDecimal("1000.00"))
        );
        // Simular que a conta foi salva com ID
        contaTeste = new Conta(NomeTitular.de("João Silva"), Saldo.de(new BigDecimal("1000.00")));
    }

    @Nested
    @DisplayName("Commands - Criar Conta")
    class CriarConta {

        @Test
        @DisplayName("Deve criar conta com sucesso")
        void deveCriarContaComSucesso() {
            // Arrange
            Conta contaMock = Conta.criar(
                NomeTitular.de("Maria Santos"),
                Saldo.de(500.0)
            );
            
            when(contaRepository.save(any(Conta.class))).thenReturn(contaMock);

            // Act
            Long contaId = contaService.criarConta("Maria Santos", 500.0);

            // Assert
            verify(contaRepository, times(1)).save(any(Conta.class));
        }

        @Test
        @DisplayName("Deve criar conta com saldo zero")
        void deveCriarContaComSaldoZero() {
            // Arrange
            Conta contaMock = Conta.criarComSaldoZero(NomeTitular.de("Pedro Oliveira"));
            when(contaRepository.save(any(Conta.class))).thenReturn(contaMock);

            // Act
            Long contaId = contaService.criarContaComSaldoZero("Pedro Oliveira");

            // Assert
            verify(contaRepository, times(1)).save(any(Conta.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar conta com nome inválido")
        void deveLancarExcecaoAoCriarComNomeInvalido() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.criarConta("AB", 100.0))
                .isInstanceOf(DadosInvalidosException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar conta com saldo negativo")
        void deveLancarExcecaoAoCriarComSaldoNegativo() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.criarConta("João Silva", -100.0))
                .isInstanceOf(DadosInvalidosException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar conta com nome nulo")
        void deveLancarExcecaoAoCriarComNomeNulo() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.criarConta(null, 100.0))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Commands - Atualizar Saldo")
    class AtualizarSaldo {

        @Test
        @DisplayName("Deve atualizar saldo com sucesso")
        void deveAtualizarSaldoComSucesso() {
            // Arrange
            when(contaRepository.findById(1L)).thenReturn(Optional.of(contaTeste));
            when(contaRepository.save(any(Conta.class))).thenReturn(contaTeste);

            // Act
            contaService.atualizarSaldo(1L, 2000.0);

            // Assert
            verify(contaRepository).findById(1L);
            verify(contaRepository).save(any(Conta.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar conta inexistente")
        void deveLancarExcecaoAoAtualizarContaInexistente() {
            // Arrange
            when(contaRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> contaService.atualizarSaldo(999L, 1000.0))
                .isInstanceOf(ContaNotFoundException.class)
                .hasMessageContaining("999");
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com saldo negativo")
        void deveLancarExcecaoAoAtualizarComSaldoNegativo() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.atualizarSaldo(1L, -500.0))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com ID nulo")
        void deveLancarExcecaoAoAtualizarComIdNulo() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.atualizarSaldo(null, 1000.0))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Commands - Depositar")
    class Depositar {

        @Test
        @DisplayName("Deve realizar depósito com sucesso")
        void deveRealizarDepositoComSucesso() {
            // Arrange
            when(contaRepository.findById(1L)).thenReturn(Optional.of(contaTeste));
            when(contaRepository.save(any(Conta.class))).thenReturn(contaTeste);

            // Act
            contaService.depositar(1L, new BigDecimal("500.00"));

            // Assert
            verify(contaRepository).findById(1L);
            verify(contaRepository).save(any(Conta.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao depositar em conta inexistente")
        void deveLancarExcecaoAoDepositarEmContaInexistente() {
            // Arrange
            when(contaRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> contaService.depositar(999L, new BigDecimal("100.00")))
                .isInstanceOf(ContaNotFoundException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao depositar valor negativo")
        void deveLancarExcecaoAoDepositarValorNegativo() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.depositar(1L, new BigDecimal("-100.00")))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao depositar valor zero")
        void deveLancarExcecaoAoDepositarValorZero() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.depositar(1L, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("deve ser positivo");
        }
    }

    @Nested
    @DisplayName("Commands - Sacar")
    class Sacar {

        @Test
        @DisplayName("Deve realizar saque com sucesso quando saldo suficiente")
        void deveRealizarSaqueComSucesso() {
            // Arrange
            when(contaRepository.findById(1L)).thenReturn(Optional.of(contaTeste));
            when(contaRepository.save(any(Conta.class))).thenReturn(contaTeste);

            // Act
            contaService.sacar(1L, new BigDecimal("500.00"));

            // Assert
            verify(contaRepository).findById(1L);
            verify(contaRepository).save(any(Conta.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao sacar com saldo insuficiente")
        void deveLancarExcecaoAoSacarComSaldoInsuficiente() {
            // Arrange
            when(contaRepository.findById(1L)).thenReturn(Optional.of(contaTeste));

            // Act & Assert
            assertThatThrownBy(() -> contaService.sacar(1L, new BigDecimal("2000.00")))
                .isInstanceOf(OperacaoInvalidaException.class)
                .hasMessageContaining("Saldo insuficiente");
        }

        @Test
        @DisplayName("Deve lançar exceção ao sacar de conta inexistente")
        void deveLancarExcecaoAoSacarDeContaInexistente() {
            // Arrange
            when(contaRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> contaService.sacar(999L, new BigDecimal("100.00")))
                .isInstanceOf(ContaNotFoundException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao sacar valor negativo")
        void deveLancarExcecaoAoSacarValorNegativo() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.sacar(1L, new BigDecimal("-100.00")))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Commands - Alterar Nome Titular")
    class AlterarNomeTitular {

        @Test
        @DisplayName("Deve alterar nome do titular com sucesso")
        void deveAlterarNomeTitularComSucesso() {
            // Arrange
            when(contaRepository.findById(1L)).thenReturn(Optional.of(contaTeste));
            when(contaRepository.save(any(Conta.class))).thenReturn(contaTeste);

            // Act
            contaService.alterarNomeTitular(1L, "Maria Silva");

            // Assert
            verify(contaRepository).findById(1L);
            verify(contaRepository).save(any(Conta.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao alterar nome de conta inexistente")
        void deveLancarExcecaoAoAlterarNomeDeContaInexistente() {
            // Arrange
            when(contaRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> contaService.alterarNomeTitular(999L, "Novo Nome"))
                .isInstanceOf(ContaNotFoundException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao alterar para nome inválido")
        void deveLancarExcecaoAoAlterarParaNomeInvalido() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.alterarNomeTitular(1L, "AB"))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Commands - Excluir Conta")
    class ExcluirConta {

        @Test
        @DisplayName("Deve excluir conta com sucesso")
        void deveExcluirContaComSucesso() {
            // Arrange
            when(contaRepository.existsById(1L)).thenReturn(true);
            doNothing().when(contaRepository).deleteById(1L);

            // Act
            contaService.excluirConta(1L);

            // Assert
            verify(contaRepository).existsById(1L);
            verify(contaRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao excluir conta inexistente")
        void deveLancarExcecaoAoExcluirContaInexistente() {
            // Arrange
            when(contaRepository.existsById(999L)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> contaService.excluirConta(999L))
                .isInstanceOf(ContaNotFoundException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao excluir com ID nulo")
        void deveLancarExcecaoAoExcluirComIdNulo() {
            // Act & Assert
            assertThatThrownBy(() -> contaService.excluirConta(null))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Queries - Buscar Conta")
    class BuscarConta {

        @Test
        @DisplayName("Deve buscar conta por ID com sucesso")
        void deveBuscarContaPorIdComSucesso() {
            // Arrange
            when(contaRepository.findById(1L)).thenReturn(Optional.of(contaTeste));

            // Act
            Conta conta = contaService.buscarContaPorId(1L);

            // Assert
            assertThat(conta).isNotNull();
            verify(contaRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar conta inexistente")
        void deveLancarExcecaoAoBuscarContaInexistente() {
            // Arrange
            when(contaRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> contaService.buscarContaPorId(999L))
                .isInstanceOf(ContaNotFoundException.class)
                .hasMessageContaining("999");
        }

        @Test
        @DisplayName("Deve buscar conta opcional com sucesso")
        void deveBuscarContaOpcionalComSucesso() {
            // Arrange
            when(contaRepository.findById(1L)).thenReturn(Optional.of(contaTeste));

            // Act
            Optional<Conta> conta = contaService.buscarContaOpcional(1L);

            // Assert
            assertThat(conta).isPresent();
        }

        @Test
        @DisplayName("Deve retornar Optional vazio para conta inexistente")
        void deveRetornarOptionalVazioParaContaInexistente() {
            // Arrange
            when(contaRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Optional<Conta> conta = contaService.buscarContaOpcional(999L);

            // Assert
            assertThat(conta).isEmpty();
        }
    }

    @Nested
    @DisplayName("Queries - Listar e Contar")
    class ListarEContar {

        @Test
        @DisplayName("Deve listar todas as contas")
        void deveListarTodasContas() {
            // Arrange
            Conta conta1 = Conta.criar(NomeTitular.de("João"), Saldo.de(100.0));
            Conta conta2 = Conta.criar(NomeTitular.de("Maria"), Saldo.de(200.0));
            when(contaRepository.findAll()).thenReturn(Arrays.asList(conta1, conta2));

            // Act
            List<Conta> contas = contaService.listarTodasContas();

            // Assert
            assertThat(contas).hasSize(2);
            verify(contaRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há contas")
        void deveRetornarListaVaziaQuandoNaoHaContas() {
            // Arrange
            when(contaRepository.findAll()).thenReturn(Arrays.asList());

            // Act
            List<Conta> contas = contaService.listarTodasContas();

            // Assert
            assertThat(contas).isEmpty();
        }

        @Test
        @DisplayName("Deve contar contas corretamente")
        void deveContarContasCorretamente() {
            // Arrange
            when(contaRepository.count()).thenReturn(5L);

            // Act
            long total = contaService.contarContas();

            // Assert
            assertThat(total).isEqualTo(5L);
        }

        @Test
        @DisplayName("Deve verificar se conta existe")
        void deveVerificarSeContaExiste() {
            // Arrange
            when(contaRepository.existsById(1L)).thenReturn(true);

            // Act
            boolean existe = contaService.contaExiste(1L);

            // Assert
            assertThat(existe).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false quando conta não existe")
        void deveRetornarFalseQuandoContaNaoExiste() {
            // Arrange
            when(contaRepository.existsById(999L)).thenReturn(false);

            // Act
            boolean existe = contaService.contaExiste(999L);

            // Assert
            assertThat(existe).isFalse();
        }

        @Test
        @DisplayName("Deve retornar false quando ID é nulo")
        void deveRetornarFalseQuandoIdNulo() {
            // Act
            boolean existe = contaService.contaExiste(null);

            // Assert
            assertThat(existe).isFalse();
            verify(contaRepository, never()).existsById(any());
        }
    }
}
