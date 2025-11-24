package com.tp2;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;

class MathFunctionsTest {
    @Property
    @Label("O dobro de um número não-negativo sempre é maior ou igual ao próprio número")
    boolean doubleIsGreaterOrEqualToOriginal(@ForAll @IntRange(min = 0, max = 1073741823) int number) {
        int result = MathFunctions.multiplyByTwo(number);
        return result >= number;
    }

    @Property
    @Label("Se x for par, então multiplyByTwo(x) também será par")
    boolean evenInputProducesEvenOutput(@ForAll int number) {
        Assume.that(number % 2 == 0);
        
        int result = MathFunctions.multiplyByTwo(number);
        return result % 2 == 0;
    }

    @Property
    @Label("O dobro de qualquer número sempre é par")
    boolean doubleIsAlwaysEven(@ForAll int number) {
        int result = MathFunctions.multiplyByTwo(number);
        return result % 2 == 0;
    }

    @Property
    @Label("O dobro dividido por 2 retorna o número original")
    boolean doubleIsDivisibleByTwo(@ForAll @IntRange(min = -1073741824, max = 1073741823) int number) {
        int result = MathFunctions.multiplyByTwo(number);
        return result / 2 == number;
    }

    @Property
    @Label("Multiplicar por 2 duas vezes é equivalente a multiplicar por 4")
    boolean doubleOfDoubleEqualsFourTimes(@ForAll @IntRange(min = -10000, max = 10000) int number) {
        int doubleOfDouble = MathFunctions.multiplyByTwo(MathFunctions.multiplyByTwo(number));
        int fourTimes = number * 4;
        return doubleOfDouble == fourTimes;
    }
}
