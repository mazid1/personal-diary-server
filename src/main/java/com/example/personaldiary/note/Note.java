package com.example.personaldiary.note;

import com.example.personaldiary.category.Category;
import com.example.personaldiary.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Note {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Date date;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    public Note() {
        super();
    }

    public Note(Long id, String title, String description, Date date, Category category, User user) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.category = category;
        this.user = user;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
