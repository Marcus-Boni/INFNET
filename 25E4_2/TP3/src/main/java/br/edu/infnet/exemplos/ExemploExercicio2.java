package br.edu.infnet.exemplos;

import br.edu.infnet.exercicio2.*;
import br.edu.infnet.exercicio1.ItemPedido;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ExemploExercicio2 {
    public static void main(String[] args) {
        System.out.println("=== Exercício 2: Refatoração e Efeitos Colaterais ===\n");

        List<ItemPedido> itens = Arrays.asList(
            new ItemPedido("prod-1", "Produto A", 2, new BigDecimal("100.00")),
            new ItemPedido("prod-2", "Produto B", 1, new BigDecimal("50.00"))
        );

        System.out.println("--- Versão COM Efeitos Colaterais (Problemática) ---");
        DescontoAplicavel descontoMutavel = new DescontoAplicavel(BigDecimal.TEN);
        CalculadoraPedidoComEfeitosColaterais calculadoraProblematica = 
            new CalculadoraPedidoComEfeitosColaterais();
        
        System.out.println("Antes do cálculo - Desconto aplicado: " + descontoMutavel.isAplicado());
        BigDecimal totalProblematico = calculadoraProblematica.calcularTotal(itens, descontoMutavel);
        System.out.println("Depois do cálculo - Desconto aplicado: " + descontoMutavel.isAplicado());
        System.out.println("Total: " + totalProblematico);
        System.out.println("Problema: O objeto descontoMutavel foi modificado!\n");

        System.out.println("--- Versão REFATORADA (Sem Efeitos Colaterais) ---");
        Desconto descontoImutavel = new Desconto(BigDecimal.TEN, "DESC10", "10% de desconto");
        CalculadoraPedidoRefatorada calculadoraRefatorada = new CalculadoraPedidoRefatorada();
        
        ResultadoCalculo resultado = calculadoraRefatorada.calcularTotal(itens, descontoImutavel);
        
        System.out.println("Resultado do cálculo:");
        System.out.println("- Subtotal: " + resultado.getSubtotal());
        System.out.println("- Valor desconto: " + resultado.getValorDesconto());
        System.out.println("- Total: " + resultado.getTotal());
        System.out.println("- Desconto aplicado: " + resultado.getDescontoAplicado().get().getCodigo());
        System.out.println("\nVantagem: O objeto descontoImutavel permanece inalterado!");
        System.out.println("Todas as informações estão no ResultadoCalculo");
    }
}
