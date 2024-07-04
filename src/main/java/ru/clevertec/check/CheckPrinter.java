package check;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckPrinter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm:ss");

    public void printToConsole(Check check) {
        System.out.println(formatCheck(check));
    }

    public void saveToCSV(Check check, String filePath) {
        saveToCSV(check, filePath, null);
    }

    public void saveToCSV(Check check, String filePath, String errorMessage) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            if (errorMessage != null) {
                writer.println(errorMessage);
            } else {
                writer.println(formatCheck(check));
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private String formatCheck(Check check) {
        StringBuilder sb = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();

        sb.append("Date;Time\n");
        sb.append(now.format(FORMATTER)).append("\n\n");

        sb.append("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
        for (Check.CheckItem item : check.getItems()) {
            sb.append(String.format("%d;%s;%.2f$;%.2f$;%.2f$\n",
                    item.getQuantity(),
                    item.getProduct().getDescription(),
                    item.getProduct().getPrice(),
                    item.getDiscount(),
                    item.getTotal()));
        }

        sb.append("\n");
        if (check.getDiscountCard() != null) {
            sb.append("DISCOUNT CARD;DISCOUNT PERCENTAGE\n");
            sb.append(String.format("%d;%d%%\n\n", 
                check.getDiscountCard().getNumber(), 
                check.getDiscountCard().getDiscountPercentage()));
        }

        sb.append("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
        sb.append(String.format("%.2f$;%.2f$;%.2f$", 
            check.getTotal(), 
            check.getDiscount(), 
            check.getTotalWithDiscount()));

        return sb.toString();
    }
}