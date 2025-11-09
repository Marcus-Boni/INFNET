
class ValidadorCPF {

    private static final int TAMANHO_CPF = 11;

    public ResultadoValidacao validar(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return ResultadoValidacao.falha("CPF não pode ser nulo ou vazio.");
        }

        if (!cpf.matches("\\d+")) {
            return ResultadoValidacao.falha("CPF deve conter apenas números.");
        }
        
        if (cpf.length() != TAMANHO_CPF) {
            return ResultadoValidacao.falha("CPF deve ter " + TAMANHO_CPF + " dígitos.");
        }

        return ResultadoValidacao.sucesso();
    }
}