package br.infnet.at.q2;

public class ConsoleEmailSender implements EmailSender {
    @Override
    public void send(String email, String content) {
        System.out.println("Enviando email para: " + email);
        System.out.println("Conteudo:\n" + content);
    }
}
