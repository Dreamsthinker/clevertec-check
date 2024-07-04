package check;

public interface DiscountCardRepository {
    DiscountCard getByNumber(int number);
}