package br.edu.infnet.logistica.servico;

import br.edu.infnet.logistica.dominio.Entrega;
import br.edu.infnet.logistica.servico.frete.FabricaCalculadoraFrete;

public class EtiquetaService {
    private final FabricaCalculadoraFrete fabricaCalculadoraFrete;

    public EtiquetaService(FabricaCalculadoraFrete fabricaCalculadoraFrete) {
        if (fabricaCalculadoraFrete == null) {
            throw new IllegalArgumentException("Fábrica de calculadora de frete não pode ser nula");
        }
        this.fabricaCalculadoraFrete = fabricaCalculadoraFrete;
    }

    public String gerarEtiqueta(Entrega entrega) {
        double valorFrete = fabricaCalculadoraFrete.calcularFrete(entrega);
        return String.format("Destinatário: %s\nEndereço: %s\nValor do Frete: R$%.2f",
                entrega.getDestinatario(),
                entrega.getEndereco(),
                valorFrete);
    }

    public String gerarResumoPedido(Entrega entrega) {
        double valorFrete = fabricaCalculadoraFrete.calcularFrete(entrega);
        return String.format("Pedido para %s com frete tipo %s no valor de R$%.2f",
                entrega.getDestinatario(),
                formatarTipoFrete(entrega.getTipoFrete()),
                valorFrete);
    }

    private String formatarTipoFrete(br.edu.infnet.logistica.dominio.TipoFrete tipoFrete) {
        switch (tipoFrete) {
            case EXPRESSO:
                return "EXP";
            case PADRAO:
                return "PAD";
            case ECONOMICO:
                return "ECO";
            default:
                return tipoFrete.toString();
        }
    }
}
