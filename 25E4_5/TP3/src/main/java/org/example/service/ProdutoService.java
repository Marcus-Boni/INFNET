package org.example.service;

import org.example.exception.NegocioException;
import org.example.exception.ProdutoNotFoundException;
import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Serviço de negócio para Produto.
 * Aplica fail-early: valida precondições antes de executar qualquer operação.
 */
@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        // Fail Early: rejeita IDs inválidos antes de consultar o banco
        if (id == null || id <= 0) {
            throw new NegocioException("ID inválido: " + id);
        }
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        if (!StringUtils.hasText(nome)) {
            return listarTodos();
        }
        // Fail Early: bloqueia entradas suspeitamente longas
        if (nome.length() > 200) {
            throw new NegocioException("Termo de busca muito longo.");
        }
        return repository.findByNomeContainingIgnoreCase(nome.trim());
    }

    public Produto salvar(Produto produto) {
        validarProduto(produto);
        sanitizar(produto);
        return repository.save(produto);
    }

    public Produto atualizar(Long id, Produto dadosNovos) {
        // Fail Early: garante existência antes de atualizar
        Produto existente = buscarPorId(id);
        validarProduto(dadosNovos);
        sanitizar(dadosNovos);

        existente.setNome(dadosNovos.getNome());
        existente.setDescricao(dadosNovos.getDescricao());
        existente.setPreco(dadosNovos.getPreco());
        existente.setEstoque(dadosNovos.getEstoque());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        // Fail Early: confirma existência antes de deletar
        Produto produto = buscarPorId(id);
        repository.delete(produto);
    }

    // -------------------------------------------------------------------------
    // Helpers privados
    // -------------------------------------------------------------------------

    /**
     * Validações de negócio além das anotações Bean Validation.
     * Fail Early: lança exceção imediatamente ao detectar violação.
     */
    private void validarProduto(Produto p) {
        if (p == null) {
            throw new NegocioException("Produto não pode ser nulo.");
        }
        if (!StringUtils.hasText(p.getNome())) {
            throw new NegocioException("O nome do produto é obrigatório.");
        }
        if (p.getPreco() == null || p.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("O preço deve ser maior que zero.");
        }
        if (p.getEstoque() == null || p.getEstoque() < 0) {
            throw new NegocioException("O estoque não pode ser negativo.");
        }
    }

    /** Remove caracteres potencialmente perigosos (XSS básico). */
    private void sanitizar(Produto p) {
        if (p.getNome() != null) {
            p.setNome(p.getNome().trim().replaceAll("[<>\"']", ""));
        }
        if (p.getDescricao() != null) {
            p.setDescricao(p.getDescricao().trim().replaceAll("[<>\"']", ""));
        }
    }
}

