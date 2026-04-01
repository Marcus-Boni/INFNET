package org.example.service.sanitization;

import org.example.model.Produto;
import org.example.service.support.ProdutoComando;
import org.springframework.stereotype.Component;

@Component
public class ProdutoSanitizador {

    public ProdutoComando sanitizar(Produto produto) {
        return sanitizar(ProdutoComando.of(produto));
    }

    public ProdutoComando sanitizar(ProdutoComando comando) {
        if (comando == null) {
            return null;
        }
        return new ProdutoComando(
                limpar(comando.nome()),
                limpar(comando.descricao()),
                comando.preco(),
                comando.estoque()
        );
    }

    private String limpar(String valor) {
        if (valor == null) {
            return null;
        }
        return valor.trim().replaceAll("[<>\"']", "");
    }
}
