package org.example.service.sanitization;

import org.example.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoSanitizador {

    public void sanitizar(Produto produto) {
        if (produto.getNome() != null) {
            produto.setNome(limpar(produto.getNome()));
        }
        if (produto.getDescricao() != null) {
            produto.setDescricao(limpar(produto.getDescricao()));
        }
    }

    private String limpar(String valor) {
        return valor.trim().replaceAll("[<>\"']", "");
    }
}
