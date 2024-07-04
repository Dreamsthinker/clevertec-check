package check;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FileProductRepository implements ProductRepository {
    private Map<Integer, Product> products = new HashMap<>();

    public FileProductRepository(String filePath) {
        loadProductsFromFile(filePath);
    }

    @Override
    public Product getById(int id) {
        return products.get(id);
    }

    private void loadProductsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                String description = parts[1];
                BigDecimal price = new BigDecimal(parts[2]);
                boolean isWholesale = Boolean.parseBoolean(parts[3]);
                products.put(id, new Product(id, description, price, isWholesale));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }
}