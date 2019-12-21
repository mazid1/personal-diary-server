package com.example.personaldiary.category;

import com.example.personaldiary.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Boolean isDefault;

    @ManyToOne
    private User user;

    public Category() {
        super();
    }

    public Category(Long id, String title, Boolean isDefault) {
        super();
        this.id = id;
        this.title = title;
        this.isDefault = isDefault;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
