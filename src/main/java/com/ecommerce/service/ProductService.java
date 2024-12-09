package com.ecommerce.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.repo.CategoryRepo;
import com.ecommerce.repo.ProductRepo;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private CategoryRepo categoryRepo;
    
    public class ResourceNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L; 

        public ResourceNotFoundException(String message) {
            super(message);
        }

        public ResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }



    public Page<ProductDTO> getAllProducts(int page, int size) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
        return new PageImpl<>(
            products.getContent().stream().map(this::mapToDTO).collect(Collectors.toList()),
            products.getPageable(),
            products.getTotalElements()
        );
    }

    public ProductDTO createProduct(Product product) {
        Long categoryId = product.getCategory().getId();
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return mapToDTO(product);
    }

    public ProductDTO updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());

        Long categoryId = updatedProduct.getCategory().getId();
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
        existingProduct.setCategory(category);

        Product savedProduct = productRepository.save(existingProduct);
        return mapToDTO(savedProduct);
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true; 
        } else {
            return false; 
        }
    }


    private ProductDTO mapToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStatus(product.getStatus());
        productDTO.setQuantity(product.getQuantity());

        ProductDTO.CategoryDTO categoryDTO = new ProductDTO.CategoryDTO();
        categoryDTO.setId(product.getCategory().getId());
        categoryDTO.setName(product.getCategory().getName());
        productDTO.setCategory(categoryDTO);

        return productDTO;
    }
}
