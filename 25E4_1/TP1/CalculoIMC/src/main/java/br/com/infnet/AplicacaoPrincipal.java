package br.com.infnet;

// --- CLASSE HIPOTÉTICA PARA TESTAR ---
public class AplicacaoPrincipal {
    
    private final IMCApiServico servicoExterno; 

    public AplicacaoPrincipal(IMCApiServico servicoExterno) {
        this.servicoExterno = servicoExterno;
    }

    public String obterClassificacaoFormatada(double p, double a) {
        double imc = servicoExterno.calcularIMC(p, a); 

        if (imc < 25) {
            return "OK";
        } else {
            return "Alerta";
        }
    }
}

// --- CLASSE DA DEPENDÊNCIA (NÃO CHAMAR ELA NO TESTE) ---
interface IMCApiServico {
    double calcularIMC(double peso, double altura);
}