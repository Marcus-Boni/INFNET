package org.example.service.validation;

import org.example.exception.NegocioException;
import org.example.model.Produto;
import org.example.service.support.ProdutoComando;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class ProdutoValidador {

    public void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("ID inválido: " + id);
        }
    }

    public void validarProduto(Produto produto) {
        validarComando(ProdutoComando.of(produto));
    }

    public void validarComando(ProdutoComando comando) {
        if (comando == null) {
            throw new NegocioException("Produto não pode ser nulo.");
        }
        if (!StringUtils.hasText(comando.nome())) {
            throw new NegocioException("O nome do produto é obrigatório.");
        }
        if (comando.preco() == null || comando.preco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("O preço deve ser maior que zero.");
        }
        if (comando.estoque() == null || comando.estoque() < 0) {
            throw new NegocioException("O estoque não pode ser negativo.");
        }
    }
}
