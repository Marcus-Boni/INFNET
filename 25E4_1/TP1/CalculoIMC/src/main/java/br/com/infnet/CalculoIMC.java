package br.com.infnet.calculoimc;
import javax.swing.JOptionPane;
import java.text.DecimalFormat;

public class CalculoIMC {

    public double calcularIMC(double peso, double altura) {
        if (altura <= 0) {
            return 0;
        }
        return peso / (altura * altura);
    }

    public String classificarIMC(double imc) {
        if (imc <= 0) {
            return "Inválido";
        } else if (imc < 18.5) {
            return "Abaixo do peso";
        } else if (imc < 24.9) {
            return "Peso normal";
        } else if (imc < 29.9) {
            return "Sobrepeso";
        } else if (imc < 34.9) {
            return "Obesidade Grau I";
        } else if (imc < 39.9) {
            return "Obesidade Grau II";
        } else {
            return "Obesidade Grau III";
        }
    }

    public static void main(String[] args) {
        CalculoIMC calculadora = new CalculoIMC();
        DecimalFormat df = new DecimalFormat("0.00");

        try {
            String inputPeso = JOptionPane.showInputDialog("Digite seu peso (em kg):");
            if (inputPeso == null) return;
            double p = Double.parseDouble(inputPeso.replace(",", "."));

            String inputAltura = JOptionPane.showInputDialog("Digite sua altura (em metros, ex: 1.75):");
            if (inputAltura == null) return;
            double a = Double.parseDouble(inputAltura.replace(",", "."));

            if (p <= 0 || a <= 0) {
                JOptionPane.showMessageDialog(null, "Peso e altura devem ser valores positivos.");
                return;
            }

            double imc = calculadora.calcularIMC(p, a);
            String classif = calculadora.classificarIMC(imc);

            String msg = "Seu IMC é: " + df.format(imc) + "\nClassificação: " + classif;
            JOptionPane.showMessageDialog(null, msg);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Entrada inválida. Digite apenas números.");
        }
    }
}