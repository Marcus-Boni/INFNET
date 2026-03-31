package br.infnet.at.q1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClassificationServiceTest {

    private final ClassificationService service = new ClassificationService();

    @Test
    void shouldClassifyAsHighWhenValueIsGreaterThanTen() {
        assertEquals("ALTO", service.classifyLevel(11));
    }

    @Test
    void shouldClassifyAsMediumWhenValueIsExactlyTen() {
        assertEquals("MÉDIO", service.classifyLevel(10));
    }

    @Test
    void shouldClassifyAsLowWhenValueIsLessThanTen() {
        assertEquals("BAIXO", service.classifyLevel(9));
    }

    @Test
    void shouldClassifyAsRareCaseWhenValueMatchesSentinel() {
        assertEquals("CASO RARO", service.classifyLevel(-9999));
    }
}
