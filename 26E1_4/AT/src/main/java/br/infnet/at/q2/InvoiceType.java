package br.infnet.at.q2;

public enum InvoiceType {
    SIMPLE(1, "Simples"),
    TAXED(2, "Com imposto"),
    UNKNOWN(0, "Desconhecido");

    private final int code;
    private final String description;

    InvoiceType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static InvoiceType fromCode(int code) {
        for (InvoiceType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public String description() {
        return description;
    }
}
