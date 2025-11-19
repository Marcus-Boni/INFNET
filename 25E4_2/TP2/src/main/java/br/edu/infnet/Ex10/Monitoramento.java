import java.util.concurrent.atomic.AtomicInteger;

public class Monitoramento {
    // Explicação: Usar AtomicInteger garante que as operações 
    // de leitura e incremento sejam seguras em ambientes multi-thread.
    private final AtomicInteger contadorAcessos = new AtomicInteger(0);

    public int getContadorAcessos() {
        // Explicação: get() é o método seguro para leitura (Query).
        return contadorAcessos.get(); 
    }

    public int incrementarContadorAcessos() {
        // Explicação: incrementAndGet() é o método seguro para atualização (Command).
        return contadorAcessos.incrementAndGet(); 
    }
}

/*
Proposta de Refatoração Seguindo o Princípio CQS:
1.  **Separação:** Dois métodos foram criados: `getContadorAcessos` (Query) e `incrementarContadorAcessos` (Command).
2.  **Segurança em Concorrência:** A variável `contadorAcessos` foi alterada para `AtomicInteger`. Embora o CQS possa ser implementado com um `int` simples e dois métodos, o uso de `AtomicInteger` reflete a **boa prática** de lidar com contadores em sistemas multi-thread, garantindo que as operações de leitura e escrita sejam thread-safe.
*/