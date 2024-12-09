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
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("Welcome to the Product and Category Management System");

        while (!exit) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Create Product");
            System.out.println("2. View Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createProduct(scanner);
                case 2 -> viewProducts();
                case 3 -> updateProduct(scanner);
                case 4 -> deleteProduct(scanner);
                case 5 -> exit = true;
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
        } else {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID " + categoryId));
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
        Iterable<Product> products = productRepository.findAll();

        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void updateProduct(Scanner scanner) {
        System.out.println("\n--- Update Product ---");

        System.out.print("Enter product ID to update: ");
        Long productId = Long.parseLong(scanner.nextLine());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID " + productId));

        System.out.print("Enter new product name (current: " + product.getName() + "): ");
        product.setName(scanner.nextLine());

        System.out.print("Enter new product description (current: " + product.getDescription() + "): ");
        product.setDescription(scanner.nextLine());

        System.out.print("Enter new product price (current: " + product.getPrice() + "): ");
        product.setPrice(Double.parseDouble(scanner.nextLine()));

        System.out.print("Enter new product quantity (current: " + product.getQuantity() + "): ");
        product.setQuantity(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter new product status (current: " + product.getStatus() + "): ");
        product.setStatus(scanner.nextLine());

        productRepository.save(product);
        System.out.println("Product updated successfully!");
    }

    private void deleteProduct(Scanner scanner) {
        System.out.println("\n--- Delete Product ---");

        System.out.print("Enter product ID to delete: ");
        Long productId = Long.parseLong(scanner.nextLine());

        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Product not found with ID " + productId);
        }
    }
}
