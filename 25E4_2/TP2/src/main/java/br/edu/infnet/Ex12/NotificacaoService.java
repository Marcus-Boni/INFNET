public enum TipoNotificacao {
    EMAIL,
    SMS,
    PUSH
}

public class NotificacaoService {
    public void enviarNotificacao(TipoNotificacao tipo) {
        // Explicação: Remoção do 'default' para garantir que o compilador exija 
        // o tratamento explícito de todos os membros do enum.
        switch (tipo) {
            case EMAIL:
                System.out.println("Enviando e-mail...");
                break;
            case SMS:
                System.out.println("Enviando SMS...");
                break;
            case PUSH:
                System.out.println("Enviando notificação push...");
                break;
        }
        // Se a aplicação precisa de uma segurança extra, a verificação
        // de null deve ser feita antes do switch.
    }
}

/*
Refatoração do Código Removendo default e Garantindo Tratamento Explícito:
1.  **Violação Identificada:** O uso de `default` oculta a necessidade de tratar explicitamente os novos membros do `enum`, empurrando o erro para o *runtime*.
2.  **Solução:** O bloco `default` foi removido. O `switch` é agora **exaustivo** para os membros atuais do `enum`. Se um novo tipo for adicionado (`FAX`, por exemplo), o compilador falhará no `switch`, alertando o desenvolvedor sobre a necessidade de adicionar `case FAX:`. Isso é preferível a deixar um erro de lógica cair na cláusula `default`.
*/