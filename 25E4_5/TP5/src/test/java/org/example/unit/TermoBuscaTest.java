package org.example.unit;

import org.example.exception.NegocioException;
import org.example.service.support.TermoBusca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TermoBusca — objeto de valor")
class TermoBuscaTest {

    @Test
    @DisplayName("Normaliza espaços ao construir")
    void normalizaEspacos() {
        TermoBusca termo = TermoBusca.of("  teclado gamer  ");
        assertThat(termo.valorNormalizado()).isEqualTo("teclado gamer");
    }

    @Test
    @DisplayName("Entrada em branco vira termo vazio")
    void vazio() {
        TermoBusca termo = TermoBusca.of("   ");
        assertThat(termo.estaVazio()).isTrue();
    }

    @Test
    @DisplayName("Falha para termos acima do limite")
    void excedeLimite() {
        assertThatThrownBy(() -> TermoBusca.of("a".repeat(201)))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining("longo");
    }
}
