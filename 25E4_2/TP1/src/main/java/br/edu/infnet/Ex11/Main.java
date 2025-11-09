
public class Main {
    public static void main(String[] args) {
        ValidadorCPF validador = new ValidadorCPF();

        String[] cpfsParaTestar = {
            "12345678901", 
            null,          
            "12345",       
            "1234567890a"  
        };

        for (String cpf : cpfsParaTestar) {
            System.out.println("Validando CPF: " + cpf);
            ResultadoValidacao resultado = validador.validar(cpf);

            if (resultado.isSucesso()) {
                System.out.println(">> SUCESSO: CPF válido.\n");
            } else {
                System.out.println(">> FALHA: " + resultado.getMensagemErro() + "\n");
            }
        }
    }
}