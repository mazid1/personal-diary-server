package com.example.personaldiary.category;

import com.example.personaldiary.user.User;
import com.example.personaldiary.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CategoryResource {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/categories")
    public List<Category> getCategories() {
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Category> defaultCategories = categoryRepository.getDefaultCategory();
        List<Category> userCategories = categoryRepository.getCategoryByUser(ud.getUsername());
        List<Category> allCategories = new ArrayList<>(defaultCategories);
        allCategories.addAll(userCategories);

        return allCategories;
    }

    @GetMapping("/api/category/{id}")
    public Category getCategoryById(@PathVariable long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (!category.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found (id-" + id + ")");
        }

        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!category.get().getUser().getUsername().equals(ud.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to get others data");
        }

        return category.get();
    }

    @DeleteMapping("/api/category/{id}")
    public void deleteCategoryById(@PathVariable long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (!category.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found (id-" + id + ")");
        }
        if(category.get().getIsDefault()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Default Categories can not be deleted");
        }

        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!category.get().getUser().getUsername().equals(ud.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not delete others data");
        }

        categoryRepository.delete(category.get());
    }

    @PostMapping("/api/category")
    public ResponseEntity<Object> createCategory(@RequestBody Category category) {
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByUsername(ud.getUsername());
        category.setUser(currentUser);
        category.setIsDefault(false);

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
        if(categoryOptional.get().getUser() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!categoryOptional.get().getUser().getUsername().equals(ud.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        category.setId(id);
        category.setUser(categoryOptional.get().getUser());
        category.setIsDefault(false);
        categoryRepository.save(category);

        return ResponseEntity.noContent().build();
    }
}
