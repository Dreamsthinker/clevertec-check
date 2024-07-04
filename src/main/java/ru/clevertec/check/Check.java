package check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Check {
    private List<CheckItem> items;
    private BigDecimal total;
    private BigDecimal discount;
    private BigDecimal totalWithDiscount;
    private DiscountCard discountCard;

    private Check() {
        this.items = new ArrayList<>();
    }

    public List<CheckItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotalWithDiscount() {
        return totalWithDiscount;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public static class CheckBuilder {
        private Check check;

        public CheckBuilder() {
            check = new Check();
        }

        public CheckBuilder addItem(Product product, int quantity, BigDecimal total, BigDecimal discount) {
            check.items.add(new CheckItem(product, quantity, total, discount));
            return this;
        }

        public CheckBuilder setTotal(BigDecimal total) {
            check.total = total;
            return this;
        }

        public CheckBuilder setDiscount(BigDecimal discount) {
            check.discount = discount;
            return this;
        }

        public CheckBuilder setTotalWithDiscount(BigDecimal totalWithDiscount) {
            check.totalWithDiscount = totalWithDiscount;
            return this;
        }

        public CheckBuilder setDiscountCard(DiscountCard discountCard) {
            check.discountCard = discountCard;
            return this;
        }

        public Check build() {
            return check;
        }
    }

    public static class CheckItem {
        private Product product;
        private int quantity;
        private BigDecimal total;
        private BigDecimal discount;

        public CheckItem(Product product, int quantity, BigDecimal total, BigDecimal discount) {
            this.product = product;
            this.quantity = quantity;
            this.total = total;
            this.discount = discount;
        }

        // Геттеры
        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public BigDecimal getTotal() { return total; }
        public BigDecimal getDiscount() { return discount; }
    }
}