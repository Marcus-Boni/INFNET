package org.example.controller.api;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
        String nome,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
        String descricao,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        @DecimalMax(value = "999999.99", message = "O preço não pode exceder 999.999,99.")
        BigDecimal preco,

        @NotNull(message = "O estoque é obrigatório.")
        @Min(value = 0, message = "O estoque não pode ser negativo.")
        @Max(value = 100000, message = "O estoque não pode exceder 100.000 unidades.")
        Integer estoque
) {
}
