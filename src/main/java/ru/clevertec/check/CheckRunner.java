package check;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CheckRunner {
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                throw new InvalidInputException("Insufficient arguments");
            }

            List<ProductItem> items = new ArrayList<>();
            Integer discountCardNumber = null;
            BigDecimal balance = null;

            for (String arg : args) {

                if (arg.startsWith("discountCard")) {
                    try {
                        String cardNumber = arg.split("=")[1];
                        if (!cardNumber.matches("\\d{4}")) throw new InvalidInputException("Invalid discount card format");
                        discountCardNumber = Integer.parseInt(cardNumber);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new InvalidInputException("Invalid discount card format");
                    }

                } else if (arg.startsWith("balanceDebitCard")) {
                    try {
                        balance = new BigDecimal(arg.split("=")[1]);
                        balance = balance.setScale(2, RoundingMode.HALF_UP);
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                        throw new InvalidInputException("Invalid balance format");
                    }

                } else {
                    try {
                        String[] parts = arg.split("-");
                        if (parts.length != 2) throw new InvalidInputException("Invalid product format");
                        int productId = Integer.parseInt(parts[0]);
                        int quantity = Integer.parseInt(parts[1]);
                        if (quantity <= 0) throw new InvalidInputException("Invalid product quantity");
                        items.add(new ProductItem(productId, quantity));
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                        throw new InvalidInputException("Invalid product format");
                    }
                }
            }

            if (balance == null) {
                throw new InvalidInputException("Balance not specified");
            }

            if (items.isEmpty()) {
                throw new InvalidInputException("No products specified");
            }

            ProductRepository productRepository = new FileProductRepository("./src/main/resources/products.csv");
            DiscountCardRepository discountCardRepository = new FileDiscountCardRepository("./src/main/resources/discountCards.csv");
            CheckService checkService = new CheckService(productRepository, discountCardRepository);

            Check check = checkService.createCheck(items, discountCardNumber, balance);
            CheckPrinter printer = new CheckPrinter();
            printer.printToConsole(check);
            printer.saveToCSV(check, "result.csv");

        } catch (InvalidInputException | ProductNotFoundException e) {
            System.out.println("BAD REQUEST\n" + e.getMessage());
            saveErrorToCSV("ERROR\nBAD REQUEST");
        } catch (InsufficientFundsException e) {
            System.out.println("NOT ENOUGH MONEY\n" + e.getMessage());
            saveErrorToCSV("ERROR\nNOT ENOUGH MONEY");
        } catch (Exception e) {
            System.out.println("INTERNAL SERVER ERROR\n" + e.getMessage());
            saveErrorToCSV("ERROR\nINTERNAL SERVER ERROR");
        }
    }

    private static void saveErrorToCSV(String errorMessage) {
        try {
            CheckPrinter printer = new CheckPrinter();
            printer.saveToCSV(null, "result.csv", errorMessage);
        } catch (Exception e) {
            System.out.println("Error saving error message to CSV: " + e.getMessage());
        }
    }
}