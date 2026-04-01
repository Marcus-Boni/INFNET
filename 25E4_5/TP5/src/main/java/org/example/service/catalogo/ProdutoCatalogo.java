package org.example.service.catalogo;

import org.example.model.Produto;
import org.example.service.support.ProdutoCollection;
import org.example.service.support.TermoBusca;

public interface ProdutoCatalogo {

    ProdutoCollection listarTodos();

    Produto buscarPorId(Long id);

    ProdutoCollection buscarPorNome(TermoBusca termoBusca);

    Produto salvar(Produto produto);

    Produto atualizar(Long id, Produto dadosNovos);

    void deletar(Long id);
}
