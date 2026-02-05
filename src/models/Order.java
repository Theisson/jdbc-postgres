package models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Double latitude;
    private Double longitude;
    private Instant moment;
    private List<Product> products = new ArrayList<>();
    private OrderStatus status;

    public Order(Long id, Double latitude, Double longitude, Instant moment, OrderStatus status) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.moment = moment;
        this.status = status;
    }

    public BigDecimal getTotal() {
        BigDecimal sum = BigDecimal.ZERO;

        for(Product product : products) {
            sum = sum.add(product.getPrice());
        }

        return sum;
    }

    public boolean canApplyDiscount() {
        if(status == OrderStatus.DELIVERED || status == OrderStatus.CANCELED || status == OrderStatus.SHIPPED) {
            return false;
        }
        return getTotal().compareTo(new BigDecimal("70.00")) >= 0;
    }

    public BigDecimal applyDiscount(double percentage) {
        if(percentage <= 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentual de desconto inválido.");
        }
        BigDecimal discount = getTotal().multiply(BigDecimal.valueOf(percentage / 100))
                                        .setScale(2, RoundingMode.HALF_UP);
                                        
        return getTotal().subtract(discount);
    }

    public BigDecimal applyDiscount(BigDecimal fixed) {
        if(fixed.compareTo(getTotal()) >= 0) {
            throw new IllegalArgumentException("Desconto fixo não pode ser maior ou igual ao total do pedido.");
        }
        
        if(fixed.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Desconto fixo deve ser maior que zero.");
        }

        return getTotal().subtract(fixed);
    }
    
    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Instant getMoment() {
        return moment;
    }

    public List<Product> getProducts() {
        return products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String toString() {
        return "Pedido #" + id + " | " + moment + " | " + status.getDisplayName() + 
               " | Produtos: " + products.size() + " | Total: R$" + getTotal();
    }
}
