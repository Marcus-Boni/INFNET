package org.example.banco.valueobjects;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes baseados em propriedades para NomeTitular usando JQwik.
 * 
 * Valida propriedades invariantes relacionadas a nomes de titulares.
 */
class NomeTitularPropertyTest {

    /**
     * Propriedade: Nomes válidos (3-100 caracteres) sempre devem ser aceitos.
     */
    @Property
    @Label("Nomes válidos sempre são aceitos")
    void nomesValidosSempreSaoAceitos(
        @ForAll @StringLength(min = 3, max = 100) @AlphaChars String nome
    ) {
        // Act
        NomeTitular nomeTitular = NomeTitular.de(nome);

        // Assert
        assertThat(nomeTitular.getValor()).isNotNull();
        assertThat(nomeTitular.getValor().length()).isBetween(3, 100);
    }

    /**
     * Propriedade: Nomes curtos (< 3 caracteres) sempre lançam exceção.
     */
    @Property
    @Label("Nomes curtos sempre lançam exceção")
    void nomesCurtosSempreLancamExcecao(
        @ForAll @StringLength(min = 0, max = 2) String nomeCurto
    ) {
        // Assume que não é vazio (esse caso é testado separadamente)
        Assume.that(!nomeCurto.trim().isEmpty());

        // Act & Assert
        assertThatThrownBy(() -> NomeTitular.de(nomeCurto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("pelo menos 3 caracteres");
    }

    /**
     * Propriedade: Nomes longos (> 100 caracteres) sempre lançam exceção.
     */
    @Property
    @Label("Nomes longos sempre lançam exceção")
    void nomesLongosSempreLancamExcecao(
        @ForAll @StringLength(min = 101, max = 200) @AlphaChars String nomeLongo
    ) {
        // Act & Assert
        assertThatThrownBy(() -> NomeTitular.de(nomeLongo))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("não pode exceder 100 caracteres");
    }

    /**
     * Propriedade: Espaços no início/fim são sempre removidos.
     */
    @Property
    @Label("Espaços no início e fim são removidos")
    void espacosInicioFimSaoRemovidos(
        @ForAll @StringLength(min = 3, max = 50) @AlphaChars String nomeBase,
        @ForAll @IntRange(min = 1, max = 5) int espacosInicio,
        @ForAll @IntRange(min = 1, max = 5) int espacosFim
    ) {
        // Arrange
        String nomeComEspacos = " ".repeat(espacosInicio) + nomeBase + " ".repeat(espacosFim);

        // Act
        NomeTitular nomeTitular = NomeTitular.de(nomeComEspacos);

        // Assert
        assertThat(nomeTitular.getValor()).isEqualTo(nomeBase);
        assertThat(nomeTitular.getValor()).doesNotStartWith(" ");
        assertThat(nomeTitular.getValor()).doesNotEndWith(" ");
    }

    /**
     * Propriedade: Dois NomeTitular com mesmo valor são sempre iguais.
     */
    @Property
    @Label("NomeTitular com mesmo valor são iguais")
    void nomesComMesmoValorSaoIguais(
        @ForAll @StringLength(min = 3, max = 100) @AlphaChars String nome
    ) {
        // Act
        NomeTitular nome1 = NomeTitular.de(nome);
        NomeTitular nome2 = NomeTitular.de(nome);

        // Assert
        assertThat(nome1).isEqualTo(nome2);
        assertThat(nome1.hashCode()).isEqualTo(nome2.hashCode());
    }

    /**
     * Propriedade: toString sempre retorna o valor armazenado.
     */
    @Property
    @Label("toString sempre retorna o valor")
    void toStringSempreRetornaValor(
        @ForAll @StringLength(min = 3, max = 100) @AlphaChars String nome
    ) {
        // Act
        NomeTitular nomeTitular = NomeTitular.de(nome);

        // Assert
        assertThat(nomeTitular.toString()).isEqualTo(nomeTitular.getValor());
    }

    /**
     * Propriedade: getValor nunca retorna null.
     */
    @Property
    @Label("getValor nunca retorna null")
    void getValorNuncaRetornaNull(
        @ForAll @StringLength(min = 3, max = 100) @AlphaChars String nome
    ) {
        // Act
        NomeTitular nomeTitular = NomeTitular.de(nome);

        // Assert
        assertThat(nomeTitular.getValor()).isNotNull();
    }

    /**
     * Propriedade: Nomes apenas com espaços sempre lançam exceção.
     */
    @Property
    @Label("Nomes apenas com espaços lançam exceção")
    void nomesApenasEspacosLancamExcecao(
        @ForAll @IntRange(min = 1, max = 20) int numeroEspacos
    ) {
        // Arrange
        String nomeEspacos = " ".repeat(numeroEspacos);

        // Act & Assert
        assertThatThrownBy(() -> NomeTitular.de(nomeEspacos))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("não pode ser vazio");
    }

    /**
     * Propriedade: NomeTitular é reflexivo (equals consigo mesmo).
     */
    @Property
    @Label("NomeTitular é reflexivo")
    void nomeTitularReflexivo(
        @ForAll @StringLength(min = 3, max = 100) @AlphaChars String nome
    ) {
        // Act
        NomeTitular nomeTitular = NomeTitular.de(nome);

        // Assert
        assertThat(nomeTitular).isEqualTo(nomeTitular);
    }

    /**
     * Propriedade: NomeTitular é simétrico (se A=B então B=A).
     */
    @Property
    @Label("NomeTitular é simétrico")
    void nomeTitularSimetrico(
        @ForAll @StringLength(min = 3, max = 100) @AlphaChars String nome
    ) {
        // Act
        NomeTitular nome1 = NomeTitular.de(nome);
        NomeTitular nome2 = NomeTitular.de(nome);

        // Assert
        assertThat(nome1.equals(nome2)).isEqualTo(nome2.equals(nome1));
    }

    /**
     * Provedor customizado para gerar nomes realistas.
     */
    @Provide
    Arbitrary<String> nomesRealisticos() {
        Arbitrary<String> primeiroNome = Arbitraries.of("João", "Maria", "Pedro", "Ana", "Carlos", "Juliana");
        Arbitrary<String> sobrenome = Arbitraries.of("Silva", "Santos", "Oliveira", "Souza", "Lima", "Costa");
        
        return Combinators.combine(primeiroNome, sobrenome)
            .as((primeiro, ultimo) -> primeiro + " " + ultimo);
    }

    /**
     * Propriedade: Nomes realísticos sempre são válidos.
     */
    @Property
    @Label("Nomes realísticos são válidos")
    void nomesRealisticosSaoValidos(@ForAll("nomesRealisticos") String nome) {
        // Act
        NomeTitular nomeTitular = NomeTitular.de(nome);

        // Assert
        assertThat(nomeTitular.getValor()).isEqualTo(nome);
        assertThat(nomeTitular.getValor()).contains(" ");
    }
}
