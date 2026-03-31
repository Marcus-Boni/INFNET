package br.infnet.at.q3;

public enum CustomerType {
    REGULAR(1, 0.10),
    PREMIUM(2, 0.15),
    OTHER(0, 0.00);

    private final int code;
    private final double baseDiscount;

    CustomerType(int code, double baseDiscount) {
        this.code = code;
        this.baseDiscount = baseDiscount;
    }

    public static CustomerType fromCode(int code) {
        for (CustomerType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return OTHER;
    }

    public double baseDiscount() {
        return baseDiscount;
    }
}
