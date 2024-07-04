package check;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String description;
    private BigDecimal price;
    private boolean isWholesale;

    public Product(int id, String description, BigDecimal price, boolean isWholesale) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.isWholesale = isWholesale;
    }

    // Геттеры
    public int getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public boolean isWholesale() { return isWholesale; }
}