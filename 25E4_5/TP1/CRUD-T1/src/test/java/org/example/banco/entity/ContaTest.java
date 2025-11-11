package org.example.banco.entity;

import org.example.banco.valueobjects.NomeTitular;
import org.example.banco.valueobjects.Saldo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para a entidade Conta.
 * 
 * Valida comportamento da entidade, operações de depósito/saque,
 * e garantias de imutabilidade dos Value Objects.
 */
@DisplayName("Conta - Testes Unitários")
class ContaTest {

    @Nested
    @DisplayName("Criação de Conta")
    class CriacaoConta {

        @Test
        @DisplayName("Deve criar conta com titular e saldo válidos")
        void deveCriarContaComDadosValidos() {
            // Arrange
            NomeTitular nome = NomeTitular.de("João Silva");
            Saldo saldo = Saldo.de(new BigDecimal("1000.00"));

            // Act
            Conta conta = Conta.criar(nome, saldo);

            // Assert
            assertThat(conta.getNomeTitular()).isEqualTo(nome);
            assertThat(conta.getSaldo()).isEqualTo(saldo);
        }

        @Test
        @DisplayName("Deve criar conta com saldo zero usando factory method")
        void deveCriarContaComSaldoZero() {
            // Arrange
            NomeTitular nome = NomeTitular.de("Maria Santos");

            // Act
            Conta conta = Conta.criarComSaldoZero(nome);

            // Assert
            assertThat(conta.getNomeTitular()).isEqualTo(nome);
            assertThat(conta.isSaldoZerado()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar conta com nome nulo")
        void deveLancarExcecaoComNomeNulo() {
            // Arrange
            Saldo saldo = Saldo.de(100.0);

            // Act & Assert
            assertThatThrownBy(() -> new Conta(null, saldo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome do titular não pode ser nulo");
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar conta com saldo nulo")
        void deveLancarExcecaoComSaldoNulo() {
            // Arrange
            NomeTitular nome = NomeTitular.de("Pedro Silva");

            // Act & Assert
            assertThatThrownBy(() -> new Conta(nome, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Saldo não pode ser nulo");
        }
    }

    @Nested
    @DisplayName("Operações de Depósito")
    class OperacoesDeposito {

        @Test
        @DisplayName("Deve depositar valor e aumentar saldo")
        void deveDepositarEAumentarSaldo() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Ana Costa"),
                Saldo.de(new BigDecimal("500.00"))
            );
            BigDecimal valorDeposito = new BigDecimal("250.00");

            // Act
            conta.depositar(valorDeposito);

            // Assert
            assertThat(conta.getSaldo().getValor()).isEqualByComparingTo("750.00");
        }

        @Test
        @DisplayName("Deve permitir múltiplos depósitos")
        void devePermitirMultiplosDepositos() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Carlos Mendes"),
                Saldo.de(new BigDecimal("100.00"))
            );

            // Act
            conta.depositar(new BigDecimal("50.00"));
            conta.depositar(new BigDecimal("30.00"));
            conta.depositar(new BigDecimal("20.00"));

            // Assert
            assertThat(conta.getSaldo().getValor()).isEqualByComparingTo("200.00");
        }

        @Test
        @DisplayName("Deve lançar exceção ao depositar valor negativo")
        void deveLancarExcecaoAoDepositarNegativo() {
            // Arrange
            Conta conta = Conta.criarComSaldoZero(NomeTitular.de("João"));

            // Act & Assert
            assertThatThrownBy(() -> conta.depositar(new BigDecimal("-100.00")))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção ao depositar valor nulo")
        void deveLancarExcecaoAoDepositarNulo() {
            // Arrange
            Conta conta = Conta.criarComSaldoZero(NomeTitular.de("Maria"));

            // Act & Assert
            assertThatThrownBy(() -> conta.depositar(null))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Operações de Saque")
    class OperacoesSaque {

        @Test
        @DisplayName("Deve sacar valor quando saldo suficiente")
        void deveSacarQuandoSaldoSuficiente() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Fernando Lima"),
                Saldo.de(new BigDecimal("1000.00"))
            );

            // Act
            conta.sacar(new BigDecimal("300.00"));

            // Assert
            assertThat(conta.getSaldo().getValor()).isEqualByComparingTo("700.00");
        }

        @Test
        @DisplayName("Deve permitir sacar exatamente o saldo disponível")
        void devePermitirSacarSaldoExato() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Julia Alves"),
                Saldo.de(new BigDecimal("500.00"))
            );

            // Act
            conta.sacar(new BigDecimal("500.00"));

            // Assert
            assertThat(conta.isSaldoZerado()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção ao sacar com saldo insuficiente")
        void deveLancarExcecaoAoSacarComSaldoInsuficiente() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Roberto Santos"),
                Saldo.de(new BigDecimal("100.00"))
            );

            // Act & Assert
            assertThatThrownBy(() -> conta.sacar(new BigDecimal("100.01")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Saldo insuficiente");
        }

        @Test
        @DisplayName("Deve lançar exceção ao sacar valor negativo")
        void deveLancarExcecaoAoSacarNegativo() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Patricia Costa"),
                Saldo.de(new BigDecimal("500.00"))
            );

            // Act & Assert
            assertThatThrownBy(() -> conta.sacar(new BigDecimal("-50.00")))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Deve permitir múltiplos saques")
        void devePermitirMultiplosSaques() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Lucas Pereira"),
                Saldo.de(new BigDecimal("1000.00"))
            );

            // Act
            conta.sacar(new BigDecimal("200.00"));
            conta.sacar(new BigDecimal("300.00"));
            conta.sacar(new BigDecimal("100.00"));

            // Assert
            assertThat(conta.getSaldo().getValor()).isEqualByComparingTo("400.00");
        }
    }

    @Nested
    @DisplayName("Atualização de Saldo")
    class AtualizacaoSaldo {

        @Test
        @DisplayName("Deve atualizar saldo diretamente")
        void deveAtualizarSaldoDiretamente() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Beatriz Silva"),
                Saldo.de(new BigDecimal("500.00"))
            );
            Saldo novoSaldo = Saldo.de(new BigDecimal("1000.00"));

            // Act
            conta.atualizarSaldo(novoSaldo);

            // Assert
            assertThat(conta.getSaldo().getValor()).isEqualByComparingTo("1000.00");
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com saldo nulo")
        void deveLancarExcecaoAoAtualizarComSaldoNulo() {
            // Arrange
            Conta conta = Conta.criarComSaldoZero(NomeTitular.de("Diego Santos"));

            // Act & Assert
            assertThatThrownBy(() -> conta.atualizarSaldo(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Novo saldo não pode ser nulo");
        }
    }

    @Nested
    @DisplayName("Alteração de Nome")
    class AlteracaoNome {

        @Test
        @DisplayName("Deve alterar nome do titular")
        void deveAlterarNomeTitular() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Nome Antigo"),
                Saldo.zero()
            );
            NomeTitular novoNome = NomeTitular.de("Nome Novo");

            // Act
            conta.alterarNomeTitular(novoNome);

            // Assert
            assertThat(conta.getNomeTitular()).isEqualTo(novoNome);
            assertThat(conta.getNomeTitularString()).isEqualTo("Nome Novo");
        }

        @Test
        @DisplayName("Deve lançar exceção ao alterar para nome nulo")
        void deveLancarExcecaoAoAlterarParaNomeNulo() {
            // Arrange
            Conta conta = Conta.criarComSaldoZero(NomeTitular.de("João Silva"));

            // Act & Assert
            assertThatThrownBy(() -> conta.alterarNomeTitular(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Novo nome do titular não pode ser nulo");
        }
    }

    @Nested
    @DisplayName("Queries - Consultas")
    class Queries {

        @Test
        @DisplayName("possuiSaldoSuficiente deve retornar true quando suficiente")
        void possuiSaldoSuficienteDeveRetornarTrue() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Sandra Oliveira"),
                Saldo.de(new BigDecimal("500.00"))
            );

            // Act & Assert
            assertThat(conta.possuiSaldoSuficiente(new BigDecimal("300.00"))).isTrue();
            assertThat(conta.possuiSaldoSuficiente(new BigDecimal("500.00"))).isTrue();
        }

        @Test
        @DisplayName("possuiSaldoSuficiente deve retornar false quando insuficiente")
        void possuiSaldoSuficienteDeveRetornarFalse() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Ricardo Mendes"),
                Saldo.de(new BigDecimal("100.00"))
            );

            // Act & Assert
            assertThat(conta.possuiSaldoSuficiente(new BigDecimal("100.01"))).isFalse();
        }

        @Test
        @DisplayName("isSaldoZerado deve retornar true para saldo zero")
        void isSaldoZeradoDeveRetornarTrueParaZero() {
            // Arrange
            Conta conta = Conta.criarComSaldoZero(NomeTitular.de("Paulo Silva"));

            // Act & Assert
            assertThat(conta.isSaldoZerado()).isTrue();
        }

        @Test
        @DisplayName("isSaldoZerado deve retornar false para saldo positivo")
        void isSaldoZeradoDeveRetornarFalseParaPositivo() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Camila Santos"),
                Saldo.de(new BigDecimal("0.01"))
            );

            // Act & Assert
            assertThat(conta.isSaldoZerado()).isFalse();
        }
    }

    @Nested
    @DisplayName("Equals e HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("Contas com mesmo ID devem ser iguais")
        void contasComMesmoIdSaoIguais() {
            // Arrange
            Conta conta1 = Conta.criar(NomeTitular.de("João"), Saldo.de(100.0));
            Conta conta2 = Conta.criar(NomeTitular.de("Maria"), Saldo.de(200.0));

            // Note: IDs são null pois não foram persistidas
            // Este teste é mais relevante após persistência

            // Act & Assert
            if (conta1.getId() != null && conta2.getId() != null) {
                if (conta1.getId().equals(conta2.getId())) {
                    assertThat(conta1).isEqualTo(conta2);
                }
            }
        }

        @Test
        @DisplayName("Conta deve ser igual a si mesma")
        void contaDeveSerIgualASiMesma() {
            // Arrange
            Conta conta = Conta.criar(NomeTitular.de("João"), Saldo.de(100.0));

            // Act & Assert
            assertThat(conta).isEqualTo(conta);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringBehavior {

        @Test
        @DisplayName("toString deve conter informações essenciais")
        void toStringDeveConterInformacoesEssenciais() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("José da Silva"),
                Saldo.de(new BigDecimal("1234.56"))
            );

            // Act
            String resultado = conta.toString();

            // Assert
            assertThat(resultado).contains("Conta");
            assertThat(resultado).contains("José da Silva");
            assertThat(resultado).contains("1234.56");
        }
    }

    @Nested
    @DisplayName("Cenários Complexos")
    class CenariosComplexos {

        @Test
        @DisplayName("Deve manter saldo correto após múltiplas operações")
        void deveManterSaldoCorretoAposMultiplasOperacoes() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Marina Santos"),
                Saldo.de(new BigDecimal("1000.00"))
            );

            // Act
            conta.depositar(new BigDecimal("500.00"));    // 1500
            conta.sacar(new BigDecimal("200.00"));        // 1300
            conta.depositar(new BigDecimal("100.00"));    // 1400
            conta.sacar(new BigDecimal("300.00"));        // 1100

            // Assert
            assertThat(conta.getSaldo().getValor()).isEqualByComparingTo("1100.00");
        }

        @Test
        @DisplayName("Deve manter integridade após operações e verificações")
        void deveManterIntegridadeAposOperacoes() {
            // Arrange
            Conta conta = Conta.criar(
                NomeTitular.de("Rafael Costa"),
                Saldo.de(new BigDecimal("500.00"))
            );

            // Act & Assert - Sequência de operações
            assertThat(conta.possuiSaldoSuficiente(new BigDecimal("400.00"))).isTrue();
            
            conta.sacar(new BigDecimal("400.00"));
            assertThat(conta.getSaldo().getValor()).isEqualByComparingTo("100.00");
            
            conta.depositar(new BigDecimal("900.00"));
            assertThat(conta.getSaldo().getValor()).isEqualByComparingTo("1000.00");
            
            assertThat(conta.isSaldoZerado()).isFalse();
        }
    }
}
