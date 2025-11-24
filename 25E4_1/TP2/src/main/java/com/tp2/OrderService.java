package com.tp2;

public class OrderService {
    private PaymentProcessor paymentProcessor;

    public OrderService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public boolean processOrder(double amount) {
        if (paymentProcessor.processPayment(amount)) {
            System.out.println("Pedido confirmado.");
            return true;
        } else {
            System.out.println("Pagamento recusado.");
            return false;
        }
    }
}
