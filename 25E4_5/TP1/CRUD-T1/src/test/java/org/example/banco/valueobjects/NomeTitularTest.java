package org.example.banco.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para a classe NomeTitular.
 * 
 * Cobertura de testes:
 * - Partições equivalentes: nomes válidos e inválidos
 * - Análise de limites: tamanhos mínimo, máximo e além dos limites
 * - Casos especiais: nulos, vazios, apenas espaços
 * - Comportamento de equals, hashCode e toString
 */
@DisplayName("NomeTitular - Testes Unitários")
class NomeTitularTest {

    @Nested
    @DisplayName("Cenários de Sucesso")
    class CenariosValidos {

        @Test
        @DisplayName("Deve criar NomeTitular com nome válido simples")
        void deveCriarComNomeValido() {
            // Arrange & Act
            NomeTitular nome = NomeTitular.de("João Silva");

            // Assert
            assertThat(nome.getValor()).isEqualTo("João Silva");
        }

        @Test
        @DisplayName("Deve criar NomeTitular com nome no limite mínimo (3 caracteres)")
        void deveCriarComNomeNoLimiteMinimo() {
            // Arrange & Act
            NomeTitular nome = NomeTitular.de("Ana");

            // Assert
            assertThat(nome.getValor()).isEqualTo("Ana");
        }

        @Test
        @DisplayName("Deve criar NomeTitular com nome no limite máximo (100 caracteres)")
        void deveCriarComNomeNoLimiteMaximo() {
            // Arrange
            String nomeGrande = "A".repeat(100);

            // Act
            NomeTitular nome = NomeTitular.de(nomeGrande);

            // Assert
            assertThat(nome.getValor()).hasSize(100);
        }

        @Test
        @DisplayName("Deve remover espaços em branco do início e fim")
        void deveRemoverEspacosEmBranco() {
            // Arrange & Act
            NomeTitular nome = NomeTitular.de("  João Silva  ");

            // Assert
            assertThat(nome.getValor()).isEqualTo("João Silva");
        }

        @Test
        @DisplayName("Deve aceitar nomes compostos")
        void deveAceitarNomesCompostos() {
            // Arrange & Act
            NomeTitular nome = NomeTitular.de("Maria da Silva Santos");

            // Assert
            assertThat(nome.getValor()).isEqualTo("Maria da Silva Santos");
        }

        @Test
        @DisplayName("Deve aceitar nomes com caracteres especiais")
        void deveAceitarNomesComCaracteresEspeciais() {
            // Arrange & Act
            NomeTitular nome = NomeTitular.de("José D'Angelo");

            // Assert
            assertThat(nome.getValor()).isEqualTo("José D'Angelo");
        }
    }

    @Nested
    @DisplayName("Cenários de Falha - Validações")
    class CenariosInvalidos {

        @Test
        @DisplayName("Deve lançar exceção quando nome é nulo")
        void deveLancarExcecaoQuandoNomeNulo() {
            // Act & Assert
            assertThatThrownBy(() -> NomeTitular.de(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser nulo");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        @DisplayName("Deve lançar exceção quando nome é vazio ou apenas espaços")
        void deveLancarExcecaoQuandoNomeVazio(String nomeInvalido) {
            // Act & Assert
            assertThatThrownBy(() -> NomeTitular.de(nomeInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser");
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
        void deveLancarExcecaoQuandoNomeMuitoCurto() {
            // Act & Assert
            assertThatThrownBy(() -> NomeTitular.de("AB"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pelo menos 3 caracteres");
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome excede 100 caracteres")
        void deveLancarExcecaoQuandoNomeMuitoLongo() {
            // Arrange
            String nomeLongo = "A".repeat(101);

            // Act & Assert
            assertThatThrownBy(() -> NomeTitular.de(nomeLongo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode exceder 100 caracteres");
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome só tem espaços após trim")
        void deveLancarExcecaoQuandoNomeApenasEspacos() {
            // Act & Assert
            assertThatThrownBy(() -> NomeTitular.de("     "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser vazio");
        }
    }

    @Nested
    @DisplayName("Análise de Limites")
    class AnaliseLimites {

        @Test
        @DisplayName("Deve aceitar exatamente 3 caracteres (limite mínimo)")
        void deveAceitarExatamente3Caracteres() {
            // Arrange & Act
            NomeTitular nome = NomeTitular.de("Ana");

            // Assert
            assertThat(nome.getValor()).hasSize(3);
        }

        @Test
        @DisplayName("Deve aceitar exatamente 100 caracteres (limite máximo)")
        void deveAceitarExatamente100Caracteres() {
            // Arrange
            String nomeExato = "A".repeat(100);

            // Act
            NomeTitular nome = NomeTitular.de(nomeExato);

            // Assert
            assertThat(nome.getValor()).hasSize(100);
        }

        @Test
        @DisplayName("Deve rejeitar 2 caracteres (abaixo do mínimo)")
        void deveRejeitarDoisCaracteres() {
            // Act & Assert
            assertThatThrownBy(() -> NomeTitular.de("AB"))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Deve rejeitar 101 caracteres (acima do máximo)")
        void deveRejeitar101Caracteres() {
            // Arrange
            String nomeExcedente = "A".repeat(101);

            // Act & Assert
            assertThatThrownBy(() -> NomeTitular.de(nomeExcedente))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Comportamento de Equals e HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("Dois NomeTitular com mesmo valor devem ser iguais")
        void devemSerIguaisQuandoValoresIguais() {
            // Arrange
            NomeTitular nome1 = NomeTitular.de("João Silva");
            NomeTitular nome2 = NomeTitular.de("João Silva");

            // Assert
            assertThat(nome1).isEqualTo(nome2);
            assertThat(nome1.hashCode()).isEqualTo(nome2.hashCode());
        }

        @Test
        @DisplayName("Dois NomeTitular com valores diferentes não devem ser iguais")
        void naoDevemSerIguaisQuandoValoresDiferentes() {
            // Arrange
            NomeTitular nome1 = NomeTitular.de("João Silva");
            NomeTitular nome2 = NomeTitular.de("Maria Santos");

            // Assert
            assertThat(nome1).isNotEqualTo(nome2);
        }

        @Test
        @DisplayName("NomeTitular deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            // Arrange
            NomeTitular nome = NomeTitular.de("João Silva");

            // Assert
            assertThat(nome).isEqualTo(nome);
        }

        @Test
        @DisplayName("NomeTitular não deve ser igual a null")
        void naoDeveSerIgualANull() {
            // Arrange
            NomeTitular nome = NomeTitular.de("João Silva");

            // Assert
            assertThat(nome).isNotEqualTo(null);
        }

        @Test
        @DisplayName("NomeTitular não deve ser igual a objeto de outra classe")
        void naoDeveSerIgualAOutraClasse() {
            // Arrange
            NomeTitular nome = NomeTitular.de("João Silva");
            String string = "João Silva";

            // Assert
            assertThat(nome).isNotEqualTo(string);
        }
    }

    @Nested
    @DisplayName("Comportamento de ToString")
    class ToStringBehavior {

        @Test
        @DisplayName("toString deve retornar o valor do nome")
        void toStringDeveRetornarValor() {
            // Arrange
            NomeTitular nome = NomeTitular.de("João Silva");

            // Act & Assert
            assertThat(nome.toString()).isEqualTo("João Silva");
        }
    }
}
