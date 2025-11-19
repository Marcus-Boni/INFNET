public enum StatusPedido {
    PENDENTE,
    PROCESSANDO,
    ENVIADO,
    ENTREGUE
}

public class PedidoService {
    public void atualizarStatus(StatusPedido status) {
        // Explicação: O switch agora trata APENAS os casos definidos no enum.
        // Se um novo status for adicionado ao enum, o compilador irá FALHAR, 
        // exigindo que este switch seja atualizado.
        switch (status) {
            case PENDENTE:
                System.out.println("O pedido está pendente.");
                break;
            case PROCESSANDO:
                System.out.println("O pedido está sendo processado.");
                break;
            case ENVIADO:
                System.out.println("O pedido foi enviado.");
                break;
            case ENTREGUE:
                System.out.println("O pedido foi entregue.");
                break;
            // REMOÇÃO DO 'default:'
        }
    }
}

/*
Proposta de Refatoração Removendo default e Garantindo Cobertura Completa:
O bloco `default` foi removido. Em Java, ao usar um `switch` em um `enum`, se você tentar compilar o código após adicionar um novo membro ao `enum` sem atualizar o `switch`, o compilador (dependendo da versão e configuração) emitirá um aviso ou um erro, forçando a atualização. Se por acaso um valor `null` for passado, uma exceção (como `NullPointerException`) será lançada, o que é preferível a um `default` silencioso.
*/