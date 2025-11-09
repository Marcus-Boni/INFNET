class ResultadoValidacao {

    private final boolean sucesso;
    private final String mensagemErro;

    private ResultadoValidacao(boolean sucesso, String mensagemErro) {
        this.sucesso = sucesso;
        this.mensagemErro = mensagemErro;
    }

    public static ResultadoValidacao sucesso() {
        return new ResultadoValidacao(true, null);
    }

    public static ResultadoValidacao falha(String motivo) {
        return new ResultadoValidacao(false, motivo);
    }

    public boolean isSucesso() {
        return this.sucesso;
    }

    public String getMensagemErro() {
        return this.mensagemErro;
    }
}