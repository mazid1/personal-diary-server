package com.example.personaldiary.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT new Category (id, title, isDefault)"
            + "FROM Category WHERE isDefault='true'")
    List<Category> getDefaultCategory();

    @Query("SELECT new Category (c.id, c.title, c.isDefault)"
            + "FROM Category c WHERE c.user= (" +
            "SELECT id from User u where u.username = :username)")
    List<Category> getCategoryByUser(@Param("username") String username);
}
