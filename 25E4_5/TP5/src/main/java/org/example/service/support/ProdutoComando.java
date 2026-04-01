package org.example.service.support;

import org.example.model.Produto;

import java.math.BigDecimal;

public record ProdutoComando(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque
) {

    public static ProdutoComando of(Produto produto) {
        if (produto == null) {
            return null;
        }
        return new ProdutoComando(
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque()
        );
    }
}