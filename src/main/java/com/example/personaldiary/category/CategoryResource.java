package com.example.personaldiary.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class CategoryResource {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/api/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/api/category/{id}")
    public Category getCategoryById(@PathVariable long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (!category.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found (id-" + id + ")");
        }

        return category.get();
    }

    @DeleteMapping("/api/category/{id}")
    public void deleteCategoryById(@PathVariable long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (!category.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found (id-" + id + ")");
        }

        categoryRepository.delete(category.get());
    }

    @PostMapping("/api/category")
    public ResponseEntity<Object> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryRepository.save(category);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCategory.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/api/category/{id}")
    public ResponseEntity<Object> updateCategory(@RequestBody Category category, @PathVariable long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (!categoryOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        category.setId(id);
        categoryRepository.save(category);

        return ResponseEntity.noContent().build();
    }
}
