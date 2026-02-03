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

    public Price applyDiscount(double percentage) {
        if(percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentual de desconto inválido.");
        }

        BigDecimal discount = price.multiply(BigDecimal.valueOf(percentage / 100));
        return new Price(price.subtract(discount));
    }

    public Price applyDiscount(BigDecimal discount) {
        if(discount == null || discount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Desconto inválido.");
        }

        if(discount.compareTo(price) > 0) {
            throw new IllegalArgumentException("Desconto maior que o preço.");
        }

        return new Price(price.subtract(discount));
    }

    public BigDecimal getValue() {
        return price;
    }
}
