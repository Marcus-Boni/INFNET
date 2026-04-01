package org.example.service.mutation;

import org.example.model.Produto;
import org.example.service.support.ProdutoComando;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMutator {

    public void aplicarAtualizacao(Produto existente, ProdutoComando dadosNovos) {
        existente.setNome(dadosNovos.nome());
        existente.setDescricao(dadosNovos.descricao());
        existente.setPreco(dadosNovos.preco());
        existente.setEstoque(dadosNovos.estoque());
    }
}
