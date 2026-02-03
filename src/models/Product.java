package models;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private Price price;
    private String description;
    private String imageUrl;

    public Product() {}

    public Product(Long id, String name, Price price, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String toString() {
        return id + " - " + name + " - " + price.getValue();
    }
}
