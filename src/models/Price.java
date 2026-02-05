package models;

import java.math.BigDecimal;

public final class Price {
    private final BigDecimal price;

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if(price == null) {
            throw new IllegalArgumentException("Preço não pode ser nulo.");
        }

        if(price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
        }
    }

    public BigDecimal getValue() {
        return price;
    }
}
