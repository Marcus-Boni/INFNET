package br.edu.infnet.exemplos;

import br.edu.infnet.exercicio3.ProdutoItem;

import java.math.BigDecimal;

public class ExemploExercicio3 {
    public static void main(String[] args) {
        System.out.println("=== Exercício 3: Objetos Menores Imutáveis ===\n");

        ProdutoItem item1 = new ProdutoItem("SKU-001", "Notebook Dell", 5, new BigDecimal("3500.00"));
        
        System.out.println("Item original:");
        System.out.println(item1);
        System.out.println();

        ProdutoItem item2 = item1.atualizarQuantidade(10);
        
        System.out.println("Após atualizar quantidade:");
        System.out.println("Item original: " + item1.getQuantidade() + " unidades");
        System.out.println("Novo item: " + item2.getQuantidade() + " unidades");
        System.out.println();

        ProdutoItem item3 = item2.atualizarPreco(new BigDecimal("3800.00"));
        
        System.out.println("Após atualizar preço:");
        System.out.println("Item anterior: R$ " + item2.getPrecoUnitario());
        System.out.println("Novo item: R$ " + item3.getPrecoUnitario());
        System.out.println();

        ProdutoItem item4 = item3.incrementarQuantidade(5);
        
        System.out.println("Após incrementar quantidade:");
        System.out.println("Item anterior: " + item3.getQuantidade() + " unidades");
        System.out.println("Novo item: " + item4.getQuantidade() + " unidades");
        System.out.println();

        ProdutoItem item5 = item4.decrementarQuantidade(3);
        
        System.out.println("Após decrementar quantidade:");
        System.out.println("Item anterior: " + item4.getQuantidade() + " unidades");
        System.out.println("Novo item: " + item5.getQuantidade() + " unidades");
        System.out.println();

        System.out.println("Demonstração de imutabilidade:");
        System.out.println("item1 (original): " + item1);
        System.out.println("item5 (final): " + item5);
        System.out.println("\nMesmo ID: " + item1.getId().equals(item5.getId()));
        System.out.println("Todos os objetos intermediários permanecem inalterados!");
    }
}
