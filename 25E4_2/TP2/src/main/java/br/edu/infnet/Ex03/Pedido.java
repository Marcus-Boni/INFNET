interface Cliente {
    String getNome();
    boolean isNull();
}

class ClienteReal implements Cliente {
    private String nome;

    public ClienteReal(String nome) { this.nome = nome; }
    
    @Override
    public String getNome() { return nome; }
    
    @Override
    public boolean isNull() { return false; }
}

class ClienteNulo implements Cliente {
    // Explicação: O Null Object fornece um comportamento seguro e padrão.
    // Ele retorna uma string neutra (como "Cliente Desconhecido") em vez de null ou erro.
    @Override
    public String getNome() { return "Cliente Desconhecido"; } 

    @Override
    public boolean isNull() { return true; }
}

public class Pedido {
    private final Cliente cliente;

    // Explicação: O construtor garante que 'cliente' nunca será null.
    // Se o cliente real for null, é substituído por um ClienteNulo.
    public Pedido(Cliente cliente) {
        if (cliente == null) {
            this.cliente = new ClienteNulo(); // Padrão Null Object
        } else {
            this.cliente = cliente;
        }
    }

    public String getNomeCliente() {
        // Explicação: Não há necessidade de verificação 'if (cliente != null)',
        // pois o Null Object garante que 'getNome()' sempre será chamado com segurança.
        return cliente.getNome(); 
    }
}

/*
Explicação da Abordagem Aplicada (Padrão Null Object):
1.  **O Problema:** O código original falhava (lançava `NullPointerException`) quando o objeto `cliente` era nulo.
2.  **A Solução:** O **Padrão Null Object** substitui referências `null` por um objeto especial (`ClienteNulo`) que implementa a mesma interface (`Cliente`) e fornece um comportamento **seguro e padrão**.
3.  **Vantagem:** Elimina a necessidade de verificar se o objeto é nulo (`if (cliente != null)`) em toda parte do código que usa o cliente. A classe `Pedido` agora lida com o cliente real ou o cliente nulo de forma unificada e segura. O método `getNomeCliente()` sempre retornará uma `String` válida ("Cliente Desconhecido") em vez de falhar.
*/