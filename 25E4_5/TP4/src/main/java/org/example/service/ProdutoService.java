package org.example.service;

import org.example.exception.NegocioException;
import org.example.exception.ProdutoNotFoundException;
import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.example.service.catalogo.ProdutoCatalogo;
import org.example.service.mutation.ProdutoMutator;
import org.example.service.sanitization.ProdutoSanitizador;
import org.example.service.support.ProdutoCollection;
import org.example.service.support.TermoBusca;
import org.example.service.validation.ProdutoValidador;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de negócio para Produto.
 * Aplica fail-early: valida precondições antes de executar qualquer operação.
 */
@Service
@Transactional
public class ProdutoService implements ProdutoCatalogo {

    private final ProdutoRepository produtoRepository;
    private final ProdutoValidador produtoValidador;
    private final ProdutoSanitizador produtoSanitizador;
    private final ProdutoMutator produtoMutator;

    public ProdutoService(ProdutoRepository produtoRepository,
                          ProdutoValidador produtoValidador,
                          ProdutoSanitizador produtoSanitizador,
                          ProdutoMutator produtoMutator) {
        this.produtoRepository = produtoRepository;
        this.produtoValidador = produtoValidador;
        this.produtoSanitizador = produtoSanitizador;
        this.produtoMutator = produtoMutator;
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoCollection listarTodos() {
        return ProdutoCollection.of(produtoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodosComoLista() {
        return listarTodos().asList();
    }

    @Override
    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        produtoValidador.validarId(id);
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoCollection buscarPorNome(TermoBusca termoBusca) {
        if (termoBusca.estaVazio()) {
            return listarTodos();
        }
        return ProdutoCollection.of(produtoRepository.findByNomeContainingIgnoreCase(termoBusca.valorNormalizado()));
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        return buscarPorNome(TermoBusca.of(nome)).asList();
    }

    @Override
    public Produto salvar(Produto produto) {
        produtoValidador.validarProduto(produto);
        produtoSanitizador.sanitizar(produto);
        return produtoRepository.save(produto);
    }

    @Override
    public Produto atualizar(Long id, Produto dadosNovos) {
        Produto existente = buscarPorId(id);
        produtoValidador.validarProduto(dadosNovos);
        produtoSanitizador.sanitizar(dadosNovos);
        produtoMutator.aplicarAtualizacao(existente, dadosNovos);

        return produtoRepository.save(existente);
    }

    @Override
    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}

