package models;

import java.math.BigDecimal;
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

   private BigDecimal calculateTotal() {
        BigDecimal sum = BigDecimal.ZERO;

        for(Product p : products) {
            sum = sum.add(p.getPrice());
        }

        return sum;
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
               " | Produtos: " + products.size() + " | Total: R$" + calculateTotal();
    }
}
