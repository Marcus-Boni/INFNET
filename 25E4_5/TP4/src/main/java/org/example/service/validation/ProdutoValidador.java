package org.example.service.validation;

import org.example.exception.NegocioException;
import org.example.model.Produto;
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
        if (produto == null) {
            throw new NegocioException("Produto não pode ser nulo.");
        }
        if (!StringUtils.hasText(produto.getNome())) {
            throw new NegocioException("O nome do produto é obrigatório.");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("O preço deve ser maior que zero.");
        }
        if (produto.getEstoque() == null || produto.getEstoque() < 0) {
            throw new NegocioException("O estoque não pode ser negativo.");
        }
    }
}
