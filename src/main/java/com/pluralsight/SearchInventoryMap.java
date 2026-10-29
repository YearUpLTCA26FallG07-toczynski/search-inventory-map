
package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SearchInventoryMap {

    // Store products using their ID as the map key
    private static Map<Integer, Product> inventory = new HashMap<>();

    public static void main(String[] args) {

        // Load products from the inventory file
        loadInventory();

        Scanner scanner = new Scanner(System.in);
        String searchAgain;

        // Allow the user to search more than once
        do {
            System.out.print("Enter a product name to search: ");
            String searchName = scanner.nextLine().trim();

            boolean found = false;

            // Search the products by name
            for (Product product : inventory.values()) {
                if (product.getName().toLowerCase()
                        .contains(searchName.toLowerCase())) {

                    System.out.println(product);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No products found.");
            }

            // Ask whether the user wants another search
            System.out.print("Do you want to search again? (yes/no): ");
            searchAgain = scanner.nextLine().trim();

        } while (searchAgain.equalsIgnoreCase("yes")
                || searchAgain.equalsIgnoreCase("y"));

        System.out.println("Thank you for using the inventory search!");

        scanner.close();
    }

    // Read the inventory file and load each product into the HashMap
    public static void loadInventory() {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader("inventory.csv"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                // Skip blank lines
                if (line.isEmpty()) {
                    continue;
                }

                // Support either pipe-delimited or comma-delimited rows
                String[] fields = line.split(
                        line.contains("|") ? "\\|" : ",", 3);

                // Skip a header row, if the file has one
                if (fields[0].trim().equalsIgnoreCase("id")) {
                    continue;
                }

                if (fields.length != 3) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(fields[0].trim());
                    String name = fields[1].trim();
                    double price = Double.parseDouble(fields[2].trim());

                    // Create the product and use its ID as the key
                    Product product = new Product(id, name, price);
                    inventory.put(product.getId(), product);

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid row: " + line);
                }
            }

            System.out.println("Inventory loaded: "
                    + inventory.size() + " products.");

        } catch (IOException e) {
            System.out.println("Error reading inventory.csv: "
                    + e.getMessage());
        }
    }
}
