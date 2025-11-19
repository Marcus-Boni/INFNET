import java.util.LinkedList;
import java.util.Queue;

public class FilaMensagens {
    private Queue<String> mensagens = new LinkedList<>();

    public String visualizarProximaMensagem() {
        // Explicação: peek() retorna a cabeça da fila sem remover.
        return mensagens.peek(); 
    }

    public String consumirProximaMensagem() {
        // Explicação: poll() remove e retorna a cabeça da fila. Este é o Command.
        return mensagens.poll(); 
    }
    
    // Getter para tamanho da fila (exemplo de outra Query)
    public int tamanho() {
        return mensagens.size();
    }
    
    // Setter para adicionar mensagem (exemplo de outro Command)
    public void adicionarMensagem(String mensagem) {
        mensagens.add(mensagem);
    }
}

/*
Proposta de Refatoração para Separar Query de Command:
1.  **Método Query (`visualizarProximaMensagem`):** Usa `Queue.peek()` para obter a mensagem na cabeça da fila **sem removê-la**. Este método é seguro para ser chamado múltiplas vezes para inspeção.
2.  **Método Command (`consumirProximaMensagem`):** Usa `Queue.poll()` para **remover e retornar** a mensagem da cabeça. Este método é reservado para o momento em que a mensagem deve ser processada e consumida.
Essa separação torna a interação com a fila clara e previsível, seguindo o CQS.
*/