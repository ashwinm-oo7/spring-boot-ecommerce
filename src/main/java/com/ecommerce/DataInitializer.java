package com.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.repo.CategoryRepo;
import com.ecommerce.repo.ProductRepo;

import java.util.Scanner;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private CategoryRepo categoryRepository;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("Welcome to the Product and Category Management System");

        while (!exit) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Create Product");
            System.out.println("2. View Products");
            System.out.println("3. View Product By ID"); 
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Exit");


            System.out.print("Enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (choice) {
            case 1 -> createProduct(scanner);
            case 2 -> viewProducts();
            case 3 -> viewProductById(scanner); 
            case 4 -> updateProduct(scanner);
            case 5 -> deleteProduct(scanner);
            case 6 -> exit = true;

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private void createProduct(Scanner scanner) {
        System.out.println("\n--- Create Product ---");

        System.out.print("Enter category ID (or 0 to create a new category): ");
        Long categoryId = Long.parseLong(scanner.nextLine());
        Category category;

        if (categoryId == 0) {
            System.out.print("Enter new category name: ");
            String categoryName = scanner.nextLine();
            category = new Category();
            category.setName(categoryName);
            category = categoryRepository.save(category);
            System.out.println("New category created with ID: " + category.getId());
        } else {
        	try {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID " + categoryId));
            System.out.println("Category found: " + category.getName());
            } catch (CategoryNotFoundException e) {
                System.out.println(e.getMessage());
                return; 
            }

        }

        Product product = new Product();
        System.out.print("Enter product name: ");
        product.setName(scanner.nextLine());

        System.out.print("Enter product description: ");
        product.setDescription(scanner.nextLine());

        System.out.print("Enter product price: ");
        product.setPrice(Double.parseDouble(scanner.nextLine()));

        System.out.print("Enter product quantity: ");
        product.setQuantity(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter product status (e.g., Available): ");
        product.setStatus(scanner.nextLine());

        product.setCategory(category);
        productRepository.save(product);

        System.out.println("Product created successfully!");
    }

private void viewProducts() {
    System.out.println("\n--- View Products ---");

    try {
        Iterable<Product> products = productRepository.findAll();

        if (!products.iterator().hasNext()) {
            System.out.println("No products found.");
            return;
        }

        for (Product product : products) {
            System.out.println("Product ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println("Status: " + product.getStatus());
            System.out.println("Category: " + (product.getCategory() != null ? product.getCategory().getName() : "No Category"));
            System.out.println("-----------------------------------");
        }
    } catch (Exception e) {
        System.out.println("An error occurred while retrieving products: " + e.getMessage());
    }
}

private void viewProductById(Scanner scanner) {
    System.out.println("\n--- View Product By ID ---");

    try {
        System.out.print("Enter Product ID: ");
        Long productId = Long.parseLong(scanner.nextLine());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Display product details
        System.out.println("Product ID: " + product.getId());
        System.out.println("Name: " + product.getName());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Price: " + product.getPrice());
        System.out.println("Quantity: " + product.getQuantity());
        System.out.println("Status: " + product.getStatus());
        System.out.println("Category: " + (product.getCategory() != null ? product.getCategory().getName() : "No Category"));

    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid numeric ID.");
    } catch (RuntimeException e) {
        System.out.println(e.getMessage());
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
    }
}


    private void updateProduct(Scanner scanner) {
        System.out.println("\n--- Update Product ---");

        System.out.print("Enter product ID to update: ");
        Long productId = Long.parseLong(scanner.nextLine());
        Product product;
        try {
        product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID " + productId));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.print("Enter new product name (current: " + product.getName() + "): ");
        String newName = scanner.nextLine();
        product.setName(newName.isBlank() ? product.getName() : newName);

        System.out.print("Enter new product description (current: " + product.getDescription() + "): ");
        String newDescription = scanner.nextLine();
        product.setDescription(newDescription.isBlank() ? product.getDescription() : newDescription);

        System.out.print("Enter new product price (current: " + product.getPrice() + "): ");
        String newPrice = scanner.nextLine();
        product.setPrice(newPrice.isBlank() ? product.getPrice() : Double.parseDouble(newPrice));

        System.out.print("Enter new product quantity (current: " + product.getQuantity() + "): ");
        String newQuantity = scanner.nextLine();
        product.setQuantity(newQuantity.isBlank() ? product.getQuantity() : Integer.parseInt(newQuantity));

        System.out.print("Enter new product status (current: " + product.getStatus() + "): ");
        String newStatus = scanner.nextLine();
        product.setStatus(newStatus.isBlank() ? product.getStatus() : newStatus);

        productRepository.save(product);
        System.out.println("Product updated successfully!");
    }

    private void deleteProduct(Scanner scanner) {
        System.out.println("\n--- Delete Product ---");

        System.out.print("Enter product ID to delete: ");
        Long productId = Long.parseLong(scanner.nextLine());
        try {
	        if (productRepository.existsById(productId)) {
	            productRepository.deleteById(productId);
	            System.out.println("Product with ID " + productId + " deleted successfully!");
	        } else {
	            throw new ProductNotFoundException("Product not found with ID " + productId);
	        }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    // Custom Exception Class
    public static class CategoryNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public CategoryNotFoundException(String message) {
            super(message);
        }
    }
    public class ProductNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ProductNotFoundException(String message) {
            super(message);
        }
    }

}
