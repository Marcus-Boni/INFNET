package br.infnet.at.q2;

import java.util.Objects;

public class InvoiceProcessor {
    private final EmailSender emailSender;

    public InvoiceProcessor(EmailSender emailSender) {
        this.emailSender = Objects.requireNonNull(emailSender, "emailSender nao pode ser nulo");
    }

    public void process(Invoice invoice) {
        validateEmail(invoice.getClientEmail());

        String note = buildInvoiceNote(invoice);
        System.out.println(note);
        emailSender.send(invoice.getClientEmail(), note);
    }

    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email invalido. Falha no envio.");
        }
    }

    private String buildInvoiceNote(Invoice invoice) {
        return "--- NOTA FISCAL ---\n" +
            "Cliente: " + invoice.getClientName() + "\n" +
            "Valor: R$ " + invoice.getAmount() + "\n" +
            "Tipo: " + invoice.getType().description() + "\n" +
            "---------------------";
    }
}
