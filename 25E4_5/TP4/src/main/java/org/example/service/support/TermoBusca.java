package org.example.service.support;

import org.example.exception.NegocioException;
import org.springframework.util.StringUtils;

public record TermoBusca(String valorNormalizado) {

    private static final int TAMANHO_MAXIMO = 200;

    public static TermoBusca of(String valorBruto) {
        if (!StringUtils.hasText(valorBruto)) {
            return vazio();
        }
        String normalizado = valorBruto.trim();
        if (normalizado.length() > TAMANHO_MAXIMO) {
            throw new NegocioException("Termo de busca muito longo.");
        }
        return new TermoBusca(normalizado);
    }

    public static TermoBusca vazio() {
        return new TermoBusca("");
    }

    public boolean estaVazio() {
        return valorNormalizado.isBlank();
    }
}
