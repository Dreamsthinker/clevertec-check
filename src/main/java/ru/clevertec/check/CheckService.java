package check;

import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CheckService {
    private ProductRepository productRepository;
    private DiscountCardRepository discountCardRepository;

    public CheckService(ProductRepository productRepository, DiscountCardRepository discountCardRepository) {
        this.productRepository = productRepository;
        this.discountCardRepository = discountCardRepository;
    }

    public Check createCheck(List<ProductItem> items, Integer discountCardNumber, BigDecimal balance) {
        /*if (balance == null) {
            throw new IllegalArgumentException("Balance not provided");
        }*/

        Check.CheckBuilder builder = new Check.CheckBuilder();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;

        Map<Integer, Integer> productQuantities = new HashMap<>();
        for (ProductItem item : items) {
            productQuantities.merge(item.getProductId(), item.getQuantity(), Integer::sum);
        }

        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productRepository.getById(productId);
            if (product == null) {
                throw new ProductNotFoundException("Product with id " + productId + " not found");
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            BigDecimal itemDiscount = calculateDiscount(product, quantity, discountCardNumber);

            builder.addItem(product, quantity, itemTotal, itemDiscount);
            total = total.add(itemTotal);
            discount = discount.add(itemDiscount);
        }

        BigDecimal totalWithDiscount = total.subtract(discount);

        if (balance.compareTo(totalWithDiscount) < 0) {
            throw new InsufficientFundsException("Not enough money on the balance");
        }

        return builder
                .setTotal(total)
                .setDiscount(discount)
                .setTotalWithDiscount(totalWithDiscount)
                .setDiscountCard(discountCardNumber != null ? discountCardRepository.getByNumber(discountCardNumber) : null)
                .build();
    }

    private BigDecimal calculateDiscount(Product product, int quantity, Integer discountCardNumber) {
        BigDecimal discount = BigDecimal.ZERO;
        if (product.isWholesale() && quantity >= 5) {
            discount = product.getPrice().multiply(BigDecimal.valueOf(quantity * 0.1)); // 10% wholesale discount
        } else if (discountCardNumber != null) {
            DiscountCard card = discountCardRepository.getByNumber(discountCardNumber);
            discount = product.getPrice().multiply(BigDecimal.valueOf(quantity))
                    .multiply(BigDecimal.valueOf(card.getDiscountPercentage()).divide(BigDecimal.valueOf(100)));
        }
        return discount;
    }
}