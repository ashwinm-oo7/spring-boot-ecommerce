# Spring Boot E-Commerce Application

This is a Spring Boot-based e-commerce application that supports **Category** and **Product** CRUD operations using **JPA** and **Hibernate**. The application uses **MySQL** as the relational database.

## Table of Contents
- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [API Endpoints](#api-endpoints)
  - [Category CRUD APIs](#category-crud-apis)
  - [Product CRUD APIs](#product-crud-apis)
- [Database Configuration](#database-configuration)
- [Annotations Used](#annotations-used)

## Project Overview

This project provides an e-commerce system where users can manage categories and products. It supports CRUD operations for both entities, with each product linked to a category using a **one-to-many** relationship. The backend is built using **Spring Boot**, with **JPA** and **Hibernate** integrated for database interaction.

The main functionalities are:

- **Category Management**: Create, Read, Update, Delete (CRUD) categories.
- **Product Management**: Create, Read, Update, Delete (CRUD) products, including linking products to categories.
---

## Technologies Used

- **Spring Boot**: Main framework for the backend
- **JPA & Hibernate**: Used for object-relational mapping (ORM)
- **MySQL**: Relational database for persistence
- **Spring Data JPA**: To interact with the database
- **RestController**: To expose REST APIs
- **Maven**: Dependency management tool.
- **REST API**: Exposed using Spring's `@RestController`.

## API Endpoints

### Category API Endpoints

1. **GET** `/api/categories?page={pageNumber}`
   - Fetch all categories with pagination.
   - Example: `GET http://localhost:8080/api/categories?page=3`

2. **POST** `/api/categories`
   - Create a new category.
   - Request Body:
     ```json
     {
       "name": "Electronics"
     }
     ```

3. **GET** `/api/categories/{id}`
   - Get a category by its ID.
   - Example: `GET http://localhost:8080/api/categories/1`

4. **PUT** `/api/categories/{id}`
   - Update a category by its ID.
   - Request Body:
     ```json
     {
       "name": "Updated Category"
     }
     ```

5. **DELETE** `/api/categories/{id}`
   - Delete a category by its ID.
   - Example: `DELETE http://localhost:8080/api/categories/1`

---

### Product API Endpoints

1. **GET** `/api/products?page={pageNumber}`
   - Fetch all products with pagination.
   - Example: `GET http://localhost:8080/api/products?page=2`

2. **POST** `/api/products`
   - Create a new product.
   - Request Body:
     ```json
     {
       "name": "Smartphone",
       "description": "Latest model",
       "price": 299.99,
       "quantity": 50,
       "status": "Available",
       "category": {
             "id": 10
              }
     }
     ```

3. **GET** `/api/products/{id}`
   - Get a product by its ID, along with its category details.
   - Example: `GET http://localhost:8080/api/products/1`

4. **PUT** `/api/products/{id}`
   - Update a product by its ID.
   - Request Body:
     ```json
     {
       "name": "Updated Smartphone",
       "description": "Updated model",
       "price": 259.99,
       "quantity": 40,
       "status": "Out of stock",
       "categoryId": 1
     }
     ```

5. **DELETE** `/api/products/{id}`
   - Delete a product by its ID.
   - Example: `DELETE http://localhost:8080/api/products/1`

---

## Annotations Used

### In Controller

- `@RestController`: Marks the class as a REST controller, capable of handling HTTP requests and returning responses in JSON format.
- `@RequestMapping`: Used to map HTTP requests to handler methods of MVC and REST controllers.
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: These are shorthand annotations for mapping HTTP GET, POST, PUT, DELETE requests respectively.

Example:
```java
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @GetMapping
    public List<Category> getAllCategories(Pageable pageable) {
        return categoryService.findAllCategories(pageable);
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
}

```
## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/ashwinm-oo7/spring-boot-ecommerce.git
   cd spring-boot-ecommerce





