# Check Generator Application

This console application generates a receipt for a store based on input parameters.

## Requirements

- Java 21
- CSV files: `products.csv` and `discountCards.csv` in `./src/main/resources/`

## How to Run

Run the application with the following command format:

```java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java <items> [discountCard=<card_number>] balanceDebitCard=<balance>```

Where:
- `<items>` are in the format `id-quantity` (e.g., `3-1 2-5 5-1`)
- `<card_number>` is an optional 4-digit discount card number
- `<balance>` is the balance on the debit card (can be negative)

Example:

```java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100```

The application will generate a `result.csv` file in the project root directory and display the receipt in the console.

## Features

- Supports multiple items with different quantities
- Applies discount cards if provided
- Applies wholesale discount (10%) for eligible products when quantity is 5 or more
- Handles various exceptions (e.g., insufficient funds, invalid input)

## File Formats

### products.csv
Contains product information: id, description, price, quantity in stock, wholesale product flag

### discountCards.csv
Contains discount card information: id, number, discount percentage

## Output

The application generates a CSV file (`result.csv`) with the following information:
- Date and Time of the transaction
- List of purchased items (quantity, description, price, total, discount)
- Total price
- Discount card information (if applicable)
- Total discount
- Final total with discount

## Error Handling

In case of errors, the application will display an error message in the console and create a `result.csv` file with the error information.

## Note

This application is a test task and should not be used in production environments without further testing and refinement.