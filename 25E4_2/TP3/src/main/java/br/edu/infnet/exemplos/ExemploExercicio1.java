package br.edu.infnet.exemplos;

import br.edu.infnet.exercicio1.ItemPedido;
import br.edu.infnet.exercicio1.Pedido;
import br.edu.infnet.exercicio1.StatusPedido;

import java.math.BigDecimal;
import java.util.Arrays;

public class ExemploExercicio1 {
    public static void main(String[] args) {
        System.out.println("=== Exercício 1: Imutabilidade do Pedido ===\n");

        ItemPedido item1 = new ItemPedido("prod-1", "Notebook", 1, new BigDecimal("3000.00"));
        ItemPedido item2 = new ItemPedido("prod-2", "Mouse", 2, new BigDecimal("50.00"));

        Pedido pedidoOriginal = new Pedido("cliente-123", Arrays.asList(item1, item2));
        
        System.out.println("Pedido Original:");
        System.out.println(pedidoOriginal);
        System.out.println();

        Pedido pedidoConfirmado = pedidoOriginal.atualizarStatus(StatusPedido.CONFIRMADO);
        
        System.out.println("Após atualizar status:");
        System.out.println("Original (não modificado): " + pedidoOriginal);
        System.out.println("Novo pedido confirmado: " + pedidoConfirmado);
        System.out.println();

        ItemPedido item3 = new ItemPedido("prod-3", "Teclado", 1, new BigDecimal("150.00"));
        Pedido pedidoComNovoItem = pedidoConfirmado.adicionarItem(item3);

        System.out.println("Após adicionar item:");
        System.out.println("Pedido anterior: " + pedidoConfirmado.getItens().size() + " itens");
        System.out.println("Novo pedido: " + pedidoComNovoItem.getItens().size() + " itens");
        System.out.println();

        System.out.println("Demonstração de imutabilidade:");
        System.out.println("- pedidoOriginal mantém status: " + pedidoOriginal.getStatus());
        System.out.println("- pedidoConfirmado tem status: " + pedidoConfirmado.getStatus());
        System.out.println("- Cada operação criou um novo objeto sem modificar o anterior");
    }
}
