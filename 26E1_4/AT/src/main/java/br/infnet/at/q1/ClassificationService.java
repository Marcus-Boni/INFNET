package br.infnet.at.q1;

import java.util.Objects;
import java.util.function.Consumer;

public class ClassificationService {

    private static final int RARE_CASE_VALUE = -9999;

    public String classifyLevel(int value) {
        if (isRareCase(value)) {
            return "CASO RARO";
        }
        if (isHigh(value)) {
            return "ALTO";
        }
        if (isMedium(value)) {
            return "MÉDIO";
        }
        return "BAIXO";
    }

    public void printClassification(int value, Consumer<String> output) {
        Objects.requireNonNull(output, "output nao pode ser nulo");
        output.accept(classifyLevel(value));
    }

    private boolean isRareCase(int value) {
        return value == RARE_CASE_VALUE;
    }

    private boolean isHigh(int value) {
        return value > 10;
    }

    private boolean isMedium(int value) {
        return value == 10;
    }
}
