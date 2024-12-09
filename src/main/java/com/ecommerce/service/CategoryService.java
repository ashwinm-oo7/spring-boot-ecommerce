package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Category;
import com.ecommerce.repo.CategoryRepo;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepository;

    public Page<Category> getAllCategories(int page, int size) {
    	Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable);	
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = getCategoryById(id);
        category.setName(updatedCategory.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
