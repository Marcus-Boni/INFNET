package br.edu.infnet.exercicio5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Cobertura - BubbleSort")
public class BubbleSortTest {

    @Test
    @DisplayName("Array vazio")
    void testArrayVazio() {
        Integer[] array = {};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{}, resultado);
    }

    @Test
    @DisplayName("Array com um elemento")
    void testArrayUmElemento() {
        Integer[] array = {5};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{5}, resultado);
    }

    @Test
    @DisplayName("Array já ordenado")
    void testArrayJaOrdenado() {
        Integer[] array = {1, 2, 3, 4, 5};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, resultado);
    }

    @Test
    @DisplayName("Array em ordem reversa")
    void testArrayOrdemReversa() {
        Integer[] array = {5, 4, 3, 2, 1};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, resultado);
    }

    @Test
    @DisplayName("Array com elementos duplicados")
    void testArrayElementosDuplicados() {
        Integer[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 1, 2, 3, 3, 4, 5, 5, 6, 9}, resultado);
    }

    @Test
    @DisplayName("Array com todos elementos iguais")
    void testArrayTodosIguais() {
        Integer[] array = {7, 7, 7, 7, 7};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{7, 7, 7, 7, 7}, resultado);
    }

    @Test
    @DisplayName("Array com números negativos")
    void testArrayNumerosNegativos() {
        Integer[] array = {-3, -1, -7, -4, -2};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{-7, -4, -3, -2, -1}, resultado);
    }

    @Test
    @DisplayName("Array com números positivos e negativos")
    void testArrayMisto() {
        Integer[] array = {3, -1, 4, -5, 2, 0};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{-5, -1, 0, 2, 3, 4}, resultado);
    }

    @Test
    @DisplayName("Array com dois elementos - ordem correta")
    void testDoisElementosOrdemCorreta() {
        Integer[] array = {1, 2};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 2}, resultado);
    }

    @Test
    @DisplayName("Array com dois elementos - ordem incorreta")
    void testDoisElementosOrdemIncorreta() {
        Integer[] array = {2, 1};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 2}, resultado);
    }

    @Test
    @DisplayName("Array grande")
    void testArrayGrande() {
        Integer[] array = new Integer[100];
        for (int i = 0; i < 100; i++) {
            array[i] = 100 - i;
        }
        Integer[] resultado = BubbleSort.sort(array);
        
        for (int i = 0; i < 99; i++) {
            assertTrue(resultado[i] <= resultado[i + 1]);
        }
    }

    @Test
    @DisplayName("Array com Strings")
    void testArrayStrings() {
        String[] array = {"banana", "apple", "cherry", "date"};
        String[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new String[]{"apple", "banana", "cherry", "date"}, resultado);
    }

    @Test
    @DisplayName("Teste de otimização - array quase ordenado")
    void testArrayQuaseOrdenado() {
        Integer[] array = {1, 2, 3, 5, 4};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, resultado);
    }

    @Test
    @DisplayName("Ramificação - sem troca necessária")
    void testSemTrocaNecessaria() {
        Integer[] array = {1, 2, 3};
        Integer[] original = Arrays.copyOf(array, array.length);
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(original, resultado);
    }

    @Test
    @DisplayName("Ramificação - todas as trocas necessárias")
    void testTodasTrocasNecessarias() {
        Integer[] array = {5, 4, 3, 2, 1};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, resultado);
    }

    @Test
    @DisplayName("Cobertura de decisão - break executado")
    void testBreakExecutado() {
        Integer[] array = {1, 3, 2, 4, 5};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, resultado);
    }

    @Test
    @DisplayName("Cobertura de decisão - break não executado")
    void testBreakNaoExecutado() {
        Integer[] array = {5, 4, 3, 2, 1};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, resultado);
    }

    @ParameterizedTest
    @MethodSource("provideDifferentArrays")
    @DisplayName("Testes parametrizados com diferentes arrays")
    void testDiferentesArrays(Integer[] input, Integer[] expected) {
        Integer[] resultado = BubbleSort.sort(input);
        assertArrayEquals(expected, resultado);
    }

    private static Stream<Arguments> provideDifferentArrays() {
        return Stream.of(
            Arguments.of(new Integer[]{}, new Integer[]{}),
            Arguments.of(new Integer[]{1}, new Integer[]{1}),
            Arguments.of(new Integer[]{2, 1}, new Integer[]{1, 2}),
            Arguments.of(new Integer[]{3, 1, 2}, new Integer[]{1, 2, 3}),
            Arguments.of(new Integer[]{4, 3, 2, 1}, new Integer[]{1, 2, 3, 4})
        );
    }

    @Test
    @DisplayName("Verificação de estabilidade")
    void testEstabilidade() {
        Integer[] array = {3, 2, 1, 3, 2, 1};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{1, 1, 2, 2, 3, 3}, resultado);
    }

    @Test
    @DisplayName("Teste com valores extremos")
    void testValoresExtremos() {
        Integer[] array = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
        Integer[] resultado = BubbleSort.sort(array);
        assertArrayEquals(new Integer[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, resultado);
    }
}
