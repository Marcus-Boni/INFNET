package br.edu.infnet.exemplos;

import br.edu.infnet.exercicio4.CategoriaTransacao;
import br.edu.infnet.exercicio4.RegistroTransacao;
import br.edu.infnet.exercicio4.StatusTransacao;

import java.math.BigDecimal;

public class ExemploExercicio4 {
    public static void main(String[] args) {
        System.out.println("=== Exercício 4: Tipos de Dados Adequados ===\n");

        RegistroTransacao transacao = new RegistroTransacao(
            new BigDecimal("1500.75"),
            3,
            StatusTransacao.PENDENTE,
            CategoriaTransacao.VENDA,
            "Venda de produtos eletrônicos"
        );

        System.out.println("Transação criada:");
        System.out.println(transacao);
        System.out.println();

        System.out.println("Tipos de dados utilizados:");
        System.out.println("- identificador: String (UUID) - único e global");
        System.out.println("- valorMonetario: BigDecimal - precisão decimal exata");
        System.out.println("- quantidade: int - número inteiro adequado ao domínio");
        System.out.println("- dataCriacao/dataAtualizacao: LocalDateTime - API moderna de datas");
        System.out.println("- status: Enum StatusTransacao - type-safe, valores válidos garantidos");
        System.out.println("- categoria: Enum CategoriaTransacao - auto-documentado");
        System.out.println();

        RegistroTransacao transacaoAtualizada = transacao.atualizarStatus(StatusTransacao.APROVADA);
        
        System.out.println("Após atualizar status:");
        System.out.println("Original: " + transacao.getStatus());
        System.out.println("Atualizada: " + transacaoAtualizada.getStatus());
        System.out.println();

        System.out.println("Cálculo de valor total:");
        System.out.println("Valor unitário: R$ " + transacao.getValorMonetario());
        System.out.println("Quantidade: " + transacao.getQuantidade());
        System.out.println("Total: R$ " + transacao.calcularValorTotal());
        System.out.println();

        System.out.println("Vantagens dos tipos escolhidos:");
        System.out.println("✓ BigDecimal evita erros de arredondamento em valores monetários");
        System.out.println("✓ LocalDateTime é imutável e thread-safe");
        System.out.println("✓ Enums garantem apenas valores válidos em compile-time");
        System.out.println("✓ int é eficiente para quantidades inteiras");
    }
}
