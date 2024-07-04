package check;

public class DiscountCard {
    private int number;
    private int discountPercentage;

    public DiscountCard(int number, int discountPercentage) {
        this.number = number;
        this.discountPercentage = discountPercentage;
    }

    // Геттеры
    public int getNumber() { return number; }
    public int getDiscountPercentage() { return discountPercentage; }
}