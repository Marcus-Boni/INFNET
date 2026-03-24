package org.example.service.mutation;

import org.example.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMutator {

    public void aplicarAtualizacao(Produto existente, Produto dadosNovos) {
        existente.setNome(dadosNovos.getNome());
        existente.setDescricao(dadosNovos.getDescricao());
        existente.setPreco(dadosNovos.getPreco());
        existente.setEstoque(dadosNovos.getEstoque());
    }
}
