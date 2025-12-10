package br.edu.infnet.logistica.dominio;

import br.edu.infnet.logistica.excecao.EntregaInvalidaException;

public final class Entrega {
    private final String destinatario;
    private final String endereco;
    private final double pesoEmKg;
    private final TipoFrete tipoFrete;

    public Entrega(String destinatario, String endereco, double pesoEmKg, TipoFrete tipoFrete) {
        validarDestinatario(destinatario);
        validarEndereco(endereco);
        validarPeso(pesoEmKg);
        validarTipoFrete(tipoFrete);

        this.destinatario = destinatario;
        this.endereco = endereco;
        this.pesoEmKg = pesoEmKg;
        this.tipoFrete = tipoFrete;
    }

    private void validarDestinatario(String destinatario) {
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new EntregaInvalidaException("Destinatário não pode ser nulo ou vazio");
        }
    }

    private void validarEndereco(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new EntregaInvalidaException("Endereço não pode ser nulo ou vazio");
        }
    }

    private void validarPeso(double peso) {
        if (peso <= 0) {
            throw new EntregaInvalidaException("Peso deve ser maior que zero");
        }
    }

    private void validarTipoFrete(TipoFrete tipoFrete) {
        if (tipoFrete == null) {
            throw new EntregaInvalidaException("Tipo de frete não pode ser nulo");
        }
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getEndereco() {
        return endereco;
    }

    public double getPesoEmKg() {
        return pesoEmKg;
    }

    public TipoFrete getTipoFrete() {
        return tipoFrete;
    }

    public boolean isElegivelFreteGratis() {
        return tipoFrete == TipoFrete.ECONOMICO && pesoEmKg < 2;
    }

    public Entrega aplicarDescontoPeso(double descontoEmKg) {
        if (descontoEmKg < 0) {
            throw new EntregaInvalidaException("Desconto não pode ser negativo");
        }
        double novoPeso = Math.max(0.1, pesoEmKg - descontoEmKg);
        return new Entrega(destinatario, endereco, novoPeso, tipoFrete);
    }
}
