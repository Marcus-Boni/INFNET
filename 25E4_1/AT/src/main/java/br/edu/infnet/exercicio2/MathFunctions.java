package br.edu.infnet.exercicio2;

public class MathFunctions {
    private MathLogger logger;

    public MathFunctions() {
        this.logger = null;
    }

    public MathFunctions(MathLogger logger) {
        this.logger = logger;
    }

    public int multiplyByTwo(int number) {
        if (logger != null) {
            logger.log("multiplyByTwo", new int[]{number});
        }
        return number * 2;
    }

    public int[] generateMultiplicationTable(int number, int limit) {
        if (logger != null) {
            logger.log("generateMultiplicationTable", new int[]{number, limit});
        }
        int[] result = new int[limit];
        for (int i = 0; i < limit; i++) {
            result[i] = number * (i + 1);
        }
        return result;
    }

    public boolean isPrime(int number) {
        if (logger != null) {
            logger.log("isPrime", new int[]{number});
        }
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    public double calculateAverage(int[] numbers) {
        if (logger != null) {
            logger.log("calculateAverage", numbers);
        }
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }
        double sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum / numbers.length;
    }
}
