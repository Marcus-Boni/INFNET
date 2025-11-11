package org.example.banco.valueobjects;

import java.util.Objects;

/**
 * Value Object que representa o nome do titular de uma conta bancária.
 * Este objeto é imutável e garante que o nome seja válido.
 * 
 * Regras de negócio:
 * - Nome não pode ser nulo
 * - Nome não pode ser vazio ou conter apenas espaços
 * - Nome deve ter entre 3 e 100 caracteres
 */
public final class NomeTitular {

    private static final int TAMANHO_MINIMO = 3;
    private static final int TAMANHO_MAXIMO = 100;
    
    private final String valor;

    /**
     * Constrói um NomeTitular validando as regras de negócio.
     *
     * @param valor o nome do titular
     * @throws IllegalArgumentException se o nome violar as regras de validação
     */
    private NomeTitular(final String valor) {
        validarNome(valor);
        this.valor = valor.trim();
    }

    /**
     * Factory method para criar uma instância de NomeTitular.
     *
     * @param valor o nome do titular
     * @return uma nova instância de NomeTitular
     * @throws IllegalArgumentException se o nome for inválido
     */
    public static NomeTitular de(final String valor) {
        return new NomeTitular(valor);
    }

    private void validarNome(final String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("Nome do titular não pode ser nulo");
        }

        final String nomeTrimmed = nome.trim();
        
        if (nomeTrimmed.isEmpty()) {
            throw new IllegalArgumentException("Nome do titular não pode ser vazio");
        }

        if (nomeTrimmed.length() < TAMANHO_MINIMO) {
            throw new IllegalArgumentException(
                String.format("Nome do titular deve ter pelo menos %d caracteres", TAMANHO_MINIMO)
            );
        }

        if (nomeTrimmed.length() > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException(
                String.format("Nome do titular não pode exceder %d caracteres", TAMANHO_MAXIMO)
            );
        }
    }

    /**
     * Query: retorna o valor do nome.
     *
     * @return o nome do titular
     */
    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NomeTitular that = (NomeTitular) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
