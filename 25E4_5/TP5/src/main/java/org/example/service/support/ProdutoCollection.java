package org.example.service.support;

import org.example.model.Produto;

import java.util.Collections;
import java.util.List;

public record ProdutoCollection(List<Produto> itens) {

    public ProdutoCollection {
        itens = List.copyOf(itens);
    }

    public static ProdutoCollection of(List<Produto> itens) {
        return new ProdutoCollection(itens == null ? Collections.emptyList() : itens);
    }

    public List<Produto> asList() {
        return itens;
    }
}
