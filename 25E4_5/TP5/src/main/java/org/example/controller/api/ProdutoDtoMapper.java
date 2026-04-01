package org.example.controller.api;

import org.example.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoDtoMapper {

    public Produto toEntity(ProdutoRequest request) {
        return new Produto(
                request.nome(),
                request.descricao(),
                request.preco(),
                request.estoque()
        );
    }

    public ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque()
        );
    }
}
