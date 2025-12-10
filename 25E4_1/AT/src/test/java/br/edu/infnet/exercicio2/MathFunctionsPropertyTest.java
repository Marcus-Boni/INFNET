package br.edu.infnet.exercicio2;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import static org.junit.jupiter.api.Assertions.*;

public class MathFunctionsPropertyTest {

    @Property
    @Label("MultiplyByTwo sempre retorna número par")
    void multiplyByTwoAlwaysReturnsEven(@ForAll int number) {
        MathFunctions math = new MathFunctions();
        int result = math.multiplyByTwo(number);
        assertEquals(0, result % 2, "O resultado de multiplicar por 2 deve ser par");
    }

    @Property
    @Label("MultiplyByTwo retorna o dobro do número")
    void multiplyByTwoReturnsDouble(@ForAll int number) {
        MathFunctions math = new MathFunctions();
        int result = math.multiplyByTwo(number);
        assertEquals(number * 2, result);
    }

    @Property
    @Label("GenerateMultiplicationTable - todos elementos são múltiplos do número")
    void multiplicationTableAllElementsAreMultiples(
            @ForAll @IntRange(min = -100, max = 100) int number,
            @ForAll @IntRange(min = 1, max = 20) int limit) {
        MathFunctions math = new MathFunctions();
        int[] table = math.generateMultiplicationTable(number, limit);

        assertEquals(limit, table.length);
        for (int i = 0; i < table.length; i++) {
            assertEquals(number * (i + 1), table[i]);
            if (number != 0) {
                assertEquals(0, table[i] % number, 
                    "Elemento " + table[i] + " deve ser múltiplo de " + number);
            }
        }
    }

    @Property
    @Label("GenerateMultiplicationTable - sequência crescente para números positivos")
    void multiplicationTableIncreasingForPositive(
            @ForAll @IntRange(min = 1, max = 100) int number,
            @ForAll @IntRange(min = 2, max = 20) int limit) {
        MathFunctions math = new MathFunctions();
        int[] table = math.generateMultiplicationTable(number, limit);

        for (int i = 1; i < table.length; i++) {
            assertTrue(table[i] > table[i - 1], 
                "Para números positivos, tabela deve ser crescente");
        }
    }

    @Property
    @Label("IsPrime - números primos não têm divisores além de 1 e ele mesmo")
    void primeNumbersHaveNoOtherDivisors(@ForAll @IntRange(min = 2, max = 1000) int number) {
        MathFunctions math = new MathFunctions();
        boolean isPrime = math.isPrime(number);

        if (isPrime) {
            for (int i = 2; i < number; i++) {
                assertNotEquals(0, number % i, 
                    "Número primo " + number + " não deve ser divisível por " + i);
            }
        }
    }

    @Property
    @Label("IsPrime - números menores ou iguais a 1 não são primos")
    void numbersLessThanTwoAreNotPrime(@ForAll @IntRange(max = 1) int number) {
        MathFunctions math = new MathFunctions();
        assertFalse(math.isPrime(number), number + " não deve ser primo");
    }

    @Property
    @Label("IsPrime - 2 é primo")
    void twoIsPrime() {
        MathFunctions math = new MathFunctions();
        assertTrue(math.isPrime(2), "2 deve ser primo");
    }

    @Property
    @Label("IsPrime - números pares maiores que 2 não são primos")
    void evenNumbersGreaterThanTwoAreNotPrime(
            @ForAll @IntRange(min = 4, max = 1000) int number) {
        Assume.that(number % 2 == 0);
        MathFunctions math = new MathFunctions();
        assertFalse(math.isPrime(number), number + " é par e maior que 2, não deve ser primo");
    }

    @Property
    @Label("CalculateAverage - média está entre o menor e maior valor")
    void averageBetweenMinAndMax(
            @ForAll @Size(min = 1, max = 100) int[] numbers) {
        MathFunctions math = new MathFunctions();
        double average = math.calculateAverage(numbers);

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int num : numbers) {
            if (num < min) min = num;
            if (num > max) max = num;
        }

        assertTrue(average >= min, "Média deve ser >= menor valor");
        assertTrue(average <= max, "Média deve ser <= maior valor");
    }

    @Property
    @Label("CalculateAverage - array com elementos iguais retorna o próprio valor")
    void averageOfEqualElementsIsTheElement(
            @ForAll int value,
            @ForAll @IntRange(min = 1, max = 50) int size) {
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = value;
        }

        MathFunctions math = new MathFunctions();
        double average = math.calculateAverage(numbers);
        assertEquals(value, average, 0.0001);
    }

    @Property
    @Label("CalculateAverage - array vazio lança exceção")
    void emptyArrayThrowsException() {
        MathFunctions math = new MathFunctions();
        assertThrows(IllegalArgumentException.class, () -> {
            math.calculateAverage(new int[]{});
        });
    }

    @Property
    @Label("CalculateAverage - array null lança exceção")
    void nullArrayThrowsException() {
        MathFunctions math = new MathFunctions();
        assertThrows(IllegalArgumentException.class, () -> {
            math.calculateAverage(null);
        });
    }

    @Property
    @Label("CalculateAverage - soma dos valores dividida pelo tamanho")
    void averageIsCorrectCalculation(@ForAll @Size(min = 1, max = 50) int[] numbers) {
        MathFunctions math = new MathFunctions();
        double average = math.calculateAverage(numbers);

        long sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        double expectedAverage = (double) sum / numbers.length;

        assertEquals(expectedAverage, average, 0.0001);
    }

    @Property
    @Label("MultiplyByTwo - propriedade de comutatividade")
    void multiplyByTwoCommutative(@ForAll int a, @ForAll int b) {
        MathFunctions math = new MathFunctions();
        Assume.that((long) a + b <= Integer.MAX_VALUE && (long) a + b >= Integer.MIN_VALUE);
        
        int result1 = math.multiplyByTwo(a + b);
        int result2 = math.multiplyByTwo(a) + math.multiplyByTwo(b);
        
        assertEquals(result1, result2, 
            "2*(a+b) deve ser igual a 2*a + 2*b");
    }

    @Property
    @Label("IsPrime - teste com números primos conhecidos")
    void knownPrimesAreIdentified() {
        MathFunctions math = new MathFunctions();
        int[] knownPrimes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};
        
        for (int prime : knownPrimes) {
            assertTrue(math.isPrime(prime), prime + " deve ser identificado como primo");
        }
    }

    @Property
    @Label("IsPrime - teste com números compostos conhecidos")
    void knownCompositesAreNotPrime() {
        MathFunctions math = new MathFunctions();
        int[] composites = {4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20};
        
        for (int composite : composites) {
            assertFalse(math.isPrime(composite), 
                composite + " não deve ser identificado como primo");
        }
    }
}
