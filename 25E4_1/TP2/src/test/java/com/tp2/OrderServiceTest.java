package com.tp2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de OrderService com Mockito")
class OrderServiceTest {

    @Mock
    private PaymentProcessor paymentProcessor;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(paymentProcessor);
    }

    @Test
    @DisplayName("Deve confirmar pedido quando pagamento for aprovado")
    void testProcessOrder_PaymentApproved_ReturnsTrue() {
        double amount = 100.0;
        when(paymentProcessor.processPayment(amount)).thenReturn(true);

        boolean result = orderService.processOrder(amount);

        assertTrue(result, "O pedido deveria ser confirmado quando o pagamento é aprovado");
        
        verify(paymentProcessor, times(1)).processPayment(amount);
    }

    @Test
    @DisplayName("Deve recusar pedido quando pagamento for negado")
    void testProcessOrder_PaymentDeclined_ReturnsFalse() {
        double amount = 50.0;
        when(paymentProcessor.processPayment(amount)).thenReturn(false);

        boolean result = orderService.processOrder(amount);

        assertFalse(result, "O pedido deveria ser recusado quando o pagamento é negado");
        
        verify(paymentProcessor, times(1)).processPayment(amount);
    }

    @Test
    @DisplayName("Deve chamar PaymentProcessor com o valor correto")
    void testProcessOrder_CallsPaymentProcessorWithCorrectAmount() {
        double amount = 250.75;
        when(paymentProcessor.processPayment(amount)).thenReturn(true);

        orderService.processOrder(amount);

        verify(paymentProcessor).processPayment(eq(amount));
    }

    @Test
    @DisplayName("Deve processar corretamente pedido com valor zero")
    void testProcessOrder_WithZeroAmount() {
        double amount = 0.0;
        when(paymentProcessor.processPayment(amount)).thenReturn(true);

        boolean result = orderService.processOrder(amount);

        assertTrue(result);
        verify(paymentProcessor, times(1)).processPayment(0.0);
    }

    @Test
    @DisplayName("Deve processar pedido com valor negativo se PaymentProcessor aceitar")
    void testProcessOrder_WithNegativeAmount() {
        double amount = -10.0;
        when(paymentProcessor.processPayment(amount)).thenReturn(false);

        boolean result = orderService.processOrder(amount);

        assertFalse(result);
        verify(paymentProcessor).processPayment(amount);
    }

    @Test
    @DisplayName("Deve chamar PaymentProcessor apenas uma vez por pedido")
    void testProcessOrder_CallsPaymentProcessorOnlyOnce() {
        double amount = 150.0;
        when(paymentProcessor.processPayment(amount)).thenReturn(true);

        orderService.processOrder(amount);

        verify(paymentProcessor, times(1)).processPayment(anyDouble());
        verifyNoMoreInteractions(paymentProcessor);
    }

    @Test
    @DisplayName("Deve processar múltiplos pedidos independentemente")
    void testProcessOrder_MultipleOrders() {
        when(paymentProcessor.processPayment(100.0)).thenReturn(true);
        when(paymentProcessor.processPayment(200.0)).thenReturn(false);
        when(paymentProcessor.processPayment(300.0)).thenReturn(true);

        assertTrue(orderService.processOrder(100.0));
        assertFalse(orderService.processOrder(200.0));
        assertTrue(orderService.processOrder(300.0));

        verify(paymentProcessor, times(1)).processPayment(100.0);
        verify(paymentProcessor, times(1)).processPayment(200.0);
        verify(paymentProcessor, times(1)).processPayment(300.0);
    }
}
