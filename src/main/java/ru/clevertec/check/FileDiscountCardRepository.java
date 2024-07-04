package check;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileDiscountCardRepository implements DiscountCardRepository {
    private Map<Integer, DiscountCard> discountCards = new HashMap<>();

    public FileDiscountCardRepository(String filePath) {
        loadDiscountCardsFromFile(filePath);
    }

    @Override
    public DiscountCard getByNumber(int number) {
        DiscountCard card = discountCards.get(number);
        if (card == null) {
            // Если карта не найдена, возвращаем карту с 2% скидки
            return new DiscountCard(number, 2);
        }
        return card;
    }

    private void loadDiscountCardsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int number = Integer.parseInt(parts[0]);
                int discountPercentage = Integer.parseInt(parts[1]);
                discountCards.put(number, new DiscountCard(number, discountPercentage));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }
}