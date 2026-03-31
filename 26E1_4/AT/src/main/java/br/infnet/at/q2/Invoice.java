package br.infnet.at.q2;

public class Invoice {
    private final String clientName;
    private final String clientEmail;
    private final double amount;
    private final InvoiceType type;

    public Invoice(String clientName, String clientEmail, double amount, InvoiceType type) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.amount = amount;
        this.type = type;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public double getAmount() {
        return amount;
    }

    public InvoiceType getType() {
        return type;
    }
}
