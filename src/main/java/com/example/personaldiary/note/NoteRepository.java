package com.example.personaldiary.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT new Note (n.id, n.title, n.description, n.date, n.category, n.user)"
            + "FROM Note n WHERE n.user= (" +
            "SELECT id from User u where u.username = :username)")
    List<Note> getNotesByUser(@Param("username") String username);
}
